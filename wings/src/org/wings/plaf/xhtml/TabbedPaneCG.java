package org.wings.plaf.xhtml;

import java.awt.Color;
import java.io.IOException;

import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class TabbedPaneCG
    implements org.wings.plaf.TabbedPaneCG
{
    private final static String propertyPrefix = "TabbedPane" + ".";

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
        STabbedPane pane = (STabbedPane)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, pane);
        Utils.writeContainerContent(d, pane);
        writePostfix(d, pane);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, STabbedPane pane)
        throws IOException
    {
    }

    protected void writePostfix(Device d, STabbedPane pane)
        throws IOException
    {
    }
}
