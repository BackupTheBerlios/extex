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
package de.dante.util.configuration;

import de.dante.extex.i18n.Messages;
import de.dante.util.StringList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;

/**
 * This class provides means to deal with configurations stored as XML files.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ConfigurationXMLImpl implements Configuration {
    /** The root element for this configuration */
    private Element root = null;

    /** The base of the resource name; i.e. the resource p up to the last /
     *  or the empty string if no / was contained.
     */
    private String base = "";

    /** This variable contains the extensions to use when searching for
     *  configuration files.
     */
    private String[] ext = {
                               "",
                               ".xml"
                           };

    /** This variable contains the path to use when searching for
     *  configuration files.
     */
    private String[] path = {
                                "",
                                "config/"
                            };

    /**
     * Creates a new object.
     * <p>
     * The path given is the location of the XML file containing the
     * configuration information. This path is used to determine the XML file
     * utilizing the class loader for this class. Thus it is possible to place
     * the XML file into a jar archive.
     * </p>
     * <p>
     * Beside of the class loader a search is performed by appending
     * <tt>.xml</tt> and/or prepending <tt>config/</tt> if the path is not 
     * sufficient to find the resource.
     * </p>
     * <p>Example
     * <pre>
     *      cfg = new XMLConfig("cfg");
     * </pre>
     * searches the following files on the classpath:
     * <pre>
     *   cfg
     *   cfg.xml
     *   config/cfg
     *   config/cfg.xml
     * </pre>
     * </p>
     *
     * @param path the name of the resource to be used
     *
     * @throws ConfigNotFoundException in case that the named path does not
     * lead to a resource
     * @throws ConfigSyntaxException in case that the resource contains syntax
     * errors
     * @throws ConfigException in case of an IO exception while reading the
     * resource
     */
    public ConfigurationXMLImpl(String path)
                         throws ConfigurationException {
        super();

        if (path == null || path.equals("")) {
            throw new ConfigurationInvalidNameException(Messages.format("XMLConfig.Empty_path"));
        }

        int i = path.lastIndexOf("/");

        if (i >= 0) {
            base = path.substring(0, i + 1);
        }

        InputStream stream = findConfig(path);

        if (stream == null) {
            throw new ConfigurationNotFoundException(path);
        }

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                                                            .newDocumentBuilder();
            root = builder.parse(stream)
                          .getDocumentElement();
        } catch (IOException e) {
            throw new ConfigurationException(Messages.format("XMLConfig.IO_Exception"),
                                             e);
        } catch (Exception e) {
            throw new ConfigurationSyntaxException(e.getMessage(), path);
        } catch (FactoryConfigurationError e) {
            throw new ConfigurationSyntaxException(e.getMessage(), path);
        }
    }

    /**
     * Creates a new object with a given root element.
     * This constructor is private since it is meant for internal purposes
     * only.
     *
     * @param top the new root element
     */
    private ConfigurationXMLImpl(Element top, String base) {
        super();
        root      = top;
        this.base = base;
    }

    /**
     * @see de.dante.util.configuration.Configuration#getAttribute(java.lang.String)
     */
    public String getAttribute(String name)
                        throws ConfigurationException {
        return root.getAttribute(name);
    }

    /**
     * Extract a sub-configuration with a given name.
     * <p>
     * Consider the following example with the configuration currently rooted
     * at cfg:
     * </p>
     * <pre>
     *   &lt;cfg&gt;
     *     . . .
     *     &lt;abc&gt;
     *     . . .
     *     &lt;/abc&gt;
     *     . . .
     *   &lt;/cfg&gt;
     * </pre>
     * <p>
     * Then <tt>getConfig("abc")</tt> returns a new XMLConfig rooted at abc.
     * </p>
     * <p>
     * If there are more than one tags with the same name then the first one is
     * used.
     * </p>
     * <p>
     * If there are no tags with the given name then an exception is thrown.
     * </p>
     *
     * @param name the tag name of the sub-configuration
     *
     * @return the sub-configuration
     *
     * @throws ConfigNotFoundException in case that the given name does not
     * correspond to one of the tags in the current configuration
     */
    public Configuration getConfiguration(String name)
                                   throws ConfigurationException {
        for (Node node = root.getFirstChild(); node != null;
                 node = node.getNextSibling()) {
            if (node.getNodeName()
                        .equals(name)) {
                String src = ((Element) node).getAttribute("src");

                return (src != null && !src.equals("")
                        ? new ConfigurationXMLImpl(base + src)
                        : new ConfigurationXMLImpl((Element) node, base));
            }
        }

        throw new ConfigurationNotFoundException("<" +
                                                 root.getNodeName() +
                                                 "><" + name + ">");
    }

    /**
     * Extract a sub-configuration with a given name and a given attribute.
     * <p>
     * Consider the following example with the configuration currently rooted
     * at cfg:
     * </p>
     * <pre>
     *   &lt;cfg&gt;
     *     . . .
     *     &lt;abc name="one"&gt;
     *     . . .
     *     &lt;/abc&gt;
     *     &lt;abc name="two"&gt;
     *     . . .
     *     &lt;/abc&gt;
     *     . . .
     *   &lt;/cfg&gt;
     * </pre>
     * <p>
     * Then <tt>getConfig("abc","two")</tt> returns a new XMLConfig rooted at
     * the abc with the name attribute "two".
     * </p>
     * <p>
     * If there are more than one tags with the same name then the first one is
     * used.
     * </p>
     * <p>
     * If there are no tags with the given name then an exception is thrown.
     * </p>
     *
     * @param key the tag name of the sub-configuration
     * @param attribute the value of the attribute name
     *
     * @return the sub-configuration
     *
     * @throws ConfigNotFoundException in case that the given name does not
     * correspond to one of the tags in the current configuration
     */
    public Configuration getConfiguration(String key, String attribute)
                                   throws ConfigurationException {
        for (Node node = root.getFirstChild(); node != null;
                 node = node.getNextSibling()) {
            if (key.equals(node.getNodeName()) &&
                    attribute.equals(((Element) node).getAttribute("name"))) {
                return new ConfigurationXMLImpl((Element) node, base);
            }
        }

        throw new ConfigurationNotFoundException(null,
                                                 key + "[" + attribute +
                                                 "]");
    }

    /**
     * Get the text value of the first tag with a given name in the
     * configuration. If none is found then the empty string is returned.
     * <p>
     * Consider the following example with the configuration currently rooted
     * at cfg:
     * </p>
     * <pre>
     *   &lt;cfg&gt;
     *     . . .
     *     &lt;one&gt;the first value&lt;/one&gt;
     *     &lt;two&gt;the second value&lt;/two&gt;
     *     . . .
     *   &lt;/cfg&gt;
     * </pre>
     * <p>
     * Then <tt>getValue(cfgNode,"two")</tt> returns the String
     * "the second value".
     * </p>
     *
     * @param element the element node to start from
     * @param tag the the tag in element
     *
     * @return the value of the tag in element or the empty string
     */
    public String getValue(Element element, String tag) {
        for (Node node = element.getFirstChild(); node != null;
                 node = node.getNextSibling()) {
            if (tag.equals(node.getNodeName())) {
                return getNodeValue(node);
            }
        }

        return "";
    }

    /**
     * Get the text value of the first tag with a given name in the
     * configuration. If none is found then the empty string is returned.
     * <p>
     * Consider the following example with the configuration currently rooted
     * at cfg:
     * </p>
     * <pre>
     *   &lt;cfg&gt;
     *     . . .
     *     &lt;one&gt;the first value&lt;/one&gt;
     *     &lt;two&gt;the second value&lt;/two&gt;
     *     . . .
     *   &lt;/cfg&gt;
     * </pre>
     * <p>
     * Then <tt>getValue("two")</tt> returns the String "the second value".
     * </p>
     *
     * @param tag the name of the tag
     *
     * @return the value of the tag or the empty string
     */
    public String getValue(String tag) {
        for (Node node = root.getFirstChild(); node != null;
                 node = node.getNextSibling()) {
            if (tag.equals(node.getNodeName())) {
                return getNodeValue(node);
            }
        }

        return "";
    }

    /**
     * @see de.dante.util.configuration.Configuration#getValueAsInteger(java.lang.String, int)
     */
    public int getValueAsInteger(String key, int defaultValue)
                          throws ConfigurationException {
        String s = getValue(key);

        if (s != null && s.matches("[0-9]+")) {
            return Integer.parseInt(s);
        }

        return defaultValue;
    }

    /**
     * Get the list of all values with the given tag name in the current
     * configuration.
     *
     * @param tag the name of the tags
     *
     * @return the list of values
     */
    public StringList getValues(String tag) {
        StringList result = new StringList();

        for (Node node = root.getFirstChild(); node != null;
                 node = node.getNextSibling()) {
            if (tag.equals(node.getNodeName())) {
                result.add(getNodeValue(node));
            }
        }

        return result;
    }

    /**
     * @see de.dante.util.configuration.Configuration#iterator(java.lang.String)
     */
    public Iterator iterator(String key) {
        List list = new ArrayList();

        for (Node node = root.getFirstChild(); node != null;
                 node = node.getNextSibling()) {
            if (key.equals(node.getNodeName())) {
                list.add(new ConfigurationXMLImpl((Element) node, base));
            }
        }

        return list.iterator();
    }

    /**
     * @see de.dante.util.configuration.Configuration#getNodeValue(Node)
     */
    private String getNodeValue(Node node) {
        StringBuffer sb = new StringBuffer();

        for (Node n = node.getFirstChild(); n != null;
                 n = n.getNextSibling()) {
            if (n.getNodeType() == Node.TEXT_NODE) {
                sb.append(n.getNodeValue());
            }
        }

        return sb.toString();
    }

    /**
     * Search fo a configuration file taking into account a list of prefixes
     * (path) and postfixes (ext)
     *
     * @param name the basename of the configuration to find.
     *
     * @return an input stream to the requested configuration or 
     * <code>null</code> if none could be opened.
     */
    private InputStream findConfig(String name) {
        ClassLoader classLoader = getClass()
                                      .getClassLoader();

        for ( int pi=0;pi<path.length;pi++ ) {
            for ( int ei=0;ei<ext.length;ei++ ) {
                InputStream stream = classLoader.getResourceAsStream(path[pi]+ name + ext[ei]);

                if (stream != null) return stream;
            }
        }
        return null;
    }

    /**
     * @see de.dante.util.configuration.Configuration#getValue()
     */
    public String getValue() throws ConfigurationException {
        return getNodeValue(root);
    }

}
