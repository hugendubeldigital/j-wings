// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StringEditor.java

package org.wingx.beans.editors;

import org.wingx.beans.SPropertyEditorSupport;

public class StringEditor extends SPropertyEditorSupport
{

    public StringEditor()
    {
    }

    public String getJavaInitializationString()
    {
        return "\"" + getValue() + "\"";
    }

    public void setAsText(String s)
    {
        setValue(s);
    }
}
