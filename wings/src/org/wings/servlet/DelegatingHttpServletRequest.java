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

import java.io.*;
import java.util.*;
import java.security.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Delegates all method calls to another HttpServletRequest.
 * Subclass it and override some methods.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DelegatingHttpServletRequest
    implements HttpServletRequest
{
    private HttpServletRequest req;

    /**
     * The request all method calls are delegated to.
     */
    protected final HttpServletRequest getRequest() {
        return req;
    }

    /**
     * @param req The request all method calls are delegated to.
     */
    public DelegatingHttpServletRequest(HttpServletRequest req) {
        this.req = req;
    }

    /*---- ServletRequest methods ----*/

    /**
     * Returns a buffered reader for reading text in the request body.
     * This translates character set encodings as appropriate.
     *
     * @see getInputStream
     * @return
     * @throws IOException
     */
    public BufferedReader getReader()
        throws IOException {
            return req.getReader();
        }

    /**
     * Returns the size of the request entity data, or -1 if not known.
     * Same as the CGI variable CONTENT_LENGTH.
     *
     * @return
     */
    public int getContentLength() {
        return req.getContentLength();
    }

    /**
     * Returns the Internet Media Type of the request entity data, or
     * null if not known. Same as the CGI variable CONTENT_TYPE.
     *
     * @return
     */
    public String getContentType() {
        return req.getContentType();
    }

    /**
     * Returns the protocol and version of the request as a string of
     * the form <code>&lt;protocol&gt;/&lt;major version&gt;.&lt;minor
     * version&gt</code>.  Same as the CGI variable SERVER_PROTOCOL.
     *
     * @return
     */
    public String getProtocol() {
        return req.getProtocol();
    }

    /**
     * Returns the scheme of the URL used in this request, for example
     * "http", "https", or "ftp".  Different schemes have different
     * rules for constructing URLs, as noted in RFC 1738.  The URL used
     * to create a request may be reconstructed using this scheme, the
     * server name and port, and additional information such as URIs.
     *
     * @return
     */
    public String getScheme()  {
        return req.getScheme();
    }

    /**
     * Returns the host name of the server that received the request.
     * Same as the CGI variable SERVER_NAME.
     *
     * @return
     */
    public String getServerName() {
        return req.getServerName();
    }

    /**
     * Returns the port number on which this request was received.
     * Same as the CGI variable SERVER_PORT.
     *
     * @return
     */
    public int getServerPort() {
        return req.getServerPort();
    }

    /**
     * Returns the IP address of the agent that sent the request.
     * Same as the CGI variable REMOTE_ADDR.
     *
     * @return
     */
    public String getRemoteAddr() {
        return req.getRemoteAddr();
    }

    /**
     * Returns the fully qualified host name of the agent that sent the
     * request. Same as the CGI variable REMOTE_HOST.
     *
     * @return
     */
    public String getRemoteHost() {
        return req.getRemoteHost();
    }

    /**
     * Applies alias rules to the specified virtual path and returns
     * the corresponding real path, or null if the translation can not
     * be performed for any reason.  For example, an HTTP servlet would
     * resolve the path using the virtual docroot, if virtual hosting
     * is enabled, and with the default docroot otherwise.  Calling
     * this method with the string "/" as an argument returns the
     * document root.
     *
     * @param path the virtual path to be translated to a real path
     * @return
     */
    public String getRealPath(String path) {
        return req.getRealPath(path);
    }

    /**
     * Returns an input stream for reading the request body.
     *
     * @return
     * @throws IOException
     */
    public ServletInputStream getInputStream()
        throws IOException {

        return req.getInputStream();
    }

    /**
     * Returns a string containing the lone value of the specified
     * parameter, or null if the parameter does not exist. For example,
     * in an HTTP servlet this method would return the value of the
     * specified query string parameter. Servlet writers should use
     * this method only when they are sure that there is only one value
     * for the parameter.  If the parameter has (or could have)
     * multiple values, servlet writers should use
     * getParameterValues. If a multiple valued parameter name is
     * passed as an argument, the return value is implementation
     * dependent: for this class, the parameter set with a
     * <tt>&lt;PARAM&gt;</tt> tag will be preferred to those posted by
     * the user.
     *
     * @param name the name of the parameter whose value is required.
     * @deprecated Please use getParameterValues
     * @see javax.servlet.ServletRequest#getParameterValues
     */
    public String getParameter(String name) {
        return req.getParameter(name);
    }

    /**
     * Returns the values of the specified parameter for the request as
     * an array of strings, or null if the named parameter does not
     * exist.  In an HTTP servlet included via the
     * <tt>&lt;SERVLET&gt;</tt> tag, these values would also include
     * any values set via a <tt>&lt;PARAM&gt;</tt> tag.
     *
     * @param name the name of the parameter whose value is required
     * @return list of parameter values
     * @see javax.servlet.ServletRequest#getParameter
     */
    public String[] getParameterValues(String name) {
        return req.getParameterValues(name);
    }

    /**
     * Returns the parameter names for this request as an enumeration
     * of strings, or an empty enumeration if there are no parameters
     * or the input stream is empty.  The input stream would be empty
     * if all the data had been read from the stream returned by the
     * method getInputStream.
     *
     * @return
     */
    public Enumeration getParameterNames() {
        return req.getParameterNames();
    }

    /**
     * Returns the value of the named attribute of the request, or
     * null if the attribute does not exist.  This method allows
     * access to request information not already provided by the other
     * methods in this interface.  Attribute names should follow the
     * same convention as package names.  The package names java.*,
     * and javax.* are reserved for use by Javasoft, and com.sun.* is
     * reserved for use by Sun Microsystems.
     *
     * @param name the name of the attribute whose value is required
     * @return
     */
    public Object getAttribute(String name) {
        return req.getAttribute(name);
    }

    /*---- HttpServletRequest ----*/

    /**
     * Returns the method with which the request was made. The returned
     * value can be "GET", "HEAD", "POST", or an extension method. Same
     * as the CGI variable REQUEST_METHOD.
     *
     * @return
     */
    public String getMethod() {
        return req.getMethod();
    }

    /**
     * Returns the request URI as a URL object.
     *
     * @return
     */
    public String getRequestURI() {
        return req.getRequestURI();
    }

    /**
     * Returns the part of the request URI that refers to the servlet
     * being invoked. Analogous to the CGI variable SCRIPT_NAME.
     *
     * @return
     */
    public String getServletPath() {
        return req.getServletPath();
    }

    /**
     * Returns optional extra path information following the servlet
     * path, but immediately preceding the query string. Returns null if
     * not specified. Same as the CGI variable PATH_INFO.
     *
     * @return
     */
    public String getPathInfo() {
        return req.getPathInfo();
    }

    /**
     * Returns extra path information translated to a real path. Returns
     * null if no extra path information specified. Same as the CGI variable
     * PATH_TRANSLATED.
     *
     * @return
     */
    public String getPathTranslated() {
        return req.getPathTranslated();
    }

    /**
     * Returns the query string part of the servlet URI, or null if none.
     * Same as the CGI variable QUERY_STRING.
     *
     * @return
     */
    public String getQueryString() {
        return req.getQueryString();
    }

    /**
     * Returns the name of the user making this request, or null if not
     * known.  The user name is set with HTTP authentication.  Whether
     * the user name will continue to be sent with each subsequent
     * communication is browser-dependent.  Same as the CGI variable
     * REMOTE_USER.
     *
     * @return
     */
    public String getRemoteUser() {
        return req.getRemoteUser();
    }

    /**
     * Returns the authentication scheme of the request, or null if none.
     * Same as the CGI variable AUTH_TYPE.
     *
     * @return
     */
    public String getAuthType() {
        return req.getAuthType();
    }

    /**
     * Returns the value of a header field, or null if not known.
     * The case of the header field name is ignored.
     * @param name the case-insensitive header field name
     *
     * @param name
     * @return
     */
    public String getHeader(String name) {
        return req.getHeader(name);
    }

    /**
     * Returns the value of an integer header field, or -1 if not found.
     * The case of the header field name is ignored.
     * @param name the case-insensitive header field name
     *
     * @param name
     * @return
     */
    public int getIntHeader(String name) {
        return req.getIntHeader(name);
    }

    /**
     * Returns the value of a date header field, or -1 if not found.
     * The case of the header field name is ignored.
     * @param name the case-insensitive header field name
     *
     * @param name
     * @return
     */
    public long getDateHeader(String name) {
        return req.getDateHeader(name);
    }

    /**
     * Returns an enumeration of strings representing the header names
     * for this request. Some server implementations do not allow headers
     * to be accessed in this way, in which case this method will return null.
     *
     * @return
     */
    public Enumeration getHeaderNames() {
        return req.getHeaderNames();
    }

    /**
     * Checks whether this request is associated with a session that is
     * valid in the current session context. If it is not valid, the
     * requested session will never be returned from the getSession
     * method.
     *
     * @return
     */
    public boolean isRequestedSessionIdValid()
    {
        return req.isRequestedSessionIdValid();
    }

    /**
     * Checks whether the session id specified by this request came
     * in as part of the URL. (The requested session may not be the
     * one returned by the getSession method.)
     *
     * @return
     */
    public boolean isRequestedSessionIdFromCookie() {
        return req.isRequestedSessionIdFromCookie();
    }

    /**
     * TODO: documentation
     *
     * @param value
     * @return
     */
    public HttpSession getSession(boolean value) {
        return req.getSession(value);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getCharacterEncoding() {
        return req.getCharacterEncoding();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getRequestedSessionId() {
        return req.getRequestedSessionId();
    }

    public Cookie[] getCookies() {
        return req.getCookies();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isRequestedSessionIdFromUrl() {
        return req.isRequestedSessionIdFromUrl();
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

    /*
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }
    */

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
