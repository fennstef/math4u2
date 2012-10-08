package math4u2.controller.relation;

/**
 * Erzeugt eine Rolle einer Beziehung.
 * 
 * @author Fenn Stefan
 * @see Role
 */
public class RoleFactory {

	/**
	 * L�scht sich Abh�ngigkeit vom Partner und l�scht den Partner mit.
	 */
	public static RoleInterface getDeleteDependenceRecursiveRole(
			boolean isRenewable, boolean createsPartner) {
		return new Role("Dependence-Rec-Role", isRenewable,
				Role.DELETE_ASK_PARTNER, true, createsPartner);
	} //getDependenceRecursiveRole

	/**
	 * L�scht sich nie.
	 */
	public static RoleInterface getDeleteNeverRole(boolean isRenewable,
			boolean createsPartner) {
		return new Role("Never-Role", isRenewable, Role.DELETE_NO, false,
				createsPartner);
	} //getNeverRole

	/**
	 * L�scht sich immer und l�scht den Partner mit.
	 */
	public static RoleInterface getDeleteAlwaysRecursiveRole(
			boolean isRenewable, boolean createsPartner) {
		return new Role("Always-Rec-Role", isRenewable, Role.DELETE_YES, true,
				createsPartner);
	} //getAlwaysRecursiveRole

	/**
	 * L�scht sich immer.
	 */
	public static RoleInterface getDeleteAlwaysRole(boolean isRenewable,
			boolean createsPartner) {
		return new Role("Always-Role", isRenewable, Role.DELETE_YES, false,
				createsPartner);
	} // getAlwaysRole

} //class RoleFactory
