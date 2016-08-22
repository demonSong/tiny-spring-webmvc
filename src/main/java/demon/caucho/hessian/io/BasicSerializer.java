package demon.caucho.hessian.io;

import java.io.IOException;

public class BasicSerializer implements Serializer {

	public static final int NULL = 0;
	public static final int BOOLEAN = NULL + 1;
	public static final int BYTE = BOOLEAN + 1;
	public static final int SHORT = BYTE + 1;
	public static final int INTEGER = SHORT + 1;
	public static final int LONG = INTEGER + 1;
	public static final int FLOAT = LONG + 1;
	public static final int DOUBLE = FLOAT + 1;
	public static final int CHARACTER = DOUBLE + 1;
	public static final int CHARACTER_OBJECT = CHARACTER + 1;
	public static final int STRING = CHARACTER_OBJECT + 1;
	
	private int _code;
	
	public BasicSerializer(int code) {
		_code = code;
	}

	@Override
	public void writeObject(Object obj, AbstractHessianOutput out)
			throws IOException {
		switch (_code) {
		case STRING:
		      out.writeString((String) obj);
		      break;
		default:
		      throw new RuntimeException(_code + " unknown code for " + obj.getClass());
		}
	}

}
