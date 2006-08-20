@echo off
rem ---------------------------------------------------------------------------
rem  This is the start script for ExTeX
rem ---------------------------------------------------------------------------
rem  Copyright (C) 2004-2006 The ExTeX Group
rem 
rem  This library is free software; you can redistribute it and/or modify it
rem  under the terms of the GNU Lesser General Public License as published by
rem  the Free Software Foundation; either version 2.1 of the License, or (at
rem  your option) any later version.
rem
rem  This library is distributed in the hope that it will be useful, but
rem  WITHOUT ANY WARRANTY; without even the implied warranty of
rem  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
rem  General Public License for more details.
rem
rem  You should have received a copy of the GNU Lesser General Public License
rem  along with this library; if not, write to the Free Software Foundation,
rem  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
rem 
rem ---------------------------------------------------------------------------

if "%JAVA_HOME%"=="" (
  echo ERROR: JAVA_HOME not found in your environment.
  echo.
  echo Please, set the JAVA_HOME variable in your environment to match the
  echo location of the Java Virtual Machine you want to use.
  goto end
  )

set EXTEX_HOME=$INSTALL_PATH
set LIBDIR=%EXTEX_HOME%\lib
set LOCALCLASSPATH=%EXTEX_HOME%\classes

for %%i in (%LIBDIR%\*.jar) do (
  set LOCALCLASSPATH=%LOCALCLASSPATH%;%%i
)

java -classpath LOCALCLASSPATH de.dante.extex.main.TeX %*

:end

rem 
