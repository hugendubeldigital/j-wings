package org.wings.plaf.xhtml;

import java.awt.Color;
import java.io.IOException;

import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class OptionPaneCG
    implements org.wings.plaf.OptionPaneCG
{
    private final static String propertyPrefix = "OptionPane";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent component) {
        component.setStyle(component.getSession().getCGManager().
                           getStyle(getPropertyPrefix() + ".style"));
    }

    public void uninstallCG(SComponent c) {}

    public void write(Device d, SComponent c)
        throws IOException
    {
        SBorder border = c.getBorder();
        SOptionPane optionPane = (SOptionPane)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, optionPane);
        Utils.writeContainerContents(d, optionPane);
        writePostfix(d, optionPane);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SOptionPane optionPane)
	throws IOException
    {
        String encodingType = optionPane.getEncodingType();

        d.append("<form method=\"");
        if (optionPane.getMethod())
            d.append("post");
        else
            d.append("get");
        d.append("\"");

        if (encodingType != null)
            d.append(" enctype=\"").append(encodingType).append("\"");

        d.append(" action=\"").append(optionPane.getServerAddress()).
            append("\">\n");
    }

    protected void writePostfix(Device d, SOptionPane optionPane)
        throws IOException
    {
        d.append("</form>");
    }
}
