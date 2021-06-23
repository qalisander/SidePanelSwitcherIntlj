package switcher.intellij.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// NOTE: do not switch Undocked
// NOTE: Dock Unpinned window by settings toggle.
// NOTE: switch float window by settings toggle.
// NOTE: add focus on window switch
// NOTE: tool window actions like attaching floating window to border
public class ToolWindowSwitcher {
    static public void switchWindow(ToolWindowAnchor anchor, AnActionEvent event) {
        Project project = (Project) event.getDataContext().getData("project");
        if (project == null)
            return;

        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

        List<ToolWindow> toolWindows = Arrays.stream(toolWindowManager.getToolWindowIds())
                .map(id -> toolWindowManager.getToolWindow(id))
                .filter(tw -> tw.getAnchor() == anchor)
                .collect(Collectors.toList());

        if (toolWindows.stream().anyMatch(tw -> tw.isVisible())) {
            for (ToolWindow toolWindow : toolWindows) {
                toolWindow.hide(null);
            }
        }
        else {
            for (ToolWindow toolWindow : toolWindows) {
                toolWindow.show(null);
            }
        }
    }
}
