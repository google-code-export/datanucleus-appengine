# Introduction #

This page contains instructions for upgrading from version 1.X of the plugin to version 2.X.


# Installation #
We recommend you make a fresh copy of the SDK, install the `DataNucleus` plugin there, and then develop against this copy of the SDK. Here is the sequence of commands you can use, assuming your sdk lives in `<sdk_root>`:

Make a copy of the SDK:
<br>
<pre><code>cp -r &lt;sdk_root&gt; &lt;new_sdk_root&gt;<br>
</code></pre>

Delete all components of the v1 plugin:<br>
<pre><code>rm -rf &lt;new_sdk_root&gt;/lib/user/orm/*<br>
rm -rf &lt;new_sdk_root&gt;/lib/tools/orm/*<br>
rm -rf &lt;new_sdk_root&gt;/src/orm/*<br>
</code></pre>

Unzip the v2 zip archive into <code>&lt;new_sdk_root&gt;</code>:<br>
<pre><code>cd &lt;new_sdk_root&gt;<br>
unzip datanucleus-appengine-2.0.0-final-dist.zip<br>
</code></pre>

<h1>Configuration</h1>
We have removed the App Engine specific JDO and JPA providers in V2, so you'll need to edit your JDO/JPA config files as follows:<br>
<br>
In jdoconfig.xml, the value of <code>javax.jdo.PersistenceManagerFactoryClass</code>
should now be <code>org.datanucleus.api.jdo.JDOPersistenceManagerFactory</code>. See <a href='http://code.google.com/p/datanucleus-appengine/source/browse/trunk/tests/META-INF/jdoconfig.xml'>http://code.google.com/p/datanucleus-appengine/source/browse/trunk/tests/META-INF/jdoconfig.xml</a> for an example.<br>
<br>
In persistence.xml, the value of <code>provider</code> should now be <code>org.datanucleus.api.jpa.PersistenceProviderImpl</code>. See <a href='http://code.google.com/p/datanucleus-appengine/source/browse/trunk/tests/META-INF/persistence.xml'>http://code.google.com/p/datanucleus-appengine/source/browse/trunk/tests/META-INF/persistence.xml</a> for an example.<br>
<br>
<h2>Configuration With Ant</h2>
The enhancement Ant task that ships with the SDK does not work with v2 of the <code>DataNucleus</code> plugin, so if you're using Ant you'll need to use the <code>DataNucleus</code> enhancement task directly in your <code>build.xml</code>:<br>
<pre><code><br>
&lt;path id="enhancer.classpath"&gt;<br>
    &lt;fileset dir="${sdk.dir}/lib/opt/tools/datanucleus/v2"&gt;<br>
        &lt;include name="**/datanucleus-enhancer*.jar"/&gt;<br>
        &lt;include name="**/asm-*.jar"/&gt;<br>
    &lt;/fileset&gt;<br>
&lt;/path&gt;<br>
<br>
&lt;!-- This target should replace your existing datanucleusenhance target --&gt;<br>
&lt;target name="datanucleusenhance" depends="compile" description="DataNucleus enhancement"&gt;<br>
    &lt;taskdef name="datanucleusenhancer" classpathref="enhancer.classpath" <br>
                classname="org.datanucleus.enhancer.tools.EnhancerTask" /&gt;<br>
<br>
    &lt;datanucleusenhancer failonerror="true"&gt;<br>
        &lt;fileset dir="war/WEB-INF/classes"&gt;<br>
            &lt;include name="**/*.class"/&gt;<br>
        &lt;/fileset&gt;<br>
        &lt;classpath&gt;<br>
            &lt;path refid="enhancer.classpath"/&gt;<br>
            &lt;path refid="project.classpath"/&gt;<br>
        &lt;/classpath&gt;<br>
    &lt;/datanucleusenhancer&gt;<br>
&lt;/target&gt;<br>
</code></pre>
Also, if you have an ant task that copies jars out of the sdk and into your WEB-INF/lib directory, you'll need to adjust it to copy the <code>DataNucleus</code> jars from their new location:<br>
<pre><code>&lt;target name="copyjars"<br>
      description="Copies the App Engine JARs to the WAR."&gt;<br>
    &lt;mkdir dir="war/WEB-INF/lib" /&gt;<br>
    &lt;copy todir="war/WEB-INF/lib" flatten="true"&gt;<br>
        &lt;fileset dir="${sdk.dir}/lib/user"&gt;<br>
            &lt;include name="**/*.jar" /&gt;<br>
        &lt;/fileset&gt;<br>
        &lt;fileset dir="${sdk.dir}/lib/opt/user"&gt;<br>
            &lt;include name="**/*.jar" /&gt;<br>
        &lt;/fileset&gt;<br>
    &lt;/copy&gt;<br>
