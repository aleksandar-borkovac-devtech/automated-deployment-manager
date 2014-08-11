/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 1 sep. 2011 File: HibernateEnvironment.java
 * Package: nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean
 * 
 * Copyright (c) 2011 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

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

import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of an enviroment.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 sep. 2011
 */
@Entity()
@Table(name = "ENVIRONMENTS", schema = "ADM")
public class HibernateEnvironment extends AbstractUpdatableDomainObject<Long> implements Environment {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -7768906998408351484L;

    /** The name of the environment. */
    @BusinessField
    private String name;

    /** The description of the environment. */
    @BusinessField
    private String description;

    /**
     * Determines if this environment is a production environment so alerts will
     * be displayed when deploying to it.
     */
    @BusinessField
    private boolean production;

    /** The users that are allowed to deploy to this environment. */
    private List<User> users;

    /**
     * Default constructor.
     */
    public HibernateEnvironment() {
        users = new ArrayList<User>();
    }

    @Override
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ENVIRONMENTS_SEQ_GEN")
    @SequenceGenerator(name = "ENVIRONMENTS_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.ENVIRONMENTS_SEQ")
    public Long getId() {
        return id;
    }

    @Override
    @Index(name = "ENVIRONMENT_NAME_IDX")
    @Column(name = "NAME", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    @Column(name = "DESCRIPTION", nullable = true, unique = false)
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    @OrderBy(value = "id")
    @ManyToMany(targetEntity = HibernateUser.class, cascade = CascadeType.ALL)
    @ForeignKey(name = "FK_ENVIRONMENT_DEPLOYERS", inverseName = "FK_DEPLOYERS")
    @JoinTable(name = "ENVIRONMENT_DEPLOYERS", schema = "ADM", joinColumns = {@JoinColumn(name = "ENVIRONMENT_ID") }, inverseJoinColumns = {@JoinColumn(name = "USER_ID") })
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void setUsers(final List<User> users) {
        this.users = users;
    }

    @Override
    @Type(type = "yes_no")
    @Column(name = "PRODUCTION", nullable = false, unique = false)
    public boolean isProduction() {
        return production;
    }

    public void setProduction(final boolean production) {
        this.production = production;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof Environment) {
            super.copy(object);

            final Environment environment = (Environment) object;
            name = environment.getName();
            description = environment.getDescription();
            production = environment.isProduction();

            users = new ArrayList<User>();
            final List<User> originalUsers = environment.getUsers();
            for (final User user : originalUsers) {
                final HibernateUser newUser = new HibernateUser();
                newUser.copy(user);
                users.add(newUser);
            }
        }
    }

}
