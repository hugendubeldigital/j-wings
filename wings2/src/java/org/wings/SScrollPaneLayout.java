/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author hengels
 * @version $Revision$
 */
public class SScrollPaneLayout
        extends SAbstractLayoutManager {
    public static final String VIEWPORT = "Viewport";
    public static final String NORTH = SBorderLayout.NORTH;
    public static final String WEST = SBorderLayout.WEST;
    public static final String EAST = SBorderLayout.EAST;
    public static final String SOUTH = SBorderLayout.SOUTH;

    private SComponent viewport;
    private SComponent north;
    private SComponent west;
    private SComponent east;
    private SComponent south;

    Map components = new HashMap(5);
    private boolean paging = true;

    public boolean isPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public void addSingletonComponent(SComponent component, Object constraint) {
        if (VIEWPORT.equals(constraint)) {
            if (viewport != component) {
                container.remove(viewport);
            }
            viewport = component;
        } else if (NORTH.equals(constraint)) {
            if (viewport != component) {
                container.remove(north);
            }
            north = component;
        } else if (WEST.equals(constraint)) {
            if (viewport != component) {
                container.remove(west);
            }
            west = component;
        } else if (EAST.equals(constraint)) {
            if (viewport != component) {
                container.remove(east);
            }
            east = component;
        } else if (SOUTH.equals(constraint)) {
            if (viewport != component) {
                container.remove(south);
            }
            south = component;
        }
    }

    public void addComponent(SComponent component, Object constraint, int index) {
        addSingletonComponent(component, constraint);
        components.put(constraint, component);
    }

    public void removeComponent(SComponent c) {
        if (c == null) {
            return;
        }

        String constraint = null;
        Iterator iterator = components.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (c.equals(entry.getValue())) {
                constraint = (String) entry.getKey();
                break;
            }
        }

        if (constraint != null) {
            components.remove(constraint);
            removeSingletonComponent(constraint);
        }
    }

    private void removeSingletonComponent(String constraint) {
        if (VIEWPORT.equals(constraint)) {
            viewport = null;
        } else if (NORTH.equals(constraint)) {
            north = null;
        } else if (WEST.equals(constraint)) {
            west = null;
        } else if (EAST.equals(constraint)) {
            east = null;
        } else if (SOUTH.equals(constraint)) {
            south = null;
        }
    }

    public Map getComponents() {
        return components;
    }
}
