package org.wings.plaf.xhtml;

import java.awt.Color;
import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class ContainerCG
    implements org.wings.plaf.ContainerCG
{
    private final static String propertyPrefix = "Container" + ".";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent component) {
        component.setStyle(component.getSession().getCGManager().
                           getStyle(getPropertyPrefix() + "style"));
    }

    public void uninstallCG(SComponent c) {
    }

    public void write(Device d, SComponent c)
        throws IOException
    {
        SBorder border = c.getBorder();
        SContainer container = (SContainer)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, container);
        Utils.writeContainerContent(d, container);
        writePostfix(d, container);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SContainer container)
        throws IOException
    {
    }

    protected void writePostfix(Device d, SContainer container)
        throws IOException
    {
    }
}
