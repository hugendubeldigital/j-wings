package ldap;

import java.net.URL;
import org.wings.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.event.EventListenerList;
import org.wings.io.Device;
import java.util.StringTokenizer;

public class SelectableTableCellRenderer
    extends SDefaultTableCellRenderer
    implements SGetListener
{
    private final static String DELIMITER = ":";
    private SHRef link = new SHRef();
    private int[] selCol = new int[0];

    protected EventListenerList listenerList = new EventListenerList();

    /** @link dependency 
     * @stereotype send*/
    /*#CellSelectionEvent lnkCellSelectionEvent;*/

    public SComponent getTableCellRendererComponent(SBaseTable baseTable,
						    Object value,
						    boolean isSelected,
						    int row,
						    int col)
    {
        if (value == null)
            value = "";
        if (value.toString().equals(""))
            value = "&nbsp";
        if (isSelectableColumn(col))
        {
            SGetAddress addr = baseTable.getServerAddress();
            addr.add(getNamePrefix() + "=" + col + DELIMITER + row);
            link.setText(value.toString());
            link.setReference(addr.toString());
            return(this);
        }
        return super.getTableCellRendererComponent(baseTable, value, isSelected, row, col);
    }

    public void setSelectableColumns(int[] arr)
    {
        if (arr != null)
            selCol = arr;
        else
            selCol = new int[0];
    }

    public boolean isSelectableColumn(int col)
    {
        for(int i=0;i<selCol.length;i++)
        {
            if (selCol[i] == col)
                return(true);
        }
        return(false);
    }

    public void getPerformed(String action, String value) {
        fireCellSelectionPerformed(value);
    }

    public void addCellSelectionListener(CellSelectionListener listener) {
        listenerList.add(CellSelectionListener.class, listener);
    }

    public void removeCellSelectionListener(CellSelectionListener listener) {
        listenerList.remove(CellSelectionListener.class, listener);
    }

    /**
     * Fire an ActionEvent at each registered listener.
     */
    protected void fireCellSelectionPerformed(String pos) {
        StringTokenizer tz = new StringTokenizer(pos, DELIMITER);
        int x=-1, y=-1;
        try
        {
            if (tz.hasMoreElements())
                x = Integer.parseInt(tz.nextToken());
            if (tz.hasMoreElements())
                y = Integer.parseInt(tz.nextToken());
        }
        catch(Exception e)
        {
            System.err.println("SelectableTableCellRenderer::fireCellSelectionPerformed("+pos+") -> invalid input");
        }
        CellSelectionEvent e = new CellSelectionEvent(this, x, y);
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == CellSelectionListener.class) {
                ((CellSelectionListener)listeners[i+1]).cellSelected(e);
            }
        }
    }


    public void write(Device d) throws IOException
    {
        if (link != null) {
            d.append("<a href=\"");
            d.append(link.getReference());
            d.append("\">");
        }
        d.append(link.getText());
        if (link != null)
            d.append("</a>");
    }

}
