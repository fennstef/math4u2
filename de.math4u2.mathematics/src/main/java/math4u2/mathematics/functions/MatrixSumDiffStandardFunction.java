package math4u2.mathematics.functions;

import math4u2.mathematics.types.DualVectorType;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.VectorType;

public abstract class MatrixSumDiffStandardFunction extends
		MatrixInfixStandardFunction {

	public Class getResultType(Class[] argTypes) {

		if (VectorType.class.isAssignableFrom(argTypes[0])
				&& VectorType.class.isAssignableFrom(argTypes[1]))
			return argTypes[0];

		if (DualVectorType.class.isAssignableFrom(argTypes[0])
				&& DualVectorType.class.isAssignableFrom(argTypes[1]))
			return argTypes[0];

		// else
		return MatrixType.class;
	}

}