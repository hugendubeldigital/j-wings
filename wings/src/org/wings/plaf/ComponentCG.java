package org.wings.plaf;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;

public interface ComponentCG
{
    /**
     * Installs the CG. <p>
     * <b>Note</b>: Be very carefull here as this method is called from
     * the SComponent constructor! Don't call any methods which relay on
     * something that will be constructed in a subconstructor later!
     */
    void installCG(SComponent c);

    /**
     * Uninstalls the CG.
     */
    void uninstallCG(SComponent c);

    /**
     * Writes the given component. <p>
     * his method should be called from the write method in SComponent or
     * a subclass.
     */
    void write(Device d, SComponent c) throws IOException;
}
