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

import org.eclipse.cdt.debug.ui.breakpoints.IBreakpointFilterRequest;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @since 3.3
 */
public class DebugCommandRequest extends Request implements IBreakpointFilterRequest {
	
    private IStructuredSelection fDebugContext;
	private ISelection fSelection;
	
	public DebugCommandRequest(IStructuredSelection debugContext, ISelection selection) {
		fDebugContext = debugContext;
		fSelection = selection;
	}

	public ISelection getSelection() {
	    return fSelection;
	}
	
	public IStructuredSelection getDebugContext() {
	    return fDebugContext;
	}
}
