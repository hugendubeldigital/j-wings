package org.wings.plaf.xhtml;

import java.awt.Color;
import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class InternalFrameCG
    implements org.wings.plaf.InternalFrameCG
{
    private final static String propertyPrefix = "InternalFrame";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent component) {
        component.setStyle(component.getSession().getCGManager().
                           getStyle(getPropertyPrefix() + ".style"));
    }

    public void uninstallCG(SComponent c) {
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SInternalFrame internalFrame = (SInternalFrame)c;
        Utils.writeContainerContents(d, internalFrame);
    }
}
