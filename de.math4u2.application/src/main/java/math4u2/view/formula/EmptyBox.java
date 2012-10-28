package math4u2.view.formula;

import java.awt.Dimension;

/**
 * Eine leere Box, die lediglich zu Platzhalterzwecken dient.
 * @author Erich Seifert
 * @version $Revision: 1.3 $
 */
public class EmptyBox extends AtomicBox {
	private final float muWidth;
	private final float muHeight;
	private Dimension dim;

	public EmptyBox(FormulaRenderContext frc) {
		this(frc, 0f, 0f);
	}
	
	public EmptyBox(FormulaRenderContext frc, float width, float height) {
		super(frc);
		muWidth = width;
		muHeight = height;
		rebuild();
	}

	protected void rebuild() {
		super.rebuild();

		int w = Math.round(muWidth * FormulaRenderContext.MU * getDisplayHeight());
		int h = Math.round(muHeight * FormulaRenderContext.MU * getDisplayHeight());

		dim = new Dimension(w, h);
		setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
	}

}
