#!/bin/bash
##
#
# Script used to install required components on an AWS EC2 instance.
# This should only need to be run once to create an AMI which can be reused
#
##

yum update -y

# Install cmake 3
yum install gcc-c++ -y
mkdir cmake
pushd cmake
wget https://cmake.org/files/v3.10/cmake-3.10.0.tar.gz
tar -xvzf cmake-3.10.0.tar.gz
pushd cmake-3.10.0
./bootstrap
###
make
make install
popd
popd

# Install opencv
mkdir opencv
pushd opencv
yum install git -y
git clone https://github.com/Itseez/opencv.git
/usr/local/bin/cmake opencv
make
make install
popd

# Install java 8
yum install java-1.8.0 -y
yum remove java-1.7.0-openjdk -y
