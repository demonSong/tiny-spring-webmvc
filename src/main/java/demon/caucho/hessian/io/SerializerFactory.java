package demon.caucho.hessian.io;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

import com.caucho.hessian.io.HessianProtocolException;

public class SerializerFactory extends AbstractSerializerFactory {

	private WeakReference<ClassLoader> _loaderRef;
	
	private ContextSerializerFactory _contextFactory;

	private ConcurrentHashMap _cachedSerializerMap;

	public SerializerFactory() {
		this(Thread.currentThread().getContextClassLoader());
	}

	public SerializerFactory(ClassLoader loader) {
		_loaderRef = new WeakReference<ClassLoader>(loader);
		
		_contextFactory =ContextSerializerFactory.create(loader);
	}

	public ClassLoader getClassLoader() {
		return _loaderRef.get();
	}

	@Override
	public Serializer getSerializer(Class cl) throws HessianProtocolException {
		Serializer serializer;
		if (_cachedSerializerMap != null) {
			serializer = (Serializer) _cachedSerializerMap.get(cl);

			if (serializer != null) {
				return serializer;
			}
		}

		serializer = loadSerializer(cl);

		if (_cachedSerializerMap == null)
			_cachedSerializerMap = new ConcurrentHashMap(8);

		_cachedSerializerMap.put(cl, serializer);

		return serializer;

	}

	protected Serializer loadSerializer(Class<?> cl)
			throws HessianProtocolException {
		Serializer serializer = null;
		serializer =_contextFactory.getSerializer(cl.getName());
		
		if(serializer !=null){
			return serializer;
		}
		return null;
	}

}
