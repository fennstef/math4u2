package math4u2.exercises.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.geom.GeneralPath;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import math4u2.application.resource.Images;
import math4u2.exercises.EIssue;
import math4u2.exercises.Exercise;
import math4u2.exercises.Folder;
import math4u2.util.io.file.xml.filestatus.FileStatusInterface;

/**
 * Steuerung der Darstellung der Knoten und Blätter im JTree
 * 
 * @author Michael Lichtenstern
 */
public class ETreeCellRenderer extends DefaultTreeCellRenderer {

	private static Icon exerciseIcon;

	private static Icon issueIconLocal;

	private static Icon issueIconRemote;

	private static Icon issueIconTemp;

	/**
	 * Initalisiert den ETreeCellRenderer mit den Icons fuer eine lokale, remote
	 * und temp Issue.
	 *  
	 */
	public ETreeCellRenderer() {
		exerciseIcon = new EExerciseIcon(16, 20);
		issueIconLocal = new EIssueIcon(16, 20, Color.black);
		issueIconRemote = new EIssueIcon(16, 20, Color.blue);
		issueIconTemp = new EIssueIcon(16, 20, Color.gray);
	}

	/**
	 * Gibt einen konfigurierten ETreeCellRenderer zurueck.
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		ETreeNodeInfo nodeInfo = (ETreeNodeInfo) (node.getUserObject());
		if (nodeInfo != null) {
			Object obj = nodeInfo.getObject();
			if (obj instanceof Exercise) {
				setIcon(exerciseIcon);
				setFont(ESkin.styleContext.getFont(ESkin.styleContext
						.getStyle(StyleContext.DEFAULT_STYLE)));
			} else if (obj instanceof EIssue) {
				FileStatusInterface status = ((EIssue) obj).getStatus();
				status.showTreeIssue(this);
			} else if (obj instanceof Folder) {
				setIcon(Images.FOLDER);
				setFont(ESkin.styleContext.getFont(ESkin.styleContext
						.getStyle("strong")));
			} //else
		} //if
		return comp;
	} //getTreeCellRendererComponent

	/**
	 * Gibt ein Uebungs-Icon zurueck.
	 * 
	 * @return Uebungs-Icon
	 */
	public static Icon getExerciseIcon() {
		return exerciseIcon;
	}

	/**
	 * Gibt ein Local-Icon zurueck.
	 * 
	 * @return Local-Icon
	 */
	public static Icon getIssueIconLocal() {
		return issueIconLocal;
	}

	/**
	 * Gibt ein Remote-Icon zurueck.
	 * 
	 * @return Remote-Icon
	 */
	public static Icon getIssueIconRemote() {
		return issueIconRemote;
	}

	/**
	 * Gibt ein Temp-Icon zurueck.
	 * 
	 * @return Temp-Icon
	 */
	public static Icon getIssueIconTemp() {
		return issueIconTemp;
	}
}
/**
 * Hilfsklasse zur Speicherung von Elementen in Knoten
 */

/**
 * Hilfsklasse zum Erzeugen eines Icons für Übungen
 */

final class EExerciseIcon extends ResizableIcon {

