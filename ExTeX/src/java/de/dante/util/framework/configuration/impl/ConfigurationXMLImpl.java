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

package de.dante.util.framework.configuration.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import de.dante.util.StringList;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationIOException;
import de.dante.util.framework.configuration.exception.ConfigurationInvalidResourceException;
import de.dante.util.framework.configuration.exception.ConfigurationNotFoundException;
import de.dante.util.framework.configuration.exception.ConfigurationSyntaxException;

/**
 * This class provides means to deal with configurations stored as XML files.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class ConfigurationXMLImpl implements Configuration, Serializable {

    /**
     * This inner class provides an iterator for all sub-configurations of a
     * given node.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.8 $
     */
    private class ConfigIterator implements Iterator {

        /**
         * The field <tt>node</tt> contains the current node.
         */
        private Node node;

        /**
         * Creates a new object.
         *
         * @param node
         */
        protected ConfigIterator(final Node node) {

            super();
            this.node = node;
        }

        /**
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {

            return node.getNextSibling() != null;
        }

        /**
         * @see java.util.Iterator#next()
         */
        public Object next() {

            node = node.getNextSibling();
            return node;
        }

        /**
         * @see java.util.Iterator#remove()
         */
        public void remove() {

            throw new UnsupportedOperationException();
        }

    }

    /**
     * The field <tt>ext</tt> contains extensions to use when searching for
     * configuration files.
     */
    private static final String[] EXTENSIONS = {"", ".xml"};

    /**
     * The field <tt>path</tt> contains the path to use when searching for
     * configuration files.
     */
    private static final String[] PATHS = {"", "config/"};

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * Recursively collect the Xpath from the root to the given node.
     *
     * @param sb the output string buffer
     * @param node the node to start with
     */
    private static void toString(final StringBuffer sb, final Node node) {

        Node p = node.getParentNode();
        if (p != null && !(p instanceof Document)) {
            toString(sb, p);
        }
        sb.append('/');
        sb.append(node.getNodeName());
    }

    /**
     * The field <tt>base</tt> contains the base of the resource name; i.e.
     * the resource up to the last slash or the empty string if no slash is
     * contained.
     */
    private String base;

    /**
     * The field <tt>resource</tt> contains the name of the resource.
     */
    private String resource;

    /**
     * The field <tt>root</tt> contains the root element for this configuration.
     */
    private Element root;

    /**
     * Creates a new object with a given root element. This constructor is
     * private since it is meant for internal purposes only.
     *
     * @param root the new root element
     * @param base the base for the resource
     * @param resource the name of the resource
     */
    private ConfigurationXMLImpl(final Element root, final String base,
            final String resource) {

        super();
        this.root = root;
        this.base = base;
        this.resource = resource;
    }

    /**
     * Creates a new object and fills it with the configuration read from an
     * input stream.
     * <p>
     * The path given is the location of the XML resource (file) containing the
     * configuration information. This path is used to determine the XML
     * resource utilizing the class loader for this class.
     * Thus it is possible to place the XML file into a jar archive.
     * </p>
     * <p>
     * Beside of the class loader a search is performed by appending
     * <tt>.xml</tt> and/or prepending <tt>config/</tt> if the path is not
     * sufficient to find the resource.
     * </p>
     *
     * @param stream the stream to read the configuration from.
     * @param resource the name of the resource to be used;
     *  i.e. something like the file name
     *
     * @throws ConfigurationInvalidResourceException in case that the given
     *  resource name is <code>null</code> or empty.
     * @throws ConfigurationNotFoundException in case that the named path does
     *  not lead to a resource.
     * @throws ConfigurationSyntaxException in case that the resource contains
     *  syntax errors.
     * @throws ConfigurationIOException in case of an IO exception while
     *  reading the resource.
     */
    public ConfigurationXMLImpl(final InputStream stream, final String resource)
            throws ConfigurationInvalidResourceException,
                ConfigurationNotFoundException,
                ConfigurationSyntaxException,
                ConfigurationIOException {

        super();
        readConfiguration(stream, resource, "");
    }

    /**
     * Creates a new object.
     * <p>
     *  The path given is the location of the XML file containing the
     *  configuration information. This path is used to determine the XML file
     *  utilizing the class loader for this class. Thus it is possible to place
     *  the XML file into a jar archive.
     * </p>
     * <p>
     *  Beside of the class loader a search is performed by appending
     *  <tt>.xml</tt> and/or prepending <tt>config/</tt> if the path is not
     *  sufficient to find the resource.
     * </p>
     *
     * <h3>Example</h3>
     * <p>
     *  Consider the following creation of an instance of this class
     *  <pre>
     *   cfg = new ConfigurationXMLImpl("cfg");
     *  </pre>
     *  Then the following files are searched on the classpath until one is
     *  found:
     *  <pre>
     *     cfg   cfg.xml   config/cfg   config/cfg.xml
     *  </pre>
     * </p>
     *
     *
     *
     *
     *
     * @param resource the name of the resource to be used;
     * i.e. the file name
     *
     * @throws ConfigurationInvalidResourceException in case that the given
     *  resource name is <code>null</code> or empty
     * @throws ConfigurationNotFoundException in case that the named path does
     *  not lead to a resource
     * @throws ConfigurationSyntaxException in case that the resource contains
     *  syntax errors
     * @throws ConfigurationIOException in case of an IO exception while
     *  reading the resource
     */
    public ConfigurationXMLImpl(final String resource)
            throws ConfigurationInvalidResourceException,
                ConfigurationNotFoundException,
                ConfigurationSyntaxException,
                ConfigurationIOException {

        super();

        if (resource == null || resource.equals("")) {
            throw new ConfigurationInvalidResourceException();
        }

        int i = resource.lastIndexOf("/");

        readConfiguration(locateConfiguration(resource, //
                getClass().getClassLoader()), //
                resource, //
                (i >= 0 ? resource.substring(0, i + 1) : ""));
    }

    /**
     * Extract a sub-configuration with a given name.
     * <p>
     *  Consider the following example with the configuration currently rooted
     *  at cfg:
     * </p>
     *
     * <pre>
     *  &lt;cfg&gt;
     *    . . .
     *    &lt;abc&gt;
     *      . . .
     *    &lt;/abc&gt;
     *    . . .
     *  &lt;/cfg&gt;
     * </pre>
     *
     * <p>
     *  Then <tt>findConfiguration("abc")</tt> returns a new XMLConfig
     *  rooted at abc.
     * </p>
     * <p>
     *  If there are more than one tags with the same name then the first one is
     *  used.
     * </p>
     * <p>
     *  If there are no tags with the given name then an exception is thrown.
     * </p>
     *
     * @param name the tag name of the sub-configuration
     *
     * @return the sub-configuration or <code>null</code> if none was found
     *
     * @throws ConfigurationInvalidResourceException in case that the given
     *  resource name is <code>null</code> or empty
     * @throws ConfigurationNotFoundException in case that the named path does
     *  not lead to a resource
     * @throws ConfigurationSyntaxException in case that the resource contains
     *  syntax errors
     * @throws ConfigurationIOException in case of an IO exception while
     *  reading the resource
     *
     * @see #getConfiguration(java.lang.String)
     * @see de.dante.util.framework.configuration.Configuration#findConfiguration(
     *      java.lang.String)
     */
    public Configuration findConfiguration(final String name)
            throws ConfigurationInvalidResourceException,
                ConfigurationNotFoundException,
                ConfigurationSyntaxException,
                ConfigurationIOException {

        for (Node node = root.getFirstChild(); node != null; node = node
                .getNextSibling()) {
            if (node.getNodeName().equals(name)) {

                String src = ((Element) node).getAttribute("src");

                if (src == null || src.equals("")) {
                    return new ConfigurationXMLImpl((Element) node, base,
                            resource);
                }

                return new ConfigurationXMLImpl(base + src).src(name, node);
            }
        }

        return null;
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
     * If there are no tags with the given name then <code>null</code> is
     * returned.
     * </p>
     *
     * @param key the tag name of the sub-configuration
     * @param attribute the value of the attribute name
     *
     * @return the sub-configuration
     *
     * @throws ConfigurationException in case of other errors.
     */
    public Configuration findConfiguration(final String key,
            final String attribute) throws ConfigurationException {

        for (Node node = root.getFirstChild(); node != null; node = node
                .getNextSibling()) {
            if (key.equals(node.getNodeName())
                    && attribute.equals(((Element) node)
                            .getAttribute(attribute))) {
                return new ConfigurationXMLImpl((Element) node, base, resource);
            }
        }

        return null;
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#getAttribute(
     *      java.lang.String)
     */
    public String getAttribute(final String name) {

        return (this.root.getAttributeNode(name) == null ? null : //
                this.root.getAttribute(name));
    }

    /**
     * Extract a sub-configuration with a given name.
     * <p>
     * Consider the following example with the configuration currently rooted
     * at cfg:
     * </p>
     *
     * <pre>
     *  &lt;cfg&gt;
     *    . . .
     *    &lt;abc&gt;
     *      . . .
     *    &lt;/abc&gt;
     *    . . .
     *  &lt;/cfg&gt;
     * </pre>
     *
     * <p>
     * Then <tt>getConfiguration("abc")</tt> returns a new XMLConfig rooted at
     * abc.
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
     * @throws ConfigurationNotFoundException in case that the configuration
     *  does not exist.
     * @throws ConfigurationIOException in case that an IOException occurred
     *  while reading the configuration.
     * @throws ConfigurationSyntaxException in case of a syntax error in the
     *  configuration.
     * @throws ConfigurationInvalidResourceException in case that the given
     *  resource name is <code>null</code> or empty
     *
     * @see #findConfiguration(String)
     */
    public Configuration getConfiguration(final String name)
            throws ConfigurationInvalidResourceException,
                ConfigurationNotFoundException,
                ConfigurationSyntaxException,
                ConfigurationIOException {

        Configuration cfg = findConfiguration(name);

        if (cfg == null) {
            throw new ConfigurationNotFoundException(name, toString());
        }
        return cfg;
    }

    /**
     * Extract a sub-configuration with a given name and a given attribute.
     * <p>
     * Consider the following example with the configuration currently rooted
     * at cfg:
     * </p>
     *
     * <pre>
     *  &lt;cfg&gt;
     *    . . .
     *    &lt;abc name="one"&gt;
     *      . . .
     *    &lt;/abc&gt;
     *    &lt;abc name="two"&gt;
     *      . . .
     *    &lt;/abc&gt;
     *    . . .
     *  &lt;/cfg&gt;
     * </pre>
     *
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
     * @throws ConfigurationNotFoundException in case that the given name does
     * not correspond to one of the tags in the current configuration
     * @throws ConfigurationException in case of some other kind of error
     */
    public Configuration getConfiguration(final String key,
            final String attribute)
            throws ConfigurationNotFoundException,
                ConfigurationException {

        for (Node node = root.getFirstChild(); node != null; node = node
                .getNextSibling()) {
            if (key.equals(node.getNodeName())
                    && attribute.equals(((Element) node)
                            .getAttribute(attribute))) {
                return new ConfigurationXMLImpl((Element) node, base, resource);
            }
        }

        throw new ConfigurationNotFoundException(null, //
                key + "[" + attribute + "]");
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#getNodeValue(Node)
     */
    private String getNodeValue(final Node node) {

        StringBuffer sb = new StringBuffer();

        for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n.getNodeType() == Node.TEXT_NODE) {
                sb.append(n.getNodeValue());
            }
        }

        return sb.toString();
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#getValue()
     */
    public String getValue() throws ConfigurationException {

        return getNodeValue(root);
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
    public String getValue(final Element element, final String tag) {

        for (Node node = element.getFirstChild(); node != null; node = node
                .getNextSibling()) {
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
    public String getValue(final String tag) {

        for (Node node = root.getFirstChild(); node != null; node = node
                .getNextSibling()) {
            if (tag.equals(node.getNodeName())) {
                return getNodeValue(node);
            }
        }

        return "";
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#getValueAsInteger(
     *      java.lang.String, int)
     */
    public int getValueAsInteger(final String key, final int defaultValue)
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
     *
     * @see de.dante.util.framework.configuration.Configuration#getValues(java.lang.String)
     */
    public StringList getValues(final String tag) {

        StringList result = new StringList();
        getValues(result, tag);
        return result;
    }

    /**
     * Get the list of all values with the given tag name in the current
     * configuration and append them to a given StringList.
     *
     * @param key the name of the tags
     * @param list the list to append the values to
     *
     * @see de.dante.util.framework.configuration.Configuration#getValues(
     *      de.dante.util.StringList, java.lang.String)
     */
    public void getValues(final StringList list, final String key) {

        for (Node node = root.getFirstChild(); node != null; node = node
                .getNextSibling()) {
            if (key.equals(node.getNodeName())) {
                list.add(getNodeValue(node));
            }
        }
    }

    /**
     * Get an iterator for all sub-configurations.
     *
     * @return an iterator for all sub-configurations
     *
     * @see de.dante.util.framework.configuration.Configuration#iterator()
     */
    public Iterator iterator() {

        return new ConfigIterator(root.getFirstChild());
    }

    /**
     * Retrieve an iterator over all items of a sub-configuration.
     *
     * @param key the name of the sub-configuration
     *
     * @return the iterator
     *
     * @throws ConfigurationIOException in case that an IO exception occurs
     *  during the reading of the configuration.
     * @throws ConfigurationSyntaxException in case that the configuration
     *  contains a syntax error.
     * @throws ConfigurationNotFoundException in case that the specified
     *  configuration can not be found.
     * @throws ConfigurationInvalidResourceException in case that the resource
     *  is invalid
     *
     * @see de.dante.util.framework.configuration.Configuration#iterator(java.lang.String)
     */
    public Iterator iterator(final String key)
            throws ConfigurationInvalidResourceException,
                ConfigurationNotFoundException,
                ConfigurationSyntaxException,
                ConfigurationIOException {

        List list = new ArrayList();

        for (Node node = root.getFirstChild(); node != null; node = node
                .getNextSibling()) {
            if (key.equals(node.getNodeName())) {
                String src = ((Element) node).getAttribute("src");
                if (src == null || src.equals("")) {
                    list.add(new ConfigurationXMLImpl((Element) node, base,
                            resource));
                } else {
                    list.add(new ConfigurationXMLImpl(base + src)
                            .src(key, node));
                }
            }
        }

        return list.iterator();
    }

    /**
     * Search for a configuration file taking into account a list of prefixes
     * (path) and postfixes (ext)
     *
     * @param name the base name of the configuration to find. The path
     *  elements and extensions are attached in turn to build the complete
     *  name.
     * @param classLoader the class loader to use for finding the resource
     *
     * @return an input stream to the requested configuration or
     * <code>null</code> if none could be opened.
     */
    private InputStream locateConfiguration(final String name,
            final ClassLoader classLoader) {

        for (int pi = 0; pi < PATHS.length; pi++) {
            for (int ei = 0; ei < EXTENSIONS.length; ei++) {
                InputStream stream = classLoader.getResourceAsStream(PATHS[pi]
                        + name + EXTENSIONS[ei]);

                if (stream != null) {
                    return stream;
                }
            }
        }
        return null;
    }

    /**
     * Read the configuration from a stream.
     *
     * @param stream the stream to read the configuration from.
     * @param theResource the name of the resource to be used;
     *  i.e. something like the file name
     * @param theBase the new value for base
     *
     * @throws ConfigurationNotFoundException in case that the configuration
     *  could not be found
     * @throws ConfigurationIOException in case of an IO error during reading
     * @throws ConfigurationSyntaxException in case of a syntax error in the
     *  configuration XML
     */
    protected void readConfiguration(final InputStream stream,
            final String theResource, final String theBase)
            throws ConfigurationNotFoundException,
                ConfigurationIOException,
                ConfigurationSyntaxException {

        if (stream == null) {
            throw new ConfigurationNotFoundException(theResource, null);
        }
        this.resource = theResource;
        this.base = theBase;

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            root = builder.parse(stream).getDocumentElement();
        } catch (IOException e) {
            throw new ConfigurationIOException(null, e);
        } catch (ParserConfigurationException e) {
            throw new ConfigurationSyntaxException(e.getLocalizedMessage(),
                    theResource);
        } catch (SAXException e) {
            throw new ConfigurationSyntaxException(e.getLocalizedMessage(),
                    theResource);
        } catch (FactoryConfigurationError e) {
            throw new ConfigurationSyntaxException(e.getLocalizedMessage(),
                    theResource);
        }
    }

    /**
     * Recursively follow the src attribute if present.
     *
     * @param name the name of the current tag
     * @param node the current DOM node
     *
     * @return the configuration
     *
     * @throws ConfigurationInvalidResourceException in case of an invalid
     *  resource
     * @throws ConfigurationNotFoundException in case of a missing
     *  configuration
     * @throws ConfigurationSyntaxException in case of an syntax error
     * @throws ConfigurationIOException in case of an IO error
     */
    protected Configuration src(final String name, final Node node)
            throws ConfigurationInvalidResourceException,
                ConfigurationNotFoundException,
                ConfigurationSyntaxException,
                ConfigurationIOException {

        String src = ((Element) root).getAttribute("src");

        if (src == null || src.equals("")) {
            return this;
        }
        Configuration cfg = new ConfigurationXMLImpl(base + src)
                .src(name, root);
        if (!((ConfigurationXMLImpl) cfg).root.getNodeName().equals(name)) {
            throw new ConfigurationNotFoundException(name, src);
        }
        return cfg;
    }

    /**
     * Get the printable representation of this configuration.
     * Something like an XPath expression describing the configuration is
     * produced for this instance.
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        if (resource != null) {
            sb.append("document(\"");
            sb.append(resource);
            sb.append("\")");
        }
        toString(sb, root);
        return sb.toString();
    }

}