package org.wings;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @author armin
 * created at 06.01.2004 13:32:57
 * @deprecated use {@link SFileImageIcon} instead
 */
public class FileImageIcon extends SFileImageIcon {
    public FileImageIcon(String name) throws IOException {
        super(name);
    }

    public FileImageIcon(File file) throws IOException {
        super(file);
    }

    public FileImageIcon(File file, String ext, String mt) {
        super(file, ext, mt);
    }
}
