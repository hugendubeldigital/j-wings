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

package org.wings.plaf.xhtml.css1;

import org.wings.border.SBevelBorder;
import org.wings.border.SBorder;
import org.wings.io.Device;

import java.io.IOException;

public final class BevelBorderCG
    extends DefaultBorderCG
{

    public String getSpanAttributes(SBorder b )
    {
      StringBuffer sb = new StringBuffer();
      
	SBevelBorder border = (SBevelBorder)b;

        sb.append(getBorderStyle(b, border.getBevelType()==SBevelBorder.RAISED ? 
                         "outset" : "inset"));

        // write insets/padding
        sb.append(super.getSpanAttributes(b));
        return sb.toString();
    }

    public void writeSpanAttributes(Device d, SBorder b )
        throws IOException
    {
	   d.print(getSpanAttributes(b));
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
