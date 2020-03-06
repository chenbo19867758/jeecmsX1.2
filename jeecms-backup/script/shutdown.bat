@echo off

echo stop jeecms-backup...

SET pid=none
call :getpid pid
if "%pid%" == "none" (
	echo ERROR: The jeecms-backup does not started!
	goto :exit
) else (
	taskkill /pid %pid% /f /t >>nul
)



:LoopStart
SET pid_t=none
call :getpid pid_t
if "%pid_t%" NEQ "none" (
	set /p="."<nul
	goto :LoopStart
) else (
	echo OK!
	echo PID: %pid%
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