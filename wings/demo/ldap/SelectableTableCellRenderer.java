package ldap;

import java.net.URL;
import org.wings.*;
import org.wings.table.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.event.EventListenerList;
import org.wings.io.Device;
import java.util.StringTokenizer;

public class SelectableTableCellRenderer
    extends SDefaultTableCellRenderer
    implements RequestListener
{
    private final static String DELIMITER = ":";
    private String reference = null;
    private int[] selCol = new int[0];

    protected EventListenerList listenerList = new EventListenerList();

    public SelectableTableCellRenderer() {
        setEscapeSpecialChars(false);
    }

    /** @link dependency 
     * @stereotype send*/
    /*#CellSelectionEvent lnkCellSelectionEvent;*/

    public SComponent getTableCellRendererComponent(SBaseTable baseTable,
						    Object value,
						    boolean isSelected,
						    int row,
						    int col)
    {
        if (value == null || value.toString() == null || value.toString().length() == 0)
            value = "&nbsp";

        if (isSelectableColumn(col)) {
            RequestURL addr = baseTable.getRequestURL();
            addr.addParameter(getNamePrefix(), col + DELIMITER + row);
            setText(value.toString());
            reference = addr.toString();
            return this;
        }
        else
            reference = null;

        return super.getTableCellRendererComponent(baseTable, value, isSelected, row, col);
    }

    public void setSelectableColumns(int[] arr) {
        if (arr != null)
            selCol = arr;
        else
            selCol = new int[0];
    }

    public boolean isSelectableColumn(int col) {
        for(int i=0;i<selCol.length;i++) {
            if (selCol[i] == col)
                return(true);
        }
        return(false);
    }

    public void processRequest(String action, String[] values) {
        fireCellSelectionPerformed(values[0]);
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
        try {
            if (tz.hasMoreElements())
                x = Integer.parseInt(tz.nextToken());
            if (tz.hasMoreElements())
                y = Integer.parseInt(tz.nextToken());
        }
        catch(Exception e) {
            System.err.println("SelectableTableCellRenderer::fireCellSelectionPerformed("
                               + pos
                               + ") -> invalid input");
        }
        CellSelectionEvent e = new CellSelectionEvent(this, x, y);
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == CellSelectionListener.class) {
                ((CellSelectionListener)listeners[i+1]).cellSelected(e);
            }
        }
    }

    public void fireIntermediateEvents() {}
    public void fireFinalEvents() {}

    public void write(Device d) throws IOException
    {
        if (reference != null) {
            d.print("<a href=\"");
            d.print(reference);
            d.print("\">");
        }

        super.write(d);

        if (reference != null)
            d.print("</a>");
    }
}



/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
