/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
 */

package de.dante.util.doclet;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.Tag;
import com.sun.javadoc.ThrowsTag;
import com.sun.javadoc.Type;

/**
 * javadoc-doclet for XML Output.
 *
 * @author <a href="mailto:Alexander.Kraenzlein@gmx.de">Alexander Kraenzlein</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class XMLDoclet1 extends Doclet {

    /**
     * root-element
     */
    private static Element rootelement;

    /**
     * called by javadoc to format the document
     *
     * @param root the root of the starting document
     * @return always true
     */
    public static boolean start(final RootDoc root) {

        System.out.println("XMLDoclet1");

        // init öffnet packages.xml , ...
        init();

        // print packages.xml
        rootelement = new Element("root");

        //main.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        //main.println("<!DOCTYPE packages SYSTEM \"" + dtd + "\" [");

        // get all packages from the root and put it on the Hashtable
        createPackagesMap(root.classes());

        //erstellen von verweisen für packagename.xml (Entities)
        //<!ENTITY package.name SYSTEM "pacvkage.name.xml">
        //        Iterator it = map.keySet().iterator();
        //        while (it.hasNext()) {
        //            Package pkg = (Package) map.get(it.next());
        //            main.println("<!ENTITY " + pkg.getPkg() + " SYSTEM \""
        //                    + pkg.getPkg() + ".xml\" >");
        //        }
        //        main.println("]>"); //DTD schließen
        //main.println("<packages>");

        // format the packages
        makePackages();

        writeRootElement();
        return true;
    }

    /**
     * filebuffer
     */
    private static final int FILEBUFFER = 0x8000;

    /**
     * Write the root-element in a file.
     */
    private static void writeRootElement() {

        try {
            XMLOutputter xmlout = new XMLOutputter("   ", true);
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(outdir +  "/extex.xml"), FILEBUFFER);
            Document doc = new Document(rootelement);
            xmlout.output(doc, out);
            out.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * format the packages
     */
    private static void makePackages() {

        // for each package
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {

            // get the next package
            Package pkg = (Package) map.get(it.next());

            Element packages = new Element("package");
            rootelement.addContent(packages);

            System.out.println("* Package: " + pkg.getPkg());

            //Verweis in main eintragen! (&package.name)
            packages.setAttribute("name", pkg.getPkg());

            // interfaces
            layoutClasses(packages, "interfaces", pkg.getInterfaces());

            // classes
            layoutClasses(packages, "classes", pkg.getClasses());

            // exceptions
            layoutClasses(packages, "exceptions", pkg.getExceptions());

            // errors
            layoutClasses(packages, "errors", pkg.getErrors());

            // primitives
            layoutClasses(packages, "primitives", pkg.getPrimitives());

        }
    }

    /**
     * get all packages from the root and add put it on the Hashtable
     * @param cls   classdoc
     */
    private static void createPackagesMap(final ClassDoc[] cls) {

        for (int i = 0; i < cls.length; ++i) {
            ClassDoc cd = cls[i];
            Package v;
            String pkg = cd.containingPackage().name();
            if ((v = (Package) map.get(pkg)) == null) {
                v = new Package(pkg);
                map.put(pkg, v);
            }
            v.addElement(cd);
        }
    }

    /**
     * Returns how many arguments would be consumed if \code{option} is a
     * recognized option.
     *
     * @param option the option to check
     * @return int
     */
    public static int optionLength(final String option) {

        if (option.equals("-dtd")) {
            return 2;
        } else if (option.equals("-help")) {
            System.err.println("XMLDoclet1 Usage:\n");
            System.err
                    .println("-outdir <outdir>    Specifies the output directory where to write to.  If none");
            System.err
                    .println("                    specified, the default is in the subdirectory \"./xmldoc\"");
            System.err
                    .println("-dtd <dtd>          XML Domain Type Definition, default  is \"XMLDoclet.dtd\"");
            System.err
                    .println("					Only necessairy if you need a valid XML File");
            System.err
                    .println("-isXML 				If not set, Converting Tags from HTML to XML");
            System.err
                    .println("					is overruled by Tag in a class (@isXML od @isHTML)");
            return 1;
        } else if (option.equals("-noheader")) {
            return 1;
        } else if (option.equals("-outdir")) {
            return 2;
        } else if (option.equals("-isXML")) {
            return 1;
        }
        System.out.println("unknown option " + option);
        return Doclet.optionLength(option);
    }

    /**
     * Checks the passed options and their arguments for validity.
     *
     * @param args the arguments to check
     * @param err the interface to use for reporting errors
     * @return true
     */
    public static boolean validOptions(final String[][] args,
            final DocErrorReporter err) {

        for (int i = 0; i < args.length; ++i) {
            if (args[i][0].equals("-outdir")) {
                outdir = args[i][1];
            } else if (args[i][0].equals("-dtd")) {
                dtd = args[i][1];
            } else if (args[i][0].equals("-isXML")) {
                defaultIsXML = true;
            }
        }
        return true;
    }

    /**
     * Layout a Class, Interface, ...
     * @param parent the parent element
     * @param type which type (classes, interfaces, ...)
     * @param classes list of all methods/...
     */
    static void layoutClasses(final Element parent, final String type,
            final TreeMap classes) {

        if (classes.size() > 0) {
            Element child = new Element(type);
            parent.addContent(child);

            Iterator it = classes.keySet().iterator();
            while (it.hasNext()) {

                // get next
                ClassDoc cd = (ClassDoc) classes.get(it.next());

                // class or interface Reale Form der Implementierung...
                String realtype = "class";
                if (!type.equals("classes")) {
                    realtype = "interface";
                }

                System.out.println("  " + cd.name());

                Element realtypeelement = new Element(realtype);
                child.addContent(realtypeelement);

                realtypeelement.setAttribute("name", cd.name());

                isXML = defaultIsXML;

                // classs/interface tags ausgeben
                // isXML nach Tag setzen!
                printTag(child, cd.tags());

                String cmt = cd.commentText();
                if (!cmt.equals("")) {
                    Element text = new Element("text");
                    child.addContent(text);
                    text.addContent(makeText(cmt));
                }
                // declaration
                Element declaration = new Element("declaration");
                child.addContent(declaration);
                declaration.addContent(cd.modifiers() + ' ');
                if (!cd.isInterface()) {
                    declaration.addContent("class ");
                }
                declaration.addContent(cd.name());

                ClassDoc sc = cd.superclass();
                if (sc != null) {
                    declaration.setAttribute("superclass", sc.qualifiedName());
                }

                // implemented Interfaces
                ClassDoc[] intf = cd.interfaces();
                if (intf.length > 0) {
                    Element impl = new Element("implements");
                    child.addContent(impl);

                    for (int j = 0; j < intf.length; ++j) {
                        ClassDoc in = intf[j];
                        String nm;
                        if (in.containingPackage().name().equals(
                                cd.containingPackage().name())) {
                            nm = in.name();
                        } else {
                            nm = in.qualifiedName();
                        }
                        if (j > 0) {
                            Element im = new Element("implement");
                            impl.addContent(im);
                            im.addContent(nm);
                        }
                    }
                }
                // serializable Fields
                ExecutableMemberDoc[] mems;
                FieldDoc[] flds;
                flds = cd.serializableFields();
                if (flds.length > 0) {
                    Element serfield = new Element("serializableFields");
                    child.addContent(serfield);
                    printFields(serfield, cd, flds);
                }

                // fields
                flds = cd.fields();
                if (flds.length > 0) {
                    Element fields = new Element("Fields");
                    child.addContent(fields);
                    printFields(fields, cd, flds);
                }

                // constructor
                mems = cd.constructors();
                if (mems.length > 0) {
                    Element constr = new Element("constructors");
                    child.addContent(constr);
                    printMembers(constr, cd, mems, true);
                }

                // methods
                mems = cd.methods();
                if (mems.length > 0) {
                    Element meth = new Element("methods");
                    child.addContent(meth);
                    printMembers(meth, cd, mems, true);
                }

                // parents
                ClassDoc par = cd.superclass();
                if (par != null
                        && !par.qualifiedName().equals("java.lang.Object")) {
                    Element parel = new Element("parents");
                    child.addContent(parel);

                    while (par != null
                            && !par.qualifiedName().equals("java.lang.Object")) {
                        Element parentel = new Element("parent");
                        parel.addContent(parentel);
                        parentel.addContent(par.qualifiedName());
                        mems = par.methods();
                        printMembers(parentel, par, mems, false);
                        par = par.superclass();
                    }
                }
            }
        }
    }

    /**
     * print the tags
     * @param parent the parent
     * @param tags print it
     */
    private static void printTag(final Element parent, final Tag[] tags) {

        // found tags?
        if (tags.length > 0) {
            for (int k = 0; k < tags.length; k++) {
                if (tags[k].name().equals("@see")) {
                    Element ref = new Element("ref");
                    parent.addContent(ref);
                    ref.addContent(tags[k].text());
                } else if (tags[k].name().equals("@isHTML")) {
                    isXML = false;
                } else if (tags[k].name().equals("@isXML")) {
                    isXML = true;
                } else {
                    Element x = new Element(tags[k].name().substring(1));
                    parent.addContent(x);
                    x.addContent(tags[k].text());
                }
            }
        }
    }

    /**
     * Enumerates the fields passed and formats them using Tex statements.
     * @param parent the parent
     * @param cd      classdoc
     * @param flds    the fields to format
     */
    static void printFields(final Element parent, final ClassDoc cd,
            final FieldDoc[] flds) {

        for (int i = 0; i < flds.length; ++i) {
            FieldDoc f = flds[i];
            if (f.isPublic() || f.isPrivate()) {
                Element field = new Element("field");
                parent.addContent(field);

                Element name = new Element("name");
                field.addContent(name);
                name.addContent(f.name());

                Element modifiers = new Element("modifiers");
                field.addContent(modifiers);
                modifiers.addContent(f.modifiers());

                Element type = new Element("type");
                field.addContent(type);
                type.addContent(f.type().typeName());

                Element text = new Element("text");
                field.addContent(text);
                text.addContent(makeText(f.commentText()));
            }
        }
    }

    /**
     * Enumerates the members of a section of the document and formats them
     * using Tex statements.
     * @param parent    the parent
     * @param cd        classoc
     * @param dmems     the members of this entity
     * @param labels    labels
     */
    static void printMembers(final Element parent, final ClassDoc cd,
            final ExecutableMemberDoc[] dmems, final boolean labels) {

        if (dmems.length == 0) {
            return;
        }
        List l = Arrays.asList(dmems);
        Collections.sort(l);
        Iterator itr = l.iterator();
        for (int i = 0; itr.hasNext(); ++i) {
            ExecutableMemberDoc mem = (ExecutableMemberDoc) itr.next();
            ParamTag[] params = mem.paramTags();
            Element method = new Element("method");
            parent.addContent(method);

            Element name = new Element("name");
            method.addContent(name);
            name.addContent(mem.name());

            Element decl = new Element("declaration");
            method.addContent(decl);

            Element dmod = new Element("dmodifiers");
            decl.addContent(dmod);
            dmod.addContent(mem.modifiers());

            if (mem instanceof MethodDoc) {
                Element dreturn = new Element("dreturn");
                decl.addContent(dreturn);
                dreturn.addContent(((MethodDoc) mem).returnType().typeName());
            }

            // methoden-parameter wegen Deklarration Nötig
            Parameter[] parms = mem.parameters();
            if (parms.length > 0) {
                Element dparams = new Element("params");
                decl.addContent(dparams);
                for (int p = 0; p < parms.length; ++p) {
                    Type t = parms[p].type();
                    Element dparam = new Element("dparam");
                    dparams.addContent(dparam);

                    Element dname = new Element("name");
                    dparam.addContent(dname);
                    dname.addContent(parms[p].name());

                    Element dtype = new Element("type");
                    dparam.addContent(dtype);
                    dtype.addContent(t.qualifiedTypeName());

                    Element ddim = new Element("dimension");
                    dparam.addContent(ddim);
                    ddim.addContent(t.dimension());
                }
            }

            // usage
            String cmnt = mem.commentText();
            if (cmnt != null && !cmnt.equals("")) {
                Element usage = new Element("usage");
                parent.addContent(usage);

                Element text = new Element("text");
                usage.addContent(text);
                text.addContent(makeText(mem.commentText()));

                // parameters
                if (params.length > 0) {
                    Element paramsel = new Element("params");
                    parent.addContent(paramsel);

                    for (int j = 0; j < params.length; ++j) {
                        Element param = new Element("param");
                        paramsel.addContent(param);

                        Element nameel = new Element("name");
                        param.addContent(nameel);
                        nameel.addContent(params[j].parameterName());

                        Element textel = new Element("text");
                        param.addContent(textel);
                        textel
                                .addContent(makeText(params[j]
                                        .parameterComment()));
                    }
                }
                if (mem instanceof MethodDoc) {
                    Tag[] ret = mem.tags("return");
                    if (ret.length > 0) {
                        Element retel = new Element("returns");
                        parent.addContent(retel);
                        for (int j = 0; j < ret.length; ++j) {
                            retel.addContent(ret[j].text() + " ");
                        }
                    }
                }
                if (mem instanceof MethodDoc) {
                    ThrowsTag[] excp = ((MethodDoc) mem).throwsTags();
                    if (excp.length > 0) {
                        Element thr = new Element("throws");
                        parent.addContent(thr);

                        for (int j = 0; j < excp.length; ++j) {
                            Element exec = new Element("exception");
                            thr.addContent(exec);

                            String ename = excp[j].exceptionName();
                            ClassDoc cdoc = excp[j].exception();
                            if (cdoc != null) {
                                ename = cdoc.qualifiedName();
                            }
                            Element nameel = new Element("name");
                            exec.addContent(nameel);
                            nameel.addContent(ename);

                            Element textel = new Element("text");
                            exec.addContent(textel);
                            textel.addContent(makeText(excp[j]
                                    .exceptionComment()));
                        }
                    }
                }
                //References müssen von XML Auswertung verknüpft werden!
                SeeTag[] sees = mem.seeTags();
                if (sees.length > 0) {
                    Element seesel = new Element("sees");
                    parent.addContent(seesel);

                    for (int j = 0; j < sees.length; ++j) {
                        Element see = new Element("see");
                        seesel.addContent(see);
                        PackageDoc pd = sees[j].referencedPackage();
                        String pkg = "";
                        if (pd != null) {
                            pkg = pd.name() + ".";
                        }
                        String cls = sees[j].referencedClassName();
                        String memn = sees[j].referencedMemberName();
                        Element refel = new Element("ref");
                        seesel.addContent(refel);
                        refel.addContent(pkg + cls);
                        if (memn != null && !memn.equals("")) {
                            Element mark = new Element("mark");
                            refel.addContent(mark);
                            mark.addContent(memn);
                        }
                    }
                }
            }
        }
    }

    /**
     * Konvertiert.
     *
     * @param str    Der eventuell zu Konvertierende Text
     * @return den Konverteirten Text
     */
    static Element makeText(final String str) {
        Element rt = new Element("text");
        
        if (!isXML) {
            // dieser Teil muss umgestellt werden!!!!
            // jeder String <code> muss in ein Element umgewandelt werden
            
            StringBuffer ret = new StringBuffer(str.length());
            for (int i = 0; i < str.length(); ++i) {
                int c = str.charAt(i);
                switch (c) {
                    case '<' :
                        //Code Artiges
                        if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "<tt>")) {
                            ret.append("<code>");
                            i += 3;
                        } else if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "</tt>")) {
                            ret.append("</code>");
                            i += 4;
                        } else if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "<pre>")) {
                            ret.append("<code>");
                            i += 4;
                        } else if (str.length() > i + 5
                                && str.substring(i, i + 6).equalsIgnoreCase(
                                        "</pre>")) {
                            ret.append("</code>");
                            i += 5;
                        } else if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "</tt>")) {
                            ret.append("</code>");
                            i += 4;
                        } else if (str.length() > i + 5
                                && str.substring(i, i + 6).equalsIgnoreCase(
                                        "<code>")) {
                            ret.append("<code>");
                            i += 5;
                        } else if (str.length() > i + 6
                                && str.substring(i, i + 7).equalsIgnoreCase(
                                        "</code>")) {
                            ret.append("</code>");
                            i += 6;
                            //Schrift Artiges
                        } else if (str.length() > i + 2
                                && str.substring(i, i + 3).equalsIgnoreCase(
                                        "<b>")) {
                            ret.append("<bold>");
                            i += 2;
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "</b>")) {
                            ret.append("</bold>");
                            i += 3;
                        } else if (str.length() > i + 2
                                && str.substring(i, i + 3).equalsIgnoreCase(
                                        "<i>")) {
                            ret.append("<emph>");
                            i += 2;
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "</i>")) {
                            ret.append("</emph>");
                            i += 3;
                            //Absatz/Zeilenbruch
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "<br>")) {
                            ret.append("\n");
                            i += 3;
                        } else if (str.length() > i + 2
                                && str.substring(i, i + 3).equalsIgnoreCase(
                                        "<p>")) {
                            ret.append("<par>");
                            i += 2;
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "</p>")) {
                            ret.append("</par>");
                            i += 3;
                            //TODO Aufzählungen, alle auf selben Typ
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "<ol>")) {
                            ret.append("<numeration>");
                            i += 3;
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "<li>")) {
                            ret.append("	<item>");
                            i += 3;
                        } else if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "</li>")) {
                            ret.append("</item>");
                            i += 4;
                        } else if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "</ol>")) {
                            ret.append("<numeration>");
                            i += 4;
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "<dl>")) {
                            ret.append("<numeration>");
                            i += 3;
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "<dt>")) {
                            ret.append("	<caption>");
                            i += 3;
                        } else if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "</dt>")) {
                            ret.append("</caption>");
                            i += 4;
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "<dd>")) {
                            ret.append("	<item>");
                            i += 3;
                        } else if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "</dd>")) {
                            ret.append("</item>");
                            i += 4;
                        } else if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "</dl>")) {
                            ret.append("</numeration>");
                            i += 4;
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "<ul>")) {
                            ret.append("<numeration>");
                            i += 3;
                        } else if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "</ul>")) {
                            ret.append("</numeration>");
                            i += 4;
                            //Tabelle
                        } else if (str.length() > i + 5
                                && str.substring(i, i + 6).equalsIgnoreCase(
                                        "<table")) {
                            ret.append("<table>");
                            i += str.indexOf('>', i) - i;//Korrektur wegen
                            // möglicher Attribute
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "<tr>")) {
                            ret.append("	<line>");
                            i += 3;
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "<td>")) {
                            ret.append("		<row>");
                            i += 3;
                        } else if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "</td>")) {
                            ret.append("</row>");
                            i += 4;
                            //TODO Table head
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "<th>")) {
                            ret.append("		<row>");
                            i += 3;
                        } else if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "</th>")) {
                            ret.append("</row>");
                            i += 4;
                        } else if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "</tr>")) {
                            ret.append("	</line>");
                            i += 4;
                        } else if (str.length() > i + 7
                                && str.substring(i, i + 8).equalsIgnoreCase(
                                        "</table>")) {
                            ret.append("</table>");
                            i += 7;
                        } else if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "<font")) {
                            i += str.indexOf('>', i) - i;//Attribute
                            // ignorieren -- font
                            // ignorieren
                        } else if (str.length() > i + 6
                                && str.substring(i, i + 7).equalsIgnoreCase(
                                        "</font>")) {
                            i += 6;
                            //Ansonsten
                        } else {
                            ret.append("&lt");
                        }
                        break;
                    case '&' :
                        if (str.length() > i + 4
                                && str.substring(i, i + 5).equalsIgnoreCase(
                                        "&amp;")) {
                            ret.append("&amp ");
                            i += 4;
                        } else if (str.length() > i + 5
                                && str.substring(i, i + 6).equalsIgnoreCase(
                                        "&nbsp;")) {
                            ret.append(" ");
                            i += 5;
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "&lt;")) {
                            ret.append("&lt ");
                            i += 3;
                        } else if (str.length() > i + 3
                                && str.substring(i, i + 4).equalsIgnoreCase(
                                        "&gt;")) {
                            ret.append("&gt ");
                            i += 3;
                        } else if (str.length() > i + 5
                                && str.substring(i, i + 6).equalsIgnoreCase(
                                        "&quot;")) {
                            ret.append("\"");
                            i += 5;
                        } else if (str.length() > i + 5
                                && str.substring(i, i + 6).equalsIgnoreCase(
                                        "&ExTeX")) {
                            ret.append("<ExTeX />");
                            i += 5;
                        } else
                            ret.append("&amp ");
                        break;
                    case '>' :
                        ret.append("&gt ");
                        break;
                    default :
                        ret.append((char) c);
                        break;
                }
            }
            rt.addContent(ret.toString());
        }
        return rt;
    }

    /**
     * TreeMap für packages (sorted!)
     */
    private static TreeMap map;

    /**
     * outdir
     */
    private static String outdir = "xmldoc";

    /**
     * dtd
     */
    private static String dtd = "XMLDoclet.dtd";

    /**
     * Variablen die Konvvertierung von HTML => XML steuern! Erlaubt Tag
     * \@isXML und \@isHTML
     */
    private static boolean defaultIsXML = false;

    /**
     * isXML
     */
    private static boolean isXML = false;

    /**
     * init
     */
    static void init() {

        // intit TreeMap (for packages)
        map = new TreeMap();

    }
}
