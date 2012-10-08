package math4u2.view.graph.drawarea;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import math4u2.application.resource.Colors;
import math4u2.application.resource.Images;
import math4u2.application.resource.Settings;
import math4u2.view.Formatierer;

/**
 * Hier werden die einzelen Einstellungen für die Zeichenfläche festgelegt.
 * 
 * @see DrawAreaInterface
 * 
 * @author Fenn Stefan
 */
public class PropertyFrame extends JFrame {
	/** Referenz zur Zeichenfläche */
	private DrawAreaInterface da;

	//Einstellungswerte
	private int detail, stroke;

	private double xMin = -5, xMax = 5, yMin = -5, yMax = 5, gridMesh;

	private double xMinA, yMinA, xMaxA, yMaxA;

	Color gridColor, axesColor;

	//Darstellung
	private JTextField left = new JTextField("-5", 3);

	private JTextField right = new JTextField("5", 3);

	private JTextField up = new JTextField("5", 3);

	private JTextField down = new JTextField("-5", 3);

	private JCheckBox checkBox = new JCheckBox("Übernehmen");

	private JCheckBox detailCheckBox = new JCheckBox(
			" grobes Detail beim Zoom ?");

	private JLabel koord = new JLabel();
	
	private JTextField title;

	private JColorChooser colorChooser = new JColorChooser(Color.black);

	private JDialog colorDialog;

	private JFrame frame;

	JComponent gridButton;

	private JPanel axesButton;

	/**
	 * Konstruktor: Initialisiert die aktuellen Werte der DrawArea
	 */
	public PropertyFrame(DrawAreaInterface da, int x, int y) {
	    super("Einstellungen");

		//Initialisierung
		frame = this;
		this.da = da;
		detail = da.getDetail();
		stroke = da.getStroke();
		gridMesh = da.getGridMeshX();
		gridColor = da.getGridColor();
		axesColor = da.getAxisColor();
		xMin = da.getXMin();
		xMax = da.getXMax();
		yMin = da.getYMin();
		yMax = da.getYMax();
		xMinA = xMin;
		xMaxA = xMax;
		yMinA = yMin;
		yMaxA = yMax;
		title = new JTextField(da.getTitle(),20);

		setIconImage(Images.LOGO_ICON.getImage());
		
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createTitledBorder("Detail-Stufe"));
		GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		p.setLayout(new GridBagLayout());

