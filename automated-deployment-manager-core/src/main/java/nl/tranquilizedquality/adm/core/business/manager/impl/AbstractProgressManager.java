package nl.tranquilizedquality.adm.core.business.manager.impl;

import nl.tranquilizedquality.adm.core.business.manager.DeployProgress;

/**
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/23/12
 */
public class AbstractProgressManager {
    /** Stateful bean keeping track on how far a specific release is. */
    private DeployProgress deployProgress;

    protected void addProgress(final int stepProgress) {
        if (deployProgress != null) {
            deployProgress.addProgress(stepProgress);
        }
    }

    protected void registerActivity(final String msg) {
        if (deployProgress != null) {
            deployProgress.registerActivity(msg);
        }
    }

    protected void complete() {
        if (deployProgress != null) {
            deployProgress.complete();
        }
    }

    public void setDeployProgress(final DeployProgress deployProgress) {
        this.deployProgress = deployProgress;
    }
}
