Log4JNA 
=======
[![Build status](https://ci.appveyor.com/api/projects/status/dre9sl70e8wegiti/branch/maven-conversion?svg=true)](https://ci.appveyor.com/project/claudiow/log4jna/branch/maven-conversion)

[![Build status](https://ci.appveyor.com/api/projects/status/ske73kq2ilvjbt0v?svg=true)](https://ci.appveyor.com/project/dblock/log4jna)

![Log4JNA](https://github.com/dblock/log4jna/raw/master/log4jna.jpg?raw=true "Log4JNA")

[![Build status](https://ci.appveyor.com/api/projects/status/l9fbjhdl9sbytjqm/branch/maven-conversion?svg=true)](https://ci.appveyor.com/project/claudiow/log4jna-xs47m/branch/maven-conversion)

Log4jna is a library of native appenders for [log4j](http://logging.apache.org/log4j/). Unlike the native implementation(s) in Log4j, this project uses [JNA](http://github.com/twall/jna) and therefore does not require a native DLL in a system directory or on PATH.

* [org.apache.log4jna.nt.Win32EventLogAppender](doc/org.apache.log4jna.nt.Win32EventLogAppender.md): a replacement for NTEventLogAppender based on JNA that doesn't require a native DLL in a system directory.

Download
========

* [log4jna-1.3.zip](http://code.dblock.org/downloads/log4jna/log4jna-1.3.zip)

License
=======

This project is licenced under the Apache Software Foundation 2.0 License.

Please note that despite what the org.apache namespace may suggest, this project is currently not endorsed or sponsored by the Apache Software Foundation.
