/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.CharacterCodingException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterFactory;
import de.dante.extex.font.FontFactory;
import de.dante.extex.i18n.Messages;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.InterpreterFactory;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Font;
import de.dante.extex.logging.LogFormatter;
import de.dante.extex.main.ErrorHandlerImpl;
import de.dante.extex.main.FileCloseObserver;
import de.dante.extex.main.FileFinderImpl;
import de.dante.extex.main.FileOpenObserver;
import de.dante.extex.main.InteractionObserver;
import de.dante.extex.main.LogMessageObserver;
import de.dante.extex.main.MainCodingException;
import de.dante.extex.main.MainConfigurationException;
import de.dante.extex.main.MainException;
import de.dante.extex.main.MainIOException;
import de.dante.extex.main.MainMissingArgumentException;
import de.dante.extex.main.MainUnknownInteractionException;
import de.dante.extex.main.MainUnknownOptionException;
import de.dante.extex.main.MessageObserver;
import de.dante.extex.main.TokenObserver;
import de.dante.extex.main.TokenPushObserver;
import de.dante.extex.main.TraceObserver;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterFactory;
import de.dante.util.GeneralException;
import de.dante.util.StringList;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationFactory;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationMissingException;
import de.dante.util.configuration.ConfigurationNoSuchMethodException;
import de.dante.util.configuration.ConfigurationSyntaxException;
import de.dante.util.configuration.ConfigurationUnsupportedEncodingException;
import de.dante.util.file.FileFinder;
import de.dante.util.file.FileFinderConfigImpl;
import de.dante.util.file.FileFinderDirect;
import de.dante.util.file.FileFinderList;
import de.dante.util.file.FileFinderPathImpl;
import de.dante.util.file.OutputFactory;
import de.dante.util.observer.NotObservableException;
import de.dante.util.observer.Observer;

/**
 * This is the command line interface to ExTeX. It does all the horrible things
 * necessary to interact with the user of the command line in nearly the same
 * way as TeX does.
 * <p>
 * The command line interface provides the following features:
 * </p>
 * <ul>
 * <li>Specifying format, input file and TeX code on the command line.</li>
 * <li>Interacting with the user to get a input file.</li>
 * <li>Interacting with the user in case on an error</li>
 * </ul>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 *
 * @version $Revision: 1.41 $
 */
public class ExTeX {

    /**
     * The constant <tt>COPYRIGHT_YEAR</tt> contains the starting year of
     * development for the copyright message. This number is fixed to be the
     * year 2003.
     */
    private static final int COPYRIGHT_YEAR = 2003;

    /**
     * The field <tt>DOT_EXTEX</tt> contains the name of the user properties
     * file. This file contains property settings which are read when ExTeX is
     * started.
     */
    private static final String DOT_EXTEX = ".extex";

    /**
     * The constant <tt>EXIT_OK</tt> contains the exit code of the program for
     * the success case.
     */
    private static final int EXIT_OK = 0;

    /**
     * The constant <tt>EXIT_INTERNAL_ERROR</tt> contains the exit code for
     * internal errors.
     */
    private static final int EXIT_INTERNAL_ERROR = -666;

    /**
     * The field <tt>PROP_CODE</tt> contains the name of the
     * property for the TeX code to be inserted at the beginning of the job.
     */
    private static final String PROP_CODE = "extex.code";

    /**
     * The field <tt>PROP_CONFIG</tt> contains the name of the
     * property for the configuration resource to use.
     */
    private static final String PROP_CONFIG = "extex.config";

    /**
     * The field <tt>PROP_ENCODING</tt> contains the name of the property for
     * the standard encoding to use.
     */
    private static final String PROP_ENCODING = "extex.encoding";

    /**
     * The field <tt>PROP_FALLBACKOUTPUTDIR</tt> contains the name of the
     * property for the fallback if the output directory fails to be writable.
     */
    private static final String PROP_FALLBACKOUTPUTDIR = "extex.FallbackOutputdir";

    /**
     * The field <tt>PROP_FILE</tt> contains the name of the property for the
     * input file to read.
     */
    private static final String PROP_FILE = "extex.file";

    /**
     * The field <tt>PROP_FMT</tt> contains the name of the property for the
     * name of the format file to use.
     */
    private static final String PROP_FMT = "extex.fmt";

    /**
     * The field <tt>PROP_INI</tt> contains the name of the property for the
     * boolean value indicating that some kind of emulations for iniTeX should
     * be provided. Currently this has no effect in ExTeX.
     */
    private static final String PROP_INI = "extex.ini";

    /**
     * The field <tt>PROP_INTERACTION</tt> contains the name of the
     * property for the interaction mode.
     */
    private static final String PROP_INTERACTION = "extex.interaction";

    /**
     * The field <tt>PROP_JOBNAME</tt> contains the name of the
     * property for the job name. The value can be overruled by the property
     * named in <tt>PROP_JOBNAME_MASTER</tt>.
     */
    private static final String PROP_JOBNAME = "extex.jobname";

