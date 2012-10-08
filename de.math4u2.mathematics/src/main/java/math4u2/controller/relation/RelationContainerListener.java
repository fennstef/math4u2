package math4u2.controller.relation;

/**
 * Listener, wenn sich ein RelationContainer ge�ndert hat.
 * 
 * @author Fenn Stefan
 */
public interface RelationContainerListener {

	/**
	 * Event, wenn eine Beziehung hinzugef�gt wird.
	 * 
	 * @see RelationContainer#addRelation(RelationInterface)
	 */
	void relationAdded(RelationContainer rc, RelationInterface r);

	/**
	 * Event, wenn eine Beziehung entfernt wird.
	 * 
	 * @see RelationContainer#removeRelation(RelationInterface)
	 */
	void relationRemoved(RelationContainer rc, RelationInterface r);

}//interface RelationContainerListener
