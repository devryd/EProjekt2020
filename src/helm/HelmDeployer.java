package helm;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
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
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.microbean.helm.ReleaseManager;
import org.microbean.helm.Tiller;
import org.microbean.helm.chart.URLChartLoader;
import org.yaml.snakeyaml.Yaml;

public class HelmDeployer {

    public static final int SMALL_SIZE = 0;
    public static final int STANDARD_SIZE = 1;
    public static final int CLUSTER_SIZE = 2;

    public void buildChart(int size) throws IOException, ExecutionException, InterruptedException {
        final URI uri = URI.create("file:///C:/Users/JNoel/Documents/helm-charts/simple-redis-cluster");
        assert uri != null;
        final URL url = uri.toURL();
        assert url != null;
        Chart.Builder chart = null;
        try (final URLChartLoader chartLoader = new URLChartLoader()) {
            chart = chartLoader.load(url);
        }
        assert chart != null;

        try (final DefaultKubernetesClient client = new DefaultKubernetesClient();
             final Tiller tiller = new Tiller(client);
             final ReleaseManager releaseManager = new ReleaseManager(tiller)) {
            final InstallReleaseRequest.Builder requestBuilder = InstallReleaseRequest.newBuilder();
            assert requestBuilder != null;
            requestBuilder.setTimeout(300L);
            requestBuilder.setName("test40");
            requestBuilder.setWait(true);
            Map<String, Object> values = null;

            switch (size) {
                case SMALL_SIZE:
                    values = buildSmallSizeChartValues();
                    System.out.println("building small size");
                    break;
                case CLUSTER_SIZE:
                    values = buildClusterSizeChartValues();
                    System.out.println("building cluster size");
                    break;
                default:
                    if (size != STANDARD_SIZE) {
                        System.out.println("Unknown size value. Building Standard size");
                    }
                    values = new LinkedHashMap<String, Object>();
            }

            final String yaml = new Yaml().dump(values);
            requestBuilder.getValuesBuilder().setRaw(yaml);
            final Future<InstallReleaseResponse> releaseFuture = releaseManager.install(requestBuilder, chart);
            assert releaseFuture != null;
            final Release release = releaseFuture.get().getRelease();
            assert release != null;
        }
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