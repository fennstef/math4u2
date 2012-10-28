package math4u2.view.gui.wizard.title;


public class HasTitleWrapper implements HasTitle {
	private HasTitle title;
	
	public void setTitle(HasTitle title){
		this.title = title;
	}//setTitle
	
	public String getTitle(){
		if(title==null) return "";
		else return title.getTitle();
	}//getTitle
}//class HasTitleWrapper
