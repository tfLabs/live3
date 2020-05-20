package test.rcp.part;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.IXtextModelListener;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import test.extension.IContentsGenerator;

public class Preview {

	private Text text = null;
	private ArrayList<IEditorPart> editors = new ArrayList<IEditorPart>();
	private static final String GENERATOR_EXTENSION_ID = "test.extension.test";
	private static final String GENERATOR_EXTENSION_LANGUAGE = "language";
	private static final String GENERATOR_EXTENSION_CLASS = "class";
	private Map<String, Object> generatorCache = new HashMap<String, Object>();
	private Map<Object, String> contentCache = new HashMap<Object, String>();

	public Preview() {
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		workbenchWindow.getPartService().addPartListener(new AbstractPartListener() {

			@Override
			public void partOpened(IWorkbenchPart arg0) {
				entryEditor(arg0);
			}

			@Override
			public void partClosed(IWorkbenchPart arg0) {
				eraseEditor(arg0);
			}

			@Override
			public void partActivated(IWorkbenchPart arg0) {
				replaceEditor(arg0);
			}
		});
	}

	@PostConstruct
	public void createControls(Composite parent) {
		text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
	}

	private void entryEditor(IWorkbenchPart arg0) {
		if (arg0 instanceof XtextEditor) {
			if (!editors.contains(arg0)) {
				XtextEditor x = (XtextEditor) arg0;
				editors.add(x);
				IXtextDocument d = x.getDocument();
				d.addModelListener(new IXtextModelListener() {
					@Override
					public void modelChanged(XtextResource resource) {
						setText(resource);
					}
				});
				refreshText(getResource(x));
			}
		}
	}

	@Focus
	public void entryActiveEditor() {
		IEditorPart e = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		entryEditor(e);
	}

	private void refreshText(Resource resource) {
		if (resource == null) {
			setText("");
		} else if (resource instanceof XtextResource) {
			setText((XtextResource) resource);
		}
	}

	private Resource getResource(XtextEditor x) {
		EObjectAtOffsetHelper helper = new EObjectAtOffsetHelper();
		int offset = 0;
		IXtextDocument d = x.getDocument();
		EObject object = d.readOnly(new IUnitOfWork<EObject, XtextResource>() {
			@Override
			public EObject exec(XtextResource state) throws Exception {
				return helper.resolveContainedElementAt(state, offset);
			}
		});
		if (object != null) {
			return object.eResource();
		}
		return null;
	}

	private void eraseEditor(IWorkbenchPart arg0) {
		if (arg0 instanceof XtextEditor) {
			if (editors.contains(arg0)) {
				XtextEditor x = (XtextEditor) arg0;
				editors.remove(x);
				if (contentCache.containsKey(getResource(x))) {
					contentCache.remove(getResource(x));
				}
				if (editors.isEmpty()) {
					refreshText(null);
					contentCache.clear();
				}
			}
		}
	}

	private void replaceEditor(IWorkbenchPart arg0) {
		if (arg0 instanceof XtextEditor) {
			XtextEditor x = (XtextEditor) arg0;
			refreshText(getResource(x));
		}
	}

	private void setText(XtextResource resource) {
		if (text != null && !text.isDisposed()) {
			if (!resource.getParseResult().hasSyntaxErrors()) {
				contentCache.put(resource, getContent(resource));
			}
			String content = contentCache.get(resource);
			if (content == null) {
				content = "";
			}
			text.setText(content);
		}
	}

	private String getContent(XtextResource resource) {
		String name = resource.getLanguageName();
		String content = "";
		if (!generatorCache.containsKey(name)) {
			IConfigurationElement[] config = Platform.getExtensionRegistry()
					.getConfigurationElementsFor(GENERATOR_EXTENSION_ID);
			try {
				for (IConfigurationElement e : config) {
			final String s = e.getAttribute(GENERATOR_EXTENSION_LANGUAGE);
					if (s.contentEquals(name)) {
						Object o = e.createExecutableExtension(GENERATOR_EXTENSION_CLASS);
						if (o instanceof IContentsGenerator) {
							generatorCache.put(name, o);
							break;
						}
					}
				}
			} catch (CoreException ex) {
				System.out.println(ex.getMessage());
			}
		}
		if (generatorCache.containsKey(name)) {
			Object o = generatorCache.get(name);
			content = ((IContentsGenerator) o).doGenerate(resource);
		}
		return content;
	}

	private void setText(String str) {
		if (text != null && !text.isDisposed()) {
			text.setText(str);
		}
	}
}
