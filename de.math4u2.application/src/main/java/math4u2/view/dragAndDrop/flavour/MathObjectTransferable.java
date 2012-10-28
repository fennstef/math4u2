package math4u2.view.dragAndDrop.flavour;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.util.exception.ExceptionManager;

public class MathObjectTransferable implements Transferable {

	public static DataFlavor mathObjectFlavor;

	public static DataFlavor[] flavors;

	private String key;

	private Broker broker;

	static {
		try {
			mathObjectFlavor = new DataFlavor(MathObject.class,
					"MathObject-Flavor");
		} catch (Exception e) {
			ExceptionManager.doError(e);
		}//catch

		flavors = new DataFlavor[] { mathObjectFlavor };
	} //statischer Konstruktor

	public MathObjectTransferable(String key, Broker broker) {
		this.key = key;
		this.broker = broker;
	} //Konstruktor

	public boolean isDataFlavorSupported(DataFlavor df) {
		return df.equals(mathObjectFlavor);
	} //isDataFlavorSupported

	public Object getTransferData(DataFlavor df)
			throws UnsupportedFlavorException, IOException {
		if (df.equals(mathObjectFlavor)) {
			return new Object[] { key, broker };
		} else
			throw new UnsupportedFlavorException(df);
	} //gettransferData

	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	} //getTransferDataFlavors

} //MathObjectTransferable
