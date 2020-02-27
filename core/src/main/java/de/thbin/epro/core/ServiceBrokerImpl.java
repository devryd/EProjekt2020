package de.thbin.epro.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbin.epro.model.*;
import helm.HelmDeployer;
import io.fabric8.kubernetes.api.model.WatchEventFluent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    //HelmDeployer depl;
    //ersetzen durch ServiceCatalog
    ServiceCatalog catalog;// = new ServiceCatalog();

    final String version = "2.14";

    Map<String, String> instance_ids; //and chosen serviceplan as value
    Map<String, String> binding_ids; //and associated instance_id as value
    //macht michael mapper ggf nicht notwendig
    /*
    stattdessen jsonobject?
    oder ganze klasse außen rum statt array?
     */
    //@Autowired
    HelmDeployer helmDeployer;
    public ServiceBrokerImpl() {
        /*ObjectMapper mapper = new ObjectMapper();
        catalog = mapper.readValue(new File("../resources/ServiceSchema.json"), ServiceOffering[].class);
        */
        //@autowired michael?
        System.out.println("gude");
        catalog = null;
        catalog = new ServiceCatalog();
        helmDeployer = new HelmDeployer();
        //catalog = serviceCatalog.getServices();
        //geht noch nicht, weil servicecatalog im falschen package
    }


    @RequestMapping (value="/v2/catalog", method = RequestMethod.GET)
    //@ResponseBody entweder das oder responseentity, ansonsten doppelt gemoppelt
    public ResponseEntity<?> getServiceCatalog(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed
            //@RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            //@RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity //notwendig? fuer request-tracing
            ) {
        /*ResponseEntity<String> response;
        response.*/
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        //return new ResponseEntity<>(dog, HttpStatus.OK);
        //if (catalog == null)
            //System.out.printf("Catalog is empty");
        //System.out.printf(String.valueOf(catalog.getServices().length));
        return new ResponseEntity<>(catalog.getServices(), HttpStatus.OK);
    }

    @RequestMapping (value = "/v2/service_instances/:instance_id/last_operation", method = RequestMethod.GET)
    public ResponseEntity<?> pollStateServiceInstance(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            //@RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            //@RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity, //notwendig? fuer request-tracing
            //@PathVariable("instance_id") String instance_id,
            @PathParam("instance_id") String instance_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id
            //@RequestParam(name = "operation") String operation
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);



        PollBody responseBody = new PollBody();
        //if abfragen...
        //bad request und gone
        if (!checkInstanceId(instance_id))
            return new ResponseEntity<>("Given instance_id doesn't exist.", HttpStatus.BAD_REQUEST);
        //optional anfang
        if (!checkServiceId(service_id))
            return new ResponseEntity<>("Given service_id doesn't exist.", HttpStatus.BAD_REQUEST);
        if (!checkPlanId(plan_id))
            return new ResponseEntity<>("Given plan_id doesn't exist.", HttpStatus.BAD_REQUEST);
        //check if given plan_id
        //optional ende
        //success, noch die zwei anderen states sinnvoll, dafür anfrage an jannik
        responseBody.setState("succeeded");
        responseBody.setDescription("not implemented yet");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @RequestMapping (value = "/v2/service_instances/:instance_id/service_bindings/:binding_id/last_operation", method = RequestMethod.GET)
    public ResponseEntity<?> pollStateServiceBinding(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            //@RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            //@RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            //@PathVariable("instance_id") String instance_id,
            @PathParam("instance_id") String instance_id,
            //@PathVariable("binding_id") String binding_id,
            @PathParam("binding_id") String binding_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id
            //@RequestParam(name = "operation") String operation
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);

        //if abfragen...
        if (!checkInstanceId(instance_id))
            return new ResponseEntity<>("Given instance_id doesn't exist.", HttpStatus.BAD_REQUEST);
        if (!checkBindingId(binding_id))
            return new ResponseEntity<>("Given binding_id doesn't exist.", HttpStatus.BAD_REQUEST);
        if (!checkBindingCorrectInstance(binding_id, instance_id))
            return new ResponseEntity<>("The given binding is no binding for the given instance", HttpStatus.BAD_REQUEST);
        //optional, see method above
        //gone
        //success
        PollBody responseBody = new PollBody(); //pollbody aus servicebinding ok?
        //state von jannik holen
        responseBody.setState("succeeded");
        responseBody.setDescription("not implemented yet");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @RequestMapping (value = "/v2/service_instances/:instance_id", method = RequestMethod.PUT)
    public ResponseEntity<?> provide(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            //@PathVariable("instance_id") String instance_id,
            @PathParam("instance_id") String instance_id,
            @RequestParam(name = "accepts_incomplete") boolean accepts_incomplete,
            @RequestBody ProvideRequestBody body

    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);


        if (!checkServiceId(body.getService_id()))
            return new ResponseEntity<>("Given service_id doesn't exist.", HttpStatus.BAD_REQUEST);
        if (!checkPlanId(body.getPlan_id()))
            return new ResponseEntity<>("Given plan_id doesn't exist.", HttpStatus.BAD_REQUEST);
        if (checkInstanceId(instance_id)){
            if (body.getPlan_id().equals(instance_ids.get(instance_id))){
                //anfrage zustand jannik
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        //erzeuge neue instanz (anfrage jannik)
        switch (body.getPlan_id()){
            case (""):
                    break;
            case ("mittel"):
                    break;
            case ("gross"):
                    break;
        }
        ////ProvideResponseBody nötig bzw sinnvolle attribute dafür?
        instance_ids.put(instance_id, body.getPlan_id());
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @RequestMapping (value = "/v2/service_instances/:instance_id", method = RequestMethod.GET)
    public ResponseEntity<?> fetchServiceInstance(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader("X-Broker-API-Request-Identity") String requestIdentity,
            //@PathVariable("instance_id") String instance_id,
            @PathParam("instance_id") String instance_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        //if instance_id provisioned
        if (!checkInstanceId(instance_id)){ //oder in progress, jannik anfragen
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        FetchResponseBody responseBody = new FetchResponseBody();
        responseBody.setService_id(catalog.getServices()[0].getId());
        responseBody.setPlan_id(instance_ids.get(instance_id));
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
        //else...

    }

    @RequestMapping(value = "/v2/service_instances/:instance_id", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateServiceInstance(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            //@PathVariable("instance_id") String instance_id,
            @PathParam("instance_id") String instance_id,
            @RequestParam(name = "accepts_incomplete") boolean accepts_incomplete,
            @RequestBody UpdateRequestBody body
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        //falsche eingabe status code
        //update in progress code
        if (!checkInstanceId(instance_id))
            return new ResponseEntity<>("Given instance_id doesn't exist.", HttpStatus.BAD_REQUEST);
        if (!catalog.getServices()[0].getId().equals(body.getService_id()))
            return new ResponseEntity<>("This service_id does not exist", HttpStatus.BAD_REQUEST);
        //jannik nach provision state fragen, und ggf 202 Accepted returnen
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/v2/service_instances/:instance_id/service_bindings/:binding_id", method = RequestMethod.PUT)
    public ResponseEntity<?> bindService(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            //@PathVariable("instance_id") String instance_id,
            @PathParam("instance_id") String instance_id,
            //@PathVariable("binding_id") String binding_id,
            @PathParam("binding_id") String binding_id,
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
        //responseBody.setCredentials(); anfrage jannik
        if (!checkInstanceId(instance_id))
            return new ResponseEntity<>("Given instance_id doesn't exist.", HttpStatus.BAD_REQUEST);
        if (!checkServiceId(body.getService_id()))
            return new ResponseEntity<>("Invalid service_id", HttpStatus.BAD_REQUEST);
        if (!checkPlanId(body.getPlan_id()))
            return new ResponseEntity<>("Invalid plan_id", HttpStatus.BAD_REQUEST);
        if (checkBindingId(binding_id)){
            if (body.getPlan_id().equals(instance_ids.get(instance_id))){
                return new ResponseEntity<>(responseBody, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
            //return new ResponseEntity<>("Given binding_id doesn't exist.", HttpStatus.BAD_REQUEST);
        //if (!checkBindingCorrectInstance(binding_id, instance_id))
            //return new ResponseEntity<>("The given binding is no binding for the given instance", HttpStatus.BAD_REQUEST);

        //if binding vorhanden
        binding_ids.put(binding_id, instance_id);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED); //so, oder nur credentials
        //if binding noch nicht vorhanden 201

    }

    @RequestMapping(value = "/v2/service_instances/:instance_id/service_bindings/:binding_id", method = RequestMethod.GET)
    public ResponseEntity<?> fetchServiceBinding(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            //@PathVariable("instance_id") String instance_id,
            @PathParam("instance_id") String instance_id,
            //@PathVariable("binding_id") String binding_id,
            @PathParam("binding_id") String binding_id,
            @RequestParam(name = "service_id") String service_id, //query string field da rein?
            @RequestParam(name = "plan_id") String plan_id
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        //if instance id und service id vorhanden
        if (!checkInstanceId(instance_id))
            return new ResponseEntity<>("Given instance_id doesn't exist.", HttpStatus.NOT_FOUND);
        if (!checkBindingId(binding_id))
            return new ResponseEntity<>("Given binding_id doesn't exist.", HttpStatus.NOT_FOUND);
        if (!checkBindingCorrectInstance(binding_id, instance_id))
            return new ResponseEntity<>("The given binding is no binding for the given instance", HttpStatus.NOT_FOUND);
        FetchBindResponseBody responseBody = new FetchBindResponseBody();
        //responseBody.setCredentials(); jannik anfragen

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
        //bed nicht erfüllt
        //return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/v2/service_instances/:instance_id/service_bindings/:binding_id", method = RequestMethod.DELETE)
    public ResponseEntity<?> unbind(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            //@PathVariable("instance_id") String instance_id,
            @PathParam("instance_id") String instance_id,
            //@PathVariable("binding_id") String binding_id,
            @PathParam("binding_id") String binding_id,
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
        if (!checkBindingId(binding_id))
            return new ResponseEntity<>("Given binding_id does not exist.", HttpStatus.GONE);
        if (!checkInstanceId(instance_id))
            return new ResponseEntity<>("Given instance_id does not exist", HttpStatus.GONE);
        if (!checkBindingCorrectInstance(binding_id, instance_id))
            return new ResponseEntity<>("The given binding is no binding for the given instance", HttpStatus.GONE);
        if (!checkServiceId(service_id))
            return new ResponseEntity<>("Invalid service_id", HttpStatus.BAD_REQUEST);
        if (!checkInstanceCorrectPlan(instance_id, plan_id)){
            return new ResponseEntity<>("This service instance does not use the given plan", HttpStatus.BAD_REQUEST);
        }
        //unbind state anfrage jannik
        //unbind anfrage jannik
        //binding aus map entfernen
        binding_ids.remove(binding_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/v2/service_instances/:instance_id", method = RequestMethod.DELETE)
    public ResponseEntity<?> deprovide(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            @RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            @RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            //@PathVariable("instance_id") String instance_id,
            @PathParam("instance_id") String instance_id,
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
        if (!checkInstanceId(instance_id))
            return new ResponseEntity<>(HttpStatus.GONE);
        if (!checkServiceId(service_id))
            return new ResponseEntity<>("Invalid service_id", HttpStatus.BAD_REQUEST);
        if (!checkInstanceCorrectPlan(instance_id, plan_id))
            return new ResponseEntity<>("This service instance does not use the given plan", HttpStatus.BAD_REQUEST);
        //if deletion am laufen 202 accepted anfrage jannik
        //ansonsten uninstall anfrage stellen

        removeBindingsByInstance(instance_id);
        instance_ids.remove(instance_id);
        return new ResponseEntity<>(HttpStatus.OK);

    }


    boolean checkInstanceId(String instance_id){
        //übergibt plattform ne service_id und plan_id?
        if (instance_ids.containsKey(instance_id))
            return true;
        return false;
    }

    boolean checkBindingId(String binding_id){
        if (binding_ids.containsKey(binding_id))
            return true;
        return false;
    }
    boolean checkBindingCorrectInstance(String binding_id, String instance_id){
        if (binding_ids.get(binding_id).equals(instance_id))
            return true;
        return false;
    }
    boolean checkInstanceCorrectPlan(String service_id, String plan_id){
        if (instance_ids.get(service_id).equals(plan_id))
            return true;
        return false;
    }
    boolean checkServiceId(String service_id){
        ServiceOffering[] catalogArray = catalog.getServices();
        if (catalogArray[0].getId().equals(service_id))
            return true;
        return false;
    }
    boolean checkPlanId(String plan_id){
        ServiceOffering[] catalogArray = catalog.getServices();
        ArrayList<ServicePlan> plans = catalogArray[0].getPlans();
        for (ServicePlan plan : plans){
            if (plan.getId().equals(plan_id))
                return true;
        }
        return false;
    }
    void removeBindingsByInstance(String instance_id){
        binding_ids.values().removeAll(Collections.singleton(instance_id));
    }

}
