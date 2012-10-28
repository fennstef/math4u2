package math4u2.view.gui.listview;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.controller.reference.PathStep;
import math4u2.controller.relation.RelationContainer;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.NotImplementedException;

/**
 * Abstrakter Darsteller eines Objekt in der AbstractListView
 * 
 * @author Fenn Stefan
 */
public abstract class AbstractListViewItem extends JPanel implements MathObject, ListViewItemInterface {

	/** Referenz zum Broker */
	protected Broker broker;

	/** Container aller Beziehungen */
	protected RelationContainer relationContainer;

	/** Funktion, die bei der Evaluierung das Model zurück gibt */
	protected UserFunction model;
	
	/** gecachtes Model, welches dargestellt wird */
	protected MathObject cachedObject;

	/** Referenz zum Behälter */
	protected ListViewInterface parent;
	
	/** Popupmenü */
	protected JPopupMenu popupMenu;
	
	/** Löschen Menü */
	protected JMenuItem deleteMenuItem;
	
	/** Export Menü */
	protected JMenuItem exportMenuItem;

	public AbstractListViewItem(UserFunction model, ListViewInterface alv,
			Broker broker) {
		super(new FlowLayout(FlowLayout.LEFT));
		this.broker = broker;
		if (model == null)
			throw new NullPointerException();
		this.model = model;
		this.parent = alv;
		onceInit();
		reInit();
	} //Konstruktor

	/** einmalige Initialisierung */
	protected void onceInit() {
		relationContainer = new RelationContainer(this);
		addListener(this);
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		
		//Popup
		popupMenu = new JPopupMenu();
		
		deleteMenuItem = new JMenuItem("Löschen");
		deleteMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				parent.selectItem(AbstractListViewItem.this);
				parent.deleteSelectedItem();
			}//actionPerformed
		});
		popupMenu.add(deleteMenuItem);
		
		exportMenuItem = new JMenuItem("Exportieren ...");
		exportMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				parent.selectItem(AbstractListViewItem.this);
				parent.exportData(AbstractListViewItem.this);
			}//actionPerformed
		});
		popupMenu.add(exportMenuItem);
		
	} //onlyFirstInit

	/** Reinitialisierung */
	protected abstract void reInit();

	public Object getKey() {
		return null;
	} //getKey
	
	public Object getIdentifier() {
		return null;
	} //getKey

	public boolean testSubstitution(MathObject oldObject, Set oldAggregateSet) {
		return true;
	} //testSubstitiution

	public boolean testDelete() {
		return true;
	} //testDelete

	/**
	 * Bevor das Item gelöscht wird, wird folgendes noch erledigt
	 */
	public void prepareDelete() {
		parent.removeItem(this);
		parent = null;
		model = null;
		cachedObject = null;
		relationContainer = null;
	} //prepareDelete

	public void renew(MathObject source) {
		cachedObject = evalModel();
		reInit();
	} //renew

	public RelationContainer getRelationContainer() {
		return relationContainer;
	} //getRelationContainer

	public void setName(String name) {
	}

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (oldObject == model)
			model = (UserFunction) newObject;
		else
			model.swapLinks(oldObject, newObject);
		cachedObject = evalModel();
	} //swapLinks

	public String toString() {
		return model.toString();
	} //toString

	public MathObject getModel() {
		if(cachedObject==null){
			cachedObject = evalModel();
		}//if
		return cachedObject;
	}//getModel
	
	public Object getModelKey(){
		return model.getKey();
	}
	
	public UserFunction getCapsulatedModel(){
		return model;
	}
	
	private MathObject evalModel(){
		try {
		    if(!model.isEncapsulated())
		        return model;
			Object obj = model.eval();
			if(obj instanceof MathObject)
				return (MathObject) obj;
			else return model;
		} catch (MathException e) {
			ExceptionManager.doError("Fehler beim Entkapseln des Objekts "+model.getKey(),e);
			return null;
		}//catch
	}//evalModel

	protected void addListener(JComponent c) {
		c.addMouseListener(new ListMouseListener());
		c.addKeyListener(new ListKeyListener());
	} //addListener

	/**
	 * Wenn die Entfernen-Taste gedrückt wird, soll das zuletzt selektierte
	 * Objekt gelöscht werden. Andernfalls passiert nichts.
	 */
	class ListKeyListener extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() != KeyEvent.VK_DELETE)
				return;
			parent.deleteSelectedItem();
		} //keyReleased
	} //class ListKeyListener

	/**
	 * Wenn ein Item angeklickt wird, soll der Container dieses Element
	 * hervorheben und sich das Element speichern. Darüberhinaus soll ein
	 * Popup kontextbezogen geöffnet werden.
	 */
	class ListMouseListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			parent.selectItem(AbstractListViewItem.this);
		} //mouseClicked

		public void doPopup(MouseEvent evt) {
			if (evt.isPopupTrigger()) {
				exportMenuItem.setVisible(parent.itemHasExport(AbstractListViewItem.this));
				popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
			} //if
		}//doPopup

		public void mousePressed(MouseEvent evt) {
			doPopup(evt);
		}//mousePressed

		public void mouseReleased(MouseEvent evt) {
			doPopup(evt);
		}//mouseReleased

	} //class MouseListener

	public PathStep createPathStep(List methods) {
		throw new NotImplementedException();
	} //createPathStep

	public Class getReturnType(PathStep nextStep) {
		throw new NotImplementedException();
	} //getReturnType

} //class AbstractListViewItem
