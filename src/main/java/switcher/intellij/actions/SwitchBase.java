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
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


// TODO: add focus on switched windows add toggle in settings, save last visible in list (What's with folding tw in ui?)
//  [*]left [*]right [*]bottom tool windows
// TODO: Add toggle in settings: Whether to remember last opened tool windows; None - show all every time
// BUG: When pressing button for a while ui freezing. (especially floating windows)

// NOTE: tool window actions like attaching floating window to border
// https://alvinalexander.com/java/java-mouse-current-position-location-coordinates/
// trigger on click and then triger (Window.setLocation()

public abstract class SwitchBase extends AnAction {
    protected static final Logger LOG = Logger.getInstance("SidePanelSwitcher");
    protected ToolWindowAnchor anchor;

    // TODO: rename to action performed
    protected void switchWindow(AnActionEvent event) {
        Project project = (Project) event.getDataContext().getData("project");
        if (project == null)
            return;

        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

        List<ToolWindow> toolWindows = Arrays.stream(toolWindowManager.getToolWindowIds())
                .map(toolWindowManager::getToolWindow)
                .filter(this::CanBeSwitched)
                .collect(Collectors.toList());

        List<String> visibleToolWindowIds = toolWindows.stream()
                .filter(ToolWindow::isVisible)
                .map(toolWindow -> {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Trying to hide window: [" + toolWindow.getId() + "]; on the: [" + anchor.toString() + "] side");
                    }

                    toolWindow.hide(null);
                    return toolWindow.getId();
                })
                .collect(Collectors.toList());

        if (visibleToolWindowIds.size() > 0) {
            setLastShownToolWindows(visibleToolWindowIds);
        } else {
            List<String> lastShownToolWindowIds = getLastShownToolWindows();

            for (ToolWindow toolWindow : toolWindows) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Trying to show window: [" + toolWindow.getId() + "]; on the: [" + anchor.toString() + "] side");
                }

                if (lastShownToolWindowIds == null
                        || lastShownToolWindowIds.size() == 0
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
