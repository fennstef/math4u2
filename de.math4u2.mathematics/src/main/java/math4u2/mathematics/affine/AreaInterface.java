package math4u2.mathematics.affine;

import java.awt.Color;

/**
 * Objekt mit einer F�llfl�che
 * 
 * @author Fenn Stefan
 */
public interface AreaInterface {

    /**
     * Wird die Fl�che zur Zeit angezeigt?
     */
    boolean isFillArea();

    /**
     * F�llfarbe holen
     */
    Color getFillColor();

    /**
     * F�llfarbe setzen
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
