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

package org.wings.template;
import org.wings.SComponent;

/**
 * Property Manager for template managed components.
 * Components managed by TemplateLayout - Managers may have certain
 * properties which can be set from parameters given in the template
 * page, e.g. a 'text' parameter for a 'Button'-Component.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface PropertyManager
{
    void setProperty(SComponent comp, String name, String value);
    Class[] getSupportedClasses();
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
