package org.wings.plaf.xhtml;

import java.io.IOException;

import java.awt.Color;
import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class ParagraphCG
    implements org.wings.plaf.ParagraphCG
{
    private final static String propertyPrefix = "Paragraph" + ".";

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
        SParagraph paragraph = (SParagraph)c;

        Utils.writeBorderPrefix(d, border);
        writePrefix(d, paragraph);
        Utils.writeContainerContent(d, paragraph);
        writePostfix(d, paragraph);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SParagraph paragraph)
        throws IOException
    {
        d.append("<p");
        writeAttributes(d, paragraph);
        d.append(">");
    }

    protected void writeAttributes(Device d, SParagraph paragraph)
        throws IOException
    {
        int alignment = paragraph.getAlignment();

        if ( alignment == SParagraph.RIGHT_ALIGN )
            d.append(" align=right");
        else if ( alignment == SParagraph.CENTER_ALIGN )
            d.append(" align=center");
        else if ( alignment == SParagraph.BLOCK_ALIGN )
            d.append(" align=justify");
    }

    protected void writePostfix(Device d, SParagraph paragraph)
        throws IOException
    {
        d.append("</p>");
    }
}
