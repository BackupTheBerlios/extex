@echo off
rem ---------------------------------------------------------------------------
rem  This is the start script for ExTeX
rem ---------------------------------------------------------------------------
rem  Copyright (C) 2006 The ExTeX Group
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

set EXTEX_HOME=.
set LIBDIR="%EXTEX_HOME%\lib"
set LOCALCLASSPATH=%EXTEX_HOME%\develop\lib\junit.jar;%EXTEX_HOME%\target\classes

set LOCALCLASSPATH=%LOCALCLASSPATH%;%EXTEX_HOME%\lib\avalon-framework-4.1.4.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%EXTEX_HOME%\lib\commons-io-dev-20040206.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%EXTEX_HOME%\lib\commons-lang-2.0-dev.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%EXTEX_HOME%\lib\commons-logging-1.0.3.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%EXTEX_HOME%\lib\icu4j_2_8.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%EXTEX_HOME%\lib\jdom.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%EXTEX_HOME%\lib\jel.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%EXTEX_HOME%\lib\log4j-1.2.9.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%EXTEX_HOME%\lib\PDFBox.jar

java -classpath %LOCALCLASSPATH% %*

:end

rem 
