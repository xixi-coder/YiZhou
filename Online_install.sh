#!/bin/bash
if [ $1 == "1" ]
    then
    mvn clean install -Dmaven.test.skip=true -P Online1
    echo "carried out Online1 version"
    command groups
elif [ $1 == "2" ]
    then
    mvn clean install -Dmaven.test.skip=true -P Online2
    echo "carried out Online2 version"
    command groups
else
    echo "please choose  parameter 1 or 2"
fi
