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

package org.wings.header;

import java.io.*;
import org.wings.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class Meta implements Renderable
{
    protected String name = null;
    protected String lang = null;
    protected String content = null;

    public Meta(String name, String lang, String content) {
	this.name = name;
	this.lang = lang;
	this.content = content;
    }

    public void setName(String name) {
	this.name = name;
    }
    public String getName() { return name; }

    public void setLang(String lang) {
	this.lang = lang;
    }
    public String getLang() { return lang; }

    public void setContent(String content) {
	this.content = content;
    }
    public String getContent() { return content; }

    public void write(Device d)
        throws IOException
    {
        d.print("<meta");
        if (name != null)
            d.print(" name=\"" + name + "\"");
        if (lang != null)
            d.print(" lang=\"" + lang + "\"");
        if (content != null)
            d.print(" content=\"" + content + "\"");
        d.print("/>");
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
