package demon.caucho.hessian.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.IdentityHashMap;

public class HessianOutput extends AbstractHessianOutput {

	protected OutputStream os;
	private IdentityHashMap _refs;
	private int _version = 1;

	public HessianOutput(OutputStream os) {
		init(os);
	}

	public HessianOutput() {
	}

	public void setVersion(int version) {
		_version = version;
	}

	public void init(OutputStream os) {
		this.os = os;

		_refs = null;

		if (_serializerFactory == null)
			_serializerFactory = new SerializerFactory();
	}

	public void call(String method, Object[] args) throws IOException {
		int length = args != null ? args.length : 0;
		startCall(method, length);

		for (int i = 0; i < length; i++)
			writeObject(args[i]);

		completeCall();
	}

	public void writeObject(Object object) throws IOException {
		if (object == null) {
			writeNull();
			return;
		}

		Serializer serializer;

		serializer = _serializerFactory.getSerializer(object.getClass());

		serializer.writeObject(object, this);
	}

	public void writeString(String value) throws IOException {
		if (value == null) {
			os.write('N');
		} else {
			int length = value.length();
			int offset = 0;

			while (length > 0x8000) {
				int sublen = 0x8000;

				// chunk can't end in high surrogate
				char tail = value.charAt(offset + sublen - 1);

				if (0xd800 <= tail && tail <= 0xdbff)
					sublen--;

				os.write('s');
				os.write(sublen >> 8);
				os.write(sublen);

				printString(value, offset, sublen);

				length -= sublen;
				offset += sublen;
			}

			os.write('S');
			os.write(length >> 8);
			os.write(length);

			printString(value, offset, length);
		}
	}

	public void writeNull() throws IOException {
		os.write('N');
	}

	public void completeCall() throws IOException {
		os.write('z');
	}

	public void printLenString(String v) throws IOException {
		if (v == null) {
			os.write(0);
			os.write(0);
		} else {
			int len = v.length();
			os.write(len >> 8);
			os.write(len);

			printString(v, 0, len);
		}
	}

	/**
	 * 开始进行编码,以备服务器所解析
	 */
	public void startCall(String method, int length) throws IOException {
		os.write('c');
		os.write(_version);
		os.write(0);

		os.write('m');
		int len = method.length();
		os.write(len >> 8);
		os.write(len);
		printString(method, 0, len);
	}

	public void printString(String v, int offset, int length)
			throws IOException {
		for (int i = 0; i < length; i++) {
			char ch = v.charAt(i + offset);

			if (ch < 0x80) {
				os.write(ch);
			} else if (ch < 0x800) {
				os.write(0xc0 + ((ch >> 6) & 0x1f));
				os.write(0x80 + (ch & 0x3f));
			} else {
				os.write(0xe0 + ((ch >> 12) & 0xf));
				os.write(0x80 + ((ch >> 6) & 0x3f));
				os.write(0x80 + (ch & 0x3f));
			}
		}
	}

	public void printString(char[] v, int offset, int length)
			throws IOException {
		for (int i = 0; i < length; i++) {
			char ch = v[i + offset];

			if (ch < 0x80) {
				os.write(ch);
			} else if (ch < 0x800) {
				os.write(0xc0 + ((ch >> 6) & 0x1f));
				os.write(0x80 + (ch & 0x3f));
			} else {
				os.write(0xe0 + ((ch >> 12) & 0xf));
				os.write(0x80 + ((ch >> 6) & 0x3f));
				os.write(0x80 + (ch & 0x3f));
			}
		}
	}

	public void flush() throws IOException {
		if (this.os != null)
			this.os.flush();
	}

	public void close() throws IOException {
		if (this.os != null)
			this.os.flush();
	}
}
