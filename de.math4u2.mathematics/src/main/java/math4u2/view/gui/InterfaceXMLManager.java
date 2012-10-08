package math4u2.view.gui;


/**
 * Hilfsklasse die beim XML-parsen unterstützt
 * 
 * @author Fenn Stefan
 * @author Michael Lichtenstern
 */
public interface InterfaceXMLManager {
    /**
     * Refresh ueber alle lokalen Dateien (xml- und temp-Verzeichnis), der
     * ServerContents-Dateien, sowie anschliessendem anzeigen
     */
    public void showRefreshFull();

    public void refreshFull(boolean showFrame);

    /**
     * Refresh des Server XML-Ordner
     */
    public void serverRefresh(String url);

 
    /**
     * Loescht alle Inhalte des temp-Verzeichnis.
     */
    public void deleteTempContents();

      /**
     * @return Returns the servernews.
     */
    public String getServernews();
    /**
     * @param servernews The servernews to set.
     */
    public  void setServernews(String servernews);
}