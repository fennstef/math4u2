package math4u2.mathematics.results;

import java.lang.reflect.Method;
import java.util.List;

import math4u2.controller.reference.AbstractPathStep;
import math4u2.controller.reference.CreatesPath;
import math4u2.controller.reference.MethodContext;
import math4u2.controller.reference.PathStep;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeDoubleResult;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.ScalarType;
import math4u2.util.exception.ExceptionManager;

public class MatrixDoubleResult extends DoubleResult implements CreatesPath {

	public final double[][] valueArray;

	public final int rowDim, colDim;

	/*
	public MatrixDoubleResult(int rowDim, int colDim, double[][] valueArray) {
		this.valueArray = valueArray;
		this.rowDim = rowDim;
		this.colDim = colDim;
	}//Konstruktor */
	
	public MatrixDoubleResult(double[][] valueArray) {
		this.valueArray = valueArray;
		this.rowDim = valueArray.length;
		this.colDim = valueArray[0].length;
	}//Konstruktor

	public double evalScalar() throws MathException {
		throw new MathException("Ergebnis ist kein Skalar");
	}//evalScalar

	private String rowToString(int rowIndex) {
		String result = Double.toString(valueArray[rowIndex][0]);
		for (int i = 1; i < colDim; i++) {
			result += " , " + Double.toString(valueArray[rowIndex][i]);
		}
		return result;
	}//rowToString

	public String toString() {
		String result = rowToString(0);
		for (int i = 1; i < rowDim; i++) {
			result += "\n" + rowToString(i);
		}//for i
		return result;
	}//toString

	//--------------------------------------

	public Object operator_index(Object[] args) throws MathException {
		int row = (int) ((ScalarDoubleResult) args[0]).value;
		int col = (int) ((ScalarDoubleResult) args[1]).value;

		if (row - 1 >= valueArray.length)
			throw new MathException("Zeilenindex (" + (row)
					+ ") größer als Zeilenzahl (" + valueArray.length + ")");
		if (row < 1)
			throw new MathException("Zeilenindex kleiner eins (" + row + ")");
		if (col - 1 >= valueArray[row - 1].length)
			throw new MathException("Spaltenindex (" + (col)
					+ ") größer als Spaltenzahl (" + valueArray[row].length
					+ ")");
		if (col < 1)
			throw new MathException("Spaltenindex kleiner eins (" + col + ")");

		return new ScalarDoubleResult(valueArray[row - 1][col - 1]);
	}//operator_index

	public Class returnType_index(MethodContext mc) {
		return ScalarType.class;
	}//class returnType_index

	//-------------------------------------------		
	
	public Object operator_zeilen(Object[] value) throws MathException {
		return new ScalarDoubleResult(valueArray.length);
	} //operator_zeilen

	public Class returnType_zeilen(MethodContext mc) {
		return ScalarType.class;
	} //class returnType_zeilen
	
	//-------------------------------------------		
	
	public Object operator_spalten(Object[] value) throws MathException {
		return new ScalarDoubleResult(valueArray[0].length);
	} //operator_spalten

	public Class returnType_spalten(MethodContext mc) {
		return ScalarType.class;
	} //class returnType_spalten

	//-------------------------------------------

	public PathStep createPathStep(List methods) {
		if (methods.isEmpty())
			return null;

		MethodContext mc = (MethodContext) methods.get(0);
		methods.remove(0);
		try {
			Method m = this.getClass().getMethod(
					"operator_" + mc.getMethodName(),
					new Class[] { Object[].class });

			if (!methods.isEmpty()) {
				MethodContext mc2 = (MethodContext) methods.get(0);
				if ("eval".equals(mc2.getMethodName())) {
					methods.remove(0);
				}//if
			}//if
			
			// es ist noch nicht der nächste Schritt eingtragen worden
			PathStep ps = AbstractPathStep.createStep(this, mc.getMethodName(),m,mc.getArgs());

			//falls keine weiteren Methoden folgen, aufhören
			if (methods.isEmpty())
				return ps;

			CreatesPath nextObj = (CreatesPath) ps.getObjectFromMethod(this);

			PathStep nextStep = nextObj.createPathStep(methods);
			ps.setNextStep(nextStep);

			return ps;
		} catch (Throwable e) {
			throw new RuntimeException("Fehler beim Erzeugen des Aufrufs ("+methods+").",e);
		}//catch
	}//createPathStep

	public Class getReturnType(PathStep nextStep) {
		MethodContext mc = nextStep.getMethodContext();
		return getReturnType(mc);
	} //getReturnType

	public Class getReturnType(MethodContext mc) {
		try {
			Method m = this.getClass().getMethod(
					"returnType_" + mc.getMethodName(),
					new Class[] { MethodContext.class });
			return (Class) m.invoke(this, new Object[] { mc });
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Erzeugen des Aufrufs.",e);
			throw new RuntimeException(e);
		}//catch
	}//getReturnType

	public MatrixDoubleResult getNullResultMatrix() {
		double[][] nullValueArray = new double[rowDim][colDim];
		for (int r = 0; r < rowDim; r++)
			for (int c = 0; c < colDim; c++)
				nullValueArray[r][c] = 0.0;
		return new MatrixDoubleResult(nullValueArray);
	}
	
	public Class getDataType() {
		return MatrixType.class;
	}
	
	public  String getTypeString() {
		return "matrix";
	}//getTypeString
	
	public TermNode getNullTerm() {
		return new TermNodeDoubleResult(getNullResultMatrix());
	}
	
	public double[][] getMatrix() throws MathException {
		return valueArray;
	}

	public double getScalar() throws MathException {
		throw new MathException("Matrix kann nicht in Skalar konvertiert werden.");
	}

	public double[] getVector() throws MathException {
		throw new MathException("Matrix kann nicht in Vektor konvertiert werden.");
	}

}//class MatrixDoubleResult
