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
public class ImageIconExternalizer
    extends ImageExternalizer
{
    private static final Class[] SUPPORTED_CLASSES = { ImageIcon.class };

    public ImageIconExternalizer() {
        super();
    }

    public ImageIconExternalizer(String format) {
        super(format);
    }

    public Class[] getSupportedClasses() {
        return SUPPORTED_CLASSES;
    }

    public void write(Object obj, java.io.OutputStream out)
        throws java.io.IOException
    {
        super.write(((ImageIcon)obj).getImage(), out);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
