/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
 *
 */

package de.dante.extex.scanner.stream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationNoSuchMethodException;
import de.dante.util.configuration.ConfigurationWrapperException;
import de.dante.util.file.FileFinder;
import de.dante.util.observer.NotObservableException;
import de.dante.util.observer.Observable;
import de.dante.util.observer.Observer;
import de.dante.util.observer.ObserverList;

/**
 * This is the factory to provide an instance of a
 * {@link de.dante.extex.scanner.stream.TokenStream TokenStream}.
 * Like any good factory it is controlled by its configuration.
 *
 * <h3>Configuration</h3>
 *
 * <p>
 * Mainly the configuration needs to specify which class to use for the
 * TokenStream. The name of the class is given as the argument "class" as
 * shown below.
 * <pre>
 *   &lt;Scanner class="the.package.TheClass"/&gt;
 * </pre>
 * </p>
 * <p>
 * The class given must implement the interface
 * {@link de.dante.extex.scanner.stream.TokenStream TokenStream}. In addition
 * an appropriate constructor is required:
 * <pre>
 *     public TheClass(Configuration config, Reader reader, Boolean isFile,
 *          final String theSource) throws IOException
 * </pre>
 * </p>
 *
 * <p>
 * In addition to the class for the Token stream the reader class can be
 * specified for the case that reading from a file is requested. In this case
 * the mapping from bytes to characters according to an encoding.
 * The name is given as the argument "reader" as shown below:
 * <pre>
 *   &lt;Scanner class="the.package.TheClass"
 *         reader="another.pack.age.TheReaderClass"/&gt;
 * </pre>
 * </p>
 * <p>
 * Note that the attribute "reader" is optional. If none is given or the value
 * is the empty string then <tt>java.io.InputStreamReader</tt> is used instead.
 * </p>
 *
 * <h3>Observable Events</h3>
 * <p>
 * {@link de.dante.util.observer.Observer Observer}s can be registered for
 * several events:
 * </p>
 * <dl>
 *  <dt><tt>file</tt></dt>
 *  <dd>This event is triggered by the request for a TokenStream fed from a
 *   file. It is defered until the file has been found and opened.
 *   The name of the file is passed as argument to the observer.
 *  </dd>
 *
 *  <dt><tt>reader</tt></dt>
 *  <dd>This event is triggered by the request for a TokenStream fed from a
 *   file where a custom reader is used. This means that the configuration
 *   contains a reader class name. The reader is passed as argument to the
 *   observer.
 *  </dd>
 *
 *  <dt><tt>string</tt></dt>
 *  <dd>This event is triggered by the request for a TokenStream fed from a
 *   String. The string is passed as argument to the observer.
 *  </dd>
 *
 *  <dt><tt>stream</tt></dt>
 *  <dd>This event is triggered by the request for a TokenStream fed from an
 *   arbitrary Reader. The reader is passed as argument to the observer.
 *  </dd>
 * </dl>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.17 $
 */
public class TokenStreamFactory implements Observable {

    /**
     * The constant <tt>CLASS_ATTRIBUTE</tt> contains the name of the
     * attribute used to get the class name.
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * The constant <tt>READER_ATTRIBUTE</tt> contains the name of the
     * attribute used to get the class name for the reader.
     */
    private static final String READER_ATTRIBUTE = "reader";

    /**
     * The field <tt>configuration</tt> contains the configuration for this
     * instance.
     */
    private Configuration configuration;

    /**
     * The field <tt>openFileObservers</tt> contains the observers registered
     * for the "file" event
     */
    private ObserverList openFileObservers = new ObserverList();

    /**
     * The field <tt>openStreamObservers</tt> contains the observers registered
     * for the "stream" event.
     */
    private ObserverList openStreamObservers = new ObserverList();

    /**
     * The field <tt>openReaderObservers</tt> contains the observers registered
     * for the "reader" event.
     */
    private ObserverList openReaderObservers = new ObserverList();

    /**
     * The field <tt>openStringObservers</tt> contains the observers registered
     * for the "string" event.
     */
    private ObserverList openStringObservers = new ObserverList();

    /**
     * The field <tt>constructor</tt> contains the constructor of the method
     * to use.
     */
    private Constructor constructor;

    /**
     * The field <tt>readerConstructor</tt> contains the constructor for the
     * input stream reader.
     */
    private Constructor readerConstructor;

    /**
     * The field <tt>fileFinder</tt> contains the file finder used when trying
     * to read from a file.
     */
    private FileFinder fileFinder = null;

