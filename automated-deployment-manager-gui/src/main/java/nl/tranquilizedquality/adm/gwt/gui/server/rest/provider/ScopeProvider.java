package nl.tranquilizedquality.adm.gwt.gui.server.rest.provider;

import javax.ws.rs.Consumes;

import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernatePrivilege;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateRole;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateScope;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.ParameterHandler;

import com.thoughtworks.xstream.XStream;

/**
 * An implementation of a {@link ParameterHandler} that can handle the
 * transformation of a {@link Scope} from XML.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
@Consumes(value = "*/*")
public class ScopeProvider implements ParameterHandler<Scope> {

    /** logger for this class */
    private static final Log LOGGER = LogFactory.getLog(ScopeProvider.class);

    /** The XStream xml transformer. */
    private final XStream xstream;

    /**
     * Default constructor.
     */
    public ScopeProvider() {
        xstream = new XStream();

        xstream.alias("scope", HibernateScope.class);
        xstream.aliasField("id", HibernateScope.class, "id");
        xstream.aliasField("name", HibernateScope.class, "name");
        xstream.aliasField("description", HibernateScope.class, "description");
        xstream.alias("privilege", HibernatePrivilege.class);
        xstream.aliasField("id", HibernatePrivilege.class, "id");
        xstream.aliasField("name", HibernatePrivilege.class, "name");
        xstream.aliasField("valid", HibernatePrivilege.class, "valid");
        xstream.aliasField("description", HibernateScope.class, "description");
        xstream.alias("role", HibernateRole.class);
        xstream.aliasField("id", HibernateRole.class, "id");
        xstream.aliasField("name", HibernateRole.class, "name");
        xstream.aliasField("valid", HibernateRole.class, "valid");
        xstream.aliasField("frozen", HibernateRole.class, "frozen");
        xstream.aliasField("description", HibernateRole.class, "description");
        xstream.alias("user", HibernateUser.class);
    }

    @Override
    public Scope fromString(final String xml) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Reading from string....");
        }

        return (Scope) xstream.fromXML(xml);
    }

}
