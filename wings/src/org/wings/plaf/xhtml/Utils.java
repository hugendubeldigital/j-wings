package org.wings.plaf.xhtml;

import java.io.IOException;

import java.awt.Color;

import org.wings.*;
import org.wings.style.*;
import org.wings.io.Device;

/**
 * Utils.java
 *
 *
 * Created: Thu Oct 28 16:23:53 1999
 *
 * @author Holger Engels
 * @version $Revision$
 */
final class Utils implements SConstants
{
    static void writeBorderPrefix(Device d, SBorder border)
        throws IOException
    {
        if (border != null)
            border.appendPrefix(d);
    }

    static void writeBorderPostfix(Device d, SBorder border)
        throws IOException
    {
        if (border != null)
            border.appendPostfix(d);
    }

    static void writeContainerContents(Device d, SContainer c)
        throws IOException
    {
        SLayoutManager layout = c.getLayout();

        if (layout != null) {
          layout.write(d);
        }
        else {
            for (int i=0; i < c.getComponentCount(); i++)
              c.getComponentAt(i).write(d);
        }
    }

    static void writeHiddenComponent(Device d, String name, String value)
        throws IOException
    {
        d.append("<input type=\"hidden\" name=\"").
	  append(name).append("\" value=\"").
	  append(value).append("\" />\n");
    }

} // Utils
