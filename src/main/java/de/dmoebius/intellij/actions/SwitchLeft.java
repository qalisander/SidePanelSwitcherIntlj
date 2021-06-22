package de.dmoebius.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindowAnchor;

public class SwitchLeft extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        ToolWindowSwitcher.switchWindow(ToolWindowAnchor.LEFT, event);
    }
}
