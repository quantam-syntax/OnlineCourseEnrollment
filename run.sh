#!/bin/bash

set -e

# Configuration
JAVAFX_VERSION="17.0.6"
JAVAFX_SDK="javafx-sdk-${JAVAFX_VERSION}"
JAVAFX_ZIP="openjfx-${JAVAFX_VERSION}_linux-x64_bin-sdk.zip"
JAVAFX_URL="https://download2.gluonhq.com/openjfx/${JAVAFX_VERSION}/${JAVAFX_ZIP}"

cd "$(dirname "$0")"

# 1. Download JavaFX SDK if not present
if [ ! -d "$JAVAFX_SDK" ]; then
    echo "JavaFX SDK not found. Downloading..."
    rm -f "$JAVAFX_ZIP" # Remove any partial downloads
    wget "$JAVAFX_URL" -O "$JAVAFX_ZIP"
    
    echo "Extracting JavaFX SDK..."
    unzip -q "$JAVAFX_ZIP"
    rm "$JAVAFX_ZIP"
fi

# 2. Compile Java Source Files
echo "Compiling Java files..."
cd Javasrc
find src -name "*.java" > sources.txt
javac --module-path ../${JAVAFX_SDK}/lib --add-modules javafx.controls,javafx.fxml -d out @sources.txt

if [ $? -ne 0 ]; then
    echo "Compilation failed."
    rm sources.txt
    exit 1
fi
rm sources.txt

# 3. Copy resources (FXML, CSS) to output directory
echo "Copying resources..."
mkdir -p out/ui/view
cp src/ui/view/*.fxml out/ui/view/
cp src/ui/view/*.css out/ui/view/

# 4. Run Application
echo "Starting JavaFX Application..."
java --module-path ../${JAVAFX_SDK}/lib --add-modules javafx.controls,javafx.fxml -cp out ui.MainApp
