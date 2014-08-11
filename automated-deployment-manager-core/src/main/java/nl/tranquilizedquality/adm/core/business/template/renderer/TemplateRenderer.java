package nl.tranquilizedquality.adm.core.business.template.renderer;

import java.util.Map;

/**
 * Renderer that renders a template.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 19 okt. 2012
 */
public interface TemplateRenderer {

    /**
     * Renders the template with the specified id to a {@link String} value
     * using the specified properties that will be used within the template.
     * 
     * @param id
     *            The template id.
     * @param properties
     *            The properties that will be used in the template.
     * @return Returns a {@link String} value of the rendered content.
     */
    String merge(String id, Map<String, Object> properties);

}