	public EExerciseIcon(int width, int height) {
		super((GeneralPath[]) null, width, height);
		GeneralPath exerciseSymbol = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
				28);
		GeneralPath circleSymbol = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
				10);
		// Kreisumrisse innen und außen erzeugen
		circleSymbol.moveTo(0.50000f, 1.00000f);
		circleSymbol.curveTo(0.77538f, 1.00000f, 1.00000f, 0.77538f, 1.00000f,
				0.50000f);
		circleSymbol.curveTo(1.00000f, 0.22462f, 0.77538f, 0.00000f, 0.50000f,
				0.00000f);
		circleSymbol.curveTo(0.22462f, 0.00000f, 0.00000f, 0.22462f, 0.00000f,
				0.50000f);
		circleSymbol.curveTo(0.00000f, 0.77538f, 0.22462f, 1.00000f, 0.50000f,
				1.00000f);
		circleSymbol.closePath();
		circleSymbol.moveTo(0.50000f, 0.92500f);
		circleSymbol.curveTo(0.73408f, 0.92500f, 0.92500f, 0.73408f, 0.92500f,
				0.50000f);
		circleSymbol.curveTo(0.92500f, 0.26592f, 0.73408f, 0.07500f, 0.50000f,
				0.07500f);
		circleSymbol.curveTo(0.26592f, 0.07500f, 0.07500f, 0.26592f, 0.07500f,
				0.50000f);
		circleSymbol.curveTo(0.07500f, 0.73408f, 0.26592f, 0.92500f, 0.50000f,
				0.92500f);
		circleSymbol.closePath();
		// Symbol für Übungen erzeugen
		exerciseSymbol.moveTo(0.55372f, 0.31453f);
		exerciseSymbol.lineTo(0.78126f, 0.087f);
		exerciseSymbol.curveTo(0.79002f, 0.093f, 0.79859f, 0.09926f, 0.80695f,
				0.10579f);
		exerciseSymbol.lineTo(0.57846f, 0.33427f);
		exerciseSymbol.curveTo(0.56184f, 0.4064f, 0.58971f, 0.43428f, 0.66184f,
				0.41765f);
		exerciseSymbol.lineTo(0.89077f, 0.18872f);
		exerciseSymbol.curveTo(0.89738f, 0.19699f, 0.90373f, 0.20547f,
				0.90981f, 0.21416f);
		exerciseSymbol.curveTo(0.83374f, 0.29024f, 0.75766f, 0.36631f,
				0.68159f, 0.44238f);
		exerciseSymbol.curveTo(0.67654f, 0.44743f, 0.63842f, 0.48315f,
				0.66493f, 0.48417f);
		exerciseSymbol.lineTo(0.70943f, 0.46511f);
		exerciseSymbol.lineTo(0.92968f, 0.24486f);
		exerciseSymbol.curveTo(0.93795f, 0.25871f, 0.94555f, 0.27299f,
				0.95248f, 0.28765f);
		exerciseSymbol.lineTo(0.73591f, 0.50422f);
		exerciseSymbol.lineTo(0.30888f, 0.68723f);
		exerciseSymbol.curveTo(0.53773f, 0.71448f, 0.72109f, 0.80462f,
				0.83397f, 0.87155f);
		exerciseSymbol.curveTo(0.79194f, 0.90937f, 0.7435f, 0.94018f, 0.69052f,
				0.96215f);
		exerciseSymbol.curveTo(0.61258f, 0.85152f, 0.48483f, 0.73106f,
				0.30888f, 0.68723f);
		exerciseSymbol.lineTo(0.49189f, 0.2602f);
		exerciseSymbol.lineTo(0.70705f, 0.04504f);
		exerciseSymbol.curveTo(0.72185f, 0.05182f, 0.73626f, 0.05929f,
				0.75025f, 0.06742f);
		exerciseSymbol.lineTo(0.531f, 0.28668f);
		exerciseSymbol.lineTo(0.51194f, 0.33104f);
		exerciseSymbol.curveTo(0.51282f, 0.35779f, 0.54865f, 0.31965f,
				0.55372f, 0.31453f);
		exerciseSymbol.closePath();
		exerciseSymbol.moveTo(0.41519f, 0.55629f);
		exerciseSymbol.lineTo(0.43957f, 0.5807f);
		exerciseSymbol.lineTo(0.63002f, 0.49913f);
		exerciseSymbol.curveTo(0.62051f, 0.4848f, 0.62372f, 0.46828f, 0.63166f,
				0.45406f);
		exerciseSymbol.curveTo(0.57481f, 0.45406f, 0.53698f, 0.42543f,
				0.54206f, 0.36446f);
		exerciseSymbol.curveTo(0.52772f, 0.37241f, 0.51133f, 0.3756f, 0.4969f,
				0.36604f);
		exerciseSymbol.lineTo(0.41519f, 0.55629f);
		exerciseSymbol.closePath();
		vectImgs = new GeneralPath[] { circleSymbol, exerciseSymbol };
		buildImage();
	}
}
/**
 * Hilfsklasse zum Erzeugen eines Icons für Übungen
 */

