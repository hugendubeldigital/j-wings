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

import java.net.URL;
import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author Dominik Bartenstein
 * @version $Revision$
 */
public class SAnchor
    extends SContainer
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "AnchorCG";

    /**
     * TODO: documentation
     */
    protected String reference = null;


    /**
     * TODO: documentation
     *
     */
    public SAnchor() {
        this(null);
    }

    /**
     * TODO: documentation
     *
     * @param reference
     */
    public SAnchor(String reference) {
        setReference(reference);
    }

    /**
     * TODO: documentation
     *
     * @param ref
     */
    public void setReference(URL ref) {
        if ( ref!=null)
            setReference(ref.toString());
    }

    /**
     * TODO: documentation
     *
     * @param r
     */
    public void setReference(String r) {
        reference = r;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getReference() {
        return reference;
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "AnchorCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
