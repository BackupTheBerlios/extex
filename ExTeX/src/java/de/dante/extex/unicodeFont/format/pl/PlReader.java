/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

package de.dante.extex.unicodeFont.format.pl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Reader for a PL file.
 *
 * TODO: incomplete
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 *
 */
public class PlReader {

    /**
     * The pl null command.
     */
    private PlCommand plnull = new PlNULL();

    /**
     * Create a new object.
     *
     * @param in    The input stream.
     * @throws IOException if an IO-error occurred.
     */
    public PlReader(final InputStream in) throws IOException {

        commands = new HashMap();
        plcommands = new HashMap();
        characters = new TreeMap();

        commands.put("VTITLE", new PlVtitle());
        commands.put("CHARACTER", new PlCharacter());
        commands.put("MAP", new PlMap());
        commands.put("SETCHAR", new PlSetChar());

        read(in);
    }

    /**
     * Create a new object.
     *
     * @param file  The file.
     * @throws IOException if an IO-error occurred.
     */
    public PlReader(final File file) throws IOException {

        this(new FileInputStream(file));
    }

    /**
     * Create a new object.
     *
     * @param file  The file.
     * @throws IOException if an IO-error occurred.
     */
    public PlReader(final String file) throws IOException {

        this(new File(file));
    }

    /**
     * The commands.
     */
    private Map commands;

    /**
     * The pl commands.
     */
    private Map plcommands;

    /**
     * The characters.
     */
    private SortedMap characters;

