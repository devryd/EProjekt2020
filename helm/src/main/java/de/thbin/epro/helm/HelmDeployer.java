package de.thbin.epro;
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
            requestBuilder.setName("test-charts"); // Set the Helm release name
            requestBuilder.setWait(true); // Wait for Pods to be ready

            final Future<InstallReleaseResponse> releaseFuture = releaseManager.install(requestBuilder, chart);
            assert releaseFuture != null;
            final Release release = releaseFuture.get().getRelease();
            assert release != null;
        }
    }
}
