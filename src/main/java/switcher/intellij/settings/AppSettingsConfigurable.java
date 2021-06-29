// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

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
    return "Side Panel Switcher Settings";
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
//    boolean modified = !mySettingsComponent.getUserNameText().equals(settings.userId);
    boolean modified = mySettingsComponent.getSwitchUndocked() != settings.switchUndocked;
    modified |= mySettingsComponent.getSwitchDockUnpinned() != settings.switchDockUnpinned;
    modified |= mySettingsComponent.getSwitchFloat() != settings.switchFloat;
    return modified;
  }

  @Override
  public void apply() {
    AppSettingsState settings = AppSettingsState.getInstance();
//    settings.userId = mySettingsComponent.getUserNameText();
    settings.switchUndocked = mySettingsComponent.getSwitchUndocked();
    settings.switchDockUnpinned = mySettingsComponent.getSwitchDockUnpinned();
    settings.switchFloat = mySettingsComponent.getSwitchFloat();
  }

  @Override
  public void reset() {
    AppSettingsState settings = AppSettingsState.getInstance();
//    mySettingsComponent.setUserNameText(settings.userId);
    mySettingsComponent.setSwitchUndocked(settings.switchUndocked);
    mySettingsComponent.setSwitchDockUnpinned(settings.switchDockUnpinned);
    mySettingsComponent.setSwitchFloat(settings.switchFloat);
  }

  @Override
  public void disposeUIResources() {
    mySettingsComponent = null;
  }
}
