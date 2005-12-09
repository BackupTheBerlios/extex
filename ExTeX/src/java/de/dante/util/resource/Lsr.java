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

package de.dante.util.resource;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class creates the output of 'ls -R'.
 *
 * <p>
 * With the method addexcludeDir() it is possible, to add directories, which
 * are excluded from the ls-R list.
 * </p>
 * <p>
 * With the method addexcludeRegExp() it is possible, to add a RegExp, which
 * are excluded an entry in each directory.
 * </p>
 * <p>
 * Files starting with a '.' are ignored!
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.3 $
 */
public class Lsr {

    /**
     * The name of the ls-R file.
     */
    private static final String LS_R = "ls-R";

    /**
     * The base directory for searching.
     */
    private File basedirectory;

    /**
     * A list of directories, which are excluded from the ls-R.
     * <p>Only in the top directory!</p>
     */
    private List excludeDir;

    /**
     * A list of RegExp, which are excluded in each directory.
     */
    private List excludeRegExp;

    /**
     * The level for the directory depth.
     */
    private int level = 0;

    /**
     * Creates a new object.
     * @param theBasedirectory  The base directory for searching
     */
    public Lsr(final File theBasedirectory) {

        basedirectory = theBasedirectory;
        excludeDir = new ArrayList();
        excludeRegExp = new ArrayList();
    }

    /**
     * Add a directory in the exclude list.
     * <p>Only in the top directory!</p>
     *
     * @param name  The name of the directory.
     */
    public void addExcludeDir(final String name) {

        excludeDir.add(name);
    }

    /**
     * Add a RegExp in the exclude list.
     *
     * @param name  The name of the directory.
     */
    public void addExcludeRegExp(final String name) {

        excludeRegExp.add(name);
    }

    /**
     * Print the ls-R-information to the printStream.
     *
     * @param printStream   The result is written to
     *                      the <code>PrintStream</code>.
     * @param directory     The base directory for output.
     * @throws IOException if an io-error occurs.
     */
    private void printLsr(final PrintStream printStream, final File directory)
            throws IOException {

        if (directory != null) {
            level++;
            File[] files = directory.listFiles(new FileFilter() {

                /**
                 * @see java.io.FileFilter#accept(java.io.File)
                 */
                public boolean accept(final File pathname) {

                    // 'ls-R'
                    if (pathname.getName().equals(LS_R)) {
                        return false;
                    }
                    // '.'
                    if (pathname.isFile() && pathname.getName().startsWith(".")) {
                        return false;
                    }
                    // exclude dir
                    if (level == 1
                            && excludeDir.size() > 0
                            && pathname.isDirectory()
                            && Collections.binarySearch(excludeDir, pathname
                                    .getName()) >= 0) {
                        return false;
                    }
                    // exclude regexp
                    for (int i = 0, n = excludeRegExp.size(); i < n; i++) {
                        String regexp = (String) excludeRegExp.get(i);
                        if (regexp != null
                                && pathname.getName().matches(regexp)) {
                            return false;
                        }
                    }
                    return true;
                }
            });

            if (files != null) {
                Arrays.sort(files);
                // replace base dir name with a '.'
                printStream.println(directory.toString().replaceFirst(
                        basedirectory.toString(), ".")
                        + ":");
                for (int i = 0; i < files.length; i++) {
                    printStream.println(files[i].getName());
                }
                printStream.println();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        printLsr(printStream, files[i]);
                    }
                }
            }
            level--;
        }
    }

    /**
     * Print the ls-R-information to the printStream.
     *
     * @param printStream   The result is written to
     *                      the <code>PrintStream</code>.
     * @throws IOException if an io-error occurs.
     */
    public void printLsr(final PrintStream printStream) throws IOException {

        Collections.sort(excludeDir);
        PrintStream out = printStream;
        if (out == null) {
            out = new PrintStream(new FileOutputStream(basedirectory
                    .getAbsolutePath()
                    + File.separator + LS_R));
        }

        printLsr(out, basedirectory);

        out.close();
    }

    /**
     * main.
     *
     * @param args      The command line.
     *                  -excludeDir xxx
     *                  -excludeRegExp xxx
     * @throws IOException if an io-error occurs.
     */
    public static void main(final String[] args) throws IOException {

        if (args.length >= 1) {
            Lsr lsr = new Lsr(new File(args[0]));
            int p = 1;
            while (p < args.length) {
                if (args[p].equalsIgnoreCase("-excludeDir")) {
                    if (p + 1 < args.length) {
                        lsr.addExcludeDir(args[++p]);
                        p++;
                    } else {
                        printCommandLine();
                    }
                } else if (args[p].equalsIgnoreCase("-excludeRegExp")) {
                    if (p + 1 < args.length) {
                        lsr.addExcludeRegExp(args[++p]);
                        p++;
                    } else {
                        printCommandLine();
                    }
                } else {
                    printCommandLine();
                }
            }
            lsr.printLsr(null);
        } else {
            printCommandLine();
        }
    }

    /**
     * print the command line
     */
    private static void printCommandLine() {

        System.err.println("java de.dante.util.resource.Lsr <directory> "
                + "[-excludeDir xxx] [-excludeRegExp xxx] [...]");
        System.exit(1);
    }
}
