package org.wings.plaf;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;

public interface BorderCG
{
    void writePrefix(Device d, SBorder c) throws IOException;
    void writePostfix(Device d, SBorder c) throws IOException;
}
