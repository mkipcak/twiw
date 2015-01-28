package com.twiw.trackman;

import org.junit.Test;

import com.twiw.trackman.bean.Talk;

import junit.framework.TestCase;

public class TalkBuilderTests extends TestCase {

	@Test
	public void testTalkBuilderWorksFineForFormatXXXNNmin() {
		//arrange
		TalkBuilder builder = new TalkBuilder();
		//act
		Talk tk = builder.build("Writing Fast Tests Against Enterprise Rails 60min");
		//assert
		assertTrue("build failed!", tk.getValue() == 60);
	}
	@Test
	public void testTalkBuilderWorksFineForFormatXXXlightning() {
		//arrange
		TalkBuilder builder = new TalkBuilder();
		//act
		Talk tk = builder.build("Rails for Python Developers lightning");
		//assert
		assertTrue("build failed!", tk.getValue() == TalkBuilder.LIGHTVAL_INMIN);
	}

}
