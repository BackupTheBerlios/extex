/*
 * Created on 15.12.2003 by gene
 *
 */
package de.dante.extex.interpreter.primitives.register;

import junit.framework.TestCase;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.InterpreterFactory;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.scanner.stream.impl.TokenStreamStringImpl;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationFactory;

/**
 * 
 * @author gene
 * @version $Revision: 1.6 $
 */
public class DimenRegisterTest extends TestCase {

    /**
     */
    public DimenRegisterTest(String arg0) {
        super(arg0);
    }

    /**
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(DimenRegisterTest.class);
    }
    
    // TODO chage InterpreterFactory
    
    public void testSp1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1234 sp"));// mgn: changed
        assertEquals("1234sp",new Dimen(null,source).toString());
    }

    public void testPt1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.000pt"));// mgn: changed
        assertEquals("65536sp",new Dimen(null,source).toString());
    }
    
    public void testPt2() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.5pt"));// mgn: changed
        assertEquals("98304sp",new Dimen(null,source).toString());
    }

    public void testPt3() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.50pt"));// mgn: changed
        assertEquals("98304sp",new Dimen(null,source).toString());
    }

    public void testPt4() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.33pt"));// mgn: changed
        assertEquals("87163sp",new Dimen(null,source).toString());
    }
   
    public void testPt5() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.333pt"));
        assertEquals("87359sp",new Dimen(null,source).toString());
    }
    
    public void testMm1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1mm"));
        assertEquals("186467sp",new Dimen(null,source).toString());
    }
    
    public void testMm2() throws Exception {
        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.33mm"));
        assertEquals("248002sp",new Dimen(null,source).toString());
    }
    
    public void testCm1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1cm"));
        assertEquals("1864679sp",new Dimen(null,source).toString());
    }
    
    public void testCm2() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.33cm"));
        assertEquals("2480027sp",new Dimen(null,source).toString());
    }
    
    public void testIn1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1in"));
        assertEquals("4736286sp",new Dimen(null,source).toString());
    }

    public void testDd1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1dd"));
        assertEquals("70124sp",new Dimen(null,source).toString());
    }

    public void testDd2() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.25dd"));
        assertEquals("87655sp",new Dimen(null,source).toString());
    }

    public void testPc1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.25pc"));
        assertEquals("983040sp",new Dimen(null,source).toString());
    }
    
    public void testPc2() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.7pc"));
        assertEquals("1336932sp",new Dimen(null,source).toString());
    }

    public void testBp1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.25bp"));
        assertEquals("82227sp",new Dimen(null,source).toString());
    }

    public void testCc1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("7,777cc"));
        assertEquals("6544254sp",new Dimen(null,source).toString());
    }

}
