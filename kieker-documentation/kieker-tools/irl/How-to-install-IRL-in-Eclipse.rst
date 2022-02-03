.. _kieker-tools-irl-how-to-install-the-irl-in-eclipse:

How to Install the Instrumentation Record Language in Eclipse
=============================================================

In this tutorial we will install Eclipse and install the IRL within Eclipse.
In case you do have Eclipse installed on your computer, you can switch to
section 3. After installation you have to setup the IRL for your target
language, e.g., Java, C or Python. This is explained in 
:ref:`tutorials-how-to-setup-irl-in-eclipse`

Installing Java
---------------

- Most Linux distributions come with packages for Java. Install Java 11 or later.
- For Windows and Mac, you find installers at
  https://www.oracle.com/java/technologies/downloads/#java11

Installing Eclipse
------------------

Eclipse is a versatile Integrated Development Environment (IDE) based on Java.
Before you can install the IRL in Eclipse you need to install Eclipse.

- Download Eclipse from the Eclipse website https://www.eclipse.org/downloads/
- Click on the Download button for *Get Eclipse IDE 2021‑12*, this directs you
  to a download page for the current version of Eclipse. As of today this is
  the 2021-12 edition. However, CP-DSL should also work with previous and
  later versions of Eclipse.
- Click on the Download button for the current version. This should start the
  download and show the donate page, which can be ignored.
- Wait until the download is complete.
- Depending on your platform you may have zip, dmg or tar.gz archive.
- Unpack the archive and run the installer within the archive. On Linux this
  can be done with:
  - `tar -xvzpf eclipse-inst-jre-linux64.tar.gz`  (unpack archive)
  - `eclipse-installer/eclipse-inst` (run installer)
- The installer will present different options of IDE setups. You can choose
  any one of them, as installing IRL later will automatically install all
  dependencies. In case you want to develop with Java, choose
  *Eclipse IDE for Java Developers*
  
  .. image:: images/install-irl/eclipse-installer.png
  
- Start the installation. This may take a while.
- After downloading, the installer may ask you whether you trust the Eclipse
  certificates. Check both of them and check "Remember accepted certificates".
- Click on "Trust selected".
- Click on "Launch" (or terminate and use regular startup options for Eclipse
  from you systems menu)

Installing the IRL in Eclipse
-----------------------------

- Open Eclipse. If you pressed on "Launch" in the previous step, this will
  automatically happen.
- Eclipse starts up and asks for a workspace directory. You may take the
  default or use a different name. The documentation will use `eclipse-workspace`
  as the name for the workspace directory.
  
  .. image:: images/install-irl/select-workspace-directory.png
  
- Now Eclipse shows its start screen and we can start to install the IRL
  extension.
  
  .. image:: images/install-irl/eclipse-first-start.png
  
- Click on *Help* and then *Install new Software* menu entry. This opens up
  the Install dialog.
  
  .. image:: images/install-irl/install-new-component.png
  
- Here we need to add the update-site for the IRL. Therefore, click on
  *Add ...* button on the right side. This allows us to add a new repository.
  
  .. image:: images/install-irl/add-kieker-repository.png
  
- Enter `Kieker IRL` as name and 
  `https://maui.se.informatik.uni-kiel.de/repo/kdt/snapshot/`
  as URL for the repository. Instead of the snapshot, you can also use one of
  the previous releases, e.g., `https://maui.se.informatik.uni-kiel.de/repo/kdt/1.2`
- Click on *Add* 
- Below the row with "Work with:" and the *Add* button, a list appears with
  the entry `Kieker Development Tools`. Check the checkbox in front and click
  the *Next >* button.
  
  .. image:: images/install-irl/select-kieker.png
  
- On the next page of the installation dialog the IRL feature is listed.

  .. image:: images/install-irl/install-details-kieker.png

- Click on *Finish* to download them.
- Now a *Security Warning* pops up indicating that our packages are not signed.
  Unfortunately, this is still true, as the signing process is not trivial,
  but will be fixed in future releases.
  
  .. image:: images/install-irl/security-warning-install-anyway.png
  
- To proceed with clicking on *Install Anyway*.
- Eclipse will install the extension and ask to restart.

  .. image:: images/install-irl/restart-eclipse.png

- Click on *Restart Now*. This ensures that the features are installed
  correctly.
- Now you are setup.

