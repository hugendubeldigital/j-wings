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

package org.wings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * An SIcon of this type is externalized globally. It is not bound
 * to a session.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SFileImageIcon extends FileResource implements SIcon {

    /**
     * The width to display the icon. This overwrites the real width of the icon. Ignored, if &lt;0
     */
    private int width = -1;

    /**
     * The height to display the icon. This overwrites the real height of the icon. Ignored, if &lt;0
     */
    private int height = -1;

    /**
     * Create a new SFileImageIcon from the File. This constructor extracts
     * the extension from the file to be appended to the externalized resource
     * name.
     *
     * @param fileName
     */
    public SFileImageIcon(String fileName) throws IOException {
        this(new File(fileName));
    }

    /**
     * crates a new SFileImageIcon from the given file. The extension and
     * mimetype are taken from the parameters given.
     *
     * @param file      the file to construct a SFileImageIcon from
     * @param extension user provided extension. The original extension of
     *                  the file is ignored, unless this paramter is
     *                  'null'.
     * @param mimetype  the user provided mimetype. If this is 'null', then
     *                  the mimetype is guessed from the extension.
     */
    public SFileImageIcon(File file, String extension, String mimetype) {
        super(file, extension, mimetype);

        // if either of the extension or mimetype is missing, try to guess it.
        if (this.mimeType == null || this.mimeType.length() == 0) {
            if (this.extension == null || this.extension.length() == 0) {
                this.extension = "";
                this.mimeType = "image/png";
            } else if (this.extension.toUpperCase().equals("JPG"))
                this.mimeType = "image/jpeg";
            else
                this.mimeType = "image/" + this.extension;
        } else if (this.extension == null || this.extension.length() == 0) {
            int slashPos = -1;
            if (this.mimeType != null
                && (slashPos = this.mimeType.lastIndexOf('/')) >= 0) {
                this.extension = this.mimeType.substring(slashPos + 1);
            }
        }
        calcDimensions();
    }

    public SFileImageIcon(File file) throws IOException {
        this(file, null, null);
    }

    /**
     *
     */
    protected void calcDimensions() {
        try {
            bufferResource();

            if (buffer != null && buffer.isValid()) {
                BufferedImage image =
                    ImageIO.read(new ByteArrayInputStream(buffer.getBytes()));
                width = image.getWidth();
                height = image.getHeight();
            }
        } catch (Throwable e) {
            // is not possible to calc Dimensions
            // maybe it is not possible to buffer resource,
            // or resource is not a
            // supported image type
        }
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }

    public void setIconWidth(int width) {
        this.width = width;
    }

    public void setIconHeight(int height) {
        this.height = height;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
