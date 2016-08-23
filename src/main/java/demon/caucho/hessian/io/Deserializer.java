package demon.caucho.hessian.io;

import java.io.IOException;

public interface Deserializer {
	
	public Object readObject(AbstractHessianInput in) throws IOException;

}
