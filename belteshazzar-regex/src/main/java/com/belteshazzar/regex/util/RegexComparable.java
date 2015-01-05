package com.belteshazzar.regex.util;

import java.util.regex.Pattern;

public class RegexComparable implements Comparable<String>
{
	private Pattern pattern;
	
	public RegexComparable(String s)
	{
		pattern = Pattern.compile(s);
	}

	public int compareTo(String s)
	{
		return pattern.matcher(s).matches()?0:1;
	}

}
