package org.wings.plaf;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;

public interface LayoutCG
{
    void write(Device d, SLayoutManager c) throws IOException;
}
