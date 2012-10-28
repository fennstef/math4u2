package math4u2.view.gui.wizard.components;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import math4u2.application.resource.Images;
import math4u2.util.swing.LabelAsButtonMouseListener;
import math4u2.view.layout.TableLayout;

public class WizMatrix extends JPanel implements CanModifyText {

	private static final int VISIBLE_ROWS = 5;
	private static final int VISIBLE_COLS = 5;
	
	private String[][] values;

	private CellField[][] field = new CellField[VISIBLE_ROWS][VISIBLE_COLS];
	
	private int curRowIndex = 1, curColIndex = 1;
	private JTextField curRowIndexField, curColIndexField;
	private JLabel[] rowsTitle = new JLabel[VISIBLE_ROWS];
	private JLabel[] colsTitle = new JLabel[VISIBLE_COLS];
	
	private List changeListeners = new LinkedList();
	
	private JLabel left, right, up, down;
	
	private JPanel indexPanel;

	public WizMatrix() {
		super(null);
		init();
	}// Konstruktor

	public void setMatrixDimension(int cols, int rows) {
		curRowIndex=1;
		curColIndex=1;
		values = new String[cols][rows];
		for(int i=0; i<values.length; i++)
			for(int j=0; j<values[i].length; j++)
				values[i][j] = "0";
				
		curColIndexField.setToolTipText("Zahl zwischen 1 und "+ (values[0].length-VISIBLE_COLS+1) +" eingeben." );
		curRowIndexField.setToolTipText("Zahl zwischen 1 und "+ (values.length-VISIBLE_ROWS+1) +" eingeben." );
		
		updateAll(this);
		fireChange();
	}// setMatrixDimension
	