		//Slider
		JSlider s = new JSlider(1, 20, 1);
		final JSlider sl = s;
		JTextField tf = new JTextField("1", 3);
		final JTextField tf2 = tf;
		tf.setEditable(false);
		final DrawAreaInterface da1 = da;
		s.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				da1.setDetail(sl.getValue());
				tf2.setText(Integer.toString(da1.getDetail()));
				da1.graphHasChanged();
			}//stateChanged
		});
		Hashtable labelTable2 = new Hashtable();
		labelTable2.put(new Integer(1), new JLabel("1"));
		labelTable2.put(new Integer(5), new JLabel("5"));
		labelTable2.put(new Integer(10), new JLabel("10"));
		labelTable2.put(new Integer(15), new JLabel("15"));
		labelTable2.put(new Integer(20), new JLabel("20"));

		s.setLabelTable(labelTable2);
		s.setMajorTickSpacing(5);
		s.setMinorTickSpacing(1);
		s.setPaintTicks(true);
		s.setPaintLabels(true);
		s.setValue(detail);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		p.add(s, gridBagConstraints);

		//grobe Detail-Stufe bei Zoom
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(0, 5, 0, 0);
		detailCheckBox.setSelected(da.isFastZoom());
		p.add(detailCheckBox, gridBagConstraints);
		JPanel p2 = new JPanel();
		p2.setLayout(new GridBagLayout());
		p2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		//Übernehmen CheckBox (Koordinaten-System)
		gridBagConstraints = new java.awt.GridBagConstraints();
		final DrawAreaInterface da3 = da;
		gridBagConstraints.insets = new Insets(0, 0, 5, 0);
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		final JCheckBox c2 = checkBox;
		checkBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (c2.isSelected()) {
					checkKoord();
				} //isSelected
				else {
					xMin = xMinA;
					xMax = xMaxA;
					yMin = yMinA;
					yMax = yMaxA;
				} //nicht übernehmen
				da3.coordinateSystem(xMin, xMax, yMin, yMax);
				da3.graphHasChanged();
			}
		});
		p2.add(checkBox, gridBagConstraints);
		//links
		gridBagConstraints = new java.awt.GridBagConstraints();
		left.setText("-5");
		left.setBackground(Colors.EDIT);
		left.setHorizontalAlignment(JTextField.RIGHT);
		left.setBorder(null);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		left.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!c2.isSelected())
					return;
				if (checkKoord()) {
					da3.coordinateSystem(xMin, xMax, yMin, yMax);
					da3.graphHasChanged();
				} //if
			}
		});
		p2.add(left, gridBagConstraints);

		//rechts
		right.setText("5");
		right.setBackground(Colors.EDIT);
		right.setBorder(null);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		right.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!c2.isSelected())
					return;
				if (checkKoord()) {
					da3.coordinateSystem(xMin, xMax, yMin, yMax);
					da3.graphHasChanged();
				} //if
			}
		});
		p2.add(right, gridBagConstraints);

		//oben
		up.setText("5");
		up.setBackground(Colors.EDIT);
		up.setHorizontalAlignment(JTextField.CENTER);
		up.setBorder(null);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		up.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!c2.isSelected())
					return;
				if (checkKoord()) {
					da3.coordinateSystem(xMin, xMax, yMin, yMax);
					da3.graphHasChanged();
				} //if
			}
		});
		p2.add(up, gridBagConstraints);

		//unten
		down.setText("-5");
		down.setBorder(null);
		down.setHorizontalAlignment(JTextField.CENTER);
		down.setBackground(Colors.EDIT);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		down.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!c2.isSelected())
					return;
				if (checkKoord()) {
					da3.coordinateSystem(xMin, xMax, yMin, yMax);
					da3.graphHasChanged();
				} //if
			}
		});
		p2.add(down, gridBagConstraints);

		//Koordinaten-Bild
		koord.setIcon(new ImageIcon(ClassLoader
				.getSystemResource("math4u2/images/koord.gif")));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		p2.add(koord, gridBagConstraints);
		JPanel p3 = new JPanel();
		p3.setBorder(BorderFactory.createTitledBorder("Koordinaten"));
		p3.add(p2);

		//Slider Stroke
		JPanel p5 = new JPanel();
		p5.setBorder(BorderFactory.createTitledBorder("Graphenstärke"));
		JSlider slider = new JSlider(1, 3, 1);
		final JSlider slider2 = slider;
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				da1.setStroke(slider2.getValue());
				da1.graphHasChanged();
			}
		});
		Hashtable labelTable = new Hashtable();
		labelTable.put(new Integer(1), new JLabel("dünn"));
		labelTable.put(new Integer(2), new JLabel("mittel"));
		labelTable.put(new Integer(3), new JLabel("stark"));
		slider.setLabelTable(labelTable);
		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		slider.setValue(stroke);
		p5.add(slider);

		//Gitterfarbe
		JPanel p6 = new JPanel();
		gridButton = new JPanel();
		gridButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		gridButton.setBackground(da.getGridColor());
		gridButton.setPreferredSize(new Dimension(20, 20));
		gridButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				colorDialog = JColorChooser.createDialog(frame,
						"Gitterfarbe wählen", true, colorChooser,
						new ColorAction(true, false), new ColorAction(false));
				colorDialog.setVisible(true);
			}
		});
		p6.add(gridButton);
		p6.add(new JLabel("Gitterfarbe"));

		//Achsenfarbe
		JPanel p7 = new JPanel();
		axesButton = new JPanel();
		axesButton.setBackground(da.getAxisColor());
		axesButton.setPreferredSize(new Dimension(20, 20));
		axesButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				colorDialog = JColorChooser.createDialog(frame,
						"Achsenfarbe wählen", true, colorChooser,
						new ColorAction(true, true), new ColorAction(false));
				colorDialog.setVisible(true);
			}
		});
		
		p7.add(axesButton);
		p7.add(new JLabel("Achsenfarbe"));

		//Zusammenfassung Achsen- und Gitterfarbe zu Farbe
		JPanel p8 = new JPanel();
		p8.setBorder(BorderFactory.createTitledBorder("Farbe"));
		p8.add(p6);
		p8.add(p7);

		//Hilfslinien
		JPanel p9 = new JPanel();
		p9.setBorder(BorderFactory.createTitledBorder("Hilfslinien"));
		slider = new JSlider(1, 5, 3);
		final JSlider slider3 = slider;
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int s = slider3.getValue();
				double val = 1.0;
				if(s==1) val=.25;
				if(s==2) val=0.5;
				if(s==3) val=1.0;
				if(s==4) val=2.0;
				if(s==5) val=2.5;
				da1.setGridMesh(val, val);
				da1.graphHasChanged();
			}//stateChanged
		});
		labelTable = new Hashtable();
		labelTable.put(new Integer(1), new JLabel("grob"));
		labelTable.put(new Integer(3), new JLabel("mittel"));
		labelTable.put(new Integer(5), new JLabel("fein"));
		slider.setLabelTable(labelTable);
		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		
		if(gridMesh==0.25) slider.setValue(1);
		else if(gridMesh==0.5) slider.setValue(2);
		else if(gridMesh==1.0) slider.setValue(3);
		else if(gridMesh==2.0) slider.setValue(4);
		else if(gridMesh==2.5) slider.setValue(5);
		else slider.setValue(3);
		
		p9.add(slider);
		
		JPanel p10 = new JPanel();
		p10.setBorder(BorderFactory.createTitledBorder("Titel"));
		p10.add(title);
		

		//OK
		final DrawAreaInterface da2 = da;
		final JCheckBox cb = detailCheckBox;
		JPanel p4 = new JPanel();
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				da2.setTitle(title.getText());
				da2.setFastZoom(cb.isSelected());
				dispose();
			}
		});

		//Abrechen
		JButton cancel = new JButton("Abbrechen");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				da2.setDetail(detail);
				da2.setStroke(stroke);
				da2.coordinateSystem(xMin, xMax, yMin, yMax);
				da2.graphHasChanged();
				da2.setGridColor(gridColor);
				da2.setAxisColor(axesColor);
				da2.setGridMesh((int) gridMesh, (int) gridMesh);
				dispose();
			}
		});
		p4.add(ok);
		p4.add(cancel);

		//Alles zusammen
		JPanel mix = new JPanel();
		mix.setLayout(new BoxLayout(mix, BoxLayout.Y_AXIS));
		mix.add(p);
		mix.add(p3);
		mix.add(p5);
		mix.add(p8);
		mix.add(p9);
		mix.add(p10);
		mix.add(p4);
		getContentPane().add(mix);
		setBounds(Settings.computeBounds((Component) da,250,600));
		setVisible(true);
	} //init

	public boolean checkKoord() {
		//left
		String s = left.getText().trim();
		Number n = Formatierer.parse2Number(s);
		if (n == null) {
			left.setBackground(Color.red);
			return false;
		}
		double dl = n.doubleValue();

		//right
		s = right.getText().trim();
		n = Formatierer.parse2Number(s);
		if (n == null) {
			right.setBackground(Color.red);
			return false;
		}
		double dr = n.doubleValue();

		//up
		s = up.getText().trim();
		n = Formatierer.parse2Number(s);
		if (n == null) {
			up.setBackground(Color.red);
			return false;
		}
		double du = n.doubleValue();

		//down
		s = down.getText().trim();
		n = Formatierer.parse2Number(s);
		if (n == null) {
			down.setBackground(Color.red);
			return false;
		}
		double dd = n.doubleValue();

		// x check
		if (dl >= dr) {
			left.setBackground(Color.red);
			right.setBackground(Color.red);
			return false;
		}
		if (dd >= du) {
			down.setBackground(Color.red);
			up.setBackground(Color.red);
			return false;
		}
		left.setBackground(Colors.EDIT);
		right.setBackground(Colors.EDIT);
		up.setBackground(Colors.EDIT);
		down.setBackground(Colors.EDIT);
		xMin = dl;
		xMax = dr;
		yMin = dd;
		yMax = du;
		return true;

	} //checkKoord

	class ColorAction implements java.awt.event.ActionListener {
		private boolean ok;

		private boolean isAxes;

		public ColorAction(boolean ok) {
			this.ok = ok;
		}//Konstruktor

		public ColorAction(boolean ok, boolean isAxes) {
			this.ok = ok;
			this.isAxes = isAxes;
		}//Konstruktor

		public void actionPerformed(ActionEvent evt) {
			if (!ok)
				return;
			Color c = colorChooser.getColor();
			if (isAxes) {
				axesButton.setBackground(c);
				da.setAxisColor(c);
			} else {
				gridButton.setBackground(c);
				da.setGridColor(c);
			}//else
			da.graphHasChanged();
		}//actionPerformed

	} //class ColorAction
} //class PropertyFrame
