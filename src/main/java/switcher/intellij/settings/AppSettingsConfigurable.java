package switcher.intellij.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 */
public class AppSettingsConfigurable implements Configurable {

  private AppSettingsComponent mySettingsComponent;

  // A default constructor with no arguments is required because this implementation
  // is registered as an applicationConfigurable EP

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public String getDisplayName() {
    return "Side Panel Switcher Filter";
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return mySettingsComponent.getPreferredFocusedComponent();
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    mySettingsComponent = new AppSettingsComponent();
    return mySettingsComponent.getPanel();
  }

  @Override
  public boolean isModified() {
    AppSettingsState settings = AppSettingsState.getInstance();
    boolean modified = mySettingsComponent.getSwitchUndocked() != settings.switchUndocked;
    modified |= mySettingsComponent.getSwitchDockUnpinned() != settings.switchDockUnpinned;
    modified |= mySettingsComponent.getSwitchFloat() != settings.switchFloat;
    modified |= mySettingsComponent.getFocusOnSwitched() != settings.focusOnSwitched;
    return modified;
  }

  @Override
  public void apply() {
    AppSettingsState settings = AppSettingsState.getInstance();
    settings.switchUndocked = mySettingsComponent.getSwitchUndocked();
    settings.switchDockUnpinned = mySettingsComponent.getSwitchDockUnpinned();
    settings.switchFloat = mySettingsComponent.getSwitchFloat();
    settings.focusOnSwitched = mySettingsComponent.getFocusOnSwitched();
  }

  @Override
  public void reset() {
    AppSettingsState settings = AppSettingsState.getInstance();
    mySettingsComponent.setSwitchUndocked(settings.switchUndocked);
    mySettingsComponent.setSwitchDockUnpinned(settings.switchDockUnpinned);
    mySettingsComponent.setSwitchFloat(settings.switchFloat);
    mySettingsComponent.setFocusOnSwitched(settings.focusOnSwitched);
  }

  @Override
  public void disposeUIResources() {
    mySettingsComponent = null;
  }
}
