package switcher.intellij.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindowAnchor;

public class SwitchRight extends SwitchBase {
    public SwitchRight() {
        anchor = ToolWindowAnchor.RIGHT;
    }

    // TODO: ommit
    @Override
    public void actionPerformed(AnActionEvent event) {
        switchWindow(event);
    }
}
