package org.wings.plaf.xhtml.css1;

import java.awt.Color;
import java.io.IOException;
import javax.swing.ListModel;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class ListCG
    extends org.wings.plaf.xhtml.ListCG
{
    /*
    public void write(Device d, SComponent c)
        throws IOException
    {
	SList list = (SList)c;
	ListModel model = list.getModel();
	int visibleRows = list.getVisibleRowCount();
	int size = list.getSize();
	Style style = list.getStyle();
	SListCellRenderer cellRenderer = list.getCellRenderer();
	
	d.append("<select name=\"");
	d.append(list.getNamePrefix());
	d.append("\"");
	Utils.writeStyleAttribute(d, style);
	
	d.append(" size=\"").append(visibleRows);
	d.append("\"");
	
	if (list.getSelectionMode() == SConstants.MULTIPLE_SELECTION)
	    d.append(" multiple=\"multiple\"");
	
	if (list.getSubmitOnChange()) 
	    d.append(" onChange=\"submit()\"");
	
	d.append(">\n");
	
	if (model != null) {
	    for (int i=0; i < model.getSize(); i++) {
		Object o = list.getElementAt(i);
		boolean selected = list.isSelectedIndex(i);
		
		d.append("<option value=\"").append(i).append("\"");
		if (selected)
		    d.append(" selected=\"selected\"");
		d.append(">");
		
		d.append(cellRenderer.getListCellRendererComponent(list, o, selected, i));
		
		d.append("\n");
	    }
	}
	
	d.append("</select>\n");
	
	writeHiddenComponent(d, list);
    }
    */
}
