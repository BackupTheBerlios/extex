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
 * This implementation makes provisions that the line length of 80 characters
 * is honoured.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
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

        StringBuffer msg = new StringBuffer(record.getMessage());
        StringBuffer out = new StringBuffer();
        int start = 0;

        for (int i = msg.indexOf("\n", start); i >= 0; i = msg.indexOf("\n",
                start)) {
            print(out, msg.subSequence(start, i + 1));
            start = i + 1;
        }
        print(out, msg.subSequence(start, msg.length()));

        Throwable t = record.getThrown();

        if (t != null) {

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(os);
            writer.write("\n");
            t.printStackTrace(writer);
            writer.write("\n");
            writer.flush();
            msg.append(os);
            col = 0;
        }

        return out.toString();
    }

    /**
     * Print a string which may contain a newline at most at the end.
     *
     * @param out the target buffer
     * @param msg the message to process
     */
    private void print(final StringBuffer out, final CharSequence msg) {

        if (msg.length() == 0) {
            return;
        }
        boolean skip = false;

        if (col == 0) {
            char c = msg.charAt(0);
            if (c == '\n' || c == '\r' || c == ' ') {
                skip = true;
            }
        }

        col += msg.length() + (skip ? 1 : 0);

        if (col >= LINE_LENGTH) {
            out.append('\n');
            col = msg.length();
            char c = msg.charAt(0);
            if (c == '\n' || c == '\r' || c == ' ') {
                skip = true;
            }
        }
        if (skip) {
            out.append(msg.subSequence(1, msg.length()));
        } else {
            out.append(msg);
        }
        if (msg.charAt(msg.length() - 1) == '\n') {
            col = 0;
        }
    }

}