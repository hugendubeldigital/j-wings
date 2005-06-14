/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf.css.msie;

import java.io.IOException;

import org.wings.RequestURL;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;

/**
 * @author ole
 *
 */
public class AnchorCG extends org.wings.plaf.css.AnchorCG {
    /* (non-Javadoc)
     * @see org.wings.plaf.css.CheckBoxCG#writeLinkStart(org.wings.io.Device, org.wings.RequestURL)
     */
    protected void writeLinkStart(final Device device, RequestURL addr) throws IOException {
        device.print("<a onclick=\"javascript:location.href='");
        Utils.write(device, addr.toString());
        device.print("';\"");
    }
}
