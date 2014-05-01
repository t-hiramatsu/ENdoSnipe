@echo off
setlocal
set BASEDIR=%~dp0
for %%i in (%BASEDIR%\..\lib\*.jar) do call :setpath %%i
goto :execute
:setpath
set CLASSPATH=%CLASSPATH%;%1
goto :EOF
:execute
if "%6"=="" goto :simply
"%JAVA_HOME%\bin\java.exe" -classpath "%CLASSPATH%" jp.co.acroquest.endosnipe.report.ReporterMain %1 %2 %3 %4 %5 %6
goto :EOF
:simply
"%JAVA_HOME%\bin\java.exe" -classpath "%CLASSPATH%" jp.co.acroquest.endosnipe.report.ReporterMain %1 %2 %3 %4 %5
