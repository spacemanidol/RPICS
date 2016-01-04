#!/bin/bash
COUNTER=1
while [  $COUNTER -lt 20 ]; do
    ./netinf -i:$COUNTER'cascades.txt' -e:1000 -o:'TestReddit/'$COUNTER'-1000' &
    let COUNTER=COUNTER+1 
done
