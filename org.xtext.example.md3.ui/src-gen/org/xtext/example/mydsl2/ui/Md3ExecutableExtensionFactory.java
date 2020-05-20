/*
 * generated by Xtext 2.20.0
 */
package org.xtext.example.mydsl2.ui;

import com.google.inject.Injector;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;
import org.xtext.example.md3.ui.internal.Md3Activator;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class Md3ExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return Platform.getBundle(Md3Activator.PLUGIN_ID);
	}
	
	@Override
	protected Injector getInjector() {
		Md3Activator activator = Md3Activator.getInstance();
		return activator != null ? activator.getInjector(Md3Activator.ORG_XTEXT_EXAMPLE_MYDSL2_MD3) : null;
	}

}