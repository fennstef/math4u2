package de.math4u2.mathematics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.parser.parser;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.IExceptionFrame;
import math4u2.util.exception.parser.ParseException;
import math4u2.view.graph.GraphInterfaceFactory;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unused")
public class TestSimpleMathExpressions extends AbstractMathTest{
	@Test
	public void testSmokeTest() throws Exception{
		UserFunction a = parseUserFunction("a:=1/2");
		assertEquals(0.5,a.evalScalar(), TOL);
	}
	
	@Test 
	public void testVektorDef01() throws Exception{
		UserFunction a = parseUserFunction("a:=vektor({3,4,5})");
		UserFunction b = parseUserFunction("b:=a.x+a.y+a.z");
		assertEquals(12,b.evalScalar(), TOL);
	}
	
	@Test 
	public void testVektorDef02() throws Exception{
		UserFunction a = parseUserFunction("a:=vektor(i,3,i+2)");
		UserFunction b = parseUserFunction("b:=a.x+a.y+a.z");
		assertEquals(12,b.evalScalar(), TOL);
	}
	
	@Test 
	public void testVektorDef03() throws Exception{
		UserFunction a = parseUserFunction("a:=vektor(i,3,i+2)");
		UserFunction b = parseUserFunction("b:=a[3]");
		assertEquals(5,b.evalScalar(), TOL);
	}
	
	@Test 
	public void testVektorDef04() throws Exception{
		UserFunction a = parseUserFunction("a:=vektor(i,3,i+2)");
		UserFunction b = parseUserFunction("b:=a.dimension");
		assertEquals(3,b.evalScalar(), TOL);
	}
	
	@Test
	public void testVektorFunctions01() throws Exception{
		UserFunction a = parseUserFunction("a:=vektor({1,1})");
		UserFunction b = parseUserFunction("b:=|a|-sqrt(2)");
		assertEquals(0,b.evalScalar(), 1e-20);
	}
	
	@Test
	public void testVektorFunctions02() throws Exception{
		UserFunction a = parseUserFunction("a:=vektor({1,1})*2");
		UserFunction b = parseUserFunction("b:=a.x+a.y");
		assertEquals(4,b.evalScalar(), TOL);
	}
	
	@Test
	public void testVektorFunctions03() throws Exception{
		UserFunction a = parseUserFunction("a:=vektor({1,-2})");
		UserFunction b = parseUserFunction("b:=vektor({1,2})");
		UserFunction c = parseUserFunction("c:=a<*>b");
		assertEquals(-3,c.evalScalar(), TOL);
	}
	
	@Test
	public void testVektorFunctions04() throws Exception{
		UserFunction a = parseUserFunction("a(x):=vektor(i,3,x^(i-1))");
		UserFunction b = parseUserFunction("b:=a(2).x+a(2).y+a(2).z");
		assertEquals(7,b.evalScalar(), TOL);
	}
	
	@Test
	public void testVektorFunctions05() throws Exception{
		UserFunction a = parseUserFunction("a(x):=vektor({0,0,x-1})");
		UserFunction b = parseUserFunction("b(x):=a(x+1)[3]");
		UserFunction c = parseUserFunction("c:=b(3)");
		assertEquals(3,c.evalScalar(), TOL);
	}
	
	@Test
	public void testMatrixFunctions01() throws Exception{
		UserFunction a = parseUserFunction("a:=1");
		UserFunction b = parseUserFunction("b := matrix({ {a*1, a*2, a*3 }, {a*4, a*5, a*6}, {a*7, a*8, a*9} })");		
		UserFunction c = parseUserFunction("c:=b[2,1]");
		assertEquals(4,c.evalScalar(), TOL);
	}
	
	@Test
	public void testMatrixFunctions02() throws Exception{
		UserFunction a = parseUserFunction("a:=matrix({ {2, 2}, {2, 2} })");
		UserFunction b = parseUserFunction("b := |a|-sqrt(4*2^2)");				
		assertEquals(0,b.evalScalar(), TOL);
	}
	
	@Test
	public void testMatrixFunctions03() throws Exception{
		UserFunction a = parseUserFunction("a:=matrix({ {1, 3}, {1, 1} })");
		UserFunction b = parseUserFunction("b := det(a)");				
		assertEquals(-2,b.evalScalar(), TOL);
	}
	
	@Test
	public void testMatrixFunctions04() throws Exception{
		UserFunction a = parseUserFunction("a:=matrix({ {1, 3}, {1, 1} })");
		UserFunction b = parseUserFunction("b := inverse(a)");
		UserFunction c = parseUserFunction("c := det(b)");		
		assertEquals(-0.5,c.evalScalar(), TOL);
	}	
	
	@Test
	public void testMatrixFunctions05() throws Exception{
		UserFunction a = parseUserFunction("a:=matrix({ {6, 12}, {3, 3} })");
		UserFunction b = parseUserFunction("b := vektor({90,9})");
		UserFunction c = parseUserFunction("c := solvelin(a,b)");
		UserFunction d = parseUserFunction("d := c[1]");
		UserFunction f = parseUserFunction("f := c[2]");
		assertEquals(-9,d.evalScalar(), TOL);
		assertEquals(12,f.evalScalar(), TOL);
	}
	
	@Test
	public void testListDef01() throws Exception{
		UserFunction a = parseUserFunction("a:=<funktion>liste({1, 2, 3})");
		UserFunction b = parseUserFunction("b := a[1]");
		assertEquals(1,b.evalScalar(), TOL);
	}	
	
	
	@Test
	public void testListDef02() throws Exception{
		UserFunction a = parseUserFunction("a:=<funktion>liste0({1, 2, 3})");
		UserFunction b = parseUserFunction("b := a[1]");
		assertEquals(2,b.evalScalar(), TOL);
	}
	
	@Test
	public void testListDef03() throws Exception{
		UserFunction a = parseUserFunction("a:=<funktion(x)>liste({sin(x), cos(x), x^2})");
		UserFunction b = parseUserFunction("b := a[1](0.8)^2+a[2](0.8)^2");
		assertEquals(1,b.evalScalar(), TOL);
	}
	
	@Test
	public void testListDef04() throws Exception{
		UserFunction a = parseUserFunction("a:=<funktion(x)>liste(i,10,x^i/i!)");
		UserFunction b = parseUserFunction("b := a[1](1)+a[2](1)");
		assertEquals(1.5,b.evalScalar(), TOL);
	}
	
	@Test
	public void testFolgeDef01() throws Exception{
		UserFunction fib = parseUserFunction("fib:=folge( i, {1,1}, fib[i-2]+fib[i-1])");
		UserFunction b = parseUserFunction("b := fib[6]");
		assertEquals(8,b.evalScalar(), TOL);
	}

	@Test
	public void testSum01() throws Exception{
		UserFunction a = parseUserFunction("a(x):=sum(i,0,20,x^i/i!)");
		UserFunction b = parseUserFunction("b := a(1)");
		assertEquals(Math.exp(1),b.evalScalar(), TOL);
	}
	
	@Test
	public void testProd01() throws Exception{
		UserFunction a = parseUserFunction("a(n):=prod(i,1,n,i)");
		UserFunction b = parseUserFunction("b := a(5)");
		assertEquals(120,b.evalScalar(), TOL);
	}
}
