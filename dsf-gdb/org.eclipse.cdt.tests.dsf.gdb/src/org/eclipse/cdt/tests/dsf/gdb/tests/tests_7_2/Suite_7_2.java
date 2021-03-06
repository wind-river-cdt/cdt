/*******************************************************************************
 * Copyright (c) 2010 Ericsson and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ericsson			  - Initial Implementation
 *******************************************************************************/
package org.eclipse.cdt.tests.dsf.gdb.tests.tests_7_2;

import org.eclipse.cdt.dsf.mi.service.command.commands.Suite_Sessionless_Tests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * This class is meant to be empty.  It enables us to define
 * the annotations which list all the different JUnit class we
 * want to run.  When creating a new test class, it should be
 * added to the list below.
 * 
 * This suite is for tests to be run with GDB 7.2
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
	// We need specific name for the tests of this suite, because of bug https://bugs.eclipse.org/172256
	MIRegistersTest_7_2.class,
	MIRunControlTest_7_2.class,
	MIRunControlTargetAvailableTest_7_2.class,
	MIRunControlNonStopTargetAvailableTest_7_2.class,
	MIExpressionsTest_7_2.class,
	MIMemoryTest_7_2.class,
	MIBreakpointsTest_7_2.class,
	MICatchpointsTest_7_2.class,
	MIDisassemblyTest_7_2.class,
	GDBProcessesTest_7_2.class,
	LaunchConfigurationAndRestartTest_7_2.class,
	OperationsWhileTargetIsRunningTest_7_2.class,
	OperationsWhileTargetIsRunningNonStopTest_7_2.class,
	PostMortemCoreTest_7_2.class,
	CommandTimeoutTest_7_2.class,
	GDBMultiNonStopRunControlTest_7_2.class,
	Suite_Sessionless_Tests.class,	
	/* Add your test class here */
})

public class Suite_7_2 {}
