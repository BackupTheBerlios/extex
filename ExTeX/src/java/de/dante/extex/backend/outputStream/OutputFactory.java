/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.backend.outputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.dante.extex.backend.documentWriter.exception.DocumentWriterException;
import de.dante.extex.backend.documentWriter.exception.OutputStreamOpenException;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This factory creates an output stream from a specification in the
 * configuration.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class OutputFactory extends AbstractFactory
        implements
            OutputStreamFactory {

    /**
     * This class provides a mutable Integer.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.3 $
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
         * Get the current value and increment the counter for further use.
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
     * The field <tt>continuousNumbering</tt> contains the indicator for
     * omitting the special treatment of the first stream.
     */
    private boolean continuousNumbering = false;

    /**
     * The field <tt>countMap</tt> contains the internal counter for file names.
     */
    private Map countMap = new HashMap();

    /**
     * The field <tt>defaultExtension</tt> contains the default extension.
     */
    private String defaultExtension = null;

    /**
     * The field <tt>defaultStream</tt> contains the default output stream to be
     * delivered to the first request for an output stream of the default type.
     */
    private OutputStream defaultStream = null;

    /**
     * The field <tt>handlers</tt> contains the registered handlers.
     */
    private Map handlers = null;

    /**
     * The field <tt>observers</tt> contains the list of registered observers.
     */
    private List observers = null;

    /**
     * The field <tt>outputDirectories</tt> contains the list of output
     * directories. The list is tried first to last.
     */
    private transient String[] outputDirectories;

    /**
     * Creates a new object.
     *
     * @param outdirs the list of output directories
     * @param basename the base name of the main stream
     *
     * @throws ConfigurationException in case of an configuration error
     */
    public OutputFactory(final String[] outdirs, final String basename)
            throws ConfigurationException {

        super();
        this.outputDirectories = outdirs;
        this.basename = basename;
    }

    /**
     * Create an output stream of a certain type.
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
     * @see de.dante.extex.backend.outputStream.OutputStreamFactory#getOutputStream(
     *      java.lang.String,
     *      java.lang.String)
     */
    public OutputStream getOutputStream(final String name, final String type)
            throws DocumentWriterException {

        OutputStream stream = makeOutputStream(name, type);

        if (stream != null && observers != null) {
            int size = observers.size();
            for (int i = 0; i < size; i++) {
                ((OutputStreamObserver) observers.get(i)).update(name, type,
                        stream);
            }
        }
        return stream;
    }

    /**
     * Create an output stream of a certain type.
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
     */
    private OutputStream makeOutputStream(final String name, final String type)
            throws DocumentWriterException {

        String t = (type != null ? type : defaultExtension != null
                ? defaultExtension
                : "");

        if (handlers != null) {
            OutputStreamFactory handler = (OutputStreamFactory) handlers.get(t);
            if (handler != null) {
                return handler.getOutputStream(name, type);
            }
        }

        Configuration c = getConfiguration();
        String format;
        if (c != null) {
            try {
                format = selectConfiguration(t).getAttribute(FORMAT_ATTRIBUTE);
            } catch (ConfigurationException e) {
                throw new DocumentWriterException(e);
            }
        } else {
            format = "{0}{1}{2,number,0000}{3}";
        }

        Int iCount = (Int) countMap.get(t);
        if (iCount == null) {
            iCount = new Int(0);
            countMap.put(t, iCount);
        }
        boolean isDefault = false;
        String filename;
        long cnt = iCount.incr();

        if (!continuousNumbering && cnt == 0 && name == null) {
            cnt = 1;
            if (defaultStream != null) {
                return defaultStream;
            }
            isDefault = true;
            filename = basename + (t == null ? "" : "." + t);
        } else {
            filename = MessageFormat.format(format, //
                    new Object[]{basename, //
                            (name == null ? "" : name), //
                            new Long(cnt), //
                            (t == null ? "" : "." + t)});
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

        if (c != null) {
            try {
                Configuration cfg = c.getConfiguration(t);
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
        }

        return null;
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
            OutputStream os = new NamedOutputStream(file.toString(),
                    new BufferedOutputStream(new FileOutputStream(file)));
            return os;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * @see de.dante.extex.backend.outputStream.OutputStreamFactory#register(
     *      de.dante.extex.backend.outputStream.OutputStreamObserver)
     */
    public void register(final OutputStreamObserver observer) {

        if (observers == null) {
            observers = new ArrayList();
        }
        observers.add(observer);
    }

    /**
     * Register a handler for creation of an output stream of a certain type.
     * If not handler is registered then an appropriate file is opened.
     * <p>
     *  You can register a handler if you want to redirect certain output
     *  streams to different directions. For instance if you want to process
     *  an output stream in memory. For instance this can be the case if an
     *  integrated preview can be used instead of writing the output to a file.
     * </p>
     *
     * @param type the type
     * @param factory the handler to be invoked
     */
    public void register(final String type, final OutputFactory factory) {

        if (type == null) {
            throw new IllegalArgumentException("type");
        }
        if (handlers == null) {
            handlers = new HashMap();
        }
        handlers.put(type, factory);
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
     * @see de.dante.extex.backend.outputStream.OutputStreamFactory#setExtension(
     *      java.lang.String)
     */
    public void setExtension(final String extension) {

        this.defaultExtension = extension;
    }

}
