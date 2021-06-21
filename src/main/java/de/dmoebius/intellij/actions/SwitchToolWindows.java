package de.dmoebius.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ToolWindowType;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class SwitchToolWindows extends AnAction {
    private static final Logger LOG = Logger.getInstance("SidePanelSwitcher");
    private static final int MAX_RETRIES = 50;

    public void update(AnActionEvent event) {
        Project project = (Project) event.getDataContext().getData("project");
        boolean enableAction = project != null && this.getVisibleBottomWindows(project).size() > 0;
        event.getPresentation().setEnabled(enableAction);
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = (Project) event.getDataContext().getData("project");
        if (project != null) {
            int retry = 0;

            do {
                List<ToolWindow> windows = this.getVisibleBottomWindows(project);
                this.hideWindows(windows);
                if (windows.size() <= 0) {
                    break;
                }

                ++retry;
            } while (retry < MAX_RETRIES);

        }
    }

    @NotNull
    private List<ToolWindow>getVisibleBottomWindows(@NotNull Project project) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        List<ToolWindow> bottomWindows = new ArrayList<>();
        for (String id : toolWindowManager.getToolWindowIds()) {
            ToolWindow window = toolWindowManager.getToolWindow(id);
            if (window.isVisible()) {
                ToolWindowType windowType = window.getType();
                ToolWindowAnchor windowAnchor = window.getAnchor();
                if ((windowType == ToolWindowType.DOCKED || windowType == ToolWindowType.SLIDING) && windowAnchor == ToolWindowAnchor.BOTTOM) {
                    bottomWindows.add(window);
                }
            }
        }

        return bottomWindows;
    }

    private void hideWindows(@NotNull List<ToolWindow> windows) {
        for (ToolWindow toolWindow : windows) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Trying to hide window >" + toolWindow.getTitle() + "<");
            }
            toolWindow.hide(null);
        }
    }
}
