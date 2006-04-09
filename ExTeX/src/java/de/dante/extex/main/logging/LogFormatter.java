/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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
 * is honored.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
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

        String message = record.getMessage();
        StringBuffer msg = new StringBuffer(message == null ? "" : message);
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
            out.append(os);
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

        CharSequence s = msg;
        int length = msg.length();
        if (length == 0) {
            return;
        }
        boolean skip = false;

        if (col == 0) {
            char c = msg.charAt(0);
            if (c == '\n' || c == '\r' || c == ' ') {
                skip = true;
            }
        }

        if (col + s.length() + (skip ? 1 : 0) >= LINE_LENGTH) {

            s = breakLine(out, s);
            col = 0;
            length = s.length();
            char c = s.charAt(0);
            skip =  (c == '\n' || c == '\r' || c == ' ');
        }

        col += length + (skip ? 1 : 0);

        if (skip) {
            out.append(s.subSequence(1, length));
        } else {
            out.append(s);
        }
        if (msg.charAt(length - 1) == '\n') {
            col = 0;
        }
    }

    /**
     * Find a break point at a white-space and break the line there.
     *
     * @param out the target buffer
     * @param msg the message to process
     *
     * @return the message
     */
    private CharSequence breakLine(final StringBuffer out,
            final CharSequence msg) {

        for (int i = LINE_LENGTH - col - 1; i >= 0; i--) {
            char c = msg.charAt(i);
            if (c == ' ' || c == '\t') {
                out.append(msg.subSequence(0, i));
                out.append('\n');
                return msg.subSequence(i, msg.length());
            }
        }
        out.append('\n');
        return msg;
    }
}