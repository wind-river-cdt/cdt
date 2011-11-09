/*******************************************************************************
 * Copyright (c) 2000, 2008 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *     QNX Software Systems - Refactored to use platform implementation
 *******************************************************************************/
package org.eclipse.cdt.debug.internal.ui.propertypages;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.debug.internal.ui.CBreakpointContext;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * A preference store that presents the state of the properties of a C/C++ breakpoint. 
 */
public class CBreakpointPreferenceStore implements IPersistentPreferenceStore {

	protected final static String ENABLED = "ENABLED"; //$NON-NLS-1$

	protected final static String CONDITION = "CONDITION"; //$NON-NLS-1$

	protected final static String IGNORE_COUNT = "IGNORE_COUNT"; //$NON-NLS-1$

	protected final static String LINE = "LINE"; //$NON-NLS-1$

    protected HashMap<String, Object> fProperties = new HashMap<String, Object>();
    private boolean fIsDirty = false; 
    private ListenerList fListeners;
    private CBreakpointContext fContext;

    public CBreakpointPreferenceStore() {
        fListeners = new ListenerList(org.eclipse.core.runtime.ListenerList.IDENTITY);
    }

    public void setContext(CBreakpointContext context) {
        fContext = context;
        
        if (fContext != null) {
            IMarker marker = context.getBreakpoint().getMarker();
            if (marker != null) {
                Map<String, Object> bpAttrs = Collections.emptyMap();
                try {
                    bpAttrs = marker.getAttributes();
                } catch (CoreException e) {}
                fProperties.clear();
                fProperties.putAll(bpAttrs);
            }
        } else {
            fProperties.clear();
        }
    }
    
    public void save() throws IOException {
        // TODO: save data to breakpoint marker(s)
    }
    
    ///////////////////////////////////////////////////////////////////////
    // IPreferenceStore
    
    public boolean needsSaving() { return fIsDirty; }

    public boolean contains(String name) {
        return fProperties.containsKey(name);
    }

    public void addPropertyChangeListener(IPropertyChangeListener listener) {
        fListeners.add(listener);
    }

    public void removePropertyChangeListener(IPropertyChangeListener listener) {
        fListeners.remove(listener);
    }

    public void firePropertyChangeEvent(String name,
                                        Object oldValue,
                                        Object newValue) 
    {
        Object[] listeners = fListeners.getListeners();
        // Do we need to fire an event.
        if (listeners.length > 0 && (oldValue == null || !oldValue.equals(newValue))) {
            PropertyChangeEvent pe = new PropertyChangeEvent(this, name, oldValue, newValue);
            for (int i = 0; i < listeners.length; ++i) {
                IPropertyChangeListener l = (IPropertyChangeListener) listeners[i];
                l.propertyChange(pe);
            }
        }
    }
    public boolean getBoolean(String name) {
        boolean retVal = false;
        Object o = fProperties.get(name);
        if (o instanceof Boolean) {
            retVal = ((Boolean)o).booleanValue();
        }
        return retVal;
    }

    public int getInt(String name) {
        int retVal = 0;
        Object o = fProperties.get(name);
        if (o instanceof Integer) {
            retVal = ((Integer)o).intValue();
        }
        return retVal;
    }

    public String getString(String name) {
        String retVal = null;
        Object o = fProperties.get(name);
        if (o instanceof String) {
            retVal = (String)o;
        }
        return retVal;
    }

    public double getDouble(String name) { return 0; }
    public float getFloat(String name) { return 0; }
    public long getLong(String name) { return 0; }

    public boolean isDefault(String name) { return false; }

    public boolean getDefaultBoolean(String name) { return false; }
    public double getDefaultDouble(String name) { return 0; }
    public float getDefaultFloat(String name) { return 0; }
    public int getDefaultInt(String name) { return 0; }
    public long getDefaultLong(String name) { return 0; }
    public String getDefaultString(String name) { return null; }

    public void putValue(String name, String value) {
        Object oldValue = fProperties.get(name);
        if ( oldValue == null || !oldValue.equals(value) ) {
            fProperties.put(name, value);
            setDirty(true);
        }
    }

    public void setDefault(String name, double value) {}
    public void setDefault(String name, float value) {}
    public void setDefault(String name, int value) {}
    public void setDefault(String name, long value) {}
    public void setDefault(String name, String defaultObject) {}
    public void setDefault(String name, boolean value) {}
    public void setToDefault(String name) {}

    public void setValue(String name, boolean value) {
        boolean oldValue = getBoolean(name);
        if (oldValue != value) {
            fProperties.put( name, new Boolean(value) );
            setDirty(true);
            firePropertyChangeEvent(
                name, new Boolean(oldValue), new Boolean(value) );
        }
    }

    public void setValue(String name, int value) {
        int oldValue = getInt(name);
        if (oldValue != value) {
            fProperties.put( name, new Integer(value) );
            setDirty(true);
            firePropertyChangeEvent(
                name, new Integer(oldValue), new Integer(value) );
        }
    }

    public void setValue(String name, String value) {
        Object oldValue = fProperties.get(name);
        if ( (oldValue == null && value != null) ||
             (oldValue != null && !oldValue.equals(value)) ) 
        {
            fProperties.put(name, value);
            setDirty(true);
            firePropertyChangeEvent(name, oldValue, value);
        }
    }

    public void setValue(String name, float value) {}        
    public void setValue(String name, double value) {}
    public void setValue(String name, long value) {}

    // IPreferenceStore
    ///////////////////////////////////////////////////////////////////////        

    private void setDirty(boolean isDirty) {
        fIsDirty = isDirty;
    }
}
