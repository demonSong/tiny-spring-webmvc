package demon.springframework.remoting.support;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.ClassUtils;

@SuppressWarnings("serial")
public class RemoteInvocation implements Serializable{

	private String methodName;

	private Class<?>[] parameterTypes;

	private Object[] arguments;

	private Map<String, Serializable> attributes;


	/**
	 * Create a new RemoteInvocation for the given AOP method invocation.
	 * @param methodInvocation the AOP invocation to convert
	 */
	public RemoteInvocation(MethodInvocation methodInvocation) {
		this.methodName = methodInvocation.getMethod().getName();
		this.parameterTypes = methodInvocation.getMethod().getParameterTypes();
		this.arguments = methodInvocation.getArguments();
	}

	/**
	 * Create a new RemoteInvocation for the given parameters.
	 * @param methodName the name of the method to invoke
	 * @param parameterTypes the parameter types of the method
	 * @param arguments the arguments for the invocation
	 */
	public RemoteInvocation(String methodName, Class<?>[] parameterTypes, Object[] arguments) {
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.arguments = arguments;
	}

	/**
	 * Create a new RemoteInvocation for JavaBean-style deserialization
	 * (e.g. with Jackson).
	 */
	public RemoteInvocation() {
	}


	/**
	 * Set the name of the target method.
	 * <p>This setter is intended for JavaBean-style deserialization.
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * Return the name of the target method.
	 */
	public String getMethodName() {
		return this.methodName;
	}

	/**
	 * Set the parameter types of the target method.
	 * <p>This setter is intended for JavaBean-style deserialization.
	 */
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	/**
	 * Return the parameter types of the target method.
	 */
	public Class<?>[] getParameterTypes() {
		return this.parameterTypes;
	}

	/**
	 * Set the arguments for the target method call.
	 * <p>This setter is intended for JavaBean-style deserialization.
	 */
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	/**
	 * Return the arguments for the target method call.
	 */
	public Object[] getArguments() {
		return this.arguments;
	}


	/**
	 * Add an additional invocation attribute. Useful to add additional
	 * invocation context without having to subclass RemoteInvocation.
	 * <p>Attribute keys have to be unique, and no overriding of existing
	 * attributes is allowed.
	 * <p>The implementation avoids to unnecessarily create the attributes
	 * Map, to minimize serialization size.
	 * @param key the attribute key
	 * @param value the attribute value
	 * @throws IllegalStateException if the key is already bound
	 */
	public void addAttribute(String key, Serializable value) throws IllegalStateException {
		if (this.attributes == null) {
			this.attributes = new HashMap<String, Serializable>();
		}
		if (this.attributes.containsKey(key)) {
			throw new IllegalStateException("There is already an attribute with key '" + key + "' bound");
		}
		this.attributes.put(key, value);
	}

	/**
	 * Retrieve the attribute for the given key, if any.
	 * <p>The implementation avoids to unnecessarily create the attributes
	 * Map, to minimize serialization size.
	 * @param key the attribute key
	 * @return the attribute value, or {@code null} if not defined
	 */
	public Serializable getAttribute(String key) {
		if (this.attributes == null) {
			return null;
		}
		return this.attributes.get(key);
	}

	/**
	 * Set the attributes Map. Only here for special purposes:
	 * Preferably, use {@link #addAttribute} and {@link #getAttribute}.
	 * @param attributes the attributes Map
	 * @see #addAttribute
	 * @see #getAttribute
	 */
	public void setAttributes(Map<String, Serializable> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Return the attributes Map. Mainly here for debugging purposes:
	 * Preferably, use {@link #addAttribute} and {@link #getAttribute}.
	 * @return the attributes Map, or {@code null} if none created
	 * @see #addAttribute
	 * @see #getAttribute
	 */
	public Map<String, Serializable> getAttributes() {
		return this.attributes;
	}


	/**
	 * Perform this invocation on the given target object.
	 * Typically called when a RemoteInvocation is received on the server.
	 * @param targetObject the target object to apply the invocation to
	 * @return the invocation result
	 * @throws NoSuchMethodException if the method name could not be resolved
	 * @throws IllegalAccessException if the method could not be accessed
	 * @throws InvocationTargetException if the method invocation resulted in an exception
	 * @see java.lang.reflect.Method#invoke
	 */
	public Object invoke(Object targetObject)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		Method method = targetObject.getClass().getMethod(this.methodName, this.parameterTypes);
		return method.invoke(targetObject, this.arguments);
	}


	@Override
	public String toString() {
		return "RemoteInvocation: method name '" + this.methodName + "'; parameter types " +
				ClassUtils.classNamesToString(this.parameterTypes);
	}	
	
}
