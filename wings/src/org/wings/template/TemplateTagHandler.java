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
import java.util.Dictionary;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;

import org.wings.template.parser.*;

import org.wings.SComponent;
import org.wings.io.Device;

/**
 * A TemplateTagHandler 
 *
 * @author <A href="mailto:zeller@think.de">Henner Zeller</A>
 * @version $Revision$
 */
public class TemplateTagHandler implements SpecialTagHandler
{
    private static String ERRORMSG = "Error in Template: &lt;/component&gt; missing";
    long startPos;
    long endPos;
    Dictionary properties;
    String name;
    boolean close_is_missing = false;

    /**
     * Parse special tag.
     * @param config    Servlet configuration
     * @param input     The PositionReader, located after the Name token of the Tag
     * @param startPos  The Position parsing of this token began
     * @param startTag  the SGMLTag found in the file.
     */
    public SGMLTag readTag(ParseContext context,
                           PositionReader input,
                           long startPosition,
                           SGMLTag startTag)
        throws IOException
    {
        /*
         * parse the full tag to get all parameters
         * (i.e. an optional 'format'-parameter)
         * and to place the Reader at the position
         * after the closing '>'
         */
        startTag.parse(input);

        /*
         * The Offset is the space the reader skipped
         * before it reached the opening '<'
         *   <!-- a comment --> some garbage <DATE>
         * ^----- offset --------------------^
         */
        startPos = startPosition + startTag.getOffset();

        /*
         * get needed properties
         */
        name = startTag.value ("NAME", null);
        if (name == null)
            return null;

        properties = startTag.getAttributes();

        endPos = input.getPosition();  // in case </component> is missing

        while (!startTag.finished()) {
            startTag = new SGMLTag (input, true);
            if (startTag.isNamed("/" +
                                 org.wings.STemplateLayout.COMPONENT_TAG) ||
                startTag.isNamed(org.wings.STemplateLayout.COMPONENT_TAG))
                break;
        }

        // Entweder EOF oder unerwartet neu oeffnendes COMPONENT ..
        if (startTag.finished() || 
            startTag.isNamed(org.wings.STemplateLayout.COMPONENT_TAG)) {
            close_is_missing = true;
        }
        else {
            // The current Position is after the closing '>'
            endPos = input.getPosition();
        }

        return startTag;
    }

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
    public void executeTag (ParseContext context)
        throws Exception
    {
        TemplateParseContext tcontext = (TemplateParseContext) context;
        Device sink = tcontext.getDevice();
        SComponent c = tcontext.getComponent (name);
        if (c == null) {
            sink.append ("<!-- NOT FOUND -->");
        }
        else {
            // set properties first ..
            c.write(sink);
        }

        // warn, if the closing tag was not found ..
        if (close_is_missing) {
            sink.append ("<table bgcolor='#FFAA55'><tr><td>");
            sink.append ("&nbsp;<blink><b>");
            sink.append ( ERRORMSG );
            sink.append (" for '<em>" + name + "</em>'");
            sink.append ("</b></blink>&nbsp;");
            sink.append ("</td></tr></table>");
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
