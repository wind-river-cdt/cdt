/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.debug.internal.ui.breakpoints;

import org.eclipse.cdt.debug.internal.ui.CDebugUIUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Plain status collector for actions. Has no result.
 * 
 * @since 3.3
 * 
 */
public class ExecuteActionRequest extends DebugCommandRequest {
	
	private ICommandParticipant fParticipant = null;
	
	public ExecuteActionRequest(IStructuredSelection debugContext, ISelection selection) {
		super(debugContext, selection);
	}

    public void done() {
    	if (fParticipant != null) {
			fParticipant.requestDone(this);
			fParticipant = null;
		}
        final IStatus status = getStatus();
        if (status != null) {
            switch (status.getSeverity()) {
            case IStatus.ERROR:
                CDebugUIUtils.getStandardDisplay().asyncExec(new Runnable() {
                    public void run() {
                        MessageDialog.openError(CDebugUIUtils.getShell(), "Error", status.getMessage());
                    }
                });
                break;
            case IStatus.WARNING:
                CDebugUIUtils.getStandardDisplay().asyncExec(new Runnable() {
                    public void run() {
                        MessageDialog.openWarning(CDebugUIUtils.getShell(), "Error", status.getMessage());
                    }
                });
                break;
            case IStatus.INFO:
                CDebugUIUtils.getStandardDisplay().asyncExec(new Runnable() {
                    public void run() {
                        MessageDialog.openInformation(CDebugUIUtils.getShell(), "Error", status.getMessage());
                    }
                });
                break;
            }
        }
    }
    
	public void setCommandParticipant(ICommandParticipant participant) {
		fParticipant = participant;
	}

}
