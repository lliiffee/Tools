package net.vidageek.mirror.dsl;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.vidageek.mirror.DefaultAccessorsController;
import net.vidageek.mirror.DefaultClassController;
import net.vidageek.mirror.DefaultFieldController;
import net.vidageek.mirror.DefaultMemberController;
import net.vidageek.mirror.DefaultProxyHandler;
import net.vidageek.mirror.Preconditions;
import net.vidageek.mirror.config.MirrorProviderBuilder;
import net.vidageek.mirror.exception.MirrorException;
import net.vidageek.mirror.provider.ReflectionProvider;
import net.vidageek.mirror.proxy.dsl.ProxyHandler;

/**
 * This is the basic class to use Mirror. All Reflection features can be
 * accessed using this class.
 * 
 * It will load configuration file [mirror.properties]. Full Mirror
 * configuration is found at
 * http://projetos.vidageek.net/mirror/extension/configuration/ *
 * 
 * @author jonasabreu
 */
public final class Mirror {

	private static final String MIRROR_CFG = "/mirror.properties";

	private static final ReflectionProvider cachedProvider;

	private final ReflectionProvider provider;

	static {
		cachedProvider = new MirrorProviderBuilder(Mirror.class.getResourceAsStream(MIRROR_CFG)).createProvider();
	}

	public Mirror(final ReflectionProvider provider) {
		this.provider = provider;
	}

	public Mirror() {
		this(Mirror.cachedProvider);
	}

	/**
	 * This method will reflect a Class object that is represented by className.
	 * 
	 * @param className
	 *            Full qualified name of the class you want to reflect.
	 * @return A Class object represented by className.
	 * @throws MirrorException
	 *             if no class on current ClassLoader has class represented by
	 *             className.
	 * @throws IllegalArgumentException
	 *             if className is null or empty.
	 */
	public Class<?> reflectClass(final String className) {
		Preconditions.checkArgument(className != null && className.trim().length() > 0, "className cannot be null or empty");
		return provider.getClassReflectionProvider(className).reflectClass();
	}

	/**
	 * Method to access reflection on clazz.
	 * 
	 * @param clazz
	 *            Class object to be used.
	 * @return A object that allows you to access reflection on class clazz.
	 * @throws IllegalArgumentException
	 *             if clazz is null.
	 */
	public <T> ClassController<T> on(final Class<T> clazz) {
		return new DefaultClassController<T>(provider, clazz);
	}

	/**
	 * Method to access reflection on object.
	 * 
	 * @param object
	 *            Object to be used.
	 * @return A object that allows you to access reflection on object.
	 * @throws IllegalArgumentException
	 *             if object is null.
	 */
	public AccessorsController on(final Object object) {
		return new DefaultAccessorsController(provider, object);
	}

	/**
	 * Convenience method for {@link Mirror#on(Class)}.
	 * 
	 * @see Mirror#on(Class)
	 * @see Mirror#reflectClass(String)
	 */
	public ClassController<?> on(final String className) {
		return on(reflectClass(className));
	}

	/**
	 * Method to access reflection on any AnnotatedElement
	 * 
	 * @see AccessibleObject
	 * @param object
	 *            AccessibleObject to be used.
	 * @return An object that allows you to access reflection on an
	 *         AccessibleObject.
	 */
	public MemberController on(final AnnotatedElement member) {
		return new DefaultMemberController(provider, member);
	}

	/**
	 * Method to access reflection on any Field.
	 * 
	 * @param field
	 *            to be used
	 * @return An object that allows you to access reflection on a Field.
	 * @throws IllegalArgumentException
	 *             if field is null.
	 */
	public FieldController on(final Field field) {
		return new DefaultFieldController(provider, field);
	}

	/**
	 * Convenience method for {@link Mirror#proxify(Class...)}
	 * 
	 * @see {@link Mirror#on(Class)}
	 * @see {@link Mirror#proxify(Class...)}
	 */
	@SuppressWarnings("unchecked")
	public <T> ProxyHandler<T> proxify(final Class<T> clazz) {
		return (ProxyHandler<T>) proxify(new Class[] { clazz });
	}

	/**
	 * Convenience method for {@link Mirror#proxify(Class...)}
	 * 
	 * @see {@link Mirror#on(Class)}
	 * @see {@link Mirror#proxify(Class...)}
	 */
	public ProxyHandler<Object> proxify(final String className) {
		return proxify(new String[] { className });
	}

	/**
	 * Convenience method for {@link Mirror#proxify(Class...)}
	 * 
	 * @see {@link Mirror#on(Class)}
	 * @see {@link Mirror#proxify(Class...)}
	 * @see {@link Mirror#reflectClass(String)}
	 */
	public ProxyHandler<Object> proxify(final String... classNames) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (String className : classNames) {
			classes.add(reflectClass(className));
		}
		return proxify(classes.toArray(new Class<?>[classNames.length]));
	}

	/**
	 * Method to create proxys of classes. There can be only one actual class.
	 * All the remaining "classes" must be interfaces.
	 * 
	 * @param classes
	 *            to be proxified
	 * 
	 * @return An object responsible for setting method interceptors
	 * @throws IllegalArgumentException
	 *             if any class is null or there is more than one class that is
	 *             not an interface
	 */
	public ProxyHandler<Object> proxify(final Class<?>... classes) {
		return new DefaultProxyHandler(provider, classes);
	}
}
