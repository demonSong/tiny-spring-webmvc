package demon.caucho.hessian.io;

import java.io.IOException;

public interface Serializer {
	
	public void writeObject(Object obj ,AbstractHessianOutput out) throws IOException;

}
