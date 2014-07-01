@echo off

REM @author Nils Christian Ehmke

setlocal enabledelayedexpansion

:main
    REM Check if the parameters are correct.
    if "%1"=="" (
		call:print_usage
		GOTO:EOF
	)
	if not exist %1\NUL (
		echo %1 is not a directory
		call:print_usage
		GOTO:EOF
	)
	if "%2"=="" (
		echo Missing file extensions
		call:print_usage
		GOTO:EOF
	)
	
	REM Now get all parameters from %2 on as extensions (SHIFT and %* don't work together very well in a Batch script).
	SET DIRNAME=%1
	SHIFT
	SET EXTS=%1
	
	:loop
		SHIFT
		if NOT "%1"=="" (
			SET EXTS=%EXTS% %1
			GOTO:loop
		)

	REM We want to count the pic and dot-files.
	SET /a PIC_COUNTER=0
	SET /a DOT_COUNTER=0
	
	REM Convert the dot and pic files using the old filenames as base for the new ones.
	IF exist %DIRNAME%\*.pic (
	for /F "delims=" %%i in ('dir /B /S "%DIRNAME%\*.pic"') do (
		for %%j in (%EXTS%) do (
			pic2plot -T %%j "%%i" > "%%i.%%j"
		  if "%%j" == "pdf" (
        pic2plot -T ps "%%i" > "%%i.ps"
        ps2pdf "%%i.ps"
      )
		)
		SET /a PIC_COUNTER=PIC_COUNTER + 1
	)
	)
	
	IF exist %DIRNAME%\*.dot (
	for /F "delims=" %%i in ('dir /B /S "%DIRNAME%\*.dot"') do (
		for %%j in (%EXTS%) do (
			dot -T %%j "%%i" > "%%i.%%j"
		)
		SET /a DOT_COUNTER=DOT_COUNTER + 1
	)
	)
	
	REM Show the user the resulting counters.
	echo. 
	echo Processed !DOT_COUNTER! .dot file(s)
	echo Processed !PIC_COUNTER! .pic file(s)
GOTO:EOF

REM This subfunction shows the user the usage of the batch-script.
:print_usage
    echo. 
	REM Normally we could use %~n0 to show the current file, but due to the fact that we have this scrips as a helper script, we have to add the name manually.
	echo Usage: dotPic-fileConverter.bat ^<output-directory^> ^<file-type-1 ... file-type-N^>
    echo.
	echo Example: dotPic-fileConverter.bat C:\Temp pdf png ps
GOTO:EOF

REM Don't close the window immediately.
@echo on
PAUSE