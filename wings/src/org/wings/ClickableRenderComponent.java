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

import org.wings.RequestURL;

/**
 * A component implementing this interface is able to produce events on user mouse
 * click. E.g. the TreeCG implementation renders nodes, this node is a SLabel
 * with icon and text. The TreeCG must be able to set an anchor, so that a
 * selection event is generated if the icon OR the text is clicked. SLabel
 * layout Icon and text with html table, so there is no way for the TreeCG to
 * generate anchor tags for this szenario. A ClickableRenderComponent takes care
 * of this. 
 *
 * @author
 * @version $Revision$
 */
public interface ClickableRenderComponent
{
    /**
     * The event parameter generating the event
     */
    void setEventURL(RequestURL url);

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
