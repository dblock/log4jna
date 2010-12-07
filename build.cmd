@echo off
setlocal ENABLEEXTENSIONS ENABLEDELAYEDEXPANSION
pushd %~dp0

set ProgramFilesDir=%ProgramFiles%
if NOT "%ProgramFiles(x86)%"=="" set ProgramFilesDir=%ProgramFiles(x86)%

set VisualStudioCmd=%ProgramFilesDir%\Microsoft Visual Studio 9.0\VC\vcvarsall.bat
if EXIST "%VisualStudioCmd%" call "%VisualStudioCmd%"

for /D %%n in ( "%ProgramFilesDir%\NUnit*" ) do (
 set NUnitDir=%%~n
)

call ant.bat %~1

popd
endlocal
goto :EOF

:Usage
echo  Syntax:
echo.
echo   build [target]
echo.
goto :EOF
