package com.twiw.trackman;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.twiw.trackman.bean.SessionFactoryTests;

@RunWith(Suite.class)
@SuiteClasses({ 
	SessionFactoryTests.class,
	TalkBuilderTests.class,
	TrackOptimizerEndToEndTest.class 
})
public class PendingTests {

}
