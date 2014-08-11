package nl.tranquilizedquality.adm.gwt.gui.client.model.navigation;

import java.io.Serializable;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;

/**
 * Basic tree item that can be used in a {@link Role} based {@link TreeGrid}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class RoleTreeItem extends BaseTreeModel implements Serializable {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -5852639910705546067L;

    /** Tree item id. Must be unique in the whole tree */
    private static int ID = 0;

    /** Indicates if this item has children */
    private boolean parent;

    /** Holds a copy of the object represented by this tree item */
    private DomainObject<Long> clientObject;

    public RoleTreeItem() {
        set("id", ID++);
    }

    /**
     * Constructor taking a scope.
     * 
     * @param scope
     *            the scope.
     */
    public RoleTreeItem(final Scope scope) {
        this();
        clientObject = scope;
        set("name", scope.getName());
        parent = true;
    }

    /**
     * Constructor taking a role.
     * 
     * @param role
     *            the role.
     */
    public RoleTreeItem(final Role role) {
        this();
        clientObject = role;
        set("name", role.getName());
        set("valid", role.isValid());
        parent = true;
    }

    /**
     * Constructor taking a privilege.
     * 
     * @param privilege
     *            the privilege.
     */
    public RoleTreeItem(final Privilege privilege) {
        this();
        clientObject = privilege;
        set("name", privilege.getName());
        set("valid", privilege.isValid());
        parent = false;
    }

    /**
     * Retrieves the unique identifier for the tree item.
     * 
     * @return an {@link Integer} value of the id.
     */
    public Integer getId() {
        return (Integer) get("id");
    }

    /**
     * Retrieves the unique key that identifies the underlying persistent
     * object.
     * 
     * @return a {@link Long} value of the key.
     */
    public Long getKey() {
        return clientObject.getId();
    }

    /**
     * Retrieves whether this tree item is a parent (i.e. has children).
     * 
     * @return a {@link Boolean} indicating if the item is a parent.
     */
    public boolean isParent() {
        return parent;
    }

    /**
     * Retrieves the domain object represented by this tree item.
     * 
     * @return the domain object.
     */
    public DomainObject<Long> getClientObject() {
        return clientObject;
    }

    @Override
    public boolean equals(final Object object) {
        if (object != null && object instanceof RoleTreeItem) {
            final RoleTreeItem treeItem = (RoleTreeItem) object;
            return treeItem.getId().equals(getId());
        }
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public String toString() {
        return get("name");
    }
}
