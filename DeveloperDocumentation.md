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


Using the drawarea module
=======

To create a drawarea you can often use the following method

    private DrawArea createDrawArea() {
		final DrawArea da = new DrawArea("test");
		da.addChangeListener(new DrawAreaChangeListener() {
			public void drawAreaChanged() {
				da.graphHasChanged();
			}
		});
		return da;
	}

Here the drawarea "test" is created and an eventlistener for change events is implemented.

To create a function graph you must implement the interface 

    public interface IFunction1<R,P1> {
        R eval(P1 p1) throws Exception;
    	String getKey();
    }
        
Where the type parameter R is the return class and P1 is the first parameter. 
For a function f:IR -> IR the class must be IScalarDoubleHolder with the following methods.

    public interface IScalarDoubleHolder extends ICanBeFixed{
        double getScalar() throws Exception;
    	void setScalar(double value, boolean propagateChange) throws Exception;
    	double getScalarOrNan();
    }
    
    public interface ICanBeFixed {
        boolean isFixed();
    	void setFixed(boolean fixed);
    }

The method getScalarOrNan() throws no exceptions but returns Double.NAN. There are a number of implementations
for the value holders. For the IScalarDoubleHolder there exists the implmentation SimpleScalarDoubleValueHolder.

With this information we can create the function graph.

    public void testFunctionGraph() throws InterruptedException {
        JFrame frame = createFrame();
        final DrawArea da = createDrawArea();
        frame.getContentPane().add(da);
        IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> func = new IFunction1<IScalarDoubleHolder, IScalarDoubleHolder>() {
        	public IScalarDoubleHolder eval(IScalarDoubleHolder p1)
        			throws Exception {
        		double d = Math.sin(p1.getScalarOrNan());
        		return new SimpleScalarDoubleValueHolder(d);
        	}
        
        	public String getKey() {
        		return "f";
        	}
        };
        
        FunctionGraph graph = new FunctionGraph(da, new DefaultGraphSettings(), func);
        graph.setColor(Color.blue);
        da.addGraph(graph);
        showFrame(frame);
    }
    
![image](https://github.com/fennstef/math4u2/blob/master/doc/images/drawarea01.jpg?raw=true)
