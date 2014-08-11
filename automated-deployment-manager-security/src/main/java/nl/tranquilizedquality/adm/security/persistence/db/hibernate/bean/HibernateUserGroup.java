/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 23, 2012 File: fHibernateGroup.java
 * Package: nl.Tranquilized Quality.adm.security.persistence.db.hibernate.bean
 * 
 * Copyright (c) 2012 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a user group.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 23, 2012
 */
@Entity
@Table(name = "ADM_USER_GROUPS", schema = "SEC")
public class HibernateUserGroup extends AbstractUpdatableDomainObject<Long> implements UserGroup {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -8085926074689980994L;

    /** The name of the user group. */
    @BusinessField
    private String name;

    /** The users that are part of this group. */
    private List<User> users;

    /**
     * Default constructor.
     */
    public HibernateUserGroup() {
        users = new ArrayList<User>();
    }

    @Id
    @Override
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "USER_GROUPS_SEQ_GEN")
    @SequenceGenerator(name = "USER_GROUPS_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "SEC.USER_GROUPS_SEQ")
    public Long getId() {
        return id;
    }

    @Override
    @Index(name = "IDX_USER_GROUP_NAME")
    @Column(name = "NAME", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    @OrderBy(value = "id")
    @ForeignKey(name = "FK_USER_GROUP_USERS", inverseName = "FK_USERS_GROUP")
    @JoinTable(name = "ADM_USER_GROUP_USERS", schema = "SEC", joinColumns = {@JoinColumn(name = "USER_GROUP_ID") }, inverseJoinColumns = {@JoinColumn(name = "USER_ID") })
    @ManyToMany(targetEntity = HibernateUser.class, cascade = CascadeType.ALL)
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void setUsers(final List<User> users) {
        this.users = users;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof UserGroup) {
            super.copy(object);

            final UserGroup group = (UserGroup) object;
            this.name = group.getName();

            final List<User> newUsers = group.getUsers();

            this.users = new ArrayList<User>();
            if (newUsers != null && !newUsers.isEmpty()) {

                for (final User user : newUsers) {
                    final HibernateUser newUser = new HibernateUser();
                    newUser.copy(user);
                    this.users.add(newUser);
                }
            }
        }
    }

}
