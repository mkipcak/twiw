package com.twiw.trackman;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TalkBuilderTests.class,
	TrackOptimizerEndToEndTest.class 
})
public class PendingTests {

}
