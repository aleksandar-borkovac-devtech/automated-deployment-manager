package nl.tranquilizedquality.adm.core.business.template.renderer;

import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

/**
 * TemplateRenderer renderer that constructs a template.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 19 okt. 2012
 */
public class VelocityTemplateRenderer extends AbstractRenderer implements TemplateRenderer {

    /** Logger for this class */
    private static final Log LOGGER = LogFactory.getLog(VelocityTemplateRenderer.class);

    @Override
    public String merge(final String id, final Map<String, Object> properties) {

        super.init();

        /*
         * Merge the template and the properties.
         */
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Template code is " + id);
            for (final Map.Entry<String, Object> entry : properties.entrySet()) {
                LOGGER.debug("Property: " + entry.getKey() + " value: " + entry.getValue());
            }
        }

        try {

            final StringWriter writer = new StringWriter();

            /*
             * Create context and add properties.
             */
            final VelocityContext context = new VelocityContext(properties);

            /*
             * Create and render template.
             */
            final Template t = velocityEngine.getTemplate(id);
            t.merge(context, writer);

            return writer.toString();
        } catch (final Exception e) {

            if (LOGGER.isErrorEnabled()) {
                LOGGER.debug("Template with ID: " + id + "not found.");
            }

            throw new IncorrectTemplateException("Exception" + e);
        }

    }

}
