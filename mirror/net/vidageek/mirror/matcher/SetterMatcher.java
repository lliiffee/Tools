package net.vidageek.mirror.matcher;

import static java.lang.reflect.Modifier.isAbstract;

import java.lang.reflect.Method;

import net.vidageek.mirror.list.dsl.Matcher;

final public class SetterMatcher implements Matcher<Method> {

	public boolean accepts(final Method element) {
		return element.getName().startsWith("set") && element.getParameterTypes().length == 1
				&& !element.isSynthetic() && !element.isBridge() && !isAbstract(element.getModifiers());
	}
}
