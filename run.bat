@echo off
echo =========================================
echo Compiling Bhopal Bus Route Finder...
echo =========================================

if not exist out mkdir out

javac -d out src\main\java\com\busroute\*.java src\main\java\com\busroute\algorithm\*.java src\main\java\com\busroute\cli\*.java src\main\java\com\busroute\graph\*.java src\main\java\com\busroute\model\*.java src\main\java\com\busroute\service\*.java src\main\java\com\busroute\util\*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo.
echo =========================================
echo Starting Application...
echo =========================================
java -cp out com.busroute.Main
pause
