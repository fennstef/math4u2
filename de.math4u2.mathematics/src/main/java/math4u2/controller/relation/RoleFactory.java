package math4u2.controller.relation;

/**
 * Erzeugt eine Rolle einer Beziehung.
 * 
 * @author Fenn Stefan
 * @see Role
 */
public class RoleFactory {

	/**
	 * Löscht sich Abhängigkeit vom Partner und löscht den Partner mit.
	 */
	public static RoleInterface getDeleteDependenceRecursiveRole(
			boolean isRenewable, boolean createsPartner) {
		return new Role("Dependence-Rec-Role", isRenewable,
				Role.DELETE_ASK_PARTNER, true, createsPartner);
	} //getDependenceRecursiveRole

	/**
	 * Löscht sich nie.
	 */
	public static RoleInterface getDeleteNeverRole(boolean isRenewable,
			boolean createsPartner) {
		return new Role("Never-Role", isRenewable, Role.DELETE_NO, false,
				createsPartner);
	} //getNeverRole

	/**
	 * Löscht sich immer und löscht den Partner mit.
	 */
	public static RoleInterface getDeleteAlwaysRecursiveRole(
			boolean isRenewable, boolean createsPartner) {
		return new Role("Always-Rec-Role", isRenewable, Role.DELETE_YES, true,
				createsPartner);
	} //getAlwaysRecursiveRole

	/**
	 * Löscht sich immer.
	 */
	public static RoleInterface getDeleteAlwaysRole(boolean isRenewable,
			boolean createsPartner) {
		return new Role("Always-Role", isRenewable, Role.DELETE_YES, false,
				createsPartner);
	} // getAlwaysRole

} //class RoleFactory
