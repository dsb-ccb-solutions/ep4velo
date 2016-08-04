package com.langtags.ep4velo;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.vaulttec.velocity.ui.IPreferencesConstants;
import org.vaulttec.velocity.ui.VelocityColorProvider;

public class Ep4VeloPreferenceInitializer extends AbstractPreferenceInitializer {

	public Ep4VeloPreferenceInitializer() {
		// TODO Auto-generated constructor stub
	}

	public void initializeDefaultPreferences() {
		IPreferenceStore aStore = Activator.getDefault().getPreferenceStore();
		aStore.setDefault(IPreferencesConstants.EDITOR_SHOW_SEGMENTS, false);
		aStore.setDefault(IPreferencesConstants.VELOCITY_COUNTER_NAME, 
						  "velocityCount");
		aStore.setDefault(IPreferencesConstants.VELOCITY_USER_DIRECTIVES, 
						  "");
		VelocityColorProvider.initializeDefaults(aStore);
	}

}
