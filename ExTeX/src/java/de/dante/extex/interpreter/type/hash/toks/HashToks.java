/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.hash.toks;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;

/**
 * A Hash for Tokens.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class HashToks implements Serializable {

    /**
     * The hash
     */
    private HashMap map = null;

    /**
     * Creates a new object.
     */
    public HashToks() {

        super();
        map = new HashMap();
    }

    /**
     * Creates a new object.
     * get the <code>TokenSource</code> for a <code>HashToks</code> (noexpand).
     * @param context   the context
     * @param source    the token source
     * @throws GeneralException ...
     */
    public HashToks(final Context context, final TokenSource source)
            throws GeneralException {

        super();
        map = new HashMap();

        // { {key1}{value1} {key2}{value2} }
        Token token = source.getNonSpace();

        if (token == null) {
            throw new HelpingException("TTP.MissingLeftBrace");
            //TODO: handle EOF
        } else if (!token.isa(Catcode.LEFTBRACE)) {
            throw new HelpingException("TTP.MissingLeftBrace");
            //TODO call the error handler
        }

        while (true) {
            String key = source.scanTokensAsString();
            if (key.trim().length() == 0) {
                throw new HelpingException("TTP.hasherrorKey");
            }
            Tokens toks = source.getTokens();
            if (toks == null) {
                throw new HelpingException("TTP.hasherrorvalue");
            }
            put(key, toks);

            // next ?
            token = source.getNonSpace();
            if (token == null) {
                throw new HelpingException("TTP.MissingRightBrace");
                //TODO: handle EOF
            } else if (token.isa(Catcode.RIGHTBRACE)) {
                break;
            }
            source.push(token);
        }
    }

    /**
     * Put the tokens on the hash with the key.
     * @param key       the key
     * @param toks      the tokens
     */
    public void put(final String key, final Tokens toks) {

        map.put(key, toks);
    }

    /**
     * Return the tokens for a key.
     * @param key   the key
     * @return  the token for this key
     */
    public Tokens get(final String key) {

        Tokens toks = (Tokens) map.get(key);
        if (toks == null) {
            toks = new Tokens();
        }
        return toks;
    }

    /**
     * Return the size of the hash.
     * @return  the size of the hash
     */
    public int size() {

        return map.size();
    }

    /**
     * Contains the key
     * @param key   the key
     * @return  <code>true</code> if the key exists, otherwise <code>false</code>
     */
    public boolean containsKey(final String key) {

        return map.containsKey(key);
    }

    /**
     * Return the value as <code>String</code>
     * @return the value as <code>String</code>
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        Iterator it = map.keySet().iterator();
        buf.append("{\n");
        while (it.hasNext()) {
            String key = (String) it.next();
            buf.append('{' + key + '}');
            buf.append('{' + get(key).toText() + "}\n");
        }
        buf.append("}\n");
        return buf.toString();
    }
}