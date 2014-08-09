#!/bin/bash

GROUP=${project.parent.groupId}
PROJECT=${project.parent.artifactId}
VERSION=${project.parent.version}

echo "Deploy          GROUP: $GROUP"
echo "Deploy        PROJECT: $PROJECT"
echo "Deploy        VERSION: $VERSION"
echo "Deploy  TARGET_SERVER: $TARGET_SERVER"
echo "Deploy    TOMCAT_HOME: $TOMCAT_HOME"
echo "Deploy INSTALL_SCRIPT: $INSTALL_SCRIPT"
echo "Deploy    ENVIRONMENT: $ENVIRONMENT"
echo "Deploy     REPOSITORY: ${REPOSITORY:-default}"
echo "Deploy       WAR_NAME: ${WAR_NAME:-default}"
echo "Deploy        PACKAGE: ${PACKAGE:-bin}"

if [ ! $GROUP ]; then
    echo "Missing GROUP variable."
    exit 1
fi

if [ ! $PROJECT ]; then
    echo "Missing PROJECT variable."
    exit 1
fi

if [ ! $VERSION ]; then
    echo "Missing VERSION variable."
    exit 1
fi

if [ ! $TARGET_SERVER ]; then
    echo "Missing TARGET_SERVER variable."
    exit 1
fi

if [ ! $TOMCAT_HOME ]; then
    echo "Missing TOMCAT_HOME variable."
    exit 1
fi

if [ ! $INSTALL_SCRIPT ]; then
    echo "Missing INSTALL_SCRIPT variable."
    exit 1
fi

if [ ! $ENVIRONMENT ]; then
    echo "Missing ENVIRONMENT variable."
    exit 1
fi

PID_CMD="ps -ef | grep '[B]ootstrap .*start' | grep \$CATALINA_BASE | awk '{ print \$2 }'"

# Login to the target server
ssh tomcat@$TARGET_SERVER << EOW

export TOMCAT_HOME=$TOMCAT_HOME
export ENVIRONMENT=$ENVIRONMENT
export WAR_NAME=$WAR_NAME

echo "Remote TOMCAT_HOME: \$TOMCAT_HOME"
echo "Remote ENVIRONMENT: \$ENVIRONMENT"
echo "Remote    WAR_NAME: \${WAR_NAME:-default}"

DIST_DIR=/tmp/distribution/$PROJECT
    
# Remove an existing distribution directory
if [ -d "\$DIST_DIR" ]; then
        echo Removing previous distribution directory \$DIST_DIR.
        rm -rf \$DIST_DIR
fi
    
echo Creating distribution directory \$DIST_DIR.
mkdir -p \$DIST_DIR
    
# Go to distribution directory for the project where we find the tar.gz distribution file
cd \$DIST_DIR
pwd
    
# Get the distribution
echo Downloading the distribution...
if expr "$VERSION" : ".*SNAPSHOT$" > /dev/null; then
    REPOSITORY=-snapshots
else
    REPOSITORY=
fi

# Define archive name so we can save it as this file name.
ARCHIVE=$PROJECT-dist-$VERSION-${PACKAGE:-bin}.tar.gz

DOWNLOAD_URL="http://localhost.itservices.lan/nexus/service/local/artifact/maven/redirect?r=public\$REPOSITORY&g=${GROUP//.//}&a=$PROJECT-dist&v=$VERSION&c=${PACKAGE:-bin}&p=tar.gz"
wget -O \$ARCHIVE \$DOWNLOAD_URL

# Go to distribution directory for the project where we find the tar.gz distribution file
cd \$DIST_DIR
pwd

# Unzip the tar.gz distribution file
if [ -r \$ARCHIVE ]; then
    tar xvzf \$ARCHIVE
else
    echo "Distribution file \$ARCHIVE not found."
    exit 1
fi
echo "Distribution retrieved."

# Go to the bin directory of Tomcat
cd $TOMCAT_HOME/bin
pwd

# Set the environment
. ./setenv.sh

# Gracefully shutdown Tomcat
wait=10
pid=\$($PID_CMD)
echo "Found process \$pid"
if [ "\$pid" != "" ] ; then
    ./shutdown.sh
    sleep \$wait
fi

# Monitor Tomcat for a few minutes
counter=180
pid=\$($PID_CMD)
echo "Check if Tomcat is still running...";
while [ \$counter -ge 0 -a "\$pid" != "" ] ; do
    echo -e "Still \$counter seconds to go...\\r\\c"

    # Tomcat seems to be down. Check if the process is still running.
    pid=\$($PID_CMD)
    
    if [ \$counter -eq 0 ] ; then
        # Tomcat is still running so we have to kill it
        if [ "\$pid" != "" ] ; then
            echo "Killing process: " \$pid
            kill \$pid
        else
            echo "No process to kill :-("
        fi
    else
        # Sleep some seconds
       sleep \$wait
    fi

    # Next counter
    counter=\$((\$counter-\$wait))
done

# If after a few minutes Tomcat still is not down do another kill attempt
pid=\$($PID_CMD)

# Tomcat is still running so we have to brutally kill it
if [ "\$pid" != "" ] ; then
    echo "Killing process: " \$pid
    kill -9 \$pid
fi
echo "Tomcat is down."

# Install application
cd \$DIST_DIR
pwd
cd $PROJECT-dist-$VERSION
echo "Distribution directory:" \$(pwd)
INSTALL_STATUS=0
if [ -r $INSTALL_SCRIPT ]; then 
    chmod 0744 $INSTALL_SCRIPT
    ./$INSTALL_SCRIPT
    INSTALL_STATUS=$?
else
    echo "Installation script $INSTALL_SCRIPT not found."
    INSTALL_STATUS=1
fi

cd $TOMCAT_HOME
echo "Cleanup Tomcat"
if [ \$INSTALL_STATUS -eq 0 ]; then
    rm -rf work/*
    echo "Compressing current catalina.out and removing catalina.out logs older than 3 months"
    cd logs
    find . \( -name "catalina.out*" -o -name "catalina.*.log" \) -mtime +91 -print -exec rm -f {} \;
    TIMESTAMP=`date +%Y-%m-%d.%H-%M-%S`
    mv catalina.out catalina.out-\$TIMESTAMP
    tar -zcvf catalina.out-\$TIMESTAMP.tar.gz catalina.out-\$TIMESTAMP
    rm -f catalina.out-\$TIMESTAMP
    touch catalina.out
fi

# Start Tomcat
cd $TOMCAT_HOME/bin
pwd
./startup.sh
echo "Tomcat started."

# Remove distribution package
if [ \$INSTALL_STATUS -eq 0 ]; then
    echo "Remove distribution package."
    rm -Rf \$DIST_DIR
else
    echo "Execution of installation script failed."
    exit \$INSTALL_STATUS    
fi

EOW
