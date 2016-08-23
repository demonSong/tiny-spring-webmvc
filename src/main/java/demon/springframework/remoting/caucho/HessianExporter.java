package demon.springframework.remoting.caucho;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.util.Assert;

import com.caucho.hessian.io.Hessian2Input;

import demon.caucho.hessian.io.AbstractHessianInput;
import demon.caucho.hessian.io.AbstractHessianOutput;
import demon.caucho.hessian.io.Hessian2Output;
import demon.caucho.hessian.io.HessianInput;
import demon.caucho.hessian.io.HessianOutput;
import demon.caucho.hessian.io.SerializerFactory;
import demon.caucho.hessian.server.HessianSkeleton;
import demon.springframework.beans.factory.InitializingBean;
import demon.springframework.remoting.support.RemoteExporter;

public class HessianExporter extends RemoteExporter implements InitializingBean{
	
	public static final String CONTENT_TYPE_HESSIAN = "application/x-hessian";
	
	private SerializerFactory serializerFactory = new SerializerFactory();
	
	private HessianSkeleton skeleton;
	
	public void setSerializerFactory(SerializerFactory serializerFactory) {
		this.serializerFactory = (serializerFactory != null ? serializerFactory : new SerializerFactory());
	}

	@Override
	public void afterPropertiesSet() {
		prepare();
	}
	
	public void prepare(){
		checkService();
		checkServiceInterface();
		this.skeleton = new HessianSkeleton(getProxyForService(),getServiceInterface());
	}
	
	public void invoke(InputStream inputStream, OutputStream outputStream) throws Throwable{
		Assert.notNull(this.skeleton, "Hessian exporter has not been initialized");
		doInvoke(this.skeleton, inputStream, outputStream);
	}
	
	protected void doInvoke(HessianSkeleton skeleton, InputStream inputStream, OutputStream outputStream)
			throws Throwable {
		ClassLoader originalClassLoader = overrideThreadContextClassLoader();
		try {
			InputStream isToUse = inputStream;
			OutputStream osToUse = outputStream;
			
			if(!isToUse.markSupported()){
				isToUse = new BufferedInputStream(isToUse);
				isToUse.mark(1);
			}
			
			int code = isToUse.read();
			int major;
			int minor;
			
			AbstractHessianInput in;
			AbstractHessianOutput out;
			
			if (code == 'c') {
				// Hessian 1.0 call
				major = isToUse.read();
				minor = isToUse.read();
				
				in = new HessianInput(isToUse);
				if(major > 2){
					out = new Hessian2Output(osToUse);
				}
				else {
					out = new HessianOutput(osToUse);
				}
			}
			else {
				throw new IOException("Expected 'H'/'C' (Hessian 2.0) or 'c' (Hessian 1.0) in hessian input at " + code);
			}
			
			if (this.serializerFactory != null) {
				in.setSerializerFactory(this.serializerFactory);
				out.setSerializerFactory(this.serializerFactory);
			}
			try {
				skeleton.invoke(in,out);
			}
			finally{
				try {
					in.close();
					isToUse.close();
				}
				catch (IOException ex) {
					// ignore
				}
				try {
					out.close();
					osToUse.close();
				}
				catch (IOException ex) {
					// ignore
				}
			}
		}
		finally{
			resetThreadContextClassLoader(originalClassLoader);
		}
		
	}

}
