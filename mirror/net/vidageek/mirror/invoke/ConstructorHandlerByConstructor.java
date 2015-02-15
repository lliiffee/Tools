/**
 * 
 */
package net.vidageek.mirror.invoke;

import java.lang.reflect.Constructor;

import net.vidageek.mirror.Preconditions;
import net.vidageek.mirror.invoke.dsl.ConstructorHandler;
import net.vidageek.mirror.provider.ConstructorBypassingReflectionProvider;
import net.vidageek.mirror.provider.ConstructorReflectionProvider;
import net.vidageek.mirror.provider.ReflectionProvider;

/**
 * This class is responsible for invoking a constructor provided.
 * 
 * @author jonasabreu
 */
public final class ConstructorHandlerByConstructor<T> implements ConstructorHandler<T> {

	private final Constructor<T> constructor;

	private final Class<T> clazz;

	private final ReflectionProvider provider;

	public ConstructorHandlerByConstructor(final ReflectionProvider provider, final Class<T> clazz,
			final Constructor<T> con) {
		Preconditions.checkArgument(clazz != null, "clazz cannot be null");
		Preconditions.checkArgument(con!= null, "constructor cannot be null");
		Preconditions.checkArgument(clazz.equals(con.getDeclaringClass()), "Constructor declaring type should be %s but was %s", 
				clazz.getName(), con.getDeclaringClass().getName());
		
		this.provider = provider;
		this.clazz = clazz;
		this.constructor = con;
	}

	public T withArgs(final Object... args) {
		ConstructorReflectionProvider<T> constructorReflectionProvider = provider
				.getConstructorReflectionProvider(clazz, constructor);
		constructorReflectionProvider.setAccessible();
		return constructorReflectionProvider.instantiate(args);
	}

	public T withoutArgs() {
		return withArgs(new Object[0]);
	}

	public T bypasser() {
		ConstructorBypassingReflectionProvider<T> bypassingProvider = provider
				.getConstructorBypassingReflectionProvider(clazz);
		return bypassingProvider.bypassConstructor();
	}

}
