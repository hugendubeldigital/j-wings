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

package org.wings.template;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.util.Hashtable;

import org.apache.servlet.ssi.FileDataSource;

/**
 * A <CODE>CachedFileDataSource</CODE> implements a DataSource
 * for a file, but caches small ones.
 *
 * @see org.apache.servlet.ssi.DataSource
 * @author <A href="mailto:zeller@think.de">Henner Zeller</A>
 * @version $Revision$
 */
public class CachedFileDataSource
    extends FileDataSource
{
    private final class CacheEntry {
        private byte[] filebuffer = null;
        private long lastModified;
        private File file;

        public CacheEntry (File f) throws IOException {
            this.file = f;
            lastModified = -1;
            checkModified();
        }

        public byte[] getBuffer() {
            return filebuffer;
        }

        /**
         * returns the time, this file has been
         * last modified. This checks the Timestamp of
         * the file and initiates a reload to the cache
         * if it changed.
         */
        public long lastModified() {
            checkModified();
            return lastModified;
        }

        private void checkModified() {
            long timestamp = file.lastModified();
            if (lastModified != timestamp) {
                lastModified = timestamp;
                try {
                    refresh();
                } catch (IOException e) { /* ignore currently */ }
            }
        }

        private void refresh () throws IOException {
            int len = (int) file.length();
            filebuffer = new byte [ len ];
            FileInputStream in = new FileInputStream (file);
            int pos = 0;
            while (pos < len) {
                pos += in.read (filebuffer, pos, len - pos);
            }
        }
    }

    /*
     * we should provide a way to expunge old
     * entries here ...
     */
    private static Hashtable cache = new Hashtable();
    private static final int CACHED_LIMIT = 1024;

    private CacheEntry entry;

    public CachedFileDataSource (File f)
        throws IOException
    {
        super (f);
        entry = (CacheEntry) cache.get (f);
        if (entry == null && f.length() <= CACHED_LIMIT) {
            entry = new CacheEntry (f);
            cache.put (f, entry);
        }
    }


    /**
     * Returns the time the content of this File
     * was last modified.
     * <p>
     * The return value is used to decide whether to reparse a
     * Source or not. Reparsing is done if the value returned
     * here differs from the value returned at the last processing
     * time.
     *
     * @return long a modification time
     */
    public long lastModified() {
        if (entry != null)
            return entry.lastModified();
        else
            return super.lastModified();
    }

    /**
     * Gets an InputStream of the File.
     */
    public InputStream  getInputStream()
        throws IOException
    {
        if (entry != null)
            return new ByteArrayInputStream (entry.getBuffer());
        else
            return super.getInputStream();
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
