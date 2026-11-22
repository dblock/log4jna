![Log4JNA](https://github.com/dblock/log4jna/raw/master/log4jna.jpg?raw=true "Log4JNA")
[![Build status](https://github.com/dblock/log4jna/actions/workflows/Default.yml/badge.svg)](https://github.com/dblock/log4jna/actions/workflows/Default.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.dblock.log4jna/log4jna)](https://search.maven.org/artifact/org.dblock.log4jna/log4jna)

Log4JNA is a library of native appenders to write into Windows Event Viewer for [Log4j 2](https://logging.apache.org/log4j/2.x/).

Unlike the native implementation(s) in Log4j, this project uses [JNA](https://github.com/java-native-access/jna) and therefore does not require a native DLL in a system directory or on PATH.

**Note:** Log4JNA is intended for Windows environments and requires access to Windows registry and Event Viewer, therefore can only be tested in Windows environments.

## Installation

Add `log4jna-api` to your `pom.xml`. See [Maven, Ivy, and Gradle Artifacts](MAVENIVYGRADLE.md) for other build systems and the [Downloads](#dw) section for zips.

```xml
<dependency>
  <groupId>org.dblock.log4jna</groupId>
  <artifactId>log4jna-api</artifactId>
  <version>2.1.0</version>
</dependency>
```

Download the message file `Win32EventLogAppender.dll` from [log4jna-win32dll-2.1.0.zip](https://repo1.maven.org/maven2/org/dblock/log4jna/log4jna-win32dll/2.1.0/log4jna-win32dll-2.1.0.zip) and place it next to your application.

## Usage

Configure the `Win32EventLog` appender in `log4j2.xml`.

```xml
<Configuration>
  <Appenders>
    <Win32EventLog name="Win32EventLog" source="MyApplication"
      eventMessageFile="Win32EventLogAppender.dll"
      categoryMessageFile="Win32EventLogAppender.dll">
      <PatternLayout pattern="%m%n" />
    </Win32EventLog>
  </Appenders>
  <Loggers>
    <Root level="info">
      <AppenderRef ref="Win32EventLog" />
    </Root>
  </Loggers>
</Configuration>
```

Log as usual, and the message appears in *Windows Logs -> Application* in Event Viewer with *MyApplication* as the *Source*.

```java
LogManager.getLogger().info("Hello from Log4JNA");
```

Run the application as Administrator once, so that Log4JNA can register the event source in the registry, or [set up the registry manually](USAGE.md#registry).

See [Log4JNA configuration](USAGE.md) for all options and the [FAQ](FAQ.md) for common problems and solutions.

## Contributing

See [Contributing](CONTRIBUTING.md) for a primer on how to help on Log4JNA development.

See [Development Environment Set Up](ENVIRONMENT.md) for some tips and requirements before starting.

## <a name="dw"></a>Downloads

### Log4JNA 2 for Log4j 2.x

Releases are published to Maven Central. See [org.dblock.log4jna on Maven Central](https://central.sonatype.com/namespace/org.dblock.log4jna) for all versions and their downloads, including the demo with dependencies and a zip with jars, sources, dependencies and documentation.

Development snapshots are published to the [Maven Central snapshots repository](https://central.sonatype.com/repository/maven-snapshots/org/dblock/log4jna/).

### Log4JNA for Log4j 1.x

Download [log4jna-1.3.zip](https://github.com/dblock/log4jna/releases/tag/1.3). It provides [org.apache.log4jna.nt.Win32EventLogAppender](log4jna-doc/org.apache.log4jna.nt.Win32EventLogAppender.md), a replacement for NTEventLogAppender based on JNA that doesn't require a native DLL in a system directory.

## License

This project is licensed under the [Apache Software Foundation 2.0 License](http://www.apache.org/licenses/LICENSE-2.0)

