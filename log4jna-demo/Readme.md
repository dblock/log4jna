# Log4JNA Demo

A demo application that logs to Event Viewer with the `Win32EventLog` appender.

## Running the Demo

Build the project with `mvn install`, then run the demo jar with dependencies from the `log4jna-demo` directory. Run it as Administrator the first time, so that Log4JNA can create the registry entries, or [set up the registry manually](../ENVIRONMENT.md#for-the-demo-application).

```
cd log4jna-demo
java -jar target\log4jna-demo-<version>-jar-with-dependencies.jar
```

The demo uses [`src/main/resources/log4j2.xml`](src/main/resources/log4j2.xml), which logs to *Applications and Services Logs -> Win32LogApplication* with *DemoLogger1* as the *Source*, and the message file in `src/test/resources`.

You can also extract `log4jna-assembly/target/run-demo.zip` and run `rundemo.cmd`.
