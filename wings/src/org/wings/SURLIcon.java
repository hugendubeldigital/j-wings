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

import java.net.URL;
import java.net.MalformedURLException;

/**
 * SURLIcon.java
 *
 *
 * Created: Sun Nov 11 02:02:47 2001
 *
 * @author <a href="mailto:armin@hyperion.intranet.mercatis.de">Armin Haaf</a>
 * @version $Revision
 */

public class SURLIcon implements SIcon {

  URL url;

  public SURLIcon (URL u) {
    url = u;
  }

  public SURLIcon (String u) {
    try {
      url = new URL(u);
    } catch ( MalformedURLException e ) {
    }
  }

  public int getIconWidth() {
    return -1;
  }
  
  public int getIconHeight() {
    return -1;
  }
  
  public URL getURL() {
    return url;
  }

}// SURLIcon
