call runcrud
if "%ERRORLEVEL%" == "0" goto runurlinbrowser
echo.
echo runcrud.bat causes errors - breaking work
goto fail

:runurlinbrowser
call start chrome http://localhost:8080/crud/v1/task/getTasks
goto end

:fail
echo.
echo There were errors

:end
echo.
echo Work is finished.