package ldap.editors;

import java.util.*;
import java.io.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.swing.ImageIcon;

import org.wings.*;
import org.wings.session.*;


public class JpegPhotoEditor
  implements Editor
{
  
  BasicAttribute oldAttribute;
  
  public SComponent createComponent(Attributes attributes) {
    
      SPanel photoPanel = new SPanel();
      SFileChooser chooser = new SFileChooser();
      photoPanel.setLayout(new SFlowDownLayout());
      photoPanel.add(chooser);
      photoPanel.add(new SLabel());
      return photoPanel;
    }
  
    public void setValue(SComponent component, Attribute attribute)
	throws NamingException
    {
      this.oldAttribute = (BasicAttribute)attribute;

      SPanel panel = (SPanel)component;
      SLabel photo = (SLabel)panel.getComponent(1);
      if (attribute == null) {
        photo.setText("kein Photo vorhanden");
        return;
      }
      SImageIcon icon = new SImageIcon(new
        ImageIcon((byte[])attribute.get()));
      photo.setIcon(icon);
    }
  
  public Attribute getValue(SComponent component, String id) {
        
    SPanel panel = (SPanel)component;
    SLabel photo = (SLabel)panel.getComponent(1);
    SFileChooser chooser = (SFileChooser)panel.getComponent(0);
    Attribute attribute = new BasicAttribute(id);
    try {
      if (chooser.getSelectedFile()!=null) {
        FileInputStream fis = new FileInputStream(chooser.getSelectedFile());
        
        int bytesNr = fis.available();
        byte [] b = new byte[bytesNr];
        
        fis.read(b);
        fis.close();
        photo.setIcon(new SImageIcon(new ImageIcon(b)));
        attribute.add(b);
        return attribute;
      }
    }
    catch(java.io.IOException e ) {
      e.printStackTrace();
    }
    System.out.println("old" + oldAttribute);
    return oldAttribute;
  }
}



