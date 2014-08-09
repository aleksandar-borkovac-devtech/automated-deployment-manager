/*
 * @(#)AdmDeployerPublisher.java 27 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilzedquality.adm.jenkins.deployer;

import hudson.Extension;
import hudson.Launcher;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSetBuild;
import hudson.maven.agent.AbortException;
import hudson.maven.reporters.MavenArtifact;
import hudson.maven.reporters.MavenArtifactRecord;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import hudson.util.RunList;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Publisher that signals ADM (Automated Deployment Manager) to deploy a specific artifact.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 27 feb. 2013
 */
public class AdmArtifactDeployerRecorder extends Publisher {

    /** The name of the release where the artifact is part of. */
    private final String releaseName;

    /** The name of the environment the artifact is supposed to be deployed to. */
    private final String environmentName;

    /** The id of the Maven artifact that needs to be deployed. */
    private final String artifactId;

    /** User name used to authenticate for an ADM deployment. */
    private final String username;

    /** Password used to authenticate for an ADM deployment. */
    private final String password;

    /**
     * Constructor taking the URL to the ADM REST service to deploy an artifact. Also the name of
     * the release is used to select the release where the artifact is part of and the target
     * environment to deploy to.
     * 
     * @param releaseName
     *        The name of the release where the artifact is configured in.
     * @param environmentName
     *        The name of the environment to deploy to.
     * @param artifactId
     *        The Maven artifact identifier.
     * @param username
     *        The user name to use for authentication.
     * @param password
     *        The password to use for authentication.
     */
    @DataBoundConstructor
    public AdmArtifactDeployerRecorder(final String releaseName, final String environmentName, final String artifactId,
            final String username, final String password) {
        this.releaseName = releaseName;
        this.environmentName = environmentName;
        this.artifactId = artifactId;
        this.username = username;
        this.password = password;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    @Override
    public boolean needsToRunAfterFinalized() {
        return true;
    }

    @Override
    public boolean perform(final AbstractBuild<?, ?> build, final Launcher launcher, final BuildListener listener)
            throws InterruptedException, IOException {

        final MavenModuleSetBuild mavenModuleSetBuild = (MavenModuleSetBuild) build;
        MavenBuild mavenBuild = null;
        final Map<MavenModule, MavenBuild> moduleLastBuilds = mavenModuleSetBuild.getModuleLastBuilds();
        final Set<Entry<MavenModule, MavenBuild>> entrySet = moduleLastBuilds.entrySet();
        for (final Entry<MavenModule, MavenBuild> entry : entrySet) {
            final MavenModule mavenModule = entry.getKey();
            final RunList<MavenBuild> builds = mavenModule.getBuilds();
            for (final MavenBuild runBuild : builds) {
                mavenBuild = runBuild;
                break;
            }
            break;
        }

        final MavenArtifactRecord mavenArtifacts = mavenBuild.getMavenArtifacts();
        final MavenArtifact mainArtifact = mavenArtifacts.mainArtifact;
        final String version = mainArtifact.version;
        final String groupId = mainArtifact.groupId;

        final DescriptorImpl buildStepDescriptor = getDescriptor();
        final String admRestUrl = buildStepDescriptor.getAdmRestUrl();

        final StringBuilder builder = new StringBuilder();
        builder.append(admRestUrl);
        builder.append("/release/");
        builder.append(releaseName);
        builder.append("/artifact/");
        builder.append(groupId);
        builder.append("/");
        builder.append(artifactId);
        builder.append("/");
        builder.append(version);
        builder.append("/deploy/");
        builder.append(environmentName);
        final String targetUrl = builder.toString();

        final PostMethod method = new PostMethod(targetUrl);
        final HttpClient client = new HttpClient();
        final HttpState state = client.getState();
        state.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM), new UsernamePasswordCredentials(
                username, password));
        final int responseCode = client.executeMethod(method);
        final String responseBody = method.getResponseBodyAsString();
        switch (responseCode) {
            case 200:
                return super.perform(build, launcher, listener);

            default:
                throw new AbortException("Failed to deploy artifact with ADM: " + responseBody);
        }

    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         * <p>
         * If you don't want fields to be persisted, use <tt>transient</tt>.
         */
        private String admRestUrl;

        /**
         * Performs on-the-fly validation of the form field 'name'.
         * 
         * @param value
         *        This parameter receives the value that the user has typed.
         * @return
         *         Indicates the outcome of the validation. This is sent to the browser.
         */
        public FormValidation doCheckName(@QueryParameter final String value) throws IOException, ServletException {
            if (value.length() == 0) {
                return FormValidation.error("Please set a name");
            }
            if (value.length() < 4) {
                return FormValidation.warning("Isn't the name too short?");
            }
            return FormValidation.ok();
        }

        @Override
        public boolean isApplicable(final Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        @Override
        public String getDisplayName() {
            return "Automated Deployment Manager";
        }

        @Override
        public boolean configure(final StaplerRequest req, final JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            admRestUrl = formData.getString("admRestUrl");
            // ^Can also use req.bindJSON(this, formData);
            // (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req, formData);
        }

        public String getAdmRestUrl() {
            return admRestUrl;
        }

    }

    public String getReleaseName() {
        return releaseName;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
