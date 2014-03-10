@echo on
set BASEDIR=%~dp0
set BASEPATH=%BASEDIR:\=/%
set JVMOPTIONS=-Dcollector.property=%BASEDIR%../conf/collector.properties -Dlog4j.configuration=file:///%BASEPATH%/../conf/log4j.properties
set JAR=%BASEDIR%..\lib\endosnipe-datacollector.jar

java %JVMOPTIONS% -Xmx256m -jar %JAR% start