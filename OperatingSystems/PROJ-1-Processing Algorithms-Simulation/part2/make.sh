#!/bin/bash

CFLAGS="-Wall"
LDFLAGS="-lm"
reset
echo "-->compiling"
g++ $CFLAGS *.cpp -o mm
if [ -f ./mm ]
  then
    echo "-->running"
    ./mm
    echo "-->exiting"
    rm mm
fi
