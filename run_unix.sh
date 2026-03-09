#!/bin/bash
echo "============================================"
echo "  MediSphere - Smart Healthcare System"
echo "============================================"
echo "Compiling..."
mkdir -p bin
find src -name "*.java" > sources.txt
javac -d bin -sourcepath src @sources.txt
if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Launching MediSphere..."
    java -cp bin medisphere.MediSphereApp
else
    echo "Compilation failed. Please ensure JDK 8+ is installed."
fi
