package demon.caucho.hessian.io;

import java.io.IOException;

public class BasicDeserializer implements Deserializer {

	public static final int NULL = BasicSerializer.NULL;
	public static final int BOOLEAN = BasicSerializer.BOOLEAN;
	public static final int BYTE = BasicSerializer.BYTE;
	public static final int SHORT = BasicSerializer.SHORT;
	public static final int INTEGER = BasicSerializer.INTEGER;
	public static final int LONG = BasicSerializer.LONG;
	public static final int FLOAT = BasicSerializer.FLOAT;
	public static final int DOUBLE = BasicSerializer.DOUBLE;
	public static final int CHARACTER = BasicSerializer.CHARACTER;
	public static final int CHARACTER_OBJECT = BasicSerializer.CHARACTER_OBJECT;
	public static final int STRING = BasicSerializer.STRING;

	private int _code;

	public BasicDeserializer(int code) {
		_code = code;
	}

	public Class getType() {
		switch (_code) {
		case NULL:
			return void.class;
		case BOOLEAN:
			return Boolean.class;
		case BYTE:
			return Byte.class;
		case SHORT:
			return Short.class;
		case INTEGER:
			return Integer.class;
		case LONG:
			return Long.class;
		case FLOAT:
			return Float.class;
		case DOUBLE:
			return Double.class;
		case CHARACTER:
			return Character.class;
		case CHARACTER_OBJECT:
			return Character.class;
		case STRING:
			return String.class;
		default:
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public Object readObject(AbstractHessianInput in) throws IOException {
		switch (_code) {
		case NULL:
			return null;
		case STRING:
			return in.readString();
		default:
			throw new UnsupportedOperationException();
		}
	}

}
