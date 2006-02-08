/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.util.xml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;

/**
 * A writer, which write xml-elements, attributes and so on
 * to an output stream.
 * <p>only xml version 1.0</p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */

public class XMLStreamWriter {

    /**
     * The writer for the output.
     */
    private Writer out;

    /**
     * The encoding.
     */
    private String encoding;

    /**
     * The default name space.
     */
    private String defaultns = null;

    /**
     * Beautify the output?
     */
    private boolean beauty = false;

    /**
     * Indent string.
     */
    private String indent = "   ";

    /**
     * is the last operation a newline?
     */
    private boolean nlset = false;

    /**
     * The buffer size.
     */
    private static final int BUFFERSIZE = 0xffff;

    /**
     * Create a new object.
     *
     * @param sout  The output.
     * @param enc   The encoding
     * @throws IOException if an error occurs.
     */
    public XMLStreamWriter(final OutputStream sout, final String enc)
            throws IOException {

        super();
        out = new BufferedWriter(new OutputStreamWriter(sout, enc), BUFFERSIZE);
        encoding = enc;

    }

    // --------------------------------------------------

    /**
     * Document open?
     */
    private boolean docopen = false;

    /**
     * Write the start of the document.
     * <p>
     * A newline is write after the header, without attend
     * the beauty-flag.
     * </p>
     *
     * @throws IOException if an error occurs.
     */
    public void writeStartDocument() throws IOException {

        out.write("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n");
        docopen = true;
    }

    /**
     * Write the end of the document.
     * <p>It makes a flush()</p>
     * @throws IOException if an error occurs.
     */
    public void writeEndDocument() throws IOException {

        closeElement();
        out.flush();
        if (stack.size() != 0) {
            throw new IOException("invalid struktur: depth=" + stack.size());
        }
    }

    /**
     * Write a comment.
     * @param text  The comment.
     * @throws IOException if an error occurs.
     */
    public void writeComment(final String text) throws IOException {

        closeElement();
        stack.setAppend();
        printIndent();
        out.write("<!-- ");
        out.write(text);
        out.write(" -->");
        printNewLine();
    }

    /**
     * Write a cdata string.
     * @param text  The cdata string.
     * @throws IOException if an error occurs.
     */
    public void writeCDATA(final String text) throws IOException {

        closeElement();
        stack.setAppend();
        printIndent();
        out.write("<![CDATA[\n");
        out.write(text);
        out.write("\n]]>");
        printNewLine();
    }

    /**
     * Write a CDATA string.
     * @param array  The CDATA as array.
     * @throws IOException if an error occurs.
     */
    public void writeCDATA(final byte[] array) throws IOException {

        writeCDATA(new String(array));
    }

    /**
     * Print a newline, if beauty is set.
     * @throws IOException if an error occurs.
     */
    private void printNewLine() throws IOException {

        if (beauty) {
            out.write("\n");
            nlset = true;
        }
    }

    /**
     * Print a indent, if beauty is set.
     * @throws IOException if an error occurs.
     */
    private void printIndent() throws IOException {

        printIndent(stack.size());
    }

    /**
     * Print a indent in a string buffer, if beauty is set.
     * @param buf   The string buffer.
     * @throws IOException if an error occurs.
     */
    private void printIndent(final StringBuffer buf) throws IOException {

        if (beauty) {
            for (int i = 0, cnt = stack.size(); i < cnt; i++) {
                buf.append(indent);
            }
        }
    }

    /**
     * Print a indent, if beauty is set.
     * @param cnt   the count of the depth
     * @throws IOException if an error occurs.
     */
    private void printIndent(final int cnt) throws IOException {

        if (beauty) {
            for (int i = 0; i < cnt; i++) {
                out.write(indent);
            }
        }
    }

    /**
     * Element are opened?
     */
    private boolean elementopen = false;

    /**
     * The stack for the elements.
     */
    private Stack stack = new Stack();

    /**
     * Write the start element to the output.
     * @param element   the element
     * @throws IOException if an error occurs.
     */
    public void writeStartElement(final String element) throws IOException {

        writeStartElement(defaultns, element);
    }

    /**
     * Write the start element to the output.
     * @param ns        the namespace
     * @param element   the element
     * @throws IOException if an error occurs.
     */
    public void writeStartElement(final String ns, final String element)
            throws IOException {

        closeElement();
        if (!nlset) {
            printNewLine();
        }
        printIndent();
        stack.setAppend();
        stack.add(ns, element);
        out.write("<");
        if (ns != null) {
            out.write(ns);
            out.write(':');
        }
        out.write(stack.get());
        elementopen = true;
    }

    /**
     * Close the element.
     * @throws IOException if an error occurs.
     */
    private void closeElement() throws IOException {

        if (elementopen) {
            out.write(">");
            printNewLine();
        }
        elementopen = false;
    }

