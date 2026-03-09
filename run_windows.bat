@echo off
echo ============================================
echo   MediSphere - Smart Healthcare System
echo ============================================
echo Compiling...
if not exist bin mkdir bin
REM Generate sources.txt if not present
if not exist sources.txt (
    echo Searching for Java files...
    dir /b /s src\*.java > sources.txt
)
javac -d bin -sourcepath src @sources.txt
if %ERRORLEVEL% NEQ 0 goto error
echo Compilation successful!
echo Launching MediSphere...
java -cp bin medisphere.MediSphereApp
goto end
:error
echo Compilation failed. Please check Java is installed (JDK 8+).
pause
:end
