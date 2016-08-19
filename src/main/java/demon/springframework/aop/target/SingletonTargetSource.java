package demon.springframework.aop.target;

import java.io.Serializable;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import demon.springframework.aop.TargetSource;

@SuppressWarnings("serial")
public class SingletonTargetSource implements TargetSource ,Serializable {
	
	private final Object target;
	
	public SingletonTargetSource(Object target) {
		Assert.notNull(target, "Target object must not be null");
		this.target = target;
	}

	@Override
	public Class<?> getTargetClass() {
		return this.target.getClass();
	}

	@Override
	public boolean isStatic() {
		return true;
	}

	@Override
	public Object getTarget() throws Exception {
		return this.target;
	}

	@Override
	public void releaseTarget(Object target) throws Exception {
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SingletonTargetSource)) {
			return false;
		}
		SingletonTargetSource otherTargetSource = (SingletonTargetSource) other;
		return this.target.equals(otherTargetSource.target);
	}

	@Override
	public int hashCode() {
		return this.target.hashCode();
	}

	@Override
	public String toString() {
		return "SingletonTargetSource for target object [" + ObjectUtils.identityToString(this.target) + "]";
	}

}
