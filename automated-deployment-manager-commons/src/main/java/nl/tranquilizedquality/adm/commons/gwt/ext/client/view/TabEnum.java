package nl.tranquilizedquality.adm.commons.gwt.ext.client.view;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.controller.navigation.AbstractNavigationController;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.navigation.TreeMenuItem;

/**
 * Generic interface for a tab enumerated type. It is used in
 * {@link TreeMenuItem} to initialize the menu tree and and
 * {@link AbstractNavigationController} to support tab navigation. The
 * application specific tab enum should implement this interface, otherwise the
 * navigation controller cannot navigate between the tabs and the
 * {@link TreeMenuItem} cannot activate the relevant tab. It is the common
 * interface used by all classes that deal with navigation.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public interface TabEnum {
}
