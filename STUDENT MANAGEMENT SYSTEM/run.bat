@echo off
echo ==================================================
echo   Building Student Management System (Core Java)
echo ==================================================
if not exist bin (
    mkdir bin
)
javac -d bin src/com/sms/model/Student.java src/com/sms/util/ValidationUtils.java src/com/sms/service/StudentManagementSystem.java src/com/sms/Main.java
if %errorlevel% neq 0 (
    echo Compilation failed. Please check the logs.
    pause
    exit /b %errorlevel%
)
echo Compilation successful.
echo Launching application...
java -cp bin com.sms.Main
pause
