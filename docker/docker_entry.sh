#! /bin/bash

set -ex

cd /live/icpc-live-v2 || exit 1

if [ X"${1}" = X"primary" ]; then
    exec mvn jetty:run
else
    exec "$@"
fi
