package org.wings.plaf.xhtml;

import java.io.IOException;

import java.awt.Color;
import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class PanelCG
    implements org.wings.plaf.PanelCG
{
    private final static String propertyPrefix = "Panel" + ".";

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
        SPanel panel = (SPanel)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, panel);
        Utils.writeContainerContent(d, panel);
        writePostfix(d, panel);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SPanel panel)
        throws IOException
    {
    }

    protected void writePostfix(Device d, SPanel panel)
        throws IOException
    {
    }
}
