/*
 * Created by IntelliJ IDEA.
 * User: hengels
 * Date: Jan 29, 2002
 * Time: 3:53:30 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package org.wings.script;

import org.wings.SComponent;

public class JavaScriptListener
    implements ScriptListener
{
    private String event;
    private String code;
    private String script;
    private SComponent[] components;

    public JavaScriptListener(String event, String code) {
        this.event = event;
        this.code = code;
    }

    public JavaScriptListener(String event, String code, String script) {
        this.event = event;
        this.code = code;
        this.script = script;
    }

    // "document.all." + component[i].getUnifiedId()
    public JavaScriptListener(String event, String code, SComponent[] components) {
        this.event = event;
        this.code = code;
        this.components = components;
    }

    public JavaScriptListener(String event, String code, String script, SComponent[] components) {
        this.event = event;
        this.code = code;
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
}
