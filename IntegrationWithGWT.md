# Introduction #

You can integrate JDO/JPA with GWT simply by observing detachability of classes, and only pass detached objects across to GWT.


# Details #

If you refer to [CloudGlow Blog](http://blog.cloudglow.com/2010/04/google-app-engine-arraylist-jdo-object.html) you see the results of using an early version of the plugin. Note that the user did not explicitly detach the object graph, and consequently any collection/map field will have been replaced by a wrapper to intercept updates. If you just take that a step further, for JDO
```
// Make sure you mark all classes with @Detachable (or equivalent in XML)


// Set our fetch groups so we retrieve all fields we want in GWT
pm.getFetchPlan().setGroup("all");

// Perform the query (replace with your own preference)
Query q = pm.newQuery(MyClass.class);
List<MyClass> myobjects = (List<MyClass>)q.execute();

// Detach copies of the query results
List<MyClass> detachedList = pm.detachCopyAll(myobjects);
// Now you can use "detachedList" for passing across to GWT via RPC.
```
Obviously this is just a simple example, and you could also take advantage of detachAllOnCommit functionality to avoid the explicit detach call (see the JDO docs for DataNucleus).



And the same for JPA
```
// Set our fetch groups so we retrieve all fields we want in GWT
((JPAEntityManager)em).getFetchPlan().setGroup("all");

// Perform the query (replace with your own preference)
TypedQuery<MyClass> q = em.createQuery("SELECT p FROM MyClass p", MyClass.class);
List<MyClass> myobjects = q.getResultList();

tx.commit();

// myobjects are now detached (when using TRANSACTION PersistenceContext).
```