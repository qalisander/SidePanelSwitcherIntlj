package switcher.intellij.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindowAnchor;

public class SwitchBottom extends SwitchBase {
    public SwitchBottom() {
        anchor = ToolWindowAnchor.BOTTOM;
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        switchWindow(event);
    }
}
