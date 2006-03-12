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

package de.dante.extex.unicodeFont;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import junit.framework.TestCase;
import de.dante.extex.ExTeX;
import de.dante.extex.unicodeFont.format.tex.psfontmap.enc.EncFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.framework.configuration.exception.ConfigurationClassNotFoundException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationInstantiationException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingAttributeException;
import de.dante.util.framework.configuration.exception.ConfigurationNoSuchMethodException;
import de.dante.util.resource.PropertyConfigurable;
import de.dante.util.resource.ResourceFinder;

/**
 * Test for the EncFactory.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */

public class EncFactoryTest extends TestCase {

    /**
     * Which configuration file.
     */
    private static final String CONFIG_EXTEX = "config/extex-font.xml";

    /**
     * my extex.
     */
    private MyExTeX extex;

    /**
     * The encoder factory.
     */
    private EncFactory encFactory;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        if (encFactory == null) {
            extex = new MyExTeX(System.getProperties(), ".extex-test");

            encFactory = new EncFactory(extex.getResourceFinder());
        }
    }

    /**
     * test 01: reader not null.
     * @throws Exception if an error occurred.
     */
    public void test01() throws Exception {

        assertNotNull(encFactory);
    }

    /**
     * test 02: 8r.enc.
     * @throws Exception if an error occurred.
     */
    public void test02() throws Exception {

        String[] et = encFactory.getEncodingTable("8r.enc");

        assertEquals(256, et.length);
        assertEquals("/.notdef", et[0]);
        assertEquals("/dotaccent", et[1]);
        assertEquals("/fi", et[2]);
        assertEquals("/fl", et[3]);
        assertEquals("/fraction", et[4]);
        assertEquals("/hungarumlaut", et[5]);
        assertEquals("/Lslash", et[6]);
        assertEquals("/lslash", et[7]);
        assertEquals("/ogonek", et[8]);
        assertEquals("/ring", et[9]);
        assertEquals("/.notdef", et[10]);
        assertEquals("/breve", et[11]);
        assertEquals("/minus", et[12]);
        // . . .
        assertEquals("/ydieresis", et[255]);
    }

    /**
     * texnansi encoding.
     */
    private static final String[] TEXNANSI_ENC = {"/.notdef", //0
            "/Euro", ///Uni20AC 1
            "/.notdef", //2
            "/.notdef", //3
            "/fraction", //4
            "/dotaccent", //   5
            "/hungarumlaut", //6
            "/ogonek", //  7
            "/fl", //  8
            "/.notdef", ///fraction, // 9   not used (see 4), backward compatability only
            "/cwm", //  10  not used, except boundary char internally maybe
            "/ff", //   11
            "/fi", //   12
            "/.notdef", ///fl   , //13  not used (see 8), backward compatability only
            "/ffi", //   14
            "/ffl", //   15
            "/dotlessi", //16
            "/dotlessj", //17
            "/grave", //   18
            "/acute", //   19
            "/caron", //   20
            "/breve", //   21
            "/macron", //  22
            "/ring", //   23
            "/cedilla", // 24
            "/germandbls", //  25
            "/ae", //   26
            "/oe", //   27
            "/oslash", //  28
            "/AE", //   29
            "/OE", //   30
            "/Oslash", //  31
            "/space", //   32 , ///suppress in TeX text
            "/exclam", //  33
            "/quotedbl", //34 , ///quotedblright in TeX text
            "/numbersign", //  35
            "/dollar", //  36
            "/percent", // 37
            "/ampersand", //   38
            "/quoteright", //  39 , ///quotesingle in ANSI
            "/parenleft", //   40
            "/parenright", //  41
            "/asterisk", //42
            "/plus", //   43
            "/comma", //   44
            "/hyphen", //  45
            "/period", //  46
            "/slash", //   47
            "/zero", //   48
            "/one", //   49
            "/two", //   50
            "/three", //   51
            "/four", //   52
            "/five", //   53
            "/six", //   54
            "/seven", //   55
            "/eight", //   56
            "/nine", //   57
            "/colon", //   58
            "/semicolon", //   59
            "/less", //   60 , ///exclamdown in Tex text
            "/equal", //   61
            "/greater", // 62 , ///questiondown in TeX text
            "/question", //63
            "/at", //  64
            "/A", //   65
            "/B", //   66
            "/C", //   67
            "/D", //   68
            "/E", //   69
            "/F", //   70
            "/G", //   71
            "/H", //   72
            "/I", //   73
            "/J", //   74
            "/K", //   75
            "/L", //   76
            "/M", //   77
            "/N", //   78
            "/O", //   79
            "/P", //   80
            "/Q", //   81
            "/R", //   82
            "/S", //   83
            "/T", //   84
            "/U", //   85
            "/V", //   86
            "/W", //   87
            "/X", //   88
            "/Y", //   89
            "/Z", //   90
            "/bracketleft", // 91
            "/backslash", //   92 , ///quotedblleft in TeX text
            "/bracketright", //93
            "/circumflex", //  94 , ///asciicircum in ASCII
            "/underscore", //  95 , ///dotaccent in TeX text
            "/quoteleft", //   96 , ///grave accent in ANSI
            "/a", //   97
            "/b", //   98
            "/c", //   99
            "/d", //   100
            "/e", //   101
            "/f", //   102
            "/g", //   103
            "/h", //   104
            "/i", //   105
            "/j", //   106
            "/k", //   107
            "/l", //   108
            "/m", //   109
            "/n", //   110
            "/o", //   111
            "/p", //   112
            "/q", //   113
            "/r", //   114
            "/s", //   115
            "/t", //   116
            "/u", //   117
            "/v", //   118
            "/w", //   119
            "/x", //   120
            "/y", //   121
            "/z", //   122
            "/braceleft", //   123, ///endash in TeX text
            "/bar", //   124, ///emdash in TeX test
            "/braceright", //  125, ///hungarumlaut in TeX text
            "/tilde", //   126, ///asciitilde in ASCII
            "/dieresis", //127 not used (see 168), use higher up instead
            "/Lslash", //  128 this position is unfortunate, but now too late to fix
            "/quotesingle", // 129
            "/quotesinglbase", //  130
            "/florin", //  131
            "/quotedblbase", //132
            "/ellipsis", //133
            "/dagger", //  134
            "/daggerdbl", //   135
            "/circumflex", //  136
            "/perthousand", // 137
            "/Scaron", //  138
            "/guilsinglleft", //   139
            "/OE", //   140
            "/Zcaron", //  141
            "/asciicircum", // 142
            "/minus", //   143
            "/lslash", //  144
            "/quoteleft", //   145
            "/quoteright", //  146
            "/quotedblleft", //147
            "/quotedblright", //   148
            "/bullet", //  149
            "/endash", //  150
            "/emdash", //  151
            "/tilde", //   152
            "/trademark", //   153
            "/scaron", //  154
            "/guilsinglright", //  155
            "/oe", //   156
            "/zcaron", //  157
            "/asciitilde", //  158
            "/Ydieresis", //   159
            "/nbspace", // 160, ///space (no break space)
            "/exclamdown", //  161
            "/cent", //   162
            "/sterling", //163
            "/currency", //164
            "/yen", //   165
            "/brokenbar", //   166
            "/section", // 167
            "/dieresis", //168
            "/copyright", //   169
            "/ordfeminine", // 170
            "/guillemotleft", //   171
            "/logicalnot", //  172
            "/sfthyphen", //   173, ///hyphen (hanging hyphen)
            "/registered", //  174
            "/macron", //  175
            "/degree", //  176
            "/plusminus", //   177
            "/twosuperior", // 178
            "/threesuperior", //   179
            "/acute", //   180
            "/mu", //   181
            "/paragraph", //   182
            "/periodcentered", //  183
            "/cedilla", // 184
            "/onesuperior", // 185
            "/ordmasculine", //186
            "/guillemotright", //  187
            "/onequarter", //  188
            "/onehalf", // 189
            "/threequarters", //   190
            "/questiondown", //191
            "/Agrave", //  192
            "/Aacute", //  193
            "/Acircumflex", // 194
            "/Atilde", //  195
            "/Adieresis", //   196
            "/Aring", //   197
            "/AE", //   198
            "/Ccedilla", //199
            "/Egrave", //  200
            "/Eacute", //  201
            "/Ecircumflex", // 202
            "/Edieresis", //   203
            "/Igrave", //  204
            "/Iacute", //  205
            "/Icircumflex", // 206
            "/Idieresis", //   207
            "/Eth", //   208
            "/Ntilde", //  209
            "/Ograve", //  210
            "/Oacute", //  211
            "/Ocircumflex", // 212
            "/Otilde", //  213
            "/Odieresis", //   214
            "/multiply", //215, //OE in T1
            "/Oslash", //  216
            "/Ugrave", //  217
            "/Uacute", //  218
            "/Ucircumflex", // 219
            "/Udieresis", //   220
            "/Yacute", //  221
            "/Thorn", //   222
            "/germandbls", //  223
            "/agrave", //  224
            "/aacute", //  225
            "/acircumflex", // 226
            "/atilde", //  227
            "/adieresis", //   228
            "/aring", //   229
            "/ae", //   230
            "/ccedilla", //231
            "/egrave", //  232
            "/eacute", //  233
            "/ecircumflex", // 234
            "/edieresis", //   235
            "/igrave", //  236
            "/iacute", //  237
            "/icircumflex", // 238
            "/idieresis", //   239
            "/eth", //   240
            "/ntilde", //  241
            "/ograve", //  242
            "/oacute", //  243
            "/ocircumflex", // 244
            "/otilde", //  245
            "/odieresis", //   246
            "/divide", //  247 % oe in T1
            "/oslash", //  248
            "/ugrave", //  249
            "/uacute", //  250
            "/ucircumflex", // 251
            "/udieresis", //   252
            "/yacute", //  253
            "/thorn", //   254
            "/ydieresis" //   255 % germandbls in T1
    };

    /**
     * test 03: texnansi.enc.
     * @throws Exception if an error occurred.
     */
    public void test03() throws Exception {

        String[] et = encFactory.getEncodingTable("texnansi.enc");

        assertEquals(256, et.length);
        assertEquals(256, TEXNANSI_ENC.length);

        for (int i = 0; i < et.length; i++) {
            assertEquals(TEXNANSI_ENC[i], et[i]);
        }

    }

    // -------------------------------------------------------
    // -------------------------------------------------------
    // -------------------------------------------------------
    // -------------------------------------------------------
    // -------------------------------------------------------

    /**
     * inner ExTeX class.
     */
    public class MyExTeX extends ExTeX {

        /**
         * Creates a new object and initializes the properties from given
         * properties and possibly from a user's properties in the file
         * <tt>.extex</tt>.
         * The user properties are loaded from the users home directory and the
         * current directory.
         *
         * @param theProperties the properties to consider
         * @param dotFile the name of the local configuration file. In the case
         *            that this value is <code>null</code> no user properties
         *            will be considered.
         *
         * @throws Exception in case of an error
         */
        public MyExTeX(final Properties theProperties, final String dotFile)
                throws Exception {

            super(theProperties, dotFile);
            makeConfig();
        }

        /**
         * Creates a new object and supplies some properties for those keys which
         * are not contained in the properties already.
         * A detailed list of the properties supported can be found in section
         * <a href="#settings">Settings</a>.
         *
         * @param theProperties the properties to start with. This object is
         *  used and modified. The caller should provide a new instance if this is
         *  not desirable.
         *
         * @throws Exception in case of an error
         */
        public MyExTeX(final Properties theProperties) throws Exception {

            super(theProperties);
            makeConfig();
        }

        /**
         * the config.
         */
        private Configuration config;

        /**
         * create the config.
         */
        private void makeConfig() throws ConfigurationException {

            config = new ConfigurationFactory().newInstance(CONFIG_EXTEX);

        }

        /**
         * the finder.
         */
        private ResourceFinder finder;

        /**
         * Returns the finder.
         * @return Returns the finder.
         * @throws ConfigurationException if an error occurs.
         */
        public ResourceFinder getResourceFinder() throws ConfigurationException {

            if (finder == null) {
                finder = makeResourceFinder(config);
            }
            return finder;
        }

        /**
         * the font factroy.
         */
        private FontFactory fontFactory;

        /**
         * Returns the font factory.
         * @return Returns the font factory.
         * @throws ConfigurationException if an error occurs
         */
        public FontFactory getFontFactory() throws ConfigurationException {

            if (fontFactory == null) {
                fontFactory = makemyFontFactory(config
                        .getConfiguration("Fonts"), getResourceFinder());
            }
            return fontFactory;
        }

        /**
         * Create a new font factory.
         * @param config the configuration object for the font factory
         * @param finder the resource finder to use
         *
         * @return the new font factory
         *
         * @throws ConfigurationException in case that some kind of problems have
         * been detected in the configuration
         */
        protected FontFactory makemyFontFactory(final Configuration config,
                final ResourceFinder finder) throws ConfigurationException {

            FontFactory fontFactory;
            String fontClass = config.getAttribute("class");

            if (fontClass == null || fontClass.equals("")) {
                throw new ConfigurationMissingAttributeException("class",
                        config);
            }

            try {
                fontFactory = (FontFactory) (Class.forName(fontClass)
                        .getConstructor(
                                new Class[]{Configuration.class,
                                        ResourceFinder.class})
                        .newInstance(new Object[]{config, finder}));
            } catch (IllegalArgumentException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (SecurityException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (InstantiationException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (IllegalAccessException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (InvocationTargetException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (NoSuchMethodException e) {
                throw new ConfigurationNoSuchMethodException(e);
            } catch (ClassNotFoundException e) {
                throw new ConfigurationClassNotFoundException(fontClass);
            }

            if (fontFactory instanceof PropertyConfigurable) {
                ((PropertyConfigurable) fontFactory)
                        .setProperties(getProperties());
            }

            return fontFactory;
        }

    }

    // --------------------------------------------

    /**
     * main.
     * @param args  the command line.
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(EncFactoryTest.class);
    }

}
