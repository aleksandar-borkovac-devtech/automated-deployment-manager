package nl.tranquilizedquality.adm.core.business.template.renderer;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Required;

/**
 * Abstract processor that handles template actions.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 19 okt. 2012
 */
public abstract class AbstractRenderer {

    /** Logger for this class */
    private static final Log LOGGER = LogFactory.getLog(AbstractRenderer.class);

    /** The velocity engine used for retrieving a template. */
    protected VelocityEngine velocityEngine;

    /** The velocity engine properties */
    protected Properties velocityProperties;

    /**
     * Initialize the velocity engine.
     */
    protected void init() {
        /*
         * Initialize the engine(with the path property).
         */
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialize the engine...");
        }

        try {
            velocityEngine.init();
        } catch (final Exception e) {
            throw new IncorrectTemplateException("Exception" + e);
        }
    }

    /**
     * Merges the given properties with the give template into a HTML/CSS
     * structure.
     * 
     * @param id
     *            The template {@link String} id.
     * @param properties
     *            The template {@link Map} properties.
     * @return Returns the {@link String} containing the HTML/CSS structure.
     */
    protected abstract String merge(String id, Map<String, Object> properties);

    /**
     * @param velocityEngine
     *            the velocityEngine to set
     */
    @Required
    public void setVelocityEngine(final VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

}
