/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;

import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationWrapperException;
import de.dante.util.file.FileFinder;
import de.dante.util.file.FileFinderConfigImpl;
import de.dante.util.observer.NotObservableException;
import de.dante.util.observer.Observable;
import de.dante.util.observer.Observer;
import de.dante.util.observer.ObserverList;

/**
 * This is the factory to provide an instance of a TokenStream.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.13 $
 */
public class TokenStreamFactory implements FileFinder, Observable {
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
     * The field <tt>openStringObservers</tt> contains the observers registered
     * for the "string" event.
     */
    private ObserverList openStringObservers = new ObserverList();

    /**
     * The field <tt>classname</tt> contains the ...
     */
    private String classname;

    /**
     * The field <tt>fileFinder</tt> contains the ...
     */
    private FileFinder fileFinder;

    /**
     * Creates a new object.
     *
     * @param config the configuration to use
     *
     * @throws ConfigurationException ...
     */
    public TokenStreamFactory(final Configuration config)
                       throws ConfigurationException {
        super();
        this.classname = config.getAttribute("class");
        this.fileFinder = new FileFinderConfigImpl(config);
    }

    /**
     * Provide a new instance of a token stream.
     *
     * @param line ...
     * @param encoding the name of the encoding to use
     *
     * @return the new instance
     *
     * @throws ConfigurationException ...
     */
    public TokenStream newInstance(final String line, final String encoding)
                            throws ConfigurationException {
        TokenStream stream;

        try {
            stream = (TokenStream) Class.forName(classname)
                                        .getConstructor(new Class[] {
                                                            String.class,
                                                            String.class
                                                        })
                                        .newInstance(new Object[] {
                                                         line,
                                                         encoding
                                                     });
        } catch (Exception e) {
            throw new ConfigurationInstantiationException(e);
        }

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
     * @param file ...
     * @param type ...
     * @param encoding the name of the encoding to use
     *
     * @return the new instance
     *
     * @throws ConfigurationException ...
     * @throws FileNotFoundException ...
     * @throws IOException ...
     */
    public TokenStream newInstance(final String file, final String type,
        final String encoding) throws ConfigurationException, IOException,
        FileNotFoundException {

        TokenStream stream;
        File theFile = fileFinder.findFile(file, type);
        if (theFile == null) {
            throw new FileNotFoundException(file);
        }

        try {
            stream = (TokenStream) Class.forName(classname).getConstructor(
                new Class[]{File.class, String.class}).newInstance(
                new Object[]{theFile, encoding});
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();

            if (cause == null) {
                throw new ConfigurationInstantiationException(e);
            } else if (cause instanceof FileNotFoundException) {
                throw (FileNotFoundException) (e.getCause());
            } else if (cause instanceof IOException) {
                throw (IOException) (e.getCause());
            }

            throw new ConfigurationInstantiationException(e);
        } catch (Exception e) {
            throw new ConfigurationInstantiationException(e);
        }

        try {
            openFileObservers.update(this, file);
        } catch (GeneralException e) {
            throw new ConfigurationWrapperException(e);
        }
        return stream;
    }

    /**
     * Provide a new instance of a token stream.
     *
     * @param reader ...
     * @param encoding the name of the encoding to use
     *
     * @return the new instance
     *
     * @throws ConfigurationException ...
     */
    public TokenStream newInstance(final InputStreamReader reader,
        final String encoding) throws ConfigurationException {

        TokenStream stream;

        try {
            stream = (TokenStream) Class.forName(classname).getConstructor(
                new Class[]{Reader.class, String.class}).newInstance(
                new Object[]{reader, encoding});
        } catch (Exception e) {
            throw new ConfigurationInstantiationException(e);
        }

        try {
            openStreamObservers.update(this, reader);
        } catch (GeneralException e) {
            throw new ConfigurationWrapperException(e);
        }
        return stream;
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
        } else {
            throw new NotObservableException(name);
        }
    }

    /**
     * Getter for the file finder.
     *
     * @return the file finder
     */
    public FileFinder getFileFinder() {
        return fileFinder;
    }

    /**
     * Setter for the file finder
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
