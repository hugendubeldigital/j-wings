package org.wings.event;

import java.lang.ref.WeakReference;



/**
 * A SRequestListener implementation, which uses WeakReference. This is
 * important for volatile objects, which needs to be registered as request
 * listener at the wings session, but which are not removed anymore from the
 * session. To make them 
 * garbage collectable, they should use this proxy for registering as a request
 * listener. 
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public final class WeakRequestListenerProxy extends WeakReference implements SRequestListener {
    
    /**
     * 
     */
    public WeakRequestListenerProxy(SRequestListener requestListener) {
        super(requestListener);
    }

    /**
     * Invokes the processRequest method of the proxied requestListener. If the
     * listener is garbage collected, it removes itself from the session as
     * request listener.
     *
     * @param e a <code>SRequestEvent</code> value
     */
    public void processRequest(SRequestEvent e) {
        SRequestListener requestListener = (SRequestListener)get();

        if (  requestListener==null ) {
            org.wings.session.SessionManager.getSession().removeRequestListener(this);
        } else {
            requestListener.processRequest(e);
        } // end of if ()
        
    }

}// WeakRequestListenerProxy

/*
   $Log$
   Revision 1.1  2002/10/25 16:29:16  ahaaf
   o add cancel editing button
   o change the table editor handling to support events on the preparation of the editor
   o change the way SDefaultCellEditor works

*/
