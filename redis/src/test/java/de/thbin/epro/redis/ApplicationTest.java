package de.thbin.epro.redis;

import de.thbin.epro.core.ServiceBrokerImpl;
import de.thbin.epro.model.ServiceOffering;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.util.Collections;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class ApplicationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @InjectMocks
    ServiceBrokerImpl serviceBroker;

    // URL for rest api calls
    private final String URL = "http://127.0.0.1:8080";
    // basic http auth user
    private final String user = "test";
    //basic http auth password
    private final String password = "test";
    //X-Broker-API-Version
    private final String brokerVersion = "2.14";
    //X-Broker-Originating-Identity
    private final String originIdentity = "test-origin";
    //X-Broker-Request-Identity
    private final String requestIdentity = "test-request";
    //request headers
    private  MultiValueMap<String, String> headers;

    @Before
    public void initRestTemplate() {
        // set up headers for request
        headers = new LinkedMultiValueMap<>();
        headers.add("X-Broker-API-Version", brokerVersion);
        headers.add("X-Broker-API-Originating-Identity", originIdentity);
        headers.add("X-Broker-API-Request-Identity", requestIdentity);
    }

    // basic test for getServiceCatalog, not the "real" rest endpoint (no security config etc.)
    @Test
    public void getServiceCatalog() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<ServiceOffering[]> responseEntity = serviceBroker.getServiceCatalog(brokerVersion, originIdentity, requestIdentity);

        assertTrue(isEqualTo(responseEntity.getStatusCodeValue(), 200));
    }

    // test for rest call of /v2/catalog
    @Test
    public void testServiceCatalog() {
        ResponseEntity<String> response = restTemplate.withBasicAuth(user, password).exchange(
                URL + "/v2/catalog", HttpMethod.GET, new HttpEntity<Object>(headers),
                String.class);
        assertTrue(isEqualTo(response.getStatusCodeValue(), 200));
    }

    // returns true if first == second
    private boolean isEqualTo(int first, int second) {
        return first == second;
    }
}
