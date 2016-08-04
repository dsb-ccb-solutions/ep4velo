package com.langtags.ep4velo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.vaulttec.velocity.ui.IPreferencesConstants;
import org.vaulttec.velocity.ui.editor.VelocityEditorEnvironment;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IPropertyChangeListener{

	// The plug-in ID
	public static final String PLUGIN_ID = "com.langtags.ep4velo"; //$NON-NLS-1$
	private static final String RESOURCE_NAME = PLUGIN_ID + ".messages";
	// The shared instance
	private static Activator plugin;
	private static ResourceBundle fResourceBundle;
	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
		
		
	}
	
	public static List<String> getVelocityUserDirectives() {
		IPreferenceStore store = getDefault().getPreferenceStore();
		String directives = store.getString(
							   IPreferencesConstants.VELOCITY_USER_DIRECTIVES);
		StringTokenizer st = new StringTokenizer(directives, ",\n\r");
		ArrayList<String> list = new ArrayList<String>();
		while (st.hasMoreElements()) {
			list.add((String)st.nextElement());
		}
		return list;
	}
	public static ResourceBundle getResourceBundle() {
		if(fResourceBundle == null){
			try {
				fResourceBundle = ResourceBundle.getBundle(RESOURCE_NAME);
			} catch (MissingResourceException e) {
//				log(e);
				fResourceBundle = null;
			}
		}
		return fResourceBundle;
	}
	public static void log(IStatus aStatus) {
		getDefault().getLog().log(aStatus);
	}
	public void propertyChange(PropertyChangeEvent anEvent) {
		String prop = anEvent.getProperty();
		if (prop.equals(IPreferencesConstants.VELOCITY_USER_DIRECTIVES) ||
							 prop.equals(IPreferencesConstants.LIBRARY_LIST)) {
			VelocityEditorEnvironment.createVelocityParser();
		}
	}
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		return (window != null ? window.getShell() : null);
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}
	public static boolean isDebug() {
		return getDefault().isDebugging();
	}

	public static boolean isDebug(String anOption) {
		boolean debug;
		if (isDebug()) {
			String value = Platform.getDebugOption(anOption);
			debug = (value != null && value.equalsIgnoreCase("true") ?
					 true : false);
		} else {
			debug = false;
		}
		return debug;
	}
	public static void log(Throwable aThrowable) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, Status.OK,
						getMessage("VelocityPlugin.internal_error"),
						aThrowable));
	}
	
	public static void logErrorMessage(String aMessage) {
		log(new Status(IStatus.ERROR,PLUGIN_ID, Status.OK, aMessage,
			null));
	}
	public static String getMessage(String aKey) {
	    String bundleString;
		ResourceBundle bundle = getResourceBundle();
		if (bundle != null) {
			try {
				bundleString = bundle.getString(aKey);
			} catch (MissingResourceException e) {
			    log(e);
				bundleString = "!" + aKey + "!";
			}
		} else {
			bundleString = "!" + aKey + "!";
		}
		return bundleString;
	}
	public static String getFormattedMessage(String aKey, String anArg) {
		return getFormattedMessage(aKey, new String[] { anArg });
	}

	public static String getFormattedMessage(String aKey, Object[] anArgs) {
		return MessageFormat.format(getMessage(aKey), anArgs);
	}
	public static void logErrorStatus(String aMessage, IStatus aStatus) {
		if (aStatus == null) {
			logErrorMessage(aMessage);
		} else {
			MultiStatus multi = new MultiStatus(PLUGIN_ID, Status.OK,
											    aMessage, null);
			multi.add(aStatus);
			log(multi);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		getPreferenceStore().addPropertyChangeListener(this);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