    /**
     * Creates a new object.
     *
     * @param config the configuration to use
     *
     * @throws ConfigurationException in case of an error in the configuration
     */
    public TokenStreamFactory(final Configuration config)
            throws ConfigurationException {

        super();
        configuration = config;
        String classname = config.getAttribute(CLASS_ATTRIBUTE);
        if (classname == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                    config);
        }
        try {
            constructor = Class.forName(classname)
                    .getConstructor(
                                    new Class[]{Configuration.class,
                                            Reader.class, Boolean.class,
                                            String.class});
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationNoSuchMethodException(e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(classname, config);
        }

        classname = config.getAttribute(READER_ATTRIBUTE);
        if (classname == null || classname.equals("")) {
            readerConstructor = null;
        } else {
            try {
                readerConstructor = Class.forName(classname)
                .getConstructor(new Class[]{InputStream.class, String.class});
            } catch (SecurityException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (NoSuchMethodException e) {
                throw new ConfigurationNoSuchMethodException(e);
            } catch (ClassNotFoundException e) {
                throw new ConfigurationClassNotFoundException(classname, config);
            }
        }
    }

    /**
     * Provide a new instance of a token stream.
     *
     * @param line the line of input to read from
     *
     * @return the new instance
     *
     * @throws ConfigurationException in case of an error in the configuration
     */
    public TokenStream newInstance(final String line)
            throws ConfigurationException {

        TokenStream stream = makeTokenStream(new StringReader(line), false, "#");

        try {
            openStringObservers.update(this, line);
        } catch (GeneralException e) {
            throw new ConfigurationWrapperException(e);
        }
        return stream;
    }

    /**
     * Provide a new instance of a token stream.
     *
     * @param fileName the name of the file to read
     * @param fileType the type of the file to read
     * @param encoding the name of the encoding to use
     *
     * @return the new instance
     *
     * @throws ConfigurationException in case of an error in the configuration
     * @throws FileNotFoundException in case that the file could not be opened
     * @throws IOException in case of an IO error of some kind
     */
    public TokenStream newInstance(final String fileName,
            final String fileType, final String encoding)
            throws ConfigurationException, FileNotFoundException, IOException {

        if (fileFinder == null) {
            throw new MissingFileFinderException("");
        }
        File file = fileFinder.findFile(fileName, fileType);

        if (file == null) {
            throw new FileNotFoundException(fileName);
        }

        BufferedInputStream inputStream = new BufferedInputStream(
                new FileInputStream(file));

        Reader reader;

        if (readerConstructor == null) {
            reader = new InputStreamReader(inputStream, encoding);
        } else {
            try {
                reader = (Reader) readerConstructor.newInstance(new Object[]{
                        inputStream, encoding});
                openReaderObservers.update(this, reader);
            } catch (IllegalArgumentException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (InstantiationException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (IllegalAccessException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (InvocationTargetException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (GeneralException e) {
                throw new ConfigurationWrapperException(e);
            }
        }

        TokenStream stream = makeTokenStream(reader, true, fileName);

        try {
            openFileObservers.update(this, fileName);
        } catch (GeneralException e) {
            throw new ConfigurationWrapperException(e);
        }
        return stream;
    }

    /**
     * Provide a new instance of a token stream.
     *
     * @param reader the reader to get new characters from
     *
     * @return the new instance
     *
     * @throws ConfigurationException in case of an error in the configuration
     */
    public TokenStream newInstance(final Reader reader)
            throws ConfigurationException {

        TokenStream stream = makeTokenStream(reader, false, "*");

        try {
            openStreamObservers.update(this, reader);
        } catch (GeneralException e) {
            throw new ConfigurationWrapperException(e);
        }
        return stream;
    }

    /**
     * Try to create a new token stream from the arguments given and the class
     * specified in the configuration.
     *
     * @param reader the reader to get new characters from
     * @param isFile indicator for file streams
     * @param source the description of the input source
     *
     * @return a new token stream
     *
     * @throws ConfigurationInstantiationException in case of an error
     */
    private TokenStream makeTokenStream(final Reader reader,
            final boolean isFile, final String source)
            throws ConfigurationInstantiationException {

        try {
            return (TokenStream) constructor.newInstance(new Object[]{
                    configuration, reader,
                    (isFile ? Boolean.TRUE : Boolean.FALSE), source});
        } catch (IllegalArgumentException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InstantiationException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InvocationTargetException e) {
            throw new ConfigurationInstantiationException(e);
        }
    }

    /**
     * @see de.dante.util.observer.Observable#registerObserver(java.lang.String,
     *      de.dante.util.Observer)
     */
    public void registerObserver(final String name, final Observer observer)
            throws NotObservableException {

        if ("file".equals(name)) {
            openFileObservers.add(observer);
        } else if ("stream".equals(name)) {
            openStreamObservers.add(observer);
        } else if ("string".equals(name)) {
            openStringObservers.add(observer);
        } else if ("reader".equals(name)) {
            openReaderObservers.add(observer);
        } else {
            throw new NotObservableException(name);
        }
    }

    /**
     * Setter for the file finder.
     *
     * @param finder the new file finder
     */
    public void setFileFinder(final FileFinder finder) {

        fileFinder = finder;
    }

    /**
     * @see de.dante.util.file.FileFinder#findFile(java.lang.String,
     *      java.lang.String)
     */
    public File findFile(final String name, final String type)
            throws ConfigurationException {

        try {
            openFileObservers.update(this, name);
        } catch (GeneralException e) {
            throw new ConfigurationWrapperException(e);
        }
        return fileFinder.findFile(name, type);
    }

}