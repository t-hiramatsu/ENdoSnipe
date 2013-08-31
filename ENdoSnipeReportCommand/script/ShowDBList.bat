@echo off
setlocal
set BASEDIR=%~dp0
for %%i in (%BASEDIR%\..\lib\*.jar) do call :setpath %%i
goto :execute
:setpath
set CLASSPATH=%CLASSPATH%;%1
goto :EOF
:execute
"%JAVA_HOME%\bin\java.exe" -classpath "%CLASSPATH%" jp.co.acroquest.endosnipe.report.ShowDBList %1
