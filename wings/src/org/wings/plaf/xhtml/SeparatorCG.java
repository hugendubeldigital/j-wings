package org.wings.plaf.xhtml;

import java.io.IOException;

import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class SeparatorCG
    implements org.wings.plaf.SeparatorCG
{
    private final static String propertyPrefix = "Separator" + ".";

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
        SSeparator separator = (SSeparator)c;

        d.append("<hr");
        writeAttributes(d, separator);
        d.append(">");
    }

    protected void writeAttributes(Device d, SSeparator separator)
        throws IOException
    {
        int width = separator.getWidth();
        int size = separator.getSize();
        int alignment = separator.getAlignment();
        boolean shade = separator.getShade();

        if ( width > 0 )
            d.append(" width=\"").append(width).append("\"");

        if ( alignment == SSeparator.RIGHT_ALIGN )
            d.append(" align=\"right\"");
        else if ( alignment == SSeparator.CENTER_ALIGN )
            d.append(" align=\"center\"");
        else if ( alignment == SSeparator.BLOCK_ALIGN )
            d.append(" align=\"justify\"");

        if ( size > 0 )
            d.append(" size=\"").append(size).append("\"");

        if ( !shade )
            d.append(" noshade");
    }
}
