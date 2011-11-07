/*******************************************************************************
 * Copyright (c) 2011 Wind River Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Wind River Systems - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.debug.internal.ui.breakpoints;

import org.eclipse.cdt.debug.ui.CDebugUIPlugin;
import org.eclipse.cdt.debug.ui.breakpoints.IBreakpointFilterFactory;
import org.eclipse.cdt.debug.ui.breakpoints.ICreateBreakpointFilterRequest;
import org.eclipse.cdt.debug.ui.breakpoints.IEnableBreakpointFilterRequest;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * 
 */
public class LaunchBreakpointFilterFactory implements IBreakpointFilterFactory {

    public void execute(ICreateBreakpointFilterRequest request) {
        ILaunch launch = getLaunch(request.getDebugContext());
        if (launch != null) {
            request.setFilters(new IAdaptable[] { launch });
        } else {
            request.setStatus(new Status(IStatus.ERROR, CDebugUIPlugin.PLUGIN_ID, "No Launch"));
        }
        request.done();
    }

    public void canExecute(IEnableBreakpointFilterRequest request) {
        ILaunch launch = getLaunch(request.getDebugContext());
        request.setEnabled(launch != null);
        request.done();
    }

    private ILaunch getLaunch(IStructuredSelection context) {
        if (!context.isEmpty()) {
            Object element = context.getFirstElement();
            if (element instanceof IDebugElement) {
                return ((IDebugElement)element).getLaunch();
            } else {
                return (ILaunch)DebugPlugin.getAdapter(element, ILaunch.class);
            }
        }
        return null;
    }
    
}

