package com.belteshazzar.regex.util;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.belteshazzar.regex.Matcher;
import com.belteshazzar.regex.Pattern;
import com.belteshazzar.regex.util.RegexComparable;

public class RegexComparableTest
{
	@Test
	public void simpleTest()
	{
		Pattern<String> pattern = new Pattern<String>()
				.add(new RegexComparable("^no.*"))
				.add('(')
				.add(new RegexComparable("^ma.*"))
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
}
