package math4u2.view.gui.wizard.title;


public class SimpleHasTitle implements HasTitle {
	public static HasTitle EMPTY_TITLE = new SimpleHasTitle("");
	
	private String title;
	
	public SimpleHasTitle(String title){
		this.title = title;
	}//Konstruktor
	
	public String getTitle(){
		return title;
	}//getTitle
}//class SimpleHasTitle
