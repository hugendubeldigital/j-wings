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

import java.io.*;
import java.util.Hashtable;
import javax.servlet.http.HttpUtils;
import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author
 * @version $Revision$
 */
public class SFileChooser
    extends SComponent
    implements RequestListener
{
    private static final String cgClassID = "FileChooserCG";

    /**
     * maximum amount of letters
     */
    protected int columns = 12;

    /**
     * TODO: documentation
     */
    protected int maxColumns = 50;

    String fileNameFilter = null;

    Class filter = null;
    String filedir = null;
    String filename = null;
    String fileid = null;
    String filetype = null;

    /**
     * TODO: documentation
     *
     */
    public SFileChooser() {}

    /**
     * TODO: documentation
     *
     * @param c
     */
    public void setColumns(int c) {
        columns = c;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getColumns() {
        return columns;
    }

    /**
     * TODO: documentation
     *
     * @param mc
     */
    public void setMaxColumns(int mc) {
        maxColumns = mc;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getMaxColumns() {
        return maxColumns;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setFileNameFilter(String s) {
        fileNameFilter = s;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getFileNameFilter() {
        return fileNameFilter;
    }

    public void getPerformed(String action, String value) {
    	System.out.println( action + "-> '" + value + "'");
        try {
            Hashtable params = HttpUtils.parseQueryString(value);
            String[] arr;
            arr = (String[])params.get("dir");
            this.filedir = (arr != null)?arr[0]:null;
            arr = (String[])params.get("name");
            this.filename = (arr != null)?arr[0]:null;
            arr = (String[])params.get("id");
            this.fileid = (arr != null)?arr[0]:null;
            arr = (String[])params.get("type");
            this.filetype = (arr != null)?arr[0]:null;
        }
        catch ( Exception ex ) {
            System.out.println( "ex: " + ex );
        }
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getFiledir() {
        return filedir;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getFilename() {
        return filename;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getFileid() {
        return fileid;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getFiletype() {
        return filetype;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public File getSelectedFile() {
        return getFile();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public File getFile() {
        if (filedir == null || fileid == null)
            return null;
        else
            return new File(filedir, fileid);
    }

    /**
     * TODO: documentation
     *
     * @param filter
     */
    public void setUploadFilter(Class filter) {
        if (!FilterOutputStream.class.isAssignableFrom(filter))
            throw new IllegalArgumentException(filter.getName() + " is not a FilterOutputStream!");

        UploadFilterManager.registerFilter(getNamePrefix(), filter);
        this.filter = filter;
    }

    public void fireIntermediateEvents() {
    }
    public void fireFinalEvents() {
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Class getUploadFilter() { return filter; }

    /**
     * TODO: documentation
     *
     * @return
     */
    public FilterOutputStream getUploadFilterInstance() {
        return UploadFilterManager.getFilterInstance(getNamePrefix());
    }

    public String getCGClassID() {
        return cgClassID;
    }

    public void setCG(FileChooserCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
