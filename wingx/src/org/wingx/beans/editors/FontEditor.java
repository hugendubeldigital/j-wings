// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FontEditor.java

package org.wingx.beans.editors;

import java.awt.*;
import java.beans.*;
import java.io.PrintStream;

public class FontEditor extends Panel
    implements PropertyEditor
{

    public FontEditor()
    {
        sampleText = "Abcde...";
        support = new PropertyChangeSupport(this);
        setLayout(null);
        toolkit = Toolkit.getDefaultToolkit();
        fonts = toolkit.getFontList();
        familyChoser = new Choice();
        for(int i = 0; i < fonts.length; i++)
            familyChoser.addItem(fonts[i]);

        add(familyChoser);
        familyChoser.reshape(20, 5, 100, 30);
        styleChoser = new Choice();
        for(int j = 0; j < styleNames.length; j++)
            styleChoser.addItem(styleNames[j]);

        add(styleChoser);
        styleChoser.reshape(145, 5, 70, 30);
        sizeChoser = new Choice();
        for(int k = 0; k < pointSizes.length; k++)
            sizeChoser.addItem("" + pointSizes[k]);

        add(sizeChoser);
        sizeChoser.reshape(220, 5, 70, 30);
        resize(300, 40);
    }

    public Dimension preferredSize()
    {
        return new Dimension(300, 40);
    }

    public void setValue(Object obj)
    {
        font = (Font)obj;
        changeFont(font);
        for(int i = 0; i < fonts.length; i++)
        {
            if(!fonts[i].equals(font.getFamily()))
                continue;
            familyChoser.select(i);
            break;
        }

        for(int j = 0; j < styleNames.length; j++)
        {
            if(font.getStyle() != styles[j])
                continue;
            styleChoser.select(j);
            break;
        }

        for(int k = 0; k < pointSizes.length; k++)
        {
            if(font.getSize() > pointSizes[k])
                continue;
            sizeChoser.select(k);
            break;
        }

    }

    private void changeFont(Font font1)
    {
        font = font1;
        if(sample != null)
            remove(sample);
        sample = new Label(sampleText);
        sample.setFont(font);
        add(sample);
        Container container = getParent();
        if(container != null)
        {
            container.invalidate();
            container.layout();
        }
        invalidate();
        layout();
        repaint();
        support.firePropertyChange("", null, null);
    }

    public Object getValue()
    {
        return font;
    }

    public String getJavaInitializationString()
    {
        return "new java.awt.Font(\"" + font.getFamily() + "\", " + font.getStyle() + ", " + font.getSize() + ")";
    }

    public boolean action(Event event, Object obj)
    {
        String s = familyChoser.getSelectedItem();
        int i = styles[styleChoser.getSelectedIndex()];
        int j = pointSizes[sizeChoser.getSelectedIndex()];
        try
        {
            Font font1 = new Font(s, i, j);
            changeFont(font1);
        }
        catch(Exception exception)
        {
            System.err.println("Couldn't create font " + s + "-" + styleNames[i] + "-" + j);
        }
        return false;
    }

    public boolean isPaintable()
    {
        return true;
    }

    public void paintValue(Graphics g, Rectangle rectangle)
    {
        Font font1 = g.getFont();
        g.setFont(font);
        FontMetrics fontmetrics = g.getFontMetrics();
        int i = (rectangle.height - fontmetrics.getAscent()) / 2;
        g.drawString(sampleText, 0, rectangle.height - i);
        g.setFont(font1);
    }

    public String getAsText()
    {
        return null;
    }

    public void setAsText(String s)
        throws IllegalArgumentException
    {
        throw new IllegalArgumentException(s);
    }

    public String[] getTags()
    {
        return null;
    }

    public Component getCustomEditor()
    {
        return this;
    }

    public boolean supportsCustomEditor()
    {
        return true;
    }

    public void addPropertyChangeListener(PropertyChangeListener propertychangelistener)
    {
        support.addPropertyChangeListener(propertychangelistener);
    }

    public void removePropertyChangeListener(PropertyChangeListener propertychangelistener)
    {
        support.removePropertyChangeListener(propertychangelistener);
    }

    private Font font;
    private Toolkit toolkit;
    private String sampleText;
    private Label sample;
    private Choice familyChoser;
    private Choice styleChoser;
    private Choice sizeChoser;
    private String fonts[];
    private String styleNames[] = {
        "plain", "bold", "italic"
    };
    private int styles[] = {
        0, 1, 2
    };
    private int pointSizes[] = {
        3, 5, 8, 10, 12, 14, 18, 24, 36, 48
    };
    private PropertyChangeSupport support;
}
