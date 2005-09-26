/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.CharacterCodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import de.dante.extex.color.ColorAware;
import de.dante.extex.color.ColorConverter;
import de.dante.extex.color.ColorConverterFacory;
import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterFactory;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.documentWriter.OutputStreamFactory;
import de.dante.extex.documentWriter.exception.DocumentWriterException;
import de.dante.extex.font.FontFactory;
import de.dante.extex.font.exception.FontException;
import de.dante.extex.interpreter.ErrorHandler;
import de.dante.extex.interpreter.ErrorHandlerFactory;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.InterpreterFactory;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.observer.InteractionObserver;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.interaction.Interaction;
import de.dante.extex.interpreter.interaction.InteractionUnknownException;
import de.dante.extex.interpreter.loader.LoaderException;
import de.dante.extex.interpreter.output.TeXOutputRoutine;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.main.Version;
import de.dante.extex.main.exception.MainCodingException;
import de.dante.extex.main.exception.MainConfigurationException;
import de.dante.extex.main.exception.MainException;
import de.dante.extex.main.exception.MainIOException;
import de.dante.extex.main.logging.LogFormatter;
import de.dante.extex.main.observer.FileOpenObserver;
import de.dante.extex.main.observer.InteractionModeObserver;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.scanner.stream.TokenStreamOptions;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterFactory;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.util.exception.GeneralException;
import de.dante.util.file.OutputFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.framework.configuration.exception.ConfigurationClassNotFoundException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationInstantiationException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingAttributeException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingException;
import de.dante.util.framework.configuration.exception.ConfigurationNoSuchMethodException;
import de.dante.util.framework.configuration.exception.ConfigurationSyntaxException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;
import de.dante.util.observer.NotObservableException;
import de.dante.util.resource.PropertyConfigurable;
import de.dante.util.resource.ResourceConsumer;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

