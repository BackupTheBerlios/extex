/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.main.logging;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * This class provides a means to format the log entries.
 * This implementation simply uses the messages as delivered.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class LogFormatter extends Formatter {

    /**
     * The constant <tt>LINE_LENGTH</tt> contains the target line length for
     * line breaking in the log file.
     */
    private static final int LINE_LENGTH = 80;

    /**
     * The field <tt>col</tt> contains the current column for the next
     * output character.
     */
    private int col;

    /**
     * Creates a new object.
     */
    public LogFormatter() {

        super();
        this.col = 0;
    }

    /**
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    public String format(final LogRecord record) {

        Throwable t = record.getThrown();
        if (t != null) {
            return format(t);
        }

        StringBuffer msg = new StringBuffer(record.getMessage());

        if (msg.length() == 0) {
            return "";
        }
        if (col == 0) {
            if (msg.charAt(0) == '\n' || msg.charAt(0) == '\r') {
                msg.deleteCharAt(0);
            }
            /*
        } else if (msg.charAt(0) != ' ') {
            msg.insert(0, ' ');
            */
        }
        int idx = msg.lastIndexOf("\n");
        if (idx >= 0) {
            col = msg.length() - idx - 1;
        } else {
            col += msg.length();
        }

        if (col >= LINE_LENGTH) {
            //TODO gene: improve formatting
            msg.append('\n');
            col = 0;
        }

        return msg.toString();
    }

    /**
     * Format any throwable into a printable format.
     *
     * @param t the throwable to log
     * @return the printed stack trace
     */
    private String format(final Throwable t) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(os);
        writer.write("\n");
        t.printStackTrace(writer);
        writer.write("\n");
        writer.flush();
        return os.toString();
    }
}