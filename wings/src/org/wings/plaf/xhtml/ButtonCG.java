package org.wings.plaf.xhtml;

import java.io.IOException;

import javax.swing.Icon;

import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.externalizer.ExternalizeManager;


public class ButtonCG implements org.wings.plaf.ButtonCG, SConstants
{
    private final static String propertyPrefix = "Button" + ".";
    
    protected String getPropertyPrefix() {
        return propertyPrefix;
    }
    
    public void installCG(SComponent component) {
	component.setStyle(component.getSession().getCGManager().getStyle(propertyPrefix + "style"));
    }
    public void uninstallCG(SComponent c) {}
    
    public void write(Device d, SComponent c)
        throws IOException
    {
	SButton button = (SButton)c;
	SBorder border = button.getBorder();
	
        Utils.writeBorderPrefix(d, border);
	
	if (button.isAnchorButton() || !button.isEnabled())
	    writeAnchorButton(d, button);
	else
	    writeFormButton(d, button);
	
        Utils.writeBorderPostfix(d, border);
    }
    
    protected void writeAnchorButton(Device d, SButton button)
        throws IOException
    {
	Icon icon = button.getIcon();
	String text = button.getText();
	int horizontalTextPosition = button.getHorizontalTextPosition();
	int verticalTextPosition = button.getVerticalTextPosition();
	String iconAddress = button.getIconAddress();
	
	if (icon == null && iconAddress == null)
	    writeAnchorText(d, button);
	else if (text == null)
	    writeAnchorIcon(d, button);
	else {
	    // Hauptsache, es funktioniert !!!
	    if (verticalTextPosition == TOP && horizontalTextPosition == LEFT) {
		d.append("<table><tr><td valign=\"top\">");
		writeAnchorText(d, button);
		d.append("</td><td>");
		writeAnchorIcon(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == CENTER && horizontalTextPosition == LEFT) {
		d.append("<table><tr><td>");
		writeAnchorText(d, button);
		d.append("</td><td>");
		writeAnchorIcon(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == LEFT) {
		d.append("<table><tr><td valign=\"bottom\">");
		writeAnchorText(d, button);
		d.append("</td><td>");
		writeAnchorIcon(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == TOP && horizontalTextPosition == CENTER) {
		d.append("<table><tr><td>");
		writeAnchorText(d, button);
		d.append("</td></tr><tr><td>");
		writeAnchorIcon(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == CENTER && horizontalTextPosition == CENTER) {
		d.append("<table><tr><td>");
		writeAnchorText(d, button);
		d.append("</td><td>");
		writeAnchorIcon(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == CENTER) {
		d.append("<table><tr><td>");
		writeAnchorIcon(d, button);
		d.append("</td></tr><tr><td>");
		writeAnchorText(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == TOP && horizontalTextPosition == RIGHT) {
		writeAnchorIcon(d, button, "top");
		writeAnchorText(d, button);
	    } else if (verticalTextPosition == CENTER && horizontalTextPosition == RIGHT) {
		writeAnchorIcon(d, button, "middle");
		writeAnchorText(d, button);
	    } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == RIGHT) {
		writeAnchorIcon(d, button, "bottom");
		writeAnchorText(d, button);
	    } else {
		writeAnchorText(d, button);
		writeAnchorIcon(d, button, "middle");
	    }
	}
    }
    
    protected void writeAnchorText(Device d, SButton button)
        throws IOException
    {
	String text = button.getText();
	if (text != null && text.trim().length() > 0) {
	    writeAnchorPrefix(d, button);
	    writeAnchorBody(d, button);
	    writeAnchorPostfix(d, button);
	}
    }
    
    protected void writeAnchorIcon(Device d, SButton button)
        throws IOException
    {
	writeAnchorIcon(d, button, null);
    }
    
    protected void writeAnchorIcon(Device d, SButton button, String align)
        throws IOException
    {
	String text = button.getText();
	String iconAddress = button.getIconAddress();
	String disabledIconAddress = button.getDisabledIconAddress();
	Icon icon = button.getIcon();
	Icon disabledIcon = button.getDisabledIcon();
	String tooltip = button.getToolTipText();
	
	String iAdr = null;
	Icon ic = null;
	
	if (!button.isEnabled()){
	    if (disabledIconAddress != null)
		iAdr = disabledIconAddress;
	    else if (disabledIcon != null) 
		ic = disabledIcon;
	    
	    if (ic == null) 
		if (iconAddress != null)
		    iAdr = iconAddress;
		else if (icon != null) 
		    ic = icon;
	} else {
	    if (iconAddress != null)
		iAdr = iconAddress.toString();
	    else if (icon != null) 
		ic = icon;
	}
	
	if (ic != null) {
	    ExternalizeManager ext = button.getExternalizeManager();
	    if (ext != null) {
		try {
		    iAdr = ext.externalize(ic);
		} catch (java.io.IOException e) {
		    // dann eben nicht !!
		    e.printStackTrace();
		}
	    }
	}
	
	if (iAdr != null) {
	    writeAnchorPrefix(d, button);
	    d.append("<img src=\"").append(iAdr).append("\"");
	    if (align != null)
		d.append(" align=").append(align);
	    if (ic != null) {
		d.append(" width=").append(ic.getIconWidth());
		d.append(" height=").append(ic.getIconHeight());
	    }
	    d.append(" border=0");
	    
	    if (tooltip != null) {
		d.append(" alt=\"").append(tooltip).append("\"");
	    } else if (text != null) {
		d.append(" alt=\"").append(text).append("\"");
	    }
	    
	    d.append(">");
	    
	    writeAnchorPostfix(d, button);
	}
    }
    
    protected String generateAnchorAddress(SButton button) {
	SGetAddress addr = button.getServerAddress();
	addr.add(button.getNamePrefix() + "=" + button.getUnifiedIdString() + SConstants.UID_DIVIDER);
	return addr.toString();
    }
    
    protected void writeAnchorPrefix(Device d, SButton button)
        throws IOException
    {
	String tooltip = button.getToolTipText();
	
	if (button.isEnabled()) {
	    d.append("<a href=\"").append(generateAnchorAddress(button)).append("\"");
	    
	    if (button.getRealTarget() != null)
		d.append(" target=\"").append(button.getRealTarget()).append("\"");
	    
	    if (tooltip != null)
		d.append(" title=\"").append(tooltip).append("\"");
	    
	    d.append(">");
	}
    }
    
    protected void writeAnchorBody(Device d, SButton button)
        throws IOException
    {
	String text = button.getText();
	boolean noBreak = button.isNoBreak();
	
	if (noBreak)
	    d.append("<nobr>");
	d.append((text != null) ? text : "");
	if (noBreak)
	    d.append("</nobr>");
    }
    
    protected void writeAnchorPostfix(Device d, SButton button)
        throws IOException
    {
	if (button.isEnabled()) {
	    d.append("</a>\n");
	}
    }
    
    
    
    protected void writeFormButton(Device d, SButton button)
        throws IOException
    {
	Icon icon = button.getIcon();
	String text = button.getText();
	int horizontalTextPosition = button.getHorizontalTextPosition();
	int verticalTextPosition = button.getVerticalTextPosition();
	String iconAddress = button.getIconAddress();
	
	if (icon == null && iconAddress == null)
	    writeFormText(d, button);
	else if (text == null)
	    writeFormIcon(d, button);
	else {
	    // Hauptsache, es funktioniert !!!
	    if (verticalTextPosition == TOP && horizontalTextPosition == LEFT) {
		d.append("<table><tr><TD valign=top>");
		writeFormText(d, button);
		d.append("</td><td>");
		writeFormIcon(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == CENTER && horizontalTextPosition == LEFT) {
		d.append("<table><tr><td>");
		writeFormText(d, button);
		d.append("</td><td>");
		writeFormIcon(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == LEFT) {
		d.append("<table><tr><TD valign=bottom>");
		writeFormText(d, button);
		d.append("</td><td>");
		writeFormIcon(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == TOP && horizontalTextPosition == CENTER) {
		d.append("<table><tr><td>");
		writeFormText(d, button);
		d.append("</td></tr><tr><td>");
		writeFormIcon(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == CENTER && horizontalTextPosition == CENTER) {
		d.append("<table><tr><td>");
		writeFormText(d, button);
		d.append("</td><td>");
		writeFormIcon(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == CENTER) {
		d.append("<table><tr><td>");
		writeFormIcon(d, button);
		d.append("</td></tr><tr><td>");
		writeFormText(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == TOP && horizontalTextPosition == RIGHT) {
		d.append("<table><tr><TD VALIGN=\"TOP\">");
		writeFormIcon(d, button);
		d.append("</td><TD ALIGN=\"RIGHT\">");
		writeFormText(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == CENTER && horizontalTextPosition == RIGHT) {
		d.append("<table><tr><td>");
		writeFormIcon(d, button);
		d.append("</td><TD ALIGN=\"RIGHT\">");
		writeFormText(d, button);
		d.append("</td></tr></table>\n");
	    } else if (verticalTextPosition == BOTTOM && horizontalTextPosition == RIGHT) {
		d.append("<table><tr><TD VALIGN=\"BOTTOM\">");
		writeFormIcon(d, button);
		d.append("</td></tr><tr><TD ALIGN=\"RIGHT\">");
		writeFormText(d, button);
		d.append("</td></tr></table>\n");
	    } else {
		d.append("<table><tr><td>");
		writeFormIcon(d, button);
		d.append("</td></tr><tr><td>");
		writeFormText(d, button);
		d.append("</td></tr></table>\n");
	    }
	}
    }
    
    protected void writeFormText(Device d, SButton button)
        throws IOException
    {
	String text = button.getText();
	if (text != null && text.trim().length() > 0) {
	    writeFormPrefix(d, button);
	    writeFormBody(d, button);
	    writeFormPostfix(d, button);
	}
    }
    
    protected void writeFormIcon(Device d, SButton button)
        throws IOException
    {
	String text = button.getText();
	String iconAddress = button.getIconAddress();
	String disabledIconAddress = button.getDisabledIconAddress();
	Icon icon = button.getIcon();
	Icon disabledIcon = button.getDisabledIcon();
	String tooltip = button.getToolTipText();
	
	String iAdr = null;
	Icon ic = null;
	
	if (!button.isEnabled()){
	    if (disabledIconAddress != null)
		iAdr = disabledIconAddress;
	    else if (disabledIcon != null) 
		ic = disabledIcon;
	    
	    if (ic == null) 
		if (iconAddress != null)
		    iAdr = iconAddress;
		else if (icon != null) 
		    ic = icon;
	} else {
	    if (iconAddress != null)
		iAdr = iconAddress;
	    else if (icon != null) 
		ic = icon;
	}
	
	if (ic != null) {
	    ExternalizeManager ext = button.getExternalizeManager();
	    if (ext != null) {
		try {
		    iAdr = ext.externalize(ic);
		} catch (java.io.IOException e) {
		    // dann eben nicht !!
		    e.printStackTrace();
		}
	    }
	}
	
	if (iAdr != null) {
	    d.append("<input type=\"image\"");
	    d.append(" src=\"").append(iAdr).append("\"");
	    if (ic != null) {
		d.append(" width=\"").append(ic.getIconWidth()).append("\"");
		d.append(" height=\"").append(ic.getIconHeight()).append("\"");
	    }
	    d.append(" border=\"0\"");
	    
	    d.append(" name=\"").append(button.getNamePrefix()).append("\"");
	    d.append(" value=\"").append(text).append("\"");
	    
	    if (tooltip != null) {
		d.append(" alt=\"").append(tooltip).append("\"");
		d.append(" title=\"").append(tooltip).append("\"");
	    } else if (text != null) {
		d.append(" alt=\"").append(text).append("\"");
		d.append(" title=\"").append(text).append("\"");
	    }
	    d.append(" />");
	}
    }
    
    protected void writeFormPrefix(Device d, SButton button)
        throws IOException
    {
	d.append("<input type=\"submit\"");
    }
    
    protected void writeFormBody(Device d, SButton button)
        throws IOException
    {
	String text = button.getText();
	
	if (button.isEnabled())
	    d.append(" name=\"").
		append(button.getNamePrefix()).append("\"");
	if (text != null)
	    d.append(" value=\"").
		append(text).
		append("\"");
    }
    
    protected void writeFormPostfix(Device d, SButton button)
        throws IOException
    {
	d.append(" />\n");
    }
}
