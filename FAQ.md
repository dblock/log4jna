# Frequently Asked Questions

## Error Processing Element Win32EventLog: CLASS_NOT_FOUND

Log4j 2 could not find the `Win32EventLog` plugin. Make sure that `log4jna-api.jar` is on the CLASSPATH next to `log4j-core.jar`, `jna.jar` and `jna-platform.jar`. If you package your application in a single jar, see [Packaging in a Single Jar](USAGE.md#jar).

## Could Not Register Event Source

Log4JNA creates the event source in the registry the first time it runs, which requires Administrator privileges. Run the application as Administrator once, or [set up the registry manually](USAGE.md#registry).

## The Description for Event ID Cannot Be Found

Event Viewer could not find the message file. Make sure that the `EventMessageFile` and `CategoryMessageFile` registry entries of your event source point to an existing `Win32EventLogAppender.dll`, see [The Message File](USAGE.md#msg).
