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

    public int hashCode() {
        Object requestListener = get();

        if (  requestListener==null ) {
            return 0;
        } else {
            return requestListener.hashCode();
        } // end of if ()
    }

    public boolean equals(WeakRequestListenerProxy p) {
        Object requestListener = get();

        if (  requestListener==null ) {
            return p.get()==null;
        } else {
            return requestListener.equals(p.get());
        } // end of if ()
    }

    public boolean equals(Object o) {
        if ( o instanceof WeakRequestListenerProxy ) {
            return equals((WeakRequestListenerProxy)o);
        } else {
            return false;   
        } // end of if ()
    }

}// WeakRequestListenerProxy

/*
   $Log$
   Revision 1.1  2004/10/04 16:13:16  hengels
   Initial revision

   Revision 1.2  2003/03/24 17:05:37  arminhaaf
   o add equals and hashcode methods

   Revision 1.1  2002/10/25 16:29:16  ahaaf
   o add cancel editing button
   o change the table editor handling to support events on the preparation of the editor
   o change the way SDefaultCellEditor works

*/
