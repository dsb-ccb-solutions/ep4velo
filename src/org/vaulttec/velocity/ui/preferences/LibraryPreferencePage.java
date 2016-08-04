package org.vaulttec.velocity.ui.preferences;

import java.io.IOException;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.vaulttec.velocity.ui.IPreferencesConstants;

import com.langtags.ep4velo.Activator;

/**
 * Velocimacro library settings.
 */
public class LibraryPreferencePage extends FieldEditorPreferencePage
										 implements IWorkbenchPreferencePage {
	private final String PREFIX = "LibraryPreferences.";

	public LibraryPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription(Activator.getMessage(PREFIX + "description"));
	}

	/**
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		DirectoryFieldEditor macroPath = new DirectoryFieldEditor(
									IPreferencesConstants.LIBRARY_PATH,
									Activator.getMessage(PREFIX + "path"),
									getFieldEditorParent());
		addField(macroPath);

		LibraryEditor library = new LibraryEditor(
							 IPreferencesConstants.LIBRARY_LIST,
							 Activator.getMessage(PREFIX + "libraryList"),
							 macroPath, getFieldEditorParent());
		addField(library);
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
