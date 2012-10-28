package math4u2.exercises.scripting;

/**
 * Animierbares Element
 * 
 * @author Fenn Stefan
 */
public interface EAction {

    float getBegin();

    float getEnd();

    float getDuration();

    /**
     * Wird aufgerufen, bevor die Animation gestartet wird.
     */
    void beforeFirstRun(long timeMillis);

    /**
     * Es wird diese Methode in jedem Schritt der Animation aufgerufen.
     */
    void doRun(long timeMillis);

    /**
     * Wird aufgerufen, nachdem die Animation abgelaufen ist.
     * 
     * @param timeMillis
     */
    void afterLastRun(long timeMillis);

    /**
     * Gibt Auskunft darüber, ob diese Action gerade läuft.
     * 
     * @return
     */
    boolean isRunning();
    
    /**
     * Gibt die Startzeit der Animation zurück
     */
    long getStartTime();

	/**
	 * Zurücksetzen, damit wieder die Aktion von vorne ablaufen kann.
	 */
	void reinit();
}//interface EAction
