# add Class path

for jarName in ../lib/*.jar
do
  CLASSPATH="$CLASSPATH:$jarName"
done
export CLASSPATH

if [ -e $6 ]
then
  $JAVA_HOME/bin/java -classpath $CLASSPATH jp.co.acroquest.endosnipe.report.ReporterMain $1 $2 $3 $4 $5
else
  $JAVA_HOME/bin/java -classpath $CLASSPATH jp.co.acroquest.endosnipe.report.ReporterMain $1 $2 $3 $4 $5 $6
fi
