/* $Id$ */
package org.wings.recorder;

import org.wings.util.StringUtil;

import javax.servlet.Filter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * @author hengels
 */
public class Recorder
    implements Filter
{
    private static Logger logger = Logger.getLogger(Recorder.class.getPackage().getName());
    public static final String RECORDER_START = "recorder_start";
    public static final String RECORDER_STOP = "recorder_stop";
    public static final String RECORDER_SCRIPT = "recorder_script";

    private File file;
    private List list;
    private String scriptName = "Recording";

    public void init(FilterConfig filterConfig) throws ServletException {
        if (filterConfig.getInitParameter("wings.servlet.recorder.script") != null)
            scriptName = filterConfig.getInitParameter("wings.servlet.recorder.script");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        try {
            if (servletRequest instanceof HttpServletRequest) {
                HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
                Map map = servletRequest.getParameterMap();
                if (map.containsKey(RECORDER_START)) {
                    if (list != null)
                        return;
                    logger.info(RECORDER_START);
                    list = new LinkedList();
                }
                else if (map.containsKey(RECORDER_STOP)) {
                    if (list == null)
                        return;
                    logger.info(RECORDER_STOP);
                    writeCode();
                    list = null;
                }
                else if (map.containsKey(RECORDER_SCRIPT)) {
                    logger.info("recorder_script " + map.get(RECORDER_SCRIPT));
                    scriptName = (String)map.get(RECORDER_SCRIPT);
                }
                else if (list != null) {
                    String resource = httpServletRequest.getPathInfo();
                    System.out.println("PATH_INFO: " + resource);

                    Request record;
                    if ("GET".equalsIgnoreCase(httpServletRequest.getMethod()))
                        record = new GET(resource);
                    else
                        record = new POST(resource);

                    Enumeration parameterNames = httpServletRequest.getParameterNames();
                    while (parameterNames.hasMoreElements()) {
                        String name = (String)parameterNames.nextElement();
                        String[] values = httpServletRequest.getParameterValues(name);
                        addEvent(record, name, values);
                    }
                    Enumeration headerNames = httpServletRequest.getHeaderNames();
                    while (headerNames.hasMoreElements()) {
                        String name = (String)headerNames.nextElement();
                        addHeader(record, name, httpServletRequest.getHeader(name));
                    }
                    list.add(record);
                }
            }
        }
        finally {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void addHeader(Request record, String name, String value) {
        if (record instanceof GET) {
            GET get = (GET)record;
            get.addHeader(name, value);
        }
        else if (record instanceof POST) {
            POST post = (POST)record;
            post.addHeader(name, value);
        }
    }

    private void addEvent(Request record, String name, String[] values) {
        if (record instanceof GET) {
            GET get = (GET)record;
            get.addEvent(name, values);
        }
        else if (record instanceof POST) {
            POST post = (POST)record;
            post.addEvent(name, values);
        }
    }

    public void destroy() {
        writeCode();
    }

    private void writeCode() {
        if (list == null || list.size() == 0)
            return;
        try {
            file = new File(scriptName + ".java");
            PrintWriter out = new PrintWriter(new FileWriter(file));
            //PrintWriter out = new PrintWriter(System.out);
            out.println("import org.wings.recorder.*;");
            out.println();
            out.println("public class " + scriptName);
            out.println("    extends Script");
            out.println("{");
            out.println("    public void execute()");
            out.println("        throws Exception");
            out.println("    {");

            long millis = ((Request)list.get(0)).getMillis();

            int index = 0;
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                Request record = (Request)iterator.next();
                if (index > 0)
                    out.println();
                out.println("        delay(" + (record.getMillis() - millis) + ");");
                out.print("        " + record.getMethod() + " request" + index +
                          " = new " + record.getMethod() + "(\"" + record.getResource() + "\")");

                for (Iterator iterator2 = record.getHeaders().iterator(); iterator2.hasNext();) {
                    Request.Header header = (Request.Header)iterator2.next();
                    out.println();
                    out.print("            .addHeader(\"");
                    out.print(header.getName());
                    out.print("\", \"");
                    out.print(StringUtil.replace(header.getValue(), "\"", "\\\""));
                    out.print("\")");
                }
                for (Iterator iterator2 = record.getEvents().iterator(); iterator2.hasNext();) {
                    Request.Event event = (Request.Event)iterator2.next();
                    out.println();
                    out.print("            .addEvent(\"");
                    out.print(event.getName());
                    out.print("\", new String[] { \"");
                    for (int i = 0; i < event.getValues().length; i++) {
                        String value = event.getValues()[i];
                        if (i > 0)
                            out.print("\", \"");
                        out.print(StringUtil.replace(value, "\"", "\\\""));
                    }
                    out.print("\" })");
                }
                out.println(";");
                out.println("        send(request" + index + ");");
                millis = record.getMillis();
                index++;
            }

            out.println("    }");
            out.println("}");
            out.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