    /**
     * The field <tt>PROP_JOBNAME_MASTER</tt> contains the name of the
     * property for the jobname to be used with high priority.
     */
    private static final String PROP_JOBNAME_MASTER = "extex.jobnameMaster";

    /**
     * The field <tt>PROP_LANG</tt> contains the name of the property for the
     * language to use for messages.
     */
    private static final String PROP_LANG = "extex.lang";

    /**
     * The field <tt>PROP_NO_BANNER</tt> contains the name of the property for
     * the boolean value indicating whether or not to show a program banner.
     */
    private static final String PROP_NO_BANNER = "extex.nobanner";

    /**
     * The field <tt>PROP_OUTPUT</tt> contains the name of the property for the
     * output driver. This value is resolved by the DocumentWriterFactory to
     * find the appropriate class.
     */
    private static final String PROP_OUTPUT = "extex.output";

    /**
     * The field <tt>PROP_OUTPUTDIR</tt> contains the name of the
     * property for the output directory.
     */
    private static final String PROP_OUTPUTDIR = "extex.outputdir";

    /**
     * The field <tt>PROP_POOL</tt> contains the name of the property for the
     * messages resource.
     */
    private static final String PROP_POOL = "extex.pool";

    /**
     * The field <tt>PROP_PROGNAME</tt> contains the name of the
     * property for the program name used in usage messages.
     */
    private static final String PROP_PROGNAME = "extex.progname";

    /**
     * The field <tt>PROP_TEXINPUTS</tt> contains the name of the
     * property for the ...
     */
    private static final String PROP_TEXINPUTS = "extex.texinputs";

    /**
     * The field <tt>PROP_TRACE_MACROS</tt> contains the name of the
     * property for the boolean determining whether or not the execution of
     * macros should produce tracing output.
     */
    private static final String PROP_TRACE_MACROS = "extex.traceMacros";

    /**
     * The field <tt>PROP_TRACE_TOKENIZER</tt> contains the name of the
     * property for the boolean determining whether or not the tokenizer
     * should produce tracing output.
     */
    private static final String PROP_TRACE_TOKENIZER = "extex.traceTokenizer";

    /**
     * The constant <tt>VERSION</tt> contains the manually incremented version
     * string.
     */
    private static final String VERSION = "0.7";

    /**
     * The field <tt>calendar</tt> contains the time and date when ExTeX has
     * been started. There is another instance in
     * {@link de.dante.extex.interpreter.Max Max}. Those two instances might be
     * different up to a few milliseconds. I guess I can live with that.
     */
    private Calendar calendar = Calendar.getInstance();

    /**
     * The field <tt>interactionObserver</tt> contains the observer called
     * whenever the interaction mode is changed.
     */
    private Observer interactionObserver = null;

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
     * <p>
     * The following properties are recognized:
     * </p>
     * <table>
     * <tr>
     * <th>Name</th>
     * <th>Default</th>
     * <th></th>
     * Description</th>
     * <tr>
     * <td>extex.progname</td>
     * ExTeX
     * <td></td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>extex.file</td>
     * <td></td>
     * <td>This parameter contains the file to read from. It has no default
     * </td>
     * </tr>
     * <tr>
     * <td>extex.code</td>
     * <td></td>
     * <td>This parameter contains TeX code to be executed directly. The
     * execution is performed after any code specified in an input file.</td>
     * </tr>
     * <tr>
     * <td>extex.config</td>
     * <td>extex.xml</td>
     * <td>This parameter contains the name of the configuration file to use.
     * This configuration file is sought on the classpath.</td>
     * </tr>
     * <tr>
     * <td>extex.ini</td>
     * <td></td>
     * <td>If set to <code>true</code> then act as initex.</td>
     * </tr>
     * <tr>
     * <td>extex.interaction</td>
     * <td>3</td>
     * <td>This parameter contains the interaction mode. possible values are
     * the numbers 0..3 and the symbolic names batchmode (0), nonstopmode (1),
     * scrollmode (2), and errorstopmode (3).</td>
     * </tr>
     * <tr>
     * <td>extex.jobname</td>
     * <td>texput</td>
     * <td>This parameter contains the name of the job.</td>
     * </tr>
     * <tr>
     * <td>extex.fmt</td>
     * <td></td>
     * <td>This parameter contains the name of the format to read. An empty
     * string denotes that no format should be read.</td>
     * </tr>
     * <tr>
     * <td>extex.encoding</td>
     * <td>ISO-8859-1</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>extex.texinputs</td>
     * <td></td>
     * <td>...</td>
     * </tr>
     * <tr>
     * <td>extex.outputdir</td>
     * <td>.</td>
     * <td>This parameter contain the directory where output files should be
     * created.</td>
     * </tr>
     * <tr>
     * <td>extex.output</td>
     * <td>pdf</td>
     * <td>This parameter contain the output format.</td>
     * </tr>
     * </table>
     *
     * @param anyProperties the properties to start with
     *
     * @throws MainException in case of an error
     */
    public ExTeX(final Properties anyProperties) throws MainException {

        super();

        this.properties = anyProperties;
        propertyDefault(PROP_CODE, "");
        propertyDefault(PROP_CONFIG, "extex.xml");
        propertyDefault(PROP_ENCODING, "ISO-8859-1");
        propertyDefault(PROP_FILE, "");
        propertyDefault(PROP_FMT, "");
        propertyDefault(PROP_INI, "");
        propertyDefault(PROP_INTERACTION, "3");
        propertyDefault(PROP_JOBNAME, "texput");
        propertyDefault(PROP_JOBNAME_MASTER, "");
        propertyDefault(PROP_NO_BANNER, "");
        propertyDefault(PROP_LANG, "");
        propertyDefault(PROP_OUTPUT, "pdf");
        propertyDefault(PROP_OUTPUTDIR, ".");
        propertyDefault(PROP_POOL, "config.extexMessage");
        propertyDefault(PROP_PROGNAME, "ExTeX");
        propertyDefault(PROP_TEXINPUTS, "");
        propertyDefault(PROP_TRACE_MACROS, "");
        propertyDefault(PROP_TRACE_TOKENIZER, "");

        applyLanguage();

        logger = Logger.getLogger(getClass().getName());
        logger.setUseParentHandlers(false);

        Handler consoleHandler = new ConsoleHandler();
        logger.addHandler(consoleHandler);
        consoleHandler.setFormatter(new LogFormatter());
        interactionObserver = new InteractionObserver(consoleHandler);
        applyInteraction();
    }

