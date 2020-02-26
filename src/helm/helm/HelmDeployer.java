package helm;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import hapi.chart.ChartOuterClass;
import hapi.chart.ChartOuterClass.Chart;
import hapi.chart.ConfigOuterClass;
import hapi.release.ReleaseOuterClass;
import hapi.release.ReleaseOuterClass.Release;
import hapi.services.tiller.Tiller.InstallReleaseRequest;
import hapi.services.tiller.Tiller.InstallReleaseResponse;
import helm.DeploymentSize;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.microbean.helm.ReleaseManager;
import org.microbean.helm.Tiller;
import org.microbean.helm.chart.URLChartLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

/**@author Jannik Nöldner
 *
 */
@Service
public class HelmDeployer {
 //   private HashMap<Long, Release> releases;
    private HashMap<String, Release> releases;
    private Tiller tiller;
    private DefaultKubernetesClient client;
    private ReleaseManager releaseManager;
    private InstallReleaseRequest.Builder requestBuilder;

    public HelmDeployer(){
        releases = new HashMap<String, Release>();
    }
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
 /*           Map<String, Object> service = new HashMap<String, Object>();
            service.put("type", "LoadBalancer");
            service.put("loadBalancerIP", "104.198.205.71");
            values.put("service", service);
*/
            final String yaml = new Yaml().dump(values);
            requestBuilder.getValuesBuilder().setRaw(yaml);
            final Future<InstallReleaseResponse> releaseFuture = releaseManager.install(requestBuilder, chart);
            assert releaseFuture != null;
            final Release release = releaseFuture.get().getRelease();
            assert release != null;
        }


    public void uninstallService(String id) throws Exception{
        Release release = releases.remove(id);
        hapi.services.tiller.Tiller.UninstallReleaseRequest.Builder unBuilder= hapi.services.tiller.Tiller.UninstallReleaseRequest.newBuilder();

        unBuilder.setTimeout(300L);
        unBuilder.setName("mbh"+id);
        unBuilder.setPurge(true);
        hapi.services.tiller.Tiller.UninstallReleaseRequest req = unBuilder.build();

        Future<hapi.services.tiller.Tiller.UninstallReleaseResponse> future = releaseManager.uninstall(req);
  //      hapi.services.tiller.Tiller.UninstallReleaseResponse resp = future.get();
    }

    private LinkedHashMap<String, Object> buildClusterSizeChartValues() {
        LinkedHashMap<String, Object> clusterValueMap = new LinkedHashMap<String, Object>();
        clusterValueMap.put("replicaCount", 3);
        return clusterValueMap;
    }

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