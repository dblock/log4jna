# Log4JNA Configuration
Log4JNA uses some configuration parameters to set up values in Windows<sup>TM</sup> Registry that are required in order to write to the Event Viewer, all other Log4j 2<sup>TM</sup> values are used as described in the [Log4j 2<sup>TM</sup> Configuration Guide](http://logging.apache.org/log4j/2.x/manual/configuration.html)

**Note:** Windows<sup>TM</sup> requires Administrator privileges in order to write into the Registry, therefore you have to run the application as Administrator at least once, or [setup the registry manually](#registry) before using Log4JNA or whenever the location of the message file `Win32EventlogAppender.dll` changes in the configuration file.

- [The message file `Win32EventlogAppender.dll`](#msg)
- [Log4JNA Configuration Parameters](#cp)
- [Log4JNA Registry Entries](#re)
- [Log4J 2 configuration](#lj)
- [Registry set up](#registry)
- [Dependencies](#dep)

## <a name="masg"></a>The message file `Win32EventlogAppender.dll`
Windows<sup>TM</sup> Event Viewer and log depends on message files to find message content, formats, categories and types.

Log4JNA uses a message file named `Win32EventlogAppender.dll`, we only define the categories and types and a generic message format that 
acts as pass through for the message format defined by your Log4j messages.

Log4JNA needs to be able to find the file in order tof

## <a name="cp"></a>Log4JNA Configuration Parameters
--------

| Name | Required | Default Value | Usage Description |
| ----: | :--------: | :-------------: | ----- |
| `name` | true | N/A | This required value **must always be `Win32EventLog`**.<br/>It identifies the appender to Log4j 2<sup>TM</sup> Plugin management implementation  |
| `application` | false | Application | Indicates the top level location in the Event Viewer<br/><br/>The default is *Windows Logs -> Application* with a *Source* as per the `source` parameter.<br/><br/>If any other value than *Application* is used a Registry key in `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\` is created and Log4JNA will log into *Application and Service Logs -> &lt;application-value&gt;* with a Source column as per the `source` parameter.  |
| `source` | false | Log4jnaTest | Indicates the text to use in the *Source* column in the Event Viewer.<br/>**We strongly recommend to use a different value**  |
| `eventMessageFile` | true | N/A | Indicates the file system location of the message file<br/> The value can be relative as far as the program can determinate the absolute location in the file system. |
| `categoryMessageFile` | true | N/A | Indicates the file system location of the message file<br/> The value can be relative as far as the program can determinate the absolute location in the file system. |

## <a name="re"></a>Log4JNA Registry Entries
-------

All entries are created in **`HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\`**.

The final location is defined by the `application` and `source` configuration parameters. 

| Entry name          | Entry Type | Value                                    | User Configurable |
| ------------------- | ---------- | ---------------------------------------- | ----------------- | 
| TypesSupported      | DWORD (32 bit) Value (REG_DWORD) | 0x7                                      | NO |
| CategoryCount       | DWORD (32 bit) Value (REG_DWORD) | 0x6                                      | NO |
| EventMessageFile    | String Value         (REG_SZ)    | &lt;Full path to&gt;\Win32EventlogAppender.dll | YES |
| CategoryMessageFile | String Value         (REG_SZ)    | &lt;Full path to&gt;\Win32EventlogAppender.dll | YES |


## <a name="lj"></a>Log4J 2 configuration
------

Sample configuration files are provide in the Log4JNA Demo project and we use them for this examples.

All Log4j 2<sup>TM</sup> configuration formats are supported, we show here *.xml* and *.properties* configuration examples, for *json*, *yaml*, and *strict xml* configuration follow the [Log4j 2<sup>TM</sup> documentation](http://logging.apache.org/log4j/2.x/manual/configuration.htm).

##### Configuring with all default values. 
`log4j2.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="TRACE">
  <Properties>
    ...
    
    <!-- Message file location -->
    <Property name="dllfile">src\main\resources\Win32EventLogAppender.dll</Property>
    
    ...
  </Properties>

  <Appenders>
    ...
    
    <Win32EventLog name="Win32EventLog" eventMessageFile="${dllfile}" categoryMessageFile="${dllfile}">
      <PatternLayout pattern="%-5p [%t] %m%n" />
    </Win32EventLog>
    
    ...
  </Appenders>
</Configuration>
```

`log4j2.properties`

```properties
status=trace
...
appender.winlogger.type = Win32EventLog
appender.winlogger.name = Win32EventLog
appender.winlogger.eventMessageFile=src\\main\\resources\\Win32EventLogAppender.dll
appender.winlogger.categoryMessageFile=src\\main\\resources\\Win32EventLogAppender.dll
appender.winlogger.layout.type = PatternLayout
appender.winlogger.layout.pattern = %d{yyyy-MM-dd HH:mm:ss} %c{1} [%p] %m%n

logger.winlogger.level = debug
logger.winlogger.appenderRef.winlogger.ref = Win32EventLog

...
```
This configuration will result in the creation of Registry entries in `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Application\Log4jnaTest` and writing to the Event Viewer `Windows Logs\Application` with a Source column value of `Log4jnaTest` 

##### Configuring with default location and named Source. 
`log4j2.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="TRACE">
  <Properties>
    ...
    
    <!-- Message file location -->
    <Property name="dllfile">src\main\resources\Win32EventLogAppender.dll</Property>
    
    ...
  </Properties>

  <Appenders>
    ...
    
    <Win32EventLog name="Win32EventLog" source="WinLogger" eventMessageFile="${dllfile}" 
        categoryMessageFile="${dllfile}">
      <PatternLayout pattern="%-5p [%t] %m%n" />
    </Win32EventLog>
    
    ...
  </Appenders>
</Configuration>
```

`log4j2.properties`

```properties
status=trace
...
appender.winlogger.type = Win32EventLog
appender.winlogger.name = Win32EventLog
appender.winlogger.source=WinLogger
appender.winlogger.eventMessageFile=src\\main\\resources\\Win32EventLogAppender.dll
appender.winlogger.categoryMessageFile=src\\main\\resources\\Win32EventLogAppender.dll
appender.winlogger.layout.type = PatternLayout
appender.winlogger.layout.pattern = %d{yyyy-MM-dd HH:mm:ss} %c{1} [%p] %m%n

logger.winlogger.level = debug
logger.winlogger.appenderRef.winlogger.ref = Win32EventLog

...
```

This configuration will result in the creation of Registry entries in `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Application\WinLogger` and writing to the Event Viewer `Windows Logs\Application` with a Source column value of `WinLogger` 

##### Configuring all values. 
`log4j2.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="TRACE">
  <Properties>
    ...
    
    <!-- Message file location -->
    <Property name="dllfile">src\main\resources\Win32EventLogAppender.dll</Property>
    
    ...
  </Properties>

  <Appenders>
    ...
    
    <Win32EventLog name="Win32EventLog" source="WinLogger" application="Win32LogApplication" 
        eventMessageFile="${dllfile}" categoryMessageFile="${dllfile}">
      <PatternLayout pattern="%-5p [%t] %m%n" />
    </Win32EventLog>
    
    ...
  </Appenders>
</Configuration>
```

`log4j2.properties`

```properties
status=trace
...
appender.winlogger.type = Win32EventLog
appender.winlogger.name = Win32EventLog
appender.winlogger.source=WinLogger
appender.winlogger.application=Win32LogApplication 
appender.winlogger.eventMessageFile=src\\main\\resources\\Win32EventLogAppender.dll
appender.winlogger.categoryMessageFile=src\\main\\resources\\Win32EventLogAppender.dll
appender.winlogger.layout.type = PatternLayout
appender.winlogger.layout.pattern = %d{yyyy-MM-dd HH:mm:ss} %c{1} [%p] %m%n

logger.winlogger.level = debug
logger.winlogger.appenderRef.winlogger.ref = Win32EventLog

...
```

This configuration will result in the creation of Registry entries in `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Win32LogApplicationger\WinLogger` and writing to the Event Viewer `Applications and Service Logs\Win32LogApplication` with a Source column value of `WinLogger` 

## <a name="registry"></a>Registry set up
-------

#### All Defaults
1. Open `regedit` and browse to `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Application\`
2. Right click and select `New Key`, name it `Log4jnaTest` 
3. Add the following entries to `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Application\Log4jnaTest`

The registry should look like this:

![Registry Img](img/registry1.png)

#### Espefiying the *Source* in the Default logger (Recommended)
1. Open `regedit` and browse to `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Application\`
2. Right click and select `New Key`, give it a unique name that identifies your application.  
3. Add the registry entries to `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Application\Log4jnaTest`

The registry should look like this:

![Registry Img](img/registry2.png)

#### Using your own application entries in Event Viwer
Here we use the Log4J Demo values as an example.

1. Open `regedit` and browse to `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\`
2. Right click and select `New Key`, name it `Win32LogApplication`
  1. Add another key to `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Win32LogApplication`, name it `WinLogger` 
3. Add the registry entries to `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Win32LogApplication\WinLogger`

The registry should look like this:

![Registry Img](img/registry2.png)

## <a name="dep"></a>Dependencies
-------
 
 <img align="left" src="img/dependencies.png" />Log4JNA depends on `log4j-core`, `log4j-api`, `jna-plaform` and `jna`. 
 
 Place the dependencies in a location visible in the CLASSPATH.
 
 Verify that the dependencies are not already provided by an Application Sever at the server class loader level.
 
 Place the JNA dependencies at the Application Server class loader level and declare JNA dependencies as provided in your build.
 
 The same principle can be applied for Log4JNA and log4j dependencies in an Application Server if several applications log to the Event Viewer.
 
 On Application Servers consider placing the message file ``
 
 Here is an example of Tomcat dependencies distribution on the file system for a single application.
 

```
 Apche Tomcat
       |+ bin
       |+ conf
       |+ lib
       |   |+ jna-core.jar
       |   |+ jna.jar
       |+ logs
       |+ temp
       |+ webapps
       |     |+ your-application
       |               |+ WEBINF
       |                     |+ lib
       |                         |+ log4jna-api.jar
       |                         |+ lo4j-core.jar
       |                         |+ log4j-api.jar
       |                         |+ Win32EventlogAppender.dll
       |+ work
```
 
 Here is an example of Tomcat dependencies distribution on the file system for multiple applications.
 
```
 Apche Tomcat
       |+ bin
       |+ conf
       |+ lib
       |   |+ jna-core.jar
       |   |+ jna.jar
       |   |+ log4jna-api.jar
       |   |+ lo4j-core.jar
       |   |+ log4j-api.jar
       |   |+ Win32EventlogAppender.dll
       |+ logs
       |+ temp
       |+ webapps
       |     |+ your-application 1
       |     |         |+ WEBINF
       |     |               |+ lib
       |     |                   |+ other-dependencies.jar
       |     |+ your-application 2
       |               |+ WEBINF
       |                     |+ lib
       |                         |+ other-dependencies.jar
       |+ work
```
 