    /**
     * Write the end element to the output.
     * @throws IOException if an error occurs.
     */
    public void writeEndElement() throws IOException {

        if (stack.size() > 0) {
            if (stack.isAppend()) {
                closeElement();
                if (!nlset) {
                    printNewLine();
                }
                printIndent(stack.size() - 1);
                out.write("</");
                String ns = stack.getNameSpace();
                if (ns != null) {
                    out.write(ns);
                    out.write(':');
                }
                out.write(stack.get());
                out.write(">");
                printNewLine();
            } else {
                out.write("/>");
                elementopen = false;
                nlset = false;
            }
            stack.remove();

        } else {
            throw new IOException("no start element!");
        }
    }

    /**
     * Write a attribute to the element.
     * @param name      The name of the attribute.
     * @param value     The value of the attribute.
     * @throws IOException if an error occurs.
     */
    public void writeAttribute(final String name, final String value)
            throws IOException {

        if (elementopen) {
            out.write(" ");
            out.write(name.trim());
            out.write("=\"");
            out.write(createEntity(value));
            out.write("\"");
        } else {
            throw new IOException("Only after writeStartElement()!");
        }
    }

    /**
     * Write a attribute to the element.
     * @param name      The name of the attribute.
     * @param value     The value of the attribute.
     * @throws IOException if an error occurs.
     */
    public void writeAttribute(final String name, final long value)
            throws IOException {

        writeAttribute(name, String.valueOf(value));
    }

    /**
     * Write a attribute to the element.
     * @param name      The name of the attribute.
     * @param value     The value of the attribute.
     * @throws IOException if an error occurs.
     */
    public void writeAttribute(final String name, final double value)
            throws IOException {

        writeAttribute(name, String.valueOf(value));
    }

    /**
     * Write a attribute to the element.
     * @param name      The name of the attribute.
     * @param value     The value of the attribute.
     * @throws IOException if an error occurs.
     */
    public void writeAttribute(final String name, final boolean value)
            throws IOException {

        writeAttribute(name, String.valueOf(value));
    }

    /**
     * Write a attribute to the element.
     * @param name      The name of the attribute.
     * @param value     The value of the attribute.
     * @throws IOException if an error occurs.
     */
    public void writeAttribute(final String name, final Object value)
            throws IOException {

        writeAttribute(name, value.toString());
    }

    /**
     * Write characters to the output.
     * @param text  The text
     * @throws IOException  if an error occurs.
     */
    public void writeCharacters(final String text) throws IOException {

        closeElement();
        stack.setAppend();
        if (nlset) {
            printIndent();
        }
        out.write(createEntity(text));
        nlset = false;
    }

    /**
     * The length of one entry.
     */
    private static final int ENTRY_LENGTH = 7;

