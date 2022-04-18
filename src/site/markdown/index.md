![Log4JNA](https://github.com/dblock/log4jna/raw/master/log4jna.jpg?raw=true "Log4JNA")
[![Gitter](https://badges.gitter.im/dblock/log4jna.svg)](https://gitter.im/dblock/log4jna?utm_source=badge&amp;utm_medium=badge&amp;utm_campaign=pr-badge)
[![Build status](https://github.com/dblock/log4jna/actions/workflows/Default.yml/badge.svg)](https://github.com/dblock/log4jna/actions/workflows/Default.yml)

Log4JNA is a library of native appenders to write into Windows<sup>TM</sup> Event Viewer for [Log4j 2<sup>TM</sup>](http://logging.apache.org/log4j/). 

Unlike the native implementation(s) in Log4j<sup>TM</sup>, this project uses [JNA](http://github.com/twall/jna) and therefore does not require a native DLL in a system directory or on PATH.

**Note:** Log4JNA is intended for Window<sup>TM</sup> environments and requires access to Windows<sup>TM</sup> registry and Event Viewer, therefore can only be tested in Windows<sup>TM</sup> environments

## Using Log4JNA

#### Getting Log4JNA

See the [Downloads](#dw) section bellow to get Log4JNA.

See [Maven, Ivy, and Gradle Artifacts](MAVENIVYGRADLE.md) to include Log4JNA from Maven Central

#### Using Log4JNA

See [Log4JNA configuration](USAGE.md) for instructions and tips.

See the [FAQ](FAQ.md) for common problems and solutions.

## Developing Log4JNA
See [Contributing](CONTRIBUTING.md) for a primer on how to help on Log4JNA development.

See [Development Environment Set Up](ENVIRONMENT.md) for some tips and requirements before starting.

## <a name="dw"></a>Downloads
#### Log4JNA 2 for Log4j 2.x

| Current Release | Latest Develpment |
| ------------- |------------- |
| [Log4jna Api 2.0](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&amp;g=org.dblock.log4jna&amp;a=log4jna-api&amp;v=RELEASE&amp;e=jar) | [Log4JNA Api 2.0.1-SNAPSHOT](https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&amp;g=org.dblock.log4jna&amp;a=log4jna-api&amp;v=LATEST&amp;e=jar) |
| [Log4jna Demo 2.0](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&amp;g=org.dblock.log4jna&amp;a=log4jna-demo&amp;v=RELEASE&amp;e=jar) | [Log4JNA Demo 2.0.1-SNAPSHOT](https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&amp;g=org.dblock.log4jna&amp;a=log4jna-demo&amp;v=LATEST&amp;e=jar) |
| [Log4jna Demo with dependencies 2.0](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&amp;g=org.dblock.log4jna&amp;a=log4jna-demo&amp;v=RELEASE&amp;e=jar) | [Log4jna Demo with dependencies 2.0.1-SNAPSHOT](https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&amp;g=org.dblock.log4jna&amp;a=log4jna-demo&amp;v=LATEST&amp;c=jar-with-dependencies&amp;e=jar) |
| [Log4jna Zip 2.0 (Jars, Sources, Dependencies and Documentation)](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&amp;g=org.dblock.log4jna&amp;a=log4jna-assembly&amp;v=RELEASE&amp;e=zip) | [Log4jna Zip 2.0.1-SNAPSHOT (Jars, Sources, Dependencies and Documentation)](https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&amp;g=org.dblock.log4jna&amp;a=log4jna-assembly&amp;v=LATEST&amp;e=zip) |

#### Log4JNA  for Log4j 1.x
* [log4jna-1.3.zip](http://code.dblock.org/downloads/log4jna/log4jna-1.3.zip)


* [org.apache.log4jna.nt.Win32EventLogAppender](log4jna-doc/org.apache.log4jna.nt.Win32EventLogAppender.md): a replacement for NTEventLogAppender based on JNA that doesn't require a native DLL in a system directory.


License
-------
This project is licensed under the [Apache Software Foundation 2.0 License](http://www.apache.org/licenses/LICENSE-2.0)

