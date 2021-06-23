package switcher.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindowAnchor;

public class SwitchBottom extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        ToolWindowSwitcher.switchWindow(ToolWindowAnchor.BOTTOM, event);
    }
}
