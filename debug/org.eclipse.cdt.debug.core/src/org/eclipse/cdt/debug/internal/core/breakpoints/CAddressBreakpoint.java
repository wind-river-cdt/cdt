/*******************************************************************************
 * Copyright (c) 2004, 2009 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * QNX Software Systems - Initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.debug.internal.core.breakpoints;

import java.util.Map;

import org.eclipse.cdt.debug.core.CDebugUtils;
import org.eclipse.cdt.debug.core.model.ICAddressBreakpoint;
import org.eclipse.cdt.debug.core.model.ICBreakpoint;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import com.ibm.icu.text.MessageFormat;

/**
 * A breakpoint that suspend the execution when a particular address is reached.
 */
public class CAddressBreakpoint extends AbstractLineBreakpoint implements ICAddressBreakpoint {

	private static final String C_ADDRESS_BREAKPOINT = "org.eclipse.cdt.debug.core.cAddressBreakpointMarker"; //$NON-NLS-1$

	/**
	 * Constructor for CAddressBreakpoint.
	 */
	public CAddressBreakpoint() {
	}

	/**
	 * Constructor for CAddressBreakpoint.
	 */
	public CAddressBreakpoint( IResource resource, Map attributes, boolean add ) throws CoreException {
		super( resource, getMarkerType(), attributes, add );
	}

	/**
	 * Returns the type of marker associated with this type of breakpoints
	 */
	public static String getMarkerType() {
		return C_ADDRESS_BREAKPOINT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.debug.internal.core.breakpoints.CBreakpoint#getMarkerMessage()
	 */
	protected String getMarkerMessage() throws CoreException {
		return MessageFormat.format( BreakpointMessages.getString( "CAddressBreakpoint.0" ), (Object[])new String[] { CDebugUtils.getBreakpointText( this, false ) } ); //$NON-NLS-1$
	}

	public int getInstalledLineNumber() throws CoreException {
		IMarker m = getMarker();
		if (m != null) {
			return m.getAttribute(ICBreakpoint.ATTR_REQUESTED_LINE, -1);
		}
		return -1;
	}
	public void setInstalledLineNumber(int lineNum) throws CoreException {
		IMarker m = getMarker();
		if (m != null) {
			m.setAttribute(ICBreakpoint.ATTR_REQUESTED_LINE, lineNum);
		}		
	}
}
