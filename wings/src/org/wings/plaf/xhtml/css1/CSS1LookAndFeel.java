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

package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.style.*;

public final class CSS1LookAndFeel
    extends LookAndFeel
{
    private StyleSheet styleSheet = null;

    public String getName() { return ""; }
    public String getDescription() { return " Look and Feel"; }

    public CGDefaults getDefaults() {
        System.err.println(getClass().getName() + " in action");
        CGDefaults table = new CGDefaults();
        initClassDefaults(table);
        initStyleDefaults(table);
        initComponentDefaults(table);
        return table;
    }

    /**
     * Initialize the cgClassID to ComponentCG mapping.
     * The JComponent classes define their own cgClassID constants
     * (see AbstractComponent.getCGClassID).  This table must
     * map those constants to a ComponentCG class of the
     * appropriate type.
     *
     * @see #getDefaults
     */
    protected void initClassDefaults(CGDefaults table)
    {
        String css1PackageName = "org.wings.plaf.xhtml.css1.";
        Object[] cgDefaults = {
                "ComponentCG", css1PackageName + "ComponentCG",
                    "FrameCG", css1PackageName + "FrameCG",
                   "AnchorCG", css1PackageName + "AnchorCG",
                   "ButtonCG", css1PackageName + "ButtonCG",
              "ResetButtonCG", css1PackageName + "ResetButtonCG",
                     "HRefCG", css1PackageName + "HRefCG",
                 "CheckBoxCG", css1PackageName + "CheckBoxCG",
              "RadioButtonCG", css1PackageName + "RadioButtonCG",
             "ToggleButtonCG", css1PackageName + "ToggleButtonCG",
                "ScrollBarCG", css1PackageName + "ScrollBarCG",
               "ScrollPaneCG", css1PackageName + "ScrollPaneCG",
                   "SliderCG", css1PackageName + "SliderCG",
                "SeparatorCG", css1PackageName + "SeparatorCG",
               "TabbedPaneCG", css1PackageName + "TabbedPaneCG",
                 "TextAreaCG", css1PackageName + "TextAreaCG",
                "TextFieldCG", css1PackageName + "TextFieldCG",
            "PasswordFieldCG", css1PackageName + "PasswordFieldCG",
                     "TreeCG", css1PackageName + "TreeCG",
                    "LabelCG", css1PackageName + "LabelCG",
                     "ListCG", css1PackageName + "ListCG",
                 "BaseEnumCG", css1PackageName + "BaseEnumCG",
                     "EnumCG", css1PackageName + "EnumCG",
                   "ChoiceCG", css1PackageName + "ChoiceCG",
                  "ToolBarCG", css1PackageName + "ToolBarCG",
                 "ComboBoxCG", css1PackageName + "ComboBoxCG",
                "BaseTableCG", css1PackageName + "BaseTableCG",
                    "TableCG", css1PackageName + "TableCG",
                "SortTableCG", css1PackageName + "SortTableCG",
            "InternalFrameCG", css1PackageName + "InternalFrameCG",
              "DesktopPaneCG", css1PackageName + "DesktopPaneCG",
               "OptionPaneCG", css1PackageName + "OptionPaneCG",
              "FileChooserCG", css1PackageName + "FileChooserCG",
                "ContainerCG", css1PackageName + "ContainerCG",
                    "PanelCG", css1PackageName + "PanelCG",
                 "DivisionCG", css1PackageName + "DivisionCG",
                     "FormCG", css1PackageName + "FormCG",
                   "DialogCG", css1PackageName + "DialogCG",
           "HorizontalRuleCG", css1PackageName + "HorizontalRuleCG",
                "LineBreakCG", css1PackageName + "LineBreakCG",
                "ParagraphCG", css1PackageName + "ParagraphCG",
        };

        table.putDefaults(cgDefaults);

        Object[] layoutCgDefaults = {
            // remove this later
            "DefaultLayoutCG", "org.wings.plaf.DefaultLayoutCG",
             "BorderLayoutCG", css1PackageName + "BorderLayoutCG",
               "CardLayoutCG", css1PackageName + "CardLayoutCG",
               "FlowLayoutCG", css1PackageName + "FlowLayoutCG",
               "GridLayoutCG", css1PackageName + "GridLayoutCG",
           "TemplateLayoutCG", css1PackageName + "TemplateLayoutCG",
        };

	table.putDefaults(layoutCgDefaults);

	Object[] borderCgDefaults = {
             "EtchedBorderCG", css1PackageName + "EtchedBorderCG",
              "BevelBorderCG", css1PackageName + "BevelBorderCG",
               "LineBorderCG", css1PackageName + "LineBorderCG",
              "EmptyBorderCG", css1PackageName + "EmptyBorderCG",
        };

        table.putDefaults(borderCgDefaults);
    }

    protected void initStyleDefaults(CGDefaults table)
    {
	Object[] styleDefaults = {
                    "Frame.style", new Style("frame"),
                   "Anchor.style", new Style("anchor"),
                   "Button.style", new Style("button"),
                     "HRef.style", new Style("href"),
                 "CheckBox.style", new Style("checkbox"),
              "RadioButton.style", new Style("radiobutton"),
             "ToggleButton.style", new Style("togglebutton"),
                "ScrollBar.style", new Style("scrollbar"),
               "ScrollPane.style", new Style("scrollpane"),
                   "Slider.style", new Style("slider"),
                "Separator.style", new Style("separator"),
               "TabbedPane.style", new Style("tabbedpane"),
                 "TextArea.style", new Style("textarea"),
                "TextField.style", new Style("textfield"),
            "PasswordField.style", new Style("passwordfield"),
                     "Tree.style", new Style("tree"),
        "TreeNodeSelection.style", new Style("treenodeselection"),
     "TreeNodeNonSelection.style", new Style("treenodenonselection"),
                    "Label.style", new Style("label"),
                   "Choice.style", new Style("choice"),
                     "Enum.style", new Style("enum"),
                 "BaseEnum.style", new Style("baseenum"),
                     "List.style", new Style("list"),
            "ListSelection.style", new Style("listselection"),
         "ListNonSelection.style", new Style("listnonselection"),
                  "ToolBar.style", new Style("toolbar"),
                 "ComboBox.style", new Style("combobox"),
                "BaseTable.style", new Style("basetable"),
          "BaseTableHeader.style", new Style("basetableheader"),
   "BaseTableCellSelection.style", new Style("basetablecell"),
                    "Table.style", new Style("table"),
              "TableHeader.style", new Style("tableheader"),
       "TableCellSelection.style", new Style("tablecellselection"),
    "TableCellNonSelection.style", new Style("tablecellnonselection"),
                "SortTable.style", new Style("sorttable"),
          "SortTableHeader.style", new Style("sorttableheader"),
            "InternalFrame.style", new Style("internalframe"),
              "DesktopPane.style", new Style("desktoppane"),
               "OptionPane.style", new Style("optionpane"),
              "FileChooser.style", new Style("filechooser"),
                "Container.style", new Style("container"),
                    "Panel.style", new Style("panel"),
                 "Division.style", new Style("division"),
                     "Form.style", new Style("form"),
                   "Dialog.style", new Style("dialog"),
        };
        table.putDefaults(styleDefaults);

        Object[] sheetDefaults = {
            "Frame.stylesheet", new ResourceStyleSheet(getClass(), "default.css"),
        };
        table.putDefaults(sheetDefaults);
    }

    protected void initComponentDefaults(CGDefaults table) {
        Object[] defaults = {
            // *** Tree
            "Tree.textForeground", table.get("textText"),
            "Tree.textBackground", table.get("text"),
            "Tree.selectionForeground", table.get("textHighlightText"),
            "Tree.selectionBackground", table.get("textHighlight"),
            "Tree.childIndent", new Integer(12),
            "Tree.openIcon", LookAndFeel.makeIcon(getClass(), "/org/wings/icons/TreeOpen.gif"),
            "Tree.closedIcon", LookAndFeel.makeIcon(getClass(), "/org/wings/icons/TreeClosed.gif"),
            "Tree.leafIcon", LookAndFeel.makeIcon(getClass(), "/org/wings/icons/TreeLeaf.gif"),

            // *** Table
            "Table.selectionForeground", table.get("textHighlightText"),
            "Table.selectionBackground", table.get("textHighlight"),
            "Table.editIcon", LookAndFeel.makeIcon(getClass(), "/org/wings/icons/Pencil.gif"),
        };
        table.putDefaults(defaults);
    }

    /**
     * Return an appropriate Device for code generation.
     * Some lafs can deal with a stream, others rely on a buffered
     * Device, because they produce code that must appear in the header.
     *
     * @return a Device that is suitable for this laf
     */
    public Device createDevice(javax.servlet.ServletOutputStream stream) {
        return null;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
