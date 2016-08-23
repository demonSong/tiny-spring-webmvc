package demon.caucho.hessian.io;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractHessianInput {

	public void init(InputStream is) {
	}

	public void setSerializerFactory(SerializerFactory ser) {
	}

	public abstract String readMethod() throws IOException;

	public abstract Object readObject(Class expectedClass) throws IOException;

	public abstract void completeCall() throws IOException;

	public abstract String readString() throws IOException;

	public void close() throws IOException {
	}

	public int readMethodArgLength() throws IOException {
		return -1;
	}

}
