/*
 * Created on 15.12.2003 by gene
 *
 */
package de.dante.extex.interpreter.primitives.register;

import junit.framework.TestCase;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.InterpreterFactory;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.scanner.stream.impl.TokenStreamImpl;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationFactory;

/**
 * 
 * @author gene
 * @version $Revision: 1.4 $
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
        source.addStream(new TokenStreamImpl("1234 sp","ISO-8859-1"));
        assertEquals("1234sp",new Dimen(source,null).toString());
    }

    public void testPt1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1.000pt","ISO-8859-1"));
        assertEquals("65536sp",new Dimen(source,null).toString());
    }
    
    public void testPt2() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1.5pt","ISO-8859-1"));
        assertEquals("98304sp",new Dimen(source,null).toString());
    }

    public void testPt3() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1.50pt","ISO-8859-1"));
        assertEquals("98304sp",new Dimen(source,null).toString());
    }

    public void testPt4() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1.33pt","ISO-8859-1"));
        assertEquals("87163sp",new Dimen(source,null).toString());
    }
   
    public void testPt5() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1.333pt","ISO-8859-1"));
        assertEquals("87359sp",new Dimen(source,null).toString());
    }
    
    public void testMm1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1mm","ISO-8859-1"));
        assertEquals("186467sp",new Dimen(source,null).toString());
    }
    
    public void testMm2() throws Exception {
        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1.33mm","ISO-8859-1"));
        assertEquals("248002sp",new Dimen(source,null).toString());
    }
    
    public void testCm1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1cm","ISO-8859-1"));
        assertEquals("1864679sp",new Dimen(source,null).toString());
    }
    
    public void testCm2() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1.33cm","ISO-8859-1"));
        assertEquals("2480027sp",new Dimen(source,null).toString());
    }
    
    public void testIn1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1in","ISO-8859-1"));
        assertEquals("4736286sp",new Dimen(source,null).toString());
    }

    public void testDd1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1dd","ISO-8859-1"));
        assertEquals("70124sp",new Dimen(source,null).toString());
    }

    public void testDd2() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1.25dd","ISO-8859-1"));
        assertEquals("87655sp",new Dimen(source,null).toString());
    }

    public void testPc1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1.25pc","ISO-8859-1"));
        assertEquals("983040sp",new Dimen(source,null).toString());
    }
    
    public void testPc2() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1.7pc","ISO-8859-1"));
        assertEquals("1336932sp",new Dimen(source,null).toString());
    }

    public void testBp1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("1.25bp","ISO-8859-1"));
        assertEquals("82227sp",new Dimen(source,null).toString());
    }

    public void testCc1() throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config.getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamImpl("7,777cc","ISO-8859-1"));
        assertEquals("6544254sp",new Dimen(source,null).toString());
    }

}
