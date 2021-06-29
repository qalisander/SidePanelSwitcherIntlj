package switcher.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// TODO: Save last opened tool windows, and add toggle in settings
// TODO: hide tool windows when start typing
// TODO: add focus on switched windows add toggle in settings, save last visible in list (What's with folding tw in ui?)

// TODO: move and window to next group and move all to group

// NOTE: tool window actions like attaching floating window to border
// https://alvinalexander.com/java/java-mouse-current-position-location-coordinates/
// trigger on click and then triger (Window.setLocation()

public abstract class SwitchBase extends AnAction {
    protected static final Logger LOG = Logger.getInstance("SidePanelSwitcher");

    static public void switchWindow(ToolWindowAnchor anchor, AnActionEvent event) {
        Project project = (Project) event.getDataContext().getData("project");
        if (project == null)
            return;

        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

        List<ToolWindow> toolWindows = Arrays.stream(toolWindowManager.getToolWindowIds())
                .map(toolWindowManager::getToolWindow)
                .filter(Objects::nonNull)
                .filter(tw -> tw.getAnchor() == anchor)
                .collect(Collectors.toList());

        if (toolWindows.stream().anyMatch(ToolWindow::isVisible)) {
            for (ToolWindow toolWindow : toolWindows) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Trying to hide window: [" + toolWindow.getTitle() + "]");
                }
                toolWindow.hide(null);
            }
        }
        else {
            for (ToolWindow toolWindow : toolWindows) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Trying to show window: [" + toolWindow.getTitle() + "]");
                }
                toolWindow.show(null);
            }
        }
    }
}
