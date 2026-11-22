# Win32EventLogAppender.dll

The Win32EventLogAppender.dll can be built either with Microsoft Visual C or GCC using MinGW on Unix or Windows.

To build on Windows with GCC download [MinGW-w64](https://www.mingw-w64.org/).

The ant `build.xml` script is designed to be run via [Maven](https://maven.apache.org/) and the [`maven-antrun-plugin`](https://maven.apache.org/plugins/maven-antrun-plugin/) associated to the `compile` goal.

The default build environment is driven by Maven profiles that are activated by the OS family attribute. On Windows it defaults to Visual C++ and on Linux or Unix it defaults to GCC MinGW32.

## Building on Windows

### Building with MS Visual C

Execute the appropriate `vcvars[32 | 64 | all]` or launch a developer command line. See [Use the Microsoft C++ toolset from the command line](https://learn.microsoft.com/en-us/cpp/build/building-on-the-command-line) for more.

To build use your IDE or run `mvn clean compile` or any goal that will include compile like `deploy` or `package`.

### Building with MinGW and GCC

Install MinGW and add `<min-gw-home>\bin` to your `PATH` variable.

To build configure your IDE to launch Maven with the `mwbuild` profile, or run `mvn clean compile -P mwbuild` or any goal that will include compile like `deploy` or `package`.

## Building on Linux or Unix

### Building with MinGW and GCC

Install MinGW32 and add `<min-gw-home>/bin` to your `PATH` variable.

To build use your IDE or run `mvn clean compile` or any goal that will include compile like `deploy` or `package`.

### Building with Visual C

**NOTE: We do not support nor have tested building this option**

Theoretically you should be able to do this with **mono**. Configure `mono` and install Visual C tools.

To build configure your IDE to launch Maven with the `msbuild` profile, or run `mvn clean compile -P msbuild` or any goal that will include compile like `deploy` or `package`.

