# How to update the SDK with a new plugin version #

**Warning**: These instructions only apply if you are only updating the DataNucleus App Engine plugin and not your entire SDK.  If you are updating the entire SDK these instructions do not apply.

  * **MAKE A COPY OF YOUR EXISTING SDK DIRECTORY**
  * Download the version of the plugin you want to install from http://code.google.com/p/datanucleus-appengine/downloads/list
  * Move the zip archive you just downloaded to the root of your SDK directory.
  * Unzip the archive in the root of your SDK directory.  If you are running OS X be sure to unzip the archive using the command line because it seems if you just use the Finder it will replace the contents of the SDK directory rather than overlay.
  * `<`sdk\_root`>`/lib/user/orm will now contain datanucleus-appengine-`<`old\_version`>`.jar and datanucleus-appengine-`<`new\_version`>`.jar.  Delete datanucleus-appengine-`<`old\_version`>`.jar.
  * If the plugin contains a DataNucleus upgrade you will also need to delete the old DataNucleus jars.  For example, if the version you're installing upgrades DataNucleus from 1.1.0 to 1.1.4 you now have both datanucleus-core-1.1.0.jar and datanucleus-core-1.1.4.jar in `<`sdk\_root`>`/lib/user/orm.  Delete the old jar (in this case datanucleus-core-1.1.0.jar).  You'll need to do this for datanucleus-core, datanucleus-jpa, and datanucleus-enhancer.