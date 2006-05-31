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

package de.dante.extex.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dante.extex.ExTeX;
import de.dante.extex.font.FontFactory;
import de.dante.extex.font.exception.FontException;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.observer.pop.PopObservable;
import de.dante.extex.interpreter.observer.pop.PopObserver;
import de.dante.extex.interpreter.observer.push.PushObservable;
import de.dante.extex.interpreter.observer.push.PushObserver;
import de.dante.extex.interpreter.observer.streamClose.StreamCloseObservable;
import de.dante.extex.interpreter.observer.streamClose.StreamCloseObserver;
import de.dante.extex.main.exception.MainCodingException;
import de.dante.extex.main.exception.MainConfigurationException;
import de.dante.extex.main.exception.MainException;
import de.dante.extex.main.exception.MainIOException;
import de.dante.extex.main.exception.MainMissingArgumentException;
import de.dante.extex.main.exception.MainUnknownOptionException;
import de.dante.extex.main.inputHandler.TeXInputReader;
import de.dante.extex.main.logging.LogFormatter;
import de.dante.extex.main.observer.FileCloseObserver;
import de.dante.extex.main.observer.FileOpenObserver;
import de.dante.extex.main.observer.TokenObserver;
import de.dante.extex.main.observer.TokenPushObserver;
import de.dante.extex.main.queryFile.QueryFileHandler;
import de.dante.extex.main.queryFile.QueryFileHandlerTeXImpl;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationUnsupportedEncodingException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;
import de.dante.util.observer.NotObservableException;
import de.dante.util.resource.ResourceFinder;

/**
 * This is the command line interface to <logo>ExTeX</logo>.
 * It does all the horrible things necessary to interact with the user of the
 * command line in nearly the same way as <logo>TeX</logo> does.
 * <p>
 * The command line interface provides the following features:
 * </p>
 * <ul>
 * <li>Specifying format, input file and <logo>TeX</logo> code on the command
 *  line.</li>
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
 *    This parameter contains <logo>ExTeX</logo> code to be executed directly.
 *    The execution is performed after any code specified in an input file.
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
 *   <dd>Property: <tt><a href="#extex.format">extex.format</a></tt></dd>
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
 *    If set to <code>true</code> then act as initex. This command line
 *    option is defined for compatibility to <logo>TeX</logo> only. In
 *   <logo>ExTeX</logo> it has no effect at all.
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
 *    This parameter contains the additional directories for searching
 *    <logo>ExTeX</logo> input files.
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
 *   <dt><a name="extex.format"/><tt>extex.format</tt></dt>
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
 *    option is defined for compatibility to <logo>TeX</logo> only. In
 *   <logo>ExTeX</logo> it has no effect at all.
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
 *   <dt><a name="extex.paper"/><tt>extex.paper</tt></dt>
 *   <dd>
 *    This parameter contains the default size of the paper. It can be one of
 *    the symbolic names defined in <tt>paper/paper.xml</tt>. Otherwise the
 *    value is interpreted as a pair of width and height separated by a space.
 *   </dd>
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
 *  compiled in defaults and the user's  Those are the system
 *  properties of the Java system. There system wide settings can be stored.
 *  Nevertheless, you should use this feature sparsely.
 * </p>
 *
 *
 *
 * <a name="invocation"/><h3>Direct Java Invocation</h3>
 *
 * <p>
 *  The direct invocation of the Java needs some settings to be preset.
 *  These settings are needed for <logo>ExTeX</logo> to run properly.
 *  The following premises are needed:
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
 *  Now <logo>ExTeX</logo> can be invoked with the same parameters described
 *  above:
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
 * @version $Revision: 1.18 $
 */
public class TeX extends ExTeX {

    /**
     * The constant <tt>COPYRIGHT_YEAR</tt> contains the starting year of
     * development for the copyright message. This number is fixed to be the
     * year 2003 and should no be modified.
     */
    private static final int COPYRIGHT_YEAR = 2003;

    /**
     * The field <tt>DOT_EXTEX</tt> contains the name of the user properties
     * file. This file contains property settings which are read when
     * <logo>ExTeX</logo> is started.
     */
    private static final String DOT_EXTEX = ".extex";

    /**
     * The constant <tt>EXIT_INTERNAL_ERROR</tt> contains the exit code for
     * internal errors.
     */
    protected static final int EXIT_INTERNAL_ERROR = -666;

