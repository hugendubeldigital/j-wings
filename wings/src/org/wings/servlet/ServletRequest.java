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

package org.wings.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Locale;

import java.security.Principal;

import javax.servlet.http.*;
import javax.servlet.*;

/*
 * ServletRequest.java
 *
 * Kapselt original Servlet Request, um Parameter aus der Parameter Liste
 * entfernen zu koennen. (Dispatchte Parameter werden entfernt).
 */
/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ServletRequest
    implements HttpServletRequest
{
    /**
     * TODO: documentation
     */
    protected HttpServletRequest orig = null;

    /**
     * TODO: documentation
     */
    protected Hashtable params = new Hashtable();

    /**
     * TODO: documentation
     *
     * @param o
     */
    public ServletRequest(HttpServletRequest o) {
        orig = o;
    }

    void addParam(String name, String[] values) {
        params.put(name, values);
    }

    // von ServletRequest

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getContentLength() {
        return orig.getContentLength();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getContentType() {
        return orig.getContentType();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getProtocol() {
        return orig.getProtocol();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getScheme() {
        return orig.getScheme();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getServerName() {
        return orig.getServerName();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getServerPort() {
        return orig.getServerPort();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getRemoteAddr() {
        return orig.getRemoteAddr();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getRemoteHost() {
        return orig.getRemoteHost();
    }

    /**
     * TODO: documentation
     *
     * @param path
     * @return
     * @deprecated
     */
    public String getRealPath(String path) {
        return orig.getRealPath(path);
    }

    /**
     * TODO: documentation
     *
     * @return
     * @throws IOException
     */
    public ServletInputStream getInputStream() throws IOException {
        return orig.getInputStream();
    }

    /**
     * TODO: documentation
     *
     * @param name
     * @return
     */
    public String getParameter(String name) {
        String[] v = getParameterValues(name);
        if ( v!=null && v.length>0 )
            return v[0];
        else
            return null;
    }

    public String[] getParameterValues(String name) {
        return (String[])params.get(name);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Enumeration getParameterNames() {
        return params.keys();
    }

    /**
     * TODO: documentation
     *
     * @param name
     * @return
     */
    public Object getAttribute(String name) {
        return orig.getAttribute(name);
    }

    /**
     * TODO: documentation
     *
     * @return
     * @throws IOException
     */
    public BufferedReader getReader() throws IOException {
        return orig.getReader();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getCharacterEncoding() {
        return orig.getCharacterEncoding();
    }

    // vpom HTTPServletRequest

    public Cookie[] getCookies() {
        return orig.getCookies();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getMethod() {
        return orig.getMethod();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getRequestURI() {
        return orig.getRequestURI();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getServletPath() {
        return orig.getServletPath();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getPathInfo() {
        return orig.getPathInfo();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getPathTranslated() {
        return orig.getPathTranslated();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getQueryString() {
        return orig.getQueryString();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getRemoteUser() {
        return orig.getRemoteUser();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getAuthType() {
        return orig.getAuthType();
    }

    /**
     * TODO: documentation
     *
     * @param name
     * @return
     */
    public String getHeader(String name) {
        return orig.getHeader(name);
    }

    /**
     * TODO: documentation
     *
     * @param name
     * @return
     */
    public int getIntHeader(String name) {
        return orig.getIntHeader(name);
    }

    /**
     * TODO: documentation
     *
     * @param name
     * @return
     */
    public long getDateHeader(String name) {
        return orig.getDateHeader(name);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Enumeration getHeaderNames() {
        return orig.getHeaderNames();
    }

    /**
     * TODO: documentation
     *
     * @param create
     * @return
     */
    public HttpSession getSession(boolean create) {
        return orig.getSession(create);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getRequestedSessionId() {
        return orig.getRequestedSessionId();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isRequestedSessionIdValid() {
        return orig.isRequestedSessionIdValid();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isRequestedSessionIdFromCookie()  {
        return orig.isRequestedSessionIdFromCookie();
    }

    /**
     * TODO: documentation
     *
     * @return
     * @deprecated
     */
    public boolean isRequestedSessionIdFromUrl() {
        return orig.isRequestedSessionIdFromUrl();
    }


    // TODO rhaag: Needed for JSDK in j2ee.jar

    public String getContextPath() {
        return null;
    }


    public Enumeration getHeaders(String s) {
        return null;
    }

    
    public HttpSession getSession() {
        return null;
    }


    public Principal getUserPrincipal() {
        return null;
    }


    public boolean isRequestedSessionIdFromURL() {
        return false;
    }


    public boolean isUserInRole(String s) {
        return false;
    }


    public Enumeration getAttributeNames() {
        return null;
    }


    public Locale getLocale() {
        return null;
    }


    public Enumeration getLocales() {
        return null;
    }

    // comment this out, if you compile against jsdk-2.0
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    public boolean isSecure() {
        return false;
    }

    
    public void removeAttribute(String s) {
    }


    public void setAttribute(String s, Object o) {
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
