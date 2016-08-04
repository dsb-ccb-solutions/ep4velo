package org.vaulttec.velocity.ui.preferences;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.preference.IPreferenceStore;

public class JavaPreferenceHelper {

	public JavaPreferenceHelper() {
		// TODO Auto-generated constructor stub
	}
	public static IPreferenceStore getJavaPreferneceStore(){
		IPreferenceStore prefs = JavaPlugin.getDefault().getPreferenceStore();
		return prefs;
	}

}