    /**
     * The constant <tt>EXIT_OK</tt> contains the exit code of the program for
     * the success case.
     */
    protected static final int EXIT_OK = 0;

    /**
     * The constant <tt>TRACE_MAP</tt> contains the mapping from single
     * characters to tracing property names.
     */
    private static final Map TRACE_MAP = new HashMap();

    static {
        TRACE_MAP.put("+", PROP_TRACING_ONLINE);
        TRACE_MAP.put("F", PROP_TRACE_INPUT_FILES);
        TRACE_MAP.put("f", PROP_TRACE_FONT_FILES);
        TRACE_MAP.put("M", PROP_TRACE_MACROS);
        TRACE_MAP.put("T", PROP_TRACE_TOKENIZER);
    }

    /**
     * This is the main method which is invoked to run the whole engine from
     * the command line. It creates a new <logo>ExTeX</logo> object and invokes
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
     */
    public static void main(final String[] args) {

        int status;

        try {

            TeX tex = new TeX(System.getProperties(), DOT_EXTEX);
            status = tex.run(args);

        } catch (Throwable e) {
            Logger logger = Logger.getLogger(TeX.class.getName());
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);

            Handler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new LogFormatter());
            consoleHandler.setLevel(Level.WARNING);
            logger.addHandler(consoleHandler);

