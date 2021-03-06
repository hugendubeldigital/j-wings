/*
 * SNumberFormatter.java
 *
 * Created on 9. September 2003, 12:15
 */

/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.text;

import org.wings.SFormattedTextField;
import org.wings.SFrame;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Script;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.session.SessionManager;
import org.wings.session.Session;
import org.wings.script.JavaScriptListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;


/**
 * SNumberFormater supports NumberFormat.parseIntegerOnly,
 * NumberFormat.maxIntegerDigits, NumberFormat.minIntegerDigits, NumberFormat.maxFractionDigits,
 * NumberFormat.minIntegerDigits.
 * Additional it's possible to set a maximal or a minimal valid value.
 *
 * @author theresia & erik
 */
public class SNumberFormatter extends SAbstractFormatter implements SParentFrameListener {

    private NumberFormat numberFormat;
    private double maxVal = Double.MAX_VALUE;
    private double minVal = Double.MIN_VALUE;

    private boolean alert = false;

    private JavaScriptListener javaScriptListener;

    private static String functStr = "saveOld(this)";

    private static final JavaScriptListener SCRIPT_SAVEOLD = new JavaScriptListener("onfocus", functStr);

    private static final String SNUMBERFORMATTER_JS = "org/wings/text/SNumberFormatter.js";
    /*
     * Creates a new instance of SNumberFormatter.
     */
    public SNumberFormatter() {
        Session session = SessionManager.getSession();
        numberFormat = session != null ? NumberFormat.getNumberInstance(session.getLocale()) : NumberFormat.getNumberInstance();
    }

    /*
     * Creates a new instance of SNumberFormatter.
     * @param NumberFormat nf the NumberFormat for the SFormattedTextField
     */
    public SNumberFormatter(NumberFormat nf) {
        numberFormat = nf;
    }

    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    /*
     * sets the NumberFormat. Doesn't work after the Frame is installed, because
     * until now it's  not possible to add a JavaScriptFunction correctly after that.
     * @param NumberFormat nf the NumberFormat for the SFormattedTextField.
     */
    public void setNumberFormat(NumberFormat nf) {
        numberFormat = nf;
        updateFormatter();
    }

    public String valueToString(Object value) {
        return numberFormat.format(value);
    }

    /**
     * Specified by stringToValue in class AbstractFormatter
     *
     * @param text text - String to convert
     * @return Object representation of text
     */
    public Object stringToValue(String text) {
        Object obj = null;
        try {
            obj = numberFormat.parseObject(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /*
     * sets the maximal possible value.
     * @param double maxVal the maximal possible value for the SFormattedTextField.
     */
    public void setMaxVal(double maxVal) {
        this.maxVal = maxVal;
        updateFormatter();
    }

    public double getMaxVal() {
        return maxVal;
    }

    /*
    * sets the minimal possible value.
    * @param double minVal the minimal possible value for the SFormattedTextField.
    */
    public void setMinVal(double minVal) {
        this.minVal = minVal;
        updateFormatter();
    }

    public double getMinVal() {
        return minVal;
    }

    private char getDecimalSeparator() {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        if (numberFormat instanceof DecimalFormat) {
            dfs = ((DecimalFormat) numberFormat).getDecimalFormatSymbols();
        }
        return dfs.getDecimalSeparator();
    }

    private char getGroupingSeparator() {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        if (numberFormat instanceof DecimalFormat) {
            dfs = ((DecimalFormat) numberFormat).getDecimalFormatSymbols();
        }
        return dfs.getGroupingSeparator();
    }

    /*
     * Generates the JavaScript-function based on numberFormat, MinVal and MaxVal
     * @param String name Name of the function.
     * @param boolean b true if an ActionListener is added to the SFormattedTextField
     **/
    public JavaScriptListener generateJavaScript(SFormattedTextField field, boolean b) {

        if (field.getParentFrame() != null) {
            addExternalizedHeader(field.getParentFrame(), SNUMBERFORMATTER_JS, "text/javascript");
        }
        field.addParentFrameListener(this);

        field.removeScriptListener(SCRIPT_SAVEOLD);
        field.addScriptListener(SCRIPT_SAVEOLD);

        if (b) {
            String funct = "if (numberFormatter(" + numberFormat.getMinimumIntegerDigits() + "," + numberFormat.getMaximumIntegerDigits() + "," + numberFormat.getMinimumFractionDigits() + "," + numberFormat.getMaximumFractionDigits() + "," + minVal + "," + maxVal + "," + numberFormat.isGroupingUsed() + "," + numberFormat.isParseIntegerOnly() + ",'" + this.getDecimalSeparator() + "','" + this.getGroupingSeparator() + "',this)) submit();";
            javaScriptListener = new JavaScriptListener("onchange", funct);
        } else {
            javaScriptListener = new JavaScriptListener("onchange", "numberFormatter(" + numberFormat.getMinimumIntegerDigits() + "," + numberFormat.getMaximumIntegerDigits() + "," + numberFormat.getMinimumFractionDigits() + "," + numberFormat.getMaximumFractionDigits() + "," + minVal + "," + maxVal + "," + numberFormat.isGroupingUsed() + "," + numberFormat.isParseIntegerOnly() + ",'" + this.getDecimalSeparator() + "','" + this.getGroupingSeparator() + "',this)");
        }
        return javaScriptListener;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean isAlert() {
        return alert;
    }

    public void parentFrameAdded(SParentFrameEvent e) {
        addExternalizedHeader(e.getParentFrame(), SNUMBERFORMATTER_JS, "text/javascript");
    }

    private void addExternalizedHeader(SFrame parentFrame, String classPath, String mimeType) {
        ClasspathResource res = new ClasspathResource(classPath, mimeType);
        String jScriptUrl = SessionManager.getSession().getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        parentFrame.addHeader(new Script(mimeType, new DefaultURLResource(jScriptUrl)));
    }

    public void parentFrameRemoved(SParentFrameEvent e) {
        // TODO Auto-generated method stub
        
    }
}