    /**
     * Creates a new object and initializes the properties from given
     * properties and possibly from a user's properties in the file
     * <tt>.extex</tt>.
     * The user properties are loaded from the users home directory and the
     * current directory.
     *
     * @param anyProperties the properties to consider
     * @param dotFile the name of the local configuration file. In the case
     *            that this value is <code>null</code> no user properties
     *            will be considered.
     *
     * @throws MainException in case of an IO Error during the reading of the
     *             properties file
     *
     * @see #ExTeX(java.util.Properties)
     */
    public ExTeX(final Properties anyProperties, final String dotFile)
            throws MainException {

        this(anyProperties);

        if (dotFile != null) {
            try {
                loadUserProperties(new File(System.getProperty("user.home"),
                        dotFile));
                loadUserProperties(new File(dotFile));
            } catch (IOException e) {
                throw new MainIOException(e);
            }

            applyLanguage();
        }
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
     * Setter for logger.
     *
     * @param aLogger the logger to set.
     */
    public void setLogger(final Logger aLogger) {

        this.logger = aLogger;
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
     * Setter for outStream.
     *
     * @param outputStream the outStream to set.
     */
    public void setOutStream(final OutputStream outputStream) {

        this.outStream = outputStream;
    }

    /**
     * This is the main method which is invoked to run the whole engine from
     * the command line. It creates a new ExTeX object and invokes
     * <tt>{@link #run(java.lang.String[]) run()}</tt> on it.
     * <p>
     * The return value is used as the exit status.
     * </p>
     * <p>
     * The properties to be used are taken from the
     * <tt>{@link java.lang.System#getProperties() System.properties}</tt> and
     * the user's properties in the file <tt>.extex</tt>. The user properties
     * are loaded both from the users home directory and the current directory.
     * Finally the properties can be overwritten on the command line.
     * </p>
     *
     * @param args the list of command line arguments
     *
     * @see #ExTeX(java.util.Properties,java.lang.String)
     */
    public static void main(final String[] args) {

        int status;

        try {
            ExTeX extex = new ExTeX(System.getProperties(), DOT_EXTEX);
            status = extex.run(args);
        } catch (Throwable e) {
            System.err.println(Messages.format("ExTeX.SevereError", //
                                               e.toString()));
            e.printStackTrace();
            status = EXIT_INTERNAL_ERROR;
        }

        System.exit(status);
    }

    /**
     * This class provides access to the whole functionality of ExTeX on the
     * command line. The exception is that this method does not call
     * <code>{@link System#exit(int) System.exit()}</code>
     * but returns the exit status as result.
     *
     * @param args the list of command line arguments
     *
     * @return the exit status
     */
    public int run(final String[] args) {

        boolean onceMore = true;
        int returnCode = EXIT_OK;

        try {

            for (int i = 0; onceMore && i < args.length; i++) {
                String arg = args[i];

                if (arg.startsWith("-")) {
                    if ("-".equals(arg)) {
                        runWithFile(args, i + 1);
                        onceMore = false;
                    } else if ("-configuration".startsWith(arg)) {
                        useArg(PROP_CONFIG, args, ++i);
                    } else if ("-copyright".startsWith(arg)) {
                        int year = calendar.get(Calendar.YEAR);
                        logger
                                .info(Messages
                                        .format(
                                                "ExTeX.Copyright",
                                                (year <= COPYRIGHT_YEAR ? Integer
                                                        .toString(COPYRIGHT_YEAR)
                                                        : Integer
                                                                .toString(COPYRIGHT_YEAR)
                                                          + "-"
                                                          + Integer
                                                                  .toString(year))));
                        onceMore = false;
                    } else if ("-help".startsWith(arg)) {
                        logger.info(Messages.format("ExTeX.Usage", "extex"));
                        onceMore = false;
                    } else if ("-fmt".startsWith(arg)) {
                        useArg(PROP_FMT, args, ++i);
                    } else if (arg.startsWith("-fmt=")) {
                        properties.setProperty(PROP_FMT, arg.substring("-fmt="
                                .length()));
                    } else if ("-halt-on-error".startsWith(arg)) {
                        properties.setProperty("halt-on-error", "true");
                    } else if ("-interaction".startsWith(arg)) {
                        useArg(PROP_INTERACTION, args, ++i);
                        applyInteraction();
                    } else if ("-ini".startsWith(arg)) {
                        properties.setProperty(PROP_INI, "true");
                    } else if (arg.startsWith("-interaction=")) {
                        properties.setProperty(PROP_INTERACTION, arg
                                .substring("-interaction=".length()));
                        applyInteraction();
                    } else if ("-job-name".startsWith(arg)) {
                        useArg(PROP_JOBNAME_MASTER, args, ++i);
                    } else if (arg.startsWith("-job-name=")) {
                        properties.setProperty(PROP_JOBNAME_MASTER, arg
                                .substring("-job-name=".length()));
                    } else if ("-language".startsWith(arg)) {
                        useArg(PROP_LANG, args, ++i);
                        applyLanguage();
                    } else if ("-progname".startsWith(arg)) {
                        useArg(PROP_PROGNAME, args, ++i);
                    } else if (arg.startsWith("-progname=")) {
                        properties.setProperty(PROP_PROGNAME, arg
                                .substring("-progname=".length()));
                    } else if ("-version".startsWith(arg)) {
                        logger
                                .info(Messages
                                        .format(
                                                "ExTeX.Version",
                                                properties
                                                        .getProperty(PROP_PROGNAME),
                                                VERSION,
                                                properties
                                                        .getProperty("java.version")));
                        onceMore = false;
                    } else if ("-output".startsWith(arg)) {
                        useArg(PROP_OUTPUT, args, ++i);
                    } else if ("-texinputs".startsWith(arg) && arg.length() > 4) {
                        useArg(PROP_TEXINPUTS, args, ++i);
                    } else if ("-texoutputs".startsWith(arg)
                               && arg.length() > 4) {
                        useArg(PROP_OUTPUTDIR, args, ++i);
                    } else if ("-texmfoutputs".startsWith(arg)
                               && arg.length() > 4) {
                        useArg("extex.fallbackOutputdir", args, ++i);
                    } else if ("-debug".startsWith(arg)) {
                        useDebug(args, ++i);
                    } else if ("--".equals(arg)) {
                        useArg(PROP_CONFIG, args, ++i);
                    } else if (!loadArgumentFile(arg.substring(1))) {
                        throw new MainUnknownOptionException(arg);
                    }
                } else if (arg.startsWith("&")) {
                    properties.setProperty(PROP_FMT, arg.substring(1));
                    runWithFile(args, i + 1);
                    onceMore = false;
                } else if (arg.startsWith("\\")) {
                    runWithArgs(args, i);
                    onceMore = false;
                } else if (!arg.equals("")) {
                    runWithFile(args, i);
                    onceMore = false;
                }
            }

            if (onceMore) {
                run();
            }
        } catch (MainException e) {
            showBanner();
            logException(e.getMessage(), e);
            returnCode = e.getCode();
        } catch (Throwable e) {
            showBanner();
            logInternalError(e);
            logger.info(Messages.format("ExTeX.Logfile", properties
                    .getProperty(PROP_JOBNAME)));

            returnCode = EXIT_INTERNAL_ERROR;
        }

        return returnCode;
    }

    /**
     * ...
     *
     * @param arg the name of the resource to load
     *
     * @return <code>true</code> iff the resource has been loaded sucessfully
     *
     * @throws IOException just in case
     */
    private boolean loadArgumentFile(final String arg) throws IOException {

        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("config.extex." + arg);
        if (is == null) {
            try {
                is = new FileInputStream(new File(".extexcfg", arg));
            } catch (FileNotFoundException e) {
                return false;
            }
        }
        properties.load(is);

        return true;
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

        showBanner = !Boolean.valueOf(properties.getProperty(PROP_NO_BANNER))
                .booleanValue();

        Handler fileHandler = makeLogFileHandler(logFile);

        try {
            Configuration config = new ConfigurationFactory()
                    .newInstance(properties.getProperty(PROP_CONFIG));

            OutputFactory outFactory = new OutputFactory(config
                    .getConfiguration("Output"), new String[]{
                    properties.getProperty(PROP_OUTPUTDIR),
                    properties.getProperty(PROP_FALLBACKOUTPUTDIR)});

            showBanner();

            FileFinder finder = makeFileFinder(config.getConfiguration("File"));
            TokenStreamFactory tokenStreamFactory //
            = makeTokenStreamFactory(config.getConfiguration("Scanner"), finder);

            FontFactory fontFactory = makeFontFactory(config
                    .getConfiguration("Fonts"));

            DocumentWriter docWriter = makeDocumentWriter(config
                    .getConfiguration("DocumentWriter"), jobname, outFactory);

            Interpreter interpreter = makeInterpreter(config
                    .getConfiguration("Interpreter"), config
                    .getConfiguration("Typesetter"), finder,
                                                      tokenStreamFactory,
                                                      docWriter, fontFactory);

            loadFormat(interpreter, properties.getProperty(PROP_FMT), jobname);

            interpreter.run();

            outStream.close();

            int pages = docWriter.getPages();
            String outname = jobname + "." + docWriter.getExtension();
            logger.info(Messages.format((pages == 0 ? "ExTeX.NoPages"
                    : pages == 1 ? "ExTeX.Page" : "ExTeX.Pages"), outname,
                                        Integer.toString(pages)));
        } catch (ConfigurationException e) {
            logger.throwing(this.getClass().getName(), "run", e);
            throw new MainConfigurationException(e);
        } catch (CharacterCodingException e) {
            logger.throwing(this.getClass().getName(), "run", e);
            throw new MainCodingException(e);
        } catch (IOException e) {
            logger.throwing(this.getClass().getName(), "run", e);
            throw new MainIOException(e);
        } catch (GeneralException e) {
            logger.throwing(this.getClass().getName(), "run", e);
            throw new MainException(e);
        } catch (Throwable e) {
            logInternalError(e);
        } finally {
            if (fileHandler != null) {
                fileHandler.close();
                logger.removeHandler(fileHandler);
                /*
                 * see "TeX -- The Program [1333]"
                 */
                logger.info(Messages.format("ExTeX.Logfile", logFile));
            }
        }
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
        }
        jobname = new File(jobname).getName();
        return jobname;
    }

    /**
     * Create a new font factory.
     *
     * @param config the configuration object for the font factory
     *
     * @return the new font factory
     *
     * @throws ConfigurationException in case that some kind of problems have
     * been detected in the configuration
     */
    private FontFactory makeFontFactory(final Configuration config)
            throws ConfigurationException {

        FontFactory fontFactory;
        String fontClass = config.getAttribute("class");

        if (fontClass == null || fontClass.equals("")) {
            throw new ConfigurationMissingAttributeException("class", config);
        }

        FileFinder fontFinder = makeFontFileFinder(config);

        try {
            fontFactory = (FontFactory) (Class.forName(fontClass)
                    .getConstructor(new Class[]{FileFinder.class})
                    .newInstance(new Object[]{fontFinder}));
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

        return fontFactory;
    }

    /**
     * Create a new interpreter.
     *
     * @param config the configuration object for the interpreter
     * @param typesetterConfig the configuration object for the typesetter
     * @param finder the file finder for files opened by the interpreter
     * @param factory the factory for new token streams
     * @param docWriter the document writer
     * @param fontFactory the font factory to request the default font from
     *
     * @return the new interpreter
     *
     * @throws ConfigurationException in case that some kind of problems have
     * been detected in the configuration
     * @throws GeneralException in case of an error of some other kind
     */
    private Interpreter makeInterpreter(final Configuration config,
            final Configuration typesetterConfig, final FileFinder finder,
            final TokenStreamFactory factory, final DocumentWriter docWriter,
            final FontFactory fontFactory) throws GeneralException,
            ConfigurationException {

        Interpreter interpreter = new InterpreterFactory(config).newInstance();
        interpreter.setErrorHandler(new ErrorHandlerImpl(logger));
        interpreter.setFileFinder(finder);
        interpreter.setTokenStreamFactory(factory);
        interpreter.setFontFactory(fontFactory);
        interpreter.setInteraction(Interaction.get(properties
                .getProperty(PROP_INTERACTION)));

        Configuration fontConfiguration = config.getConfiguration("Font");

        if (fontConfiguration == null) {
            throw new ConfigurationMissingException("Font", config.toString());
        }
        interpreter.getContext()
                .setTypesettingContext(
                                       makeDefaultFont(fontConfiguration,
                                                       fontFactory));

        initializeStreams(interpreter);

        Typesetter typesetter = makeTypesetter(typesetterConfig, docWriter,
                                               interpreter.getContext());

        interpreter.setTypesetter(typesetter);

        interpreter.registerObserver("close", new FileCloseObserver(logger));
        interpreter.registerObserver("message", new MessageObserver(logger));
        interpreter.registerObserver("log", new LogMessageObserver(logger));
        if (Boolean.valueOf(properties.getProperty(PROP_TRACE_TOKENIZER))
                .booleanValue()) {
            interpreter.registerObserver("pop", new TokenObserver(logger));
            interpreter.registerObserver("push", new TokenPushObserver(logger));
        }
        if (Boolean.valueOf(properties.getProperty(PROP_TRACE_MACROS))
                .booleanValue()) {
            interpreter.registerObserver("macro", new TraceObserver(logger));
        }

        return interpreter;
    }

    /**
     * Creat a default font for the interpreter context.
     *
     * @param config the configuration object for the font
     * @param fontFactory the font factory to request the font from
     *
     * @return the default font
     *
     * @throws ConfigurationException in case that some kind of problems have
     * been detected in the configuration
     * @throws GeneralException in case of an error of some other kind
     */
    private Font makeDefaultFont(final Configuration config,
            final FontFactory fontFactory)
            throws ConfigurationException, GeneralException {

        String defaultFont = config.getAttribute("default");

        if (defaultFont == null || defaultFont.equals("")) {
            throw new ConfigurationMissingAttributeException("default",
                    config);
        }

        String size = config.getAttribute("size");
        if (size == null) {
            throw new ConfigurationMissingAttributeException("size",
                    config);
        }

        Font font;
        try {
            float f = Float.parseFloat(size);
            font = fontFactory.getInstance(defaultFont, new Dimen(
                    ((long) (Dimen.ONE * f))));
        } catch (NumberFormatException e) {
            throw new ConfigurationSyntaxException("size",
                    config.toString());
        }

        return font;
    }

    /**
     * Create a new Handler for the log file.
     *
     * @param logFile the name of the log file
     *
     * @return the new handler
     */
    private Handler makeLogFileHandler(final String logFile) {

        Handler fileHandler;
        try {
            fileHandler = new FileHandler(logFile);
            fileHandler.setFormatter(new LogFormatter());
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        } catch (SecurityException e) {
            logger
                    .severe(Messages.format("ExTeX.LogFileError", e
                            .getMessage()));
            fileHandler = null;
        } catch (IOException e) {
            logger.severe(Messages.format("ExTeX.LogFileError", e.toString()));
            fileHandler = null;
        }
        return fileHandler;
    }

    /**
     * Create a new typesetter.
     *
     * @param config the configuration object for the typesetter
     * @param docWriter the document writer to be used as backend
     * @param context the interpreter context
     *
     * @return the new typesetter
     *
     * @throws ConfigurationException in case that some kind of problems have
     * been detected in the configuration
     */
    private Typesetter makeTypesetter(final Configuration config,
            final DocumentWriter docWriter, final Context context)
            throws ConfigurationException {

        Typesetter typesetter = new TypesetterFactory(config)
                .newInstance(context);
        typesetter.setDocumentWriter(docWriter);

        return typesetter;
    }

    /**
     * Create a new document writer.
     *
     * @param config the configuration object for the document writer
     * @param jobname the jobname to use
     * @param outFactory the output factory
     *
     * @return the new document writer
     *
     * @throws ConfigurationException in case that some kind of problems have
     * been detected in the configuration
     * @throws FileNotFoundException in case that the output file could not
     * be opened
     */
    private DocumentWriter makeDocumentWriter(final Configuration config,
            final String jobname, final OutputFactory outFactory)
            throws ConfigurationException, FileNotFoundException {

        DocumentWriter docWriter = new DocumentWriterFactory(config)
                .newInstance(properties.getProperty(PROP_OUTPUT));

        if (outStream == null) {
            outStream = outFactory.createOutputStream(jobname, docWriter
                    .getExtension());
        }
        docWriter.setOutputStream(outStream);

        return docWriter;
    }

    /**
     * Create a TokenStreamFactory.
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
    private TokenStreamFactory makeTokenStreamFactory(
            final Configuration config, final FileFinder finder)
            throws ConfigurationException, NotObservableException {

        TokenStreamFactory factory = new TokenStreamFactory(config);

        factory.setFileFinder(finder);
        factory.registerObserver("file", new FileOpenObserver(logger));
        return factory;
    }

    /**
     * Create a {@link FileFinder FileFinder} to be used for token input
     * streams.
     *
     * @param config the configuration object for the file finder
     *
     * @return the new file finder
     *
     * @throws ConfigurationException in case that some kind of problems have
     * been detected in the configuration
     */
    private FileFinder makeFileFinder(final Configuration config)
            throws ConfigurationException {

        FileFinderList finder = new FileFinderList(new FileFinderDirect(
                new StringList(":tex", ":")));

        String path = properties.getProperty(PROP_TEXINPUTS, "");

        if (!path.equals("")) {
            finder.add(new FileFinderPathImpl(new StringList(path, System
                    .getProperty("path.separator")),
                    new StringList(":tex", ":")));
        }

        finder.add(new FileFinderConfigImpl(config));
        finder.add(new FileFinderImpl(logger));

        return finder;
    }

    /**
     * Create a {@link FileFinder FileFinder} to be used for fonts.
     *
     * @param config the configuration object for the font file finder
     *
     * @return the file finder for fonts
     *
     * @throws ConfigurationException in case that some kind of problems have
     * been detected in the configuration
     */
    private FileFinder makeFontFileFinder(final Configuration config)
            throws ConfigurationException {

        FileFinderList finder = new FileFinderList(new FileFinderDirect(
                new StringList("", ":")));

        String path = properties.getProperty(PROP_TEXINPUTS, "");

        if (!path.equals("")) {
            finder.add(new FileFinderPathImpl(new StringList(path, System
                    .getProperty("path.separator")),
                    new StringList("", ":")));
        }

        finder.add(new FileFinderConfigImpl(config));

        return finder;
    }

    /**
     * Propagate the settings for the interaction mode to the
     * <code>interactionObserver</code>.
     *
     * @throws MainUnknownInteractionException in case that the interaction is
     *             not set properly
     */
    private void applyInteraction() throws MainUnknownInteractionException {

        try {
            interactionObserver.update(null, Interaction.get(properties
                    .getProperty(PROP_INTERACTION)));
        } catch (GeneralException e) {
            throw new MainUnknownInteractionException("");
        }
    }

    /**
     * Try to determine which language to use and configure the Messages
     * accordingly.
     */
    private void applyLanguage() {

        String lang = (String) properties.get(PROP_LANG);
        String bundle = (String) properties.get(PROP_POOL);

        if (lang != null) {
            if (lang.matches("^..$")) {
                Messages.configure(bundle, new Locale(lang));
                return;
            } else if (lang.matches("^..[_-]..$")) {
                Messages.configure(bundle, new Locale(lang.substring(0, 2), //
                        lang.substring(3, 5)));
                return;
            } else if (lang.matches("^..[_-]..[_-]..$")) {
                Messages.configure(bundle, new Locale(lang.substring(0, 2), //
                        lang.substring(3, 5), lang.substring(6, 8)));
                return;
            }
        } else {
            Messages.configure(bundle);
        }
    }

    /**
     * Load properties from a given file if it exists.
     *
     * @param file the file to consider
     *
     * @throws IOException in case of an IO Error during the reading of the
     *             properties file
     */
    private void loadUserProperties(final File file) throws IOException {

        if (file != null && file.canRead()) {
            properties.load(new FileInputStream(file));
        }
    }

    /**
     * Log a throwable including its stack trace to the logger.
     *
     * @param text the prefix text to log
     * @param e the throwable to log
     */
    private void logException(final String text, final Throwable e) {

        logger.severe(text);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(os);
        e.printStackTrace(pw);
        pw.flush();
        logger.fine(os.toString());
    }

    /**
     * Log a throwable including its stack trace to the logger.
     *
     * @param e the throwable to log
     */
    private void logInternalError(final Throwable e) {

        String msg = e.getMessage();
        for (Throwable t = e; t != null && msg == null; t = t.getCause()) {
            msg = t.getMessage();
            if ("".equals(msg)) {
                msg = null;
            }
        }

        if (msg == null) {
            msg = e.getClass().getName();
            msg = msg.substring(msg.lastIndexOf('.') + 1);
        }

        logException(Messages.format("ExTeX.InternalError", msg), e);
    }

    /**
     * Initialize the input streams. If the property <i>extex.file</i> is set
     * and not the empty string, (e.g. from the command line) then this value
     * is used as file name to read from. If the property <i>extex.code</i>
     * is set and not the empty string (e.g. from the command line) then this
     * value is used as initial input after the input file has been processed.
     * Finally, if everything before failed then read input from the stdin
     * stream.
     *
     * @param interpreter the interpreter context
     *
     * @throws ConfigurationException in case of a configuration error
     * @throws MainIOException in case of an IO error
     */

    private void initializeStreams(final Interpreter interpreter)
            throws ConfigurationException, MainIOException {

        TokenStreamFactory factory = interpreter.getTokenStreamFactory();
        boolean notInitialized = true;
        String filename = properties.getProperty(PROP_FILE);

        if (filename != null && !filename.equals("")) {

            try {
                TokenStream stream = factory
                        .newInstance(filename, "tex", properties
                                .getProperty(PROP_ENCODING));
                interpreter.addStream(stream);
                notInitialized = false;
            } catch (FileNotFoundException e) {
                logger.severe(Messages.format("CLI.FileNotFound", filename));
            } catch (IOException e) {
                throw new MainIOException(e);
            }
        }

        String post = properties.getProperty(PROP_CODE);

        if (post != null && !post.equals("")) {
            TokenStream stream = factory.newInstance(post);
            interpreter.addStream(stream);
            notInitialized = false;
        }

        if (notInitialized) {
            try {
                TokenStream stream = factory.newInstance(new InputStreamReader(
                        System.in, properties.getProperty(PROP_ENCODING)));
                interpreter.addStream(stream);
            } catch (UnsupportedEncodingException e) {
                throw new ConfigurationUnsupportedEncodingException(properties
                        .getProperty(PROP_ENCODING), "???");
            }

        }
    }

    /**
     * Load a format if a name of a format is given.
     *
     * @param interpreter the interpreter to delegate the loading to
     * @param fmt the name of the format to use or <code>null</code>
     * @param jobname the name of the job
     *
     * @throws GeneralException in case of some error
     * @throws IOException in case, well, you guess it
     */
    private void loadFormat(final Interpreter interpreter, final String fmt,
            final String jobname) throws IOException, GeneralException {

        String time = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                                                     DateFormat.SHORT,
                                                     Locale.ENGLISH)
                .format(new Date());

        if (fmt != null && !fmt.equals("")) {
            interpreter.loadFormat(fmt);
            logger.config(Messages.format("ExTeX.FormatDate", fmt, time));
        } else {
            logger.config(Messages.format("ExTeX.NoFormatDate", time));
        }
        interpreter.setJobname(jobname);
    }

    /**
     * Set a property to a given value if not set yet.
     *
     * @param name the name of the property
     * @param value the default value
     */
    private void propertyDefault(final String name, final String value) {

        if (!properties.containsKey(name)) {
            properties.setProperty(name, value);
        }
    }

    /**
     * The command line is processed starting at an argument which starts with
     * a backslash. This argument and any following argument are taken as input
     * to the tokenizer.
     *
     * @param arg the list of arguments to process
     * @param index starting index
     *
     * @throws MainException in case of an error in {@link #run() run()}
     */
    private void runWithArgs(final String[] arg, final int index)
            throws MainException {

        if (index < arg.length) {
            StringBuffer in = new StringBuffer();

            for (int i = index; i < arg.length; i++) {
                in.append(" ");
                in.append(arg[i]);
            }

            properties.setProperty(PROP_CODE, in.toString());
        }

        run();
    }

    /**
     * Process the command line arguments when the i <sup>th</sup> argument
     * is a file name. The file is prepared to be read from. The remaining
     * arguments are used as input to the processor.
     *
     * @param arg the list of arguments to process
     * @param idx starting index
     *
     * @throws MainException in case of an error
     */
    private void runWithFile(final String[] arg, final int idx)
            throws MainException {

        if (idx >= arg.length) {
            run();
            return;
        }

        String name = arg[idx];
        properties.setProperty(PROP_JOBNAME, //
                               (name.matches(".*\\.[a-zA-Z0-9]*") //
                                       ? name.substring(0, name
                                               .lastIndexOf(".")) : name));
        properties.setProperty(PROP_FILE, arg[idx]);

        runWithArgs(arg, idx + 1);
    }

    /**
     * Print the program banner to the logger stream and remember that this has
     * been done already to avoid repeating.
     */
    private void showBanner() {

        if (showBanner) {
            logger
                    .info(Messages
                            .format("ExTeX.Version", //
                                    properties.getProperty(PROP_PROGNAME),
                                    VERSION, //
                                    properties.getProperty("java.version")));
            showBanner = false;
        }
    }

    /**
     * Acquire the next argument from the command line and set a property
     * accordingly. If none is found then an exception is thrown.
     *
     * @param name the name of the argument
     * @param arg the list of arguments
     * @param idx the starting index
     *
     * @throws MainMissingArgumentException in case of an error
     */
    private void useArg(final String name, final String[] arg, final int idx)
            throws MainMissingArgumentException {

        if (idx >= arg.length) {
            throw new MainMissingArgumentException(name);
        }

        properties.setProperty(name, arg[idx]);
    }

    /**
     * Acquire the next argument from the command line and use it as a
     * specification to control the tracing features. The appropriate properties
     * are set accordingly.
     *
     * @param arg the list of arguments
     * @param idx the starting index
     *
     * @throws MainException in case of an index overflow
     */
    private void useDebug(final String[] arg, final int idx)
            throws MainException {

        logger.setLevel(Level.FINE);
        if (idx >= arg.length) {
            throw new MainMissingArgumentException("debug");
        }
        String s = arg[idx];
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case 'M' :
                    properties.setProperty(PROP_TRACE_MACROS, "true");
                    break;
                case 'T' :
                    properties.setProperty(PROP_TRACE_TOKENIZER, "true");
                    break;
                default :
                    throw new MainUnknownOptionException(Integer.toString(s
                            .charAt(i)));
            }
        }
    }
}