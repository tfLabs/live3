package test.rcp.part;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

public abstract class AbstractPartListener implements IPartListener {

	@Override
	abstract public void partActivated(IWorkbenchPart arg0);

	@Override
	public void partBroughtToTop(IWorkbenchPart arg0) {
	}

	@Override
	abstract public void partClosed(IWorkbenchPart arg0);

	@Override
	public void partDeactivated(IWorkbenchPart arg0) {
	}

	@Override
	abstract public void partOpened(IWorkbenchPart arg0);

}
