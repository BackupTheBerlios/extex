/*
 * Copyright (C) 2003  Gerd Neugebauer
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
import java.lang.reflect.InvocationTargetException;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.NotObservableException;
import de.dante.util.Observable;
import de.dante.util.Observer;
import de.dante.util.ObserverList;
import de.dante.util.StringList;
import de.dante.util.StringListIterator;

/**
 * This is the factory to provide an instance of a TokenStream.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
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

    private FileFinder fileFinder = this;

    /**
     * Creates a new object.
     *
     * @param config the configuration to use
     */
    public TokenStreamFactory(Configuration config)
                       throws ConfigurationException {
        super();
        this.config    = config;
        this.classname = config.getAttribute("class");
    }

    /**
     * Provide a new instance of a token stream.
     *
     * @return the new instance
     */
    public TokenStream newInstance(String line, String encoding)
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
     */
    public TokenStream newInstance(File file, String encoding)
                            throws ConfigurationException, 
                                   IOException, 
                                   FileNotFoundException {
        TokenStream stream;

        try {
            stream = (TokenStream) Class.forName(classname)
                                        .getConstructor(new Class[] {
                                                            File.class,
                                                            String.class
                                                        })
                                        .newInstance(new Object[] {
                                                         file,
                                                         encoding
                                                     });
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();

            if (cause == null) {
                throw new ConfigurationInstantiationException(e);
            }
            else if (cause instanceof FileNotFoundException) {
                throw (FileNotFoundException) (e.getCause());
            }
            else if (cause instanceof IOException) {
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
     */
    public TokenStream newInstance(InputStreamReader reader,
                                   String encoding)
                            throws ConfigurationException, 
                                   IOException {
        TokenStream stream;

        try {
            stream = (TokenStream) Class.forName(classname)
                                        .getConstructor(new Class[] {
                                                            InputStreamReader.class,
                                                            String.class
                                                        })
                                        .newInstance(new Object[] {
                                                         reader,
                                                         encoding
                                                     });
        } catch (Exception e) {
            throw new ConfigurationInstantiationException(e);
        }

        openStreamObservers.update(this, reader);
        return stream;
    }

    /**
     * @see de.dante.util.Observable#registerObserver(java.lang.String, de.dante.util.Observer)
     */
    public void registerObserver(String name, Observer observer)
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
    
    private StringList path = null;
    
    /**
     * @see de.dante.extex.scanner.stream.FileFinder#findFile(java.lang.String, java.lang.String)
     * TODO Aufrufparameter texinputs verwenden
     */
    public File findFile(String name, String type) throws ConfigurationException {
        File file = new File(name);
        if (file.canRead()) return file;
        
        Configuration cfg = config.getConfiguration(type);
        StringListIterator pathIt = cfg.getValues("path").getIterator();
        while (pathIt.hasNext()) {
            String path = pathIt.next();
            StringListIterator extIt = cfg.getValues("extension").getIterator();
            while (extIt.hasNext()) {
                String ext = extIt.next();
                file = new File(path,file+ext);
                if (file.canRead()) return file;
            }
        }
        
        return null;
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
    public void setFileFinder(FileFinder finder) {
        fileFinder = finder;
    }

    /**
     * Setter for path. The given string is splitted at the separator stored in
     * the system property <tt>path.separator</tt>. This is usually the
     * value <tt>:</tt> on Unix systems and <tt>;</tt> on Windows.
     * <p>
     * If this property can not be found then the value <tt>:</tt> is used.
     * </p>
     * 
     * @param path the path to set.
     */
    public void setPath(String path) {
        this.path = new StringList(path, System.getProperty("path.separator",
                                                            ":"));
    }
}
