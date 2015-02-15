package net.vidageek.mirror.invoke;

import java.lang.reflect.Method;

import net.vidageek.mirror.Preconditions;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.exception.MirrorException;
import net.vidageek.mirror.invoke.dsl.MethodHandler;
import net.vidageek.mirror.provider.ReflectionProvider;

/**
 * Class responsible for invoking methods using is name and arguments.
 * 
 * @author jonasabreu
 */
public final class MethodHandlerByName implements MethodHandler {

	private final Object target;

	private final String methodName;

	private final Class<?> clazz;

	private final ReflectionProvider provider;

	public MethodHandlerByName(final ReflectionProvider provider, final Object target, final Class<?> clazz,
			final String methodName) {
		Preconditions.checkArgument(clazz != null, "clazz cannot be null");
		Preconditions.checkArgument(methodName != null && methodName.trim().length() > 0, "methodName cannot be null or empty");

		this.provider = provider;
		this.target = target;
		this.clazz = clazz;
		this.methodName = methodName;
	}

	public Object withoutArgs() {
		return withArgs(new Object[0]);
	}

	public Object withArgs(final Object... args) {
		return new MethodHandlerByMethod(provider, target, clazz, getMethod(args)).withArgs(args);
	}

	private Method getMethod(final Object[] args) {
		int length = args == null ? 0 : args.length;

		Class<?>[] classes = new Class<?>[length];
		for (int i = 0; i < length; i++) {
			Preconditions.checkArgument(args[i] != null, "Cannot invoke a method by name if one of it's arguments is null. First reflect the method.");
			classes[i] = args[i].getClass();
		}

		Method method = new Mirror(provider).on(clazz).reflect().method(methodName).withArgs(classes);
		if (method == null) {
			throw new MirrorException("Could not find method " + methodName + " on class " + clazz.getName());
		}
		return method;

	}

}
