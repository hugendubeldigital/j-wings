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

package wingset;

import org.wings.SComponent;
import org.wings.SDateChooser;
import org.wings.SForm;
import wingset.WingSetPane;



/**
 * <!--
 * DateChooserExample.java
 * Created: Mon Nov 18 21:11:24 2002
 * -->
 *
 *
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class DateChooserExample extends WingSetPane  {
    
    /**
     * 
     */
    public DateChooserExample() {
	
    }

    public SComponent createExample() {
	SForm form = new SForm();

	form.add(new SDateChooser());

	return form;
    }

    
}// DateChooserExample

/*
   $Log$
   Revision 1.5  2002/11/19 19:21:18  ahaaf
   o initial

*/
