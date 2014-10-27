@echo off 

REM @author Nils Christian Ehmke

setlocal enabledelayedexpansion

SET JAVAARGS=-Dlog4j.configuration=./log4j.properties -Xms56m -Xmx1024m
SET MAINCLASSNAME=kieker.tools.bridge.cli.CLIServerMain

REM Get the directory of this file and change the working directory to it.
cd %~dp0

REM Set every variable we will need for the execution.
SET BINDIR=%cd%

SET CLASSPATH=%BINDIR%
for /F "delims=" %%i in ('dir /B /S "%BINDIR%\..\lib\*.jar"') do (
SET CLASSPATH=!CLASSPATH!;%%i
)
for /F "delims=" %%i in ('dir /B /S "%BINDIR%\..\dist\*.jar"') do (
SET CLASSPATH=!CLASSPATH!;%%i
)
for /F "delims=" %%i in ('dir /B /S "%BINDIR%\..\examples\JavaEEServletContainerExample\jetty\lib\jetty-*.jar"') do (
SET CLASSPATH=!CLASSPATH!;%%i
)

REM Now start the tool, but don't forget to deliver the parameters.
java %JAVAARGS% -cp "%CLASSPATH%" %MAINCLASSNAME% %*

REM Don't close the window immediately.
@echo on
@PAUSE