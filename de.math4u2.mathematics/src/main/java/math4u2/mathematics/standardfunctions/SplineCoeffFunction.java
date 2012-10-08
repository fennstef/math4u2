package math4u2.mathematics.standardfunctions;

import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscBinaryStandardFunction;
import math4u2.mathematics.numerics.LinearAlgorithms;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.VectorType;

public class SplineCoeffFunction extends MiscBinaryStandardFunction {

    public SplineCoeffFunction() {
		name = "splinecoeff";
		summaryText = "Berechnet die Koeffizienten einer kubischen Spline-Interpolation";
	}

	public Class getResultType(Class[] argTypes) {
		return MatrixType.class;
	} //getResultType

	public Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
			return VectorType.class;
		case 1:
			return VectorType.class;
		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		} //switch
	} //getVariableType
    
    
	public Object eval(Object[] args) throws MathException {
		
		System.out.println("splinecoeff");
		VectorDoubleResult xVec = (VectorDoubleResult) (args[0]);
		VectorDoubleResult yVec = (VectorDoubleResult) (args[1]);
		
		
		if(xVec.rowDim != yVec.rowDim) throw new MathException("Anzahl x-Werte nicht gleich Anzahl y-Werte");
		
		if(xVec.rowDim < 2) throw new MathException("Weniger als 2 Stuetzstellen");
		
		//System.out.println("spline2");
		
		double [][] xArray = xVec.valueArray;
		double [][] yArray = yVec.valueArray;
		
		int dim = xVec.rowDim - 1;
		
		/*
		System.out.println("spline dim " + dim);
		for( int i = 0; i<=dim; i++){
			System.out.println(xArray[i][0]+"   " + yArray[i][0]);
		}*/
		
		double [] h = new double[dim];	
		for( int i = 0; i < dim; i++)
			h[i]=xArray[i+1][0]-xArray[i][0];

		// Koeffizientenmatrix erstellen
		
		double [][] koeff = new double[dim-1][dim-1];
		
		koeff[0][0] = 2.0*(h[0]+h[1]);
		koeff[1][0] = h[1];
		
		for(int i = 1; i<dim-2; i++){
			koeff[i-1][i] = h[i];
			koeff[i][i] = 2*(h[i]+h[i+1]);
			koeff[i+1][i] = h[i+1];
		}
		
		koeff[dim-2][dim-2] = 2*(h[dim-2]+h[dim-1]);
		koeff[dim-3][dim-2] = h[dim-2];
		
		double [] delta = new double[dim];	
		for( int i = 0; i < dim; i++)
			delta[i]=(yArray[i+1][0]-yArray[i][0])/h[i];
		
		// Inhomogenität erstellen
		double [][] inhom = new double[dim-1][1];
		for( int i = 0; i < dim-1; i++ ){
			inhom[i][0] = 3.0*(delta[i+1]-delta[i]);
		}
		
		//System.out.println("spline3");
		
		// Gleichungssystem loesen
		double [][] cc = LinearAlgorithms.solveLinearEquation(dim-1, koeff, inhom);
		
		double [] c = new double[dim+1];
		c[0] = 0;
		c[dim] = 0;
		for( int i = 1; i < dim; i++ ){
			c[i] = cc[i-1][0];
		}
			
		
		double[][]  polyCoeff = new double[dim+1][5];
		for( int i = 0; i < dim; i++){
			polyCoeff[i][0] = yArray[i][0];
			polyCoeff[i][1] = delta[i]-h[i]/3.0*(2*c[i]+c[i+1]);
			polyCoeff[i][2] = c[i];
			polyCoeff[i][3] = (c[i+1]-c[i])/3.0/h[i];	
			polyCoeff[i][4] = xArray[i][0];
		}
		polyCoeff[dim][4] = xArray[dim][0];
		
		
		
		
		
		
		return new MatrixDoubleResult(polyCoeff);
		

	} //eval
	
	public String[] getArgumentStrings() {
		return new String[] {"x-Werte", "y-Werte"};
	}
	
	public String[] getArgumentTexts() {
		return new String[] {"Vektor, der die x-Werte <br>der zu interpolierenden Punkte enthält.", 
				"Vektor, der die y-Werte <br>der zu interpolierenden Punkte enthält."};
	}
	
	
	
	

} //class SplineFunction
