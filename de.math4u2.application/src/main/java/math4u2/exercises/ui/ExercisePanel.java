package math4u2.exercises.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import math4u2.application.resource.Folders;
import math4u2.application.resource.Images;
import math4u2.exercises.EIssue;
import math4u2.exercises.EParser;
import math4u2.exercises.Exercise;
import math4u2.exercises.Folder;
import math4u2.exercises.Solvable;
import math4u2.exercises.Step;
import math4u2.exercises.scripting.BreakActionSingelton;
import math4u2.exercises.scripting.Scriptable;
import math4u2.exercises.scripting.Ticker;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.parser.ParseException;
import math4u2.util.io.file.xml.filestatus.FileStatusInterface;
import math4u2.util.io.file.xml.filestatus.Local;
import math4u2.util.swing.RoundedRectBorder;
import math4u2.view.dragAndDrop.target.DropTargetAndDeny;
import math4u2.view.formula.FormulaRenderContext;
import math4u2.view.graph.drawarea.DrawAreasManager;
import math4u2.view.gui.Math4U2Win;

/**
 * Fenster zum Darstellen von Übungen.
 * 
 * @author Erich Seifert, Fenn Stefan
 * 
 * @see math4u2.exercises.EIssue
 * @see math4u2.exercises.EElement
 */
public class ExercisePanel extends JPanel {
	private static EIssue issue;

	/** container for main controls */
	private JPanel navcontainer = new JPanel();

	/** button for showing the failures */
	private JButton showFailure = new JButton("Überprüfen");

	/** button for showing the solution */
	private JButton showSolution = new JButton("Lösen");

	/** button for showing then next step */
	JButton nextButton = new JButton("Weiter");

	/** button for showing the beginning */
	JButton beginButton = new JButton("Anfang");

	/** slider for setting up the speedRate */
	private JSlider slider = new JSlider(0, 200);

	/** EParser */
	protected EParser parser;

	/**
	 * Aktuelles darzustellendes <code>EElement</code>.
	 */
	protected Viewable curView;

	/**
	 * Aktuelles ausgewähltes Element
	 */
	protected Object curElement;

	/** Schritt zur Initialsierung */
	public static Step INIT_STEP = null;

	/** Schritt zum löschen aller Zeichenflächen und Objekten */
	public static Step REINIT_STEP = null;

	protected Scriptable curScript;

	private boolean isHelpFrame;

	private JFrame parent;

	private JPanel mainView;

	private JComponent contentView;

	JScrollPane contentScroller;

	JPopupMenu popupMenu = new JPopupMenu();

	private JLabel loadingLabel = null;

	/**
	 * Konstruktor, der ein neues ExerciseFrame-Objekt erzeugt.
	 * 
	 */
	public ExercisePanel(JFrame parent, boolean isHelpFrame, EParser parser) {
		super(new BorderLayout(0, 0));
		this.parser = parser;
		this.parent = parent;
		this.isHelpFrame = isHelpFrame;
		mainView = new JPanel();
		Border b1 = BorderFactory.createLineBorder(getBackground(), 10);
		Border b2 = new RoundedRectBorder(Color.BLACK, 1.2f, 7, 0);
		Border b3 = BorderFactory.createCompoundBorder(b1, b2);
		mainView.setBorder(b3);
		mainView.setLayout(new BorderLayout(0, 0));
		loadingLabel = new JLabel("Laden  ...", Images.REFRESH, JLabel.LEFT);
		loadingLabel.setFont(FormulaRenderContext.getFont("Dialog", Font.BOLD,
				24));
		loadingLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

		if (INIT_STEP == null) {
			EIssue issue = null;
			try {
				EParser tempParser = new EParser(Math4U2Win.getInstance()
						.getBroker());
				tempParser.analyseDOM(Folders.INIT_XML_FILE);
				issue = tempParser.getIssue();
				INIT_STEP = issue.getStep(0);
				tempParser.clear();
			} catch (ParseException e) {
				ExceptionManager.doError(
						"Fehler beim Lesen des Initialisierungsskripts "
								+ Folders.MATH4U2_FOLDER
								+ "math4u2/conf/initialisierung.xml", e);
				return;
			}// catch
		} // if

		if (REINIT_STEP == null) {
			EIssue issue = null;
			try {
				EParser tempParser = new EParser(Math4U2Win.getInstance()
						.getBroker());
				tempParser.analyseDOM(Folders.REINIT_XML_FILE);
				issue = tempParser.getIssue();
				REINIT_STEP = issue.getStep(0);
				tempParser.clear();
			} catch (ParseException e) {
				ExceptionManager.doError(e);
				return;
			}// catch
		} // if

		init();
	} // Konstruktor

