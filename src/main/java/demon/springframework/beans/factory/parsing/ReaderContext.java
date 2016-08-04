package demon.springframework.beans.factory.parsing;

import demon.springframework.beans.io.Resource;

public class ReaderContext {
	
	private final Resource resource;

	public ReaderContext(Resource resource){
		this.resource =resource;
	}
	
	public Resource getResource() {
		return resource;
	}
}
