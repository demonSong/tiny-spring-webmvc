package demon.springframework.aop.target;

import java.io.Serializable;

import org.springframework.util.ObjectUtils;

import demon.springframework.aop.TargetSource;

@SuppressWarnings("serial")
public class EmptyTargetSource implements TargetSource, Serializable{
	
	public static final EmptyTargetSource INSTANCE = new EmptyTargetSource(null, true);
	
	public static EmptyTargetSource forClass(Class<?> targetClass) {
		return forClass(targetClass, true);
	}

	public static EmptyTargetSource forClass(Class<?> targetClass, boolean isStatic) {
		return (targetClass == null && isStatic ? INSTANCE : new EmptyTargetSource(targetClass, isStatic));
	}
	
	private final Class<?> targetClass;

	private final boolean isStatic;

	private EmptyTargetSource(Class<?> targetClass, boolean isStatic) {
		this.targetClass = targetClass;
		this.isStatic = isStatic;
	}
	
	@Override
	public Class<?> getTargetClass() {
		return this.targetClass;
	}

	/**
	 * Always returns {@code true}.
	 */
	@Override
	public boolean isStatic() {
		return this.isStatic;
	}

	/**
	 * Always returns {@code null}.
	 */
	@Override
	public Object getTarget() {
		return null;
	}

	/**
	 * Nothing to release.
	 */
	@Override
	public void releaseTarget(Object target) {
	}


	/**
	 * Returns the canonical instance on deserialization in case
	 * of no target class, thus protecting the Singleton pattern.
	 */
	private Object readResolve() {
		return (this.targetClass == null && this.isStatic ? INSTANCE : this);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EmptyTargetSource)) {
			return false;
		}
		EmptyTargetSource otherTs = (EmptyTargetSource) other;
		return (ObjectUtils.nullSafeEquals(this.targetClass, otherTs.targetClass) && this.isStatic == otherTs.isStatic);
	}

	@Override
	public int hashCode() {
		return EmptyTargetSource.class.hashCode() * 13 + ObjectUtils.nullSafeHashCode(this.targetClass);
	}

	@Override
	public String toString() {
		return "EmptyTargetSource: " +
				(this.targetClass != null ? "target class [" + this.targetClass.getName() + "]" : "no target class") +
				", " + (this.isStatic ? "static" : "dynamic");
	}

}
