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

import java.io.File;
import java.io.FilterOutputStream;
import java.util.Hashtable;
import java.util.logging.*;

import javax.servlet.http.HttpUtils;

import org.wings.plaf.FileChooserCG;
import org.wings.io.Device;

/**
 * A filechooser shows a textfiled with
 * a browse-button to enter a file. The file is uploaded via HTTP and
 * made accessible to the WingS application. 
 *
 * <p>The uploaded file is stored temporarily in the filesystem of the 
 * server with a unique name, so that uploaded files with the same 
 * filename do not clash. You can access this internal name with 
 * the {@link #getFiledir()} and {@link #getFileid()} methods. 
 * The user provided filename can be queried with the 
 * {@link #getFilename()} method.
 *
 * Since the file is stored temporarily in the filesystem, you should
 * call {@link #reset()} after you are done with the file - this removes
 * the temporary file and thus avoids a 'filesystem memory'-leak in your 
 * application.
 *
 * <p>If you don't call reset, the file is eventually be removed
 * by the Java garbage collector, if you haven't renamed it.
 *
 * <p>The form, you add this FileChooser needs to have the encoding type
 * <code>multipart/form-data</code> set 
 * (form.setEncodingType("multipart/form-data")).
 * @author <a href="mailto:HEngels@mercatis.de">Holger Engels</a>
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision$
 */
public class SFileChooser
    extends SComponent
    implements RequestListener
{
    private static final String cgClassID = "FileChooserCG";

    /**
     * maximum visible amount of characters in the file chooser.
     */
    protected int columns = 16;

    /**
     * maximum allowed number of columns. Default: none.
     */
    protected int maxColumns = -1;

    protected String fileNameFilter = null;

    protected Class filter    = null;
    protected String filedir  = null;
    protected String filename = null;
    protected String fileid   = null;
    protected String filetype = null;

    /**
     * the temporary file created on upload. This file is automatically
     * removed if and when it is not accessible anymore.
     */
    protected TempFile currentFile = null;

    /**
     * Creates a new FileChooser. 
     */
    public SFileChooser() {}

    /**
     * Set the visible amount of columns in the textfield.
     *
     * @param c columns; '-1' sets the default that is browser dependent.
     */
    public void setColumns(int c) {
        int oldColumns = columns;
        columns = c;
        if (columns != oldColumns)
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     * returns the number of visible columns.
     *
     * @return number of visible columns.
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
        int oldMaxColumns = maxColumns;
        maxColumns = mc;
        if (maxColumns != oldMaxColumns)
            reload(ReloadManager.RELOAD_CODE);
    }

    /**
     *
     *
     * @return
     */
    public int getMaxColumns() {
        return maxColumns;
    }

    /**
     * The filter for filenames, like '*.txt'; This seems to be ignored
     * by current browsers.
     *
     * @param s
     */
    public void setFileNameFilter(String s) {
        fileNameFilter = s;
    }

    /**
     * returns the current filename filter.
     *
     * @return the current filename filter or 'null', if no filename filter
     *         is provided.
     */
    public String getFileNameFilter() {
        return fileNameFilter;
    }

    /**
     * Returns the filename, that has been given by the user in the
     * upload text-field.
     *
     * @return the filename, given by the user.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Returns the name of the system directory, the file has been stored 
     * temporarily in. You won't need this, unless you want to access the
     * file directly.  Don't store the value you receive here for use later, 
     * since the SFileChooser does its own garbage collecting of unused files.
     *
     * @return the pathname of the system directory, the file is stored in.
     */
    public String getFiledir() {
        return filedir;
    }

    /**
     * Returns the internal ID of this file, that has been assigned at upload
     * time. This ID is unique to prevent clashes with other uploaded files.
     * You won't need this, unless you want to access the file directly. Don't
     * store the value you receive here for use later, since the SFileChooser
     * does its own garbage collecting of unused files.
     *
     * @return the internal, unique file id given to the uploaded file.
     */
    public String getFileid() {
        return fileid;
    }

    /**
     * Returns the mime type of this file, if known.
     *
     * @return the mime type of this file.
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
     * resets this FileChooser. This removes the temporary file.
     */
    public void reset() {
        if (currentFile != null) {
            currentFile.cleanup();
            currentFile = null;
            fileid  = null;
            filedir = null;
        }
    }

    /**
     * returns the file, that has been uploaded. Use this, to open and
     * read from the file uploaded by the user. Don't use this method
     * to query the actual filename given by the user, since this file
     * wraps a system generated file that has not the filename given by
     * the user. Use {@link #getFilename()} instead. The file returned is
     * not removed from the filesystem until you loose the reference to
     * it. If you rename the file, it is not removed from the filesystem.
     *
     * @return a File to access the content of the uploaded file.
     */
    public File getFile() {
        return currentFile;
    }

    /**
     * An FilterOutputStream, that filters incoming files. You can use 
     * UploadFilters to inspect the stream or rewrite it to some own
     * format.
     * 
     * @param filter the Class that is instanciated to filter incoming
     *               files.
     */
    public void setUploadFilter(Class filter) {
        if (!FilterOutputStream.class.isAssignableFrom(filter))
            throw new IllegalArgumentException(filter.getName() + " is not a FilterOutputStream!");

        UploadFilterManager.registerFilter(getNamePrefix(), filter);
        this.filter = filter;
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

    // -- Implementation of RequestListener
    public void processRequest(String action, String[] values) {
        String value = values[0];
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
            if (filedir != null && fileid != null) {
                currentFile = new TempFile(filedir, fileid);
            }
        }
        catch ( Exception e ) {
            logger.log(Level.SEVERE, null, e);
        }
    }
    public void fireIntermediateEvents() {
    }
    public void fireFinalEvents() {
    }

    /**
     * A temporary file. This file removes its representation in the
     * filesysten, when there are no references to it.
     */
    private static class TempFile extends File {
        private boolean isTemp;
        public TempFile(String parent, String child) {
            super(parent, child);
            isTemp = true;
        }

        /**
         * when this file is renamed, then it is not temporary anymore,
         * thus will not be removed on cleanup.
         */
        public boolean renameTo(File newfile) {
            boolean success;
            success = super.renameTo(newfile);
            isTemp &= !success; // we are not temporary anymore on success.
            return success;
        }

        /**
         * removes the file in the filesystem, if it is still temporary.
         */
        private void cleanup() {
            if (isTemp) {
                delete();
            }
        }
        
        /**
         * do a cleanup, if this temporary file is garbage collected.
         */
        protected void finalize() throws Throwable {
            super.finalize();
            cleanup();
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
