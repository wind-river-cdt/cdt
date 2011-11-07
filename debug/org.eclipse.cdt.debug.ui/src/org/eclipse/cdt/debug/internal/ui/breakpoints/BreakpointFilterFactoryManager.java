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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.cdt.debug.internal.ui.CDebugUIUtils;
import org.eclipse.cdt.debug.ui.CDebugUIPlugin;
import org.eclipse.cdt.debug.ui.breakpoints.IBreakpointFilterFactory;
import org.eclipse.cdt.debug.ui.breakpoints.ICreateBreakpointFilterRequest;
import org.eclipse.cdt.debug.ui.breakpoints.IEnableBreakpointFilterRequest;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.expressions.ExpressionTagNames;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.internal.core.IConfigurationElementConstants;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.IDetailPaneFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 
 */
public class BreakpointFilterFactoryManager {

    private class FactoryProxy implements IBreakpointFilterFactory {

        private IConfigurationElement fConfigElement;
        private IBreakpointFilterFactory fFactory;
        private Expression fEnablementExpression;
        
        public FactoryProxy(IConfigurationElement configElement){
            fConfigElement = configElement;         
        }
        
        public void execute(ICreateBreakpointFilterRequest request) {
            if (getFactory() != null){
                getFactory().execute(request);
            } else {
                request.setStatus(new Status(IStatus.ERROR, CDebugUIPlugin.PLUGIN_ID, "Unable to create factory"));
                request.done();
            }
        }
        
        public void canExecute(IEnableBreakpointFilterRequest request) {
            if (getFactory() != null){
                getFactory().canExecute(request);
            } else {
                request.setEnabled(false);
                request.done();
            }
        }
        
        /**
         * Returns the instantiated factory specified by the class property. 
         * @return the singleton {@link IDetailPaneFactory}
         */
        private IBreakpointFilterFactory getFactory(){
            if (fFactory != null) return fFactory;
            try{
                Object obj = fConfigElement.createExecutableExtension(IConfigurationElementConstants.CLASS);
                if(obj instanceof IDetailPaneFactory) {
                    fFactory = (IBreakpointFilterFactory)obj;
                } else {
                    throw new CoreException(new Status(IStatus.ERROR, DebugUIPlugin.getUniqueIdentifier(), IDebugUIConstants.INTERNAL_ERROR, "org.eclipse.cdt.debug.ui.breakpointFilterFactories extension failed to load a detail factory because the specified class does not implement IBreakpointFilterFactory.  Class specified was: " + obj, null)); //$NON-NLS-1$
                }   
            } catch (CoreException e){
                CDebugUIPlugin.log(e.getStatus());
                fFactory = null;
            }
            return fFactory;
        }
        
        /**
         * Checks if the enablement expression for the factory evaluates to true for the
         * given selection.
         * @param debugContext active debug context in window or view
         * @param selection the current view selection
         * @return <code>true</code> if the backing {@link IBreakpointFilterFactory} applies to the given selection, <code>false</code> otherwise
         */
        public boolean isEnabled(IStructuredSelection debugContext, IStructuredSelection selection) {
            boolean enabled = false;
            
            if (debugContext == null) debugContext = StructuredSelection.EMPTY;
            if (selection == null) selection = StructuredSelection.EMPTY;
            
            // Only the default factory should be enabled for null selections
            Expression expression = getEnablementExpression();
            if (expression != null) {
                List<?> list = debugContext.toList();
                IEvaluationContext context = CDebugUIUtils.createEvaluationContext(list);
                context.addVariable("debugContext", list); //$NON-NLS-1$
                context.addVariable("selection", selection.toList()); //$NON-NLS-1$
                enabled = evalEnablementExpression(context, expression);
            } else {
                enabled = true;
            }
            return enabled;
        }
        
        /**
         * Evaluate the given expression within the given context and return
         * the result. Returns <code>true</code> iff result is either TRUE or NOT_LOADED.
         * This allows optimistic inclusion of shortcuts before plug-ins are loaded.
         * Returns <code>false</code> if expression is <code>null</code>.
         * 
         * @param exp the enablement expression to evaluate or <code>null</code>
         * @param context the context of the evaluation. Usually, the
         *  user's selection.
         * @return the result of evaluating the expression
         */
        private boolean evalEnablementExpression(IEvaluationContext context, Expression exp) {
            try{
                if (exp != null){
                    EvaluationResult result = exp.evaluate(context);
                    if (result == EvaluationResult.TRUE || result == EvaluationResult.NOT_LOADED){
                        return true;
                    }
                }
            } catch (CoreException e){
                DebugUIPlugin.log(e.getStatus());
            }
            return false;
        }
        
        /**
         * Returns an expression that represents the enablement logic for the
         * detail pane factory or <code>null</code> if none.
         * 
         * @return an evaluatable expression or <code>null</code>
         */
        private Expression getEnablementExpression(){
            // all of this stuff is optional, so...tedious testing is required
            if (fEnablementExpression == null) {
                try{
                    IConfigurationElement[] elements = fConfigElement.getChildren(ExpressionTagNames.ENABLEMENT);
                    IConfigurationElement enablement = elements.length > 0 ? elements[0] : null; 
                    if (enablement != null) {
                        fEnablementExpression = ExpressionConverter.getDefault().perform(enablement);
                    }
                } catch (CoreException e){
                    DebugUIPlugin.log(e.getStatus());
                    fEnablementExpression = null;
                }
            }
            return fEnablementExpression;
        }
    
    }

    /**
     * The set of all factories that have been loaded from the extension point.
     */
    private Collection<FactoryProxy> fKnownFactories;


    private synchronized void initializeDetailFactories(){
        if (fKnownFactories == null){
            fKnownFactories = new ArrayList<FactoryProxy>();
            IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(DebugUIPlugin.getUniqueIdentifier(), "BreakpointFilterFactory");
            IConfigurationElement[] infos = extensionPoint.getConfigurationElements();
            FactoryProxy delegate = null;
            for(int i = 0; i < infos.length; i++) {
                delegate = new FactoryProxy(infos[i]);
                fKnownFactories.add(delegate);
            }       
        }
    }
    
    private Collection<FactoryProxy> getEnabledFactories(IStructuredSelection debugContext, IStructuredSelection selection){
        Collection<FactoryProxy> enabledFactories = new ArrayList<FactoryProxy>();
        if (fKnownFactories == null) initializeDetailFactories();
        for (FactoryProxy factory : fKnownFactories) {
            if (factory.isEnabled(debugContext, selection)){
                enabledFactories.add(factory);
            }
        }   
        return enabledFactories;
    }

    
}
