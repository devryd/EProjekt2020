package helm;
import java.net.URI;
import java.net.URI;
import java.net.URL;

import java.util.Collection;

import java.util.concurrent.Future;

import hapi.chart.ChartOuterClass;
import hapi.chart.ChartOuterClass.Chart;

import hapi.release.ReleaseOuterClass;
import hapi.release.ReleaseOuterClass.Release;

import hapi.services.tiller.Tiller.InstallReleaseRequest;
import hapi.services.tiller.Tiller.InstallReleaseResponse;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;

import org.microbean.helm.ReleaseManager;
import org.microbean.helm.Tiller;

import org.microbean.helm.chart.URLChartLoader;


public class HelmDeployer {
    public void testDeploy() throws Exception {
        final URI uri = URI.create("https://kubernetes-charts.storage.googleapis.com/wordpress-0.6.6.tgz");
        assert uri != null;
        final URL url = uri.toURL();
        assert url != null;
        Chart.Builder chart = null;
        DefaultKubernetesClient kub = new DefaultKubernetesClient();
        InstallReleaseRequest req = InstallReleaseRequest.newBuilder().build();

    }
}
