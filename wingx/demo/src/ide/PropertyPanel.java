package ide;

import java.awt.event.*;
import java.util.*;

import org.wingx.beans.beaneditor.*;

import org.wings.*;
import org.wings.event.*;


public class PropertyPanel
    extends SPanel
{
    BeanEditor editor;

    public PropertyPanel() {
        SForm form = new SForm();
        editor = new BeanEditor();
        form.add(editor);

        SButton button = new SButton("apply");
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
		    apply();
                }
            });
        form.add(button);
	add(form);
    }

    public void setComponent(SComponent component) {
	editor.setBean(component);
    }

    public SComponent getComponent() {
	return (SComponent)editor.getBean();
    }

    public void apply() {}
}