&lt;/target&gt;<br>
</code></pre>
You can find an example of a working ant config in <code>&lt;new_sdk_root&gt;/demos/helloorm/build.xml</code>

<h2>Configuration With Eclipse</h2>
(The following instructions are for Indigo and assume you already have the <a href='http://code.google.com/appengine/docs/java/tools/eclipse.html'>Google Eclipse Plugin</a> installed)<br>
<ul><li>Disable the v1 <code>DataNucleus</code> enhancement by going to Project --> Properties --> Google --> App Engine --> ORM and then removing any patterns that are listed. Click "OK."<br>
</li><li>Configure your project to use the SDK containing the new version of the <code>DataNucleus</code> App Engine plugin by going to Project --> Properties --> Google --> App Engine. Add the SDK by clicking "Configure SDKs" and then select that SDK for your project (Use specific SDK).<br>
</li><li>Add the JDO or JPA API jar in <code>&lt;new_sdk_root&gt;</code>/lib/opt/user/datanucleus/v2 to the Build Path for your project: Right-click on your project, choose Build Path --> Configure Build Path, then click "Add External JARs." For JDO, add jdo-api-3.0.jar. For JPA add geronimo-jpa_2.0_spec-1.0.jar.<br>
</li><li>Copy the contents of <code>&lt;new_sdk_root&gt;</code>/lib/opt/user/datanucleus/v2 to your project's war/WEB-INF/lib directory.<br>
</li><li>Install and configure the <code>DataNucleus</code> Eclipse plugin. Instructions are available at <a href='http://www.datanucleus.org/products/accessplatform/guides/eclipse/index.html'>http://www.datanucleus.org/products/accessplatform/guides/eclipse/index.html</a>.<br>
<ul><li>Follow the instructions under "Plugin Installation"<br>
</li><li>Follow the instructions under "Plugin Configuration"<br>
</li><li>Follow the instructions under "Plugin Configuration - General". You will instead want to add all jars in <code>&lt;new_sdk_root&gt;</code>/lib/opt/tools/datanucleus/v2 and make sure the checkbox labeled "Use project classpath when running tools" is <i>not</i> selected.<br>
</li><li>Follow the instructions under "Plugin configuration - Enhancer"<br>
</li><li><i>Skip</i> the instructions under "Plugin configuration - SchemaTool"<br>
</li><li>Follow the instructions under "Enabling DataNucleus support"<br>
</li><li><i>Skip</i> the instructions under "Defining JDO2 Metadata"<br>
</li><li><i>Skip</i> the instructions under "Defining 'persistence.xml'"<br>
</li><li>Follow the instructions under "Enhancing the classes"<br>
</li><li><i>Skip</i> the instructions under "Generating your database schema (RDBMS)"</li></ul></li></ul>

Once you've completed these steps, you should see output from the Enhancer whenever you modify and save one of your model classes.<br>
<br>
<h2>Configuration With IntelliJ</h2>
TODO(maxr)<br>
<br>
<h2>Configuration With Maven</h2>
In terms of dependencies, you need the <b>datanucleus-appengine</b> jar and its dependencies, namely <b>datanucleus-core</b>, <b>datanucleus-api-jdo</b> or <b>datanucleus-api-jpa</b> depending if using JDO or JPA respectively, as well as the JDO API jar (<b>jdo-api-3.0.jar</b>) or JPA API jar (<b>geronimo-jpa_2.0_spec-1.0.jar</b>). Like this for JDO<br>
<pre><code>    &lt;dependencies&gt;<br>
        &lt;dependency&gt;<br>
            &lt;groupId&gt;com.google.appengine.orm&lt;/groupId&gt;<br>
            &lt;artifactId&gt;datanucleus-appengine&lt;/artifactId&gt;<br>
            &lt;version&gt;2.0.1.1&lt;/version&gt;<br>
        &lt;/dependency&gt;<br>
        &lt;dependency&gt;<br>
            &lt;groupId&gt;org.datanucleus&lt;/groupId&gt;<br>
            &lt;artifactId&gt;datanucleus-core&lt;/artifactId&gt;<br>
            &lt;version&gt;[3.0, 3.0.99)&lt;/version&gt;<br>
        &lt;/dependency&gt;<br>
        &lt;dependency&gt;<br>
            &lt;groupId&gt;org.datanucleus&lt;/groupId&gt;<br>
            &lt;artifactId&gt;datanucleus-api-jdo&lt;/artifactId&gt;<br>
            &lt;version&gt;[3.0, 3.0.99)&lt;/version&gt;<br>
            &lt;optional&gt;true&lt;/optional&gt;<br>
        &lt;/dependency&gt;<br>
        &lt;dependency&gt;<br>
            &lt;groupId&gt;javax.jdo&lt;/groupId&gt;<br>
            &lt;artifactId&gt;jdo-api&lt;/artifactId&gt;<br>
            &lt;version&gt;[3.0, 4.0)&lt;/version&gt;<br>
        &lt;/dependency&gt;<br>
        ...<br>
    &lt;/dependencies&gt;<br>
