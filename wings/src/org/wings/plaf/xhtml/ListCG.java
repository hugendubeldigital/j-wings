package org.wings.plaf.xhtml;

import java.awt.Color;
import java.io.IOException;
import javax.swing.ListModel;

import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class ListCG implements org.wings.plaf.ListCG
{
    private final static String propertyPrefix = "List";
    
    protected String getPropertyPrefix() {
        return propertyPrefix;
    }
    
    public void installCG(SComponent component) {
	SList list = (SList)component;
	component.setStyle(component.getSession().getCGManager().getStyle(propertyPrefix + ".style"));
	list.add(new SCellRendererPane());
	installCellRenderer(list);
    }
    public void uninstallCG(SComponent component) {
	SList list = (SList)component;
	SCellRendererPane rendererComponent = getCellRendererPane(list);
	list.remove(rendererComponent);
	uninstallCellRenderer(list);
    }
    
    protected void installCellRenderer(SList list) {
	SListCellRenderer cellRenderer = list.getCellRenderer();
	if (cellRenderer == null || cellRenderer instanceof SDefaultListCellRenderer) {
	    cellRenderer = new SDefaultListCellRenderer();
	    configureCellRenderer(list, (SDefaultListCellRenderer)cellRenderer);
	    list.setCellRenderer(cellRenderer);
	}
    }
    protected void uninstallCellRenderer(SList list) {
	SListCellRenderer cellRenderer = list.getCellRenderer();
	if (cellRenderer != null && cellRenderer instanceof SDefaultListCellRenderer)
	    list.setCellRenderer(null);
    }
    
    protected void configureCellRenderer(SList list, SDefaultListCellRenderer cellRenderer) {
	CGManager cgManager = list.getSession().getCGManager();
	cellRenderer.setTextSelectionStyle(cgManager.getStyle(propertyPrefix + "Selection.style"));
	cellRenderer.setTextNonSelectionStyle(cgManager.getStyle(propertyPrefix + "NonSelection.style"));
    }
    
    protected SCellRendererPane getCellRendererPane(SList list) {
	int count = list.getComponentCount();
	for (int i=0; i < count; i++) {
	    SComponent component = list.getComponent(i);
	    if (component instanceof SCellRendererPane)
		return (SCellRendererPane)component;
	}
	return null;
    }
    
    public void write(Device d, SComponent c)
        throws IOException
    {
	SList list = (SList)c;
	
	if (list.getShowAsFormComponent())
	    writeFormList(d, list);
	else
	    writeAnchorList(d, list);
    }
    
    protected void writeFormList(Device d, SList list)
	throws IOException
    {
	writeFormPrefix(d, list);
	writeFormBody(d, list);
	writeFormPostfix(d, list);
    }

    public void writeFormPrefix(Device d, SList list)
        throws IOException
    {
	int visibleRows = list.getVisibleRowCount();
	int selectionMode = list.getSelectionMode();

	d.append("<select name=\"");
	d.append(list.getNamePrefix());
	d.append("\"");
	
	d.append(" size=\"").append(visibleRows);
	d.append("\"");
	
	if (selectionMode == SConstants.MULTIPLE_SELECTION)
	    d.append(" multiple=\"multiple\"");
	
	//if (submitOnChange) 
	//    d.append(" onChange=\"submit()\"");
	
	d.append(">\n");
    }	

    public void writeFormBody(Device d, SList list)
        throws IOException
    {
	ListModel model = list.getModel();
	int size = model.getSize();
	
	if (model != null) {
	    SListCellRenderer cellRenderer = list.getCellRenderer();
	    SCellRendererPane rendererPane = getCellRendererPane(list);
	    
	    for (int i=0; i < size; i++) {
		Object o = model.getElementAt(i);
		boolean selected = list.isSelectedIndex(i);
		
		d.append("<option value=\"").append(i).append("\"");
		if (selected)
		    d.append(" selected=\"selected\"");
		d.append(">");
		
		SComponent renderer
		    = cellRenderer.getListCellRendererComponent(list, o, false, i);
		rendererPane.writeComponent(d, renderer, list);
		
		d.append("\n");
	    }
	}
    }

    protected void writeFormPostfix(Device d, SList list)
	throws IOException
    {
	d.append("</select>\n");
	Utils.writeHiddenComponent(d, list.getNamePrefix(), "-1");
    }

    protected void writeAnchorList(Device d, SList list)
	throws IOException
    {
	writeAnchorPrefix(d, list);
	writeAnchorBody(d, list);
	writeAnchorPostfix(d, list);
    }

    public void writeAnchorPrefix(Device d, SList list)
        throws IOException
    {
	String orderType = list.getOrderType();
	int start = list.getStart();
	
	d.append("<");
	writeType(d, list);
	if (orderType != null)
	    d.append(" type=\"").append(orderType).append("\"");
	if (start > 0)
	    d.append(" start=\"").append(start).append("\"");
	d.append(">\n");
    }
    
    public void writeAnchorBody(Device d, SList list)
        throws IOException
    {
	ListModel model = list.getModel();
	int size = model.getSize();
	SListCellRenderer cellRenderer = list.getCellRenderer();
	
	if (model != null) {
	    SCellRendererPane rendererPane = getCellRendererPane(list);
	    
	    for (int i=0; i < size; i++) {
		Object o = model.getElementAt(i);
		boolean selected = list.isSelectedIndex(i);
		
		d.append("<li>");
		SComponent renderer
		    = cellRenderer.getListCellRendererComponent(list, o, selected, i);
		rendererPane.writeComponent(d, renderer, list);
		d.append("</li>\n");
	    }
	}
    }
    
    public void writeAnchorPostfix(Device d, SList list)
        throws IOException
    {
	d.append("</");
	writeType(d, list);
	d.append(">\n");
    }
    
    protected void writeType(Device d, SList list) {
	d.append(list.getType());
    }
    
}