/**
 * This is the programmatic interface to the <logo>ExTeX</logo> functionality.
 * A program may use this functionality to perform all necessary actions without
 * the burden of the traditional <logo>TeX</logo> command line interface.
 * <p>
 * The programmatic interface provides the following features:
 * </p>
 * <ul>
 * <li>Specifying format, input file and <logo>TeX</logo> code in properties.</li>
 * </ul>
 *
 * <a name="settings"/><h3>Settings</h3>
 *
 * <p>
 *  Settings can be stored in properties files. Those settings are the
 *  fallback if none are provided otherwise.
 * </p>
 * <p>
 *  The properties are stored in a file named <tt>.extex</tt>. It is
 *  sought in the users home directory. This determined by the system
 *  property <tt>user.home</tt>. Afterwards it is sought in the
 *  current directory. The local settings of a directory overwrite the
 *  user's setting. The user's setting overwrite the compiled in defaults
 * </p>
 * <p>
 * The following properties are recognized:
 * </p>
 * <dl>
 *   <dt><a name="extex.code"/><tt>extex.code</tt></a></dt>
 *   <dd>
 *    This parameter contains <logo>ExTeX</logo> code to be executed directly.
 *    The execution is performed after any code specified in an input file.
 *    on the command line the code has to start with a backslash. This
 *    restriction does not hold for the property settings.
 *   </dd>
 *   <dd>Command line: <tt>&lang;code&rang;</tt></dd>
 *
 *   <dt><a name="extex.color.converter"/><tt>extex.color.converter</tt></dt>
 *   <dd>
 *    This parameter contains the name of the configuration resource to use for
 *    the color converter.
 *   </dd>
 *   <dd>Default: <tt></tt></dd>
 *
 *   <dt><a name="extex.config"/><tt>extex.config</tt></dt>
 *   <dd>
 *    This parameter contains the name of the configuration resource to use.
 *    This configuration resource is sought on the class path.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-configuration"><tt>-configuration &lang;resource&rang;</tt></a></dd>
 *   <dd>Default: <tt>extex.xml</tt></dd>
 *
 *   <dt><a name="extex.encoding"/><tt>extex.encoding</tt></dt>
 *   <dd>
 *    This parameter contains the name of the property for
 *    the standard encoding to use.
 *   </dd>
 *   <dd>Default: <tt>ISO-8859-1</tt></dd>
 *
 *   <dt><a name="extex.error.handler"/><tt>extex.error.handler</tt></dt>
 *   <dd>
 *    This parameter contains the logical name of the error handler.
 *   </dd>
 *
 *   <dt><a name="extex.error.handler"/><tt>extex.error.handler</tt></dt>
 *   <dd>
 *    This parameter contains the logical name of the error handler.
 *   </dd>
 *
 *   <dt><a name="extex.fonts"/><tt>extex.fonts</tt></dt>
 *   <dd>
 *    This parameter contains the name of the property indicating where to
 *    find font files. The value is a path similar to extex.texinputs.
 *   </dd>
 *
 *   <dt><a name="extex.halt.on.error"/><tt>extex.halt.on.error</tt></dt>
 *   <dd>
 *    This parameter contains the name of the property indicating whether the
 *    processing should stop after the first error.
 *   </dd>
 *
 *   <dt><a name="extex.file"><tt>extex.file</tt></a></dt>
 *   <dd>
 *    This parameter contains the file to read from. It has no default
 *   </dd>
 *
 *   <dt><a name="extex.fmt"/><tt>extex.fmt</tt></dt>
 *   <dd>
 *    This parameter contains the name of the format to read. An empty
 *    string denotes that no format should be read. This is the default.
 *   </dd>
 *
 *   <dt><a name="extex.ini"/><tt>extex.ini</tt></dt>
 *   <dd>
 *    If set to <code>true</code> then act as initex. This command line
 *    option is defined for compatibility to <logo>TeX</logo> only. In
 *   <logo>ExTeX</logo> it has no effect at all.
 *   </dd>
 *
 *   <dt><a name="extex.interaction"/><tt>extex.interaction</tt></dt>
 *   <dd>
 *    This parameter contains the interaction mode. possible values are
 *    the numbers 0..3 and the symbolic names <tt>batchmode</tt> (0),
 *    <tt>nonstopmode</tt> (1), <tt>scrollmode</tt> (2), and
 *    <tt>errorstopmode</tt> (3).
 *   </dd>
 *   <dd>Default: <tt>3</tt></dd>
 *
 *   <dt><a name="extex.jobname"/><tt>extex.jobname</tt></dt>
 *   <dd>
 *    This parameter contains the name of the job. It is overwritten
 *    if a file is given to read from. In this case the base name of
 *    the input file is used instead.
 *   </dd>
 *   <dd>Default: <tt>texput</tt></dd>
 *
 *   <dt><a name="extex.jobname.master"/><tt>extex.jobname.master</tt></dt>
 *   <dd>
 *    This parameter contains the name of the job to be used with high
 *    priority.
 *   </dd>
 *   <dd>Default: <tt>texput</tt></dd>
 *
 *   <dt><a name="extex.lang"/><tt>extex.lang</tt></dt>
 *   <dd>
 *    This parameter contains the name of the locale to be used for the
 *    messages.
 *   </dd>
 *
 *   <dt><a name="extex.nobanner"/><tt>extex.nobanner</tt></dt>
 *   <dd>
 *    This parameter contains a Boolean indicating that the banner should be
 *    suppressed.
 *   </dd>
 *
 *   <dt><a name="extex.output"/><tt>extex.output</tt></dt>
 *   <dd>
 *    This parameter contains the output format. This logical name is resolved
 *    via the configuration.
 *   </dd>
 *   <dd>Default: <tt>pdf</tt></dd>
 *
 *   <dt><a name="extex.outputdir"/><tt>extex.outputdir</tt></dt>
 *   <dd>
 *    This parameter contain the directory where output files should be
 *    created.
 *   </dd>
 *   <dd>Default: <tt>.</tt></dd>
 *
 *   <dt><a name="extex.outputdir.fallback"/><tt>extex.outputdir.fallback</tt></dt>
 *   <dd>
 *    This parameter contains the name of the
 *    property for the fallback if the output directory fails to be writable.
 *   </dd>
 *
 *   <dt><a name="extex.progname"/><tt>extex.progname</tt></dt>
 *   <dd>
 *    This parameter can be used to overrule the name of the program shown in
 *    the banner and the version information.
 *   </dd>
 *   <dd>Default: <tt>ExTeX</tt></dd>
 *
 *   <dt><a name="extex.texinputs"/><tt>extex.texinputs</tt></dt>
 *   <dd>
 *    This parameter contains the additional directories for searching TeX
 *    input files.
 *   </dd>
 *
 *   <dt><a name="extex.token.stream"/><tt>extex.token.stream</tt></dt>
 *   <dd>
 *    This string parameter contains the logical name of the configuration to
 *    use for the token stream.
 *   </dd>
 *
 *   <dt><a name="extex.trace.input.files"/><tt>extex.trace.input.files</tt></dt>
 *   <dd>
 *    This Boolean parameter contains the indicator whether or not to trace the
 *    search for input files.
 *   </dd>
 *
 *   <dt><a name="extex.trace.font.files"/><tt>extex.trace.font.filess</tt></dt>
 *   <dd>
 *    This Boolean parameter contains the indicator whether or not to trace the
 *    search for font files.
 *   </dd>
 *
 *   <dt><a name="extex.trace.macros"/><tt>extex.trace.macros</tt></dt>
 *   <dd>
 *    This Boolean parameter contains the indicator whether or not to trace the
 *    execution of macros.
 *   </dd>
 *
 *   <dt><a name="extex.trace.tokenizer"/><tt>extex.trace.tokenizer</tt></dt>
 *   <dd>
 *    This Boolean parameter contains the indicator whether or not to trace the
 *    work of the tokenizer.
 *   </dd>
 *
 *   <dt><a name="extex.typesetter"/><tt>extex.typesetter</tt></dt>
 *   <dd>
 *    This parameter contains the name of the typesetter to use. If it is
 *    not set then the default from the configuration file is used.
 *   </dd>
 * </dl>
 *
 * <p>
 *  There is another level of properties which is considered between the
 *  compiled in defaults and the user's properties. Those are the system
 *  properties of the Java system. There system wide settings can be stored.
 *  Nevertheless, you should use this feature sparsely.
 * </p>
 *
 *
 * <a name="configuration"/><h3>Configuration Resources</h3>
 *
 * <p>
 *  The configuration of <logo>ExTeX</logo> is controlled by several
 *  configuration resources. The fallback for those configuration resources are
 *  contained in the <logo>ExTeX</logo> jar file. In this section we will
 *  describe how to overwrite the settings in the default configuration
 *  resource.
 * </p>
 *
 * TODO gene: doc incomplete
 *
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 *
 * @version $Revision: 1.116 $
 */
