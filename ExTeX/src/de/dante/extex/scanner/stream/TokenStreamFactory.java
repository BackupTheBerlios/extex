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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
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
import de.dante.util.observer.NotObservableException;
import de.dante.util.observer.Observable;
import de.dante.util.observer.Observer;
import de.dante.util.observer.ObserverList;
import de.dante.util.resource.ResourceFinder;

/**
 * This is the factory to provide an instance of a
 * {@link de.dante.extex.scanner.stream.TokenStream TokenStream}.
 * Like any good factory it is controlled by its configuration.
 *
 * <h3>Configuration</h3>
 *
 * <p>
 * Mainly the configuration needs to specify which class to use for the
 * TokenStream. The name of the class is given as the argument <tt>class</tt>
 * as shown below.
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
 * If the Token stream is fed from a file then the additional parameter
 * <tt>buffersize</tt> is taken into account. This parameter is optional.
 * Its usage can look as follows:
 * <pre>
 *   &lt;Scanner class="the.package.TheClass"
 *         buffersize="0"/&gt;
 * </pre>
 * The value given is a number. If this number is positive then it is
 * interpreted as the size of the buffer for the file reading operation.
 * If it is 0 or empty then no buffer will be used.
 * If it is negative, then the default buffer size will be used.
 * </p>
 *
 * <p>
 * In addition to the class for the Token stream the reader class can be
 * specified for the case that reading from a file is requested. In this case
 * the mapping from bytes to characters according to an encoding.
 * The name is given as the parameter <tt>reader</tt> as shown below:
 * <pre>
 *   &lt;Scanner class="the.package.TheClass"
 *         reader="another.pack.age.TheReaderClass"/&gt;
 * </pre>
 * </p>
 * <p>
 * Note that the attribute <tt>reader</tt> is optional. If none is given or the
 * value is the empty string then <tt>java.io.InputStreamReader</tt> is used
 * instead.
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
 *  <dd>This event is triggered by the request for a TokenStream fed from an
 *   arbitrary Reader. The reader is passed as argument to the observer.
 *  </dd>
 *
 *  <dt><tt>string</tt></dt>
 *  <dd>This event is triggered by the request for a TokenStream fed from a
 *   String. The string is passed as argument to the observer.
 *  </dd>
 * </dl>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.21 $
 */
public class TokenStreamFactory implements Observable {

    /**
     * The constant <tt>CLASS_ATTRIBUTE</tt> contains the name of the
     * attribute used to get the class name.
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * The field <tt>bufferSize</tt> contains the desired size for the input
     * buffer. Negative values mean that the default size should be used. If the
     * vakue is zero then nol buffer at all should be used.
     */
    private int bufferSize = -1;

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
     * The field <tt>options</tt> contains the ...
     */
    private TokenStreamOptions options;

    /**
     * The field <tt>readerConstructor</tt> contains the constructor for the
     * reader variant.
     */
    private Constructor readerConstructor;

    /**
     * The field <tt>resourceFinder</tt> contains the file finder used when trying
     * to read from a file.
     */
    private ResourceFinder resourceFinder = null;

    /**
     * The field <tt>streamConstructor</tt> contains the constructor for the
     * file variant.
     */
    private Constructor streamConstructor;

    /**
     * The field <tt>stringConstructor</tt> contains the constructor for the
     * string variant.
     */
    private Constructor stringConstructor;

    /**
     * Creates a new object.
     * @param theConfiguration the configuration to use
     * @param theOptions TODO
     *
     * @throws ConfigurationException in case of an error in the configuration
     */
    public TokenStreamFactory(final Configuration theConfiguration,
            final TokenStreamOptions theOptions) throws ConfigurationException {

        super();
        this.configuration = theConfiguration;
        this.options = theOptions;
        String classname = theConfiguration.getAttribute(CLASS_ATTRIBUTE);
        if (classname == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                    theConfiguration);
        }
        try {
            readerConstructor = Class.forName(classname).getConstructor(
                    new Class[]{Configuration.class, TokenStreamOptions.class,
                            Reader.class, Boolean.class, String.class});
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationNoSuchMethodException(e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(classname,
                    theConfiguration);
        }

        try {
            stringConstructor = Class.forName(classname).getConstructor(
                    new Class[]{Configuration.class, TokenStreamOptions.class,
                            String.class, String.class});
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationNoSuchMethodException(e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(classname,
                    theConfiguration);
        }

        try {
            streamConstructor = Class.forName(classname).getConstructor(
                    new Class[]{Configuration.class, TokenStreamOptions.class,
                            InputStream.class, String.class, String.class});
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationNoSuchMethodException(e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(classname,
                    theConfiguration);
        }
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

        TokenStream stream;
        try {

            stream = (TokenStream) readerConstructor.newInstance(new Object[]{
                    configuration, options, reader, Boolean.FALSE, "*"});

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
        return stream;
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

        TokenStream stream;
        try {

            stream = (TokenStream) stringConstructor.newInstance(new Object[]{
                    configuration, options, line, "#"});

            openStringObservers.update(this, line);

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
     */
    public TokenStream newInstance(final String fileName,
            final String fileType, final String encoding)
            throws ConfigurationException,
                FileNotFoundException {

        if (resourceFinder == null) {
            throw new MissingResourceFinderException("");
        }
        InputStream stream = resourceFinder.findResource(fileName, fileType);

        if (stream == null) {
            throw new FileNotFoundException(fileName);
        }

        TokenStream tStream;
        try {
            tStream = (TokenStream) streamConstructor.newInstance(new Object[]{
                    configuration, options, stream, fileName, encoding});

        } catch (IllegalArgumentException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InstantiationException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InvocationTargetException e) {
            throw new ConfigurationInstantiationException(e);
        }

        try {
            openFileObservers.update(this, fileName);
        } catch (GeneralException e) {
            throw new ConfigurationWrapperException(e);
        }
        return tStream;
    }

    /**
     * @see de.dante.util.observer.Observable#registerObserver(java.lang.String,
     *      de.dante.util.observer.Observer)
     */
    public void registerObserver(final String name, final Observer observer)
            throws NotObservableException {

        if ("file".equals(name)) {
            openFileObservers.add(observer);
        } else if ("string".equals(name)) {
            openStringObservers.add(observer);
        } else if ("reader".equals(name)) {
            openReaderObservers.add(observer);
        } else {
            throw new NotObservableException(name);
        }
    }

    /**
     * Setter for options.
     *
     * @param options the options to set.
     */
    public void setOptions(TokenStreamOptions options) {

        this.options = options;
    }

    /**
     * Setter for the file finder.
     *
     * @param finder the new file finder
     */
    public void setResourceFinder(final ResourceFinder finder) {

        this.resourceFinder = finder;
    }
}