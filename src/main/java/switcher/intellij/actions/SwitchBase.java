package switcher.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ToolWindowType;
import org.jetbrains.annotations.NotNull;
import switcher.intellij.settings.AppSettingsState;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// BUG: When pressing switch shortcut for a while ui freezing. (especially floating windows)
// NOTE: tool window actions like attaching floating window to border
// https://alvinalexander.com/java/java-mouse-current-position-location-coordinates/
// trigger on click and then trigger (Window.setLocation()

public abstract class SwitchBase extends AnAction implements DumbAware {
    private static final Logger LOG = Logger.getInstance("SidePanelSwitcher");
    private static final AppSettingsState settings = AppSettingsState.getInstance();
    protected ToolWindowAnchor anchor;

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = (Project) event.getDataContext().getData("project");
        if (project == null)
            return;

        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

        List<ToolWindow> availableToolWindows = Arrays.stream(toolWindowManager.getToolWindowIds())
                .map(toolWindowManager::getToolWindow)
                .filter(this::canBeSwitched)
                .collect(Collectors.toList());

        List<String> hiddenToolWindowIds = tryHide(availableToolWindows);

        if (hiddenToolWindowIds.size() > 0) {
            setLastShownToolWindows(hiddenToolWindowIds);
        } else {
            show(availableToolWindows);
        }
    }

    @NotNull
    private List<String> tryHide(List<ToolWindow> toolWindows) {
        return toolWindows.stream()
                .filter(ToolWindow::isVisible)
                .map(toolWindow -> {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Trying to hide window: [" + toolWindow.getId() + "]; on the: [" + anchor.toString() + "] side");
                    }

                    if (toolWindow.isActive()) {
                        setLastFocusedToolWindow(toolWindow.getId());
                    }

                    toolWindow.hide(null);
                    return toolWindow.getId();
                })
                .collect(Collectors.toList());
    }

    private void show(List<ToolWindow> toolWindows) {
        List<String> lastShownToolWindowIds = getLastShownToolWindows();
        String lastFocusedToolWindowId = removeLastFocusedToolWindow();

        for (ToolWindow toolWindow : toolWindows) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Trying to show window: [" + toolWindow.getId() + "]; on the: [" + anchor.toString() + "] side");
            }

            if (!settings.rememberLastOpened
                    || lastShownToolWindowIds == null
                    || lastShownToolWindowIds.size() == 0
                    || lastShownToolWindowIds.stream().anyMatch(str -> str.equals(toolWindow.getId()))) {
                toolWindow.show(null);
            }

            if (settings.focusOnSwitched
                    && toolWindow.getId().equals(lastFocusedToolWindowId)){
                toolWindow.activate(null);
            }
        }
    }

    private boolean canBeSwitched(ToolWindow toolWindow) {
        if (toolWindow == null || toolWindow.getAnchor() != anchor)
            return false;

        if (toolWindow.getType() == ToolWindowType.DOCKED && !toolWindow.isAutoHide())
            return true;

        return (settings.switchDockUnpinned && toolWindow.getType() == ToolWindowType.DOCKED && toolWindow.isAutoHide())
                || (settings.switchUndocked && toolWindow.getType() == ToolWindowType.SLIDING)
                || (settings.switchFloat && toolWindow.getType() == ToolWindowType.FLOATING);
    }

    private List<String> getLastShownToolWindows() {
        return settings.lastShownToolWindows.get(anchor.toString());
    }

    private void setLastShownToolWindows(List<String> toolWindowIds) {
        settings.lastShownToolWindows.put(anchor.toString(), toolWindowIds);
    }

    private String removeLastFocusedToolWindow() {
        return settings.lastFocusedToolWindow.remove(anchor.toString());
    }

    private void setLastFocusedToolWindow(String toolWindowId) {
        settings.lastFocusedToolWindow.put(anchor.toString(), toolWindowId);
    }
}
