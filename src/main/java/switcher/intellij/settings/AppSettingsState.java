package switcher.intellij.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "SidePanelSwitcher", // TODO: Rename?
        storages = {@Storage("SidePanelSwitcher.xml")}
)
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

  public boolean switchUndocked = false;
  public boolean switchDockUnpinned = true;
  public boolean switchFloat = true;

  // NOTE: Only POJO serializes: https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000483244-XmlSerializerUtil-Unable-to-serialize-state-for-PersistentStateComponent.
  public Map<String, List<String>> lastShownToolWindows = new HashMap<>();

  public static AppSettingsState getInstance() {
    return ApplicationManager.getApplication().getService(AppSettingsState.class);
  }

  @Nullable
  @Override
  public AppSettingsState getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull AppSettingsState state) {
    XmlSerializerUtil.copyBean(state, this);
  }
}
