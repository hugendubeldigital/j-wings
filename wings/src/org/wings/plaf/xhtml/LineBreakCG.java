package org.wings.plaf.xhtml;

import java.io.IOException;

import org.wings.plaf.*;
import org.wings.io.*;
import org.wings.*;

public class LineBreakCG
    implements org.wings.plaf.LineBreakCG
{
    private final static String propertyPrefix = "LineBreak" + ".";

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
        SLineBreak lineBreak = (SLineBreak)c;

        d.append("<br");
        writeAttributes(d, lineBreak);
        d.append(">");
    }

    protected void writeAttributes(Device d, SLineBreak lineBreak)
        throws IOException
    {
        int clear = lineBreak.getClear();

        if ( clear == SLineBreak.CLEAR_ALL )
            d.append(" clear=\"all\"");
        else if ( clear == SLineBreak.CLEAR_LEFT )
            d.append(" clear=\"left\"");
        else if ( clear == SLineBreak.CLEAR_RIGHT )
            d.append(" clear=\"right\"");
    }
}
