Demo.

Work in progress.

To run the demo:
 1 checkout log4jna project and run "maven install" on it.
 2 create the registry entries HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Win32LogApplication\WinLogger
  	Name:            CategoryCount
  	Type:            REG_DWORD
  	Data:            0x6

  	Name:            TypesSupported
  	Type:            REG_DWORD
  	Data:            0x7