
package ldap;

import java.util.EventListener;

public interface CellSelectionListener extends EventListener {

    public void cellSelected(CellSelectionEvent e);

}
