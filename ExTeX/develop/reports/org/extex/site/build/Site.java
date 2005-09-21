/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package org.extex.site.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains a generator for the web site.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Site {

    /**
     * The field <tt>force</tt> contains the indicator that all files have to be
     * processed &ndash; needed or not.
     */
    private static boolean force = false;

    /**
     * The field <tt>logger</tt> contains the logger.
     */
    private static final Logger logger = Logger.getLogger(Site.class.getName());

    /**
     * Copy a file to a new destination.
     *
     * @param from existing file
     * @param to new file
     *
     * @throws IOException in case of an error
     */
    private static void copy(final File from, final File to) throws IOException {

        InputStream in = new FileInputStream(from);
        OutputStream out = new FileOutputStream(to);

        byte[] buf = new byte[4096];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * This method terminates the program. If the argument is <code>null</code>
     * then the termination indicates success. Otherwise the message is printed
     * as error message ans failure is signaled.
     *
     * @param message the message to print or <code>null</code> for none
     */
    private static void exit(final String message) {

        if (message != null) {
            logger.severe(message);
            System.exit(1);
        } else {
            System.exit(0);
        }
    }

    /**
     * The command line interface.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        String directory = ".";
        String destination = "tmp";
        logger.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new Formatter() {

            /**
             * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
             */
            public String format(final LogRecord record) {

                return record.getMessage() + "\n";
            }

        });
        logger.addHandler(handler);
        logger.setLevel(Level.WARNING);

        int i = 0;

        for (; i < args.length; i++) {
            String a = args[i];
            if (a.length() < 2) {
                exit("argument too short: " + a);
            } else if ("-source".startsWith(a)) {
                if (++i < args.length) {
                    directory = args[i];
                }
            } else if ("-output".startsWith(a)) {
                if (++i < args.length) {
                    destination = args[i];
                }
            } else if ("-verbose".startsWith(a)) {
                logger.setLevel(Level.INFO);
            } else if ("-force".startsWith(a)) {
                force = true;
            } else if ("-help".startsWith(a)) {
                System.out.println("Options:\n" + //
                        "\t-source <dir>\n" + //
                        "\t-output <dir>\n" + //
                        "\t-force\n" + //
                        "\t-verbose\n" + //
                        "\t-source\n");
                exit(null);
            } else {
                exit("unknown argument: " + a);
            }
        }

        try {
            traverse(new File(directory), new File(destination), null);
        } catch (Exception e) {
            exit(e.getMessage());
        }

        exit(null);
    }

    /**
     * Process a directory
     *
     * @param dir the directory to process
     */
    private static void processDir(final File dir) {

    }

    /**
     * Process the current file according to its type.
     *
     * @param in the input file
     * @param out the output file
     * @param site the current site
     *
     * @throws IOException in case something went wrong
     */
    private static void processFile(final File in, final File out,
            final Site site) throws IOException {

        String filename = in.getName();
        if (filename.endsWith("~") || filename.endsWith(".bak")
                || filename.startsWith(".")) {
            return;
        }

        if (filename.endsWith(".html")) {
            logger.info("processing " + in + " to " + out);
            processHtml(in, out, site);

        } else if (force || !out.exists()
                || out.lastModified() < in.lastModified()) {
            logger.info("copying " + in + " to " + out);
            copy(in, out);
        }

    }

    /**
     * Process a HTML file.
     *
     * @param in the input file
     * @param out the output file
     * @param site the current site
     */
    private static void processHtml(final File infile, final File outfile,
            final Site site) throws IOException {

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outfile);

            PrintStream printStream = new PrintStream(out);
            processHtml(infile, printStream, site);
            printStream.close();

        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * Process a HTML file.
     *
     * @param from
     * @param stream
     * @param site the current site
     *
     * @throws FileNotFoundException in case that the file could ot be opened
     */
    private static void processHtml(final File from, final PrintStream stream,
            final Site site) throws FileNotFoundException {

        String buffer = new String(readFile(from));

        Date now = new Date();
        Matcher matcher;

        buffer = Pattern.compile("</head>").matcher(buffer).replaceAll(
                site.find(".headEnd") + "</head>");

        buffer = Pattern.compile("<body>").matcher(buffer).replaceAll(
                "<body>" + site.find(".bodyStart"));

        buffer = Pattern.compile("</body>").matcher(buffer).replaceAll(
                site.find(".bodyEnd") + "</body>");

        matcher = Pattern.compile("<navigation\\s*/>").matcher(buffer);
        if (matcher.find()) {
            buffer = matcher.replaceAll(site.find(".navigation"));
        }

        matcher = Pattern.compile("<info\\s*/>").matcher(buffer);
        if (matcher.find()) {
            buffer = matcher.replaceAll(site.find(".info"));
        }

        matcher = Pattern.compile("<tabs\\s*/>").matcher(buffer);
        if (matcher.find()) {
            buffer = matcher.replaceAll(site.find(".tabs"));
        }

        buffer = Pattern.compile("&top;").matcher(buffer).replaceAll(
                site.getTop());

        buffer = Pattern.compile("&day;").matcher(buffer).replaceAll(
                new SimpleDateFormat("d").format(now));

        buffer = Pattern.compile("&month;").matcher(buffer).replaceAll(
                new SimpleDateFormat("M").format(now));

        buffer = Pattern.compile("&hour;").matcher(buffer).replaceAll(
                new SimpleDateFormat("h").format(now));

        buffer = Pattern.compile("&min;").matcher(buffer).replaceAll(
                new SimpleDateFormat("m").format(now));

        buffer = Pattern.compile("&sec;").matcher(buffer).replaceAll(
                new SimpleDateFormat("s").format(now));

        buffer = Pattern.compile("&year;").matcher(buffer).replaceAll(
                new SimpleDateFormat("yyyy").format(now));

        buffer = Pattern.compile("&TeX;").matcher(buffer).replaceAll("TeX");

        buffer = Pattern.compile("&LaTeX;").matcher(buffer).replaceAll("LaTeX");

        buffer = Pattern.compile("&ExTeX;").matcher(buffer).replaceAll("ExTeX");

        buffer = Pattern.compile("<logo>TeX</logo>").matcher(buffer)
                .replaceAll("TeX");

        buffer = Pattern.compile("<logo>LaTeX</logo>").matcher(buffer)
                .replaceAll("LaTeX");

        buffer = Pattern.compile("<logo>ExTeX</logo>").matcher(buffer)
                .replaceAll("ExTeX");

        stream.println(buffer);
    }

    /**
     * Read the contents of a file into memory.
     *
     * @param from the file to read
     *
     * @return the contents of the file
     *
     * @throws FileNotFoundException in case that the file could ot be opened
     */
    private static char[] readFile(final File from)
            throws FileNotFoundException {

        logger.fine(" [" + from.toString() + "]");
        Reader in = new FileReader(from);

        char[] ba = new char[(int) from.length() + 1];
        try {
            in.read(ba);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.warning(e.getMessage());
            }
        }
        return ba;
    }

    /**
     * Traverse a directory hierarchy and perform all necessary steps.
     *
     * @param dir the current directory
     * @param out the output file
     * @param parent the parent site
     *
     * @throws IOException in case something went wrong
     */
    private static void traverse(final File dir, final File out,
            final Site parent) throws IOException {

        String dirname = dir.getName();
        if (dirname.equals("CVS") || dirname.startsWith(".")) {
            return;
        }

        if (!out.exists()) {
            logger.info("creating directory " + out.toString());
            if (!out.mkdir()) {
                exit("mkdir failed: " + out.toString());
            }
        } else if (!out.isDirectory()) {
            exit("destination must be a directory: " + out.toString());
        }

        Site here = new Site(parent, dir);
        File[] listFiles = dir.listFiles();

        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].isDirectory()) {
                traverse(listFiles[i], new File(out, listFiles[i].getName()),
                        here);
            }
        }

        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].isDirectory()) {
                processDir(listFiles[i]);
            } else if (listFiles[i].isFile()) {
                processFile(listFiles[i],
                        new File(out, listFiles[i].getName()), here);
            }
        }
    }

    /**
     * The field <tt>dir</tt> contains the current directory.
     */
    private File dir;

    /**
     * The field <tt>parent</tt> contains the parent site.
     */
    private Site parent;

    /**
     * The field <tt>top</tt> contains the relative path to the top.
     */
    private String top = "";

    /**
     * Creates a new object.
     *
     * @param parent the parent site or <code>null</code> for the top
     * @param dir the associated directory
     */
    protected Site(final Site parent, final File dir) {

        super();
        this.parent = parent;
        this.dir = dir;
        top = (parent == null ? "" : parent.getTop() + "../");
    }

    /**
     * Find a file here or up the hierarchy and return its contents.
     *
     * @param name the file to find
     *
     * @return the contents
     *
     * @throws FileNotFoundException in case that the file could ot be opened
     */
    public String find(final String name) throws FileNotFoundException {

        File file = new File(dir, name);
        if (file.exists()) {
            return new String(readFile(file));
        }
        if (parent != null) {
            return parent.find(name);
        }
        throw new FileNotFoundException(name);
    }

    /**
     * Getter for top.
     *
     * @return the top
     */
    public String getTop() {

        return top;
    }

    /**
     * Setter for parent.
     *
     * @param parent the parent to set
     */
    public void setParent(Site parent) {

        this.parent = parent;
    }

}
