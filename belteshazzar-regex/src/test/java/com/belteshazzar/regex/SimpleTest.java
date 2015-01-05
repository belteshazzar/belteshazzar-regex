package com.belteshazzar.regex;

import java.util.LinkedList;
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
		
		List<String> input = new LinkedList<String>();
		input.add("not");
		input.add("matches");
		input.add("matches");
		input.add("not");
		input.add("matches");
		input.add("not");
		input.add("matches");
		input.add("matches");
		input.add("not");	
		
		Matcher<String> matcher = pattern.matcher(input);
		
		while (matcher.find())
		{
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
