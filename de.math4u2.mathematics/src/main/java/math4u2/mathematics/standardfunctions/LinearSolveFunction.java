package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscBinaryStandardFunction;
import math4u2.mathematics.numerics.LinearAlgorithms;
import math4u2.mathematics.results.*;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.VectorType;

public class LinearSolveFunction extends MiscBinaryStandardFunction {

	public LinearSolveFunction() {
		name = "solvelin";
		summaryText = "Berechnet den Lösungsvektor x eines " +
				"<br>eindeutig lösbaren linearen Gleichungssystems A*x=b" +
				"<br>mit quadratischer Koeffizientenmatrix A.";
	}

	public Class getResultType(Class[] argTypes) {
		return VectorType.class;
	} //getResultType

	public Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
			return MatrixType.class;
		case 1:
			return VectorType.class;

		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		} //switch
	} //getVariableType

	public Object eval(Object[] args) throws MathException {
		MatrixDoubleResult koeff = (MatrixDoubleResult) (args[0]);
		MatrixDoubleResult inhom = (MatrixDoubleResult) (args[1]);

		if (koeff.colDim != koeff.rowDim)
			throw new MathException("Funktion " + name
					+ " nicht definiert bei Matrix vom Typ " + koeff.rowDim
					+ " x " + koeff.colDim);

		if (koeff.rowDim != inhom.rowDim)
			throw new MathException("Bei Funktion " + name
					+ " passt Spaltenzahl " + koeff.colDim
					+ " der Matrix nicht zur Zeilenzahl " + inhom.rowDim
					+ " des Vektors");

		if (1 != inhom.colDim)
			throw new MathException("Aufruf der Funktion " + name
					+ " mit einer Inhomogenitaet mit " + inhom.colDim
					+ " Spalten. Eine Spalte ist erlaubt.");

		double[][] koeffWork = new double[koeff.rowDim][koeff.colDim];
		double[][] inhomWork = new double[koeff.rowDim][1];

		for (int rowIndex = 0; rowIndex < koeff.rowDim; rowIndex++) {
			inhomWork[rowIndex][0] = inhom.valueArray[rowIndex][0];
			for (int colIndex = 0; colIndex < koeff.colDim; colIndex++) {
				koeffWork[rowIndex][colIndex] = koeff.valueArray[rowIndex][colIndex];
			}
		}
		return new VectorDoubleResult(LinearAlgorithms.solveLinearEquation(koeff.rowDim, koeffWork, inhomWork));
	} //eval

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		throw new MathException(
				"derive fuer lineare Gleichungssysteme nicht definiert");
	}

	public String[] getArgumentStrings() {
		return new String[] {"Koeffizientenmatrix A", "Inhomogenität b"};
	}
	
	public String[] getArgumentTexts() {
		return new String[] {"Die Koeffizientenmatrix muss regulär" +
				"<br>d.h. inverierbar sein.", null};
	}
	
	

	

} //LinearSolveFunction
