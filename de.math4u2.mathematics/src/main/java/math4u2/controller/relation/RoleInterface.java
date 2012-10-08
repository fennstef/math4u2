package math4u2.controller.relation;

/**
 * Bei einer Beziehung ist RoleInterface eine Seite der beiden Partner. <br>
 * Diese k�nnen unterschiedlich sein.
 * 
 * @author Fenn Stefan
 * @see RelationInterface
 */
public interface RoleInterface {

	/** bei isDeleteable ja zur�ck geben */
	final static int DELETE_YES = 1;

	/** bei isDeleteable nein zur�ck geben */
	final static int DELETE_NO = 2;

	/** bei isDeleteable Partner fragen */
	final static int DELETE_ASK_PARTNER = 3;

	/** erzeugt den Partner bei der Initialisierung */
	final static boolean CREATES_PARTNER = true;

	/** erzeugt nicht den Partner bei der Initialisierung */
	final static boolean CREATES_NOT_PARTNER = false;

	/** es wird das Renew weitergeschickt */
	final static boolean RENEW = true;

	/** es wird das Renew nicht weitergeschickt */
	final static boolean NO_RENEW = false;

	/**
	 * zeigt an, ob diese Rolle ein Renew beziehen will
	 */
	boolean receivesRenew();

	/**
	 * berechnet, ob diese Rolle sich von partner l�schen l��t.
	 * 
	 * @param partner
	 *            Objekt, dass auf der anderen Seite der Beziehung steht.
	 */
	int isDeleteable();

	/**
	 * gibt an, ob diese Rolle beim L�schvorgang den Partner mitl�schen w�rde.
	 */
	boolean deletesPartner();

	/**
	 * Gibt an, der Partner bei der Initialisierung sofort miterzeugt wird.
	 */
	boolean createsPartner();

	/** Setzt den ShortName z.B. p.x w�re hier x */
	void setShortName(String name);

	/** ShortName bekommen */
	String getShortName();

}//interface RoleInterface