	public void organizeTreeMousePress() {
		if (isHelpFrame) {
			organizeInHelpFramePress();
			return;
		} // if isHelpFrame

		if (curElement instanceof EIssue) {
			FileStatusInterface fs = ((EIssue) curElement).getStatus();
			startElement(REINIT_STEP);
			startElement(INIT_STEP);
			curElement = fs.manageAction(this, curElement);
		} // if is EIssue

		if (curElement instanceof Step) {
			((Step) curElement).clearHistory();
		} // if is Step

		if (curElement instanceof Folder) {
			DrawAreasManager.unregisterAllDrawAreas();
			curElement = INIT_STEP;
		} // if (curElement instanceof Folder)

		if (curElement instanceof Exercise) {
			Exercise exercise = (Exercise) curElement;
			EIssue issue2 = exercise.getParent();

			checkCurrentIssue(issue2);

			Iterator iterator = issue.getExerciseIterator();
			while (iterator.hasNext()) {
				Exercise ex = (Exercise) iterator.next();
				if (ex.equals(exercise)) {
					curElement = ex;
					break;
				} // if
			} // while
		} // if is Exercise

		startElement(curElement);
	}// organizeTreeMousePress

	public void organizeInHelpFramePress() {
		if (curElement instanceof Folder)
			return;

		if (curElement instanceof EIssue) {
			checkCurrentIssue((EIssue) curElement);
			curElement = issue.getStep(0);
		} // if is EIssue

		if (curElement instanceof Exercise) {
			Exercise exercise = (Exercise) curElement;
			EIssue issue2 = exercise.getParent();

			checkCurrentIssue(issue2);

			Iterator iterator = issue.getExerciseIterator();
			while (iterator.hasNext()) {
				Exercise ex = (Exercise) iterator.next();
				if (ex.equals(exercise)) {
					curElement = ex;
					break;
				} // if
			} // while
		} // if is Exercise
		DrawAreasManager.setLocked(true);
		startElement(curElement);
		DrawAreasManager.setLocked(false);
	} // organizeInHelpFramePress

	public void startElement(Object curElement) {
		if (curElement == null)
			ExceptionManager.doError("Die Lektion wurde nicht gefunden");

		if (curElement instanceof Layoutable) {
			((Layoutable) curElement).buildLayout();
		}
		if (curElement instanceof Scriptable) {
			animate((Scriptable) curElement);
		}
		if (curElement instanceof Viewable) {
			view((Viewable) curElement);
		}

		// Für BreakAction
		if (curElement instanceof Step) {
			Step step = (Step) curElement;
			Step step2 = step.getParentIssue().getStep(0);
			boolean hasBreakAction = step.hasBreakAction();
			while (step.hasNextStep() && !hasBreakAction) {
				step = step.getNextStep();
				hasBreakAction = step.hasBreakAction();
			}

			// System.out.println("HasBreak: "+hasBreakAction);
			if (hasBreakAction) {
				BreakActionSingelton.getInstance().setBeforeBreak(true);
				nextButtonAction();
			}
		}
	} // startElement

	/**
	 * Schaut, ob das Issue schon gecacht ist, oder ladet das Issue neu
	 */
	public void checkCurrentIssue(EIssue curElement) {
		// kein caching
		// if (issue != null && issue.getTitle().equals(curElement.getTitle()))
		// return;
		try {
			parser.analyseDOM(new File(((EIssue) curElement).getFilename()));
			issue = parser.getIssue();
			issue.setStatus(new Local());
			issue.setFilename(((EIssue) curElement).getFilename());
		} catch (ParseException e) {
			ExceptionManager.doError(e);
		} // catch
		parser.clear();
	} // getCurrentIssue

