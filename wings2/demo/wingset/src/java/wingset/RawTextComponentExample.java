package wingset;

import org.wings.SComponent;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SRawTextComponent;
import org.wings.STemplateLayout;

public class RawTextComponentExample extends WingSetPane {

    protected SComponent createExample() {
        final SPanel examplePanel = new SPanel();
        try {
            java.net.URL templateURL =
                    getSession().getServletContext().getResource("/templates/RawTextComponentExample.thtml");
            if (templateURL == null) {
                examplePanel.add(new SLabel("Sorry, can't find RawTextComponentExample.thtml. Are you using a JAR-File?"));
                return examplePanel;
            }
            // you can of course directly give files here.
            STemplateLayout layout = new STemplateLayout(templateURL);
            examplePanel.setLayout(layout);
        } catch (java.io.IOException except) {
            except.printStackTrace();
        }
        
        examplePanel.add(new SRawTextComponent("This is a Raw Text. Other than an SLabel, it does break and has no div's wrapping it."), "contentText");
        examplePanel.add(new SRawTextComponent("javascript:alert('This is a text put inside the href of the link');"), "linkText");
        
        return examplePanel;
        
    }

}
