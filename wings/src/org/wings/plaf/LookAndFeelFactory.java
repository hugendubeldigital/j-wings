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

package org.wings.plaf;

import java.io.*;
import java.net.*;
import java.util.*;

import org.wings.plaf.*;
import org.wings.session.*;

public class LookAndFeelFactory
{
    private static Map lafs = new HashMap();
    private static List sessions = new LinkedList();

    public static void deployLookAndFeel(URL classpath)
	throws IOException
    {
        ClassLoader classLoader = new URLClassLoader(new URL[] { classpath },
                                                     LookAndFeelFactory.class.getClassLoader());
        LookAndFeel lookAndFeel = new LookAndFeel(classLoader);

	if (lafs.put(lookAndFeel.getName(), lookAndFeel) != null) {
	    System.err.println("redeploy");
	    notifySessions(lookAndFeel);
	}
    }

    public static LookAndFeel getLookAndFeel(String name) {
	return (LookAndFeel)lafs.get(name);
    }

    public static Collection getInstalledLookAndFeels() {
	return lafs.values();
    }

    public static void registerSession(Session session) {
	sessions.add(session);
    }

    public static void unregisterSession(Session session) {
	sessions.remove(session);
    }

    /**
     * Notify all CGManagers of all registered Sessions that have a LookAndFeel
     * with the name of the given LookAndFeel.
     */
    protected static void notifySessions(LookAndFeel lookAndFeel) {
	Session[] snapshot = (Session[])sessions.toArray(new Session[sessions.size()]);
	for (int i=0; i < snapshot.length; i++) {
	    CGManager cgManager = snapshot[i].getCGManager();
	    LookAndFeel currentLookAndFeel = cgManager.getLookAndFeel();
	    if (currentLookAndFeel.getName().equals(lookAndFeel.getName()))
		cgManager.setLookAndFeel(lookAndFeel);
	}
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
