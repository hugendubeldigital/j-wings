package ldap;

import java.io.IOException;
import javax.naming.*;
import javax.naming.directory.*;
import javax.swing.*;

import org.wings.*;
import org.wings.externalizer.ExternalizeManager;

public class AttributesCellRenderer
    extends SelectableTableCellRenderer
    implements RequestListener
{
    public SComponent getTableCellRendererComponent(SBaseTable baseTable,
						    Object value,
						    boolean isSelected,
						    int row,
						    int col)
    {
	if (value instanceof Attribute) {
	    Attribute attribute = (Attribute)value;

	    try {
		// special treatment for photos and passwords ..
		if (attribute.get().getClass().getName().equals("[B")) {
		    if (attribute.getID().equals("jpegPhoto")) {
			String src = null;
			ImageIcon icon = new ImageIcon((byte[])attribute.get());

			ExternalizeManager ext = baseTable.getExternalizeManager();
			if (ext != null) {
                          src = ext.externalize(icon);
			}

			if (src != null) {
			    StringBuffer buffer = new StringBuffer();
			    buffer.append("<img src=\"").append(src).append("\"");
			    buffer.append(" width=\"").append(icon.getIconWidth()).append("\"");
			    buffer.append(" height=\"").append(icon.getIconHeight()).append("\"");
			    buffer.append(" border=\"0\" />");
			    value = buffer.toString();
			}
			else
			    value = "-";
		    }
		    else if (attribute.getID().equals("userPassword")) {
			value = "-";
		    }
		}
		// use toString() as default
		else {
		    StringBuffer buffer = new StringBuffer();
		    
		    for (int i=0; i < attribute.size(); i++) {
			if (i > 0)
			    buffer.append(", ");
			buffer.append(attribute.get(i).toString());
		    }

		    value = buffer.toString();
		}
	    }
	    catch (NamingException e) {
		System.err.println(e.getMessage());
		e.printStackTrace(System.err);
	    }

	}
	return super.getTableCellRendererComponent(baseTable, value, isSelected, row, col);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
