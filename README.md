Log4JNA
=======

![Log4JNA](https://github.com/dblock/log4jna/raw/master/log4jna.jpg?raw=true "Log4JNA")
[![Gitter](https://badges.gitter.im/dblock/log4jna.svg)](https://gitter.im/dblock/log4jna?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Build status](https://ci.appveyor.com/api/projects/status/ske73kq2ilvjbt0v/branch/master?svg=true)](https://ci.appveyor.com/project/dblock/log4jna/branch/master)

Log4jna is a library of native appenders for [log4j](http://logging.apache.org/log4j/). Unlike the native implementation(s) in Log4j, this project uses [JNA](http://github.com/twall/jna) and therefore does not require a native DLL in a system directory or on PATH.

* [org.apache.log4jna.nt.Win32EventLogAppender](doc/org.apache.log4jna.nt.Win32EventLogAppender.md): a replacement for NTEventLogAppender based on JNA that doesn't require a native DLL in a system directory.


### Downloads

#### Log4JNA 2 for Log4j 2.x


| Stable Release Version | Current Development Version |
| ------------- | ------------- |
| Some          | Another       |

 [Log4jna Api](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&amp;g=org.dbloc.log4jna&amp;a=log4jna-api&amp;v=2.0&amp;e=jar)|[Log4JNA Api 2.0-SNAPSHOT](https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&amp;g=org.dblock.log4jna&amp;a=log4jna-api&amp;v=2.0-SNAPSHOT&amp;e=jar) |

#### Log4JNA  for Log4j 1.x
* [log4jna-1.3.zip](http://code.dblock.org/downloads/log4jna/log4jna-1.3.zip)

License
=======

This project is licenced under the Apache Software Foundation 2.0 License.

Please note that despite what the org.apache namespace may suggest, this project is currently not endorsed or sponsored by the Apache Software Foundation.
