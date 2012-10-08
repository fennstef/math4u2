package math4u2.parser.importdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.DualVectorDoubleResult;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.string.StringUtil;
import math4u2.view.Formatierer;

/**
 * Importiert Matrizen oder Vektoren, die mit Whitespace getrennt sind. Die
 * Zeilen müssen durch Newline oder Carriage Return getrennt sein.
 * 
 * @author Fenn Stefan
 */
public class WhitespaceSeperated implements ImportData {

	private static JFileChooser fc = new JFileChooser();

	public static boolean allwaysMatrixResult = false;

	public String importFromStream(InputStream is)
			throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		LinkedList lines = new LinkedList();
		String line = "";
		int lineCounter = 0;

		try {
			while ((line = br.readLine()) != null) {
				String[] cellArray = line.trim().split("\\s+");
				String tmp = "{" + StringUtil.implode(cellArray, ", ") + "}";
				lines.add(tmp);
				lineCounter++;
			}//while
		} catch (IOException e) {
			br.close();
			throw new IOException();
		} finally {
			br.close();
		}//finally

		if (lineCounter == 1 && !allwaysMatrixResult)
			return (String) lines.getFirst();
		return "{"
				+ StringUtil.implode((String[]) lines.toArray(new String[0]),
						", ") + "}";
	}//importFromStream

	public void setAllwaysMatrixResult(boolean isMatrix) {
		allwaysMatrixResult = isMatrix;
	}//setAllwaysMatrixResult

	public boolean isAllwaysMatrixResult() {
		return allwaysMatrixResult;
	}//isallwaysMatrixResult

	public String importFromStream(String filename, String objectName, JFrame frame)
			throws FileNotFoundException, IOException {
		//System.out.println( " importFromStream: " + filename );
		File file = new File(filename);
		if (!file.exists() || !file.isFile()) {
			if (filename != null && !filename.equals("")) {
				JOptionPane
						.showMessageDialog(
								frame,
								"Die Datei "
										+ filename
										+ " ist nicht vorhanden.\nWählen Sie eine andere Datei aus.",
								"Datei nicht vorhanden",
								JOptionPane.WARNING_MESSAGE);
			}//if
			fc.setDialogTitle("Import für das Objekt " + objectName);
			int returnVal = fc.showOpenDialog(frame);

			if (returnVal != JFileChooser.APPROVE_OPTION) {
				return "";
			}//if
			file = fc.getSelectedFile();
			if (!file.exists()) {
				throw new FileNotFoundException("Die Datei "
						+ file.getAbsolutePath() + " wurde nicht gefunden.");
			}//if
			return importFromStream(new FileInputStream(file));
		}//if
		return importFromStream(new FileInputStream(file));
	}//importFromStream

	public static boolean canExport(MathObject obj) {
		if (obj instanceof UserFunction) {
			try {
				Object result = ((UserFunction) obj).eval();
				if (result instanceof MatrixDoubleResult)
					return true;
			} catch (MathException e) {
				return false;
			}
		}//if
		return false;
	}//canExport

public static void exportData(MathObject obj, JFrame frame){
		if(!(obj instanceof UserFunction)){
			ExceptionManager.doError("Daten können nicht exportiert werden. (Objekt "+obj.getKey()+" ist vom Typ "+obj.getClass()+")");
			return;
		}//if
		
		Object result = null;
		try {
			result = ((UserFunction)obj).eval();
		} catch (MathException e) {
			ExceptionManager.doError("Fehler beim Evaluieren der Funktion "+obj.getKey(),e);
			return;
		}//catch
		
		if(!(result instanceof MatrixDoubleResult)){
			ExceptionManager.doError("Daten können nicht exportiert werden. (Daten sind in der Form "+result.getClass()+")");
			return;			
		}//if
		
		fc.setDialogTitle("Export des Objekts "+obj.getKey());
		int returnVal = fc.showSaveDialog(frame);

		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}//if
		
		File file = fc.getSelectedFile();
		
        //Falls die Datei schon existiert
        if (file.exists()) {
            Object[] options = { "OK", "Abbrechen" };
            int i = JOptionPane.showOptionDialog(null,
                    "Soll die Datei überschrieben werden?",
                    "Die Datei existiert bereits",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if (i != 0) {
                return;
            }
        }// if file exists

		MatrixDoubleResult mdr = (MatrixDoubleResult) result;
		double[][] m = mdr.valueArray;
		StringBuffer sb = new StringBuffer();
		for(int s=0; s<m.length; s++){
			if(s!=0){
				if(mdr instanceof DualVectorDoubleResult){
					sb.append('\t');
				}else{
					sb.append('\n');
				}//else
			}//if
			for(int z=0; z<m[s].length; z++){
				if(z!=0)
					sb.append('\t');
				sb.append(Formatierer.fullValue2String(m[s][z]));
			}//for z
		}//for s

		FileWriter fw=null;
		try {
			fw = new FileWriter(file);
			fw.write(sb.toString());
			
		} catch (IOException e1) {
			ExceptionManager.doError("Fehler beim Schreiben der Datei "+file.getAbsolutePath(),e1);
		} finally {
			if(fw!=null){
				try {
					fw.close();
				} catch (IOException e2) {
					ExceptionManager.doError(e2);
				}//catch
			}//if
		}//finally
		
		JOptionPane.showMessageDialog(frame, "Die Daten wurden erfolgreich in die Datei "+file.getAbsolutePath()+" exportiert.",
				"Daten wurden exportiert", JOptionPane.INFORMATION_MESSAGE);
	}//exportData

}//class WhitespaceSeperated
