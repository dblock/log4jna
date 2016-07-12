Files
=====

Include *log4jna.jar*, *jna.jar* and *platform.jar* in your application. If you're logging under a web server, JNA files (*jna.jar* and *platform.jar*) should be placed in a parent classloader such as Tomcat's lib directory.

Properties
==========

    log4j.appender.A=org.apache.log4jna.nt.Win32EventLogAppender
    log4j.appender.A.source=Log4jna
    log4j.appender.A.layout=org.apache.log4j.PatternLayout 
    log4j.appender.A.layout.ConversionPattern=%d{EEE dd MMM HH:mm:ss} - %m%n

Registry
========

In order to get properly formatted messages in the event log, create `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\services\eventlog\Application\Log4jna` with the values of `EventMessageFile` and `CategoryMessageFile` pointing to the location of *Win32EventLogAppender.dll* (eg. `c:\Program Files\MyApplication\Win32EventLogAppender.dll`) and the values of `TypesSupported = 7` and `CategoryCount = 6`. Note that unlike log4j's NTEventLogAppender, this DLL only contains message formats and you do not need to place it in the system directory.

![org.apache.log4jna.nt.Win32EventLogAppender registry](org.apache.log4jna.nt.Win32EventLogAppender.registry.png?raw=true "Registry")

The Log4jna value above is the name of your event source. This can be changed by defining the appender's source property.

Creating this registry key is typically done in your application's installer. Log4jna will attempt to create it if it doesn't exist, but won't know where to find the *Win32EventLogAppender.dll*. This will cause the Event Viewer to be confused and include a warning about not finding the message resource. Messages will still be logged and display.

FAQ
===

* [log4j:ERROR Could not register event source (Access is denied.)](http://code.dblock.org/log4jna-log4jerror-could-not-register-event-source-access-is-denied)
* [The description for Event ID 4096 from source log4jna cannot be found.](http://code.dblock.org/log4jna-the-description-for-event-id-4096-from-source-log4jna-cannot-be-found)