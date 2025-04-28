@echo off
echo Cleaning and compiling the project...
call mvn clean compile

echo.
echo Starting the server...
start "Chat Server" cmd /k "cd %~dp0 && mvn exec:java -Dexec.mainClass=chatApp.Server"

echo Waiting for server to start...
timeout /t 5

echo.
echo Starting three chat clients...
echo.

echo Starting Client 1...
start "Chat Client 1" cmd /k "cd %~dp0 && mvn javafx:run"

echo Waiting for Client 1 to initialize...
timeout /t 5

echo Starting Client 2...
start "Chat Client 2" cmd /k "cd %~dp0 && mvn javafx:run"

echo Waiting for Client 2 to initialize...
timeout /t 5

echo Starting Client 3...
start "Chat Client 3" cmd /k "cd %~dp0 && mvn javafx:run"

echo.
echo All components should be starting now. Please wait for them to initialize.
echo You can now chat between the clients. 