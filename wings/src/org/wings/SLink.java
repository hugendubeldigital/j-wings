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
    protected String type = null;
    protected String hRef = null;

    public SLink(String rel, String type, String hRef) {
	this.rel = rel;
	this.type = type;
	this.hRef = hRef;
    }

    public void setRel(String rel) {
	this.rel = rel;
    }
    public String getRel() { return rel; }

    public void setType(String type) {
	this.type = type;
    }
    public String getType() { return type; }

    public void setHRef(String hRef) {
	this.hRef = hRef;
    }
    public String getHRef() { return hRef; }

    /**
     * Write the link tag to the device.
     *
     * @param s the Device to write into
     * @throws IOException thrown when the connection to the client gets broken,
     *         for example when the user stops loading
     */
    public void write(Device d) throws IOException {
	d.append("<link rel=\"");
	d.append(rel);
	d.append("\" type=\"");
	d.append(type);
	d.append("\" href=\"");
	d.append(hRef);
	d.append("\"/>\n");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
