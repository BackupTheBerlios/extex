/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.util.resource;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * This class creates the output of "ls -R".
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.2 $
 */
public class Lsr {
    /**
     * The base directory for searching.
     *
     */
    private File basedirectory;

    /**
     * Creates a new <code>Lsr</code> instance.
     *
     * @param theBasedirectory basedirectory for searching
     */
    public Lsr(final File theBasedirectory) {
        basedirectory = theBasedirectory;
    }

    /**
     * Print the lsr-information to printStream.
     *
     * @param printStream the result ist written to this <code>PrintStream</code>
     * @param directory base directory for output
     */
    public void printLsr(final PrintStream printStream, final File directory) {
        File[] files = directory.listFiles();
        Arrays.sort(files);

        if (files != null) {
            printStream.println(directory.toString() + ":");
            for (int i = 0; i < files.length; i++) {
                //LS-R if (files[i].isFile()) {
                printStream.println(files[i].getName());
                //LS-R}
            }
            printStream.println();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    printLsr(printStream, files[i]);
                }
            }
        }
    }


    /**
     * Print the lsr-information to printStream.
     *
     * @param printStream the result ist written to this <code>PrintStream</code>
     */
    public void printLsr(final PrintStream printStream) {
        printLsr(printStream, basedirectory);
    }


    /**
     * Main entrance code for testing.
     *
     * @param args arguments to main function
     */
    public static void main(final String[] args) {
        //LS-R Lsr lsr = new Lsr(new File(System.getProperty("user.dir")));
        Lsr lsr = new Lsr(new File("."));

        lsr.printLsr(System.out);
    }
}
