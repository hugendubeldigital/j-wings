package wingset;

import org.wings.SComponent;
import org.wings.SDesktopPane;
import org.wings.SDimension;
import org.wings.SInternalFrame;
import org.wings.SLabel;
import org.wings.STextField;

public class DesktopPaneExample extends WingSetPane {

    private static final int FRAME_COUNT = 5;

    protected SComponent createExample() {
        SDesktopPane desktopPane = new SDesktopPane();
        for (int i = 0; i < FRAME_COUNT; i++) {
            SInternalFrame iFrame = new SInternalFrame();
            iFrame.setTitle("Frame " + (i+1));
            
            desktopPane.add(iFrame);
            fillFrame(iFrame);
        }
        return desktopPane;
    }

    private void fillFrame(SInternalFrame frame) {
        frame.getContentPane().add(new STextField());
        frame.getContentPane().add(new SLabel("This is a label"));
    }

}
