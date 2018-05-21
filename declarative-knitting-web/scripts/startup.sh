#!/bin/bash
##
#
# Script used to start up a new EC2 instance.
# This should be run at start-up.  Requires 1 argument, the URL in S3 of the built JAR.
#
##

sudo yum update -y

# Forward port 80 -> 8080
sudo iptables -t nat -I PREROUTING -p tcp --dport 80 -j REDIRECT --to-ports 8080

# Copy executable from S3
mkdir declarative-knitting
cd declarative-knitting
aws s3 cp $1  .

# Launch Spring Boot
java -jar declarative-knitting-web-1.0-SNAPSHOT.jar &