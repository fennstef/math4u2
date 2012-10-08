package math4u2.controller.relation;

/**
 * Bei einer Beziehung ist RoleInterface eine Seite der beiden Partner. <br>
 * Diese können unterschiedlich sein.
 * 
 * @author Fenn Stefan
 * @see RelationInterface
 */
public class Role implements RoleInterface {

	private boolean receivesRenew;

	private boolean deletesPartner;

	private int isDeleteable;

	private boolean createsPartner;

	private String roleName = null;

	/** Kurzname, der Rolle z.B. p.x => wäre hier x */
	private String shortName;

	public Role(String roleName, boolean receivesRenew, int isDeleteable,
			boolean deletesPartner, boolean createsPartner) {
		this.roleName = roleName;
		this.receivesRenew = receivesRenew;

		if ((isDeleteable == DELETE_YES)
				|| (isDeleteable == DELETE_ASK_PARTNER)
				|| (isDeleteable == DELETE_NO)) {
			this.isDeleteable = isDeleteable;
		} else
			throw new IllegalArgumentException(
					"isDeleteable wurde falsch initialisiert");

		this.deletesPartner = deletesPartner;
		this.createsPartner = createsPartner;
	} //Konstruktor

	/**
	 * @see RoleInterface#receivesRenew()
	 */
	public boolean receivesRenew() {
		return receivesRenew;
	} //receivesRenew

	/**
	 * @see RoleInterface#isDeleteable()
	 */
	public int isDeleteable() {
		return isDeleteable;
	} //isDeletable

	/**
	 * @see RoleInterface#deletesPartner()
	 */
	public boolean deletesPartner() {
		return deletesPartner;
	} //deletesPartner

	/**
	 * @see RoleInterface#createsPartner()
	 */
	public boolean createsPartner() {
		return createsPartner;
	} //createsPartner

	/**
	 * Zwei Rollen sind gleich, wenn sie bei receivesRenew(), isDeleteable() und
	 * deletesPartner() das gleiche liefern.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Role))
			return false;
		Role tmp = (Role) o;
		if (receivesRenew != tmp.receivesRenew)
			return false;
		if (deletesPartner != tmp.deletesPartner)
			return false;
		if (isDeleteable != tmp.isDeleteable)
			return false;
		return true;
	} //equals

	public String toString() {
		if (roleName != null)
			return roleName;
		String delStr = "'ask Partner'";
		if (isDeleteable == DELETE_YES)
			delStr = "true";
		if (isDeleteable == DELETE_NO)
			delStr = "false";
		return "Role(renew:" + receivesRenew + ", delete?:" + delStr
				+ ", del Partner:" + deletesPartner + ")";
	} //toString

	/**
	 * @see math4u2.controller.relation.RoleInterface#setShortName(java.lang.String)
	 */
	public void setShortName(String name) {
		shortName = name;
	} //setShortName

	/**
	 * @see math4u2.controller.relation.RoleInterface#getShortName()
	 */
	public String getShortName() {
		return shortName;
	} //getShortName

} // class Role