public class ExTeX {

    /**
     * The constant <tt>EXIT_INTERNAL_ERROR</tt> contains the exit code for
     * internal errors.
     */
    protected static final int EXIT_INTERNAL_ERROR = -666;

    /**
     * The field <tt>EXIT_NO_FORMAT</tt> contains the exit code when no format
     * is found.
     */
    private static final int EXIT_NO_FORMAT = 42;

    /**
     * The constant <tt>EXIT_OK</tt> contains the exit code of the program for
     * the success case.
     */
    protected static final int EXIT_OK = 0;

    /**
     * The constant <tt>VERSION</tt> contains the manually incremented version
     * string.
     */
    private static final String EXTEX_VERSION = new Version().toString();

    /**
     * The field <tt>FORMAT_FALLBACK</tt> contains the fallback to be tried if
     * the specified format can not be loaded. If it is <code>null</code> then
     * none is tried.
     */
    private static final String FORMAT_FALLBACK = "tex";

    /**
     * The field <tt>PROP_CODE</tt> contains the name of the property for the
     * <logo>TeX</logo> code to be inserted at the beginning of the job.
     */
    protected static final String PROP_CODE = "extex.code";

    /**
     * The field <tt>PROP_COLOR_CONVERTER</tt> contains the name of the
     * property for the color converter to use.
     */
    protected static final String PROP_COLOR_CONVERTER = "extex.color.converter";

    /**
     * The field <tt>PROP_CONFIG</tt> contains the name of the
     * property for the configuration resource to use.
     */
    protected static final String PROP_CONFIG = "extex.config";

    /**
     * The field <tt>PROP_ENCODING</tt> contains the name of the property for
     * the standard encoding to use.
     */
    protected static final String PROP_ENCODING = "extex.encoding";

    /**
     * The field <tt>PROP_ERROR_HANDLER</tt> contains the name of the property
     * for the error handler type to use. Possible values are resolved via the
     * configuration.
     */
    protected static final String PROP_ERROR_HANDLER = "extex.error.handler";

    /**
     * The field <tt>PROP_FILE</tt> contains the name of the property for the
     * input file to read.
     */
    protected static final String PROP_FILE = "extex.file";

    /**
     * The field <tt>PROP_FMT</tt> contains the name of the property for the
     * name of the format file to use.
     */
    protected static final String PROP_FMT = "extex.fmt";

    /**
     * The field <tt>PROP_HALT_ON_ERROR</tt> contains the name of the property
     * indicating whether the processing should stop at the first error.
     */
    protected static final String PROP_HALT_ON_ERROR = "extex.halt.on.error";

    /**
     * The field <tt>PROP_INI</tt> contains the name of the property for the
     * Boolean value indicating that some kind of emulations for iniTeX should
     * be provided. Currently this has no effect in <logo>ExTeX</logo>.
     */
    protected static final String PROP_INI = "extex.ini";

    /**
     * The field <tt>PROP_INTERACTION</tt> contains the name of the
     * property for the interaction mode.
     */
    protected static final String PROP_INTERACTION = "extex.interaction";

    /**
     * The field <tt>PROP_INTERNAL_STACKTRACE</tt> contains the ...
     */
    protected static final String PROP_INTERNAL_STACKTRACE = "extex.stacktrace.on.internal.error";

    /**
     * The field <tt>PROP_JOBNAME</tt> contains the name of the
     * property for the job name. The value can be overruled by the property
     * named in <tt>PROP_JOBNAME_MASTER</tt>.
     */
    protected static final String PROP_JOBNAME = "extex.jobname";

    /**
     * The field <tt>PROP_JOBNAME_MASTER</tt> contains the name of the
     * property for the job name to be used with high priority.
     */
    protected static final String PROP_JOBNAME_MASTER = "extex.jobname.master";

    /**
     * The field <tt>PROP_LANG</tt> contains the name of the property for the
     * language to use for messages.
     */
    protected static final String PROP_LANG = "extex.lang";

    /**
     * The field <tt>PROP_NO_BANNER</tt> contains the name of the property for
     * the Boolean value indicating whether or not to show a program banner.
     */
    protected static final String PROP_NO_BANNER = "extex.nobanner";

    /**
     * The field <tt>PROP_OUTPUT_TYPE</tt> contains the name of the property for
     * the output driver. This value is resolved by the
     * {@link de.dante.extex.documentWriter.DocumentWriterFactory DocumentWriterFactory}
     * to find the appropriate class.
     */
    protected static final String PROP_OUTPUT_TYPE = "extex.output";

    /**
     * The field <tt>PROP_OUTPUTDIR</tt> contains the name of the
     * property for the output directory.
     */
    protected static final String PROP_OUTPUTDIR = "extex.outputdir";

    /**
     * The field <tt>PROP_OUTPUTDIR_FALLBACK</tt> contains the name of the
     * property for the fallback if the output directory fails to be writable.
     */
    protected static final String PROP_OUTPUTDIR_FALLBACK = "extex.outputdir.fallback";

    /**
     * The field <tt>PROP_PROGNAME</tt> contains the name of the
     * property for the program name used in usage messages.
     */
    protected static final String PROP_PROGNAME = "extex.progname";

    /**
     * The field <tt>PROP_TEXINPUTS</tt> contains the name of the
     * property for the additional texinputs specification of directories.
     */
    protected static final String PROP_TEXINPUTS = "extex.texinputs";

