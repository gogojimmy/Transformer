package com.main;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class Transformer{

   public static void main (String args[]){
	   
      Frame frame = new Frame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(500,500);
      frame.setVisible(true);
      frame.setLocationRelativeTo(null);
      frame.setResizable(false);
   }
}
