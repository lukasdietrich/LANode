package me.lukasdietrich.commons;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

public class Activities {

	public static void center(Window frame) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((int)(screen.getWidth() / 2 - frame.getWidth() / 2),
						  (int)(screen.getHeight() / 2 - frame.getHeight() / 2));
	}
	 
}
