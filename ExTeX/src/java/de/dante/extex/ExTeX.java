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
package de.dante.extex;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterFactory;
import de.dante.extex.i18n.Messages;
import de.dante.extex.interpreter.ErrorHandler;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.InterpreterFactory;
import de.dante.extex.logging.Logger;
import de.dante.extex.main.ErrorHandlerImpl;
import de.dante.extex.main.FileCloseObserver;
import de.dante.extex.main.FileFinderImpl;
import de.dante.extex.main.FileOpenObserver;
import de.dante.extex.main.MainCodingException;
import de.dante.extex.main.MainConfigurationException;
import de.dante.extex.main.MainException;
import de.dante.extex.main.MainIOException;
import de.dante.extex.main.MainMissingArgumentException;
import de.dante.extex.main.MainOutputFileNotFoundException;
import de.dante.extex.main.MainUnknownOptionException;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterFactory;

import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.nio.charset.CharacterCodingException;

import java.text.DateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.LogManager;

/**
 * This is the command line interface to ExTeX.
 * It does all the horrible things necessary to interact with the user of the
 * command line in nearly the same way as TeX does.
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
 * @version $Revision: 1.1 $
 */
public class ExTeX {
    /** Exit code for success
     */
    private static final int EXIT_OK = 0;

    /** Exit code for internal errors
     */
    private static final int EXIT_INTERNAL_ERROR = -111;

    /** The manually incremented version string
     */
    private static final String VERSION = "0.4";

    /** This calendar instance contains the time and date when ExTeX
     *  has been started
     *  There is another instance in Max. Those two might be different
     *  up to a few milliseconds. I guess I can live with that.
     */
    private Calendar calendar = Calendar.getInstance();

    /** The logger currently in use
     */
    private Logger logger;

    /** The properties containing the settings for the invocation
     */
    private Properties properties;

    /** Boolean indicating that it is necessary to display the banner
     *  This in formation is needed for the cases where errors show up before
     *  the normal banner has been printed.
     */
    private boolean showBanner = true;

    /**
     * Creates a new object and supplies some properties for those keys which
     * are not contained in the properties already.
     * <p>
     * The following properties are recognized:
     * </p>
     * <table>
     *  <tr><th>Name</th><th>Default</th><th></th>Description</th>
     *  <tr><td>extex.progname     </td>ExTeX<td></td>
     *      <td></td></tr>
     *  <tr><td>extex.file         </td><td></td>
     *      *      <td>This is the file to read from. It has no default</td></tr>
     *  <tr><td>extex.trailingInput</td><td></td>
     *      <td></td></tr>
     *  <tr><td>extex.config       </td><td>config/extex.xml</td>
     *      <td></td></tr>
     *  <tr><td>extex.ini          </td><td></td>
     *      <td>If set to <code>true</code> then act as initex</td></tr>
     *  <tr><td>extex.interaction  </td><td>3</td>
     *      <td></td></tr>
     *  <tr><td>extex.jobname      </td><td>texput</td>
     *      <td></td></tr>
     *  <tr><td>extex.fmt          </td><td></td>
     *      <td></td></tr>
     *  <tr><td>extex.encoding     </td><td></td>
     *      <td></td></tr>
     * </table>
     *
     * @param properties the properties to start with
     */
    public ExTeX(Properties properties) throws MainException {
        super();
        this.properties = properties;
        propertyDefault("extex.progname", "ExTeX");
        propertyDefault("extex.file", "");
        propertyDefault("extex.trailingInput", "");
        propertyDefault("extex.interaction", "3");
        propertyDefault("extex.jobname", "texput");
        propertyDefault("extex.jobname2", "");
        propertyDefault("extex.ini", "");
        propertyDefault("extex.fmt", "");
        propertyDefault("extex.outdir", "");
        propertyDefault("extex.encoding", "ISO-8859-1");
        propertyDefault("extex.config", "config/extex.xml");

        useLogger("extex.initial.log", "config/logger.properties");
    }

