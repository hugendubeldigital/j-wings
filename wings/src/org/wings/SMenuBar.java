/*
 * $id: SMenuBar.java,v 1.2.2.2 2001/11/26 08:52:58 hzeller Exp $
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;
import java.beans.*;
import java.util.ArrayList;
import java.net.URL;
import javax.swing.*;

import java.util.ArrayList;

import org.wings.plaf.*;
import org.wings.io.Device;
import org.wings.externalizer.ExternalizeManager;

/**
 * An implementation of a MenuBar. You add SMenu objects to the
 * menu bar to construct a menu. When the user selects a SMenu
 * object, its associated {@link org.wings.SMenu} is displayed, allowing the
 * user to select one of the {@link org.wings.SMenuItem}s on it.
 * <p>
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @see SMenu
 * @see SMenuItem
 */
public class SMenuBar extends SContainer
{    
    /**
     * @see #getUIClassID
     * @see #readObject
     */
    private static final String cgClassID = "MenuBarCG";

    /*
     * Model for the selected subcontrol
     */
    private transient SingleSelectionModel selectionModel;

    private boolean paintBorder           = true;
    private Insets     margin             = null;

    /**
     * SMenu's
     */
    protected final ArrayList fComponents = new ArrayList();

    /**
     * Creates a new menu bar.
     */
    public SMenuBar() {
        super();
        setSelectionModel(new DefaultSingleSelectionModel());
    }

    /**
     * Returns the model object that handles single selections.
     *
     * @return the SingleSelectionModel in use
     * @see SingleSelectionModel
     */
    public SingleSelectionModel getSelectionModel() {
        return selectionModel;
    }

    /**
     * Set the model object to handle single selections.
     *
     * @param model the SingleSelectionModel to use
     * @see SingleSelectionModel
     * @beaninfo
     *       bound: true
     * description: The selection model, recording which child is selected.
     */
    public void setSelectionModel(SingleSelectionModel model) {
	SingleSelectionModel oldValue = selectionModel;
        this.selectionModel = model;
        firePropertyChange("selectionModel", oldValue, selectionModel);
    }


    /**
     * Appends the specified menu to the end of the menu bar.
     *
     * @param c the SMenu component to add
     */
    public SMenuItem add(SMenuItem c) {
        super.add( c );
        
        if ( c instanceof SMenu ) {
            c.addActionListener( new MenuAction( this ) );
        }
        c.setShowAsFormComponent( false );
    	fComponents.add(c);
        return c;
    }

    /**
     * Gets the menu at the specified position in the menu bar.
     *
     * @param index  an int giving the position in the menu bar, where
     *               0 is the first position
     * @return the SMenu at that position
     */
    public SMenuItem getMenuItem(int index) {
        SComponent c = (SComponent) fComponents.get(index);
        if (c instanceof SMenuItem) 
            return (SMenuItem) c;
        return null;
    }

    /**
     * Returns the number of items in the menu bar.
     *
     * @return the number of items in the menu bar
     */
    public int getMenuCount() {
        return fComponents.size();
    }

    /**
     * Returns the component at the specified index.
     * This method is obsolete, please use <code>getComponent(int i)</code> instead.
     *
     * @param i an int specifying the position, where 0 = first
     * @return the SComponent at the position, or null for an
     *         invalid index
     */
    public SComponent getComponentAtIndex(int i) {	
	return (SComponent) fComponents.get(i);
    }

    /**
     * Returns the index of the specified component.
     *
     * @param c  the SComponent to find
     * @return an int giving the component's position, where 0 = first
     */
    public int getComponentIndex(SComponent c) {
        return fComponents.indexOf(c);
    }

    /**
     * Sets the currently selected component, producing a
     * a change to the selection model.
     *
     * @param sel the SComponent to select
     */
    public void setSelected(SComponent sel) {    
        SingleSelectionModel model = getSelectionModel();
        int index = getComponentIndex(sel);
        model.setSelectedIndex(index);
    }

    /**
     * Returns true if the MenuBar currently has a component selected
     *
     * @return true if a selection has been made, else false
     */
    public boolean isSelected() {       
        return selectionModel.isSelected();
    }

    /** 
     * Returns true if the Menubar's border should be painted.
     *
     * @return  true if the border should be painted, else false
     */
    public boolean isBorderPainted() {
        return paintBorder;
    }

    /**
     * Sets whether the border should be painted.
     * @param b if true and border property is not null, the border is painted.
     * @see #isBorderPainted
     * @beaninfo
     *        bound: true
     *    attribute: visualUpdate true
     *  description: Whether the border should be painted.
     */
    public void setBorderPainted(boolean b) {
        boolean oldValue = paintBorder;
        paintBorder = b;
        firePropertyChange("borderPainted", oldValue ? Boolean.TRUE : Boolean.FALSE, 
                           paintBorder ? Boolean.TRUE : Boolean.FALSE );
        if (b != oldValue) {
            /*
              revalidate();
              repaint();
            */
        }
    }

    /**
     * Paint the menubar's border if BorderPainted property is true.
     * 
     * @param g the Graphics context to use for painting
     * @see SComponent#paint
     * @see SComponent#setBorder
     */
    /*
    protected void paintBorder(Graphics g) {
        if (isBorderPainted()) {
            super.paintBorder(g);
        }
    }
    */

    /**
     * Sets the margin between the menubar's border and
     * its menus. Setting to null will cause the menubar to
     * use the default margins.
     *
     * @param margin an Insets object containing the margin values
     * @see Insets
     * @beaninfo
     *        bound: true
     *    attribute: visualUpdate true
     *  description: The space between the menubar's border and its contents
     */
    public void setMargin(Insets m) {
        Insets old = margin;
        this.margin = m;
        firePropertyChange("margin", old, m);
        if (old == null || !m.equals(old)) {
            /*
              revalidate();
              repaint();
            */
        }
    }

    /**
     * Returns the margin between the menubar's border and
     * its menus.
     * 
     * @return an Insets object containing the margin values
     * @see Insets
     */
    public Insets getMargin() {
        if(margin == null) {
            return new Insets(0,0,0,0);
        } else {
            return margin;
        }
    }


    /**
     * Implemented to be a MenuElement -- does nothing.
     *
     * @see #getSubElements
     */
    public void menuSelectionChanged(boolean isIncluded) {
    }

    /**
     * Returns a string representation of this SMenuBar. This method 
     * is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this SMenuBar.
     */
    public String paramString() {
	String paintBorderString = (paintBorder ?
				    "true" : "false");
	String marginString = (margin != null ?
			       margin.toString() : "");

	return super.paramString() +
            ",margin=" + marginString +
            ",paintBorder=" + paintBorderString;
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(MenuBarCG cg) {
        super.setCG(cg);
    }

    /**
     * Close all currently open menus.
     */
    public void closeAllMenus()
    {
        /*        for ( int i = 0; i < fComponents.size(); i++ ) {
            if ( fComponents.get(i) instanceof SMenu ) {
                ((SMenu) fComponents.get(i)).setActive( false );
            }
            }*/
    }

    /**
     * Handle open and close of menus
     */
    class MenuAction implements ActionListener
    {
        private SMenuBar fMenuBar = null;

        public MenuAction( SMenuBar mbar )
        {
            fMenuBar = mbar;
        }

        public void actionPerformed( ActionEvent e )
        {
            SMenu menu = (SMenu) e.getSource();
            //            boolean active = menu.isActive();
            //            fMenuBar.closeAllMenus();
            //menu.setActive( ! active );
        }
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
