package org.wings.event;

import org.wings.session.Session;
import java.util.EventObject;

/**
 * SExitEvent.java
 *
 *
 * Created: Fri Dec  6 11:07:21 2002
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SExitEvent extends EventObject {
    
    /**
     * 
     */
    public SExitEvent(Session source) {
        super(source);
    }
    
}// SExitEvent

/*
   $Log$
   Revision 1.1  2002/12/10 17:15:30  arminhaaf
   o support for exit listener

*/
