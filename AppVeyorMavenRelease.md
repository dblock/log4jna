# Configuring Builds, SNAPSHOT Deployments and Releases Loj4JNA with AppVeyor and Maven Release Plugin

## Introduction

This document explains how Log4JNA developers manage the Continuous Integration server and delivery to the SNAPSHOT and Maven Central repositories.

We do all our deployments and releases from the Continuous Integration Server on the `master` branch.

There are GPG key signatures and repositories user ID and password requirement that are encrypted in the `log4jna-build` subproject that can be managed by authorized developers only.

There're 3 projects and configurations in AppVeyor and Maven profiles to perform these tasks.

1. Project **Log4JNA Default**: The default configuration, uses `appveyor.yml` and launches with every check in on GitHub.
     Checks that all test pass and thet nothing is broken in the build.
2. Project **Log4JNA Deploy**: Uses `appveyor-deploy.yml` and launches once a day at 2 AM EST to update the SNAPSHOT repository if there're changes.
3. Project **Log4JNA Release**: Uses `appveyor-mvn-relesase.yml` and is manually launched when a new release is ready.
     Uses `maven-relese-plugin' to build and deploy to Maven Central.
     **NOTE**: This build has to be completed before 2 AM or a conflict with the snapshot build may occur.

## Requirements

## Configuring Projects

### Project Log4JNA Default

This is the default build, see [APPVEYOR](APPVEYOR.md).

#### AppVeyor

This project runs mvn install locally to trigger all tests and generate the jars and zip files for distribution.

1. On the AppVeyor server click on New Project name it **Log4JNA Default**.
2. Just save the project.

#### Maven

1. Open a command window as administrator.
2. Run `vcvars<all | 32 | 64>` to create the VC invironment.
3. Run `mvn clean install` and see all files copied to `%USERPROFILE%\.m2\repository\org\dblock\log4jna` tree.

### Project Log4JNA Deploy

This project is launched nightly by the CI server and requires the following configuration.

1. On the AppVeyor server click on New Project name it **Log4JNA Deploy**
2. Scroll down to `Default Branch` and enter `master`
3. On `Branches To Build drop down` select `Only branches specified below` and click on `Add branch`
4. On the opened text box enter `master`
5. Scroll down to `Build schedule` and enter `0 6 * * *` (AppVeyor times are UTC EST is UTC - 4 this makes the build run at 2 AM EST daily)
6. Scroll down to `Custom configuration .yml' file name` and enter `apveyor-deploy.yml`
7. Save the project.

### Project Log4JNA Release

1. On the AppVeyor server click on New Project name it **Log4JNA Release**
2. Scroll down to `Default Branch` and enter `master`
3. On `Branches To Build drop down` select `Only branches specified below` and click on `Add branch`
4. On the opened text box enter `master`
5. Scroll down to `Custom configuration .yml' file name` and enter `apveyor-mvn-relese.yml`
7. Save the project.

## Configuring Maven

TODO
