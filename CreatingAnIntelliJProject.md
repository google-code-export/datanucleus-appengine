# How To Create An IntelliJ Project For The DataNucleus App Engine Plugin #
These instructions assume you are using IntelliJ 8.1.

  * Follow the instructions at http://code.google.com/p/datanucleus-appengine/source/checkout to checkout the source from svn.  The remainder of these instructions assume you checked out the source into a directory named "dnae" located in /usr/local/dev.
  * Launch IntelliJ
  * From the File menu select "New Project", choose "Create Java project from existing sources" from the next screen, and then click "Next."
  * In the next window, set the "Name" field to be "dnae" and the "Project files location" field to be "/usr/local/dev/dnae".   The value of the "Project storage format" drop-down should be ".ipr (file based)"
  * Click "Next."
  * IntelliJ will find 4 directories with source files.  Clear the checkbox for the one named "/usr/local/dev/dnae/testing/library/src"  Leave the other 3 selected and click "Next."
  * IntelliJ will create a library named "lib."  You don't want this.  Clear the checkbox next to the library named "lib."
  * IntelliJ will create two modules - one named "Dnae" and one named "Helloorm."  Leave both of these checked and click "Next."
  * IntelliJ will detect a total of 5 facets for these modules.  Clear the checkboxes next to all of them and click "Finish."
  * You should now have a project with two modules listed on the left: "Dnae" and "Helloorm."  The Google App Engine SDK supports java 5 so, in order to ensure that we don't accidentally introduce and java 6 dependencies we need to make sure we're compiling against a java 5 sdk.  To do this, from the File menu select "Project Structure," and then select "Project" from the list of options on the left.
  * Under "Project SDK," choose a Java 5 SDK.  Under "Project language level" make sure "5.0" is selected.
  * Select "Modules" from the list of options on the left and then select "Dnae" from the list that appears just to the right.
  * Click on the "Dependencies" tab, click the "Add" button, and then choose "Single-Entry Module Library" from the list that appears.
  * In the window that appears, navigate to /usr/local/dev/dnae/lib and select the following jars:
    * appengine-api-stubs.jar
    * appengine-api.jar
    * appengine-local-runtime.jar
    * asm-3.1.jar
    * datanucleus-core-1.1.0.jar
    * datanucleus-jpa-1.1.0.jar
    * easymock.jar
    * geronimo-jpa\_3.0\_spec-1.1.1.jar
    * jdo2-api-2.3-SNAPSHOT.jar
    * junit.jar
    * log4j-1.2.9.jar
  * Do not include datanucleus-enhancer-1.1.4.jar.  We are leaving this out intentionally.
  * Click "OK"
  * Now select "Helloorm."  Click on the "Dependencies" tab, click the "Add" button, and then choose "Single-Entry Module Library" from the list that appears.
  * In the window that appears, navigate to /usr/local/dev/dnae/lib and select the following jars:
    * geronimo-jpa\_3.0\_spec-1.1.1.jar
    * geronimo-servlet\_2.5\_spec-1.2.jar
    * jdo2-api-2.3-SNAPSHOT.jar
  * Click "OK"
  * Click "OK" again
  * Now we need to tell the compiler about a couple of file types that it doesn't consider by default.  Go to the "File" menu and select "Settings."  Then, on the left, select "Compiler."
  * In the "Resource patterns" field on the left you'll need to append the following to the existing value:
```
 ;?*.MF;javax.*
```
  * Click "OK"
  * You should now be able to build the project:  Go to the "Build" menu and select "Make Project."
  * Now you're ready to run the test suite.  In order to run unit tests inside IntelliJ you need to set a couple of jvm flags.  These flags are listed in dnae/test/README but we'll walk through the process of adding them in IntelliJ.
  * Go to the "Run" menu, select "Edit Configurations," and then click the "Edit Defaults" button in the lower left.
  * Select "JUnit" from the list on the left and then set the "VM parameters" field to the following:
```
-javaagent:lib/datanucleus-enhancer-1.1.4.jar=org.datanucleus.test -Dappengine.orm.disable.duplicate.emf.exception -Dappengine.orm.disable.duplicate.pmf.exception
```
  * Click "OK"
  * Click "OK" again
  * You're now ready to run the test suite.  Open up org.datanucleus.store.appengine.AllTests and from the "Run" menu select "Run."  Click the "Run" button in the lower right corner of the window that pops up.  All tests should pass.  If they don't I give you permission to yell at me for bad hygiene.