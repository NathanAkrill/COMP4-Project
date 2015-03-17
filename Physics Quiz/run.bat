@echo off
start .\WebServer\usbwebserver.exe
echo Press Enter when the web server has been loaded
pause >nul
start PhysicsQuiz.jar -classpath .\drivers\mysql-connector-java-5.1.34-bin.jar