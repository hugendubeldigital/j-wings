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

import java.io.IOException;
import java.util.*;

import org.wings.plaf.*;
import org.wings.io.Device;
import org.wings.util.*;
import org.wings.event.SContainerListener;
import org.wings.event.SContainerEvent;

/**
 * This is a container which can hold several other <code>SComponents</code>.
 *
 * @see SLayoutManager
 * @see SComponent
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SContainer extends SComponent implements ClickableRenderComponent
{
    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "ContainerCG";

    /**
     * The constraints for the component.
     */
    protected ArrayList containerListener;

    /**
     * The layout for the component.
     */
    protected SLayoutManager layout;

    /**
     * creates a new container with the given layout
     *
     * @param l the layout for this container
     */
    public SContainer(SLayoutManager l) {
        setLayout(l);
    }

    /**
     * creates a new container with no layout manager, i.e. the components
     * are simply written in the same order they were added.
     */
    public SContainer() {}


    public void updateCG() {
        super.updateCG();

        if (layout != null)
            layout.updateCG();
    }

    /**
     * Sets a new layout manager.
     *
     * @param l new layout manager
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
     * Returns the current layout
     *
     * @return current layout
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
    public void addContainerListener(SContainerListener l) {
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
    public void removeContainerListener(SContainerListener l) {
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
     * @see SComponent#enableEvents
     * @param e the container event
     */
    protected void processContainerEvent(SContainerEvent e) {
        if (containerListener != null && containerListener.size()>0) {
            Iterator it=containerListener.iterator();
            switch(e.getID()) {
            case SContainerEvent.COMPONENT_ADDED:
                while (it.hasNext())
                    ((SContainerListener)it.next()).componentAdded(e);
                break;

            case SContainerEvent.COMPONENT_REMOVED:
                while (it.hasNext())
                    ((SContainerListener)it.next()).componentRemoved(e);
                break;
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
     * returns the number of components in this container
     *
     * @return number of components
     */
    public int getComponentCount() {
        return getComponentList().size();
    }

    /**
     * returns the component at the given position
     *
     * @param i position
     * @return component at given pos
     */
    public SComponent getComponentAt(int i) {
        return (SComponent)getComponentList().get(i);
    }

    /**
     * returns the component at the given position
     *
     * @param i position
     * @return component at given pos
     */
    public SComponent getComponent(int i) {
        return getComponentAt(i);
    }

    public SComponent[] getComponents() {
        // vorsichtig mit Threads ( eigentlich TreeLock!!!)
        return (SComponent[])getComponentList().toArray(new SComponent[getComponentCount()]);
    }


    /**
     * returns the constraint for the given component position
     *
     * @param i position
     * @return constraint for component at given position
     */
    public Object getConstraintAt(int i) {
        return getConstraintList().get(i);
    }



    /**
     * Removes the given component from the container. This includes removing
     * it from the LayoutManager (if set), the internal list and
     * the dispatcher.
     *
     * @param c the component to remove
     * @return true if the component was found and removed; false otherwise.
     */
    public boolean removeComponent(SComponent c) {
        if ( c==null )
            return false;

        if ( layout!=null )
            layout.removeComponent(c);

        int index = getComponentList().indexOf(c);
        boolean isRemoved = getComponentList().remove(c);
        if ( isRemoved ) {
            getConstraintList().remove(index);

            processContainerEvent(new SContainerEvent(this,
                                  SContainerEvent.COMPONENT_REMOVED, c));
            c.setParent(null);
        }
        return isRemoved;
    }

    /**
     * Removes the component at the given position from the container.
     *
     * @param i the position of the component to remove
     * @return the removed component
     * @see #removeComponent(org.wings.SComponent)
     */
    public SComponent removeComponentAt(int i) {
        SComponent c = getComponentAt(i);
        removeComponent(c);
        return c;
    }

    /**
     * Removes the component at the given position from the container.
     *
     * @param c
     * @see #removeComponent(org.wings.SComponent)
     */
    public void remove(SComponent c) {
        removeComponent(c);
    }

    /**
     * Removes the component at the given position from the container.
     *
     * @param i
     * @see #removeComponent(int)
     */
    public void remove(int i) {
        removeComponentAt(i);
    }

    /**
     * Removes all components from the container.
     */
    public void removeAllComponents() {
        removeAll();
    }

    /**
     * Removes all components from the container.
     */
    public void removeAll() {
        while ( getComponentCount() > 0 )
            removeComponentAt(0);
    }


    /**
     * Adds a component to the container with null constraint at the end
     * of the internal list.
     *
     * @param c the component to add
     * @return the added component
     */
    public SComponent add(SComponent c) {
        return addComponent(c, null);
    }

    /**
     * Adds a component to the container with the given constraint at the end
     * of the internal list.
     *
     * @param c the component to add
     * @param constraint the constraint for this component
     * @return the added component
     */
    public void add(SComponent c, Object constraint) {
        addComponent(c, constraint);
    }

    /**
     * Adds a component to the container with null constraint at the given
     * index.
     *
     * @param c the component to add
     * @param index the index of the component
     * @return the added component
     */
    public SComponent add(SComponent c, int index) {
        return addComponent(c, null, index);
    }

    /**
     * Adds a component to the container with the given constraint at
     * the given index.
     *
     * @param c the component to add
     * @param index the index of the component
     * @return the added component
     */
    public void add(SComponent c, Object constraint, int index) {
        addComponent(c, constraint, index);
    }

    /**
     * Adds a component to the container with null constraint at the end
     * of the internal list.
     *
     * @param c the component to add
     * @return the added component
     */
    public SComponent addComponent(SComponent c) {
        return addComponent(c, null);
    }

    /**
     * Adds a component to the container with the given constraint at the end
     * of the internal list.
     *
     * @param c the component to add
     * @param constraint the constraint for this component
     * @return the added component
     */
    public SComponent addComponent(SComponent c, Object constraint) {
        if (c != null) {
            if (layout != null)
                layout.addComponent(c, constraint);

            getComponentList().add(c);
            getConstraintList().add(constraint);
            c.setParent(this);

            processContainerEvent(new SContainerEvent(this,
                                  SContainerEvent.COMPONENT_ADDED, c));
        }

        return c;
    }

    /**
     * Adds a component to the container with the given constraint at
     * the given index.
     *
     * @param c the component to add
     * @param index the index of the component
     * @return the added component
     */
    public SComponent addComponent(SComponent c, int index) {
        return addComponent(c, null, index);
    }

    /**
     * Adds a component to the container with the given constraint at
     * the given index.
     *
     * @param c the component to add
     * @param index the index of the component
     * @return the added component
     */
    public SComponent addComponent(SComponent c, Object constraint, int index){
        if (c != null) {
            if (layout != null)
                layout.addComponent(c, constraint);

            getComponentList().add(index, c);
            getConstraintList().add(index, constraint);
            c.setParent(this);

            processContainerEvent(new SContainerEvent(this,
                                  SContainerEvent.COMPONENT_ADDED, c));
        }

        return c;
    }

    /**
     * Sets the parent frame.
     *
     * @param f parent frame
     */
    protected void setParentFrame(SFrame f) {
        if ( f!=parentFrame ) {
            super.setParentFrame(f);
            for ( int i=0; i<getComponentCount(); i++ ) {
                getComponentAt(i).setParentFrame(getParentFrame());
            }
        }
    }

    public Object clone() {
        try {
            SContainer erg = (SContainer)super.clone();
            // componentList and constraintList contain references to the
            // original components / constraints
            erg.componentList.clear();
            erg.constraintList.clear();
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

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(ContainerCG cg) {
        super.setCG(cg);
    }
    
    /**
     * Calls the visitor on each SComponent this container has. You might
     * want to call this in your visitor.
     *
     * @param visitor an implementation of the {@link ComponentVisitor}
     *                interface.
     */
    public void inviteEachComponent(ComponentVisitor visitor) 
        throws Exception {
	Iterator iterator = getComponentList().iterator();
	while (iterator.hasNext()) {
	    ((SComponent)iterator.next()).invite(visitor);
        }
    }

    /**
     * Invite a ComponentVisitor.
     *
     * @param visitor an implementation of the {@link ComponentVisitor}
     *                interface.
     */
    public void invite(ComponentVisitor visitor)
        throws Exception
    {
        visitor.visit(this);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
