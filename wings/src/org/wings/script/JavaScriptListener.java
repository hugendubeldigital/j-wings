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
package org.wings.script;

import java.util.*;

import org.wings.SComponent;

public class JavaScriptListener
    implements ScriptListener
{
    private String event;
    private String code;
    private String script;
    private SComponent[] components;

    /**
     * @param event one of 'onclick', 'onmouseover', ..
     * @param code the code that is written as a value of the event attribute
     */
    public JavaScriptListener(String event, String code) {
        this.event = event;
        this.code = code;
    }

    /**
     * @param event one of 'onclick', 'onmouseover', ..
     * @param code the code that is written as a value of the event attribute
     * @param script larger code block (java script functions), that is written
     *        to a separate script file, that is linked in the header
     */
    public JavaScriptListener(String event, String code, String script) {
        this.event = event;
        this.code = code;
        this.script = script;
    }

    /**
     * The code is parsed and all occurrences of '{0..components.length}' are substituted
     * with that component's id. You can use this to address elements by id.
     * This mechanism is highly dependant on the code, the plaf generates for a component.
     * @param event one of 'onclick', 'onmouseover', ..
     * @param code the code that is written as a value of the event attribute
     * @param components the components whose ids are substituted into the code
     */
    public JavaScriptListener(String event, String code, SComponent[] components) {
        this.event = event;
        this.code = substituteIds(code, components);
        this.components = components;
    }

    /**
     * The code is parsed and all occurrences of '{0..components.length}' are substituted
     * with that component's id. You can use this to address elements by id.
     * This mechanism is highly dependant on the code, the plaf generates for a component.
     * @param event one of 'onclick', 'onmouseover', ..
     * @param code the code that is written as a value of the event attribute
     * @param script larger code block (java script functions), that is written
     *        to a separate script file, that is linked in the header
     * @param components the components whose ids are substituted into the code
     */
    public JavaScriptListener(String event, String code, String script, SComponent[] components) {
        this.event = event;
        this.code = substituteIds(code, components);
        this.script = script;
        this.components = components;
    }

    public void setEvent(String event) {
        this.event = event;
    }
    public String getEvent() { return event; }

    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() { return code; }

    public void setScript(String script) {
        this.script = script;
    }
    public String getScript() { return script; }

    public void setComponents(SComponent[] components) {
            this.components = components;
    }
    public SComponent[] getComponents() { return components; }

    private String substituteIds(String code, SComponent[] components) {
	StringBuffer buffer = new StringBuffer();

	StringTokenizer tokens = new StringTokenizer(code, "{}");
	while (tokens.hasMoreTokens()) {
	    buffer.append(tokens.nextToken());

	    int i = new Integer(tokens.nextToken()).intValue();
	    buffer.append(components[i].getComponentId());
	}
	return buffer.toString();
    }
}
