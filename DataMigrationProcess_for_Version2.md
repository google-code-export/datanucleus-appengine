# Introduction #

With v2 of the JDO/JPA plugin, to add the necessary flexibility to be able to support unowned relations, and to fix many outstanding problems present in v1, the "storage version" of data was changed. This change was to save the Key(s) of related objects into a property in the Entity that had the relation to them. This property is not present in data persisted using v1 of the plugin. This migration process below generates the necessary property(s) in the Entity(s) to be able to be used thereafter using v2 of the plugin.


# Details #

We make use of the Google Mapper API here, as defined by
http://googleappengine.blogspot.co.uk/2010/07/introducing-mapper-api.html
The first thing we’ll have to do is define a Mapper.

```
package com.google.appengine.datanucleus.migration;

import java.util.Date;
import java.util.logging.Logger;

import org.apache.hadoop.io.NullWritable;

import javax.jdo.JDOHelper;
import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import org.datanucleus.NucleusContext;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.Migrator;
import com.google.appengine.tools.mapreduce.AppEngineMapper;

/**
 * Mapper to convert data for a particular user class to the latest storage version
 * suitable for use with v2 of the DataNucleus AppEngine plugin.
 */
public class MigratorMapper extends AppEngineMapper<Key, Entity, NullWritable, NullWritable> {
    private static final Logger log = Logger.getLogger(NaiveToLowercaseMapper.class.getName());

    public static final String ENTITY_CLASSNAME = "mapreduce.mapper.entityClassName";

    private Class entityClass;

    private Migrator migrator;

    private DatastoreService datastore;

    @Override
    public void taskSetup(Context context) {
        String className = context.getConfiguration().get(ENTITY_CLASSNAME);
        if (className == null) {
            throw new IOException("No mapreduce.mapper.entityClassName specified in configuration");
        }

        // Get the PMF and its NucleusContext
        JDOPersistenceManagerFactory pmf = 
            (JDOPersistenceManagerFactory)JDOHelper.getPersistenceManagerFactory("pmf-name");
        NucleusContext nucCtx = pmf.getNucleusContext();

        // If using JPA, comment out the above and do this
        // Get the EMF and its NucleusContext
        // JPAEntityManagerFactory emf =
        //     (JPAEntityManagerFactory)Persistence.createEntityManagerFactory("persistence-unit-name");
        // NucleusContext nucCtx = emf.getNucleusContext();

        // Get the class of the entity
        this.entityClass = nucCtx.getClassLoaderResolver(null).classForName(className);

        // Create a Migrator to do the work
        this.migrator = new Migrator(nucCtx);

        // Get the datastore service
        this.datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void map(Key key, Entity value, Context context) {
        String className = context.getConfiguration().get(ENTITY_CLASSNAME);
        log.info("Migrating entity with key=" + key + " for class=" + className);

        boolean changed = migrator.migrate(entity, entityClass);
        if (changed) {
            datastore.put(entity);
        }
    }
}
```

Note that the "migrator.migrate" will only migrate Entities that are of that specific class. If they are of subclasses
then you need to call passing in the subclass.

Now let’s add this job to _mapreduce.xml_
```
<configurations>
  <configuration name="Migrator Mapper for class">
    <property>
      <name>mapreduce.map.class</name>
      <value>com.google.appengine.datanucleus.migration.MigratorMapper</value>
    </property>

    <property>
      <name>mapreduce.inputformat.class</name>
      <value>com.google.appengine.tools.mapreduce.DatastoreInputFormat</value>
    </property>

    <property>
      <name human="Entity Kind to Map Over">mapreduce.mapper.inputformat.datastoreinputformat.entitykind</name>
      <value template="optional">Person</value>
    </property>

    <property>
      <name human="Entity Class-name being migrated">mapreduce.mapper.entityClassName</name>
      <value template="optional">mydomain.model.Person</value>
    </property>
  </configuration>
</configurations>
```

So now you are ready to run this migration for this particular class using the Mapper API.