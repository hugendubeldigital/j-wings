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
 * Creates a 'normal' 
 * &lt;a href=&quothttp://whatever/&quot&gt;...&lt;/a&gt;
 * HTML link around some components that are stored in the container.
 *
 * @author Dominik Bartenstein
 * @version $Revision$
 */
public class SAnchor extends SContainer
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "AnchorCG";

    /**
     * the URL to link to.
     */
    protected String reference;

    /**
     * the target frame/window.
     */
    protected String target;

    /**
     * creates an anchor with emtpy URL and target.
     */
    public SAnchor() {
        this(null, null);
    }

    /**
     * create an anchor that points to the URL url.
     *
     * @param url the url to point to.
     */
    public SAnchor(String url) {
        this(url, null);
    }

    /**
     * creates an anchor that points to the URL and is openend
     * in the frame or window named target.
     *
     * @param reference the url to link to.
     * @param target the target window or frame.
     */
    public SAnchor(String reference, String target) {
        setReference(reference);
        setTarget(target);
    }

    /**
     * set the url this anchor points to.
     *
     * @param ref the url.
     */
    public void setReference(URL ref) {
        if (ref != null) {
            setReference(ref.toString());
        }
    }

    /**
     * set the url this anchor points to.
     *
     * @param r
     */
    public void setReference(String r) {
        String oldReference = reference;
        reference = r;
        if (reference == null && oldReference != null ||
            reference != null && !reference.equals(oldReference))
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * set the name of the target frame/window.
     */
    public void setTarget(String t) {
        target = t;
    }

    /**
     * get the name of the target frame/window.
     */
    public String getTarget() {
        return target;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getReference() {
        return reference;
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(AnchorCG cg) {
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
