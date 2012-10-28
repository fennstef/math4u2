package math4u2.util.swing.print;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PreviewButton extends JButton {
	protected String command;

	public PreviewButton(String name, ActionListener listener) {
		String iconFile = "math4u2/images/printIcons/" + name + "Icon.gif";
		ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(iconFile));
		setIcon(icon);
		setPreferredSize(new Dimension(22, 22));
		setMaximumSize(new Dimension(22, 22));
		setFocusPainted(false);
		addActionListener(listener);
		command = name;
	}//Konstruktor

	public boolean isFocusable() {
		return true;
	}//isFocusable

	public boolean isDefaultButton() {
		return false;
	}//isDefaultButton

	public String getCommand() {
		return command;
	}//getCommand
}