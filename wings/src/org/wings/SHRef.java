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
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SHRef
    extends SButton
{
    private static final String cgClassID = "HRefCG";

    /**
     * TODO: documentation
     */
    protected String ref = null;

    /**
     * TODO: documentation
     *
     */
    public SHRef() {
        super.setShowAsFormComponent(false);
    }

    /**
     * TODO: documentation
     *
     * @param text
     */
    public SHRef(String text) {
        super(text);
        super.setShowAsFormComponent(false);
    }

    public SHRef(String text, String ref) {
        super(text);
        setReference(ref);
        super.setShowAsFormComponent(false);
    }

    /**
     * TODO: documentation
     *
     * @param showAsFormComponent
     */
    public void setShowAsFormComponent(boolean showAsFormComponent) {
        super.setShowAsFormComponent(false);
    }

    /**
     * TODO: documentation
     *
     * @param url
     */
    public void setReference(String url) {
        ref = url;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getReference() {
        return ref;
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(HRefCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