    /**
     * Convert a int-value to a hex string with x digits.
     * @param value     The int value.
     * @param digits    The digits.
     * @return Returns a hex string.
     */
    private String toHexString(final int value, final int digits) {

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            buf.append("0");
        }
        buf.append(Integer.toHexString(value));
        return buf.substring(buf.length() - digits);
    }

    /**
     * Write a byte array with hex values to the output.
     *
     * @param bytes The byte array.
     * @throws IOException if an error occurs.
     */
    public void writeByteArray(final byte[] bytes) throws IOException {

        StringBuffer buf = new StringBuffer(bytes.length * ENTRY_LENGTH);
        for (int i = 0; i < bytes.length; i++) {
            buf.append("0x").append(toHexString(bytes[i], 2)).append(" ");
            if (beauty && i % ENTRY_LINES == ENTRY_LINES - 1) {
                buf.append("\n");
                printIndent(buf);
            }
        }
        writeCharacters(buf.toString());
    }

    /**
     * Entries each line.
     */
    private static final int ENTRY_LINES = 8;

    /**
     * The length of a short value.
     */
    private static final int SHORT_LENGTH = 4;

    /**
     * Write a short array with hex values to the output.
     *
     * @param shorts The short array.
     * @throws IOException if an error occurs.
     */
    public void writeShortArray(final short[] shorts) throws IOException {

        StringBuffer buf = new StringBuffer(shorts.length * ENTRY_LENGTH);

        for (int i = 0; i < shorts.length; i++) {
            buf.append("0x").append(toHexString(shorts[i], SHORT_LENGTH))
                    .append(" ");
            if (beauty && i % ENTRY_LINES == ENTRY_LINES - 1) {
                buf.append("\n");
                printIndent(buf);
            }
        }
        writeCharacters(buf.toString());
    }

    /**
     * Create a new text with entities.
     * @param text  The text.
     * @return Returns the text with entities.
     */
    private String createEntity(final String text) {

        StringBuffer buf = new StringBuffer(text.length());

        for (int i = 0, n = text.length(); i < n; i++) {
            char c = text.charAt(i);
            switch (c) {
                case '<' :
                    buf.append("&lt;");
                    break;
                case '>' :
                    buf.append("&gt;");
                    break;
                case '&' :
                    buf.append("&amp;");
                    break;
                case '"' :
                    buf.append("&quot;");
                    break;
                case '\'' :
                    buf.append("&apos;");
                    break;
                default :
                    buf.append(c);
                    break;
            }
        }

        return buf.toString();
    }

    // --------------------------------------------------

    /**
     * Write a newline to the output.
     * @throws IOException if an error occurs.
     */
    public void newLine() throws IOException {

        out.write('\n');
    }

    /**
     * Flush the buffer to the output.
     * @throws IOException if an error occurs.
     */
    public void flush() throws IOException {

        out.flush();
    }

    /**
     * Close the output.
     * @throws IOException if an error occurs.
     */
    public void close() throws IOException {

        out.close();
        if (stack.size() != 0) {
            throw new IOException("invalid struktur: depth=" + stack.size());
        }

    }

    // --------------------------------------------------
    /**
     * Returns the default namespace.
     * @return Returns the defaultns.
     */
    public String getDefaultNamespace() {

        return defaultns;
    }

    /**
     * Set the default namespace for elements.
     * @param ns The defaultns to set.
     * @throws IOException if an error occurs.
     */
    public void setDefaultNamespace(final String ns) throws IOException {

        if (!docopen) {
            defaultns = ns;
        } else {
            throw new IOException("only before writeStartDocument()!");
        }
    }

    /**
     * Set the beauty.
     * @param b The beauty to set.
     * @throws IOException if an error occurs.
     */
    public void setBeauty(final boolean b) throws IOException {

        if (!docopen) {
            beauty = b;
        } else {
            throw new IOException("only before writeStartDocument()!");
        }
    }

    /**
     * Set the indent.
     * @param i The indent to set.
     * @throws IOException if an error occurs.
     */
    public void setIndent(final String i) throws IOException {

        if (!docopen) {
            if (indent != null) {
                indent = i;
            }
        } else {
            throw new IOException("only before writeStartDocument()!");
        }

    }

    // -------------------------------------

    /**
     * The stack for the elements.
     */
    private class Stack {

        /**
         * The stack.
         */
        private LinkedList istack = new LinkedList();

        /**
         * Returns the size of the stack.
         * @return Returns the size of the stack.
         */
        public int size() {

            return istack.size();
        }

        /**
         * Add a element.
         * @param element   the element
         */
        public void add(final String element) {

            istack.addLast(new Values(element));
        }

        /**
         * Add a element.
         * @param ns        the namespace
         * @param element   the element
         */
        public void add(final String ns, final String element) {

            istack.addLast(new Values(ns, element));
        }

        /**
         * Remove a element.
         * @return Returns the name of the element.
         */
        public String remove() {

            return ((Values) istack.removeLast()).getElement();
        }

        /**
         * Get a element name.
         * @return Returns the name of the element.
         */
        public String get() {

            return ((Values) istack.getLast()).getElement();
        }

        /**
         * Get a element namespace.
         * @return Returns the name of the element.
         */
        public String getNameSpace() {

            return ((Values) istack.getLast()).getNameSpace();
        }

        /**
         * Set the appendvalue.
         */
        public void setAppend() {

            if (size() > 0) {
                ((Values) istack.getLast()).setAppend(true);
            }
        }

        /**
         * Chekc, if the element is in append mode.
         * @return Returns <code>true</code>, if the elements
         * have childrens or text.
         */
        public boolean isAppend() {

            if (size() > 0) {
                return ((Values) istack.getLast()).isAppend();
            }
            return false;
        }

        /**
         * The values.
         */
        public class Values {

            /**
             * The element.
             */
            private String element;

            /**
             * The namespace.
             */
            private String namespace;

            /**
             * append.
             */
            private boolean append;

            /**
             * Create a new object.
             * @param el the element
             */
            public Values(final String el) {

                element = el.trim();
                append = false;
                namespace = null;
            }

            /**
             * Create a new object.
             * @param ns the namespace
             * @param el the element
             */
            public Values(final String ns, final String el) {

                element = el.trim();
                append = false;
                namespace = ns;
            }

            /**
             * Returns the append.
             * @return Returns the append.
             */
            public boolean isAppend() {

                return append;
            }

            /**
             * Set the append.
             * @param app The append to set.
             */
            public void setAppend(final boolean app) {

                append = app;
            }

            /**
             * Returns the element.
             * @return Returns the element.
             */
            public String getElement() {

                return element;
            }

            /**
             * Returns the namespace.
             * @return Returns the namespace.
             */
            public String getNameSpace() {

                return namespace;
            }
        }
    }

}
