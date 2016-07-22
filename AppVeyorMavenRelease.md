# Configuring Builds, SNAPSHOT Deployments and Releases Loj4JNA with AppVeyor and Maven Release Plugin.

## Introduction

This are instruction for the Log4JNA developers that manage the Continuous Integration server and delivery to the SNAPSHOT and Maven Central repositories.

We do all our deployments and releases from the Continuous Integration Server on the [master](https://github.com/dblock/) branch.

There are GPG key signatures and repositories user id and password requirement that are encrypted in the `log4jna-build` subproject that can be managed by authorized developers only.

There are 3 projects and configurations in AppVeyor and Maven profiles to perform these tasks.

1. Project **Log4JNA Default**: The default configuration, uses `appveyor.yml` and launches with every check in on GitHub. 
     Checks that all test pass and thet nothing is broken in the build.
2. Project **Log4JNA Deploy**: Uses `appveyor-deploy.yml` and launches once a day at 2 AM EST to update the SNAPSHOT repository if there are changes.
3. Project **Log4JNA Release**: Uses `appveyor-mvn-relesase.yml` and is manually launched when a new release is ready. 
     Uses `maven-relese-plugin' to build and deploy to Maven Central. 
     **NOTE**: This build has to be completed before 2 AM or a conflict with the snapshot build may occur.

## Configuring AppVeyor projects

### Project Log4JNA Default
1. On the AppVeyor server click on New Project name it **Log4JNA Default**.
2. Just save the project.

### Project Log4JNA Deploy
1. On the AppVeyor server click on New Project name it **Log4JNA Deploy**
2. Scroll down to `Default Branch` and enter `master`
3. On `Branches To Build drop down` select `Only branches specified below` and click on `Add branch`
4. On the opened text box enter `master`
5. Scroll down to `Build schedule` and enter `0 6 * * *` (AppVeyor times are UTC EST is UTC - 4 this makes the build run at 2 AM EST daily).
6. Scroll down to `Custom configuration .yml' file name` and enter `apveyor-deploy.yml`
7. Save the project.

### Project Log4JNA Relese
1. On the AppVeyor server click on New Project name it **Log4JNA Release**
2. Scroll down to `Default Branch` and enter `master`
3. On `Branches To Build drop down` select `Only branches specified below` and click on `Add branch`
4. On the opened text box enter `master`
5. Scroll down to `Custom configuration .yml' file name` and enter `apveyor-mvn-relese.yml`
7. Save the project.

## Configuring Maven
The maven confi
