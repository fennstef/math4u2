package math4u2.exercises.ui;

/**
 * @author lichten
 */
public class ETreeNodeInfo {

	private Object obj;

	private String title;

	public ETreeNodeInfo(Object obj, String title) {
		this.obj = obj;
		this.title = title;
	}

	public Object getObject() {
		return obj;
	}

	public String getTitle() {
		return title;
	}

	public String toString() {
		return title;
	}
}