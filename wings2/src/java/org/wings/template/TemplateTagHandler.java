/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.template;

import java.io.*;
import java.util.Map;
import java.util.Iterator;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;

import org.wings.template.parser.*;

import org.wings.SComponent;
import org.wings.STemplateLayout;
import org.wings.io.Device;

/**
 * A TemplateTagHandler 
 *
 * @author <A href="mailto:zeller@think.de">Henner Zeller</A>
 * @version $Revision$
 */
abstract class TemplateTagHandler implements SpecialTagHandler
{
    long startPos;
    long endPos;
    Map properties;
    String name;

    /**
     * Get start position of the area in the sourcefile this
     * handler processes.
     */
    public long getTagStart () {
        return startPos;
    }

    /**
     * Get the length of the area in the sourcefile.
     * The area this handler processes is skipped in the inputfile.
     */
    /*
     * Since we just have a single tag, this is just the area
     * this tag spans:
     */
    public long getTagLength () {
        return endPos - startPos;
    }

    /**
     * actually perform the action associated with this tag.
     * @exception Exception anything can happen .. and throw an Exception
     *            which is caught in PageParser
     */
    public void executeTag(ParseContext context, InputStream input)
        throws Exception
    {
        TemplateParseContext tcontext = (TemplateParseContext) context;
        Device sink = tcontext.getDevice();

        /*
         * get the component that is associtated with this name. This has
         * been set as Layout Manager Constraint.
         */
        SComponent c = tcontext.getComponent (name);
        if (c == null) {
            sink.print ("<!-- Template: '" + name + "' Component not given -->");
        }
        else {
            // set properties; the STemplateLayout knows how
            if (properties.size() > 0) {
                PropertyManager propManager = 
                    STemplateLayout.getPropertyManager(c.getClass());

                if (propManager != null) {
                    Iterator iter = properties.keySet().iterator();
                    while ( iter.hasNext()) {
                        String key = (String) iter.next();
                        String value = (String) properties.get(key);
                        // System.out.println("set Property " + key + "=" +value + "  for " + name);
                        propManager.setProperty(c, key, value);
                    }
                }
            }
            c.write(sink);
        }
        input.skip(getTagLength());
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
