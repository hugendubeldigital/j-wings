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

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public final class SAlert
    extends SComponent
{
    /**
     * TODO: documentation
     */
    protected final StringBuffer alert = new StringBuffer();

    /**
     * TODO: documentation
     *
     */
    public SAlert() {
    }

    /**
     * TODO: documentation
     */
    public final void setAlert(String a) {
        alert.setLength(0);
        addAlert(a);
    }

    /**
     * TODO: documentation
     */
    public final void addAlert(String a) {
        if ( alert.length()>0 )
            alert.append("\n\n");
        alert.append(a);
    }

    /**
     * TODO: documentation
     */
    public final void appendPrefix(Device s) {
    }

    /**
     * TODO: documentation
     */
    public final void appendPostfix(Device s) {
    }

    /**
     * TODO: documentation
     */
    public final void appendBorderPrefix(Device s) {
    }

    /**
     * TODO: documentation
     */
    public final void appendBorderPostfix(Device s) {
    }

    /**
     * TODO: documentation
     */
    public final void appendBody(Device s) {
        if ( alert.length()>0 ) {
            setAlert(org.wings.util.StringUtil.replace(alert.toString(), "\n", "\\n"));

            s.append("\n<script language=\"JavaScript\">\n");
            s.append("<!--\n");
            s.append("alert(\"").append(alert).append("\");\n");
            s.append("//-->\n");
            s.append("</script>\n");

            alert.setLength(0);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
