package org.wings.plaf.xhtml;

import java.io.IOException;

import java.awt.Color;
import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class AnchorCG
    implements org.wings.plaf.AnchorCG
{
    private final static String propertyPrefix = "Anchor" + ".";

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
        SAnchor anchor = (SAnchor)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, anchor);
        Utils.writeContainerContents(d, anchor);
        writePostfix(d, anchor);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SAnchor anchor)
        throws IOException
    {
        String name = anchor.getName();
        String reference = anchor.getReference();

        d.append("<a");

        if (name != null)
            d.append(" name=\"").append(name).append("\"");

        if (reference != null)
            d.append(" href=\"").append(reference).append("\"");

        d.append(">");
    }

    protected void writePostfix(Device d, SAnchor anchor)
        throws IOException
    {
        d.append("</a>");
    }
}