    /**
     * The field <tt>PROP_TOKEN_STREAM</tt> contains the name of the property
     * for the token stream class to use.
     */
    protected static final String PROP_TOKEN_STREAM = "extex.token.stream";

    /**
     * The field <tt>PROP_TRACE_FONT_FILES</tt> contains the name of the
     * property for the Boolean determining whether or not the searching for
     * font files should produce tracing output.
     */
    protected static final String PROP_TRACE_FONT_FILES = "extex.trace.font.files";

    /**
     * The field <tt>PROP_TRACE_INPUT_FILES</tt> contains the name of the
     * property for the Boolean determining whether or not the searching for
     * input files should produce tracing output.
     */
    protected static final String PROP_TRACE_INPUT_FILES = "extex.trace.input.files";

    /**
     * The field <tt>PROP_TRACE_MACROS</tt> contains the name of the
     * property for the Boolean determining whether or not the execution of
     * macros should produce tracing output.
     */
    protected static final String PROP_TRACE_MACROS = "extex.trace.macros";

    /**
     * The field <tt>PROP_TRACE_TOKENIZER</tt> contains the name of the
     * property for the Boolean determining whether or not the tokenizer
     * should produce tracing output.
     */
    protected static final String PROP_TRACE_TOKENIZER = "extex.trace.tokenizer";

    /**
     * The field <tt>PROP_TYPESETTER_TYPE</tt> contains the name of the property
     * for the typesetter to use. This value is resolved by the
     * {@link de.dante.extex.typesetter.TypesetterFactory TypesetterFactory}
     * to find the appropriate class.
     */
    protected static final String PROP_TYPESETTER_TYPE = "extex.typesetter";

    /**
     * The field <tt>TAG_ERRORHANDLER</tt> contains the name of the tag in the
     * configuration file which contains the specification for the
     * error handler factory.
     */
    private static final String TAG_ERRORHANDLER = "ErrorHandler";

    /**
     * The field <tt>TAG_FONT</tt> contains the name of the tag in the
     * configuration file which contains the specification for the font.
     */
    private static final String TAG_FONT = "Font";

    /**
     * Log a Throwable including its stack trace to the logger.
     *
     * @param logger the target logger
     * @param text the prefix text to log
     * @param e the Throwable to log
     */
    protected static void logException(final Logger logger, final String text,
            final Throwable e) {

        logger.severe(text == null ? "" : text);
        logger.log(Level.FINE, "", e);
    }

    /**
     * The field <tt>errorHandler</tt> contains the error handler to use.
     */
    private ErrorHandler errorHandler = null;

    /**
     * The field <tt>interactionObserver</tt> contains the observer called
     * whenever the interaction mode is changed.
     */
    private InteractionObserver interactionObserver = null;

    /**
     * The field <tt>localizer</tt> contains the localizer. It is initiated
     * with a localizer for the name of this class.
     */
    private Localizer localizer = LocalizerFactory.getLocalizer(ExTeX.class
            .getName());

    /**
     * The field <tt>logger</tt> contains the logger currently in use.
     */
    private Logger logger;

    /**
     * The field <tt>outStream</tt> contains the output stream for the document
     * writer.
     */
    private OutputStream outStream = null;

    /**
     * The field <tt>properties</tt> contains the properties containing the
     * settings for the invocation.
     */
    private Properties properties;

