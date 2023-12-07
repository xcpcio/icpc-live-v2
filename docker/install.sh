#! /bin/bash

apt update -y

apt install -y ca-certificates curl gnupg \
    htop vim git wget openjdk-8-jdk-headless maven

cd /live || exit 1

mvn install
