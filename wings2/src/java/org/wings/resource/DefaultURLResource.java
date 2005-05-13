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
package org.wings.resource;

import org.wings.SimpleURL;
import org.wings.URLResource;

/**
 * Default implementation of an Resource accessible via an HTTP URL.
 *
 * @author armin
 *         created at 15.01.2004 17:58:29
 */
public class DefaultURLResource implements URLResource {

    private final SimpleURL url;

    /**
     * @param urlString The URL to access this ressource.
     */
    public DefaultURLResource(String urlString) {
        url = new SimpleURL(urlString);
    }

    public SimpleURL getURL() {
        return url;
    }
}
