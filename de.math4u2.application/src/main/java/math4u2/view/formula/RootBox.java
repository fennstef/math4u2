package math4u2.view.formula;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Box für Wurzeln (Radix).
 * @author Erich Seifert
 * @version $Revision: 1.7 $
 */
public class RootBox extends ContainerBox {
	/** Konstante, die den Radikanten kennzeichnet */
	public static final String RADICAND = "radicand";
	/** Konstante, die den Wurzelindex kennzeichnet */
	public static final String INDEX = "index";
	/** Konstante, die den Behälter für den Wurzelindex kennzeichnet */
	private static final String INDEX_CONTAINER = "indexContainer";
	/** Darstellungsoptionen für Textelemente */
    private static final RenderingHints ANTIALIAS = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    /** Radikand (Hauptkomoponente) */
    private Component radicand;
    /** Index (Grad der Wurzel) */
    private Component radicalIndex;

	/** Index (Grad der Wurzel) */
    private final ContainerBox indexContainer;
    /** Platzhalter unterhalb des Indexes */
    private final AtomicBox indexGap;

    /** Umrisskurve des Wurzelstriches */
    private Shape rootShape;
    
    /**
     * Konstruktor, der ein neues leeres <code>RootBox</code>-Objekt
     * für eine Quadratwurzel erzeugt und initialisiert.
     * @param frc Darstellungsumgebung
     */
    public RootBox(FormulaRenderContext frc) {
        this(frc, null, null);
    }

    /**
     * Konstruktor, der ein neues <code>RootBox</code>-Objekt für
     * eine Quadratwurzel erzeugt und mit einem bestimmten Inhalt
     * initialisiert.
     * @param frc Darstellungsumgebung
     * @param radicand Inhalt, der unter der Wurzel stehen soll
     */
    public RootBox(FormulaRenderContext frc, String radicand) {
    	this(frc, radicand, null);
    }

    /**
     * Konstruktor, der ein neues <code>RootBox</code>-Objekt für
     * eine Wurzel erzeugt und mit einem bestimmten Inhalt und
     * einem definierten Wurzelgrad initialisiert.
     * @param frc Darstellungsumgebung
     * @param radicand Inhalt, der unter der Wurzel stehen soll
     * @param index Grad der Wurzel
     */
    public RootBox(FormulaRenderContext frc, String radicand, String index) {
        super(frc);

        indexContainer = new GridBox(getRenderContext(), 1, 2);

		radicalIndex = new EmptyBox(getRenderContext(), 9f, 9f);
        indexContainer.add(radicalIndex);

		indexGap = new EmptyBox(getRenderContext(), 9f, 9f);
        indexContainer.add(indexGap);

		add(indexContainer, RootBox.INDEX_CONTAINER);

        if (radicand!=null && radicand.length()>0) {
            this.radicand = new ContainerBox(getRenderContext());
            ((ContainerBox)this.radicand).add(new OrdBox(getRenderContext(), radicand));
            add(this.radicand, RootBox.RADICAND);
        }

        if (index!=null && index.length()>0) {
            radicalIndex = new ContainerBox(getRenderContext());
            ((ContainerBox)radicalIndex).add(new OrdBox(getRenderContext(), index));
			add(radicalIndex, RootBox.INDEX);
        }
	}

    /**
     * Überschriebene Methode.
     * @see math4u2.view.formula.AtomicBox#rebuild()
     */
    protected void rebuild() {
        super.rebuild();

        // Abstände der einzelnen Komontenten neu berechenen,
        // da sich die Zeichensatzgröße geändert haben könnte
        regap();
		// Vektorumriss der Wurzellinie neu berechnen
		reshape();
     }

    /**
     * Hilfsmethode zur Neuberechnung der Abstände zwischen den
     * Unterkomponenten dieses Behälters.
     */
    private void regap() {
    	float muDh = FormulaRenderContext.MU * getDisplayHeight();
    	
    	if (indexContainer!=null) {
            ((AtomicBox)indexContainer).setSpacing(
					0,
					0,
					0,
					Math.round(8f * muDh));
    	}

    	if (radicand!=null) {
	        ((AtomicBox)radicand).setSpacing(
					Math.round(2f * getRenderContext().getLineWidth() * getDisplayHeight()),
					0,
					Math.round(1f * muDh),
					Math.round(2f * muDh));
    	}
    }

