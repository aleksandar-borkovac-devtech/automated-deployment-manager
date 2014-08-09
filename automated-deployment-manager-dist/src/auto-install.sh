#!/bin/bash

# Parse any arguments given
USAGE="`basename $0` <DEMO|PRD> <tomcat-home> <war-name>"

if [ $1 ]; then
    ENVIRONMENT=$1
fi
if [ $2 ]; then
    TOMCAT_HOME=$2
fi
if [ $3 ]; then
    WAR_NAME=$3
fi

# Check which environment to install for
if [ $ENVIRONMENT ]; then

    if [ $ENVIRONMENT = DEMO ]; then
        SOURCE_DIR=tomcat-demo
    fi
    
    if [ $ENVIRONMENT = PRD ]; then
        SOURCE_DIR=tomcat-prod
    fi
    
    if [ ! $SOURCE_DIR ]; then
        echo "Invalid environment $ENVIRONMENT"
        echo $USAGE
        exit 1
    fi
else
    echo "Missing \$ENVIRONMENT"
    echo $USAGE
    exit 1
fi

if [ ! $TOMCAT_HOME ]; then
    echo "Missing \$TOMCAT_HOME"
    echo $USAGE
    exit 1
fi

if [ ! -d "$TOMCAT_HOME/webapps" ]; then
    echo "Invalid Tomcat home directory $TOMCAT_HOME"
    echo $USAGE
    exit 1
fi

if [ ! $WAR_NAME ]; then
    WAR_NAME=${final-war-name}
fi
if [ ! $WAR_NAME ]; then
    echo "Missing \$WAR_NAME or missing maven property final-war-name"
    echo $USAGE
    exit 1
fi

echo "Install ENVIRONMENT: $ENVIRONMENT"
echo "Install TOMCAT_HOME: $TOMCAT_HOME"
echo "Install    WAR_NAME: $WAR_NAME"

echo
dateString=`date +%Y-%m-%d.%H:%M:%S`
if [ -r $TOMCAT_HOME/webapps/$WAR_NAME.war ]; then 
    echo "Making backup of previous installation."
    # Create backup directory
    mkdir -p $TOMCAT_HOME/backup/$dateString/
    # Move WAR to backup directory
    mv $TOMCAT_HOME/webapps/$WAR_NAME.war $TOMCAT_HOME/backup/$dateString/
    # Copy configuration classes
    cp -Rpf $TOMCAT_HOME/shared/classes $TOMCAT_HOME/backup/$dateString/
    cp -Rpf $TOMCAT_HOME/conf/Catalina/localhost $TOMCAT_HOME/backup/$dateString/
    cp -Rpf $TOMCAT_HOME/conf/context.xml $TOMCAT_HOME/backup/$dateString/
fi

# Delete exploded WAR (always)
rm -rf $TOMCAT_HOME/webapps/$WAR_NAME
echo "Installing application $WAR_NAME."
cp $WAR_NAME.war $TOMCAT_HOME/webapps/$WAR_NAME.war
if [ $? -eq 1 ]; then
    echo "Copy of $WAR_NAME.war to webapps failed!"
    exit 1
fi

echo
echo "The following files will copied to the TOMCAT_HOME directory:"
cd $SOURCE_DIR && find . -type f -print && cd - > /dev/null

echo "Exisiting files will be overwritten."
echo "Copying files from $SOURCE_DIR to $TOMCAT_HOME"
cd $SOURCE_DIR && find . -type d -exec mkdir -p $TOMCAT_HOME/{} \; && cd - > /dev/null
cd $SOURCE_DIR && find . -type f -exec cp -p {} $TOMCAT_HOME/{} \; && cd - > /dev/null

# Secure private keys
if [ -d $TOMCAT_HOME/shared/classes/security ]; then
    chmod 700 $TOMCAT_HOME/shared/classes/security
    if [ -r $TOMCAT_HOME/shared/classes/security/pubring.pgp ]; then
        chmod 640 $TOMCAT_HOME/shared/classes/security/*
    fi
fi

echo "Done."
