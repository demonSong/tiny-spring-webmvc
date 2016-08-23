package demon.caucho.hessian.io;

import java.io.IOException;
import java.io.OutputStream;

public class Hessian2Output extends AbstractHessianOutput {

	protected OutputStream _os;

	public Hessian2Output() {
	}

	public Hessian2Output(OutputStream os) {
		init(os);
	}

	@Override
	public void init(OutputStream os) {
		_os = os;
	}

	@Override
	public void startCall(String method, int length) throws IOException {
	}

	@Override
	public void writeObject(Object object) throws IOException {
	}

	@Override
	public void completeCall() throws IOException {
	}

	@Override
	public void writeNull() throws IOException {
	}

	@Override
	public void writeString(String value) throws IOException {
	}

}
