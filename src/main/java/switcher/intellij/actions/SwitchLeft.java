package switcher.intellij.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindowAnchor;

public class SwitchLeft extends SwitchBase {
    public SwitchLeft() {
        anchor = ToolWindowAnchor.LEFT;
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        switchWindow(event);
    }
}
