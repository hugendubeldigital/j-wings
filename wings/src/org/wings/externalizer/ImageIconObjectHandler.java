/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.externalizer;

import java.awt.Image;
import javax.swing.ImageIcon;

import Acme.JPM.Encoders.GifEncoder;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public class ImageIconObjectHandler
    implements ObjectHandler
{
    public String getExtension(Object obj) {
        return ".gif";
    }

    public String getMimeType(Object obj) {
        return "image/gif";
    }

    public boolean isStable(Object obj) {
        return false;
    }

    public void write(Object obj, java.io.OutputStream out)
        throws java.io.IOException
    {
        Image img = ((ImageIcon) obj).getImage();
        GifEncoder encoder = new GifEncoder(img, out, true);
        encoder.encode();
    }

    public Class getSupportedClass() {
        return ImageIcon.class;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
