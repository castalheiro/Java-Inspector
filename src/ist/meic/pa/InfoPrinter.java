package ist.meic.pa;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This class is responsible for printing object information
 */

public class InfoPrinter {

	/**
	 * 
	 * If the object class is a primitive type, only the name of the object is
	 * printed, otherwise several other kinds of information are printed
	 * 
	 * @param inspectedObject
	 *            - the object with information to print
	 */
	public static void printObjectInfo(InspectedObject inspectedObject) {
		System.err.println(inspectedObject.getObject() + " is an instance of "
				+ inspectedObject.getName());

		if (!inspectedObject.isPrimitive() && !inspectedObject.isNull())
			printStructureInfo(inspectedObject.getObject());
	}

	/**
	 * Prints object information about annotations, constructors, implemented
	 * interfaces, methods, superclasses
	 * 
	 * @param object
	 *            - the object with information to print
	 */
	public static void printStructureInfo(Object object) {

		try {

			System.err.println("----------");
			System.err.println("Attributes:");

			printFieldsInfo(object);

			System.err.println("----------");
			printAnnotationsInfo(object.getClass().getAnnotations());
			printConstructorsInfo(object.getClass().getConstructors());
			printInterfacesInfo(object.getClass().getInterfaces());
			printMethodsInfo(object.getClass().getDeclaredMethods());
			printSuperClassesInfo(object);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Prints object information about fields
	 * 
	 * @param object
	 *            - the object with information to print
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */

	private static void printFieldsInfo(Object object)
			throws IllegalArgumentException, IllegalAccessException,
			SecurityException, NoSuchMethodException, InvocationTargetException {

		Class<?> actualClass = object.getClass();

		while (actualClass != Object.class) {

			for (Field field : actualClass.getDeclaredFields()) {

				// don't print static variables
				if (Modifier.isStatic(field.getModifiers())) {
					continue;
				}

				Boolean fieldAccess = field.isAccessible();
				field.setAccessible(true);
				Object fieldObj = field.get(object);
				field.setAccessible(fieldAccess);

				System.err.print(field.toString());

				if (fieldObj != null && fieldObj.getClass().isArray()) {

					System.err.print(" = [ ");

					for (int i = 0; i < Array.getLength(fieldObj); i++) {
						System.err.print(Array.get(fieldObj, i) + " ");
					}
					System.err.println("];");

				} else {
					System.err.println(" = " + fieldObj + ";");
				}

			}

			actualClass = actualClass.getSuperclass();

		}

	}

	/**
	 * Prints object information about annotations
	 * 
	 * @param annotations
	 *            - list of object annotations to print
	 */
	private static void printAnnotationsInfo(Annotation[] annotations) {
		System.err.print("Annotations: ");

		if (annotations.length < 1) {
			System.err.print("there are no annotations.");
		}

		for (Annotation annotation : annotations) {
			System.err.print(annotation.toString() + "; ");
		}

		System.err.println();
	}

	/**
	 * Prints object information about constructors
	 * 
	 * @param constructors
	 *            - list of object constructors to print
	 */

	private static void printConstructorsInfo(Constructor<?>[] constructors) {
		System.err.print("Constructors: ");

		if (constructors.length == 0) {
			System.err.print("there are no constructors.");
		}

		for (Constructor<?> constructor : constructors) {
			System.err.print(constructor.toString() + "; ");
		}

		System.err.println();
	}

	/**
	 * Prints object information about implemented interfaces
	 * 
	 * @param interfaces
	 *            - list of interfaces to print
	 */

	private static void printInterfacesInfo(Class<?>[] interfaces) {
		System.err.print("Interfaces: ");

		if (interfaces.length == 0) {
			System.err.print("there are no interfaces.");
		}

		for (Class<?> interf : interfaces) {
			System.err.print(interf.toString() + "; ");
		}

		System.err.println();
	}

	/**
	 * Prints object information about methods
	 * 
	 * @param methods
	 *            - list of object methods
	 */

	private static void printMethodsInfo(Method[] methods) {
		System.err.print("Methods: ");

		if (methods.length == 0) {
			System.err.print("there are no interfaces.");
		}

		for (Method m : methods) {
			System.err.print(m.toString() + "; ");
		}

		System.err.println();
	}

	/**
	 * Prints object information about superclasses
	 * 
	 * @param object
	 */

	private static void printSuperClassesInfo(Object object) {
		Class<?> actualClass = object.getClass().getSuperclass();

		System.err.print("Superclasses: ");

		while (actualClass != Object.class) {
			System.err.print(actualClass.getName() + " ");
			actualClass = actualClass.getSuperclass();
		}

		System.err.println(actualClass);
	}

	/**
	 * Message to be printed when object doesn't exist
	 * 
	 * @param s
	 *            - object name
	 */

	public static void printNullInfo(String s) {
		System.err.println(s + ": the object invoked does not exist");
	}

	/**
	 * Message to be printed when a command can't be executed
	 */

	public static void printNothingToDo() {
		System.err
				.println("Nothing to do here... object is null or has a primitive type or number of arguments is wrong");

	}

	public static void printNoAssignMessage(String objectName, String argName,
			String argType) {
		System.err.println(objectName + " field of type " + argType
				+ " couldn't be assigned with value " + argName);
	}
	
	public static void printCommandNotFound(String command) {
		System.err
				.println("Command " + command + " not found");

	}

}