	private void init(){
		for(int i=0; i<field.length; i++){
			for(int j=0; j<field[i].length; j++){
				field[i][j] = new CellField(i,j);
			}//for j
		}//for i
		
		left = new JLabel(Images.MATRIX_ARROW_LEFT);
		left.addMouseListener(new LabelAsButtonMouseListener(left));
		left.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				if(!isValidCurColIndex(curColIndex-1))
					return;
				curColIndex--;
				updateAll(left);
			}
		});

		right = new JLabel(Images.MATRIX_ARROW_RIGHT);
		right.addMouseListener(new LabelAsButtonMouseListener(right));
		right.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				if(!isValidCurColIndex(curColIndex+1))
					return;
				curColIndex++;
				updateAll(right);
			}
		});
		
		up = new JLabel(Images.MATRIX_ARROW_UP);
		up.addMouseListener(new LabelAsButtonMouseListener(up));
		up.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				if(!isValidCurRowIndex(curRowIndex-1))
					return;
				curRowIndex--;
				updateAll(up);
			}
		});
		
		down = new JLabel(Images.MATRIX_ARROW_DOWN);
		down.addMouseListener(new LabelAsButtonMouseListener(down));
		down.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				if(!isValidCurRowIndex(curRowIndex+1))
					return;
				curRowIndex++;
				updateAll(down);
			}
		});		
		
		curRowIndexField = new JTextField(8);
		curRowIndexField.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent event){
				try{
					int row = Integer.parseInt(curRowIndexField.getText());
					if(!isValidCurRowIndex(row))
						return;
					curRowIndex = row;
					updateAll(curRowIndexField);
				}catch(NumberFormatException nfe){}
			}
		});
		
		curColIndexField = new JTextField(8);
		curColIndexField.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent event){
				try{
					int col = Integer.parseInt(curColIndexField.getText());
					if(!isValidCurColIndex(col))
						return;
					curColIndex = col;
					updateAll(curColIndexField);
				}catch(NumberFormatException nfe){}
			}
		});
		
		//TableLayout
		double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
		
		//X-Layout
		// Berechnung der Zellen:
		// border + rowsTitle + pfeil + Matrixzellen mit border + 
		// pfeil + border
		double[] xSize = new double[1+1+1+VISIBLE_COLS + VISIBLE_COLS-1 + 1 + 1];
		int index = 0;
		xSize[index++] = border;
		xSize[index++] = P;
		xSize[index++] = P;
		for(int i=0; i<VISIBLE_COLS; i++){
			if(i!=0)
				xSize[index++] = border;
			xSize[index++] = F;			
		}
		xSize[index++] = P;
		xSize[index++] = border;
		
		//Y-Layout
		// Berechnung der Zellen:
		// border + akt. Indexanzeige + border + colsTitle + pfeil
		// Matrixzellen mit border + pfeil + border
		double[] ySize = new double[1+1+1+1+1+ VISIBLE_ROWS + VISIBLE_ROWS-1 + 1 + 1];
		index=0;
		ySize[index++] = border;
		ySize[index++] = P;
		ySize[index++] = border;
		ySize[index++] = P;
		ySize[index++] = P;
		for(int i=0; i<VISIBLE_COLS; i++){
			if(i!=0)
				ySize[index++] = border;
			ySize[index++] = P;			
		}
		ySize[index++] = P;
		ySize[index++] = border;
		
		double size[][] = { xSize, ySize};

		setLayout(new TableLayout(size));
	
		Font font = (new JLabel()).getFont();
		Font newFont = new Font(font.getName(), Font.BOLD, font.getSize());
		
		//RowsTitle
		for(int i=0; i<rowsTitle.length; i++){
			rowsTitle[i]= new JLabel((i+1)+"");			
			rowsTitle[i].setFont(newFont);
			add(rowsTitle[i], "1, "+(5+2*i)+", C,C");
		}//for i

		add(up, "1,3, F,F");
		add(down, "1, "+(5+2*rowsTitle.length-1)+", F,F");
		
		//ColsTitle
		for(int i=0; i<colsTitle.length; i++){
			JComponent comp;
			colsTitle[i]= new JLabel((i+1)+"", JLabel.CENTER);
			colsTitle[i].setFont(newFont);
			if(i==0){
				JPanel panel = new JPanel(null);
				panel.setLayout(new TableLayout(new double[][]{{F,P,F},{F}}));
				panel.add(left, "0,0, L,C");
				panel.add(colsTitle[i], "1,0, C, C");
				comp = panel;
			}else if(i==colsTitle.length-1){
				JPanel panel = new JPanel(null);
				panel.setLayout(new TableLayout(new double[][]{{F,P,F},{F}}));
				panel.add(right, "2,0, R,C");
				panel.add(colsTitle[i], "1,0, C, C");
				comp = panel;				
			}else{
				comp = colsTitle[i];
			}
			add(comp, (3+2*i)+" 3, F,C");
		}//for i	
		
		//Sichtbare Matrix
		for(int i=0; i<field.length; i++){
			for(int j=0; j<field[i].length; j++){
				add((JComponent)field[i][j], (3+2*j)+", "+(5+2*i)+", F, F");
			}//for j			
		}//for i
		
		//Index-Anzeige
		indexPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		indexPanel.add(new JLabel("Index: ["));
		indexPanel.add(curRowIndexField);
		indexPanel.add(new JLabel(", "));
		indexPanel.add(curColIndexField);
		indexPanel.add(new JLabel("]"));
		add(indexPanel, "1,1,12,1");
	}//init
	
	public void updateAll(JComponent source){
		indexPanel.setVisible(VISIBLE_ROWS<values.length || VISIBLE_COLS<values[0].length);
		up.setVisible(VISIBLE_ROWS<values.length);
		down.setVisible(VISIBLE_ROWS<values.length);
		left.setVisible(VISIBLE_COLS<values[0].length);
		right.setVisible(VISIBLE_COLS<values[0].length);
		
		if(source!=curRowIndexField)
			curRowIndexField.setText(curRowIndex+"");
		if(source!=curColIndexField)
			curColIndexField.setText(curColIndex+"");
				
		for(int i=0; i<colsTitle.length; i++){
			colsTitle[i].setVisible(isValidColIndex(i+1));
			colsTitle[i].setText((curColIndex+i)+"");
		}//for i
		right.setVisible(isValidColIndex(curColIndex+colsTitle.length));
		left.setVisible(isValidColIndex(curColIndex-1));
		
		for(int i=0; i<rowsTitle.length; i++){
			rowsTitle[i].setVisible(isValidRowIndex(i+1));
			rowsTitle[i].setText((curRowIndex+i)+"");
		}//for i		
		up.setVisible(isValidRowIndex(curRowIndex-1));
		down.setVisible(isValidRowIndex(curRowIndex+rowsTitle.length));
		
		for(int i=0; i<field.length; i++){
			for(int j=0; j<field[i].length; j++){
				field[i][j].setVisible(isValidIndex(i+1, j+1));
				if(field[i][j]!= source)
					field[i][j].updateText();				
			}//for j
		}//for i
	}//updateAll
	
	public void fireChange(){
		for (Iterator iter = changeListeners.iterator(); iter.hasNext();) {
			ActionListener element = (ActionListener) iter.next();
			element.actionPerformed(null);
		}//for iter
	}//fireChange
	
	private boolean isValidCurRowIndex(int row){
		if(row<1) return false;
		if(row-1> values.length-VISIBLE_ROWS) return false;
		return true;
	}//isVaidCurRowIndex
	
	private boolean isValidCurColIndex(int col){
		if(col<1) return false;
		if(col-1> values[0].length-VISIBLE_COLS) return false;
		return true;
	}//isVaidCurColIndex
	
	private boolean isValidIndex(int row, int col){
		if(!isValidRowIndex(row)) return false;
		if(!isValidColIndex(col)) return false;
		return true;
	}//isValidIndex
	
	private boolean isValidRowIndex(int row){
		if(row<1) return false;
		if(row-1 >= values.length) return false;
		return true;
	}//isValidRowIndex

	private boolean isValidColIndex(int col){
		if(col<1) return false;
		if(col-1 >= values[0].length) return false;		
		return true;
	}//isValidColIndex
	
	public String getText() {
		if(values==null) return "";
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		for(int i=0; i<values.length; i++){
			if(i!=0) 
				buffer.append(", ");
			buffer.append("{");	
			for(int j=0; j<values[i].length; j++){
				if(j!=0)
					buffer.append(",");
				buffer.append(values[i][j]);
			}//for j
			buffer.append("}");			
		}//for i
		buffer.append("}");
		return buffer.toString();
	}//getText

	public void setText(String text) {
	}

	public void addChangeListener(Object listener) {
		changeListeners.add(listener);
	}//addChangeListener

	public void addExitListener(Object listener) {
	}

	public void addFinishListener(Object listener) {
	}

	public void addFocusListener(FocusListener listener) {
	}
	
	class CellField extends JTextField{
		private int row, col;
		
		public CellField(int row, int col){
			this.row = row;
			this.col = col;
			
			addKeyListener(new KeyAdapter(){
				public void keyReleased(KeyEvent event){
					int rowIndex = curRowIndex + CellField.this.row;
					int colIndex = curColIndex + CellField.this.col;
					if(!isValidIndex(rowIndex, colIndex))
						return;
					values[rowIndex-1][colIndex-1] = getText();
					fireChange();
				}//keyReleased
			});			
		}//Konstruktor
		
		public void updateText(){
			int rowIndex = curRowIndex + row;
			int colIndex = curColIndex + col;
			if(!isValidIndex(rowIndex, colIndex))
				return;
			setText(values[rowIndex-1][colIndex-1]);
		}//updateText
	}//class CellFIeld
		
}// class WizMatrix
