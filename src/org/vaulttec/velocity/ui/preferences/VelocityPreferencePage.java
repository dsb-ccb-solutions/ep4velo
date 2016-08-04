package org.vaulttec.velocity.ui.preferences;

import java.io.IOException;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.vaulttec.velocity.ui.IPreferencesConstants;

import com.langtags.ep4velo.Activator;

/**
 * Velocity runtime settings, e.g. loop counter name.
 */
public class VelocityPreferencePage extends FieldEditorPreferencePage
										 implements IWorkbenchPreferencePage {
	private final String PREFIX = "VelocityPreferences.";

	public VelocityPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription(Activator.getMessage(PREFIX + "description"));
	}

	/**
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		StringFieldEditor counterName = new StringFieldEditor(
							 IPreferencesConstants.VELOCITY_COUNTER_NAME,
							 Activator.getMessage(PREFIX + "counterName"),
							 getFieldEditorParent());
		counterName.setEmptyStringAllowed(false);
		addField(counterName);

		DirectiveEditor directives = new DirectiveEditor(
						  IPreferencesConstants.VELOCITY_USER_DIRECTIVES,
						  Activator.getMessage(PREFIX + "userDirectives"),
						  getFieldEditorParent());
		addField(directives);
    }

	/**
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench aWorkbench) {
	}

    /**
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
        boolean value = super.performOk();
        
        try {
			((IPersistentPreferenceStore)Activator.getDefault().getPreferenceStore()).save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return value;
    }
}
