/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.language.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import de.dante.extex.language.Language;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.util.framework.Registrar;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.resource.ResourceConsumer;
import de.dante.util.resource.ResourceFinder;

/**
 * This class manages the <code>Language</code>s. It is a container
 * which can be asked to provide an appropriate instance. This instance is
 * either taken from existing instances or a new instance is created. Since
 * at the time of creation it can not be decided whether a new one should be
 * used or an existing one should be loaded a future object is returned
 * which enables us to postpone the operation until the decision can be made.
 * <p>
 * The future object invokes one of th methods of
 * {@link LanguageCreator LanguageCreator}. In this case we know whether to
 * create or load the language. Thus the appropriate operation is performed and
 * the resulting language is put into the map overwriting the future object.
 * </p>
 * <p>
 *  ...
 * </p>
 *
 * <h2>Configuration</h2>
 *
 * This instance is configurable. The configuration is used to select the
 * appropriate class and optional parameters for a requested instance. In this
 * respect this class makes best use of the infrastructure of the
 * {@link de.dante.util.framework.AbstractFactory AbstractFactory}.
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class LoadingLanguageManager extends BaseLanguageManager
        implements
            LanguageCreator,
            ResourceConsumer {

    /**
     * The constant <tt>NON_LOADABLE_LANGUAGE_PATTERN</tt> contains the patter
     * to detect languages which should not be handled via external resources.
     * Currently this value detects purely numerical names.
     */
    private static final String NON_LOADABLE_LANGUAGE_PATTERN = "^\\d*$";

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2006L;

    /**
     * The field <tt>TABLE_EXTENSION</tt> contains the extension for language
     * files.
     */
    private static final String TABLE_EXTENSION = ".lfm";

    /**
     * The constant <tt>VERSION</tt> contains the version id to be written into
     * the external file.
     */
    private static final String VERSION = "1.0";

    /**
     * The field <tt>finder</tt> contains the resource finder to search for
     * language files.
     */
    private transient ResourceFinder finder;

    /**
     * Creates a new object.
     */
    public LoadingLanguageManager() {

        super();
    }

    /**
     * @see de.dante.extex.language.impl.BaseLanguageManager#createLanguage(
     *      java.lang.String)
     */
    protected Language createLanguage(final String name) {

        return new FutureLanguage(name, this);
    }

    /**
     * @see de.dante.extex.language.impl.LanguageCreator#createLanguageInstance(
     *      java.lang.String)
     */
    public Language createLanguageInstance(final String name)
            throws HyphenationException {

        Language language;
        try {
            language = super.createLanguage(name);
        } catch (ConfigurationException e) {
            throw new HyphenationException(e);
        }
        language.setName(name);
        getTables().put(name, language);
        return language;
    }

    /**
     * @see de.dante.extex.language.impl.LanguageCreator#loadLanguageInstance(
     *      java.lang.String)
     */
    public Language loadLanguageInstance(final String name)
            throws HyphenationException {

        if (name == null || name.matches(NON_LOADABLE_LANGUAGE_PATTERN)) {
            //
        } else if (finder == null) {
            getLogger().warning(getLocalizer().format("MissingResourceFinder"));
        } else {
            ObjectInputStream in = null;
            try {
                InputStream ins = finder.findResource(name, TABLE_EXTENSION);
                if (ins != null) {

                    ins = new BufferedInputStream(ins);

                    for (int c = ins.read(); c != '\n'; c = ins.read()) {
                        if (c < 0) {
                            throw new HyphenationException("EOF");
                        }
                    }
                    Object version = in.readObject();
                    in = new ObjectInputStream(new GZIPInputStream(ins));

                    Language lang = (Language) version;

                    in.close();
                    getTables().put(name, lang);
                    return lang;
                } else {
                    getLogger().warning(
                            getLocalizer().format("LanguageNotFound", name));
                }
            } catch (ConfigurationException e) {
                throw new HyphenationException(e);
            } catch (IOException e) {
                throw new HyphenationException(e);
            } catch (ClassNotFoundException e) {
                throw new HyphenationException(e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        throw new HyphenationException(e);
                    }
                }
            }
        }

        return createLanguageInstance(name);
    }

    /**
     * This method arranges that the data written out about the tables can be
     * read back in.
     *
     * @param in the stream to read from
     *
     * @throws IOException in case of an IO error
     * @throws ClassNotFoundException in case of a non existing class
     *  definition
     */
    private void readObject(final ObjectInputStream in)
            throws IOException,
                ClassNotFoundException {

        //        Registrar.register(new CreatorInjector(this), ManagedLanguage.class);
    }

    /**
     * Restore the internal state when the instance is loaded from file.
     *
     * @return the object which should be used instead of the one read
     *
     * @throws ObjectStreamException in case of an error
     */
    protected Object readResolve() throws ObjectStreamException {

        Iterator iterator = getTables().values().iterator();
        while (iterator.hasNext()) {
            Object lang = iterator.next();
            if (lang instanceof ManagedLanguage) {
                ((ManagedLanguage) lang).setCreator(this);
            }

        }

        return Registrar.reconnect(this);
    }

    /**
     * Try to save the hyphenation table by other means. This can be used to
     * write the result to another place than the default output stream.
     * <p>
     *  The table is not saved if the name is purely numeric. This guarantees
     *  the backward compatibility with TeX, since <logo>TeX</logo> uses
     *  numerical names for hyphenation tables only.
     * </p>
     *
     * @param name the name of the table
     * @param value the table itself
     *
     * @return <code>true</code> iff the table has been saved
     *
     * @throws IOException in case of an IO error
     */
    protected boolean saveTable(final String name, final Language value)
            throws IOException {

        if (name == null || name.matches(NON_LOADABLE_LANGUAGE_PATTERN)) {
            return false;
        }

        File file = new File(".", name + TABLE_EXTENSION);
        OutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
        fos.write("#!extex -lfm\n".getBytes());
        ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(
                fos));
        out.writeObject(VERSION);
        out.writeObject(value);
        out.close();
        getLogger().info(
                getLocalizer().format("LanguageSaved", name,
                        file.toString()));
        return true;
    }

    /**
     * @see de.dante.util.resource.ResourceConsumer#setResourceFinder(
     *      de.dante.util.resource.ResourceFinder)
     */
    public void setResourceFinder(final ResourceFinder finder) {

        this.finder = finder;
    }

    /**
     * Write the hyphenation tables to the output stream which can not by saved
     * separately.
     *
     * @param out the output stream to write on
     *
     * @throws IOException in case of an IO error
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {

        Map map = new HashMap();
        Iterator iter = getTables().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry e = (Map.Entry) iter.next();
            String key = (String) e.getKey();
            Language value = (Language) e.getValue();
            if (!saveTable(key, value)) {
                map.put(key, value);
            }
        }
    }

}
