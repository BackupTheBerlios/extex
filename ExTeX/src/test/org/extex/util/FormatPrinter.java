/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package org.extex.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.dante.extex.interpreter.loader.LoaderException;
import de.dante.extex.interpreter.loader.SerialLoader;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FormatPrinter {

    /**
     * TODO gene: missing JavaDoc.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    protected interface PrintRoutine {

        void print(PrintStream out, Object obj);
    };

    private static Map aaa = new HashMap();

    private static boolean deep = true;

    private static int listLimit = Integer.MAX_VALUE;

    private static int mapLimit = Integer.MAX_VALUE;

    private static boolean verbose = false;

    static {
        aaa.put(String.class, new PrintRoutine() {

            public void print(PrintStream out, Object obj) {

                out.print(" \"");
                out.print(obj);
                out.print("\"");
            }
        });
        aaa.put(Long.class, new PrintRoutine() {

            public void print(PrintStream out, Object obj) {

                out.print(" ");
                out.print(((Long) obj).longValue());
            }
        });
        aaa.put(Short.class, new PrintRoutine() {

            public void print(PrintStream out, Object obj) {

                out.print(" ");
                out.print(((Short) obj).shortValue());
            }
        });
        aaa.put(Integer.class, new PrintRoutine() {

            public void print(PrintStream out, Object obj) {

                out.print(" ");
                out.print(((Integer) obj).intValue());
            }
        });
        aaa.put(Boolean.class, new PrintRoutine() {

            public void print(PrintStream out, Object obj) {

                out.print(" ");
                out.print(((Boolean) obj).booleanValue() ? "t" : "nil");
            }
        });
        aaa.put(Count.class, new PrintRoutine() {

            public void print(PrintStream out, Object obj) {

                out.print(" ");
                out.print(obj.toString());
            }
        });
        aaa.put(Glue.class, new PrintRoutine() {

            public void print(PrintStream out, Object obj) {

                out.print(" ");
                out.print(obj.toString());
            }
        });
        aaa.put(Dimen.class, new PrintRoutine() {

            public void print(PrintStream out, Object obj) {

                out.print(" ");
                out.print(obj.toString());
            }
        });
        aaa.put(UnicodeChar.class, new PrintRoutine() {

            public void print(PrintStream out, Object obj) {

                out.print(" '");
                UnicodeChar uc = ((UnicodeChar) obj);
                out.print(obj.toString());
                out.print("'");
            }
        });
        aaa.put(Character.class, new PrintRoutine() {

            public void print(PrintStream out, Object obj) {

                out.print(" '");
                Character uc = ((Character) obj);
                out.print(obj.toString());
                out.print("'");
            }
        });

    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param args ...
     */
    public static void main(final String[] args) {

        PrintStream err = System.err;
        int i = 0;

        try {
            for (String a = args[i]; a.startsWith("-"); a = args[++i]) {
                if ("-help".startsWith(a)) {
                    // TODO
                } else if ("-listlimit".startsWith(a)) {
                    if (++i >= args.length) {
                        err.println("*** Missing argument");
                        System.exit(-1);
                    }
                    listLimit = Integer.parseInt(args[i]);

                } else if ("-maplimit".startsWith(a)) {
                    if (++i >= args.length) {
                        err.println("*** Missing argument");
                        System.exit(-1);
                    }
                    mapLimit = Integer.parseInt(args[i]);

                } else if ("-narrow".startsWith(a)) {
                    deep = false;

                } else if ("-verbose".startsWith(a)) {
                    verbose = true;

                } else if ("-quiet".startsWith(a)) {
                    verbose = false;

                } else {
                    break;
                }
            }
        } catch (NumberFormatException e) {
            err.println("*** Non-numeric argument: " + args[i]);
            System.exit(-1);
        }

        if (i >= args.length) {
            err.println("*** Missing format file argument");
            System.exit(-1);
        }

        String file = args[i];
        File fmt = new File(file);

        if (!fmt.exists()) {
            err.println("*** Format file does not exist: " + file);
            System.exit(-1);
        } else if (!fmt.canRead()) {
            err.println("*** Format file is not readable: " + file);
            System.exit(-1);
        }

        try {
            Object obj = new SerialLoader().load(new FileInputStream(fmt));
            print(System.out, "\n", obj);

        } catch (LoaderException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param out ...
     * @param prefix ...
     * @param obj ...
     */
    private static void print(final PrintStream out, final String prefix,
            final Object obj) {

        if (obj == null) {
            out.print(" nil");
            return;
        }
        String pre = prefix + "  ";
        String pre2 = prefix + "    ";
        Class c = obj.getClass();
        if (c.isArray()) {
            int len = Array.getLength(obj);
            out.print(" [");
            if (len > 0) {
                print(out, pre, Array.get(obj, 0));
                for (int i = 1; i < len; i++) {
                    out.print(pre2);
                    print(out, pre, Array.get(obj, i));
                }
            }
            out.print("]");
            // TODO
            return;
        } else if (obj instanceof Map) {
            Map m = (Map) obj;
            int size = m.size();
            if (size == 0) {
                out.print(" (map)");
            } else {
                out.print(" (map ");
                int limit = mapLimit;
                Iterator iterator = m.keySet().iterator();
                while (limit-- > 0 && iterator.hasNext()) {
                    Object key = iterator.next();
                    out.print(pre);
                    print(out, pre2, key);
                    out.print(" ");
                    print(out, pre2, m.get(key));
                }
                if (limit <= 0) {
                    out.print(pre);
                    out.print("; ");
                    out.print(mapLimit);
                    out.print(" of ");
                    out.print(size);
                }
                out.print(")");
            }
            return;
        } else if (obj instanceof List) {
            List l = (List) obj;
            int size = l.size();
            if (size == 0) {
                out.print(" (list)");
            } else {
                out.print(pre);
                out.print(" (list ");
                int limit = listLimit;
                Iterator iterator = l.iterator();
                while (limit-- > 0 && iterator.hasNext()) {
                    print(out, pre2, iterator.next());
                }
                if (limit <= 0) {
                    out.print(pre);
                    out.print("; ");
                    out.print(listLimit);
                    out.print(" of ");
                    out.print(size);
                }
                out.print(")");
            }
            return;
            //        } else if (obj instanceof Array) {
            //            Array a = ((Array) obj);
        }

        PrintRoutine pr = (PrintRoutine) aaa.get(c);
        if (pr != null) {
            pr.print(out, obj);
            return;
        }

        out.print(prefix);
        out.print("(");
        out.print(c.getName());

        boolean indirect = false;

        do {
            Field[] fields = c.getDeclaredFields();
            if (fields.length > 0) {
                if (indirect && verbose) {
                    out.print(prefix);
                    out.print("; from ");
                    out.print(c.getName());
                }

                for (int i = 0; i < fields.length; i++) {
                    Field f = fields[i];
                    if ((f.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) != 0) {
                        continue;
                    }
                    f.setAccessible(true);

                    try {
                        out.print(pre);
                        out.print("'");
                        out.print(f.getName());
                        print(out, pre2, f.get(obj));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            c = c.getSuperclass();
            indirect = true;
        } while (deep && c != null);

        out.print(prefix);
        out.print(")");
    }
}
