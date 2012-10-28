package math4u2.view.formula;

import java.awt.FlowLayout;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Diese Klasse stellt Bilder in der AtomicBox dar.
 */
public class ImageBox extends AtomicBox {

	private JLabel label;

	/**
	 * Dieser Konstruktor initialsiert sich mit einem URL-String. Fall der
	 * String keine URL ist, wird von dem Classpath ausgehend das Image-File
	 * gesucht
	 * 
	 * @param sourceStr
	 *            String-Name des Bildpfades
	 * @see AtomicBox
	 */
	public ImageBox(String sourceStr) throws FileNotFoundException {
		super(new FormulaRenderContext());
		//try to get URL
		URL sourceURL = null;
		try {
			sourceURL = new URL(sourceStr);
		} catch (MalformedURLException e) {
			//wird später gefangen
		}//catch

		if (sourceURL == null)
			sourceURL = ClassLoader.getSystemResource(sourceStr);

		//get Image
		if (sourceURL == null) {
			throw new FileNotFoundException("Das Bild " + sourceStr
					+ " wurde nicht gefunden");
		} else {
			//if URL
			init(new ImageIcon(sourceURL));
		}//else
	} //Konstruktor

	private void init(ImageIcon image) {
	    setLayout(new FlowLayout());
		label = new JLabel(image);
		add(label);
	} //init

	/**
	 * Überschriebene Methode, um das Element vertikal zu zentrieren.
	 * @see math4u2.view.formula.FormulaElement#getBaseline()
	 */
	public float getBaseline() {
		return (float)super.getHeight()/2f;
	}
	
} //ImageBox
