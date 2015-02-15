package net.vidageek.mirror.invoke.dsl;

/**
 * Interface for classes responsible for invoking constructors.
 * 
 * @author jonasabreu
 */
public interface ConstructorHandler<T> {

	/**
	 * Invoke a constructor without arguments.
	 * 
	 * @return An instance of object this constructor can create.
	 */
	public T withoutArgs();

	/**
	 * Invoke a constructor using args as its arguments.
	 * 
	 * @param args
	 *            arguments to use when invoking this constructor.
	 * @return An instance of object this constructor can create.
	 */
	public T withArgs(final Object... args);

	/**
	 * Instantiates an object without using the constructor. This is only
	 * supported for Objenesis supported VM. Please check
	 * http://code.google.com/p/objenesis/wiki/ListOfCurrentlySupportedVMs to
	 * see if your VM is supported.
	 * 
	 * @return An instance created without invoking any constructor.
	 */
	public T bypasser();

}