final class EIssueIcon extends ResizableIcon {

	public EIssueIcon(int width, int height, Color color) {
		super((GeneralPath[]) null, width, height);
		GeneralPath issueSymbol = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 49);
		// Symbol für Themen anlegen
		issueSymbol.moveTo(0.14286f, 0.1f);
		issueSymbol.lineTo(0.14286f, 0.9f);
		issueSymbol.lineTo(0.85714f, 0.9f);
		issueSymbol.lineTo(0.85714f, 0.25f);
		issueSymbol.lineTo(0.64377f, 0.25f);
		issueSymbol.lineTo(0.64377f, 0.1f);
		issueSymbol.lineTo(0.14286f, 0.1f);
		issueSymbol.closePath();
		issueSymbol.moveTo(0.85714f, 0.2f);
		issueSymbol.lineTo(0.85714f, 0.19142f);
		issueSymbol.lineTo(0.72654f, 0.1f);
		issueSymbol.lineTo(0.7152f, 0.1f);
		issueSymbol.lineTo(0.7152f, 0.2f);
		issueSymbol.lineTo(0.85714f, 0.2f);
		issueSymbol.closePath();
		issueSymbol.moveTo(0f, 0f);
		issueSymbol.lineTo(0f, 1f);
		issueSymbol.lineTo(1f, 1f);
		issueSymbol.lineTo(1f, 0.15f);
		issueSymbol.lineTo(0.78571f, 0f);
		issueSymbol.lineTo(0f, 0f);
		issueSymbol.closePath();
		issueSymbol.moveTo(0.21429f, 0.2f);
		issueSymbol.lineTo(0.5f, 0.2f);
		issueSymbol.lineTo(0.5f, 0.25f);
		issueSymbol.lineTo(0.21429f, 0.25f);
		issueSymbol.lineTo(0.21429f, 0.2f);
		issueSymbol.closePath();
		issueSymbol.moveTo(0.21429f, 0.35f);
		issueSymbol.lineTo(0.78571f, 0.35f);
		issueSymbol.lineTo(0.78571f, 0.4f);
		issueSymbol.lineTo(0.21429f, 0.4f);
		issueSymbol.lineTo(0.21429f, 0.35f);
		issueSymbol.closePath();
		issueSymbol.moveTo(0.21429f, 0.45f);
		issueSymbol.lineTo(0.78571f, 0.45f);
		issueSymbol.lineTo(0.78571f, 0.5f);
		issueSymbol.lineTo(0.21429f, 0.5f);
		issueSymbol.lineTo(0.21429f, 0.45f);
		issueSymbol.closePath();
		issueSymbol.moveTo(0.21429f, 0.55f);
		issueSymbol.lineTo(0.78571f, 0.55f);
		issueSymbol.lineTo(0.78571f, 0.6f);
		issueSymbol.lineTo(0.21429f, 0.6f);
		issueSymbol.lineTo(0.21429f, 0.55f);
		issueSymbol.closePath();
		issueSymbol.moveTo(0.21429f, 0.65f);
		issueSymbol.lineTo(0.78571f, 0.65f);
		issueSymbol.lineTo(0.78571f, 0.7f);
		issueSymbol.lineTo(0.21429f, 0.7f);
		issueSymbol.lineTo(0.21429f, 0.65f);
		issueSymbol.closePath();
		issueSymbol.moveTo(0.21429f, 0.75f);
		issueSymbol.lineTo(0.78571f, 0.75f);
		issueSymbol.lineTo(0.78571f, 0.8f);
		issueSymbol.lineTo(0.21429f, 0.8f);
		issueSymbol.lineTo(0.21429f, 0.75f);
		issueSymbol.closePath();
		vectImgs = new GeneralPath[] { issueSymbol };
		this.setColor(color);
		buildImage();
	}
}