package nl.tranquilizedquality.adm.core.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;

/**
 * Factory that can create release snapshots. A release snapshot are the
 * artifacts that were being released at a certain moment in time.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 8 okt. 2011
 */
public interface ReleaseSnapshotFactory {

    /**
     * Creates a snapshot from the passed in release and artifacts.
     * 
     * @param execution
     *        The execution where this snapshot is part of.
     * @param artifacts
     *        The artifacts that will be used in the snapshot.
     * @return Returns a list of {@link MavenArtifactSnapshot} objects based on the artifacts that
     *         are being deployed.
     */
    List<MavenArtifactSnapshot> createSnapshot(ReleaseExecution execution, List<MavenArtifact> artifacts);

}
