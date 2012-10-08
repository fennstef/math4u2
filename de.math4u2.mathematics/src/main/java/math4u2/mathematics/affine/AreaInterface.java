package math4u2.mathematics.affine;

import java.awt.Color;

/**
 * Objekt mit einer Füllfläche
 * 
 * @author Fenn Stefan
 */
public interface AreaInterface {

    /**
     * Wird die Fläche zur Zeit angezeigt?
     */
    boolean isFillArea();

    /**
     * Füllfarbe holen
     */
    Color getFillColor();

    /**
     * Füllfarbe setzen
     */
    void setFillColor(Color c);
    
    /**
     * Randforabe holen
     */
    Color getBorderColor();

    /**
     * Randfarbe setzen
     */
    void setBorderColor(Color c);

    
}//interface AreaInterface
