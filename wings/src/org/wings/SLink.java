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

import java.io.*;
import org.wings.io.Device;
import org.wings.plaf.LinkCG;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
/*
 * Why is this a component ??? Actually, it is used internally in
 * the Frame, but is not very useful as standalone object .. or am I
 * missing someting ? (hen).
 */
public class SLink extends SComponent
{
    private static final String cgClassID = "LinkCG";

    protected String rel = null;
    protected String rev = null;
    protected String type = null;
    protected String target = null;
    protected URLResource urlSource = null;

    public SLink(String rel, String rev, String type, 
                 String target, URLResource urlSource) 
    {
	this.rel = rel;
	this.rev = rev;
	this.type = type;
	this.target = target;
        this.urlSource = urlSource;
    }

    public void setRel(String rel) {
	this.rel = rel;
    }
    public String getRel() { return rel; }

    public void setRev(String rev) {
	this.rev = rev;
    }
    public String getRev() { return rev; }

    public void setType(String type) {
	this.type = type;
    }
    public String getType() { return type; }

    public SimpleURL getURL() { return urlSource.getURL(); }
    
    public void setTarget(String target) {
	this.target = target;
    }
    public String getTarget() { return target; }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(LinkCG cg) {
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
