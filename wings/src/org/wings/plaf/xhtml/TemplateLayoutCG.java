// $Id$

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
public class TemplateLayoutCG implements LayoutCG {

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
