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
 * A component implementing this interface is able to produce events on 
 * user mouse click. E.g. the TreeCG implementation renders nodes with each 
 * node being a SLable with icon and text. Or the TableCG needs to receive
 * events on clicked cells to switch to the edit cell renderer.
 * 
 * <p>What is simple in graphical user interfaces -- they just receive a click
 * and know the component below where it occured -- is not that easy
 * in HTML rendering. In order to receive events here, we've to write
 * some anchor around it. This is what this ClickableRenderComponent is all
 * about: it gets an URL and the plaf for that component makes sure, that
 * there is an anchor rendered around the important parts so that the 
 * application gets the event.
 * 
 * <p>Why is this needed, and we cannot just write an anchor tag around
 * the component being rendered? Consider the layout for the SLabel
 * for instance. It renderes the Icon and text using a html table, so the
 * the part that wants the event (e.g. the TreeCG) cannot just
 * generate anchor tags around the whole SLabel (browsers cannot handle
 * a-href tags, that surround complex containers like tables).
 * A ClickableRenderComponent takes care of this (The SLabel implements a
 * ClickableRenderComponent and its plaf writes the appropriate a-href
 * tags around each element (icon and text)). 
 * See the LabelCG plaf implementation, if you are curious.
 *
 * <p>If you want your own components be rendered correctly in STree, STables 
 * or SAnchors, you need to implement this interface. And if you are lazy, then
 * you just use the SLabel which already does this ;-)
 *
 * <p>The use of this interface is as follows: if a plaf wants to render
 * anchors and finds out, that the component is <code>instanceof</code>
 * ClickableRenderComponent, it sets the URL, calls 
 * {@link SComponent#write(Device)} and resets the URL again. The plaf of
 * that component then has to take care of the URL.
 * <p>Since multiple threads can render content in parallel, you must
 * make sure, that the internal variable is thread save - use a ThreadLocal
 * variable, or the utility class {@link ClickableRenderUtil}; it is used
 * by the SLabel as well.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision$
 */
public interface ClickableRenderComponent
{   
    /**
     * The URL that is used to generate the event. This method is called
     * by the plaf that wants this ClickableRenderComponent to sourround
     * its contents by an anchor (or more, if necessary).
     */
    void pushEventURL(RequestURL url, String target);
    
    /**
     * pop the latest URL.
     */
    void popEventURL();
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
