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

import org.wings.session.SessionManager;

import java.net.URL;
import javax.swing.ImageIcon;

public class SImageIcon implements SIcon {
    private ImageIcon img;
    private URL url;
    
    public SImageIcon(ImageIcon image) {
	this.img = image;
    }

    public SImageIcon(String name) {
        this.img = new ImageIcon(name);
    }
    
    public int getIconWidth() {
	return img.getIconWidth();
    }

    public int getIconHeight() {
	return img.getIconHeight();
    }

    /**
     * returns the URL, the icon can be fetched from. This URL may
     * be relative, usually if generated from the externalizer.
     */
    public URL getURL() {
	if (url == null) {
	    String urlString = SessionManager.getSession()
		.getExternalizeManager()
		.externalize(img);
	    if (urlString != null)
		url = new URL(urlString);
	}
	return url;
    }

    // java.awt.Image getImage() // get the image e.g. if you want to grey it out
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
