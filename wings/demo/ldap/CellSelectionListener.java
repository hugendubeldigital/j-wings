package ldap;

import java.util.EventListener;

public interface CellSelectionListener extends EventListener {
    void cellSelected(CellSelectionEvent e);
}
