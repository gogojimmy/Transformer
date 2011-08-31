package com.util;

import javax.swing.ImageIcon;

public class CreatecdIcon {
	public static ImageIcon add(String ImageName) {
		// URL IconUrl = mainFrame.class.getResource("/" + ImageName);
		// System.out.println("IconUrl :" + IconUrl.toString());
		ImageIcon icon = new ImageIcon(ImageName);
		return icon;
	}
	/*
	 * public static void main(String args[]){ URL IconUrl =
	 * mainFrame.class.getResource("/"); System.out.println("IconUrl :" +
	 * IconUrl); }
	 */
}
