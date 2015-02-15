package net.vidageek.mirror.reflect;

import java.lang.annotation.Annotation;

import net.vidageek.mirror.Preconditions;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.provider.ReflectionProvider;
import net.vidageek.mirror.reflect.dsl.AnnotationHandler;
import net.vidageek.mirror.reflect.dsl.MethodAnnotationHandler;

/**
 * This class is responsible for choosing where to reflect a single annotation.
 * 
 * @author dnfeitosa
 */
public final class DefaultAnnotationHandler<T extends Annotation> implements AnnotationHandler<T> {

	private final Class<?> clazz;

	private final Class<T> annotation;

	private final ReflectionProvider provider;

	public DefaultAnnotationHandler(final ReflectionProvider provider, final Class<?> clazz, final Class<T> annotation) {
		Preconditions.checkArgument(clazz != null, "clazz cannot be null");
		Preconditions.checkArgument(annotation != null, "annotation cannot be null");
		this.provider = provider;
		this.clazz = clazz;
		this.annotation = annotation;
	}

	public T atField(final String fieldName) {
		Preconditions.checkArgument(fieldName != null && fieldName.trim().length() > 0, "fieldName cannot be null or empty");
		return provider
				.getAnnotatedElementReflectionProvider(new Mirror(provider).on(clazz).reflect().field(fieldName))
				.getAnnotation(annotation);
	}

	@SuppressWarnings("unchecked")
	public MethodAnnotationHandler<T> atMethod(final String methodName) {
		Preconditions.checkArgument(methodName != null && methodName.trim().length() > 0, "methodName cannot be null or empty");
		return new DefaultMethodAnnotationHandler(provider, clazz, methodName, annotation);
	}

	public T atClass() {
		return provider.getAnnotatedElementReflectionProvider(clazz).getAnnotation(annotation);
	}

}
