# Setting Up Your Development Environment

The first time you run the test suite or any application using Log4JNA, Log4JNA attempts to write the necessary entries in Windows Registry. If you don't have Administrator privileges the run fails.

## Running Tests

In order for the tests to pass you must run them as Administrator or set up the registry before your first run.

Open a command window as administrator and execute `mvn test`.

If you are using an IDE, launch it as Administrator, import the Maven projects and run the tests.

## IDE Setup

Please be careful and do not check IDE configuration files into GitHub, you can add the project to your IDE following the instructions below.

Eclipse configuration files are already added to `.gitignore`, please add any configuration file created by your IDE to the ignore file.

### Eclipse

See [this article](https://eclipse.dev/m2e/) to import the Maven projects.

### IntelliJ IDEA

See [this article](https://www.jetbrains.com/help/idea/maven-support.html) to import the Maven projects.

### NetBeans

See [this article](https://netbeans.apache.org/wiki/main/wiki/MavenBestPractices/) to import the Maven projects.

## Set Up the Registry Manually

### For Log4JNA API Tests

The `Win32EventLogAppender.dll` file is provided in the source as a test asset in `<project-location>/log4jna-api/src/test/resources`.

The tests configure Log4j programmatically and write into `Event Viewer-->Windows Logs-->Application`.

1. Open `regedit` and browse to `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Application\`
2. Right click and select `New Key`, name it `Log4jnaTest`
3. Add the following entries to `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Application\Log4jnaTest`

| Entry name          | Entry Type | Value                                    |
| ------------------- | ---------- | ---------------------------------------- |
| TypesSupported      | DWORD (32 bit) Value (REG_DWORD) | 0x7                                      |
| CategoryCount       | DWORD (32 bit) Value (REG_DWORD) | 0x6                                      |
| EventMessageFile    | String Value         (REG_SZ)    | &lt;Full path to&gt;\Win32EventLogAppender.dll |
| CategoryMessageFile | String Value         (REG_SZ)    | &lt;Full path to&gt;\Win32EventLogAppender.dll |

The registry should look like this:

![Registry Img](img/registry1.png)

### For the Demo Application

The demo Log4j 2 configuration `log4j2.xml` is provided in `<project-location>/log4jna-demo/src/main/resources` and the `Win32EventLogAppender.dll` file in `<project-location>/log4jna-demo/src/test/resources`.

The demo configuration writes into `Event Viewer-->Applications and Services Logs-->Win32LogApplication`.

1. Open `regedit` and browse to `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\`
2. Right click and select `New Key`, name it `Win32LogApplication`
    1. Add another key to `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Win32LogApplication`, name it `DemoLogger1`
3. Add the following entries to `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Win32LogApplication\DemoLogger1`

| Entry name          | Entry Type | Value                                    |
| ------------------- | ---------- | ---------------------------------------- |
| TypesSupported      | DWORD (32 bit) Value (REG_DWORD) | 0x7                                      |
| CategoryCount       | DWORD (32 bit) Value (REG_DWORD) | 0x6                                      |
| EventMessageFile    | String Value         (REG_SZ)    | &lt;Full path to&gt;\Win32EventLogAppender.dll |
| CategoryMessageFile | String Value         (REG_SZ)    | &lt;Full path to&gt;\Win32EventLogAppender.dll |

The registry should look like this:

![Registry Img](img/registry2.png)
