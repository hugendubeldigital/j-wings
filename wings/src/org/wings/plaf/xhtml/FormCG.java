package org.wings.plaf.xhtml;

import java.awt.Color;
import java.io.IOException;

import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class FormCG
    implements org.wings.plaf.FormCG
{
    private final static String propertyPrefix = "Form" + ".";

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
        SForm form = (SForm)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, form);
        Utils.writeContainerContent(d, form);
        writePostfix(d, form);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SForm form)
	throws IOException
    {
        String encodingType = form.getEncodingType();

        d.append("<form method=\"");
        if (form.getMethod())
            d.append("post");
        else
            d.append("get");
        d.append("\"");

        if (encodingType != null)
            d.append(" enctype=\"").append(encodingType).append("\"");

        d.append(" action=\"").append(form.getServerAddress()).
            append("\">\n");
    }

    protected void writePostfix(Device d, SForm form)
        throws IOException
    {
        d.append("</form>");
    }
}
