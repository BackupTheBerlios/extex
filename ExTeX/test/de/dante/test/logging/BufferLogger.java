/*
 * Copyright (C) 2004 Gerd Neugebauer
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *  
 */
package de.dante.test.logging;

import java.io.File;
import java.util.logging.Level;

import de.dante.extex.logging.Logger;

/*
 * ...
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de"> Gerd Neugebauer </a>
 * 
 * @version $Revision: 1.1 $
 */
public class BufferLogger implements Logger {

    private static boolean logPrefixed = false;

    private static boolean logSevere = true;

    private static boolean logWarning = true;

    private static boolean logInfo = true;

    private static boolean logFine = true;

    private static boolean logFiner = true;

    private static boolean logFinest = true;

    private static boolean logThrowing = true;

    private static boolean logEntering = true;

    private static boolean logExiting = true;

    public static void setProfile(String s) {
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
            case 'p':
                logPrefixed = false;
                break;
            case 'P':
                logPrefixed = true;
                break;
            case 's':
                logSevere = false;
                break;
            case 'S':
                logSevere = true;
                break;
            case 'w':
                logWarning = false;
                break;
            case 'W':
                logWarning = true;
                break;
            case 'i':
                logInfo = false;
                break;
            case 'I':
                logInfo = true;
                break;
            case 'f':
                logFine = false;
                break;
            case 'F':
                logFine = true;
                break;
            case 'e':
                logFiner = false;
                break;
            case 'E':
                logFiner = true;
                break;
            case 'd':
                logFinest = false;
                break;
            case 'D':
                logFinest = true;
                break;
            case 't':
                logThrowing = false;
                break;
            case 'T':
                logThrowing = true;
                break;
            case 'a':
                logEntering = false;
                break;
            case 'A':
                logEntering = true;
                break;
            case 'x':
                logExiting = false;
                break;
            case 'X':
                logExiting = true;
                break;
            }
        }
    }

    /**
     * The field <tt>buffer</tt> ...
     */
    private static StringBuffer buffer = new StringBuffer();

    /**
     * ...
     * 
     * @param name ...
     * @param log ...
     * @param template ...
     * @return ...
     */
    static Logger getLogger(String name, File log, String template) {
        return new BufferLogger();
    }

    /**
     * Creates a new object.
     */
    public BufferLogger() {
        super();
    }

    /**
     * ...
     * 
     * @return
     */
    public static String close() {
        String s = buffer.toString();
        buffer.setLength(0);
        return s;
    }

    /**
     * @see de.dante.extex.logging.Logger#config(java.lang.String)
     */
    public void config(String arg0) {
    }

    /**
     * @see de.dante.extex.logging.Logger#entering(java.lang.String,
     *      java.lang.String)
     */
    public void entering(String arg0, String arg1) {
        if (logEntering) {
            if (logPrefixed) buffer.append("[entering] ");
            buffer.append(arg0);
            buffer.append(" ");
            buffer.append(arg1);
        }
    }

    /**
     * @see de.dante.extex.logging.Logger#entering(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    public void entering(String arg0, String arg1, Object arg2) {
        if (logEntering) {
            if (logPrefixed) buffer.append("[entering] ");
            buffer.append(arg0);
            buffer.append(" ");
            buffer.append(arg1);
            buffer.append(" ");
            buffer.append(arg2.toString());
        }
    }

    /**
     * @see de.dante.extex.logging.Logger#entering(java.lang.String,
     *      java.lang.String, java.lang.Object[])
     */
    public void entering(String arg0, String arg1, Object[] arg2) {
        if (logEntering) {
            if (logPrefixed) buffer.append("[entering] ");
            buffer.append(arg0);
            buffer.append(" ");
            buffer.append(arg1);
            for (int i = 0; i < arg2.length; i++) {
                buffer.append(" ");
                buffer.append(arg2[i].toString());
            }
        }
    }

    /**
     * @see de.dante.extex.logging.Logger#exiting(java.lang.String,
     *      java.lang.String)
     */
    public void exiting(String arg0, String arg1) {
        if (logExiting) {
            if (logPrefixed) buffer.append("[exiting] ");
            buffer.append(arg0);
            buffer.append(" ");
            buffer.append(arg1);
        }
    }

    /**
     * @see de.dante.extex.logging.Logger#exiting(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    public void exiting(String arg0, String arg1, Object arg2) {
        if (logExiting) {
            if (logPrefixed) buffer.append("[exiting] ");
            buffer.append(arg0);
            buffer.append(arg1);
            buffer.append(" ");
            buffer.append(arg2.toString());
        }
    }

    /**
     * @see de.dante.extex.logging.Logger#fine(java.lang.String)
     */
    public void fine(String arg0) {
        if (logFiner) {
            if (logPrefixed) buffer.append("[fine] ");
            buffer.append(arg0);
        }
    }

    /**
     * @see de.dante.extex.logging.Logger#finer(java.lang.String)
     */
    public void finer(String arg0) {
        if (logFiner) {
            if (logPrefixed) buffer.append("[finer] ");
            buffer.append(arg0);
        }
    }

    /**
     * @see de.dante.extex.logging.Logger#finest(java.lang.String)
     */
    public void finest(String arg0) {
        if (logFinest) {
            if (logPrefixed) buffer.append("[finest] ");
            buffer.append(arg0);
        }
    }

    /**
     * @see de.dante.extex.logging.Logger#info(java.lang.String)
     */
    public void info(String arg0) {
        if (logInfo) {
            if (logPrefixed) buffer.append("[info] ");
            buffer.append(arg0);
        }
    }

    /**
     * @see de.dante.extex.logging.Logger#log(java.util.logging.Level,
     *      java.lang.String)
     */
    public void log(Level arg0, String arg1) {
        if (logPrefixed) {
            buffer.append("[");
            buffer.append(arg0);
            buffer.append("] ");
        }
        buffer.append(arg1);
    }

    /**
     * @see de.dante.extex.logging.Logger#log(java.util.logging.Level,
     *      java.lang.String, java.lang.Object)
     */
    public void log(Level arg0, String arg1, Object arg2) {
        if (logPrefixed) {
            buffer.append("[");
            buffer.append(arg0);
            buffer.append("] ");
        }
        buffer.append(arg1);
        buffer.append(" ");
        buffer.append(arg2.toString());
    }

    /**
     * @see de.dante.extex.logging.Logger#log(java.util.logging.Level,
     *      java.lang.String, java.lang.Object[])
     */
    public void log(Level arg0, String arg1, Object[] arg2) {
        if (logPrefixed) {
            buffer.append("[");
            buffer.append(arg0);
            buffer.append("] ");
        }
        buffer.append(arg1);
        for (int i = 0; i < arg2.length; i++) {
            buffer.append(" ");
            buffer.append(arg2[i].toString());
        }
    }

    /**
     * @see de.dante.extex.logging.Logger#log(java.util.logging.Level,
     *      java.lang.String, java.lang.Throwable)
     */
    public void log(Level arg0, String arg1, Throwable arg2) {
        if (logPrefixed) {
            buffer.append("[");
            buffer.append(arg0);
            buffer.append("] ");
        }
        buffer.append(arg1);
        buffer.append(" ");
        buffer.append(arg2.getMessage());
    }

    /**
     * @see de.dante.extex.logging.Logger#severe(java.lang.String)
     */
    public void severe(String arg0) {
        if (logSevere) {
            if (logPrefixed) buffer.append("[severe] ");
            buffer.append(arg0);
        }
    }

    /**
     * @see de.dante.extex.logging.Logger#throwing(java.lang.String,
     *      java.lang.String, java.lang.Throwable)
     */
    public void throwing(String arg0, String arg1, Throwable arg2) {
        if (logThrowing) {
            if (logPrefixed) buffer.append("[throwing] ");
            buffer.append(arg0);
        }
    }

    /**
     * @see de.dante.extex.logging.Logger#warning(java.lang.String)
     */
    public void warning(String arg0) {
        if (logWarning) {
            if (logPrefixed) buffer.append("[warning] ");
            buffer.append(arg0);
        }
    }

}
