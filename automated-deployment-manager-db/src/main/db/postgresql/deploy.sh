#!/bin/bash

export CHANGE_LOG_FILE=liquibase/adm-changelog.xml
export LIQUIBASE_JAR=liquibase/liquibase.jar
export DRIVER=org.postgresql.Driver
export CLASS_PATH="liquibase/postgresql-8.4-703.jdbc3.jar"

echo "LIQUIBASE_JAR: $LIQUIBASE_JAR"
echo "DRIVER: $DRIVER"
echo "CLASS_PATH: $CLASS_PATH"
echo "CHANGE_LOG_FILE: $CHANGE_LOG_FILE"
echo "CONNECTION_URL: $CONNECTION_URL"
echo "USERNAME: $USERNAME"
echo "PASSWORD: $PASSWORD"

if [ ! $LIQUIBASE_JAR ]; then
    echo "Missing LIQUIBASE_JAR variable."
    exit 1
fi

if [ ! $DRIVER ]; then
    echo "Missing DRIVER variable."
    exit 1
fi

if [ ! $CLASS_PATH ]; then
    echo "Missing CLASS_PATH variable."
    exit 1
fi

if [ ! $CHANGE_LOG_FILE ]; then
    echo "Missing CHANGE_LOG_FILE variable."
    exit 1
fi

if [ ! $CONNECTION_URL ]; then
    echo "Missing CONNECTION_URL variable."
    exit 1
fi

if [ ! $USERNAME ]; then
    echo "Missing USERNAME variable."
    exit 1
fi

if [ ! $PASSWORD ]; then
    echo "Missing PASSWORD variable."
    exit 1
fi

java -jar $LIQUIBASE_JAR --driver=$DRIVER --classpath=$CLASS_PATH --changeLogFile=$CHANGE_LOG_FILE --contexts=$CONTEXTS --url=$CONNECTION_URL --username=$USERNAME --password=$PASSWORD update
