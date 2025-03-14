#!/bin/bash

# Function to run Maven formatting in modules
run_maven_format() {
    echo "Running Spotless on $1..."
    cd "$1" || exit
    mvn spotless:apply
    cd - > /dev/null || exit
}

# Detect the operating system
if [[ "$OSTYPE" == "linux-gnu"* ]] || [[ "$OSTYPE" == "darwin"* ]]; then
    echo "Detected Linux/macOS"
    run_maven_format "modules/shorturl-api"
    run_maven_format "modules/shorturl-common"
elif [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "cygwin" ]]; then
    echo "Detected Windows - Running PowerShell script..."
    pwsh -Command "& {
        Set-Location -Path 'modules/shorturl-api'
        mvn spotless:apply
        Set-Location -Path '../..'

        Set-Location -Path 'modules/shorturl-common'
        mvn spotless:apply
        Set-Location -Path '../..'
    }"
else
    echo "Unsupported OS"
    exit 1
fi
