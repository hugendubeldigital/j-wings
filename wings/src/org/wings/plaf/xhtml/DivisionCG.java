package org.wings.plaf.xhtml;

import java.awt.Color;
import java.io.IOException;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class DivisionCG
    implements org.wings.plaf.DivisionCG
{
    private final static String propertyPrefix = "Division";

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
        SBorder border = c.getBorder();
        SDivision division = (SDivision)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, division);
        Utils.writeContainerContents(d, division);
        writePostfix(d, division);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SDivision division)
        throws IOException
    {
	d.append("\n<div>\n");
    }

    protected void writePostfix(Device d, SDivision division)
        throws IOException
    {
	d.append("\n</div>\n");
    }
}
