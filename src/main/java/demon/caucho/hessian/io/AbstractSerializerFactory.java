package demon.caucho.hessian.io;

import com.caucho.hessian.io.HessianProtocolException;

public abstract class AbstractSerializerFactory {
	
	public abstract Serializer getSerializer(Class cl) throws HessianProtocolException;
	
}
