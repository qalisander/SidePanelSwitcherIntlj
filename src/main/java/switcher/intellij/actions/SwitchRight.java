package switcher.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindowAnchor;

public class SwitchRight extends SwitchBase {
    @Override
    public void actionPerformed(AnActionEvent event) {
        switchWindow(ToolWindowAnchor.RIGHT, event);
    }
}
