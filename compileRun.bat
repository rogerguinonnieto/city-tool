@echo off
REM filepath: d:\Documentos\GitHub\city-tool\compileRun.bat

REM Execute shutdown.bat
call D:\apache-tomcat-9\bin\shutdown.bat

REM Wait for shutdown to complete
timeout /t 3 /nobreak

REM Remove war file and directory
if exist "D:\apache-tomcat-9\webapps\citytool.war" del "D:\apache-tomcat-9\webapps\citytool.war"
if exist "D:\apache-tomcat-9\webapps\citytool" rmdir /s /q "D:\apache-tomcat-9\webapps\citytool"

REM Run Maven clean package
call mvn clean package

REM Copy the newly built war file to webapps
copy "D:\Documentos\GitHub\city-tool\target\citytool.war" "D:\apache-tomcat-9\webapps\"

REM Execute startup.bat
call D:\apache-tomcat-9\bin\startup.bat

echo Deploy complete!
pause