package demon.caucho.hessian.io;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.WeakHashMap;

public class ContextSerializerFactory {

	private static final WeakHashMap<ClassLoader, SoftReference<ContextSerializerFactory>> _contextRefMap = new WeakHashMap<ClassLoader, SoftReference<ContextSerializerFactory>>();

	// 链表,天啊噜,原因在于loader有它自己的链表
	private ContextSerializerFactory _parent;
	private WeakReference<ClassLoader> _loaderRef;

	private static final ClassLoader _systemClassLoader;

	private static HashMap<String, Serializer> _staticSerializerMap;
	private final HashMap<String, Serializer> _serializerClassMap = new HashMap<String, Serializer>();

	static {
		_staticSerializerMap = new HashMap<String, Serializer>();
		addBasic(String.class, "string", BasicSerializer.STRING);

		ClassLoader systemClassLoader = null;
		try {
			systemClassLoader = ClassLoader.getSystemClassLoader();
		} catch (Exception e) {
		}

		_systemClassLoader = systemClassLoader;
	}

	public ContextSerializerFactory(ContextSerializerFactory parent,
			ClassLoader loader) {
		if (loader == null)
			loader = _systemClassLoader;

		_loaderRef = new WeakReference<ClassLoader>(loader);

		init();
	}

	private void init() {
		if (_parent != null) {
			_serializerClassMap.putAll(_parent._serializerClassMap);
		}

		if (_parent == null) {
			_serializerClassMap.putAll(_staticSerializerMap);
		}
	}

	public static ContextSerializerFactory create() {
		return create(Thread.currentThread().getContextClassLoader());
	}

	public static ContextSerializerFactory create(ClassLoader loader) {
		synchronized (_contextRefMap) {
			SoftReference<ContextSerializerFactory> factoryRef = _contextRefMap
					.get(loader);

			ContextSerializerFactory factory = null;

			if (factoryRef != null)
				factory = factoryRef.get();

			if (factory == null) {
				ContextSerializerFactory parent = null;

				// 递归调用，为何需要这样的父节点和子节点之分

				// AppClassLoader ---> ExtClassLoader ---> null
				if (loader != null) {
					parent = create(loader.getParent());
				}

				// systemClassLoader ---> extClassLoader ---> appClassLoader
				factory = new ContextSerializerFactory(parent, loader);
				factoryRef = new SoftReference<ContextSerializerFactory>(
						factory);

				_contextRefMap.put(loader, factoryRef);
			}

			return factory;
		}
	}

	private static void addBasic(Class cl, String typeName, int type) {
		_staticSerializerMap.put(cl.getName(), new BasicSerializer(type));
	}

	public Serializer getSerializer(String className) {
		Serializer serializer = _serializerClassMap.get(className);
		if (serializer == null) {
			return null;
		} else {
			return serializer;
		}
	}
}
