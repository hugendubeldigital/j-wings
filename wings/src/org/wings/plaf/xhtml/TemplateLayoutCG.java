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

package org.wings.plaf.xhtml;

import java.io.IOException;
import org.wings.SLayoutManager;
import org.wings.STemplateLayout;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;
import org.wings.template.TemplateParseContext;
import org.wings.template.TemplateTagHandler;
import org.wings.template.parser.DataSource;
import org.wings.template.parser.PageParser;

/**
 * @author Achim Derigs
 * @version $Revision$
 */
public class TemplateLayoutCG
    implements LayoutCG
{
    /**
     *
     */
    static private final PageParser PARSER = new PageParser();

    /**
     *
     */
    static {
        PARSER.addTagHandler(STemplateLayout.COMPONENT,
	    TemplateTagHandler.class);
    }

    /**
     *
     */
    private void write(Device device, STemplateLayout layout)
        throws IOException {

        final DataSource source = layout.getDataSource();

        if(source == null) {
            device.append("Unable to open template-file <b>'");
            device.append(source);
            device.append("'</b>");
        }
	else {
            PARSER.process(source,
	        new TemplateParseContext(device, layout.getComponents()));
        }
    }

    /**
     * TODO: documentation
     *
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device device, SLayoutManager manager)
        throws IOException {

        write(device, (STemplateLayout) manager);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
