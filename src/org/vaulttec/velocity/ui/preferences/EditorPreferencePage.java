package org.vaulttec.velocity.ui.preferences;

import java.io.IOException;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.vaulttec.velocity.ui.IPreferencesConstants;

import com.langtags.ep4velo.Activator;

/**
 * Color settings for syntax highliting.
 */
public class EditorPreferencePage extends FieldEditorPreferencePage
										 implements IWorkbenchPreferencePage {
	private final String PREFIX = "EditorPreferences.";

	public EditorPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription(Activator.getMessage(PREFIX + "description"));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_DEFAULT,
				Activator.getMessage(PREFIX + "default"),
								 getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_COMMENT,
				Activator.getMessage(PREFIX + "comment"),
								 getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_DOC_COMMENT,
				Activator.getMessage(PREFIX + "docComment"),
							  getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_DIRECTIVE,
				Activator.getMessage(PREFIX + "directive"),
							   getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_STRING,
				Activator.getMessage(PREFIX + "string"),
								  getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_REFERENCE,
				Activator.getMessage(PREFIX + "reference"),
							   getFieldEditorParent()));
		addField(new ColorFieldEditor(
        				 IPreferencesConstants.COLOR_STRING_REFERENCE,
        				 Activator.getMessage(PREFIX + "stringReference"),
						 getFieldEditorParent()));
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench aWorkbench) {
	}

    /* (non-Javadoc)
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
