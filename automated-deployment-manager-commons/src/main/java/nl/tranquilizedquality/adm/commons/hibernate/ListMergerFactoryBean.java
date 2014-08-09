package nl.tranquilizedquality.adm.commons.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.ListFactoryBean;

/**
 * {@link ListFactoryBean} that can merge factory bean lists.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public class ListMergerFactoryBean extends ListFactoryBean {

	/** The list that contains lists with values that can be merged. */
	private List<List<Object>> mergeList;

	/**
	 * Default constructor.
	 */
	public ListMergerFactoryBean() {
		mergeList = new ArrayList<List<Object>>();

		setSourceList(new ArrayList<Object>());
	}

	/**
	 * Sets the list of lists that need to be merged.
	 * 
	 * @param mergeList
	 *            The list of lists that will be merged.
	 */
	public void setMergeList(final List<List<Object>> mergeList) {
		this.mergeList = mergeList;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List createInstance() {
		final List<Object> listOrigin = super.createInstance();

		for (final List list : mergeList) {

			for (final Object object : list) {
				listOrigin.add(object);
			}
		}

		System.out.println(listOrigin);

		return listOrigin;
	}
}
