package nl.tranquilizedquality.adm.gwt.gui.server.service.user;

import java.util.HashMap;
import java.util.Map;

/**
 * This bean is to be used on a session scope as a statefull bean.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class UserDataBean {

    /** The data used for a specific user. */
    private final Map<String, Object> data;

    /**
     * Default constructor.
     */
    public UserDataBean() {
        data = new HashMap<String, Object>();
    }

    public void addData(final String key, final Object data) {
        this.data.put(key, data);
    }

    public void removeData(final String key) {
        this.data.remove(key);
    }

    @SuppressWarnings("unchecked")
    public <X> X getData(final String key) {
        return (X) this.data.get(key);
    }

}