    /**
     * Read the file.
     *
     * @param in    The input.
     * @throws IOException if an IO-error occurred.
     */
    private void read(final InputStream in) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(in,
                "ASCII"));

        commandLoop(reader);

        reader.close();
    }

    /**
     * Loop to read the commands.
     *
     * @param reader    The reader.
     * @throws IOException if an IO-error occurred.
     */
    private void commandLoop(final Reader reader) throws IOException {

        String command;
        while ((command = readCommand(reader)) != null) {

            PlCommand cmd = (PlCommand) commands.get(command);
            if (cmd != null) {
                cmd.execute(reader);
            } else {
                plnull.execute(reader);
            }
        }
    }

    /**
     * Read the command.
     *
     * @param reader    The reader.
     * @return Returns the command.
     * @throws IOException if an IO-error occurred.
     */
    private String readCommand(final Reader reader) throws IOException {

        StringBuffer buf = new StringBuffer();

        boolean start = true;
        int c;
        while ((c = readChar(reader)) != -1) {
            if (start && c == ' ') {
                // ignore space before the command
                continue;
            } else {
                start = false;
            }
            if (c == '(') {
                start = false;
                continue;
            }
            if (c == ' ' || c == ')') {
                break;
            }
            buf.append((char) c);
        }
        String cmd = buf.toString().trim().toUpperCase();
        if (cmd.length() == 0) {
            return null;
        }
        return cmd;
    }

    /**
     * Read a number.
     *
     * @param reader    The reader.
     * @return Returns the number of -1 if not found.
     * @throws IOException if an IO-error occurred.s
     */
    private float readNumber(final Reader reader) throws IOException {

        float f = -1.0f;
        String type = readCommand(reader);
        String value = readCommand(reader);

        if (type.equals("O")) {
            // octal
            f = (int) Integer.parseInt(value, 8);
        } else if (type.equals("C")) {
            // char
            char c = value.charAt(0);
            f = (int) c;
        } else if (type.equals("R")) {
            // real
            f = Float.parseFloat(value);
        }

        return f;
    }

    /**
     * Read the parameter.
     *
     * Read until ')' and brace level is zero.
     *
     * @param reader    The reader.
     * @return Returns the parameter.
     * @throws IOException if an IO-error occurred.
     */
    private String readParameter(final Reader reader) throws IOException {

        StringBuffer buf = new StringBuffer();

        int level = 0;
        int c;
        while ((c = readChar(reader)) != -1) {
            if (c == '(') {
                level++;
            } else if (c == ')' && level > 0) {
                level--;
            } else if (c == ')' && level == 0) {
                break;
            }
            buf.append((char) c);
        }
        return buf.toString().trim();
    }

    /**
     * Read a character from the reader (ignore cr and newline).
     *
     * @param reader    The reader.
     * @return  Returns the character.
     * @throws IOException  if an IO-error occurred.
     */
    private int readChar(final Reader reader) throws IOException {

        int c;
        while ((c = reader.read()) != -1) {
            if (c != '\n' && c != '\r') {
                return c;
            }
            c = ' ';
        }

        return c;
    }

    /**
     * The command NULL.
     */
    private class PlNULL extends AbstractPlCommand {

        /**
         * @see de.dante.extex.unicodeFont.format.pl.PlCommand#execute(java.io.Reader)
         */
        public void execute(final Reader reader) throws IOException {

            param = readParameter(reader);
        }
    }

    /**
     * The command VTITLE.
     */
    private class PlVtitle extends AbstractPlCommand {

        /**
         * @see de.dante.extex.unicodeFont.format.pl.PlCommand#execute(java.io.Reader)
         */
        public void execute(final Reader reader) throws IOException {

            param = readParameter(reader);
        }
    }

    /**
     * The actual character object.
     */
    private Chars actual = null;

    /**
     * The command Character.
     */
    private class PlCharacter extends AbstractPlCommand {

        /**
         * @see de.dante.extex.unicodeFont.format.pl.PlCommand#execute(java.io.Reader)
         */
        public void execute(final Reader reader) throws IOException {

            actual = new Chars();
            param = readParameter(reader);
            StringReader r = new StringReader(param);
            int ch = (int) readNumber(r);
            actual.setCh(ch);
            characters.put(new Integer(ch), actual);
            commandLoop(r);
            actual = null;
        }

    }

    /**
     * The command Map.
     */
    private class PlMap extends AbstractPlCommand {

        /**
         * @see de.dante.extex.unicodeFont.format.pl.PlCommand#execute(java.io.Reader)
         */
        public void execute(final Reader reader) throws IOException {

            param = readParameter(reader);
            StringReader r = new StringReader(param);
            commandLoop(r);
        }
    }

    /**
     * The command setchar.
     */
    private class PlSetChar extends AbstractPlCommand {

        /**
         * The character.
         */
        private int ch;

        /**
         * @see de.dante.extex.unicodeFont.format.pl.PlCommand#execute(java.io.Reader)
         */
        public void execute(final Reader reader) throws IOException {

            param = readParameter(reader);
            StringReader r = new StringReader(param);
            ch = (int) readNumber(r);
            if (actual != null) {
                actual.setMapch(ch);
            }
        }

        /**
         * Returns the ch.
         * @return Returns the ch.
         */
        public int getCh() {

            return ch;
        }

    }

    /**
     * Returns the characters.
     * @return Returns the characters.
     */
    public SortedMap getCharacters() {

        return characters;
    }

    /**
     * Returns the plcommands.
     * @return Returns the plcommands.
     */
    public Map getPlcommands() {

        return plcommands;
    }

    /**
     * Container for a character.
     */
    public static class Chars {

        /**
         * The character.
         */
        private int ch = -1;

        /**
         * The map character.
         */
        private int mapch = -1;

        /**
         * Returns the ch.
         * @return Returns the ch.
         */
        public int getCh() {

            return ch;
        }

        /**
         * The ch to set.
         * @param c The ch to set.
         */
        public void setCh(final int c) {

            ch = c;
        }

        /**
         * Returns the mapch.
         * @return Returns the mapch.
         */
        public int getMapch() {

            return mapch;
        }

        /**
         * The mapch to set.
         * @param c The mapch to set.
         */
        public void setMapch(final int c) {

            mapch = c;
        }
    }

}
