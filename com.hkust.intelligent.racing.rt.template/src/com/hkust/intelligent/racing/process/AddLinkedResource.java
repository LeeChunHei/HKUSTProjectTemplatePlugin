package com.hkust.intelligent.racing.process;

import org.eclipse.cdt.core.templateengine.TemplateCore;
import org.eclipse.cdt.core.templateengine.process.ProcessArgument;
import org.eclipse.cdt.core.templateengine.process.ProcessFailureException;
import org.eclipse.cdt.core.templateengine.process.ProcessRunner;
import org.eclipse.cdt.core.templateengine.process.processes.Messages;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;

public class AddLinkedResource extends ProcessRunner{
	public void process(TemplateCore template, ProcessArgument[] args, String processId, IProgressMonitor monitor)
			throws ProcessFailureException {
		IProject projectHandle = null;
		String path = null;
		String startPattern = null;
		String endPattern = null;
		String target = null;
		
		for (ProcessArgument arg : args) {
			String argName = arg.getName();
			if (argName.equals("projectName")) { //$NON-NLS-1$
				projectHandle = ResourcesPlugin.getWorkspace().getRoot().getProject(arg.getSimpleValue());
			} else if (argName.equals("path")) { //$NON-NLS-1$
				path = arg.getSimpleValue();
			} else if (argName.equals("startPattern")) { //$NON-NLS-1$
				startPattern = arg.getSimpleValue();
			} else if (argName.equals("endPattern")) { //$NON-NLS-1$
				endPattern = arg.getSimpleValue();
			} else if (argName.equals("target")) { //$NON-NLS-1$
				target = arg.getSimpleValue();
			}
		}
		
		if (projectHandle == null)
			throw new ProcessFailureException(
					getProcessMessage(processId, IStatus.ERROR, Messages.getString("AddFiles.8"))); //$NON-NLS-1$

		IFolder link = projectHandle.getFolder(target);
		IPath location = new Path(path);
		try {
			link.createLink(location, IResource.NONE, null);
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			projectHandle.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			throw new ProcessFailureException(Messages.getString("AddFiles.7") + e.getMessage(), e); //$NON-NLS-1$
		}
	}
}
