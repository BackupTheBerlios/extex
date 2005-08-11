/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
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

package de.dante.util.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.dante.extex.documentWriter.OutputStreamFactory;
import de.dante.extex.documentWriter.exception.DocumentWriterException;
import de.dante.extex.documentWriter.exception.OutputStreamOpenException;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingException;

/**
 * This factory creates an output stream from a specification in the
 * configuration.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.14 $
 */
public class OutputFactory extends AbstractFactory
        implements
            OutputStreamFactory {

    /**
     * This class provides a mutable Integer.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.14 $
     */
    private class Int {

        /**
         * The field <tt>value</tt> contains the encapsulated value.
         */
        private int value;

        /**
         * Creates a new object.
         *
         * @param val the initial value
         */
        public Int(final int val) {

            super();
            this.value = val;
        }

        /**
         * Getter for value.
         *
         * @return the value
         */
        public int getValue() {

            return this.value;
        }

        /**
         * Get the current value and increment the counter fur further use.
         *
         * @return the old value
         */
        public int incr() {

            return this.value++;
        }
    }

    /**
     * The constant <tt>ENCODING_ATTRIBUTE</tt> contains the name of the
     * attribute to get the encoding from.
     */
    private static final String ENCODING_ATTRIBUTE = "encoding";

    /**
     * The field <tt>FORMAT_ATTRIBUTE</tt> contains the name of the attribute
     * to get the format for the file name from.
     */
    private static final String FORMAT_ATTRIBUTE = "format";

    /**
     * The constant <tt>PATH_TAG</tt> contains the name of the tag to specify
     * the path.
     */
    private static final String PATH_TAG = "path";

    /**
     * The field <tt>basename</tt> contains the base name for all file names.
     */
    private String basename;

    /**
     * The field <tt>countMap</tt> contains the internal counter for file names.
     */
    private Map countMap = new HashMap();

    /**
     * The field <tt>defaultStream</tt> contains the default output stream to be
     * delivered to the first request for an output stream of the default type.
     */
    private OutputStream defaultStream = null;

    /**
     * The field <tt>destination</tt> contains the name of the destination. This
     * is usally the file name &ndash; or at least the base without extension.
     */
    private String destination;

    /**
     * The field <tt>defaultExtension</tt> contains the default extension.
     */
    private String defaultExtension = null;

    /**
     * The field <tt>outputDirectories</tt> contaoins the list of output
     * directories. The list is tried first to last.
     */
    private transient String[] outputDirectories;

    /**
     * Creates a new object.
     *
     * @param configuration the configuration object for this instance
     * @param outdirs the list of output directories
     * @param basename the base name of the main stream
     *
     * @throws ConfigurationException in case of an configuration error
     */
    public OutputFactory(final Configuration configuration,
            final String[] outdirs, final String basename)
            throws ConfigurationException {

        super();
        if (configuration == null) {
            throw new ConfigurationMissingException("Output");
        }
        configure(configuration);
        this.outputDirectories = outdirs;
        this.basename = basename;
        this.destination = basename;
    }

    /**
     * @see de.dante.extex.documentWriter.OutputStreamFactory#getDestination()
     */
    public String getDestination() {

        return this.destination;
    }

    /**
     * @see de.dante.extex.documentWriter.OutputStreamFactory#getOutputStream()
     */
    public OutputStream getOutputStream() throws DocumentWriterException {

        return getOutputStream(null, defaultExtension);
    }

    /**
     * @see de.dante.extex.documentWriter.OutputStreamFactory#getOutputStream(
     *      java.lang.String)
     */
    public OutputStream getOutputStream(final String type)
            throws DocumentWriterException {

        return getOutputStream(null, type);
    }

    /**
     * Create an output stream of a type.
     * The creation is tried in a number of directories. The first succeeding
     * attempt is returned.
     *
     * @param name the name of the file to open
     * @param type the type of the file
     *
     * @return a stream for the output or <code>null</code> if none could be
     * opened.
     *
     * @throws DocumentWriterException in case of an error
     *
     * @see de.dante.extex.documentWriter.OutputStreamFactory#getOutputStream(
     *      java.lang.String, java.lang.String)
     */
    public OutputStream getOutputStream(final String name, final String type)
            throws DocumentWriterException {

        String format;
        try {
            format = selectConfiguration(type).getAttribute(FORMAT_ATTRIBUTE);
        } catch (ConfigurationException e) {
            throw new DocumentWriterException(e);
        }

        Int iCount = (Int) countMap.get(type);
        if (iCount == null) {
            iCount = new Int(0);
            countMap.put(type, iCount);
        }
        boolean isDefault = false;
        String filename;
        long cnt = iCount.incr();

        if (cnt == 0 && name == null) {
            cnt = 1;
            if (defaultStream != null) {
                return defaultStream;
            }
            isDefault = true;
            filename = basename + (type == null ? "" : "." + type);
            destination = filename;
        } else {
            filename = MessageFormat.format(format, //
                    new Object[]{basename, //
                            (name == null ? "" : name), //
                            new Long(cnt), //
                            (type == null ? "" : "." + type)});
        }

        if (outputDirectories != null) {
            for (int i = 0; i < outputDirectories.length; i++) {
                OutputStream os = openOutputStream(outputDirectories[i],
                        filename, isDefault);
                if (os != null) {
                    return os;
                }
            }
        }

        try {
            Configuration cfg = getConfiguration().getConfiguration(type);
            Iterator iter = cfg.iterator(PATH_TAG);
            while (iter.hasNext()) {
                OutputStream os = openOutputStream((String) (iter.next()),
                        filename, isDefault);
                if (os != null) {
                    return os;
                }
            }
        } catch (Exception e) {
            throw new OutputStreamOpenException(name, e);
        }

        throw new OutputStreamOpenException(name, new FileNotFoundException(
                name));
    }

    /**
     * This method tries to open a new output stream.
     *
     * @param dir the directory or <code>null</code>
     * @param filename the file name
     * @param isDefault the indicator whether the file should be saved
     *
     * @return the output stream or <code>null</code>
     */
    protected OutputStream openOutputStream(final String dir,
            final String filename, final boolean isDefault) {

        if (dir == null) {
            return null;
        }

        try {
            File file = new File(dir, filename);
            OutputStream os = new BufferedOutputStream(new FileOutputStream(
                    file));
            if (isDefault) {
                destination = file.toString();
            }
            return os;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * Setter for defaultStream.
     *
     * @param defaultStream the defaultStream to set
     */
    public void setDefaultStream(final OutputStream defaultStream) {

        this.defaultStream = defaultStream;
    }

    /**
     * @see de.dante.extex.documentWriter.OutputStreamFactory#setExtension(
     *      java.lang.String)
     */
    public void setExtension(final String extension) {

        this.defaultExtension = extension;
    }

}