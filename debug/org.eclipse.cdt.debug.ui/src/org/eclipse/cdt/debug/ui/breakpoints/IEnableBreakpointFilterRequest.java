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
package org.eclipse.cdt.debug.ui.breakpoints;


/**
 * @since 7.2
 */
public interface IEnableBreakpointFilterRequest extends IBreakpointFilterRequest {
    /**
     * Sets the enabled state of a command handler.
     * 
     * @param result whether enabled
     */
    public void setEnabled(boolean result);
    
}
