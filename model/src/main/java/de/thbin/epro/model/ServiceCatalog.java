package de.thbin.epro.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class ServiceCatalog {

    // must have

    private ServiceOffering[] services;

    // CONSTRUCTOR

    public ServiceCatalog(){
        try {
            services = new ServiceOffering[1];

            InputStream schemaInput = new FileInputStream("model/src/main/java/de/thbin/epro/model/ServiceSchema.json");
            JSONObject parameters = new JSONObject(new JSONTokener(schemaInput));

            JSONArray js = (JSONArray) parameters.get("services");
            JSONObject subjects = (JSONObject) js.get(0);   // content of services

            // collect all informations of the service offering
            String name = (String) subjects.get("name");
            String id = (String) subjects.get("id");
            String description = (String) subjects.get("description");
            boolean bindable = (boolean) subjects.get("bindable");

            // collect all informations of the service plans
            ArrayList<ServicePlan> planParameter = new ArrayList<>();
            JSONArray plans = (JSONArray) subjects.get(("plans")); // 0 small - 1 standard - 2 cluster

            JSONObject plan = (JSONObject) plans.get(0);
            planParameter.add(new ServicePlan((String) plan.get("id"), (String) plan.get("name"), (String) plan.get("description")));
            plan = (JSONObject) plans.get(1);
            planParameter.add(new ServicePlan((String) plan.get("id"), (String) plan.get("name"), (String) plan.get("description")));
            plan = (JSONObject) plans.get(2);
            planParameter.add(new ServicePlan((String) plan.get("id"), (String) plan.get("name"), (String) plan.get("description")));

            services[0] = new ServiceOffering(name, id, description, bindable, new ArrayList<>());
        }catch (FileNotFoundException | JSONException e){
            e.printStackTrace();
        }
    }

    // GETTER AND SETTER

    public ServiceOffering[] getServices() {
        return services;
    }

    public void setServices(ServiceOffering[] services) {
        this.services = services;
    }

}
