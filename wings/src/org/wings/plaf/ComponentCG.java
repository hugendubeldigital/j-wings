package org.wings.plaf;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;

public interface ComponentCG
{
    void installCG(SComponent c);
    void uninstallCG(SComponent c);

    void write(Device d, SComponent c) throws IOException;
}
