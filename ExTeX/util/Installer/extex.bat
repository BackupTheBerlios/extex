@echo off
rem ---------------------------------------------------------------------------
rem  This is the start script for ExTeX
rem ---------------------------------------------------------------------------
rem  Copyright (C) 2004 The ExTeX Group
rem 
rem  This library is free software; you can redistribute it and/or
rem  modify it under the terms of the GNU Lesser General Public
rem  License as published by the Free Software Foundation; either
rem  version 2.1 of the License, or (at your option) any later version.
rem 
rem  This library is distributed in the hope that it will be useful,
rem  but WITHOUT ANY WARRANTY; without even the implied warranty of
rem  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
rem  Lesser General Public License for more details.
rem 
rem  You should have received a copy of the GNU Lesser General Public
rem  License along with this library; if not, write to the Free Software
rem  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
rem 
rem ---------------------------------------------------------------------------

if "%JAVA_HOME%"=="" (
  echo ERROR: JAVA_HOME not found in your environment.
  echo.
  echo Please, set the JAVA_HOME variable in your environment to match the
  echo location of the Java Virtual Machine you want to use.
  goto end
  )

set LOCALCLASSPATH=.
set EXTEX_HOME="$INSTALL_PATH"
set LIBDIR="%EXTEX_HOME%\lib"

for %%i in (%LIBDIR%\*.jar) do (
    set LOCALCLASSPATH=%LOCALCLASSPATH%;%%i
  )

java -classpath "%LOCALCLASSPATH%" de.dante.extex.ExTeX %*
  
:end

rem 
