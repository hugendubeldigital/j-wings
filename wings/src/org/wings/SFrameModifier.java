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

import java.io.IOException;
/**
 * Some {@link SComponent}s need to modify the head section of
 * the frame during code generation (f.e. for adding
 * component javascript code)
 * These components have to implement SFrameModifier and register
 * itself to the parent frame.
 * After standard header generation and before start of body generation
 * the <code>FrameCG</code> will call the 
 * {@link org.wings.SFrameModifier#writeHead(org.wings.io.Device, org.wings.SFrame)}.
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public interface SFrameModifier
{
    /**
      * Write code to head section of frame.
      */
    public void writeHead(org.wings.io.Device d, org.wings.SFrame f) throws IOException;
    
    /**
      * Compare to objects of this type.
      * This should be always implemented to support removal
      * of FrameModifiers from Frame during unregistering of components.
      * Please use {@link org.wings.SFrameModifier#getUnifiedId()} or
      * if inherits {@link org.wings.SComponent} the method
      * {@link org.wings.SComponent#getUnifiedId()} for comparison!
      * @param obj compare to this object.
      * @return <code>true</code> if equals, <code>false</code> otherwise.
      */
    public boolean equals(java.lang.Object obj);
    
    /**
      * Return a jvm wide unique id.
      * @return an id
      */
    public int getUnifiedId();
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
