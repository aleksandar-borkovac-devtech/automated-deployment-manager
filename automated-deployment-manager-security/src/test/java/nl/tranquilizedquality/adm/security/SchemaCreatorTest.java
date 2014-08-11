/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 27 jul. 2011 File: SchemaCreatorTest.java
 * Package: nl.Tranquilized Quality.adm.core.persistence.db.hibernate
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
package nl.tranquilizedquality.adm.security;

import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernatePrivilege;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateRole;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateScope;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserRole;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * Schema creator.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 27 jul. 2011
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class SchemaCreatorTest {

    /**
     * @param args
     *            Arguments to pass
     */
    public static void main(final String[] args) {
        createSchema("org.hibernate.dialect.Oracle9iDialect", "true", "target/adm-oracle9i-schema.sql");
        createSchema("org.hibernate.dialect.Oracle10gDialect", "true", "target/adm-oracle10g-schema.sql");
        createSchema("org.hibernate.dialect.PostgreSQLDialect", "true", "target/adm-postgresql-schema.sql");
        createSchema("org.hibernate.dialect.HSQLDialect", "true", "target/adm-hsql-schema.sql");
        createSchema("org.hibernate.dialect.MySQLDialect", "true", "target/adm-mysql-schema.sql");
    }

    /**
     * 
     */
    private static void createSchema(final String dialect, final String formatSql, final String fileName) {
        // create the configuration
        final Configuration cfg = new Configuration();

        // add annotated classes
        cfg.addAnnotatedClass(HibernateUser.class);
        cfg.addAnnotatedClass(HibernateScope.class);
        cfg.addAnnotatedClass(HibernatePrivilege.class);
        cfg.addAnnotatedClass(HibernateUserRole.class);
        cfg.addAnnotatedClass(HibernateRole.class);
        cfg.addAnnotatedClass(HibernateUserGroup.class);

        // setup Hibernate properties
        cfg.setProperty("hibernate.dialect", dialect);
        cfg.setProperty("hibernate.format_sql", formatSql);

        // setup the schema exporter
        final SchemaExport schemaExport = new SchemaExport(cfg);
        schemaExport.setOutputFile(fileName);
        schemaExport.setDelimiter(";");

        // create the script
        schemaExport.create(true, false);
    }

    @Test
    public void testCreateSQLSchemaScript() {
        main(null);
    }

}