    /**
     * This is the main method which is invoked to run the whole engine
     * from the command line.
     * It creates a new ExTeX object and invokes run() on it. The return value
     * is used as the exit status.
     *
     * @param args the list of command line arguments
     */
    public static void main(String[] args) {
        int status = EXIT_OK;

        try {
            ExTeX extex = new ExTeX(System.getProperties());
            status = extex.run(args);
        } catch (MainException e) {
            System.err.println(e.getMessage() + "\n");
            status = e.getCode();
        }

        System.exit(status);
    }

    /**
     * This class provides access to the whole functionality of ExTeX on the
     * command line.
     * The exception is that this method does not call
     * <code>{@link System#exit(int) System.exit()}</code> but returns the
     * exit status as result.
     *
     * @param args the list of command line arguments
     *
     * @return the exit status
     */
    public int run(String[] args) {
        boolean onceMore = true;
        int returnCode   = EXIT_OK;

        try {
            for (int i = 0; onceMore && i < args.length; i++) {
                String arg = args[i];

                if (arg.startsWith("-")) {
                    if (arg.equals("-")) {
                        runWithFile(args, i + 1);
                        onceMore = false;
                    } else if ("-configuration".startsWith(arg)) {
                        useArg("extex.config", args, ++i);
                    } else if ("-copyright".startsWith(arg)) {
                        int year = calendar.get(Calendar.YEAR);
                        System.err.print(Messages.format("ExTeX.Copyright",
                                                         (year <= 2003
                                                          ? "2003"
                                                          : "2003-" +
                                                          Integer.toString(year))));
                        onceMore = false;
                    } else if ("-help".startsWith(arg)) {
                        System.err.print(Messages.format("ExTeX.Usage",
                                                         "extex"));
                        onceMore = false;
                    } else if ("-fmt".startsWith(arg)) {
                        useArg("extex.fmt", args, ++i);
                    } else if (arg.startsWith("-fmt=")) {
                        properties.setProperty("extex.fmt",
                                               arg.substring("-fmt=".length()));
                    } else if ("-halt-on-error".startsWith(arg)) {
                        properties.setProperty("halt-on-error", "true");
                    } else if ("-interaction".startsWith(arg)) {
                        useArg("extex.interaction", args, ++i);
                    } else if ("-ini".startsWith(arg)) {
                        properties.setProperty("extex.ini", "true");
                    } else if (arg.startsWith("-interaction=")) {
                        properties.setProperty("extex.interaction",
                                               arg.substring("-interaction=".length()));
                    } else if ("-job-name".startsWith(arg)) {
                        useArg("extex.jobname2", args, ++i);
                    } else if (arg.startsWith("-job-name=")) {
                        properties.setProperty("extex.jobname2",
                                               arg.substring("-job-name=".length()));
                    } else if ("-version".startsWith(arg)) {
                        System.err.println(Messages.format("ExTeX.Version",
                                                           properties.getProperty("extex.progname"),
                                                           VERSION,
                                                           properties.getProperty("java.version")));
                        onceMore = false;
                    } else {
                        throw new MainUnknownOptionException(arg);
                    }
                } else if (arg.startsWith("&")) {
                    properties.setProperty("extex.fmt", arg.substring(1));
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
            returnCode = e.getCode();
            logger.severe(e.getMessage());
        } catch (Throwable e) {
            if (showBanner) {
                logger.info(Messages.format("ExTeX.Version",
                                            properties.getProperty("extex.progname"),
                                            VERSION,
                                            properties.getProperty("java.version")));
            }

            String msg = e.getMessage();
            logger.severe(Messages.format("ExTeX.InternalError",
                                          (msg != null &&
                                           !msg.equals("") ? msg
                                           : e.getCause() != null &&
                                             e.getCause()
                                              .getMessage() != null
                                             ? e.getCause()
                                                .getMessage() : "")));

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintWriter pw           = new PrintWriter(os);
            e.printStackTrace(pw);
            pw.flush();
            logger.config(os.toString());

            logger.info(Messages.format("ExTeX.Logfile",
                                        properties.getProperty("extex.jobname")));

            returnCode = EXIT_INTERNAL_ERROR;
        }

        return returnCode;
    }

    /**
     * Run the program with the parameters already stored in the properties.
     *
     * @throws MainException in case of an error
     */
    public void run() throws MainException {
        String jobname = properties.getProperty("extex.jobname2");

        if (jobname == null || jobname.equals("")) {
            jobname = properties.getProperty("extex.jobname");
        }

        useLogger("extex.log",
                  "config/logger-" +
                  properties.getProperty("extex.interaction") +
                  ".properties");
        logger.info(Messages.format("ExTeX.Version",
                                    properties.getProperty("extex.progname"),
                                    VERSION,
                                    properties.getProperty("java.version")));
        showBanner = false;

        try {
            Configuration config = new ConfigurationFactory().newInstance(properties.getProperty("extex.config"));

            Interpreter interpreter = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
            interpreter.setLogger(logger);
            interpreter.setErrorHandler((ErrorHandler) (new ErrorHandlerImpl(logger)));
            interpreter.registerObserver("close",
                                         new FileCloseObserver(logger));

            TokenStreamFactory factory = new TokenStreamFactory(config.getConfiguration("Reader"));
            factory.setFileFinder(new FileFinderImpl(factory, logger));
            factory.registerObserver("file",
                                     new FileOpenObserver(logger));
            interpreter.setTokenStreamFactory(factory);
            interpreter.setInteraction(Interaction.get(properties.getProperty("extex.interaction")));

            initializeStreams(interpreter);

            Typesetter typesetter = new TypesetterFactory(config.getConfiguration("Typesetter")).newInstance();

            DocumentWriter docWriter = new DocumentWriterFactory(config.getConfiguration("DocumentWriter")).newInstance();

            docWriter.setOutputStream(openOutput(docWriter.getExtension()));
            typesetter.setDocumentWriter(docWriter);

            interpreter.setTypesetter(typesetter);
            loadFormat(interpreter, properties.getProperty("extex.fmt"));
            interpreter.setJobname(jobname);

            interpreter.run();

            int pages = docWriter.getPages(); // todo: this might change
            logger.info(Messages.format((pages == 0 ? "ExTeX.NoPages"
                                         : pages == 1 ? "ExTeX.Page"
                                           : "ExTeX.Pages"),
                                        Integer.toString(pages)));
        } catch (ConfigurationException e) {
            throw new MainConfigurationException(e);
        } catch (CharacterCodingException e) {
            throw new MainCodingException(e);
        } catch (IOException e) {
            throw new MainIOException(e);
        } catch (GeneralException e) {
            throw new MainException(e);
        }

        // see "TeX -- The Program [1333]"
        logger.info(Messages.format("ExTeX.Logfile",
                                    properties.getProperty("extex.jobname")));
    }

    /**
     * Initialize the input streams.
     * If the property <i>extex.file</i> is set and not the empty string,
     * (e.g. from the command line) then this value is used as file name to
     * read from.
     * If the property <i>extex.trailingInput</i> is set and not the empty
     * string (e.g. from the command line) then this value is used as initial
     * input after the input file has been processed.
     * Finally, if everything before failed then read input from the stdin
     * stream.
     *
     * @param interpreter the interpreter context
     */
    private void initializeStreams(Interpreter interpreter)
                            throws CharacterCodingException, 
                                   ConfigurationException, 
                                   MainIOException {
        TokenStreamFactory factory = interpreter.getTokenStreamFactory();
        boolean notInitialized     = true;
        String filename            = properties.getProperty("extex.file");

        if (filename != null && !filename.equals("")) {
            File file = factory.getFileFinder()
                               .findFile(filename, "tex");

            try {
                TokenStream stream = factory.newInstance(file,
                                                         properties.getProperty("extex.encoding"));
                interpreter.addStream(stream);
                notInitialized = false;
            } catch (FileNotFoundException e) {
                logger.severe(Messages.format("CLI.FileNotFound", file));
            } catch (IOException e) {
                throw new MainIOException(e);
            }
        }

        String post = properties.getProperty("extex.trailingInput");

        if (post != null && !post.equals("")) {
            TokenStream stream = factory.newInstance(post,
                                                     properties.getProperty("extex.encoding"));
            interpreter.addStream(stream);
            notInitialized = false;
        }

        if (notInitialized) {
            try {
                TokenStream stream = factory.newInstance(new InputStreamReader(System.in),
                                                         properties.getProperty("extex.encoding"));
                interpreter.addStream(stream);
            } catch (IOException e) {
                throw new MainIOException(e);
            }
        }
    }

    /**
     * Load a format if a name of a format is given
     *
     * @param interpreter the interpreter to delegate the loading to
     * @param fmt the name of the format to use or <code>null</code>
     */
    private void loadFormat(Interpreter interpreter, String fmt)
                     throws IOException {
        String time = DateFormat.getDateTimeInstance()
                                .format(new Date());

        //TODO: use correct locale for date formatting
        if (fmt != null) {
            interpreter.loadFormat(fmt);
            logger.config(Messages.format("ExTeX.FormatDate", fmt, time));
        } else {
            logger.config(Messages.format("ExTeX.NoFormatDate", time));
        }
    }

    /**
     * Open the output stream for writing.
     *
     * @param ext the extension to the basename of the file
     *
     * @return the new output stream
     *
     * @throws MainOutputFileNotFoundException in case that the output file
     * could not be opened
     */
    private OutputStream openOutput(String ext) throws MainException {
        OutputStream os;
        String outname = properties.getProperty("extex.jobname") + ext;

        try {
            os = new BufferedOutputStream(new FileOutputStream(outname));
        } catch (FileNotFoundException e) {
            throw new MainOutputFileNotFoundException(outname);
        }

        return os;
    }

    /**
     * Set a property to a given value if not set yet.
     *
     * @param name the name of the property
     * @param value the default value
     */
    private void propertyDefault(String name, String value) {
        if (!properties.containsKey(name)) {
            properties.setProperty(name, value);
        }
    }

    /**
     * The command line is processed starting at an argument which starts with
     * a backslash. This argument and any following argument are taken as
     * input to the tokenizer.
     *
     * @param arg the list of arguments to process
     * @param i starting index
     */
    private void runWithArgs(String[] arg, int i)
                      throws MainException {
        if (i < arg.length) {
            StringBuffer in = new StringBuffer();

            do {
                in.append(" ");
                in.append(arg[i]);
            } while (++i < arg.length);

            properties.setProperty("extex.trailingInput", in.toString());
        }

        run();
    }

    /**
     * Process the command line arguments when the i<sup>th</sup> argument
     * is a file name. The file is prepared to be read from. The remaining
     * arguments are used as input to the processor.
     *
     * @param arg the list of arguments to process
     * @param i starting index
     *
     * @throws MainException in case of an error
     */
    private void runWithFile(String[] arg, int i)
                      throws MainException {
        if (i >= arg.length) {
            run();
            return;
        }

        String name = arg[i];
        properties.setProperty("extex.jobname",
                               (name.matches(".*\\.[a-zA-Z0-9]*")
                                ? name.substring(0,
                                                 name.lastIndexOf("."))
                                : name));
        properties.setProperty("extex.file", arg[i]);

        runWithArgs(arg, i + 1);
    }

    /**
     * Acquire the next argument from the command line and set a property
     * accordingly. If none is found then a exception is thrown.
     *
     * @param name the name of the argument
     * @param arg the list of arguments
     * @param i the starting index
     *
     * @throws MainMissingArgumentException in case of an error
     */
    private void useArg(String name, String[] arg, int i)
                 throws MainMissingArgumentException {
        if (i >= arg.length) {
            throw new MainMissingArgumentException(name);
        }

        properties.setProperty(name, arg[i]);
    }

    /**
     * Initialize or re-initialize the logger.
     *
     * @param loggerName the name of the logger to initialize
     * @param prop the name of the properties template to use
     */
    private void useLogger(String loggerName, String prop)
                    throws MainException {
        try {
            InputStream s = getClass()
                                .getClassLoader()
                                .getResourceAsStream(prop);
            byte[] bb  = new byte[8192];
            String cfg = "";
            int i;

            while ((i = s.read(bb)) >= 0) {
                cfg = cfg + new String(bb, 0, i);
            }

            cfg = cfg.replaceAll("\\$JOBNAME",
                                 properties.getProperty("extex.jobname"));

            LogManager logManager = LogManager.getLogManager();
            logManager.reset();
            logManager.readConfiguration(new ByteArrayInputStream(cfg.getBytes()));
        } catch (IOException e) {
            throw new MainIOException(e);
        }

        logger = Logger.getLogger(loggerName);
    }
}
