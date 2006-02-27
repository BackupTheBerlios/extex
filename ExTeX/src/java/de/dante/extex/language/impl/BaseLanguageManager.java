/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.language.Language;
import de.dante.extex.language.LanguageManager;
import de.dante.extex.language.ModifiableLanguage;
import de.dante.extex.language.ligature.LigatureBuilder;
import de.dante.extex.language.word.WordTokenizer;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class manages the <code>HyphenationTable</code>s. It is a container
 * which can be asked to provide an appropriate instance. This instance is
 * either taken from existing instances or a new instance is created.
 *
 * <h2>Configuration</h2>
 *
 * This instance is configurable. The configuration is used to select the
 * appropriate class and optional parameters for a requested instance. In this
 * respect this class makes best use of the infrastructure of the
 * {@link de.dante.util.framework.AbstractFactory AbstractFactory}.
 *
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public class BaseLanguageManager extends AbstractFactory
        implements
            LanguageManager,
            Serializable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>tables</tt> contains the mapping from index to
     * hyphenation table.
     */
    private Map tables = new HashMap();

    /**
     * Create a new language and put it into the table.
     *
     * @param name the name of the language
     *
     * @return the new instance of a language
     *
     * @throws ConfigurationException in case of a configuration error
     */
    protected Language createLanguage(final String name)
            throws ConfigurationException {

        ModifiableLanguage table;
        Configuration cfg = selectConfiguration(name);
        table = (ModifiableLanguage) createInstanceForConfiguration(cfg,
                ModifiableLanguage.class);

        Configuration config = cfg.findConfiguration("LigatureBuilder");
        table.setLigatureBuilder(//
                (LigatureBuilder) createInstanceForConfiguration(config,
                        LigatureBuilder.class));

        config = cfg.findConfiguration("WordTokenizer");
        table.setWordTokenizer(//
                (WordTokenizer) createInstanceForConfiguration(config,
                        WordTokenizer.class));

        tables.put(name, table);
        return table;
    }

    /**
     * Return the <code>Language</code> for a given name.
     * <p>
     *  If there is no language present with the given name then
     *  a new one is created.
     * </p>
     * <p>
     *  The index in <logo>TeX</logo> is the language number as
     *  <code>String</code>. This implementation does not have this restriction.
     *  The name can be any string.
     * </p>
     * <p>
     *  The proposal is to use a natural number for backward compatibility and
     *  ISO language codes otherwise.
     * </p>
     *
     * @param name the name for which the language is requested
     *
     * @return the language for the given name
     *
     * @throws ConfigurationException in case of an error in the configuration
     *
     * @see de.dante.extex.language.LanguageManager#getLanguage(
     *      java.lang.String)
     */
    public Language getLanguage(final String name)
            throws ConfigurationException {

        Language table = (ModifiableLanguage) tables.get(name);
        if (table == null) {
            table = createLanguage(name);
        }
        return table;
    }

    /**
     * Getter for the tables.
     *
     * @return the tables map
     */
    protected Map getTables() {

        return tables;
    }

}
