package math4u2.mathematics.functions;

import math4u2.controller.Broker;
import math4u2.mathematics.results.*;
import math4u2.mathematics.termnodes.*;
import math4u2.mathematics.types.VectorType;

/**
 * <p>
 * Konstante Vektoren
 * </p>
 * t
 * 
 * @version 1.0
 */
public abstract class StandardVector extends StandardFunction {

	public Class getResultType(Class[] argTypes) {
		return VectorType.class;
	}

	/**
	 * Haelt den Wert des konstanten Vektors. Muss vom Konstruktor der abgeleiteten
	 * Klassen initialisiert werden.
	 */
	
	protected TermNode [] zeroNodeVector;
    protected double [][] value;

	public StandardVector(double [] valueVector) {
		value = new double[valueVector.length][1];
		zeroNodeVector = new TermNode[valueVector.length];
		for ( int row = 0; row < valueVector.length; row++ )
		{
			value[row][0] = valueVector[row];
			zeroNodeVector[row] = new TermNodeNum(0.0);
		}
	}

	public Object eval(Object[] v) {
		return new VectorDoubleResult(value);
	}

	/**
	 * Die Ableitung eines konstanten Vektors ist der Nullvektor der entsprechenden Dimension.
	 * 
	 * @param argumentTerms
	 * @param derivedArgumentTerms
	 * @param broker
	 * @return 0-Konten.
	 */
	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker) throws MathException {

		return new TermNodeVector(zeroNodeVector);
	}

	/**
	 * Stelligkeit ist 0.
	 * 
	 * @return 0
	 */
	public int getArity() {
		return 0;
	}

	public Class getVariableType(int pos) throws MathException {
		throw new MathException("Funktion " + name
				+ " hat keine Variable an der Stelle " + pos);
	}

	/**
	 * Konstante Vektoren sind nullstellige Funktionen. Für die Variablennamen wir
	 * deshalb ein Array der Länge 0 zurückgegeben.
	 * 
	 * @return Array der Laenge 0.
	 */
	public String[] getVariableNames() {
		return new String[] {};
	}

	/**
	 * Der Term-String besteht aus dem Namen des konstanen Vektors.
	 * 
	 * @param terms
	 *            Wird in diesem Fall (nullstellige Funktion) nicht verwendet.
	 * @return Name des Parameters.
	 */
	public String buildTermString(String[] terms) {
		return (String) getIdentifier();
	}
}