</code></pre>
or for JPA<br>
<pre><code>    &lt;dependencies&gt;<br>
        &lt;dependency&gt;<br>
            &lt;groupId&gt;com.google.appengine.orm&lt;/groupId&gt;<br>
            &lt;artifactId&gt;datanucleus-appengine&lt;/artifactId&gt;<br>
            &lt;version&gt;2.0.1.1&lt;/version&gt;<br>
        &lt;/dependency&gt;<br>
        &lt;dependency&gt;<br>
            &lt;groupId&gt;org.datanucleus&lt;/groupId&gt;<br>
            &lt;artifactId&gt;datanucleus-core&lt;/artifactId&gt;<br>
            &lt;version&gt;[3.0, 3.0.99)&lt;/version&gt;<br>
        &lt;/dependency&gt;<br>
        &lt;dependency&gt;<br>
            &lt;groupId&gt;org.datanucleus&lt;/groupId&gt;<br>
            &lt;artifactId&gt;datanucleus-api-jpa&lt;/artifactId&gt;<br>
            &lt;version&gt;[3.0, 3.0.99)&lt;/version&gt;<br>
        &lt;/dependency&gt;<br>
        &lt;dependency&gt;<br>
            &lt;groupId&gt;org.apache.geronimo.specs&lt;/groupId&gt;<br>
            &lt;artifactId&gt;geronimo-jpa_2.0_spec&lt;/artifactId&gt;<br>
            &lt;version&gt;1.0&lt;/version&gt;<br>
        &lt;/dependency&gt;<br>
        &lt;dependency&gt;<br>
            &lt;groupId&gt;javax.jdo&lt;/groupId&gt;<br>
            &lt;artifactId&gt;jdo-api&lt;/artifactId&gt;<br>
            &lt;version&gt;[3.0, 4.0)&lt;/version&gt;<br>
        &lt;/dependency&gt;<br>
        ...<br>
    &lt;/dependencies&gt;<br>
</code></pre>
in addition to any other appengine dependencies such as appengine-api. To provide enhancement you can use the DataNucleus Maven plugin, like this<br>
<br>
<pre><code>    &lt;plugin&gt;<br>
        &lt;groupId&gt;org.datanucleus&lt;/groupId&gt;<br>
        &lt;artifactId&gt;maven-datanucleus-plugin&lt;/artifactId&gt;<br>
        &lt;version&gt;3.0.2&lt;/version&gt;<br>
        &lt;configuration&gt;<br>
            &lt;verbose&gt;true&lt;/verbose&gt;<br>
        &lt;/configuration&gt;<br>
        &lt;executions&gt;<br>
            &lt;execution&gt;<br>
                &lt;phase&gt;process-classes&lt;/phase&gt;<br>
                &lt;goals&gt;<br>
                    &lt;goal&gt;enhance&lt;/goal&gt;<br>
                &lt;/goals&gt;<br>
            &lt;/execution&gt;<br>
        &lt;/executions&gt;<br>
    &lt;/plugin&gt;<br>
</code></pre>
Please refer to the DataNucleus docs for more details <a href='http://www.datanucleus.org/products/accessplatform/enhancer.html#maven2'>http://www.datanucleus.org/products/accessplatform/enhancer.html#maven2</a>


<h2></h2>