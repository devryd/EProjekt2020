package de.thbin.epro.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * ServiceCatalog reads all services, service plans and all information from the ServiceSchema.json by just making an instance of ServiceCatalog.
 * Itself contains only a array of ServiceOffering, which contains the services.
 * @author Michael Kunz
 * */
public class ServiceCatalog {

    // must have

    private ServiceOffering[] services;

    // CONSTRUCTOR

    public ServiceCatalog(){
        try {
            services = new ServiceOffering[1];


            // InputStream schemaInput = new FileInputStream("model/src/main/java/de/thbin/epro/model/ServiceSchema.json");
            JSONObject parameters = null;// = new JSONObject(new JSONTokener("hjkl"));

            // reading the json file for collecting all informations
            FileInputStream stream = new FileInputStream(new File("model/src/main/java/de/thbin/epro/model/ServiceSchema.json"));
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                parameters =new JSONObject(Charset.defaultCharset().decode(bb).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //entering the json file
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

            services[0] = new ServiceOffering(name, id, description, bindable, planParameter);

            /*
            // test
            System.out.println(services[0].getId());
            System.out.println(services[0].getName());
            System.out.println(services[0].getPlans().get(1).getId());
            System.out.println(services[0].getPlans().get(1).getName());
            System.out.println(services[0].getPlans().get(1).getDescription());
            System.out.println(services[0].getPlans().get(1).getSchemas().getService_binding().getCreate().getParameters().toString());
            */

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
