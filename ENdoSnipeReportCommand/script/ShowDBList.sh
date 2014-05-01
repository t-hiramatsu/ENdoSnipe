# add Class path

for jarName in ../lib/*.jar
do
  CLASSPATH="$CLASSPATH:$jarName"
done
export CLASSPATH

$JAVA_HOME/bin/java -classpath $CLASSPATH jp.co.acroquest.endosnipe.report.ShowDBList $1
