/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings;

import java.awt.event.ContainerListener;
import java.awt.event.ContainerEvent;
import java.io.IOException;
import java.util.ArrayList;

import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @see SLayoutManager
 * @see SComponent
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SContainer extends SComponent
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "ContainerCG";

    /**
     * The components in this container.
     */
    protected ArrayList components;

    /**
     * The constraints for the components.
     */
    protected ArrayList constraints;

    /**
     * The constraints for the components.
     */
    protected ArrayList containerListener;

    SLayoutManager layout = null;

    /**
     * TODO: documentation
     *
     * @param l
     */
    public SContainer(SLayoutManager l) {
        init();
        setLayout(l);
    }

    /**
     * TODO: documentation
     *
     */
    public SContainer() {
        this(null);
    }

    /**
     * TODO: documentation
     *
     */
    protected void init() {
        if (components != null)
            return;
        components = new ArrayList(3);
        constraints = new ArrayList(3);
    }

    /**
     * TODO: documentation
     *
     * @param newCG
     */
    protected void setCG(ComponentCG newCG) {
        init();
        super.setCG(newCG);
    }


    public void updateCG() {
        super.updateCG();

        if (layout != null)
            layout.updateCG();
    }

    /**
     * TODO: documentation
     *
     * @param l
     */
    public void setLayout(SLayoutManager l) {
        if (layout != null)
            for ( int i=0; i<components.size(); i++ )
                layout.removeComponent((SComponent)components.get(i));

        layout = l;

        if ( layout!=null ) {
            for ( int i=0; i<components.size(); i++ )
                layout.addComponent((SComponent)components.get(i),
                                    constraints.get(i));
            layout.setContainer(this);
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SLayoutManager getLayout() {
        return layout;
    }

    /**
     * Adds the specified container listener to receive container events
     * from this container.
     * If l is null, no exception is thrown and no action is performed.
     *
     * @param    l the container listener
     */
    public void addContainerListener(ContainerListener l) {
        if (l == null) {
            return;
        }

        if ( containerListener==null )
            containerListener = new ArrayList();

        containerListener.add(l);
    }

    /**
     * Removes the specified container listener so it no longer receives
     * container events from this container.
     * If l is null, no exception is thrown and no action is performed.
     *
     * @param 	l the container listener
     */
    public void removeContainerListener(ContainerListener l) {
        if (l == null) {
            return;
        }
        containerListener.remove(l);
    }

    /**
     * Processes container events occurring on this container by
     * dispatching them to any registered ContainerListener objects.
     * NOTE: This method will not be called unless container events
     * are enabled for this component; this happens when one of the
     * following occurs:
     * a) A ContainerListener object is registered via addContainerListener()
     * b) Container events are enabled via enableEvents()
     * @see Component#enableEvents
     * @param e the container event
     */
    protected void processContainerEvent(ContainerEvent e) {
        if (containerListener != null && containerListener.size()>0) {
            switch(e.getID()) {
            case ContainerEvent.COMPONENT_ADDED:
                {
                    Object[] cls = containerListener.toArray();
                    for ( int i=0; i<cls.length; i++ )
                        ((ContainerListener)cls[i]).componentAdded(e);
                    break;
                }

            case ContainerEvent.COMPONENT_REMOVED:
                {
                    Object[] cls = containerListener.toArray();
                    for ( int i=0; i<cls.length; i++ )
                        ((ContainerListener)cls[i]).componentRemoved(e);
                    break;
                }
            }
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getComponentCount() {
        return components.size();
    }

    /**
     * TODO: documentation
     *
     * @param i
     * @return
     */
    public SComponent getComponentAt(int i) {
        return (SComponent)components.get(i);
    }

    /**
     * TODO: documentation
     *
     * @param i
     * @return
     */
    public SComponent getComponent(int i) {
        return (SComponent)components.get(i);
    }

    public SComponent[] getComponents() {
        // vorsichtig mit Threads ( eigentlich TreeLock!!!)
        return (SComponent[])components.toArray(new SComponent[components.size()]);
    }


    /*
     * Entfernt die Komponente gleichzeitig aus dem LayoutManager,
     * aus dem internen ArrayList und vom Dispatcher.
     */
    /**
     * TODO: documentation
     *
     * @param c
     * @return
     */
    public boolean removeComponent(SComponent c) {
        if ( c==null )
            return false;

        if ( layout!=null )
            layout.removeComponent(c);

        c.setParent(null);

        int index = components.indexOf(c);
        boolean erg = components.remove(c);
        if ( erg ) {
            constraints.remove(index);
            /*
             processContainerEvent(new ContainerEvent(this,
             ContainerEvent.COMPONENT_REMOVED,
             c));
             */
        }
        return erg;
    }

    /**
     * TODO: documentation
     *
     * @param i
     * @return
     */
    public SComponent removeComponentAt(int i) {
        SComponent c = getComponentAt(i);
        removeComponent(c);
        return c;
    }

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void remove(SComponent c) {
        removeComponent(c);
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void remove(int i) {
        removeComponentAt(i);
    }

    /**
     * TODO: documentation
     *
     */
    public void removeAllComponents() {
        removeAll();
    }

    /**
     * TODO: documentation
     *
     */
    public void removeAll() {
        while ( getComponentCount()>0 )
            removeComponentAt(0);
    }

    /**
     * TODO: documentation
     *
     * @param c
     * @return
     */
    public SComponent addComponent(SComponent c) {
        return addComponent(c, null);
    }

    public SComponent addComponent(SComponent c, Object constraint) {
        if (c != null) {
            if (layout != null)
                layout.addComponent(c, constraint);
            components.add(c);
            constraints.add(constraint);
            c.setParent(this);

            /*
             processContainerEvent(new ContainerEvent(this,
             ContainerEvent.COMPONENT_ADDED,
             c));
             */

        }

        return c;
    }

    /**
     * TODO: documentation
     *
     * @param f
     */
    protected void setParentFrame(SFrame f) {
        if ( f!=parentFrame ) {
            super.setParentFrame(f);
            for ( int i=0; i<components.size(); i++ ) {
                ((SComponent)components.get(i)).setParentFrame(getParentFrame());
            }
        }
    }

    /**
     * TODO: documentation
     *
     * @param c
     * @return
     */
    public SComponent add(SComponent c) {
        return addComponent(c, null);
    }

    public void add(SComponent c, Object constraint) {
        addComponent(c, constraint);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Object clone() {
        try {
            // ein bisschen kompliziert, muss aber alle Elemente einzeln
            // klonen !!
            SContainer erg = (SContainer)super.clone();
            erg.components = new ArrayList(2);
            for ( int i=0; i<components.size(); i++ ) {
                erg.addComponent((SComponent)((SComponent)components.get(i)).clone());
            }
            return erg;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Ist ganz geschickt um Dinge erst dann aufzubauen, wenn auch ein Frame da
     * ist. Z.B. Locale sensitive Dinge.
     */
    /**
     * TODO: documentation
     *
     */
    protected void addedToFrame() {
        for ( int i=0; i<getComponentCount(); i++ ) {
            getComponentAt(i).addedToFrame();
        }
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "ContainerCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
