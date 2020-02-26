package de.thbin.epro.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbin.epro.model.*;
import io.fabric8.kubernetes.api.model.WatchEventFluent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
/*
Fragen:
json datei an object mappen?
welche version x-broker-api? 2.14 oder egal?
final string version?
default werte für die ganzen klassen? also wenn zb nicht alle sachen im requestbody angegeben
weitere @bean bzw @service?
kann ich den requestheader früher abfangen, oder muss ich den in jeder methode prüfen?
wenn requestheader nich vorhanden, inhalt = "" oder inhalt = null?
responseentity<serviceoffering[]> notwendig oder geht auch responseentity<object>, sprich reflection?
bzw sinnvoller body bei ungültiger anfrage?
ResponseEntity<> klammer leer lassen bzw lambda operator
bel. viele service instanzen
binding liefert credentials/host zurück
schema geben vor welche inputs man definieren kann
schema in json datei nicht zwingend notwendig

 */

@Controller
public class ServiceBrokerImpl { //implements de.thbin.epro.core.ServiceBrokerInterface

    //ersetzen durch ServiceCatalog
    ServiceCatalog catalog;

    final String version = "2.14";

    //macht michael mapper ggf nicht notwendig
    /*
    stattdessen jsonobject?
    oder ganze klasse außen rum statt array?
     */
    public ServiceBrokerImpl() {
        /*ObjectMapper mapper = new ObjectMapper();
        catalog = mapper.readValue(new File("../resources/ServiceSchema.json"), ServiceOffering[].class);
        */
        catalog = new ServiceCatalog();
        //catalog = serviceCatalog.getServices();
        //geht noch nicht, weil servicecatalog im falschen package
    }

    @RequestMapping (value="/v2/catalog", method = RequestMethod.GET)
    //@ResponseBody entweder das oder responseentity, ansonsten doppelt gemoppelt
    public ResponseEntity<?> getServiceCatalog(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity //notwendig? fuer request-tracing
            ) {
        /*ResponseEntity<String> response;
        response.*/
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        return new ResponseEntity<>(catalog.getServices(), HttpStatus.OK);
    }

    @RequestMapping (value = "/v2/service_instances/:instance_id/last_operation", method = RequestMethod.GET)
    public ResponseEntity<?> pollStateServiceInstance(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity, //notwendig? fuer request-tracing
            @PathVariable("instance_id") String instance_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id,
            @RequestParam(name = "operation") String operation
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);

        PollBody responseBody = new PollBody();
        //if abfragen...
        //bad request und gone
        //success
        responseBody.setState("succeeded");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @RequestMapping (value = "/v2/service_instances/:instance_id/service_bindings/:binding_id/last_operation", method = RequestMethod.GET)
    public ResponseEntity<?> pollStateServiceBinding(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @PathVariable("binding_id") String binding_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id,
            @RequestParam(name = "operation") String operation
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        PollBody responseBody = new PollBody(); //pollbody aus servicebinding ok?
        //if abfragen...
        //bad request und gone
        //success
        responseBody.setState("succeeded");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @RequestMapping (value = "/v2/service_instances/:instance_id", method = RequestMethod.PUT)
    public ResponseEntity<?> provide(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @RequestParam(name = "accepts_incomplete") boolean accepts_incomplete,
            @RequestBody ProvideRequestBody body

    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);

        //wenn keine fehler und noch nicht vorhanden
        //erzeuge neue instans (anfrage jannik)
        //ProvideResponseBody nötig bzw sinnvolle attribute dafür?
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @RequestMapping (value = "/v2/service_instances/:instance_id", method = RequestMethod.GET)
    public ResponseEntity<?> fetchServiceInstance(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        //if instance_id provisioned
        FetchResponseBody responseBody = new FetchResponseBody();
        //attribute festlegen
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
        //else...

    }

    @RequestMapping(value = "/v2/service_instances/:instance_id", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateServiceInstance(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @RequestParam(name = "accepts_incomplete") boolean accepts_incomplete,
            @RequestBody UpdateRequestBody body
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        //falsche eingabe status code
        //update in progress code
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/v2/service_instances/:instance_id/service_bindings/:binding_id", method = RequestMethod.PUT)
    public ResponseEntity<?> bindService(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @PathVariable("binding_id") String binding_id,
            @RequestParam(name = "accepts_incomplete") boolean accepts_incomplete,
            @RequestBody BindRequestBody body
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        //return null;
        //if in progress 202
        //if invalid data 400
        //parameter in schema vergleichen?
        //if service binding id unter der instance id vorhanden, aber mit anderen werten 409
        //422 Unprocessable Entity
        BindResponseBody responseBody = new BindResponseBody();
        //if binding vorhanden
        //responseBody.setCredentials();
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
        //if binding noch nicht vorhanden 201

    }

    @RequestMapping(value = "/v2/service_instances/:instance_id/service_bindings/:binding_id", method = RequestMethod.GET)
    public ResponseEntity<?> fetchServiceBinding(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @PathVariable("binding_id") String binding_id,
            @RequestParam(name = "service_id") String service_id, //query string field da rein?
            @RequestParam(name = "plan_id") String plan_id
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        //if instance id und service id vorhanden
        FetchBindResponseBody responseBody = new FetchBindResponseBody();
        //responseBody.setCredentials();
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
        //bed nicht erfüllt
        //return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/v2/service_instances/:instance_id/service_bindings/:binding_id", method = RequestMethod.DELETE)
    public ResponseEntity<?> unbind(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @PathVariable("binding_id") String binding_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id,
            @RequestParam(name = "accepts_incomplete") boolean accepts_incomplete
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        //if invalid data 400 bad request
        //if... 422 Unprocessable Entity
        //if binding id nicht vorhanden 410 Gone
        //if unbinding am laufen 202 Accepted
        //ansonsten: unbind anfrage schicken
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/v2/service_instances/:instance_id", method = RequestMethod.DELETE)
    public ResponseEntity<?> deprovide(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id,
            @RequestParam(name = "accepts_incomplete") boolean accepts_incomplete
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        //if invalid data 400 bad request
        //if... 422 unprocessable entity
        //if instance id nicht vorhanden 410 gone
        //if deletion am laufen 202 accepted
        //ansonsten uninstall anfrage stellen
        return new ResponseEntity<>(HttpStatus.OK);

    }



}
