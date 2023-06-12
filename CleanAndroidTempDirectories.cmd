@echo off
REM set arg1=%%1
REM set arg2=%%2
REM echo %%arg1%%
REM echo %%arg2%%

title Clean Temps Android Proyects
color 09
echo .................................................
echo. 
echo. 
echo Eliminando Carpeta(s) build
FOR /d /r . %%d IN (build) DO @IF EXIST "%%d" rd /s /q "%%d"
echo. 
echo. 
color 06
echo Eliminando Carpeta(s) .gradle
FOR /d /r . %%d IN (.gradle) DO @IF EXIST "%%d" rd /s /q "%%d"
echo. 
echo.
color 02
echo Eliminando Carpeta(s) .idea
FOR /d /r . %%d IN (.idea) DO @IF EXIST "%%d" rd /s /q "%%d"
echo. 
echo. 
color 04
echo Eliminando Carpeta(s) .cxx
FOR /d /r . %%d IN (.cxx) DO @IF EXIST "%%d" rd /s /q "%%d"
echo. 
echo. 
echo. 
echo .................................................
echo. 
echo.  
pause > nul
