# Object regex

This is a port of the standard Java regular expression (regex) classes `java.util.regex.Pattern` and `java.util.regex.Matcher` to operate on a `java.util.List` of `java.lang.Comparable` rather than Strings.

What, why? Well this give you the power of regular expressions over any type of object (well, as long as its Comparable).

Lets show an example, the following creates a pattern, but with strings instead of chars. Note that the normal regex characters
that make up a pattern remain the same, but need to be separate elements in the list.

```java
		Pattern<String> pattern = new Pattern<String>()
				.add("not")
				.add('(')
				.add("matches")
				.add('{')
				.add(2)
				.add('}')
				.add(')')
				.compile();
```

Now lets create a list of elements to apply the regex to.

```java
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
```

The api is exactly the same as the standard Java regex classes, so this should look very familiar:

```java
		Matcher<String> matcher = pattern.matcher(input);
		
		while (matcher.find())
		{
			System.out.println(matcher.start());
			System.out.println(matcher.group());
			System.out.println(matcher.group(0));
			System.out.println(matcher.group(1));
		}
```

From this we get 2 matches, the pattern that is matched is ["not","matches","matches"] and is matched at indexes 0 and 5.

# Use with Maven

To use the package, you need to use following Maven dependency:

```xml
<dependency>
  <groupId>com.belteshazzar</groupId>
  <artifactId>regex</artifactId>
  <version>0.0.2</version>
</dependency>
```

## Non-Maven Download

For non-Maven use cases, you download jars from [Central Maven repository](http://repo1.maven.org/maven2/com/belteshazzar/regex/0.0.2/regex-0.0.2.jar).

## More Info

For more information, including some simple tutorials of how to use this package [check out the wiki](https://github.com/belteshazzar/regex/wiki).
