package org.wings.plaf.xhtml;

import java.awt.Color;
import java.io.IOException;

import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class DialogCG
    implements org.wings.plaf.DialogCG
{
    private final static String propertyPrefix = "Dialog";

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
        SDialog dialog = (SDialog)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, dialog);
        Utils.writeContainerContents(d, dialog);
        writePostfix(d, dialog);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SDialog dialog)
	throws IOException
    {
        String encodingType = dialog.getEncodingType();

        d.append("<form method=\"");
        if (dialog.getMethod())
            d.append("post");
        else
            d.append("get");
        d.append("\"");

        if (encodingType != null)
            d.append(" enctype=\"").append(encodingType).append("\"");

        d.append(" action=\"").append(dialog.getServerAddress()).
            append("\">\n");
    }

    protected void writePostfix(Device d, SDialog dialog)
        throws IOException
    {
        d.append("</form>");
    }
}
