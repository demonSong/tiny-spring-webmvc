package demon.springframework.beans;


public interface TestPropertyValues {

	TestPropertyValue[] getPropertyValues();

	TestPropertyValue getPropertyValue(String propertyName);

	TestPropertyValues changesSince(TestPropertyValues old);

	boolean contains(String propertyName);

	boolean isEmpty();
}
