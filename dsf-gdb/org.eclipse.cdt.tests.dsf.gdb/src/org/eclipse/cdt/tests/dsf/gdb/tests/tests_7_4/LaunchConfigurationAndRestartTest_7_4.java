/*******************************************************************************
 * Copyright (c) 2012 Ericsson and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Marc Khouzam (Ericsson) - Initial implementation of Test cases
 *******************************************************************************/
package org.eclipse.cdt.tests.dsf.gdb.tests.tests_7_4;

import org.eclipse.cdt.tests.dsf.gdb.framework.BackgroundRunner;
import org.eclipse.cdt.tests.dsf.gdb.tests.ITestConstants;
import org.eclipse.cdt.tests.dsf.gdb.tests.tests_7_3.LaunchConfigurationAndRestartTest_7_3;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(BackgroundRunner.class)
public class LaunchConfigurationAndRestartTest_7_4 extends LaunchConfigurationAndRestartTest_7_3 {
	// For the launch config test, we must set the attributes in the @Before method
	// instead of the @BeforeClass method.  This is because the attributes are overwritten
	// by the tests themselves
	@Before
	public void beforeMethod_7_4() {
		setGdbProgramNamesLaunchAttributes(ITestConstants.SUFFIX_GDB_7_4);
	}
}
