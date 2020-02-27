/**
 * @author Maximilian Fuhrich
 */
package de.thbin.epro.core;

import de.thbin.epro.helm.DeploymentSize;
import de.thbin.epro.helm.HelmDeployer;
import de.thbin.epro.model.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * This class is a REST controller, written according to the Open Service Broker API
 * (see https://github.com/openservicebrokerapi/servicebroker)
 */
@Controller
public class ServiceBrokerImpl {
    /**
     * The catalog which contains the service offerings and plans.
     * @see ServiceCatalog
     */
    ServiceCatalog catalog;

    /**
     * The version of the service broker.
     */
    final String version = "2.14";

    /**
     * A map of the instance_ids and the associated service plans
     */
    HashMap<String, String> instance_ids; //and chosen serviceplan as value
    /**
     * A map of the binding_ids and the associated instance ids
     */
    HashMap<String, String> binding_ids; //and associated instance_id as value

    /**
     * Responsible for helm deployment.
     * @see HelmDeployer
     */
    HelmDeployer helmDeployer;

    /**
     * Constructor for initializing the catalog and the helm deployer.
     */
    public ServiceBrokerImpl() {
        catalog = new ServiceCatalog();
        helmDeployer = new HelmDeployer();
        instance_ids = new HashMap<>();
        binding_ids = new HashMap<>();
    }


    /**
     * Returns the service catalog on request.
     * @param brokerVersionUsed
     * @return ReponseEntity with tha catalog in its body
     */
    @RequestMapping (value="/v2/catalog", method = RequestMethod.GET)
    public ResponseEntity<?> getServiceCatalog(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed
            //@RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            //@RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        return new ResponseEntity<>(catalog.getServices(), HttpStatus.OK);
    }

    /**
     * Returns the state of the last operation of a given instance
     * @param brokerVersionUsed
     * @param instance_id
     * @param service_id
     * @param plan_id
     * @return ResponseEntity with the state of the last operation ("in progress", "succeeded" or "failed")
     */
    @RequestMapping (value = "/v2/service_instances/{instance_id}/last_operation", method = RequestMethod.GET)
    public ResponseEntity<?> pollStateServiceInstance(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            //@RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            //@RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            //@PathParam("instance_id") String instance_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id
            //@RequestParam(name = "operation") String operation
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);



        PollBody responseBody = new PollBody();
        if (!checkInstanceId(instance_id))
            return new ResponseEntity<>("Given instance_id doesn't exist.", HttpStatus.BAD_REQUEST);
        if (!checkServiceId(service_id))
            return new ResponseEntity<>("Given service_id doesn't exist.", HttpStatus.BAD_REQUEST);
        if (!checkPlanId(plan_id))
            return new ResponseEntity<>("Given plan_id doesn't exist.", HttpStatus.BAD_REQUEST);
        if (checkInstanceCorrectPlan(instance_id, plan_id))
            return new ResponseEntity<>("Wrong plan_id for given instance_id", HttpStatus.BAD_REQUEST);
        responseBody.setState("succeeded");
        responseBody.setDescription("not implemented yet");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * Returns the state of the last operation for a given binding id
     * @param brokerVersionUsed
     * @param instance_id
     * @param binding_id
     * @param service_id
     * @param plan_id
     * @return ResponseEntity with the state of the last operation ("in progress", "succeeded" or "failed")
     */
    @RequestMapping (value = "/v2/service_instances/{instance_id}/service_bindings/{binding_id}/last_operation", method = RequestMethod.GET)
    public ResponseEntity<?> pollStateServiceBinding(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            //@RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            //@RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            //@PathParam("instance_id") String instance_id,
            @PathVariable("binding_id") String binding_id,
            //@PathParam("binding_id") String binding_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id
            //@RequestParam(name = "operation") String operation
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);

        if (!checkInstanceId(instance_id))
            return new ResponseEntity<>("Given instance_id doesn't exist.", HttpStatus.BAD_REQUEST);
        if (!checkBindingId(binding_id))
            return new ResponseEntity<>("Given binding_id doesn't exist.", HttpStatus.BAD_REQUEST);
        if (!checkBindingCorrectInstance(binding_id, instance_id))
            return new ResponseEntity<>("The given binding is no binding for the given instance", HttpStatus.BAD_REQUEST);

        PollBody responseBody = new PollBody();
        responseBody.setState("succeeded");
        responseBody.setDescription("not implemented yet");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * Creates a service instance with the given id.
     * @param brokerVersionUsed
     * @param instance_id
     * @param body
     * @return ResponseEntity with information about the creation of the service instance
     */
    @RequestMapping (value = "/v2/service_instances/{instance_id}", method = RequestMethod.PUT)
    public ResponseEntity<?> provide(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            //@RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            //@RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            //@PathParam("instance_id") String instance_id,
            //@RequestParam(name = "accepts_incomplete") boolean accepts_incomplete,
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
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        try{
            switch (body.getPlan_id()){
                case ("EproSmP"):
                    helmDeployer.deployRedis(DeploymentSize.SMALL, instance_id);
                    break;
                case ("EproSdP"):
                    helmDeployer.deployRedis(DeploymentSize.STANDARD, instance_id);
                    break;
                case ("EproClP"):
                    helmDeployer.deployRedis(DeploymentSize.CLUSTER, instance_id);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        instance_ids.put(instance_id, body.getPlan_id());
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    /**
     * Returns information about a given service instance
     * @param brokerVersionUsed
     * @param instance_id
     * @return ResponseEntity with service_id and plan_id associated with the service instance
     */
    @RequestMapping (value = "/v2/service_instances/{instance_id}", method = RequestMethod.GET)
    public ResponseEntity<?> fetchServiceInstance(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            //@RequestHeader("X-Broker-API-Originating-Identity") String originIdentity,
            //@RequestHeader("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id
            //@PathParam("instance_id") String instance_id
            //@RequestParam(name = "service_id") String service_id,
            //@RequestParam(name = "plan_id") String plan_id
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        if (!checkInstanceId(instance_id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        FetchResponseBody responseBody = new FetchResponseBody();
        responseBody.setService_id(catalog.getServices()[0].getId());
        responseBody.setPlan_id(instance_ids.get(instance_id));
        return new ResponseEntity<>(responseBody, HttpStatus.OK);

    }

    /**
     * Updates a service instance
     * @param brokerVersionUsed
     * @param instance_id
     * @param body
     * @return ResponseEntity with information about the update
     */
    @RequestMapping(value = "/v2/service_instances/{instance_id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateServiceInstance(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            //@RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            //@RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            //@PathParam("instance_id") String instance_id,
            //@RequestParam(name = "accepts_incomplete") boolean accepts_incomplete,
            @RequestBody UpdateRequestBody body
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        if (!checkInstanceId(instance_id))
            return new ResponseEntity<>("Given instance_id doesn't exist.", HttpStatus.BAD_REQUEST);
        if (!catalog.getServices()[0].getId().equals(body.getService_id()))
            return new ResponseEntity<>("This service_id does not exist", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Creates a binding for an existing service instance on the given binding_id
     * @param brokerVersionUsed
     * @param instance_id
     * @param binding_id
     * @param body
     * @return ResponseEntity with the credentials of the chosen service
     */
    @RequestMapping(value = "/v2/service_instances/{instance_id}/service_bindings/{binding_id}", method = RequestMethod.PUT)
    public ResponseEntity<?> bindService(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            //@RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            //@RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            //@PathParam("instance_id") String instance_id,
            @PathVariable("binding_id") String binding_id,
            //@PathParam("binding_id") String binding_id,
            //@RequestParam(name = "accepts_incomplete") boolean accepts_incomplete,
            @RequestBody BindRequestBody body
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        BindResponseBody responseBody = new BindResponseBody();
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

        binding_ids.put(binding_id, instance_id);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);

    }

    /**
     * Returns information about a service binding
     * @param brokerVersionUsed
     * @param instance_id
     * @param binding_id
     * @return ResponseEntity with the credentials of the binding
     */
    @RequestMapping(value = "/v2/service_instances/{instance_id}/service_bindings/{binding_id}", method = RequestMethod.GET)
    public ResponseEntity<?> fetchServiceBinding(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            //@RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            //@RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            //@PathVariable("instance_id") String instance_id,
            @PathParam("instance_id") String instance_id,
            //@PathVariable("binding_id") String binding_id,
            @PathParam("binding_id") String binding_id
            //@RequestParam(name = "service_id") String service_id, //query string field da rein?
            //@RequestParam(name = "plan_id") String plan_id
    ) {
        //not implemented yet
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);
        if (!checkInstanceId(instance_id))
            return new ResponseEntity<>("Given instance_id doesn't exist.", HttpStatus.NOT_FOUND);
        if (!checkBindingId(binding_id))
            return new ResponseEntity<>("Given binding_id doesn't exist.", HttpStatus.NOT_FOUND);
        if (!checkBindingCorrectInstance(binding_id, instance_id))
            return new ResponseEntity<>("The given binding is no binding for the given instance", HttpStatus.NOT_FOUND);
        FetchBindResponseBody responseBody = new FetchBindResponseBody();

        return new ResponseEntity<>(responseBody, HttpStatus.OK);

    }

    /**
     * Deletes a binding of an existing service.
     * @param brokerVersionUsed
     * @param instance_id
     * @param binding_id
     * @param service_id
     * @param plan_id
     * @return ResponseEntity with information about the unbind
     */
    @RequestMapping(value = "/v2/service_instances/{instance_id}/service_bindings/{binding_id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> unbind(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            //@RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            //@RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            //@PathParam("instance_id") String instance_id,
            @PathVariable("binding_id") String binding_id,
            //@PathParam("binding_id") String binding_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id
            //@RequestParam(name = "accepts_incomplete") boolean accepts_incomplete
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);

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
        try {
            helmDeployer.uninstallService(instance_id);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        binding_ids.remove(binding_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Uninstalls an existing service instance
     * @param brokerVersionUsed
     * @param instance_id
     * @param service_id
     * @param plan_id
     * @return ResponseEntity with information about the uninstall request
     */
    @RequestMapping(value = "/v2/service_instances/{instance_id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deprovide(
            @RequestHeader("X-Broker-API-Version") String brokerVersionUsed,
            //@RequestHeader ("X-Broker-API-Originating-Identity") String originIdentity,
            //@RequestHeader ("X-Broker-API-Request-Identity") String requestIdentity,
            @PathVariable("instance_id") String instance_id,
            //@PathParam("instance_id") String instance_id,
            @RequestParam(name = "service_id") String service_id,
            @RequestParam(name = "plan_id") String plan_id
            //@RequestParam(name = "accepts_incomplete") boolean accepts_incomplete
    ) {
        if (brokerVersionUsed == null)
            return new ResponseEntity<>("Error: Header needs to contain a version number.", HttpStatus.BAD_REQUEST);
        if (!brokerVersionUsed.equals(version))
            return new ResponseEntity<>("Error: Wrong version used. This broker uses version %s.%n", HttpStatus.PRECONDITION_FAILED);

        if (!checkInstanceId(instance_id))
            return new ResponseEntity<>(HttpStatus.GONE);
        if (!checkServiceId(service_id))
            return new ResponseEntity<>("Invalid service_id", HttpStatus.BAD_REQUEST);
        if (!checkInstanceCorrectPlan(instance_id, plan_id))
            return new ResponseEntity<>("This service instance does not use the given plan", HttpStatus.BAD_REQUEST);

        removeBindingsByInstance(instance_id);
        instance_ids.remove(instance_id);
        return new ResponseEntity<>(HttpStatus.OK);

    }


    /**
     * Checks if the instance id exists.
     * @param instance_id
     * @return True if it exists, false otherwise
     */
    boolean checkInstanceId(String instance_id){
        if (instance_ids.containsKey(instance_id))
            return true;
        return false;
    }

    /**
     * Checks if the binding id exists
     * @param binding_id
     * @return True if it exists, false otherwise.
     */
    boolean checkBindingId(String binding_id){
        if (binding_ids.containsKey(binding_id))
            return true;
        return false;
    }

    /**
     * Checks if the binding id is associated with the instance id
     * @param binding_id
     * @param instance_id
     * @return True if it is associated, false otherwise
     */
    boolean checkBindingCorrectInstance(String binding_id, String instance_id){
        if (binding_ids.get(binding_id).equals(instance_id))
            return true;
        return false;
    }

    /**
     * Checks if the instance is associated with the plan id
     * @param service_id
     * @param plan_id
     * @return True if it is associated, false otherwise
     */
    boolean checkInstanceCorrectPlan(String service_id, String plan_id){
        if (instance_ids.get(service_id).equals(plan_id))
            return true;
        return false;
    }

    /**
     * Checks if the service id exists in the catalog
     * @param service_id
     * @return True if it exists, false otherwise
     */
    boolean checkServiceId(String service_id){
        ServiceOffering[] catalogArray = catalog.getServices();
        if (catalogArray[0].getId().equals(service_id))
            return true;
        return false;
    }

    /**
     * Checks if the plan id exists in the catalog
     * @param plan_id
     * @return True if it exists, false otherwise
     */
    boolean checkPlanId(String plan_id){
        ServiceOffering[] catalogArray = catalog.getServices();
        ArrayList<ServicePlan> plans = catalogArray[0].getPlans();
        for (ServicePlan plan : plans){
            if (plan.getId().equals(plan_id))
                return true;
        }
        return false;
    }

    /**
     * Removes all binding ids associated to the service instance
     * @param instance_id
     */
    void removeBindingsByInstance(String instance_id){
        binding_ids.values().removeAll(Collections.singleton(instance_id));
    }

}
