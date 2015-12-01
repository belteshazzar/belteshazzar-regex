package com.belteshazzar.regex;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SimpleTest
{
	@Test
	public void simpleTest01()
	{
		Pattern<String> pattern = new Pattern<String>()
				.add("not")
				.add('(')
				.add("matches")
				.add('{')
				.add(2)
				.add('}')
				.add(')')
				.compile();
		
		List<String> input = Arrays.asList(
				"not",
				"matches",
				"matches",
				"not",
				"matches",
				"not",
				"matches",
				"matches",
				"not");	
		
		Matcher<String> matcher = pattern.matcher(input);
		
		while (matcher.find())
		{
			System.out.println(matcher.start());
			System.out.println(matcher.group());
			System.out.println(matcher.group(0));
			System.out.println(matcher.group(1));
		}
	}
	
	@Test(expected = IllegalStateException.class)
	public void addAfterCompile()
	{
		@SuppressWarnings("unused")
		Pattern<String> pattern = new Pattern<String>()
				.add("fred")
				.compile()
				.add("error");
	}
}
