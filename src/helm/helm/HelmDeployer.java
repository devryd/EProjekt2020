package helm;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import de.thbin.epro.model.ServicePlan;
import hapi.chart.ChartOuterClass;
import hapi.chart.ChartOuterClass.Chart;
import hapi.chart.ConfigOuterClass;
import hapi.release.ReleaseOuterClass;
import hapi.release.ReleaseOuterClass.Release;
import hapi.services.tiller.Tiller.InstallReleaseRequest;
import hapi.services.tiller.Tiller.InstallReleaseResponse;
import helm.DeploymentSize;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.microbean.helm.ReleaseManager;
import org.microbean.helm.Tiller;
import org.microbean.helm.chart.URLChartLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

/**
 * Service to trigger deployment and uninstalling of
 * helm charts. MUST be used with a helm-version prior to 3.1
 * @author Jannik Nöldner
 *
 */
@Service
public class HelmDeployer {

    private HashMap<String, DeploymentData> releases;
    private Tiller tiller;
    private DefaultKubernetesClient client;
    private ReleaseManager releaseManager;
    private InstallReleaseRequest.Builder requestBuilder;

    public HelmDeployer(){
        releases = new HashMap<String, DeploymentData>();
    }

    /**
     * Creates new Helm-Deployment, using a predefined helm chart. Values to vary deployment size
     * are added before deployment
     * @param size one of enumeration "DeploymentSize"
     * @param id String to identify Deployment
     * @throws IOException          when loading chart fails
     * @throws ExecutionException   when installing chart fails
     * @throws InterruptedException when fetching deployed release fails
     */
    public void deployRedis(DeploymentSize size, String id) throws IOException, ExecutionException, InterruptedException {
        final URI uri = URI.create("file:///C:/Users/JNoel/Documents/helm-charts/simple-redis-cluster");
        assert uri != null;
        final URL url = uri.toURL();
        assert url != null;
        Chart.Builder chart = null;
        try (final URLChartLoader chartLoader = new URLChartLoader()) {
            chart = chartLoader.load(url);
        }
        assert chart != null;

            client = new DefaultKubernetesClient();
             tiller = new Tiller(client);
             releaseManager = new ReleaseManager(tiller);
            requestBuilder = InstallReleaseRequest.newBuilder();
            assert requestBuilder != null;
            requestBuilder.setTimeout(300L);
            requestBuilder.setName("mbh"+id);
            requestBuilder.setWait(true);
            Map<String, Object> values = null;
            switch (size) {
                case SMALL:
                    values = buildSmallSizeChartValues();
                    break;
                case CLUSTER:
                    values = buildClusterSizeChartValues();
                    break;
                case STANDARD:
                    values = new LinkedHashMap<String, Object>();
            }
            final String yaml = new Yaml().dump(values);
            requestBuilder.getValuesBuilder().setRaw(yaml);
            final Future<InstallReleaseResponse> releaseFuture = releaseManager.install(requestBuilder, chart);
            assert releaseFuture != null;
            final Release release = releaseFuture.get().getRelease();
            assert release != null;
            releases.put(id, new DeploymentData(release, "mbh"+id+"-simple-redis-cluster"));
        }

    /**
     * Uninstalls release identified by given ID.
     * @param id identifies service to be uninstalled
     * @throws IOException if uninstalling process fails
     * @throws ExecutionException   if fetching UninstallResponse fails
     * @throws InterruptedException if fetching UninstallResponse fails
     */
    public void uninstallService(String id) throws ExecutionException, InterruptedException, IOException {
        Release release = releases.remove(id).getRelease();
        hapi.services.tiller.Tiller.UninstallReleaseRequest.Builder unBuilder= hapi.services.tiller.Tiller.UninstallReleaseRequest.newBuilder();

        unBuilder.setTimeout(300L);
        unBuilder.setName("mbh"+id);
        unBuilder.setPurge(true);
        hapi.services.tiller.Tiller.UninstallReleaseRequest req = unBuilder.build();

        Future<hapi.services.tiller.Tiller.UninstallReleaseResponse> future = releaseManager.uninstall(req);
        hapi.services.tiller.Tiller.UninstallReleaseResponse resp = future.get();
    }

    /**
     * builds Chart for a cluster-sized deployment
     * @return built chart-map
     */
    private LinkedHashMap<String, Object> buildClusterSizeChartValues() {
        LinkedHashMap<String, Object> clusterValueMap = new LinkedHashMap<String, Object>();
        clusterValueMap.put("replicaCount", 3);
        return clusterValueMap;
    }

    /**
     * builds chart for small-sized deployment
     * @return built chart-map
     */
    private LinkedHashMap<String, Object> buildSmallSizeChartValues() {
        LinkedHashMap<String, Object> smallSizeValueMap = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> resources = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> limits = new LinkedHashMap<String, Object>();
        limits.put("cpu", 1);
        limits.put("memory", "500mi");
        resources.put("limits", limits);
        smallSizeValueMap.put("resources", resources);
        return smallSizeValueMap;
    }

}
