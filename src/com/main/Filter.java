package com.main;

import java.io.File;

class Filter extends javax.swing.filechooser.FileFilter{

   public boolean accept(File file){
   
      String fil = "";
      if (file.getPath().lastIndexOf('.') > 0){
      
         fil = file.getPath().substring(file.getPath().lastIndexOf('.')+1).toLowerCase();

      }

      if (fil != ""){
         return fil.equals("prg");
      }else{
         return file.isDirectory();
      }
      
   }

   public String getDescription(){
      return "PRG files (*.prg)";
   }

}
