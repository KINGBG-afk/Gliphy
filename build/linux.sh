#!/bin/bash

echo "Building jar..."

mvn clean package

if [ $? -ne 0 ]; then
    echo "Maven build failed"
    exit 1
fi

echo "Creating AppImage..."


jpackage \
  --type app-image \
  --name Gliphy \
  --input target \
  --main-jar Gliphy.jar \
  --main-class me.mert.Main \

echo "Done!"