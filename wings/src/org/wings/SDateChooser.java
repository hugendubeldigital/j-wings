/*
 * $Id$
 * (c) Copyright 2002 wingS development team.
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.wings.SComponent;
import org.wings.plaf.DateChooserCG;



/**
 * <!--
 * SDateChooser.java
 * Created: Mon Nov 18 20:34:25 2002
 * -->
 *
 *
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SDateChooser extends SComponent  {

    private static final String cgClassID = "DateChooserCG";

    private Calendar calendar;

    /**
     * 
     */
    public SDateChooser() {
	calendar = new GregorianCalendar(getSession().getLocale());
    }

    /**
     * 
     */
    public SDateChooser(Calendar calendar) {
	this.calendar = calendar;
    }

    public Calendar getCalendar() {
	return calendar;
    }

    public void setTimeInMillis(long millis) {
	calendar.setTimeInMillis(millis);
    }

    public long getTimeInMillis() {
	return calendar.getTimeInMillis();
    }

    public void setTime(Date d) {
	calendar.setTime(d);
    }

    public Date getTime() {
	return calendar.getTime();
    }


    public String getCGClassID() {
        return cgClassID;
    }
  
    public void setCG(DateChooserCG cg) {
        super.setCG(cg);
    }

    
}// SDateChooser

/*
   $Log$
   Revision 1.1  2002/11/19 19:21:01  ahaaf
   o initial

*/
