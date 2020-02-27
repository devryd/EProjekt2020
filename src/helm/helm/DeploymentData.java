package helm;

import hapi.release.ReleaseOuterClass.Release;

/**
 * class for capsuling release and deployment-name
 * @author Jannik Nöldner
 */
public class DeploymentData {
    private Release release;
    private String deploymentName;

    public DeploymentData(Release release, String deploymentName){
        this.release=release;
        this.deploymentName=deploymentName;
    }

    public Release getRelease(){
        return release;
    }

    public String getDeploymentName(){
        return deploymentName;
    }

}
