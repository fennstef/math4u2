package math4u2.view.gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.border.Border;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.mathematics.affine.AreaInterface;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.swing.RoundedRectBorder;
import math4u2.view.graph.HasGraph;
import math4u2.view.gui.Math4U2Win;

/**
 * Farbwähler für HasGraph-Implementierungen
 * 
 * @author Fenn Stefan
 */
public class ColorButton extends JComponent implements MathComponentView {

	private static JDialog colorDialog;

	private static JColorChooser colorChooser = new JColorChooser(Color.black);

	protected HasGraph hasGraph;

	protected Broker broker;
	
	public ColorButton(HasGraph hasGraph, Broker broker) {
		this.hasGraph = hasGraph;
		this.broker = broker;
		init();
	} //Konstruktor

	public void init() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		refresh();
		setPreferredSize(new Dimension(20, 20));
		addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				colorDialog = JColorChooser.createDialog(Math4U2Win
						.getInstance(), "Bitte Farbe wählen", true,
						colorChooser, new ColorAction(true), new ColorAction(
								false));				
				colorDialog.setVisible(true);
			}
			
		});
	} //init

	public void refresh() {
	    Object obj = hasGraph;
	    try {
	        if(((UserFunction)hasGraph).isEncapsulated())
	            obj = ((UserFunction)hasGraph).eval();
        } catch (MathException e) {
            ExceptionManager.doError("Fehler beim Entkapseln des Objekts "+hasGraph.getIdentifier(),e);
        }
		if (obj instanceof AreaInterface && ((AreaInterface) obj).isFillArea()) {
			//Falls das Objekt auch Flächen hat, wird eine kombinierte
			//Darstellung erzeugt.
			setBackground(((UserFunction) hasGraph).getFillColor());
			Border b1 = BorderFactory.createLineBorder(((UserFunction)hasGraph).getBorderColor(),3);
			Border b2 = BorderFactory.createLineBorder(Color.BLACK);
			Border b3 = BorderFactory.createLineBorder(Color.BLACK);
			Border b4 = BorderFactory.createCompoundBorder(b2,b1);
			setBorder(BorderFactory.createCompoundBorder(b4,b3));
		} else {
			setBackground(hasGraph.getColor());
			Border b2 = new RoundedRectBorder(Color.BLACK, 1, 3, 0);
		}//else
	} //refresh

	public void setMathModel(MathObject mo) {
		hasGraph = (HasGraph) mo;
	}//setHasGraph

	public void applyColor(Color c) {
		hasGraph.setColor(c);
		ColorButton.this.setBackground(hasGraph.getColor());
	}//applyColor

	/**
	 * Sicherstellen, dass auch Alpha-Farben korrekt dargestellt werden.
	 */
	protected void paintComponent(Graphics gr) {
	    Graphics2D g = (Graphics2D) gr;
	    Stroke oldStroke = g.getStroke();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setStroke(oldStroke);
		super.paintComponent(gr);
	}//paintComponent

	public void deactivate() {
		colorChooser.setEnabled(false);
	}//deactivate	
	
	/** Farbauswahl */
	class ColorAction implements ActionListener {
		private boolean ok;

		public ColorAction(boolean ok) {
			this.ok = ok;
		} //Konstruktor

		public void actionPerformed(ActionEvent evt) {
			if (!ok)
				return;
			applyColor(colorChooser.getColor());
			try {
				broker.propagateChange(hasGraph);
			} catch (BrokerException e) {
				ExceptionManager.doError("Fehler beim Erneuern des Objekts "+hasGraph.getIdentifier(),e);
			} //catch
		} //actionPerformed
	}//ColorAction


} //class ColorButton
