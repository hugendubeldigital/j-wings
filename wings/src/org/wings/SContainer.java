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
public class SContainer
    extends SComponent
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "ContainerCG";

    /**
     * The constraints for the components.
     */
    protected ArrayList containerListener;

    SLayoutManager layout;

    /**
     * TODO: documentation
     *
     * @param l
     */
    public SContainer(SLayoutManager l) {
        setLayout(l);
    }

    /**
     * TODO: documentation
     *
     */
    public SContainer() {}


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
        if (layout != null) {
            for ( int i=0; i<getComponentCount(); i++ ) {
                layout.removeComponent(getComponentAt(i));
            }
        }

        layout = l;

        if ( layout!=null ) {
            for ( int i=0; i<getComponentCount(); i++ ) {
                layout.addComponent(getComponentAt(i), getConstraintAt(i));
            }

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
     * The components in this container.
     */
    private ArrayList componentList;

    protected ArrayList getComponentList() {
        if ( componentList == null )
            componentList = new ArrayList(3);
        return componentList;
    }

    /**
     * The constraints for the components.
     */
    private ArrayList constraintList;

    protected ArrayList getConstraintList() {
        if ( constraintList == null )
            constraintList = new ArrayList(3);
        return constraintList;
    }


    /**
     * TODO: documentation
     *
     * @return
     */
    public int getComponentCount() {
        return getComponentList().size();
    }

    /**
     * TODO: documentation
     *
     * @param i
     * @return
     */
    public SComponent getComponentAt(int i) {
        return (SComponent)getComponentList().get(i);
    }

    /**
     * TODO: documentation
     *
     * @param i
     * @return
     */
    public SComponent getComponent(int i) {
        return getComponentAt(i);
    }

    public SComponent[] getComponents() {
        // vorsichtig mit Threads ( eigentlich TreeLock!!!)
        return (SComponent[])getComponentList().toArray(new SComponent[getComponentCount()]);
    }


    /**
     * TODO: documentation
     *
     * @param i
     * @return
     */
    public Object getConstraintAt(int i) {
        return getConstraintList().get(i);
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

        int index = getComponentList().indexOf(c);
        boolean erg = getComponentList().remove(c);
        if ( erg ) {
            getConstraintList().remove(index);

            // processContainerEvent(new ContainerEvent(this,
            // ContainerEvent.COMPONENT_REMOVED, c));
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
        while ( getComponentCount() > 0 )
            removeComponentAt(0);
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

    public SComponent add(SComponent c, int index) {
        return addComponent(c, null, index);
    }

    public void add(SComponent c, Object constraint, int index) {
        addComponent(c, constraint, index);
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

            getComponentList().add(c);
            getConstraintList().add(constraint);
            c.setParent(this);

            // processContainerEvent(new ContainerEvent(this,
            // ContainerEvent.COMPONENT_ADDED, c));
        }

        return c;
    }

    public SComponent addComponent(SComponent c, int index) {
        return addComponent(c, null, index);
    }

    public SComponent addComponent(SComponent c, Object constraint, int index) {
        if (c != null) {
            if (layout != null)
                layout.addComponent(c, constraint);

            getComponentList().add(index, c);
            getConstraintList().add(index, constraint);
            c.setParent(this);

            // processContainerEvent(new ContainerEvent(this,
            // ContainerEvent.COMPONENT_ADDED, c));
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
            for ( int i=0; i<getComponentCount(); i++ ) {
                getComponentAt(i).setParentFrame(getParentFrame());
            }
        }
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
            // erg.components = new ArrayList(2);
            for ( int i=0; i<getComponentCount(); i++ ) {
                erg.addComponent((SComponent)getComponentAt(i).clone());
            }
            return erg;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public void setCG(ContainerCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
