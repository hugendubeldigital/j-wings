/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 *
 *
 * @see javax.swing.BoundedRangeModel
 * @see SList
 * @see STable
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SDefaultBoundedRangeModel
    extends DefaultBoundedRangeModel
    implements SBoundedRangeModel
{

    /**
     * indicates if we should fire event immediately when they arise, or if we
     * should collect them for a later delivery
     */
    private boolean delayEvents = false;

    /**
     * got a delayed Event?
     */
    protected boolean gotDelayedEvent = false;

    public SDefaultBoundedRangeModel() {
        super();
    }

    public SDefaultBoundedRangeModel(int value, int extent, int min, int max) {
        super(value, extent, min, max);
    }

    public boolean getDelayEvents() {
        return delayEvents;
    }

    public void setDelayEvents(boolean b) {
        delayEvents = b;
    }

    /**
     * fire event with isValueIsAdjusting true
     */
    public void fireDelayedIntermediateEvents() {}
    
    public void fireDelayedFinalEvents() {
        if ( !delayEvents && gotDelayedEvent ) {
            fireStateChanged();
            gotDelayedEvent = false;
        }
    }

    protected void fireStateChanged() {
        if ( delayEvents ) {
            gotDelayedEvent = true;
        } else {
            super.fireStateChanged();
        }
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
