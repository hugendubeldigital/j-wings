/*
 * Copyright (c) 1997-1999 The Java Apache Project.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. All advertising materials mentioning features or use of this
 *    software must display the following acknowledgment:
 *    "This product includes software developed by the Java Apache 
 *    Project for use in the Apache JServ servlet engine project
 *    (http://java.apache.org/)."
 *
 * 4. The names "Apache JServ", "Apache JServ Servlet Engine" and 
 *    "Java Apache Project" must not be used to endorse or promote products 
 *    derived from this software without prior written permission.
 *
 * 5. Products derived from this software may not be called "Apache JServ"
 *    nor may "Apache" nor "Apache JServ" appear in their names without 
 *    prior written permission of the Java Apache Project.
 *
 * 6. Redistributions of any form whatsoever must retain the following
 *    acknowledgment:
 *    "This product includes software developed by the Java Apache 
 *    Project for use in the Apache JServ servlet engine project
 *    (http://java.apache.org/)."
 *    
 * THIS SOFTWARE IS PROVIDED BY THE JAVA APACHE PROJECT "AS IS" AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE JAVA APACHE PROJECT OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Java Apache Group. For more information
 * on the Java Apache Project and the Apache JServ Servlet Engine project,
 * please see <http://java.apache.org/>.
 *
 */

/**
 * <CODE>DataSource</CODE> is a general Source
 * Template Pages can come from. You can think of
 * Files, URLs, Database-BLOBS ..
 *
 * A DataSource just has to implement this semantic
 * contract
 *
 * @version $Revision$ $Date$
 * @author <A href="mailto:zeller@think.de">Henner Zeller</A>
 *
 */

package org.wings.template.parser;

import java.io.IOException;
import java.io.InputStream;

public interface DataSource {
    /**
     * Returns a canonical name of this DataSource.
     * <p>
     * The name returned here is used to look up the parsing
     * result of the internal caching, so it should differ
     * for all different Sources :-)
     * May return <code>null</code> if this Source is supposed
     * to be parsed each time.
     */
    String getCanonicalName ();
    
    /**
     * Returns the time the content of this DataSource
     * was last modified. 
     * <p>
     * The return value is used to decide whether to reparse a
     * Source or not. Reparsing is done if the value returned
     * here <em>differs</em> from the value returned at the last processing 
     * time. This may not return a 'real' time, it needs just 
     * to be comparable to itself.
     *
     * @return long a modification time
     */
    long lastModified();
    
    /**
     * Gets an InputStream of this DataSource. 
     * <p>
     * <em>Note</em> 
     * that this method may be called twice if the page has to
     * be parsed first. So you probably have to implement a
     * buffer if your underlying source is transient ..
     */
    InputStream  getInputStream() throws IOException;
}

/* 
 * Local variables:
 * c-basic-offset: 4
 * End:
 */


