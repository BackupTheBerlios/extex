@echo off
rem ---------------------------------------------------------------------------
rem  This is a script to set the CLASSPATH for ExTeX
rem ---------------------------------------------------------------------------
rem  Copyright (C) 2004-2005 The ExTeX Group
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
rem  @author <a href="mailto:uwe.siart@tum.de">Uwe Siart</a>
rem  @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
rem 
rem ---------------------------------------------------------------------------
set EXTEXPATH=e:\extex\eclipse\ExTeX
set PATH=%EXTEXPATH%\util;%PATH%
cd %EXTEXPATH%
set CLASSPATH=.;%EXTEXPATH%\target\classes\;%EXTEXPATH%\src\de\dante\extex\config\;
for %%i in (%EXTEXPATH%\lib\*.jar) do (
    set CLASSPATH=%CLASSPATH%;%%i
  )
for %%i in (%EXTEXPATH%\lib.develop\*.jar) do (
    set CLASSPATH=%CLASSPATH%;%%i
  )
