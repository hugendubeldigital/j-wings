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

/**
 * TODO: documentation
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SLink
    extends SComponent
{
    protected String rel = null;
    protected String rev = null;
    protected String type = null;
    protected String target = null;
    protected Object href = null;

    public SLink(String rel, String rev, String type, String target, Object href) {
	this.rel = rel;
	this.rev = rev;
	this.type = type;
	this.target = target;
	this.href = href;
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

    public void setHref(Object href) {
	this.href = href;
    }
    public Object getHref() { return href; }

    public void setTarget(String target) {
	this.target = target;
    }
    public String getTarget() { return target; }

    /**
     * Write the a/link tag to the device.
     *
     * @param s the Device to write into
     * @throws IOException thrown when the connection to the client gets broken,
     *         for example when the user stops loading
     */
    public void write(Device d) throws IOException {
	d.append("<link");
        if (rel != null)
            d.append(" rel=\"")
                .append(rel)
                .append("\"");

        if (rev != null)
            d.append(" rev=\"")
                .append(rev)
                .append("\"");

        if (type != null)
            d.append(" type=\"")
                .append(type)
                .append("\"");

        if (href != null) {
            d.append(" href=\"");

            if (href instanceof Resource)
                d.append(((Resource)href).getURL());
            else if (href instanceof String)
                d.append((String)href);

            d.append("\"");
        }

        if (target != null)
            d.append(" target=\"")
                .append(target)
                .append("\"");

	d.append("/>\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
