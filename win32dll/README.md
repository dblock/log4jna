# Win32EventLogAppender.dll 

The Win32EventLogAppender.dll can build either with Microsoft Visual C or GCC using MinGW on UN*X or Windows.

To build on Windows with GCC download [MinGW](http://www.mingw.org/).

The ant `build.xml` script is designed to be run via [Maven](http://maven.apache.org/) and the 
[`maven-ant-run-plugin`](http://maven.apache.org/plugins/maven-antrun-plugin/) associated to the `compile` goal.

The default build environment is driven by Maven profiles that are activated by the os family attribute. In windows environments it defaults 
to Visual C++ and in Linux/Un*x it defaults to GCC MinGW32.

## Building in Winods
### Building with MS Visual C
Execute the appropriate `vcvars[32 | 64 | all]` or launch a developer command line. See 
[this](https://msdn.microsoft.com/en-us/library/f2ccy3wt.aspx) for more on Visual C command line.

To build use your IDE or run `mvn clean compile` or any goal that will include compile like `deploy` or `package`.

### Building with MinGW and gcc
Install MinGW and add `<min-gw-home>\bin' to your `PATH` variable. 

To build configure your IDE to launch Maven with the 'mwbuild' profile, or run `mvn clean compile -P mwbuild` or any goal that will include 
compile like `deploy` or `package`.

## Building in Linux/Un*x
### Building with MinGW and gcc

Install MinGW32 and add `<min-gw-home>\bin' to your `PATH` variable. 

To build use your IDE or run `mvn clean compile` or any goal that will include compile like `deploy` or `package`.

### Building with Visual C

**NOTE: We do not support nor have tested building this option**

Theoretically you should be able to do this with **mono**. Configure `mono` and install Visual C tools.

To build configure your IDE to launch Maven with the 'msbuild' profile, or run `mvn clean compile -P msbuild` or any goal that will include 
compile like `deploy` or `package`.

