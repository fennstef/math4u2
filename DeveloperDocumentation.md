math4u2 - Developer Documentation
=======

math4u2 consists of different modules. 

![image](https://github.com/fennstef/math4u2/blob/master/doc/images/math4u2Modules.jpg?raw=true)

- **de.math4u2.core** Common utility classes and the exception handling mechanism.
- **de.math4u2.drawarea** The implementation of the drawarea and a rich set of graphs. The drawarea and graphs are independent from the mathematic implementation (See the test cases).
- **de.math4u2.mathematics** Implementation of the parser and all the matematical stuff
- **de.math4u2.application** This module uses all other modules and builds the math4u2-system
- **de.math4u2.build-project** Maven module to build the complete math4u2-system

Using the mathematics module
=======

You can define and evaluate functions with the mathematics module in every java program (Read the Test cases in this module).

At startup time you must create a broker which is a math object container. The broker handles the relations and fires updates to the responsible elements. Also a ExceptionManager must be instaniated. For a console application the SystemErrorExceptionFrame can be used. 

    ExceptionManager.initExceptionFrame(new SystemErrorExceptionFrame());
    broker = new Broker();
    
Also the function and the parser must be initialized. Here the EmptyViewFactory can be used for every application which need no visual representants.

    EmptyViewFactory evf = new EmptyViewFactory();
    Function.setViewFactory(evf);
    parser.init(broker, evf);
    
To parse a function you can use the following method:

    protected UserFunction parseUserFunction(String def) throws Exception{
      String key = parser.NEWParseDefinition(def, true, broker);
    	MathObject mo = broker.getMathObject(key);
    	UserFunction func = (UserFunction) mo;
    	broker.publishObject(func, func.getKey());
    	return func;
    }
    
Now everything is set up and you can run some examples.
    
**Examples**

    public void testSum() throws Exception{
      UserFunction a = parseUserFunction("a(x):=sum(i,0,20,x^i/i!)");
      UserFunction b = parseUserFunction("b := a(1)");
      assertEquals(Math.exp(1),b.evalScalar(), TOL);
    }
  
    public void testLinearEquation() throws Exception{
        UserFunction a = parseUserFunction("a:=matrix({ {6, 12}, {3, 3} })");
        UserFunction b = parseUserFunction("b := vektor({90,9})");
        UserFunction c = parseUserFunction("c := solvelin(a,b)");
        VectorDoubleResult vdr = (VectorDoubleResult) c.eval();
        assertEquals(-9,vdr.valueArray[0][0], TOL);
        assertEquals(12,vdr.valueArray[1][0], TOL);
    }
