package demon.caucho.hessian.io;

import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractHessianOutput {

	private SerializerFactory _defaultSerializerFactory;

	protected SerializerFactory _serializerFactory;

	public void setSerializerFactory(SerializerFactory factory) {
		_serializerFactory = factory;
	}

	public SerializerFactory getSerializerFactory() {
		// the default serializer factory cannot be modified by external
		// callers
		// 保护性拷贝.serial 和 default所指向的factory应不同,即defaultFactory不能被改变
		if (_serializerFactory == _defaultSerializerFactory) {
			_serializerFactory = new SerializerFactory();
		}

		return _serializerFactory;
	}

	public void init(OutputStream os) {
	}

	public void call(String method, Object[] args) throws IOException {
		int length = args != null ? args.length : 0;

		startCall(method, length);

		for (int i = 0; i < length; i++)
			writeObject(args[i]);

		completeCall();
	}

	public void flush() throws IOException {
	}

	public void close() throws IOException {
	}

	public abstract void startCall(String method, int length)
			throws IOException;

	public abstract void writeObject(Object object) throws IOException;

	public abstract void completeCall() throws IOException;

	public abstract void writeNull() throws IOException;

	public abstract void writeString(String value) throws IOException;

}
