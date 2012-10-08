package math4u2.mathematics.types;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.termnodes.TermNode;

public class OperatorExpert {

	public static Function getFunctionForArgument(TermNode argument1,
			String operator, Broker broker)
	throws MathException {
		try {
			Class a1C = argument1.getResultType();
			if (operator.equals("-")) {
					if (ScalarType.class.isAssignableFrom(a1C)) 
							return (Function) (broker.getObject("-"));

					if (VectorType.class.isAssignableFrom(a1C))
							return (Function) (broker.getObject("VectorMinus"));

					if (DualVectorType.class.isAssignableFrom(a1C))
						   return (Function) (broker.getObject("DualVectorMinus"));

					if (MatrixType.class.isAssignableFrom(a1C)) 
							return (Function) (broker.getObject("MatrixMinus"));
			}
		}
        catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
        throw new MathException("Operator " + operator
				+ " fuer Argument vom Typ "
				+ argument1.getResultType().toString() + " nicht definiert");
	}
	
	
	public static Function getFunctionForArguments(TermNode argument1,
			TermNode argument2, String operator, Broker broker)
			throws MathException {
		Class a1C = argument1.getResultType();
		Class a2C = argument2.getResultType();
		try {						
			if (operator.equals("*")) {
				// erster Operand ist ein Skalar
				if (ScalarType.class.isAssignableFrom(a1C)) {

					if (ScalarType.class.isAssignableFrom(a2C)) {
						return (Function) (broker.getObject("prod"));
					}

					if (VectorType.class.isAssignableFrom(a2C)) {
						return (Function) (broker.getObject("ScalarVectorProd"));
					}

					if (DualVectorType.class.isAssignableFrom(a2C)) {
						return (Function) (broker
								.getObject("ScalarDualVectorProd"));
					}

					if (MatrixType.class.isAssignableFrom(a2C)) {
						return (Function) (broker.getObject("ScalarMatrixProd"));
					}
				}

				// zweiter Operand ist ein Skalar
				if (ScalarType.class.isAssignableFrom(a2C)) {

					if (VectorType.class.isAssignableFrom(a1C)) {
						return (Function) (broker.getObject("VectorScalarProd"));
					}

					if (DualVectorType.class.isAssignableFrom(a1C)) {
						return (Function) (broker
								.getObject("DualVectorScalarProd"));
					}

					if (MatrixType.class.isAssignableFrom(a1C)) {
						return (Function) (broker.getObject("MatrixScalarProd"));
					}
				}

				// Matrizen und Vektoren
				if (DualVectorType.class.isAssignableFrom(a1C)
						&& MatrixType.class.isAssignableFrom(a2C)) {
					return (Function) (broker.getObject("DualVectorMatrixProd"));
				}

				if (MatrixType.class.isAssignableFrom(a1C)
						&& VectorType.class.isAssignableFrom(a2C)) {
					return (Function) (broker.getObject("MatrixVectorProd"));
				}

				if (MatrixType.class.isAssignableFrom(a1C)
						&& MatrixType.class.isAssignableFrom(a2C)) {
					return (Function) (broker.getObject("MatrixProd"));
				}
			}

			if (operator.equals("/")) {
				if (ScalarType.class.isAssignableFrom(a2C)) {

					if (ScalarType.class.isAssignableFrom(a1C)) {
						return (Function) (broker.getObject("quot"));
					}

					if (VectorType.class.isAssignableFrom(a1C)) {
						return (Function) (broker.getObject("VectorScalarQuot"));
					}

					if (DualVectorType.class.isAssignableFrom(a1C)) {
						return (Function) (broker.getObject("DualVectorScalarQuot"));
					}

					if (MatrixType.class.isAssignableFrom(a1C)) {
						return (Function) (broker.getObject("MatrixScalarQuot"));
					}
				}
			}

			if (operator.equals("+")) {
				if (ScalarType.class
						.isAssignableFrom(a1C)
						&& ScalarType.class.isAssignableFrom(a2C)) {
					return (Function) (broker.getObject("sum"));
				}

				if (VectorType.class.isAssignableFrom(a1C)
						&& VectorType.class.isAssignableFrom(a2C)) {
					return (Function) (broker.getObject("VectorSum"));
				}

				if (DualVectorType.class.isAssignableFrom(a1C)
						&& DualVectorType.class.isAssignableFrom(a2C)) {
					return (Function) (broker.getObject("DualVectorSum"));
				}

				if (MatrixType.class.isAssignableFrom(a1C)
						&& MatrixType.class.isAssignableFrom(a2C)) {
					return (Function) (broker.getObject("MatrixSum"));
				}
			}

			if (operator.equals("-")) {
				if (ScalarType.class.isAssignableFrom(a1C)
						&& ScalarType.class.isAssignableFrom(a2C)) {
					return (Function) (broker.getObject("diff"));
				}

				if (VectorType.class.isAssignableFrom(a1C)
						&& VectorType.class.isAssignableFrom(a2C)) {
					return (Function) (broker.getObject("VectorDiff"));
				}

				if (DualVectorType.class.isAssignableFrom(a1C)
						&& DualVectorType.class.isAssignableFrom(a2C)) {
					return (Function) (broker.getObject("DualVectorDiff"));
				}

				if (MatrixType.class.isAssignableFrom(a1C)
						&& MatrixType.class.isAssignableFrom(a2C)) {
					return (Function) (broker.getObject("MatrixDiff"));
				}
			}

		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}

		throw new MathException("Operator " + operator
				+ " fuer Argumente vom Typ "
				+ a1C.toString() + " und "
				+ a2C.toString() + " nicht definiert");
	}
}