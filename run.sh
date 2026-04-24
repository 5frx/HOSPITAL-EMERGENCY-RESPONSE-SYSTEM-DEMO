#!/bin/bash

FX="./lib/javafx-sdk-26/lib"

# Detect OS for native libraries
OS="$(uname -s)"
case "$OS" in
    Darwin*) NATIVE="./lib/javafx-sdk-26/lib" ;;  # macOS
    Linux*)  NATIVE="./lib/javafx-sdk-26/lib" ;;  # Linux
esac

echo "Compiling..."

# Find all .java files
FILES=$(find src -name "*.java")

javac --module-path "$FX" \
      --add-modules javafx.controls,javafx.graphics,javafx.base \
      -encoding UTF-8 \
      -cp "src" \
      -d bin \
      $FILES

if [ $? -eq 0 ]; then
    echo "Launching..."
    java --module-path "$FX" \
         --add-modules javafx.controls,javafx.graphics,javafx.base \
         -cp bin \
         gui.MainApp
else
    echo "Compilation failed."
fi