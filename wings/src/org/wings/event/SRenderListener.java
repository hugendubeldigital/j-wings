package org.wings.event;

import java.util.EventListener;



/**
 * SRenderListener.java
 *
 *
 * Created: Wed Nov  6 10:17:41 2002
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface SRenderListener extends EventListener {

    public void startRendering(SRenderEvent e);

    public void doneRendering(SRenderEvent e);
    
}// SRenderListener

/*
   $Log$
   Revision 1.1  2002/11/06 17:00:29  ahaaf
   o add support for render events

*/
