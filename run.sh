#!/bin/bash

javac -cp '.:rJava/jri/JRI.jar' *.java
cd rJava/jri
./run Main