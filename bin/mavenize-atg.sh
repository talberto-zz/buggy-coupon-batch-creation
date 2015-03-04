#!/bin/sh

MODULES="DAS DPS DCS DSS"

for module in $MODULES; do
  echo "Installing module $module"
  mvn install:install-file -Dfile="$DYNAMO_ROOT/$module/lib/classes.jar" -DgroupId="atg.platform" -DartifactId="$module" -Dclassifier="classes" -Dversion="11.1" -Dpackaging="jar"
  mvn install:install-file -Dfile="$DYNAMO_ROOT/$module/lib/resources.jar" -DgroupId="atg.platform" -DartifactId="$module" -Dclassifier="resources" -Dversion="11.1" -Dpackaging="jar"
done
