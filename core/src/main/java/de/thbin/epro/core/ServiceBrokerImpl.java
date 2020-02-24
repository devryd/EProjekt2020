package de.thbin.epro;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ServiceBrokerImpl { //implements de.thbin.epro.ServiceBrokerInterface

    ServiceOffering[] catalog;

    @RequestMapping (value="/v2/catalog", method = RequestMethod.GET)
    //@ResponseBody entweder das oder responseentity, ansonsten doppelt gemoppelt
    public ResponseEntity<ServiceOffering[]> getServiceCatalog(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity //notwendig? fuer request-tracing
            ) {
        /*ResponseEntity<String> response;
        response.*/
        return new ResponseEntity<ServiceOffering[]>(catalog, HttpStatus.OK);
    }

    @RequestMapping (value = "/v2/service_instances/:instance_id/last_operation", method = RequestMethod.GET)
    public ResponseEntity<String> pollStateServiceInstance(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity, //notwendig? fuer request-tracing
            @PathVariable("instance_id") String instance_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id,
            @RequestParam(name = "operation") String operation
    ) {
        return null;
    }

    @RequestMapping (value = "/v2/service_instances/:instance_id/service_bindings/:binding_id/last_operation", method = RequestMethod.GET)
    public ResponseEntity<String> pollStateServiceBinding(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @PathVariable("binding_id") String binding_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id,
            @RequestParam(name = "operation") String operation
    ) {
        return null;
    }

    @RequestMapping (value = "/v2/service_instances/:instance_id", method = RequestMethod.PUT)
    public ResponseEntity<ProvideRequestBody> provide(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @RequestParam(name = "accepts_incomplete") boolean accepts_incomplete,
            @RequestBody ProvideRequestBody body
    ) {
        return null;
    }

    @RequestMapping (value = "/v2/service_instances/:instance_id", method = RequestMethod.GET)
    public ResponseEntity<FetchResponseBody> fetchServiceInstance(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id
    ) {
        return null;
    }

    @RequestMapping(value = "/v2/service_instances/:instance_id", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateServiceInstance(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @RequestParam(name = "accepts_incomplete") boolean accepts_incomplete,
            @RequestBody UpdateRequestBody body
    ) {
        return null;
    }

    @RequestMapping(value = "/v2/service_instances/:instance_id/service_bindings/:binding_id", method = RequestMethod.PUT)
    public ResponseEntity<String> bindService(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @PathVariable("binding_id") String binding_id,
            @RequestParam(name = "accepts_incomplete") boolean accepts_incomplete,
            @RequestBody BindRequestBody body
    ) {
        return null;
    }

    @RequestMapping(value = "/v2/service_instances/:instance_id/service_bindings/:binding_id", method = RequestMethod.GET)
    public ResponseEntity<String> fetchServiceBinding(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @PathVariable("binding_id") String binding_id,
            @RequestParam(name = "service_id") String service_id, //query string field da rein?
            @RequestParam(name = "plan_id") String plan_id
    ) {
        return null;
    }

    @RequestMapping(value = "/v2/service_instances/:instance_id/service_bindings/:binding_id", method = RequestMethod.DELETE)
    public ResponseEntity<String> unbind(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @PathVariable("binding_id") String binding_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id,
            @RequestParam(name = "accepts_incomplete") boolean accepts_incomplete
    ) {
        return null;
    }

    @RequestMapping(value = "/v2/service_instances/:instance_id", method = RequestMethod.DELETE)
    public ResponseEntity<String> deprovide(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id,
            @RequestParam(name = "accepts_incomplete") boolean accepts_incomplete
    ) {
        return null;
    }



}
