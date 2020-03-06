@echo off
setlocal enabledelayedexpansion


set YEAR=%date:~0,4%
set MOUTH=%date:~5,2%
set DAY=%date:~8,2%
set HOUR=%time:~0,2%
set MINUTE=%time:~3,2%
set SECOUND=%time:~6,2%


echo start jeecms-backup...

SET SERVER_NAME=jeecms-backup
SET pid=none
call :getpid pid


if "%1%" == "status" (
	if "%pid%" NEQ "none" (
		echo The %SERVER_NAME% is running
	) else (
		echo The %SERVER_NAME% is stopped
	)

	goto :exit
)

if "%pid%" NEQ "none" (
	echo ERROR: The port 19889 already used^^!
	goto :exit
) 

set logname=%YEAR%_%MOUTH%_%DAY%_%HOUR%_%MINUTE%_%SECOUND%.log
start "" /i /b "javaw" -Dfile.encoding=utf-8 -jar jeecms-backup-x1.0.9.jar >./%logname% 2>&1



:LoopStart
call :getpid pid
if "%pid%" == "none" (
	set /p="."<nul
	goto :LoopStart
) else (
	set pwd= 
	call :getPwd pwd
	echo OK^^!
	echo PID: %pid%
	
	
	echo STDOUT: !pwd!\%logname%
)


:exit
echo.
pause
exit /b 0







:getpid
for /F "tokens=1,2,3,4,5"  %%i in ('netstat -ano') do ( 
	::echo _%%i_  _%%j_  _%%k_  _%%l_  _%%m_
	if "%%j" == "0.0.0.0:19889" (
		if "%%l" == "LISTENING" (
			set %1=%%m
			exit /b 0
		)
	)
)
exit /b 0

:getPwd
set pwd_t=1
for /F "tokens=1 delims= " %%i in ('dir') do ( 
	set pwd_t=%%i

	if "!pwd_t:~1,2!" == ":\" (
		set %1=!pwd_t!
		exit /b 0
	)
)
exit /b 0