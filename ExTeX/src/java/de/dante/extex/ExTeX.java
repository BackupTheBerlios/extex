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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.CharacterCodingException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterFactory;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.font.FontFactory;
import de.dante.extex.font.exception.FontException;
import de.dante.extex.interpreter.ErrorHandler;
import de.dante.extex.interpreter.ErrorHandlerFactory;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.InterpreterFactory;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.observer.InteractionObserver;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.loader.LoaderException;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.main.Version;
import de.dante.extex.main.InputHandler.TeXInputReader;
import de.dante.extex.main.exception.MainCodingException;
import de.dante.extex.main.exception.MainConfigurationException;
import de.dante.extex.main.exception.MainException;
import de.dante.extex.main.exception.MainIOException;
import de.dante.extex.main.exception.MainMissingArgumentException;
import de.dante.extex.main.exception.MainUnknownInteractionException;
import de.dante.extex.main.exception.MainUnknownOptionException;
import de.dante.extex.main.logging.LogFormatter;
import de.dante.extex.main.observer.FileCloseObserver;
import de.dante.extex.main.observer.FileOpenObserver;
import de.dante.extex.main.observer.InteractionModeObserver;
import de.dante.extex.main.observer.TokenObserver;
import de.dante.extex.main.observer.TokenPushObserver;
import de.dante.extex.main.observer.TraceObserver;
import de.dante.extex.main.queryFile.QueryFileHandler;
import de.dante.extex.main.queryFile.QueryFileHandlerTeXImpl;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.scanner.stream.TokenStreamOptions;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterFactory;
import de.dante.util.GeneralException;
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
import de.dante.util.file.OutputFactory;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;
import de.dante.util.observer.NotObservableException;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

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
 * <h3>ExTeX: Command Line Usage</h3>
 *
 * <p>
 *  This program is normally used through a wrapper which performs all
 *  necessary initializations and hides the implementation language from the
 *  casual user. Since this is the default case it is described here first.
 *  Details about the direct usage without the wrapper can be found in section
 *  <a href="#invocation">Direct Java Invocation</a>.
 * </p>
 * <p>
 *  This program &ndash; called <tt>extex</tt> here &ndash; has in its normal
 *  form of invocation one parameter. This parameter is the name of the file to
 *  process:
 * </p>
 * <pre class="CLISample">
 *   extex file.tex </pre>
 * <p>
 *  The input file is sought in the current directory and other locations.
 *  Details about searching can be found in <a href="#fileSearch">Searching TeX
 *  Files</a>.
 * </p>
 * <p>
 *  In general the syntax of invocation is as follows:
 * </p>
 * <pre class="CLIsyntax">
 *   extex &lang;options&rang; &lang;file&rang; &lang;code&rang; </pre>
 * <p>
 *  Command line parameters are the way of setting options with
 *  highest priority. The command line parameters overrule all
 *  settings in other parameter files. The command line options are
 *  contained in the table below.
 * </p>
 *
 * <dl>
 *   <dt><tt>&lang;code&rang;</tt></dt>
 *   <dd>
 *    This parameter contains ExTeX code to be executed directly. The
 *    execution is performed after any code specified in an input file.
 *    on the command line the code has to start with a backslash. This
 *    restriction does not hold for the property settings.
 *   </dd>
 *   <dd>Property: <tt><a href="#extex.code">extex.code</a></tt></dd>
 *
 *   <dt><tt>&lang;file&rang;</tt></dt>
 *   <dd>
 *    This parameter contains the file to read from. A file name may
 *    not start with a backslash or a ampercent. It has no default.
 *   </dd>
 *   <dd>Property:
 *    <a href="#extex.file"><tt>extex.file</tt></a></dd>
 *
 *   <dt><a name="-configuration"><tt>-configuration &lang;resource&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the name of the configuration resource to use.
 *    This configuration resource is sought on the class path.
 *   </dd>
 *   <dd>Property: <tt><a href="#extex.config">extex.config</a></tt></dd>
 *
 *   <dt><a name="-copyright"><tt>-copyright</tt></a></dt>
 *   <dd>
 *    This command line option produces a copyright notice on the
 *    standard output stream and terminates the program afterwards.
 *   </dd>
 *
 *   <dt><tt>&amp;&lang;format&rang;</tt></dt>
 *   <dt><a name="-fmt"><tt>-fmt &lang;format&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the name of the format to read. An empty
 *    string denotes that no format should be read. This is the default.
 *   </dd>
 *   <dd>Property: <tt><a href="#extex.fmt">extex.fmt</a></tt></dd>
 *
 *   <dt><a name="-debug"><tt>-debug &lang;spec&rang;</tt></a></dt>
 *   <dd>
 *    This command line parameter can be used to instruct the program to produce
 *    debugging output of several kinds. The specification &lang;spec&rang; is
 *    interpreted left to right. Each character is interpreted according to the
 *    following table:
 *    <table>
 *     <th>
 *      <td>Spec</td>Description<td></td><td>See</td>
 *     </th>
 *     <tr>
 *      <td><tt>F</tt></td>
 *      <td>
 *       This specifier contains the indicator whether or not to trace the
 *       searching for input files.
 *      </td>
 *      <td><tt><a href="#extex.trace.input.files">extex.trace.input.files</a></tt></td>
 *     </tr>
 *     <tr>
 *      <td><tt>f</tt></td>
 *      <td>
 *       This specifier contains the indicator whether or not to trace the
 *       searching for font files.
 *      </td>
 *      <td><tt><a href="#extex.trace.font.files">extex.trace.font.files</a></tt></td>
 *     </tr>
 *     <tr>
 *      <td><tt>M</tt></td>
 *      <td>
 *       This specifier contains the indicator whether or not to trace the
 *       execution of macros.
 *      </td>
 *      <td><tt><a href="#extex.trace.macros">extex.trace.macros</a></tt></td>
 *     </tr>
 *     <tr>
 *      <td><tt>T</tt></td>
 *      <td>
 *       This specifier contains the indicator whether or not to trace the
 *       work of the tokenizer.
 *      </td>
 *      <td><tt><a href="#extex.trace.tokenizer">extex.trace.tokenizer</a></tt></td>
 *     </tr>
 *    </table>
 *   </dd>
 *
 *   <dt><a name="-halt"><tt>-halt-on-error</tt></a></dt>
 *   <dd>
 *    This parameter contains the indicator whether the processing should
 *    halt after the first error has been encountered.
 *   </dd>
 *   <dd>Property:
 *     <tt><a href="#extex.halt.on.error">extex.halt.on.error</a></tt></dd>
 *
 *   <dt><a name="-help"><tt>-help</tt></a></dt>
 *   <dd>
 *    This command line option produces a short usage description on the
 *    standard output stream and terminates the program afterwards.
 *   </dd>
 *
 *   <dt><a name="-ini"><tt>-ini</tt></a></dt>
 *   <dd>
 *    If set to <code>true</code> then act as initex. This coµmand line
 *    option is defined for compatibility to TeX only. In ExTeX it has no
 *    effect at all.
 *   </dd>
 *   <dd>Property: <tt><a href="#extex.ini">extex.ini</a></tt> </dd>
 *
 *   <dt><a name="-interaction"><tt>-interaction &lang;mode&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the interaction mode. Possible values are
 *    the numbers 0..3 and the symbolic names batchmode (0), nonstopmode (1),
 *    scrollmode (2), and errorstopmode (3).
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.interaction">extex.interaction</a></tt></dd>
 *
 *   <dt><a name="-job"><tt>-job-name &lang;name&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the name of the job. It is overwritten
 *    if a file is given to read from. In this case the base name of
 *    the input file is used instead.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.jobname">extex.jobname</a></tt></dd>
 *
 *   <dt><a name="-language"><tt>-language &lang;language&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the name of the locale to be used for the
 *    messages.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.lang">extex.lang</a></tt> </dd>
 *
 *   <dt><a name="-output"><tt>-output &lang;format&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the output format. This logical name is resolved
 *    via the configuration.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.output">extex.output</a></tt></dd>
 *
 *   <dt><a name="-progname"/><tt>-progname &lang;name&rang;</tt></dt>
 *   <dd>
 *    This parameter can be used to overrule the name of the program shown in
 *    the banner and the version information.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.progname">extex.progname</a></tt></dd>
 *
 *   <dt><a name="-texinputs"/><tt>-texinputs &lang;path&rang;</tt></dt>
 *   <dd>
 *    This parameter contains the additional directories for searching TeX
 *    input files.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.texinputs">extex.texinputs</a></tt> </dd>
 *
 *   <dt><a name="-texmfoutputs"><tt>-texmfoutputs &lang;dir&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contains the name of the
 *    property for the fallback if the output directory fails to be writable.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.outputdir.fallback">extex.outputdir.fallback</a></tt>
 *   </dd>
 *
 *   <dt><a name="-texoutputs"><tt>-texoutputs &lang;dir&rang;</tt></a></dt>
 *   <dd>
 *    This parameter contain the directory where output files should be
 *    created.
 *   </dd>
 *   <dd>Property:
 *    <tt><a href="#extex.outputdir">extex.outputdir</a></tt></dd>
 *
 *   <dt><a name="-version"/><tt>-version</tt></dt>
 *   <dd>
 *    This command line parameter forces that the version information
 *    is written to standard output and the program is terminated.
 *   </dd>
 * </dl>
 *
 * <p>
 *  Command line parameters can be abbreviated up to a unique prefix
 *  &ndash; and sometimes even more. Thus the following invocations
 *  are equivalent:
 * <pre class="CLIsyntax">
 *   extex -v
 *   extex -ve
 *   extex -ver
 *   extex -vers
 *   extex -versi
 *   extex -versio
 *   extex -version  </pre>
 * </p>
 *
 *
 * <a name="fileSearch"/><h3>Searching Files</h3>
 *
 * TODO gene: doc incomplete
 *
 *
 *
 * <a name="settings"/><h3>Settings and Command Line Parameters</h3>
 *
 * <p>
 *  Settings can be stored in properties files. Those settings are the
 *  fallbacks if no corresponding command line arguments are found.
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
 *    This parameter contains ExTeX code to be executed directly. The
 *    execution is performed after any code specified in an input file.
 *    on the command line the code has to start with a backslash. This
 *    restriction does not hold for the property settings.
 *   </dd>
 *   <dd>Command line: <tt>&lang;code&rang;</tt></dd>
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
 *   <dd>Command line:
 *    <a href="#-halt"><tt>-halt-on-error</tt></a> </dd>
 *
 *   <dt><a name="extex.file"><tt>extex.file</tt></a></dt>
 *   <dd>
 *    This parameter contains the file to read from. It has no default
 *   </dd>
 *   <dd>Command line:
 *    <a href="&lang"><tt>&lang;file&rang;</tt></a> </dd>
 *
 *   <dt><a name="extex.fmt"/><tt>extex.fmt</tt></dt>
 *   <dd>
 *    This parameter contains the name of the format to read. An empty
 *    string denotes that no format should be read. This is the default.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-fmt"><tt>-fmt &lang;format&rang;</tt></a></dd>
 *
 *   <dt><a name="extex.ini"/><tt>extex.ini</tt></dt>
 *   <dd>
 *    If set to <code>true</code> then act as initex. This command line
 *    option is defined for compatibility to TeX only. In ExTeX it has no
 *    effect at all.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-ini"><tt>-ini</tt></a> </dd>
 *
 *   <dt><a name="extex.interaction"/><tt>extex.interaction</tt></dt>
 *   <dd>
 *    This parameter contains the interaction mode. possible values are
 *    the numbers 0..3 and the symbolic names batchmode (0), nonstopmode (1),
 *    scrollmode (2), and errorstopmode (3).
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-interaction"><tt>-interaction &lang;mode&rang;</tt></a></dd>
 *   <dd>Default: <tt>3</tt></dd>
 *
 *   <dt><a name="extex.jobname"/><tt>extex.jobname</tt></dt>
 *   <dd>
 *    This parameter contains the name of the job. It is overwritten
 *    if a file is given to read from. In this case the basename of
 *    the input file is used instead.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-job"><tt>-job-name &lang;name&rang;</tt></a></dd>
 *   <dd>Default: <tt>texput</tt></dd>
 *
 *   <dt><a name="extex.jobnameMaster"/><tt>extex.jobnameMaster</tt></dt>
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
 *   <dd>Command line:
 *    <a href="#-language"><tt>-language &lang;language&rang;</tt></a> </dd>
 *
 *   <dt><a name="extex.nobanner"/><tt>extex.nobanner</tt></dt>
 *   <dd>
 *    This parameter contains a boolean indicating that the banner should be
 *    suppressed.
 *   </dd>
 *
 *   <dt><a name="extex.output"/><tt>extex.output</tt></dt>
 *   <dd>
 *    This parameter contains the output format. This logical name is resolved
 *    via the configuration.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-output"><tt>-output &lang;format&rang;</tt></a></dd>
 *   <dd>Default: <tt>pdf</tt></dd>
 *
 *   <dt><a name="extex.outputdir"/><tt>extex.outputdir</tt></dt>
 *   <dd>
 *    This parameter contain the directory where output files should be
 *    created.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-texoutputs"><tt>-texoutputs &lang;dir&rang;</tt></a></dd>
 *   <dd>Default: <tt>.</tt></dd>
 *
 *   <dt><a name="extex.outputdir.fallback"/><tt>extex.outputdir.fallback</tt></dt>
 *   <dd>
 *    This parameter contains the name of the
 *    property for the fallback if the output directory fails to be writable.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-texmfoutputs"><tt>-texmfoutputs &lang;dir&rang;</tt></a> </dd>
 *
 *   <dt><a name="extex.progname"/><tt>extex.progname</tt></dt>
 *   <dd>
 *    This parameter can be used to overrule the name of the program shown in
 *    the banner and the version information.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-progname"><tt>-progname</tt></a></dd>
 *   <dd>Default: <tt>ExTeX</tt></dd>
 *
 *   <dt><a name="extex.texinputs"/><tt>extex.texinputs</tt></dt>
 *   <dd>
 *    This parameter contains the additional directories for searching TeX
 *    input files.
 *   </dd>
 *   <dd>Command line:
 *    <a href="#-texinputs"><tt>-texinputs &lang;path&rang;</tt></a> </dd>
 *
 *   <dt><a name="extex.token.stream"/><tt>extex.token.stream</tt></dt>
 *   <dd>
 *    This string parameter contains the logical name of the configuration to
 *    use for the token stream.
 *   </dd>
 *
 *   <dt><a name="extex.trace.input.files"/><tt>extex.trace.input.files</tt></dt>
 *   <dd>
 *    This boolean parameter contains the indicator whether or not to trace the
 *    search for input files.
 *   </dd>
 *
 *   <dt><a name="extex.trace.font.files"/><tt>extex.trace.font.filess</tt></dt>
 *   <dd>
 *    This boolean parameter contains the indicator whether or not to trace the
 *    search for font files.
 *   </dd>
 *
 *   <dt><a name="extex.trace.macros"/><tt>extex.trace.macros</tt></dt>
 *   <dd>
 *    This boolean parameter contains the indicator whether or not to trace the
 *    execution of macros.
 *   </dd>
 *
 *   <dt><a name="extex.trace.tokenizer"/><tt>extex.trace.tokenizer</tt></dt>
 *   <dd>
 *    This boolean parameter contains the indicator whether or not to trace the
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
 *  The configuration of ExTeX is controlled by several configuration resources.
 *  The fallback for those configuration resources are contained in the ExTeX
 *  jar file. In this section we will describe how to overwrite the settings in
 *  the default configuration resource.
 * </p>
 *
 * TODO gene: doc incomplete
 *
 *
 *
 * <a name="invocation"/><h3>Direct Java Invocation</h3>
 *
 * <p>
 *  The direct invocation of the Java needs some settings to be preset.
 *  These settings are needed for ExTeX to run properly. The following
 *  premises are needed:
 * </p>
 * <ul>
 *  <li>Java needs to be installed (see section
 *   <a href="#installation">Installation</a>. The program <tt>java</tt> is
 *   assumed to be on the path of executables.
 *  </li>
 *  <li>Java must be configured to find the jar files from the ExTeX
 *   distribution. This can be accomplished by setting the environment variable
 *   <tt>CLASSPATH</tt> or <tt>JAVA_HOME</tt>. See the documentation of your
 *   Java system for details.
 *  </li>
 * </ul>
 * <p>
 *  Now ExTeX can be invoked with the same parameters described above:
 * </p>
 * <pre class="CLIsyntax">
 *   java de.dante.extex.ExTeX &lang;options&rang; &lang;file&rang; </pre>
 * <p>
 *  The result should be the same as the invocation of the wrapper.
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 *
 * @version $Revision: 1.93 $
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
     * The constant <tt>EXIT_INTERNAL_ERROR</tt> contains the exit code for
     * internal errors.
     */
    private static final int EXIT_INTERNAL_ERROR = -666;

    /**
     * The constant <tt>EXIT_OK</tt> contains the exit code of the program for
     * the success case.
     */
    private static final int EXIT_OK = 0;

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
     * The field <tt>PROP_ERROR_HANDLER</tt> contains the name of the property
     * for the error handler type to use. Possible values are resolved via the
     * configuration.
     */
    private static final String PROP_ERROR_HANDLER = "extex.error.handler";

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
     * The field <tt>PROP_HALT_ON_ERROR</tt> contains the name of the property
     * indicating whether the processing should stop at the first error.
     */
    private static final String PROP_HALT_ON_ERROR = "extex.halt.on.error";

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
     * The field <tt>PROP_OUTPUT_TYPE</tt> contains the name of the property for
     * the output driver. This value is resolved by the
     * {@link de.dante.extex.documentWriter.DocumentWriterFactory DocumentWriterFactory}
     * to find the appropriate class.
     */
    private static final String PROP_OUTPUT_TYPE = "extex.output";

    /**
     * The field <tt>PROP_OUTPUTDIR</tt> contains the name of the
     * property for the output directory.
     */
    private static final String PROP_OUTPUTDIR = "extex.outputdir";

    /**
     * The field <tt>PROP_OUTPUTDIR_FALLBACK</tt> contains the name of the
     * property for the fallback if the output directory fails to be writable.
     */
    private static final String PROP_OUTPUTDIR_FALLBACK = "extex.outputdir.fallback";

    /**
     * The field <tt>PROP_PROGNAME</tt> contains the name of the
     * property for the program name used in usage messages.
     */
    private static final String PROP_PROGNAME = "extex.progname";

    /**
     * The field <tt>PROP_TEXINPUTS</tt> contains the name of the
     * property for the additional texinputs specification of directories.
     */
    private static final String PROP_TEXINPUTS = "extex.texinputs";

    /**
     * The field <tt>PROP_TOKEN_STREAM</tt> contains the name of the property
     * for the token stream class to use.
     */
    private static final String PROP_TOKEN_STREAM = "extex.token.stream";

    /**
     * The field <tt>PROP_TRACE_FONT_FILES</tt> contains the name of the
     * property for the boolean determining whether or not the searching for
     * font files should produce tracing output.
     */
    private static final String PROP_TRACE_FONT_FILES = "extex.trace.font.files";

    /**
     * The field <tt>PROP_TRACE_INPUT_FILES</tt> contains the name of the
     * property for the boolean determining whether or not the searching for
     * input files should produce tracing output.
     */
    private static final String PROP_TRACE_INPUT_FILES = "extex.trace.input.files";

    /**
     * The field <tt>PROP_TRACE_MACROS</tt> contains the name of the
     * property for the boolean determining whether or not the execution of
     * macros should produce tracing output.
     */
    private static final String PROP_TRACE_MACROS = "extex.trace.macros";

    /**
     * The field <tt>PROP_TRACE_TOKENIZER</tt> contains the name of the
     * property for the boolean determining whether or not the tokenizer
     * should produce tracing output.
     */
    private static final String PROP_TRACE_TOKENIZER = "extex.trace.tokenizer";

    /**
     * The field <tt>PROP_TYPESETTER_TYPE</tt> contains the name of the property
     * for the typesetter to use. This value is resolved by the
     * {@link de.dante.extex.typesetter.TypesetterFactory TypesetterFactory}
     * to find the appropriate class.
     */
    private static final String PROP_TYPESETTER_TYPE = "extex.typesetter";

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
     * The constant <tt>TRACE_MAP</tt> contains the mapping from single
     * characters to tracing property names.
     */
    private static final Map TRACE_MAP = new HashMap();

    static {
        TRACE_MAP.put("F", PROP_TRACE_INPUT_FILES);
        TRACE_MAP.put("f", PROP_TRACE_FONT_FILES);
        TRACE_MAP.put("M", PROP_TRACE_MACROS);
        TRACE_MAP.put("T", PROP_TRACE_TOKENIZER);
    }

    /**
     * Log a Throwable including its stack trace to the logger.
     *
     * @param logger the target logger
     * @param text the prefix text to log
     * @param e the Throwable to log
     */
    private static void logException(final Logger logger, final String text,
            final Throwable e) {

        logger.severe(text == null ? "" : text);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(os);
        e.printStackTrace(writer);
        writer.flush();
        logger.fine(os.toString());
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
            Logger logger = Logger.getLogger(ExTeX.class.getName());
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);

            Handler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new LogFormatter());
            consoleHandler.setLevel(Level.WARNING);
            logger.addHandler(consoleHandler);

            Localizer localizer = LocalizerFactory.getLocalizer(ExTeX.class
                    .getName());
            logException(logger, //
                    localizer.format("ExTeX.SevereError", e.toString()), e);
            status = EXIT_INTERNAL_ERROR;
        }

        System.exit(status);
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
     * The field <tt>queryFileHandler</tt> contains the instance of the handler
     * to ask for a file name if none is given.
     */
    private QueryFileHandler queryFileHandler = new QueryFileHandlerTeXImpl();

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
     * @param theProperties the properties to start with
     *
     * @throws MainException in case of an error
     */
    public ExTeX(final Properties theProperties) throws MainException {

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
        propertyDefault(PROP_PROGNAME, "ExTeX");
        propertyDefault(PROP_TEXINPUTS, "");
        propertyDefault(PROP_TOKEN_STREAM, "base");
        propertyDefault(PROP_TRACE_INPUT_FILES, "");
        propertyDefault(PROP_TRACE_FONT_FILES, "");
        propertyDefault(PROP_TRACE_MACROS, "");
        propertyDefault(PROP_TRACE_TOKENIZER, "");
        propertyDefault(PROP_TYPESETTER_TYPE, "");

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
     * @throws MainException in case of an IO Error during the reading of the
     *             properties file
     *
     * @see #ExTeX(java.util.Properties)
     */
    public ExTeX(final Properties theProperties, final String dotFile)
            throws MainException {

        this(theProperties);

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
     * Propagate the settings for the interaction mode to the
     * <code>interactionObserver</code>.
     *
     * @throws MainUnknownInteractionException in case that the interaction is
     *             not set properly
     */
    protected void applyInteraction() throws MainUnknownInteractionException {

        try {
            interactionObserver.receiveInteractionChange(null, Interaction
                    .get(properties.getProperty(PROP_INTERACTION)));
        } catch (Exception e) {
            throw new MainUnknownInteractionException("");
        }
    }

    /**
     * Try to determine which language to use and configure the localizer
     * accordingly.
     */
    protected void applyLanguage() {

        String lang = (String) properties.get(PROP_LANG);
        Matcher m;

        if (lang != null) {
            if (lang.length() == 2) {
                Locale.setDefault(new Locale(lang));
            } else if ((m = Pattern.compile("^(..)[_-](..)$").matcher(lang))
                    .matches()) {
                Locale.setDefault(new Locale(m.group(1), m.group(2)));
            } else if ((m = Pattern.compile("^(..)[_-](..)[_-](..)$").matcher(
                    lang)).matches()) {
                Locale
                        .setDefault(new Locale(m.group(1), m.group(2), m
                                .group(3)));
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
        }
        jobname = new File(jobname).getName();
        return jobname;
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
     * Getter for queryFileHandler.
     *
     * @return the queryFileHandler
     */
    public QueryFileHandler getQueryFileHandler() {

        return this.queryFileHandler;
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

    protected void initializeStreams(final Interpreter interpreter)
            throws ConfigurationException,
                MainIOException {

        TokenStreamFactory factory = interpreter.getTokenStreamFactory();
        boolean notInitialized = true;
        String filename = properties.getProperty(PROP_FILE);

        if (filename != null && !filename.equals("")) {

            try {
                TokenStream stream = factory.newInstance(filename, "tex",
                        properties.getProperty(PROP_ENCODING));
                interpreter.addStream(stream);
                notInitialized = false;
            } catch (FileNotFoundException e) {
                logger.severe(localizer.format("TTP.FileNotFound", filename));
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
                TokenStream stream = factory.newInstance(new TeXInputReader(
                        logger, properties.getProperty(PROP_ENCODING)));
                interpreter.addStream(stream);
            } catch (UnsupportedEncodingException e) {
                throw new ConfigurationUnsupportedEncodingException(properties
                        .getProperty(PROP_ENCODING), "???");
            }

        }
    }

    /**
     * Loads a properties file into the already existing properties.
     * The values from the file overwrite existing values.
     *
     * @param arg the name of the resource to load
     *
     * @return <code>true</code> iff the resource has been loaded sucessfully
     *
     * @throws IOException just in case
     */
    protected boolean loadArgumentFile(final String arg) throws IOException {

        InputStream is = getClass().getClassLoader().getResourceAsStream(
                "config.extex." + arg);
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
                throw new MainException(42, localizer.format("FormatNotFound",
                        format));
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
     * Log a throwable including its stack trace to the logger.
     * @param e the throwable to log
     */
    protected void logInternalError(final Throwable e) {

        e.printStackTrace();

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
     * Creat a default font for the interpreter context.
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
     * @param jobname the jobname to use
     * @param outFactory the output factory
     * @param options the options tobe passed to the document writer
     *
     * @return the new document writer
     *
     * @throws ConfigurationException in case that some kind of problems have
     * been detected in the configuration
     * @throws FileNotFoundException in case that the output file could not
     * be opened
     */
    protected DocumentWriter makeDocumentWriter(final Configuration config,
            final String jobname, final OutputFactory outFactory,
            final DocumentWriterOptions options)
            throws ConfigurationException,
                FileNotFoundException {

        DocumentWriterFactory factory = new DocumentWriterFactory(config);
        factory.enableLogging(logger);
        DocumentWriter docWriter = factory.newInstance(properties
                .getProperty(PROP_OUTPUT_TYPE), options, outStream, outFactory,
                jobname);

        docWriter.setParameter("Creator", "ExTeX " + new Version().toString());

        return docWriter;
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
    protected FontFactory makeFontFactory(final Configuration config)
            throws ConfigurationException {

        FontFactory fontFactory;
        String fontClass = config.getAttribute("class");

        if (fontClass == null || fontClass.equals("")) {
            throw new ConfigurationMissingAttributeException("class", config);
        }

        ResourceFinder fontFinder = (new ResourceFinderFactory())
                .createResourceFinder(config.getConfiguration("Resource"),
                        logger, properties);
        if (Boolean.valueOf(properties.getProperty(PROP_TRACE_FONT_FILES))
                .booleanValue()) {
            fontFinder.enableTracing(true);
        }

        try {
            fontFactory = (FontFactory) (Class.forName(fontClass)
                    .getConstructor(
                            new Class[]{Configuration.class,
                                    ResourceFinder.class})
                    .newInstance(new Object[]{config, fontFinder}));
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
     * @param finder the file finder for files opened by the interpreter
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
            final ResourceFinder finder, final TokenStreamFactory factory,
            final FontFactory fontFactory)
            throws ConfigurationException,
                GeneralException,
                FontException {

        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config);
        interpreterFactory.enableLogging(logger);

        Interpreter interpreter = interpreterFactory.newInstance();
        ErrorHandler errHandler = errorHandler;
        if (errHandler == null) {
            ErrorHandlerFactory errorHandlerFactory = new ErrorHandlerFactory(
                    config.getConfiguration(TAG_ERRORHANDLER));
            errorHandlerFactory.enableLogging(logger);
            errHandler = errorHandlerFactory.newInstance(properties
                    .getProperty(PROP_ERROR_HANDLER));
        }
        interpreter.setErrorHandler(errHandler);
        interpreter.setResourceFinder(finder);
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
        context.setTypesettingContext(makeDefaultFont(fontConfiguration,
                fontFactory));
        context.getTypesettingContext().setLanguage(context.getLanguage("0"));

        initializeStreams(interpreter);

        //TODO gene: if weak references are used then the instances have to be kept in some variables:-(

        interpreter.registerObserver("close", new FileCloseObserver(logger));
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
    protected TokenStreamFactory makeTokenStreamFactory(
            final Configuration config, final ResourceFinder finder)
            throws ConfigurationException,
                NotObservableException {

        TokenStreamFactory factory = new TokenStreamFactory(config, properties
                .getProperty(PROP_TOKEN_STREAM));

        factory.enableLogging(logger);
        factory.setResourceFinder(finder);
        factory.registerObserver("file", new FileOpenObserver(logger));

        return factory;
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
    protected Typesetter makeTypesetter(final Configuration config,
            final DocumentWriter docWriter, final Context context)
            throws ConfigurationException {

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

        if (!properties.containsKey(name)) {
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

        //if (showBanner) {
        //    showBanner = !Boolean.valueOf(
        //            properties.getProperty(PROP_NO_BANNER)).booleanValue();
        //}

        Handler fileHandler = makeLogFileHandler(logFile);

        try {
            Configuration config = new ConfigurationFactory()
                    .newInstance(properties.getProperty(PROP_CONFIG));
            showBanner(config, (showBanner ? Level.INFO : Level.FINE));

            OutputFactory outFactory = new OutputFactory(//
                    config.getConfiguration("Output"), //
                    new String[]{properties.getProperty(PROP_OUTPUTDIR),
                            properties.getProperty(PROP_OUTPUTDIR_FALLBACK)});

            ResourceFinder finder = (new ResourceFinderFactory())
                    .createResourceFinder(config.getConfiguration("Resource"),
                            logger, properties);
            if (Boolean.valueOf(properties.getProperty(PROP_TRACE_INPUT_FILES))
                    .booleanValue()) {
                finder.enableTracing(true);
            }
            TokenStreamFactory tokenStreamFactory //
            = makeTokenStreamFactory(config.getConfiguration("Scanner"), finder);

            FontFactory fontFactory = makeFontFactory(config
                    .getConfiguration("Fonts"));

            Interpreter interpreter = makeInterpreter(config
                    .getConfiguration("Interpreter"), finder,
                    tokenStreamFactory, fontFactory);

            DocumentWriter docWriter = makeDocumentWriter(config
                    .getConfiguration("DocumentWriter"), jobname, outFactory,
                    (DocumentWriterOptions) interpreter.getContext());

            Typesetter typesetter = makeTypesetter(config
                    .getConfiguration("Typesetter"), docWriter, interpreter
                    .getContext());
            interpreter.setTypesetter(typesetter);

            loadFormat(interpreter, finder, properties.getProperty(PROP_FMT),
                    jobname);

            interpreter.run();

            int pages = docWriter.getPages();
            String outname = jobname + "." + docWriter.getExtension();
            logger.info(localizer.format((pages == 0
                    ? "ExTeX.NoPages"
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
                        int year = Calendar.getInstance().get(Calendar.YEAR);
                        logger.info(localizer.format("ExTeX.Copyright",
                                (year <= COPYRIGHT_YEAR ? Integer
                                        .toString(COPYRIGHT_YEAR) : Integer
                                        .toString(COPYRIGHT_YEAR)
                                        + "-" + Integer.toString(year))));
                        onceMore = false;
                    } else if ("-help".startsWith(arg)) {
                        logger.info(localizer.format("ExTeX.Usage", "extex"));
                        onceMore = false;
                    } else if ("-fmt".startsWith(arg)) {
                        useArg(PROP_FMT, args, ++i);
                        i++;
                    } else if (arg.startsWith("-fmt=")) {
                        properties.setProperty(PROP_FMT, arg.substring("-fmt="
                                .length()));
                    } else if ("-halt-on-error".startsWith(arg)) {
                        properties.setProperty(PROP_HALT_ON_ERROR, "true");
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
                        logger.info(localizer.format("ExTeX.Version",
                                properties.getProperty(PROP_PROGNAME),
                                EXTEX_VERSION, properties
                                        .getProperty("java.version")));
                        onceMore = false;
                    } else if ("-output".startsWith(arg)) {
                        useArg(PROP_OUTPUT_TYPE, args, ++i);
                    } else if ("-texinputs".startsWith(arg)) {
                        useArg(PROP_TEXINPUTS, args, ++i);
                    } else if ("-texoutputs".startsWith(arg)) {
                        useArg(PROP_OUTPUTDIR, args, ++i);
                    } else if ("-texmfoutputs".startsWith(arg)) {
                        useArg("extex.fallbackOutputdir", args, ++i);
                    } else if ("-debug".startsWith(arg)) {
                        useTrace(args, ++i);
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
                runWithoutFile();
            }
        } catch (MainException e) {
            try {
                showBanner(null, Level.INFO);
            } catch (MainException e1) {
                logException(logger, e1.getLocalizedMessage(), e1);
            }
            logException(logger, e.getLocalizedMessage(), e);
            returnCode = e.getCode();
        } catch (Throwable e) {
            try {
                showBanner(null, Level.INFO);
            } catch (MainException e1) {
                logException(logger, e1.getLocalizedMessage(), e1);
            }
            logInternalError(e);
            logger.info(localizer.format("ExTeX.Logfile", properties
                    .getProperty(PROP_JOBNAME)));

            returnCode = EXIT_INTERNAL_ERROR;
        }

        return returnCode;
    }

    /**
     * The command line is processed starting at an argument which starts with
     * a backslash. This argument and any following argument are taken as input
     * to the tokenizer.
     *
     * @param arguments the list of arguments to process
     * @param position starting index
     *
     * @throws MainException in case of an error in {@link #run() run()}
     */
    private void runWithArgs(final String[] arguments, final int position)
            throws MainException {

        if (position < arguments.length) {
            StringBuffer in = new StringBuffer();

            for (int i = position; i < arguments.length; i++) {
                in.append(" ");
                in.append(arguments[i]);
            }

            properties.setProperty(PROP_CODE, in.toString());
        }

        run();
    }

    /**
     * Process the command line arguments when the i<sup>th</sup> argument
     * is a file name. The file is prepared to be read from. The remaining
     * arguments are used as input to the processor.
     *
     * @param arguments the list of arguments to process
     * @param position starting index
     *
     * @throws MainException in case of an error
     */
    private void runWithFile(final String[] arguments, final int position)
            throws MainException {

        if (position >= arguments.length) {
            runWithoutFile();
        } else {
            setInputFileName(arguments[position]);
            runWithArgs(arguments, position + 1);
        }
    }

    /**
     * Ask the query file handler to provide a file name and use it.
     *
     * @throws MainException in case of an error
     */
    private void runWithoutFile() throws MainException {

        //        if (showBanner) {
        //            showBanner = !Boolean.valueOf(
        //                    properties.getProperty(PROP_NO_BANNER)).booleanValue();
        try {
            showBanner(new ConfigurationFactory().newInstance(properties
                    .getProperty(PROP_CONFIG)), Level.INFO);
        } catch (ConfigurationException e) {
            // ignored on purpose. It will be checked again later
        }
        //        }

        setInputFileName((queryFileHandler != null ? queryFileHandler
                .query(getLogger()) : null));
        run();
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
     * Setter for the input file name.
     *
     * @param name the name of the input file. If it is <code>null</code> then
     *  the values are reset to the initial state
     */
    private void setInputFileName(final String name) {

        if (name != null) {
            properties.setProperty(PROP_JOBNAME, //
                    (name.matches(".*\\.[a-zA-Z0-9_]*") //
                            ? name.substring(0, name.lastIndexOf(".")) : name));
            properties.setProperty(PROP_FILE, name);
        } else {
            properties.setProperty(PROP_JOBNAME, "texput");
            properties.setProperty(PROP_FILE, "");
        }
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
     * Setter for queryFileHandler.
     *
     * @param queryFileHandler the queryFileHandler to set
     */
    public void setQueryFileHandler(final QueryFileHandler queryFileHandler) {

        this.queryFileHandler = queryFileHandler;
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

    /**
     * Acquire the next argument from the command line and set a property
     * accordingly. If none is found then an exception is thrown.
     *
     * @param name the name of the argument
     * @param arguments the list of arguments
     * @param position the starting index
     *
     * @throws MainMissingArgumentException in case of an error
     */
    protected void useArg(final String name, final String[] arguments,
            final int position) throws MainMissingArgumentException {

        if (position >= arguments.length) {
            throw new MainMissingArgumentException(name);
        }

        properties.setProperty(name, arguments[position]);
    }

    /**
     * Acquire the next argument from the command line and use it as a
     * specification to control the tracing features. The appropriate properties
     * are set accordingly.
     *
     * @param arguments the list of arguments
     * @param position the starting index
     *
     * @throws MainMissingArgumentException in case that no key letters follow
     * @throws MainUnknownOptionException in case that the specified option
     *  letter has no assigned property to set
     */
    protected void useTrace(final String[] arguments, final int position)
            throws MainUnknownOptionException,
                MainMissingArgumentException {

        logger.setLevel(Level.FINE);
        if (position >= arguments.length) {
            throw new MainMissingArgumentException("debug");
        }
        String s = arguments[position];
        for (int i = 0; i < s.length(); i++) {
            String prop = (String) TRACE_MAP.get(s.substring(i, i + 1));
            if (prop != null) {
                properties.setProperty(prop, "true");
            } else {
                throw new MainUnknownOptionException(s.substring(i, i + 1));
            }
        }
    }

}