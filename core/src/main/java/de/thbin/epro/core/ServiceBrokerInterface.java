package de.thbin.epro.core;

import de.thbin.epro.model.FetchResponseBody;
import de.thbin.epro.model.ProvideResponseBody;
import de.thbin.epro.model.ServiceOffering;
import org.springframework.http.ResponseEntity;

public interface ServiceBrokerInterface {
    ResponseEntity<ServiceOffering[]> getServiceCatalog(String brokerVersionUsed, String originIdentity, String requestIdentity); //string???? + HttpStatus
    /*
    route: GET /v2/catalog
     */

    ResponseEntity<String> pollStateServiceInstance(String brokerVersionUsed, String originIdentity, String requestIdentity,
    String instance_id);
    //poll state of last requestet operation
    /*
    typ ok?
    neue klassen für body von request (@RequestBody) und response (auch für response oder
    einfach generischen typen bei responseentity anpassen?)
    route: GET /v2/service_instances/:instance_id/last_operation
     */

    ResponseEntity<String> pollStateServiceBinding(String brokerVersionUsed, String originIdentity, String requestIdentity);
    /*
    typ sollte passen
    route: GET /v2/service_instances/:instance_id/service_bindings/:binding_id/last_operation
     */

    ResponseEntity<ProvideResponseBody> provide(String brokerVersionUsed, String originIdentity, String requestIdentity);
    /*
    typ string passt
    service instance auf id hinterlegen?
    zum bereitstellen einer service instanz
    route: PUT /v2/service_instances/:instance_id
     */

    ResponseEntity<FetchResponseBody> fetchServiceInstance(String brokerVersionUsed, String originIdentity, String requestIdentity);
    /*
    typ ok? string string string object im return
    nur notwendig, wenn instances_retrievable :true
    route: GET /v2/service_instances/:instance_id
     */

    ResponseEntity<String> updateServiceInstance(String brokerVersionUsed, String originIdentity, String requestIdentity);
    /*
    typ passt
    route: PATCH /v2/service_instances/:instance_id
    instance_id = id einer zuvor provideten service instance
     */

    ResponseEntity<String> bindService(String brokerVersionUsed, String originIdentity, String requestIdentity);
    /*
    string ok
    route: PUT /v2/service_instance/:instance_id/service_bindings/:binding_id
     */

    ResponseEntity<String> fetchServiceBinding(String brokerVersionUsed, String originIdentity, String requestIdentity);
    /*
    viele verschiedene typen, generischer typ?
    route: GET /v2/service_instance/:instance_id/service_bindings/:binding_id
     */

    ResponseEntity<String> unbind(String brokerVersionUsed, String originIdentity, String requestIdentity);
    /*
    string ok
    route: DELETE /v2/service_instances/:instance_id/service_bindings/:binding_id
     */

    ResponseEntity<String> deprovide(String brokerVersionUsed, String originIdentity, String requestIdentity);
    /*
    string ok
    route: DELETE /v2/service_instances/:instance_id
     */


}
