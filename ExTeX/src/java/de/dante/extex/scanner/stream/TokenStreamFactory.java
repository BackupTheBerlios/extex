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

import de.dante.util.NotObservableException;
import de.dante.util.Observable;
import de.dante.util.Observer;
import de.dante.util.ObserverList;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.file.FileFinder;
import de.dante.util.file.FileFinderConfigImpl;

/**
 * This is the factory to provide an instance of a TokenStream.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class TokenStreamFactory implements FileFinder, Observable {
    /** The configuration to use */
    private Configuration config;

    /** the observers registered for the "file" event */
    private ObserverList openFileObservers = new ObserverList();

    /** the observers registered for the "stream" event */
    private ObserverList openStreamObservers = new ObserverList();

    /** the observers registered for the "string" event */
    private ObserverList openStringObservers = new ObserverList();

    /** ... */
    private String classname;

    private FileFinder fileFinder;

    /**
     * Creates a new object.
     *
     * @param config the configuration to use
     */
    public TokenStreamFactory(final Configuration config)
                       throws ConfigurationException {
        super();
        this.config    = config;
        this.classname = config.getAttribute("class");
        this.fileFinder = new FileFinderConfigImpl(config);
    }

    /**
     * Provide a new instance of a token stream.
     *
     * @return the new instance
     *
     * @throws ConfigurationInstantiationException ...
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

        openStringObservers.update(this, line);
        return stream;
    }

    /**
     * Provide a new instance of a token stream.
     * 
     * @return the new instance
     * 
     * @throws ConfigurationInstantiationException ...
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

        openFileObservers.update(this, file);
        return stream;
    }

    /**
     * Provide a new instance of a token stream.
     * 
     * @return the new instance
     * 
     * @throws ConfigurationInstantiationException ...
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

        openStreamObservers.update(this, reader);
        return stream;
    }

    /**
     * @see de.dante.util.Observable#registerObserver(java.lang.String, de.dante.util.Observer)
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
     * ...
     * 
     * @return
     */
    public FileFinder getFileFinder() {
        return fileFinder;
    }

    /**
     * ...
     * 
     * @param finder
     */
    public void setFileFinder(final FileFinder finder) {
        fileFinder = finder;
    }
    
    /**
     * @see de.dante.util.file.FileFinder#findFile(java.lang.String, java.lang.String)
     */
    public File findFile(final String name, final String type)
            throws ConfigurationException {
        openFileObservers.update(this,name);
        return fileFinder.findFile(name,type);
    }
}
