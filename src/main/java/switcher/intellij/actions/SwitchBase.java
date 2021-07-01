package switcher.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ToolWindowType;
import switcher.intellij.settings.AppSettingsState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    protected ToolWindowAnchor anchor;

    protected void switchWindow(AnActionEvent event) {
        Project project = (Project) event.getDataContext().getData("project");
        if (project == null)
            return;

        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

        List<ToolWindow> toolWindows = Arrays.stream(toolWindowManager.getToolWindowIds())
                .map(toolWindowManager::getToolWindow)
                .filter(this::CanBeSwitched)
                .collect(Collectors.toList());

        if (toolWindows.stream().anyMatch(ToolWindow::isVisible)) {
            ArrayList<String> lastShownToolWindowIds = new ArrayList<String>();
            for (ToolWindow toolWindow : toolWindows) { // TODO: iterate on stream of visible windows
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Trying to hide window: [" + toolWindow.getTitle() + "]");// TODO: prlly use get id
                }

                if (toolWindow.isVisible()){
                    lastShownToolWindowIds.add(toolWindow.getId());
                }

                toolWindow.hide(null);
            }
            setLastShownToolWindows(lastShownToolWindowIds);
        } else {
            for (ToolWindow toolWindow : toolWindows) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Trying to show window: [" + toolWindow.getTitle() + "]");
                }

                List<String> lastShownToolWindowIds = getLastShownToolWindows();

                if (lastShownToolWindowIds == null
                        || lastShownToolWindowIds.stream().anyMatch(str -> str.equals(toolWindow.getId()))) {
                    toolWindow.show(null);
                }
            }
        }
    }

    private boolean CanBeSwitched(ToolWindow toolWindow) {
        if (toolWindow == null || toolWindow.getAnchor() != anchor)
            return false;

        if (toolWindow.getType() == ToolWindowType.DOCKED && !toolWindow.isAutoHide())
            return true;

        AppSettingsState settings = AppSettingsState.getInstance();

        return (settings.switchDockUnpinned && toolWindow.getType() == ToolWindowType.DOCKED && toolWindow.isAutoHide())
                || (settings.switchUndocked && toolWindow.getType() == ToolWindowType.SLIDING)
                || (settings.switchFloat && toolWindow.getType() == ToolWindowType.FLOATING);
    }

    private List<String> getLastShownToolWindows() {
        return AppSettingsState.getInstance().lastShownToolWindows.get(anchor.toString());
    }

    private void setLastShownToolWindows(List<String> toolWindowIds) {
        AppSettingsState.getInstance().lastShownToolWindows.put(anchor.toString(), toolWindowIds);
    }
}
