package math4u2.view.graph.util;

public interface LineStyleConstants {
	/** Durchgehende Linie */
	static final int SOLID_STROKE = 0;

	/** Gestrichelte Linie */
	static final int DASH_STROKE = 1;

	/** Gepunktete Linie */
	static final int DOT_STROKE = 2;

	/** Punkt-Strich Linie */
	static final int DOT_DASH_STROKE = 3;

	/** Array der Linestyles */
	static final String[] LINE_STYLES = new String[] { "solid", "dash", "dot",
			"dot-dash" };

}
