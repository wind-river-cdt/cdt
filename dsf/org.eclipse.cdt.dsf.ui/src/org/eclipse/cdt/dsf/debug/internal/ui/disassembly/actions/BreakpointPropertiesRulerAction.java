/*******************************************************************************
 * Copyright (c) 2008, 2009 Wind River Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Wind River Systems - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.dsf.debug.internal.ui.disassembly.actions;

import org.eclipse.cdt.debug.core.model.ICBreakpoint;
import org.eclipse.cdt.debug.ui.breakpoints.CBreakpointPropertyDialogAction;
import org.eclipse.cdt.dsf.debug.internal.ui.disassembly.DisassemblyMessages;
import org.eclipse.cdt.dsf.debug.internal.ui.disassembly.provisional.IDisassemblyPart;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.contexts.IDebugContextListener;
import org.eclipse.debug.ui.contexts.IDebugContextProvider;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Ruler action to display breakpoint properties.
 */
public class BreakpointPropertiesRulerAction extends AbstractDisassemblyBreakpointRulerAction {
    
    
	private ICBreakpoint fBreakpoint;

	protected BreakpointPropertiesRulerAction(IDisassemblyPart disassemblyPart, IVerticalRulerInfo rulerInfo) {
		super(disassemblyPart, rulerInfo);
		setText(DisassemblyMessages.Disassembly_action_BreakpointProperties_label);
	}
	
	/*
	 * @see org.eclipse.cdt.dsf.debug.internal.ui.disassembly.actions.AbstractDisassemblyAction#run()
	 */
	@Override
	public void run() {
		if ( fBreakpoint != null ) {
		    final ISelection debugContext = getDebugContext();
		    
            CBreakpointPropertyDialogAction propertiesAction = new CBreakpointPropertyDialogAction(
                getDisassemblyPart().getSite(), 
                new ISelectionProvider() {
                    public ISelection getSelection() {
                        return new StructuredSelection( fBreakpoint );
                    }
                    public void addSelectionChangedListener( ISelectionChangedListener listener ) {}
                    public void removeSelectionChangedListener( ISelectionChangedListener listener ) {}
                    public void setSelection( ISelection selection ) {}
                }, 
                new IDebugContextProvider() {
                    public ISelection getActiveContext() {
                        return debugContext;
                    }
                    public void addDebugContextListener(IDebugContextListener listener) {}
                    public void removeDebugContextListener(IDebugContextListener listener) {}
                    public IWorkbenchPart getPart() { return null; }
                    
                }
                );
            propertiesAction.run();
            propertiesAction.dispose();
		}
	}

	/*
	 * @see IUpdate#update()
	 */
	@Override
	public void update() {
	    IBreakpoint breakpoint= getBreakpoint();
	    
	    if (breakpoint instanceof ICBreakpoint) {
	        fBreakpoint = (ICBreakpoint)breakpoint;
	    } else {
	        fBreakpoint = null;
	    }
		setEnabled( fBreakpoint != null );
	}
	
	private ISelection getDebugContext() {
	    return DebugUITools.getDebugContextManager().getContextService(getDisassemblyPart().getSite().getWorkbenchWindow()).getActiveContext();
	}

}
