package org.wings.plaf.xhtml;

import java.io.IOException;

import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class HorizontalRuleCG
    implements org.wings.plaf.HorizontalRuleCG
{
    private final static String propertyPrefix = "HorizontalRule" + ".";

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
        SHorizontalRule hrule = (SHorizontalRule)c;

        d.append("<hr");
        writeAttributes(d, hrule);
        d.append(">");
    }

    protected void writeAttributes(Device d, SHorizontalRule hrule)
        throws IOException
    {
        int width = hrule.getWidth();
        int size = hrule.getSize();
        int alignment = hrule.getAlignment();
        boolean shade = hrule.getShade();

        if ( width > 0 )
            d.append(" width=\"").append(width).append("\"");

        if ( alignment == SHorizontalRule.RIGHT_ALIGN )
            d.append(" align=\"right\"");
        else if ( alignment == SHorizontalRule.CENTER_ALIGN )
            d.append(" align=\"center\"");
        else if ( alignment == SHorizontalRule.BLOCK_ALIGN )
            d.append(" align=\"justify\"");

        if ( size > 0 )
            d.append(" size=\"").append(size).append("\"");

        if ( !shade )
            d.append(" noshade");
    }
}
