package switcher.intellij.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class AppSettingsComponent {

  private final JPanel myMainPanel;
  private final JBCheckBox switchUndocked = new JBCheckBox("Switch undocked windows");
  private final JBCheckBox switchDockUnpinned = new JBCheckBox("Switch dock unpinned windows");
  private final JBCheckBox switchFloat = new JBCheckBox("Switch float windows");
  private final JBCheckBox focusOnSwitched = new JBCheckBox("Focus on shown windows (When one of them has been active last time)");

  public AppSettingsComponent() {
    myMainPanel = FormBuilder.createFormBuilder()
            .addComponent(new JBLabel("Tool windows filtering options:"))
            .addComponent(switchUndocked, 1) //TODO: sort out what is topInset
            .addComponent(switchDockUnpinned, 1)
            .addComponent(switchFloat, 1)
            .addComponent(new JBLabel("Other options:"))
            .addComponent(focusOnSwitched, 1)
            .addComponentFillVertically(new JPanel(), 0)
            .getPanel();
  }

  public JPanel getPanel() {
    return myMainPanel;
  }

  public JComponent getPreferredFocusedComponent() {
    return switchUndocked;
  }

  // region get set
  public boolean getSwitchUndocked() {
    return switchUndocked.isSelected();
  }

  public void setSwitchUndocked(boolean status) {
    switchUndocked.setSelected(status);
  }

  public boolean getSwitchDockUnpinned() {
    return switchDockUnpinned.isSelected();
  }

  public void setSwitchDockUnpinned(boolean status) {
    switchDockUnpinned.setSelected(status);
  }

  public boolean getSwitchFloat() {
    return switchFloat.isSelected();
  }

  public void setSwitchFloat(boolean status) {
    switchFloat.setSelected(status);
  }

  public boolean getFocusOnSwitched() {
    return focusOnSwitched.isSelected();
  }

  public void setFocusOnSwitched(boolean status) {
    focusOnSwitched.setSelected(status);
  }
  // endregion
}