	/**
	 * Hilfsmethode zur Neuberechnung des Vektorumrisses der Wurzellinie.
	 * TODO: Schöneren Umriss für Wurzel zeichnen: Statt Linie, Fläche
	 */
	private void reshape() {
		// Unterelemente neu anordnen
		doLayout();

		GeneralPath rootPath = new GeneralPath();
    	rootShape = rootPath;

		if (indexContainer!=null && radicand!=null) {
			Dimension sizeIndexCont = indexContainer.getPreferredSize();
			Dimension sizeIndex     = radicalIndex.getPreferredSize();
			Dimension sizeRadicand  = radicand.getPreferredSize();

			float muDh = FormulaRenderContext.MU * getDisplayHeight();
	    	float radicalIndexXMax = indexContainer.getX() + sizeIndexCont.width - indexContainer.getSpacing().right;
			float radicalIndexYMax = indexContainer.getY() + radicalIndex.getY() + sizeIndex.height;
			rootPath.moveTo(radicalIndexXMax - 7f*muDh, radicalIndexYMax + 3f*muDh);
			rootPath.lineTo(radicalIndexXMax - 5f*muDh, radicalIndexYMax + 2f*muDh);

			// TODO: Fehler in Größenberechnung des FormulaLayouts
			float yMax = Math.max(
					radicand.getY() + sizeRadicand.height,
					indexContainer.getY() + sizeIndexCont.height);
			rootPath.lineTo(radicalIndexXMax, yMax-1);

			float lineWidth_2 = (getRenderContext().getLineWidth() * getDisplayHeight()) / 2f;
			rootPath.lineTo(radicand.getX(), lineWidth_2);
			rootPath.lineTo(radicand.getX() + sizeRadicand.width, lineWidth_2);
		}
	}

    /**
     * Überschriebene Methode, die versucht dem Container ein
     * Component-Objekt hinzuzufügen.
     * @throws IllegalArgumentException falls das Component-Objekt nicht das Interface FormulaElement implmentiert
     * @see java.awt.Container#addImpl(java.awt.Component, java.lang.Object, int)
     */
    protected void addImpl(Component comp, Object constraints, int index) {
    	if (constraints==null)
    		throw new IllegalArgumentException("Cannot add component. A constraint name must be provided.");

    	if (constraints.equals(INDEX_CONTAINER)) {
			super.addImpl(comp, null, index);
		}
		else if (constraints.equals(RADICAND)) {
		    radicand = comp;
			super.addImpl(radicand, null, index);
		}
		else if (constraints.equals(INDEX)) {
			radicalIndex = (comp!=null) ? comp : new EmptyBox(getRenderContext(), 9f, 9f);

			// Indexelement sichtbar verkleinern
		    ((AtomicBox)radicalIndex).shrinkVisually();

			// Neu einfügen
			indexContainer.removeAll();
		    indexContainer.add(radicalIndex);
		    indexContainer.add(indexGap);
		}

		// Umrissgrafiken neuberechnen
    	rebuild();
    }

    /**
     * Überschriebene Methode.
     * @see math4u2.view.formula.ContainerBox#getMainComponent()
     */
    protected Component getMainComponent() {
        return radicand;
    }

    /**
     * Überschriebene Methode, die für die Darstellung dieser
     * Komponente zuständig ist.
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
    	super.paint(g);

		// FIXME: Umgeht einen Pufferungsfehler bei der Größenberechnung
		// im FormulaLayout
		reshape();
		if (rootShape!=null) {
	    	Graphics2D g2 = (Graphics2D)g;
	        g2.setRenderingHints(ANTIALIAS);
	
	    	float lineWidth = getRenderContext().getLineWidth() * getDisplayHeight();
	    	g2.setStroke(new BasicStroke(lineWidth));
	    	g2.draw(rootShape);
		}
    }

}