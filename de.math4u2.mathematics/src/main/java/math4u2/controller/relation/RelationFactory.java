package math4u2.controller.relation;

/**
 * Erzeugt Beziehungen mit verschieden Konfigurationen.
 * 
 * @author Fenn Stefan
 */
public class RelationFactory {

	/**
	 * Erzeugt eine Canvas-View-Beziehung. <br>
	 * Z.B. Zeichenfläche-Graph, Textfeld-Formel oder ListBox-ListView sind
	 * typische Beziehungsbeispiele für diesen Beziehungstyp.
	 */
	public static RelationInterface getCanvas_View_Relation() {
		return new Relation("Canvas-View Relation", RoleFactory
				.getDeleteAlwaysRecursiveRole(Role.NO_RENEW,
						Role.CREATES_NOT_PARTNER), RoleFactory
				.getDeleteAlwaysRole(Role.RENEW, Role.CREATES_NOT_PARTNER));
	} //RelationInterface getCanvas_Vie...

	/**
	 * Erzeugt eine View-Funktion-Beziehung. <br>
	 * Z.B. Graph-Funktion oder Textfeld-Funktion sind typische
	 * Beziehungsbeispiele für diesen Beziehungstyp.
	 */
	public static RelationInterface getView_Function_Relation() {
		return new Relation("View-Function Relation", RoleFactory
				.getDeleteAlwaysRole(Role.NO_RENEW, Role.CREATES_NOT_PARTNER),
				RoleFactory.getDeleteDependenceRecursiveRole(Role.RENEW,
						Role.CREATES_NOT_PARTNER));
	} //RelationInterface getView_Funct...

	/**
	 * Erzeugt eine Funktion-Unterfunktion-Beziehung. <br>
	 * Z.B. f(x):=g(x) + 2 ist ein typisches Beziehungsbeispiel für diesen
	 * Beziehungstyp.
	 */
	public static RelationInterface getFunction_SubFunction_Relation() {
		return new Relation("Function-SubFunction Relation", RoleFactory
				.getDeleteAlwaysRole(Role.NO_RENEW, Role.CREATES_NOT_PARTNER),
				RoleFactory.getDeleteNeverRole(Role.RENEW,
						Role.CREATES_NOT_PARTNER));
	} //RelationInterface getFunction_S...

	/**
	 */
	public static RelationInterface getIndex_Function_Relation() {
		return new Relation("Index-Function Relation", RoleFactory
				.getDeleteAlwaysRole(Role.NO_RENEW, Role.CREATES_NOT_PARTNER),
				RoleFactory.getDeleteNeverRole(Role.NO_RENEW,
						Role.CREATES_NOT_PARTNER));
	} //RelationInterface getFunction_S...

	/**
	 * Erzeugt eine Part-Of-Beziehung. <br>
	 * Z.B. sind beim Punkt p die Koordinaten p.x und p.y typische
	 * Beziehungsbeispiele für diesen Beziehungstyp.
	 */
	public static RelationInterface getPart_Of_Relation() {
		return new Relation("Part-Of-Relation", RoleFactory
				.getDeleteDependenceRecursiveRole(Role.NO_RENEW,
						Role.CREATES_PARTNER), RoleFactory.getDeleteNeverRole(
				Role.RENEW, Role.CREATES_NOT_PARTNER));
	} //RelationInterface getPart_Of_Re...

	/**
	 * Erzeugt eine Zeichenfläche-Punkt-Beziehung.
	 */
	public static RelationInterface getDrawArea_Point_Relation() {
		return new Relation("DrawArea-Point Relation", RoleFactory
				.getDeleteAlwaysRecursiveRole(Role.NO_RENEW,
						Role.CREATES_NOT_PARTNER), RoleFactory
				.getDeleteAlwaysRole(Role.NO_RENEW, Role.CREATES_NOT_PARTNER));
	} //RelationInterface getDrawArea_P...

	/**
	 * Erzeugt eine Funktion-ListView-Beziehung.
	 */
	public static RelationInterface getFunction_ListView_Relation() {
		return new Relation("Function-ListView Relation",
				RoleFactory.getDeleteAlwaysRecursiveRole(Role.RENEW,
						Role.CREATES_PARTNER), RoleFactory
						.getDeleteDependenceRecursiveRole(Role.RENEW,
								Role.CREATES_NOT_PARTNER));
	} //RelationInterface getFunction_L...

} //class RelationFactory