    /**
     * The field <tt>showBanner</tt> is a boolean indicating that it is
     * necessary to display the banner. This information is needed for the
     * cases where errors show up before the normal banner has been printed.
     */
    private boolean showBanner = true;

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
     * @throws InterpreterException in case of an error
     */
    public ExTeX(final Properties theProperties) throws InterpreterException {

        super();

        this.properties = theProperties;
        propertyDefault(PROP_CODE, "");
        propertyDefault(PROP_CONFIG, "extex.xml");
        propertyDefault(PROP_ENCODING, "ISO-8859-1");
        propertyDefault(PROP_ERROR_HANDLER, "");
        propertyDefault(PROP_FILE, "");
        propertyDefault(PROP_FMT, "");
        propertyDefault(PROP_INI, "");
        propertyDefault(PROP_INTERACTION, "3");
        propertyDefault(PROP_JOBNAME, "texput");
        propertyDefault(PROP_JOBNAME_MASTER, "");
        propertyDefault(PROP_NO_BANNER, "");
        propertyDefault(PROP_LANG, "");
        propertyDefault(PROP_OUTPUT_TYPE, "");
        propertyDefault(PROP_OUTPUTDIR, ".");
        propertyDefault(PROP_OUTPUTDIR_FALLBACK, ".");
        propertyDefault(PROP_PROGNAME, "ExTeX");
        propertyDefault(PROP_TEXINPUTS, null);
        propertyDefault(PROP_TOKEN_STREAM, "base");
        propertyDefault(PROP_TRACE_INPUT_FILES, "");
        propertyDefault(PROP_TRACE_FONT_FILES, "");
        propertyDefault(PROP_TRACE_MACROS, "");
        propertyDefault(PROP_TRACE_TOKENIZER, "");
        propertyDefault(PROP_TYPESETTER_TYPE, "");
        propertyDefault(PROP_INTERNAL_STACKTRACE, "");

        showBanner = !Boolean.valueOf(properties.getProperty(PROP_NO_BANNER))
                .booleanValue();

        applyLanguage();

        logger = Logger.getLogger(getClass().getName());
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new LogFormatter());
        consoleHandler.setLevel(Level.WARNING);
        logger.addHandler(consoleHandler);
        interactionObserver = new InteractionModeObserver(consoleHandler);
        applyInteraction();
    }

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
     * @throws InterpreterException in case of an invalid inetraction mode
     * @throws IOException in case of an IO Error during the reading of the
     *             properties file
     *
     * @see #ExTeX(java.util.Properties)
     */
    public ExTeX(final Properties theProperties, final String dotFile)
            throws InterpreterException,
                IOException {

        this(theProperties);

        if (dotFile != null) {
            loadUserProperties(new File(System.getProperty("user.home"),
                    dotFile));
            loadUserProperties(new File(dotFile));

            applyLanguage();
        }
    }

    /**
     * Propagate the settings for the interaction mode to the
     * <code>interactionObserver</code>.
     *
     * @throws InteractionUnknownException in case that the interaction is
     *             not set properly
     */
    protected void applyInteraction() throws InteractionUnknownException {

        try {
            interactionObserver.receiveInteractionChange(null, Interaction
                    .get(properties.getProperty(PROP_INTERACTION)));
        } catch (Exception e) {
            throw new InteractionUnknownException("");
        }
    }

    /**
     * Try to determine which language to use and configure the localizer
     * accordingly.
     */
    protected void applyLanguage() {

        String lang = (String) properties.get(PROP_LANG);

        if (lang != null) {
            int len = lang.length();
            if (len == 2) {
                Locale.setDefault(new Locale(lang));
            } else if (len == 5
                    && (lang.charAt(2) == '-' || lang.charAt(2) == '_')) {

                Locale.setDefault(new Locale(lang.substring(0, 1), lang
                        .substring(3, 4)));

            } else if (len == 8
                    && (lang.charAt(2) == '-' || lang.charAt(2) == '_')
                    && (lang.charAt(5) == '-' || lang.charAt(5) == '_')) {

                Locale.setDefault(new Locale(lang.substring(0, 1), lang
                        .substring(3, 4), lang.substring(6, 7)));

            } else {
                // TODO ignored on purpose?
            }
        }

        localizer = LocalizerFactory.getLocalizer(ExTeX.class.getName());
    }

    /**
     * Find the name of the job.
     *
     * @return the correct job name
     */
    private String determineJobname() {

        String jobname = properties.getProperty(PROP_JOBNAME_MASTER);

        if (jobname == null || jobname.equals("")) {
            jobname = properties.getProperty(PROP_JOBNAME);
            if (jobname == null || jobname.equals("")) {
                jobname = "texput";
            }
        }
        return new File(jobname).getName();
    }

    /**
     * Getter for localizer.
     *
     * @return the localizer
     */
    protected Localizer getLocalizer() {

        return this.localizer;
    }

    /**
     * Getter for logger.
     *
     * @return the logger.
     */
    public Logger getLogger() {

        return logger;
    }

    /**
     * Getter for outStream.
     *
     * @return the outStream.
     */
    public OutputStream getOutStream() {

        return this.outStream;
    }

    /**
     * Getter for properties.
     *
     * @return the properties
     */
    public Properties getProperties() {

        return this.properties;
    }

    /**
     * Getter for a named property.
     *
     * @param key the property name
     *
     * @return the value of the named property or <code>null</code>
     */
    public String getProperty(final String key) {

        return this.properties.getProperty(key);
    }

    /**
     * Initialize the input streams. If the property <i>extex.file</i> is set
     * and not the empty string, (e.g. from the command line) then this value
     * is used as file name to read from. If the property <i>extex.code</i>
     * is set and not the empty string (e.g. from the command line) then this
     * value is used as initial input after the input file has been processed.
     *
     * @param interpreter the interpreter context
     * @param prop the properties
     *
     * @return <code>true</code> if the stream have not been initialized
     *
     * @throws ConfigurationException in case of a configuration error
     * @throws MainIOException in case of an IO error
     */

    protected boolean initializeStreams(final Interpreter interpreter,
            final Properties prop)
            throws ConfigurationException,
                MainIOException {

        TokenStreamFactory factory = interpreter.getTokenStreamFactory();
        boolean notInitialized = true;

        String filename = prop.getProperty(PROP_FILE);

        if (filename != null && !filename.equals("")) {

            try {
                TokenStream stream = factory.newInstance(filename, "tex", prop
                        .getProperty(PROP_ENCODING));
                interpreter.addStream(stream);
                notInitialized = false;
            } catch (FileNotFoundException e) {
                logger.severe(localizer.format("TTP.FileNotFound", filename));
            }
        }

        String post = prop.getProperty(PROP_CODE);

        if (post != null && !post.equals("")) {
            interpreter.addStream(factory.newInstance(post));
            notInitialized = false;
        }

        return notInitialized;
    }

    /**
     * Load a format if a non-empty name of a format is given.
     *
     * @param interpreter the interpreter to delegate the loading to
     * @param finder  the resource finder to use for locating the format file
     * @param fmt the name of the format to use or <code>null</code>
     * @param jobname the name of the job
     *
     * @throws GeneralException in case of some error
     * @throws IOException in case, well, you guess it
     * @throws ConfigurationException in case of a configuration error
     */
    protected void loadFormat(final Interpreter interpreter,
            final ResourceFinder finder, final String fmt, final String jobname)
            throws IOException,
                GeneralException,
                ConfigurationException {

        String format = fmt;
        String time = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, Locale.ENGLISH).format(new Date());

        if (format != null && !format.equals("")) {
            InputStream stream = finder.findResource(fmt, "fmt");

            if (stream == null && !format.equals(FORMAT_FALLBACK)) {
                logger.warning(localizer.format("FormatSubstituted", format,
                        FORMAT_FALLBACK));
                format = FORMAT_FALLBACK;
                stream = finder.findResource(FORMAT_FALLBACK, "fmt");
            }
            if (stream == null) {
                throw new MainException(EXIT_NO_FORMAT, localizer.format(
                        "FormatNotFound", format));
            }
            try {
                interpreter.loadFormat(stream, fmt);
            } catch (LoaderException e) {
                throw new HelpingException(localizer, "TTP.FormatFileError",
                        format);
            }
            logger.config(localizer.format("ExTeX.FormatDate", format, time));
        } else {
            logger.config(localizer.format("ExTeX.NoFormatDate", time));
        }
        interpreter.setJobname(jobname);
    }

    /**
     * Load properties from a given file if it exists.
     *
     * @param file the file to consider
     *
     * @throws IOException in case of an IO Error during the reading of the
     *             properties file
     */
    protected void loadUserProperties(final File file) throws IOException {

        if (file != null && file.canRead()) {
            properties.load(new FileInputStream(file));
        }
    }

    /**
     * Log a Throwable including its stack trace to the logger.
     *
     * @param e the Throwable to log
     */
    protected void logInternalError(final Throwable e) {

        if (Boolean.valueOf(properties.getProperty(PROP_INTERNAL_STACKTRACE))
                .booleanValue()) {
            e.printStackTrace();
        }

        String msg = e.getLocalizedMessage();
        for (Throwable t = e; t != null && msg == null; t = t.getCause()) {
            msg = t.getLocalizedMessage();
            if ("".equals(msg)) {
                msg = null;
            }
        }

        if (msg == null) {
            msg = e.getClass().getName();
            msg = msg.substring(msg.lastIndexOf('.') + 1);
        }

        logException(logger, localizer.format("ExTeX.InternalError", msg), e);
    }

    /**
     * Make a new instance of a color converter.
     *
     * @param cfg the configuration to use
     *
     *  @return the new color converter
     *
     * @throws ConfigurationException in case of a configuration problem
     */
    private ColorConverter makeColorConverter(final Configuration cfg)
            throws ConfigurationException {

        ColorConverterFacory factory = new ColorConverterFacory();
        factory.configure(cfg);
        factory.enableLogging(logger);
        return factory
                .newInstance(properties.getProperty(PROP_COLOR_CONVERTER));
    }

    /**
     * Create a default font for the interpreter context.
     *
     * @param config the configuration object for the font
     * @param fontFactory the font factory to request the font from
     *
     * @return the default font
     *
     * @throws GeneralException in case of an error of some other kind
     * @throws ConfigurationException in case that some kind of problems have
     *  been detected in the configuration
     * @throws FontException in case of problems with the font itself
     */
    protected Font makeDefaultFont(final Configuration config,
            final FontFactory fontFactory)
            throws GeneralException,
                ConfigurationException,
                FontException {

        final String attributeName = "name";
        final String attributeSize = "size";
        String defaultFont = config.getAttribute(attributeName);

        if (defaultFont == null || defaultFont.equals("")) {
            return fontFactory.getInstance();
        }

        String size = config.getAttribute(attributeSize);
        if (size == null) {
            return fontFactory.getInstance(defaultFont);
        }

        Font font = null;
        try {
            float f = Float.parseFloat(size);
            font = fontFactory.getInstance(defaultFont, new Dimen(
                    ((long) (Dimen.ONE * f))));
        } catch (NumberFormatException e) {
            throw new ConfigurationSyntaxException(attributeSize, config
                    .toString());
        }

        return font;
    }

    /**
     * Create a new document writer.
     *
     * @param config the configuration object for the document writer
     * @param jobname the job name to use
     * @param outFactory the output factory
     * @param options the options to be passed to the document writer
     * @param colorConfig the configuration for the color converter
     * @param finder the resource finder if one is requested
     *
     * @return the new document writer
     *
     * @throws DocumentWriterException in case of an error
     * @throws ConfigurationException in case of a configuration problem
     */
    protected DocumentWriter makeDocumentWriter(final Configuration config,
            final String jobname, final OutputStreamFactory outFactory,
            final DocumentWriterOptions options,
            final Configuration colorConfig, final ResourceFinder finder)
            throws DocumentWriterException,
                ConfigurationException {

        DocumentWriterFactory factory = new DocumentWriterFactory(config);
        factory.enableLogging(logger);
        DocumentWriter docWriter = factory.newInstance(//
                properties.getProperty(PROP_OUTPUT_TYPE), //
                options, //
                outFactory);
        if (docWriter instanceof PropertyConfigurable) {
            ((PropertyConfigurable) docWriter).setProperties(properties);
        }
        if (docWriter instanceof ColorAware) {
            ((ColorAware) docWriter)
                    .setColorConverter(makeColorConverter(colorConfig));
        }
        if (docWriter instanceof ResourceConsumer) {
            ((ResourceConsumer) docWriter).setResourceFinder(finder);
        }
        docWriter.setParameter("Creator", "ExTeX " + new Version().toString());
        docWriter.setParameter("Title", "");
        docWriter.setParameter("Paper", "A4");
        docWriter.setParameter("Orientation", "Portrait");
        docWriter.setParameter("Pages", "*");
        docWriter.setParameter("PageOrder", "Ascend");

        return docWriter;
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
    protected FontFactory makeFontFactory(final Configuration config,
            final ResourceFinder finder) throws ConfigurationException {

        FontFactory fontFactory;
        String fontClass = config.getAttribute("class");

        if (fontClass == null || fontClass.equals("")) {
            throw new ConfigurationMissingAttributeException("class", config);
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
            ((PropertyConfigurable) fontFactory).setProperties(properties);
        }

        return fontFactory;
    }

    /**
     * Create a new interpreter.
     *
     * @param config the configuration object for the interpreter
     * @param factory the factory for new token streams
     * @param fontFactory the font factory to request the default font from
     *
     * @return the new interpreter
     *
     * @throws ConfigurationException in case that some kind of problems have
     * been detected in the configuration
     * @throws GeneralException in case of an error of some other kind
     * @throws FontException in case of problems with the font itself
     */
    protected Interpreter makeInterpreter(final Configuration config,
            final TokenStreamFactory factory, final FontFactory fontFactory)
            throws ConfigurationException,
                GeneralException,
                FontException {

        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config);
        interpreterFactory.enableLogging(logger);

        Interpreter interpreter = interpreterFactory.newInstance();

        if (interpreter instanceof PropertyConfigurable) {
            ((PropertyConfigurable) interpreter).setProperties(properties);
        }

        ErrorHandler errHandler = errorHandler;
        if (errHandler == null) {
            ErrorHandlerFactory errorHandlerFactory = new ErrorHandlerFactory(
                    config.getConfiguration(TAG_ERRORHANDLER));
            errorHandlerFactory.enableLogging(logger);
            errHandler = errorHandlerFactory.newInstance(properties
                    .getProperty(PROP_ERROR_HANDLER));
        }
        interpreter.setErrorHandler(errHandler);
        Context context = interpreter.getContext();
        factory.setOptions((TokenStreamOptions) context);
        interpreter.setTokenStreamFactory(factory);
        interpreter.setFontFactory(fontFactory);
        interpreter.setInteraction(Interaction.get(properties
                .getProperty(PROP_INTERACTION)));
        context.registerInteractionObserver(interactionObserver);

        Configuration fontConfiguration = config.getConfiguration(TAG_FONT);

        if (fontConfiguration == null) {
            throw new ConfigurationMissingException(TAG_FONT, config.toString());
        }
        context.set(makeDefaultFont(fontConfiguration, fontFactory), true);
        context.set(context.getLanguage("0"), true);

        initializeStreams(interpreter, properties);

        return interpreter;
    }

    /**
     * Create a new Handler for the log file.
     *
     * @param logFile the name of the log file
     *
     * @return the new handler
     */
    protected Handler makeLogFileHandler(final String logFile) {

        Handler fileHandler = null;
        try {
            fileHandler = new StreamHandler(new FileOutputStream(logFile),
                    new LogFormatter());
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        } catch (SecurityException e) {
            logger.severe(localizer.format("ExTeX.LogFileError", e
                    .getLocalizedMessage()));
            fileHandler = null;
        } catch (IOException e) {
            logger.severe(localizer.format("ExTeX.LogFileError", e
                    .getLocalizedMessage()));
            fileHandler = null;
        }
        return fileHandler;
    }

    /**
     * Create a ResourceFinder.
     * Implicitly the logger and the properties are used.
     *
     * @param config the configuration
     *
     * @return the new resource finder
     *
     * @throws ConfigurationException in case of an configuration error
     */
    protected ResourceFinder makeResourceFinder(final Configuration config)
            throws ConfigurationException {

        return (new ResourceFinderFactory()).createResourceFinder(config
                .getConfiguration("Resource"), logger, properties);
    }

    /**
     * Create a TokenStreamFactory.
     * Implicitly the logger is used.
     *
     * @param config the configuration object for the token stream factory
     * @param finder the file finder for the token stream factory
     *
     * @return the token stream factory
     *
     * @throws ConfigurationException in case that some kind of problems have
     * been detected in the configuration
     * @throws NotObservableException in case that the observer for file
     * events could not be registered
     */
    protected TokenStreamFactory makeTokenStreamFactory(
            final Configuration config, final ResourceFinder finder)
            throws ConfigurationException,
                NotObservableException {

        TokenStreamFactory factory = new TokenStreamFactory(config, properties
                .getProperty(PROP_TOKEN_STREAM));

        factory.enableLogging(logger);
        factory.setResourceFinder(finder);
        factory.registerObserver(new FileOpenObserver(logger));

        return factory;
    }

    /**
     * Create a new typesetter.
     *
     * @param config the configuration object for the typesetter
     * @param docWriter the document writer to be used as back end
     * @param context the interpreter context
     *
     * @return the new typesetter
     *
     * @throws ConfigurationException in case that some kind of problems have
     *  been detected in the configuration
     * @throws TypesetterException in case of an error
     */
    protected Typesetter makeTypesetter(final Configuration config,
            final DocumentWriter docWriter, final Context context)
            throws TypesetterException,
                ConfigurationException {

        TypesetterFactory factory = new TypesetterFactory(config);
        factory.enableLogging(logger);
        Typesetter typesetter = factory.newInstance(properties
                .getProperty(PROP_TYPESETTER_TYPE), context);
        typesetter.setDocumentWriter(docWriter);

        return typesetter;
    }

    /**
     * Set a property to a given value if not set yet.
     *
     * @param name the name of the property
     * @param value the default value
     */
    protected void propertyDefault(final String name, final String value) {

        if (!properties.containsKey(name) && value != null) {
            properties.setProperty(name, value);
        }
    }

    /**
     * Run the program with the parameters already stored in the properties.
     *
     * @throws MainException in case of an error
     */
    public void run() throws MainException {

        final String jobname = determineJobname();
        final String logFile = new File(properties.getProperty(PROP_OUTPUTDIR),
                jobname + ".log").getPath();

        Handler fileHandler = makeLogFileHandler(logFile);

        try {
            Configuration config = new ConfigurationFactory()
                    .newInstance(properties.getProperty(PROP_CONFIG));
            showBanner(config, (showBanner ? Level.INFO : Level.FINE));

            OutputFactory outFactory = new OutputFactory(//
                    config.getConfiguration("Output"), //
                    new String[]{properties.getProperty(PROP_OUTPUTDIR),
                            properties.getProperty(PROP_OUTPUTDIR_FALLBACK)},
                    jobname);
            outFactory.setDefaultStream(outStream);

            ResourceFinder finder = makeResourceFinder(config);
            if (Boolean.valueOf(properties.getProperty(PROP_TRACE_INPUT_FILES))
                    .booleanValue()) {
                finder.enableTracing(true);
            }
            TokenStreamFactory tokenStreamFactory = makeTokenStreamFactory(
                    config.getConfiguration("Scanner"), finder);

            FontFactory fontFactory = makeFontFactory(config
                    .getConfiguration("Fonts"), finder);

            Interpreter interpreter = makeInterpreter(//
                    config.getConfiguration("Interpreter"), //
                    tokenStreamFactory, fontFactory);

            DocumentWriter docWriter = makeDocumentWriter(//
                    config.getConfiguration("DocumentWriter"), //
                    jobname, //
                    outFactory, //
                    (DocumentWriterOptions) interpreter.getContext(), //
                    config.getConfiguration("ColorConverter"), finder);

            Typesetter typesetter = makeTypesetter(//
                    config.getConfiguration("Typesetter"), docWriter,
                    interpreter.getContext());
            typesetter.setOutputRoutine(new TeXOutputRoutine(interpreter));
            interpreter.setTypesetter(typesetter);

            loadFormat(interpreter, finder, properties.getProperty(PROP_FMT),
                    jobname);

            interpreter.run();

            int pages = docWriter.getPages();
            logger.info(localizer.format((pages == 0
                    ? "ExTeX.NoPages"
                    : pages == 1 ? "ExTeX.Page" : "ExTeX.Pages"), //
                    outFactory.getDestination(), Integer.toString(pages)));
        } catch (ConfigurationException e) {
            logger.throwing(this.getClass().getName(), "run", e);
            throw new MainConfigurationException(e);
        } catch (CharacterCodingException e) {
            logger.throwing(this.getClass().getName(), "run", e);
            throw new MainCodingException(e);
        } catch (IOException e) {
            logger.throwing(this.getClass().getName(), "run", e);
            throw new MainIOException(e);
        } catch (MainException e) {
            logger.throwing(this.getClass().getName(), "run", e);
            throw e;
        } catch (GeneralException e) {
            logger.throwing(this.getClass().getName(), "run", e);
            throw new MainException(e);
        } catch (Throwable e) {
            logInternalError(e);
        } finally {
            if (fileHandler != null) {
                fileHandler.close();
                logger.removeHandler(fileHandler);
                // see "TeX -- The Program [1333]"
                logger.info(localizer.format("ExTeX.Logfile", logFile));
            }
        }
    }

    /**
     * Setter for errorHandler.
     *
     * @param handler the errorHandler to set.
     */
    public void setErrorHandler(final ErrorHandler handler) {

        this.errorHandler = handler;
    }

    /**
     * Setter for logger.
     *
     * @param aLogger the logger to set.
     */
    public void setLogger(final Logger aLogger) {

        this.logger = aLogger;
    }

    /**
     * Setter for outStream.
     *
     * @param outputStream the outStream to set.
     */
    public void setOutStream(final OutputStream outputStream) {

        this.outStream = outputStream;
    }

    /**
     * Setter for a named property.
     *
     * @param key the property name
     * @param value the new value of the named property
     */
    protected void setProperty(final String key, final String value) {

        this.properties.setProperty(key, value);
    }

    /**
     * Print the program banner to the logger stream and remember that this has
     * been done already to avoid repeating.
     *
     * @param configuration the configuration to use
     * @param priority the log level
     *
     * @throws MainException in case of an error
     */
    protected void showBanner(final Configuration configuration,
            final Level priority) throws MainException {

        if (showBanner) {
            String banner;
            if (configuration != null) {
                try {
                    banner = configuration.getConfiguration("banner")
                            .getValue();
                } catch (ConfigurationException e) {
                    banner = properties.getProperty("java.version");
                }
            } else {
                banner = properties.getProperty("java.version");
            }

            logger.log(priority, localizer.format("ExTeX.Version", //
                    properties.getProperty(PROP_PROGNAME), //
                    EXTEX_VERSION, //
                    banner));
            showBanner = false;
        }
    }
}