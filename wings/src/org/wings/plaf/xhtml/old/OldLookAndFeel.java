package org.wings.plaf.xhtml.old;

import java.io.IOException;

import org.wings.io.*;
import org.wings.plaf.*;

public final class OldLookAndFeel
    extends LookAndFeel
{
    public String getName() { return "XHTML"; }
    public String getDescription() { return "XHTML Look and Feel"; }

    public CGDefaults getDefaults() {
	CGDefaults table = new CGDefaults();
	initClassDefaults(table);
	initComponentDefaults(table);
	return table;
    }

    /**
     * Initialize the cgClassID to ComponentCG mapping.
     * The JComponent classes define their own cgClassID constants
     * (see AbstractComponent.getCGClassID).  This table must
     * map those constants to a BasicComponentCG class of the
     * appropriate type.
     *
     * @see #getDefaults
     */
    protected void initClassDefaults(CGDefaults table)
    {
	String oldPackageName = "org.wings.plaf.xhtml.old.";
	Object[] cgDefaults = {
		"ComponentCG", oldPackageName + "ComponentCG",
		    "FrameCG", oldPackageName + "FrameCG",
		   "AnchorCG", oldPackageName + "AnchorCG",
		   "ButtonCG", oldPackageName + "ButtonCG",
	      "ResetButtonCG", oldPackageName + "ResetButtonCG",
		     "HRefCG", oldPackageName + "HRefCG",
		 "CheckBoxCG", oldPackageName + "CheckBoxCG",
	      "RadioButtonCG", oldPackageName + "RadioButtonCG",
	     "ToggleButtonCG", oldPackageName + "ToggleButtonCG",
		"ScrollBarCG", oldPackageName + "ScrollBarCG",
	       "ScrollPaneCG", oldPackageName + "ScrollPaneCG",
		   "SliderCG", oldPackageName + "SliderCG",
		"SeparatorCG", oldPackageName + "SeparatorCG",
	       "TabbedPaneCG", oldPackageName + "TabbedPaneCG",
		 "TextAreaCG", oldPackageName + "TextAreaCG",
		"TextFieldCG", oldPackageName + "TextFieldCG",
	    "PasswordFieldCG", oldPackageName + "PasswordFieldCG",
		     "TreeCG", oldPackageName + "TreeCG",
		    "LabelCG", oldPackageName + "LabelCG",
		 "BaseEnumCG", oldPackageName + "BaseEnumCG",
		     "EnumCG", oldPackageName + "EnumCG",
		     "ListCG", oldPackageName + "ListCG",
		   "ChoiceCG", oldPackageName + "ChoiceCG",
		  "ToolBarCG", oldPackageName + "ToolBarCG",
		 "ComboBoxCG", oldPackageName + "ComboBoxCG",
		"BaseTableCG", oldPackageName + "BaseTableCG",
		    "TableCG", oldPackageName + "TableCG",
		"SortTableCG", oldPackageName + "SortTableCG",
	    "InternalFrameCG", oldPackageName + "InternalFrameCG",
	      "DesktopPaneCG", oldPackageName + "DesktopPaneCG",
	       "OptionPaneCG", oldPackageName + "OptionPaneCG",
	      "FileChooserCG", oldPackageName + "FileChooserCG",
		"ContainerCG", oldPackageName + "ContainerCG",
	            "PanelCG", oldPackageName + "PanelCG",
		 "DivisionCG", oldPackageName + "DivisionCG",
		     "FormCG", oldPackageName + "FormCG",
		   "DialogCG", oldPackageName + "DialogCG",
	   "HorizontalRuleCG", oldPackageName + "HorizontalRuleCG",
	        "LineBreakCG", oldPackageName + "LineBreakCG",
                "ParagraphCG", oldPackageName + "ParagraphCG",
	};
	
	table.putDefaults(cgDefaults);

	Object[] layoutCgDefaults = {
	     "BorderLayoutCG", oldPackageName + "BorderLayoutCG",
	       "CardLayoutCG", oldPackageName + "CardLayoutCG",
	       "FlowLayoutCG", oldPackageName + "FlowLayoutCG",
	       "GridLayoutCG", oldPackageName + "GridLayoutCG",
        };
	
	table.putDefaults(layoutCgDefaults);
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
