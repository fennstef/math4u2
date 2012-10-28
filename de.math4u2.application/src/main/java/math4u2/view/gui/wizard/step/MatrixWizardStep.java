package math4u2.view.gui.wizard.step;

import math4u2.view.gui.wizard.components.WizMatrix;
import math4u2.view.gui.wizard.title.HasTitle;
import math4u2.view.gui.wizard.types.WizParamType;

public class MatrixWizardStep extends StandardWizardStep {
	
	private int rows, cols;

	public MatrixWizardStep(String fullname, HasTitle previousTitle, String name) {
		super(fullname, previousTitle, name, WizParamType.MATRIX);
	}

	public MatrixWizardStep(String fullname, HasTitle previousTitle, String name,
			String description) {
		super(fullname, previousTitle, name, description, WizParamType.MATRIX);
	}

	public void prepare() {
		ParamEntry param = (ParamEntry) params.get(0);
		WizMatrix matrix = (WizMatrix) param.comp;
		
		rows = Integer.parseInt(model.get("rows"));
		cols = Integer.parseInt(model.get("cols"));
		matrix.setMatrixDimension(cols, rows);
		
		matrix.addChangeListener(changeListener);
		
		super.prepare();
	}//prepare
	
	protected void refreshResult() {
		if(rows+cols>10){
			resultLabel.setText("matrix(...)");
		}else{
			resultLabel.setText(_refreshResult(true));
		}
		output = _refreshResult(false);
		refreshErrorHighlights();
	}//refreshResult
}//class MatrixWizardStep
