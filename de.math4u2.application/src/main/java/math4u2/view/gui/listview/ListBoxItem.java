package math4u2.view.gui.listview;

import javax.swing.JComponent;
import javax.swing.JLabel;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.Relation;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.dragAndDrop.DNDHandler;
import math4u2.view.graph.HasGraph;
import math4u2.view.gui.component.ColorButton;
import math4u2.view.gui.component.VisibleButton;
import math4u2.view.layout.TableLayout;

/**
 * Darsteller eines Objekt in der ListBox
 * 
 * @author Fenn Stefan
 */
public class ListBoxItem extends AbstractListViewItem {
	private boolean isDrawable;

	private VisibleButton visibleBox;

	private ColorButton colorButton;

	private JLabel fnLabel;

	public static void register(MathObject lbi, MathObject mo, Broker broker) {
		try {
			RelationInterface ri = RelationFactory.getCanvas_View_Relation();
			ri.setShortName(Relation.FIRST, "$" + mo.getIdentifier());

			MathObject listBox = broker.getObject("ListBox");
			broker.addRelation(listBox, lbi, ri, Broker.FIRST_OBJECT);

			ri = RelationFactory.getFunction_ListView_Relation();

			broker.addRelation(mo, lbi, ri, Broker.FIRST_OBJECT);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler beim Registrieren des Objekts "+mo.getIdentifier()+" in der Definitionsliste.",e);
		} //catch
	} //register

	public ListBoxItem(UserFunction f, ListViewInterface parent, Broker broker) {
		super(f, parent, broker);
	} //Konstruktor

	protected void onceInit(){
		super.onceInit();

		Object theModel = model;
		if(model.isEncapsulated()){
			try {
				theModel = model.eval();
			} catch (MathException e) {
				ExceptionManager.doError(e);
			}
		}
		
		isDrawable = (theModel instanceof HasGraph)
				&& (((HasGraph) theModel).hasCurrentObjectGraph());
		
		
		if (isDrawable) {
			// VisibleBox
			visibleBox = new VisibleButton((HasGraph) model, broker);
			addListener(visibleBox);
			
			//ColorButton
			colorButton = new ColorButton((HasGraph) model, broker);
			addListener(colorButton);			
		}//if
		
		//Function Label
		fnLabel = new JLabel();
		addListener(fnLabel);

		//Layout
		double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = {
		/* Zeilen */// 
				{ border, P, border / 2, P, border, P, F, border },
				/* Spalten */// 
				{ border, P, border } };

		setLayout(new TableLayout(size));

		if (isDrawable) {
			add(visibleBox, "1, 1, L, C");
			add(colorButton, "3, 1, L, C");
			add(fnLabel, "5, 1, L, C");
		} else {
			add(fnLabel, "1, 1, L, C");
		} //else
	} //onlyFirstInit

	public void reInit() {
		fnLabel.setText(model.toString());
		if (isDrawable) {
			colorButton.setMathModel((HasGraph) model);
			colorButton.refresh();
			visibleBox.setMathModel((HasGraph) model);
			visibleBox.refresh();
		} //if
	} //init

	protected void addListener(JComponent c) {
		super.addListener(c);
		String name = model.getKey() + "";
		new DNDHandler(c, name, broker);
	} //addListener

	public void renew(MathObject source) {
		reInit();
	}//renew

} //class ListBoxItem