            Localizer localizer = LocalizerFactory.getLocalizer(TeX.class
                    .getName());
            logException(logger, //
                    localizer.format("ExTeX.SevereError", e.toString()), e);
            status = EXIT_INTERNAL_ERROR;
        }

        System.exit(status);
    }

    /**
     * The field <tt>localizer</tt> contains the localizer.
     */
    private Localizer localizer;

    /**
     * The field <tt>observers</tt> contains the observers.
     * <p>
     * NOTE: if weak references are used then the instances have to be kept
     *       in some variables to avoid that the garbage collector does its
     *       work.
     * </p>
     */
    private List observers = new ArrayList();

    /**
     * The field <tt>queryFileHandler</tt> contains the instance of the handler
     * to ask for a file name if none is given.
     */
    private QueryFileHandler queryFileHandler = null;

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
     * @see de.dante.extex.ExTeX#ExTeX(java.util.Properties, java.lang.String)
     */
    public TeX(final Properties theProperties, final String dotFile)
            throws InterpreterException,
                IOException {

        super(theProperties, dotFile);
        localizer = LocalizerFactory.getLocalizer(TeX.class.getName());
        setQueryFileHandler(new QueryFileHandlerTeXImpl());
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
     * @param properties the controlling properties
     *
     * @return <code>true</code> if the stream have not been initialized
     *
     * @throws ConfigurationException in case of a configuration error
     * @throws IOException in case of an IO error
     *
     * @see de.dante.extex.ExTeX#initializeStreams(
     *      de.dante.extex.interpreter.Interpreter,
     *      java.util.Properties)
     */
    protected boolean initializeStreams(final Interpreter interpreter,
            final Properties properties)
            throws ConfigurationException,
                IOException {

        TokenStreamFactory factory = interpreter.getTokenStreamFactory();
        boolean notInitialized = true;

        try {
            interpreter.addStream(factory.newInstance(new TeXInputReader(
                    getLogger(), properties.getProperty(PROP_ENCODING))));
            notInitialized = false;
        } catch (UnsupportedEncodingException e) {
            throw new ConfigurationUnsupportedEncodingException(properties
                    .getProperty(PROP_ENCODING), "???");
        }

        if (!super.initializeStreams(interpreter, properties)) {
            notInitialized = false;
        }

        String post = properties.getProperty(PROP_CODE);

        if (post != null && !post.equals("")) {
            interpreter.addStream(factory.newInstance(post));
            notInitialized = false;
        }

        if (notInitialized) {
            try {
                interpreter.addStream(factory.newInstance(new TeXInputReader(
                        getLogger(), properties.getProperty(PROP_ENCODING))));
            } catch (UnsupportedEncodingException e) {
                throw new ConfigurationUnsupportedEncodingException(properties
                        .getProperty(PROP_ENCODING), "???");
            }
        }
        return notInitialized;
    }

    /**
     * Loads a properties file into the already existing properties.
     * The values from the file overwrite existing values.
     *
     * @param arg the name of the resource to load
     *
     * @return <code>true</code> iff the resource has been loaded successfully
     *
     * @throws IOException just in case
     */
    protected boolean loadArgumentFile(final String arg) throws IOException {

        InputStream is = getClass().getResourceAsStream("config.extex." + arg);
        if (is == null) {
            try {
                is = new FileInputStream(new File(".extexcfg", arg));
            } catch (FileNotFoundException e) {
                return false;
            }
        }
        getProperties().load(is);

        return true;
    }

    /**
     * @see de.dante.extex.ExTeX#makeInterpreter(
     *      de.dante.util.framework.configuration.Configuration,
     *      de.dante.extex.scanner.stream.TokenStreamFactory,
     *      de.dante.extex.font.FontFactory)
     */
    protected Interpreter makeInterpreter(final Configuration config,
            final TokenStreamFactory factory, final FontFactory fontFactory)
            throws ConfigurationException,
                GeneralException,
                FontException,
                IOException {

        Interpreter interpreter = super.makeInterpreter(config, factory,
                fontFactory);
        Logger logger = getLogger();

        interpreter.getContext().setStandardTokenStream(
                factory.newInstance(new InputStreamReader(System.in)));

        if (interpreter instanceof StreamCloseObservable) {
            StreamCloseObserver observer = new FileCloseObserver(logger);
            ((StreamCloseObservable) interpreter).registerObserver(observer);
            observers.add(observer);
        }
        if (Boolean.valueOf(getProperties().getProperty(PROP_TRACE_TOKENIZER))
                .booleanValue()) {

            if (interpreter instanceof PopObservable) {
                PopObserver observer = new TokenObserver(logger);
                ((PopObservable) interpreter).registerObserver(observer);
                observers.add(observer);
            }
            if (interpreter instanceof PushObservable) {
                PushObserver observer = new TokenPushObserver(logger);
                ((PushObservable) interpreter).registerObserver(observer);
                observers.add(observer);
            }
        }
        if (Boolean.valueOf(getProperties().getProperty(PROP_TRACE_MACROS))
                .booleanValue()) {
            interpreter.getContext().setCount("tracingcommands", 1, true);
        }

        /*
         interpreter.getContext().registerCountObserver("escapechar",
         new CountObserver() {
         **
         * @see de.dante.extex.interpreter.context.observer.CountObserver#receiveCountChange(
         *      de.dante.extex.interpreter.context.ContextInternals,
         *      java.lang.String,
         *      de.dante.extex.interpreter.type.count.Count)
         *
         public void receiveCountChange(
         final ContextInternals context, final String name,
         final Count value) throws Exception {

         S ystem.err.println("change " + name + " to "
         + value.toString());
         }
         });
         */
        return interpreter;
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

        TokenStreamFactory factory = super.makeTokenStreamFactory(config,
                finder);
        factory.registerObserver(new FileOpenObserver(getLogger()));

        return factory;
    }

    /**
     * This class provides access to the whole functionality of
     * <logo>ExTeX</logo> on the command line. The exception is that this
     * method does not call <code>{@link System#exit(int) System.exit()}</code>
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
                    if (arg.startsWith("--")) {
                        arg = arg.substring(2);
                        if ("".equals(arg)) {
                            runWithFile(args, i + 1);
                            onceMore = false;
                        } else {
                            useArg(arg, args, ++i);
                        }
                    } else if ("-configuration".startsWith(arg)) {
                        useArg(PROP_CONFIG, args, ++i);
                    } else if ("-copyright".startsWith(arg)) {
                        int year = Calendar.getInstance().get(Calendar.YEAR);
                        String copyrightYear = (year <= COPYRIGHT_YEAR
                                ? Integer.toString(COPYRIGHT_YEAR)
                                : Integer.toString(COPYRIGHT_YEAR) + "-"
                                        + Integer.toString(year));
                        getLogger().info(
                                getLocalizer().format("ExTeX.Copyright",
                                        copyrightYear));
                        onceMore = false;
                    } else if ("-help".startsWith(arg)) {
                        getLogger().info(
                                getLocalizer().format("ExTeX.Usage", "extex"));
                        onceMore = false;
                    } else if ("-fmt".startsWith(arg)) {
                        useArg(PROP_FMT, args, ++i);
                        i++;
                    } else if (arg.startsWith("-fmt=")) {
                        setProperty(PROP_FMT, arg.substring("-fmt=".length()));
                    } else if ("-halt-on-error".startsWith(arg)) {
                        setProperty(PROP_HALT_ON_ERROR, "true");
                    } else if ("-interaction".startsWith(arg)) {
                        useArg(PROP_INTERACTION, args, ++i);
                        applyInteraction();
                    } else if ("-ini".startsWith(arg)) {
                        setProperty(PROP_INI, "true");
                    } else if (arg.startsWith("-interaction=")) {
                        setProperty(PROP_INTERACTION, arg
                                .substring("-interaction=".length()));
                        applyInteraction();
                    } else if ("-job-name".startsWith(arg)) {
                        useArg(PROP_JOBNAME_MASTER, args, ++i);
                    } else if (arg.startsWith("-job-name=")) {
                        setProperty(PROP_JOBNAME_MASTER, arg
                                .substring("-job-name=".length()));
                    } else if ("-language".startsWith(arg)) {
                        useArg(PROP_LANG, args, ++i);
                        applyLanguage();
                    } else if ("-progname".startsWith(arg)) {
                        useArg(PROP_PROGNAME, args, ++i);
                    } else if (arg.startsWith("-progname=")) {
                        setProperty(PROP_PROGNAME, arg.substring("-progname="
                                .length()));
                    } else if ("-version".startsWith(arg)) {
                        getLogger().info(
                                getLocalizer().format("ExTeX.Version",
                                        getProperty(PROP_PROGNAME),
                                        getVersion(),
                                        getProperty("java.version")));
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
                    setProperty(PROP_FMT, arg.substring(1));
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
            showBanner(null, Level.INFO);
            logException(getLogger(), e.getLocalizedMessage(), e);
            returnCode = e.getCode();
        } catch (Throwable e) {
            showBanner(null, Level.INFO);
            logInternalError(e);
            getLogger().info(
                    getLocalizer().format("ExTeX.Logfile",
                            getProperty(PROP_JOBNAME)));

            returnCode = EXIT_INTERNAL_ERROR;
        }

        return returnCode;
    }

    /**
     * Run super.run() and remap the Exceptions
     *
     * @return the interpreter instance used
     *
     * @throws MainException in case of an error
     */
    private Interpreter run2() throws MainException {

        try {
            return run();
        } catch (CharacterCodingException e) {
            throw new MainCodingException(e);
        } catch (ConfigurationException e) {
            throw new MainConfigurationException(e);
        } catch (IOException e) {
            throw new MainIOException(e);
        } catch (GeneralException e) {
            throw new MainException(e);
        }
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

            setProperty(PROP_CODE, in.toString());
        }

        run2();
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

        try {
            showBanner(new ConfigurationFactory()
                    .newInstance(getProperty(PROP_CONFIG)), Level.INFO);
        } catch (ConfigurationException e) {
            // ignored on purpose. It will be checked again later
        }

        QueryFileHandler queryHandler = getQueryFileHandler();
        setInputFileName((queryHandler != null //
                ? queryHandler.query(getLogger()) : null));
        run2();
    }

    /**
     * Setter for the input file name.
     *
     * @param name the name of the input file. If it is <code>null</code> then
     *  the values are reset to the initial state
     */
    private void setInputFileName(final String name) {

        if (name != null) {
            setProperty(PROP_JOBNAME, //
                    (name.matches(".*\\.[a-zA-Z0-9_]*") //
                            ? name.substring(0, name.lastIndexOf(".")) : name));
            setProperty(PROP_FILE, name);
        } else {
            setProperty(PROP_JOBNAME, "texput");
            setProperty(PROP_FILE, "");
        }
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

        setProperty(name, arguments[position]);
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

        getLogger().setLevel(Level.FINE);
        if (position >= arguments.length) {
            throw new MainMissingArgumentException("debug");
        }
        String s = arguments[position];
        for (int i = 0; i < s.length(); i++) {
            String prop = (String) TRACE_MAP.get(s.substring(i, i + 1));
            if (prop != null) {
                setProperty(prop, "true");
            } else {
                throw new MainUnknownOptionException(s.substring(i, i + 1));
            }
        }
    }

}
