package nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.converter.BeanModelConverter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

import com.extjs.gxt.ui.client.data.BeanModel;

/**
 * Test for a {@link BeanModelConverter}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class BeanModelConverterTest {

	/** {@link Converter} that will be tested. */
	private BeanModelConverter converter;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		converter = new BeanModelConverter();
	}

	@Test
	public void testConvertFieldValueObject() {
		class BeanModelTest extends BeanModel {

			/**
             * 
             */
			private static final long serialVersionUID = 3292292434901788352L;

			public BeanModelTest() {
				setBean("SALOMO");
			}
		}

		final BeanModel beanModel = new BeanModelTest();

		final Object value = converter.convertFieldValue(beanModel);

		assertNotNull(value);
		assertEquals("SALOMO", value.toString());
	}

	@Test
	public void testConvertModelValueObject() {
		// class Person implements BeanModelTag {
		//
		// private String name;
		//
		// /**
		// * @return the name
		// */
		// public String getName() {
		// return name;
		// }
		//
		// /**
		// * @param name
		// * the name to set
		// */
		// public void setName(String name) {
		// this.name = name;
		// }
		//
		// }
		// ;

		// TODO: Fix the test by using a GWT test.
		// Person person = new Person();
		//
		// Object modelValue = converter.convertModelValue(person);
		//
		// if (!(modelValue instanceof BeanModel)) {
		// fail("Invalid object created: " + modelValue);
		// }
	}

}
