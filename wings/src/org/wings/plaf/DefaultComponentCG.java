package org.wings.plaf;

import java.io.IOException;

import org.wings.*;
import org.wings.io.*;

public class DefaultComponentCG implements ComponentCG
{
    public void installCG(SComponent c) {}
    
    public void uninstallCG(SComponent c) {}
    
    public void write(Device d, SComponent c) throws IOException {
	c.appendBorderPrefix(d);
	c.appendPrefix(d);
	c.appendBody(d);
	c.appendPostfix(d);
	c.appendBorderPostfix(d);
    }
}
