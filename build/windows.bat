@echo off

echo Building jar...

mvn clean package

echo Done building jar

if %ERRORLEVEL% NEQ 0 (
    echo Maven build failed
    exit /b %ERRORLEVEL%
)

echo Creating EXE...
jpackage --input target --name Gliphy --main-jar Gliphy.jar --main-class me.mert.Main --type app-image

echo Done!
pause