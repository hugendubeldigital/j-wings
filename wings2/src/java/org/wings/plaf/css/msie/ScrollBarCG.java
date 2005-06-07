/**
 * 
 */
package org.wings.plaf.css.msie;

import java.io.IOException;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SFrame;
import org.wings.SScrollBar;
import org.wings.STabbedPane;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.session.SessionManager;

/**
 * @author ole
 *
 */
public class ScrollBarCG extends org.wings.plaf.css.ScrollBarCG implements SParentFrameListener {
    private static final String FORMS_JS = (String) SessionManager
    .getSession().getCGManager().getObject("JScripts.form",
            String.class);


    public void installCG(SComponent component) {
        super.installCG(component);
        component.addParentFrameListener(this);
    }

    public void parentFrameAdded(SParentFrameEvent e) {
        SFrame parentFrame = e.getParentFrame();
        ClasspathResource res = new ClasspathResource(FORMS_JS, "text/javascript");
        String jScriptUrl = SessionManager.getSession().getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        parentFrame.addHeader(new Script("text/javascript", new DefaultURLResource(jScriptUrl)));
    }

    public void parentFrameRemoved(SParentFrameEvent e) {
    }

    /* (non-Javadoc)
     * @see org.wings.plaf.css.ScrollBarCG#writeButtonStart(org.wings.io.Device, org.wings.SScrollBar, java.lang.String)
     */
    protected void writeButtonStart(Device device, SScrollBar scrollBar, String event) throws IOException {
        device.print("<button onclick=\"addHiddenField(this.form,'");
        device.print(scrollBar.getParentFrame().getEventEpoch());
        device.print(SConstants.UID_DIVIDER);
        device.print(SConstants.IEFIX_BUTTONACTION);
        device.print("','");
        device.print(scrollBar.getName());
        device.print(SConstants.UID_DIVIDER);
        device.print(event);
        device.print("')\"");
    }


}