	/**
	 * Initialisiert die Bedienelemente des neuen Fensters
	 */
	private void init() {
		new DropTargetAndDeny(this);
		// Erklärungsfenster initialisieren
		EExplanationFrame explFrame = new EExplanationFrame(parent);
		explFrame.setLocationRelativeTo(parent);
		ESkin.setExplanationFrame(explFrame);

		// Schaltflächen einfügen
		showFailure.setVisible(false);
		showSolution.setVisible(false);
		nextButton.setVisible(false);
		beginButton.setVisible(false);
		slider.setVisible(false);

		nextButton.setIcon(Images.NEXT);
		nextButton.setHorizontalTextPosition(SwingConstants.LEFT);
		nextButton.setIconTextGap(10);

		beginButton.setIcon(Images.BEGIN);
		beginButton.setIconTextGap(10);

		navcontainer.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints1;
		gridBagConstraints1 = new GridBagConstraints();

		JPanel bag = new JPanel();
		bag.setOpaque(false);
		bag.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		bag.add(showFailure);
		bag.add(showSolution);
		bag.add(beginButton);
		bag.add(nextButton);
		// ShowFailure + ShowSolution + BeginButton + NextButton
		gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 3;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1;
		gridBagConstraints1.anchor = GridBagConstraints.SOUTHWEST;
		navcontainer.add(bag, gridBagConstraints1);
		// Slider
		slider.setBackground(Color.WHITE);
		gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 5;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1;
		gridBagConstraints1.anchor = GridBagConstraints.EAST;
		navcontainer.add(slider, gridBagConstraints1);

		// Create the label table
		Hashtable labelTable = new Hashtable();
		labelTable.put(new Integer(0), new JLabel("Langsam"));
		labelTable.put(new Integer(100), new JLabel("Normal"));
		labelTable.put(new Integer(200), new JLabel("Schnell"));
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(20);
		slider.setPaintTicks(true);

		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				double value = source.getValue();
				value -= 100;
				value = Math.pow(10, value / 100.f);
				math4u2.exercises.scripting.EActionAnimation
						.setSpeedRate(value);
			} // stateChanged
		});

		// Aktion festlegen, die beim Anklicken der Schaltfläche showSolution
		// ausgeführt wird.
		showSolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Solvable) curView).showSolution();
			}
		});

		// Aktion festlegen, die beim Anklicken der Schaltfläche showFailure
		// ausgeführt wird.
		showFailure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Solvable) curView).showFailures();
			}
		});
		// Aktion festlegen, die beim Anklicken der Schaltfläche beginButton
		// ausgeführt wird.
		beginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				curElement = getIssue();
				organizeTreeMousePress();
			}// actionPerformed
		});

		// Aktion festlegen, die beim Anklicken der Schaltfläche nextButton
		// ausgeführt wird.
		final ExercisePanel ep2 = this;
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ep2.nextButtonAction();
			}
		});

		// Kontrollelemente in die Hauptansicht einfügen
		mainView.add(navcontainer, BorderLayout.SOUTH);

		// Rollbereich erzeugen
		contentScroller = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contentScroller.setBackground(Color.WHITE);
		contentScroller.getViewport().setBackground(Color.WHITE);

		Border b1 = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY);
		Border b2 = BorderFactory.createEmptyBorder(0, 0, 3, 0);

		contentScroller.setBorder(BorderFactory.createCompoundBorder(b2, b1));
		mainView.setBackground(Color.WHITE);
		navcontainer.setOpaque(false);

		mainView.add(contentScroller, BorderLayout.CENTER);
		loadingLabel.setVisible(false);
		mainView.add(loadingLabel, BorderLayout.NORTH);

		add(mainView, BorderLayout.CENTER);
	} // init

	public void setCurrentElement(Object o) {
		if (o == null) {
			Math4U2Win.getInstance().setTitleText("");
		} else if (o instanceof EIssue && !isHelpFrame) {
			Math4U2Win.getInstance().setTitleText(((EIssue) o).getTitle());
		}
		curElement = o;
	} // setCurrentElement

	/** Setzt die Standard-Zeichenfläche und initialisiert sich */
	public void refresh() {
		issue = null;
		curElement = INIT_STEP;
		DrawAreasManager.unregisterAllDrawAreas();
		startElement(curElement);
	} // refresh

	/** Setzt die Standard-Zeichenfläche und initialisiert sich */
	public void reinit() {
		issue = null;
		curElement = REINIT_STEP;
		DrawAreasManager.unregisterAllDrawAreas();
		startElement(curElement);
	} // reinit

	public void nextButtonAction() {
		Step step = ((Step) curElement).getNextStep();
		step.buildLayout();
		if (step.getActions().hasAnimation())
			setAnimationIsPlaying(true);
		animate(step);
		view(step);
		nextButton.setVisible(step.hasNextStep());
		beginButton.setVisible(true);
		curElement = step;

		if (!BreakActionSingelton.getInstance().isBeforeBreak()) {
			return;
		}

		executor.execute(waitForAnimationEnd);
	} // nextButtonAction

	private static ExecutorService executor = Executors.newCachedThreadPool();
	private WaitForAnimationEnd waitForAnimationEnd = new WaitForAnimationEnd();

	private class WaitForAnimationEnd implements Runnable {
		public void run() {
			if (!((Step) curElement).hasNextStep())
				return;
			while (Ticker.getInstance().getAction() != null) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					ExceptionManager.doError(e);
				}
			}
			try {
				SwingUtilities.invokeAndWait(invokeNextButtonAction);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}
	}

	private InvokeNextButtonAction invokeNextButtonAction = new InvokeNextButtonAction();

	private class InvokeNextButtonAction implements Runnable {
		public void run() {
			if (!BreakActionSingelton.getInstance().isBeforeBreak())
				return;
			if (!((Step) curElement).hasNextStep())
				return;
			nextButtonAction();
		}
	}

	public void setAnimationIsPlaying(boolean b) {
		nextButton.setEnabled(!b);
	} // setAnimationIsPlaying (boolean b)

	/**
	 * Zeigt das übergebene darstellbare Element an.
	 * 
	 * @param v
	 *            Das darzustellende Element
	 */
	public void view(Viewable v) {
		curView = v;

		// Ansicht des neuen Elements holen
		contentView = curView.getView();
		contentView.setOpaque(false);

		// Anzeigen der neuen Ansicht
		contentScroller.getViewport().add(contentView);
		Component comp = contentView.getComponent(contentView
				.getComponentCount() - 2);
		if (comp instanceof Math4u2TextPane) {
			Math4u2TextPane c = (Math4u2TextPane) comp;
			c.prepareForScroll();
			c.invalidate();
			c.revalidate();
			if (c.isHistory()) {
				contentView.scrollRectToVisible(new Rectangle(0, contentView
						.getSize().height, 0, 0));
			} // if isHistory
		} // if instanceof Math4u2TextPane

		// Popup hinzufügen
		// contentView.addMouseListener(popupListener);
		// Component[] ca =contentView.getComponents();
		// for(int i=0; i<ca.length;i++) ca[i].addMouseListener(popupListener);

		if (v instanceof Solvable) {
			showFailure.setVisible(true);
			showSolution.setVisible(true);
			nextButton.setVisible(false);
			beginButton.setVisible(false);
			slider.setVisible(false);
		} else {
			if (curElement != null && curElement instanceof Step) {
				nextButton.setVisible(((Step) curElement).hasNextStep());
				beginButton.setVisible(((Step) curElement).hasNextStep());
				// slider.setVisible(curScript.getActions().hasAnimation());
				slider.setVisible(((Step) curElement).hasNextStep());
			} // if
			showFailure.setVisible(false);
			showSolution.setVisible(false);
		} // else
	}// view

	public Math4u2TextPane[] getAllViewsToPrint() {
		if (contentView == null)
			return new Math4u2TextPane[0];
		Component[] comps = contentView.getComponents();
		Math4u2TextPane[] result = new Math4u2TextPane[comps.length - 1];
		for (int i = 0; i < result.length; i++) {
			result[i] = (Math4u2TextPane) comps[i];
		}
		return result;
	}// getAllViewsToPrint

	/**
	 * Zeigt das übergebene darstellbare Element an.
	 * 
	 * @param v
	 *            Das darzustellende Element
	 */
	public void animate(Scriptable s) {
		curScript = s;
		curScript.animate();
	} // animate

	/**
	 * @return
	 */
	public static EIssue getIssue() {
		return issue;
	}

	public void showLoading() {
		contentScroller.setVisible(false);
		loadingLabel.setVisible(true);
	}

	public void hideLoading() {
		contentScroller.setVisible(true);
		loadingLabel.setVisible(false);
	}

} // class ExercisePanel
