package de.thbin.epro.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class InputParameterSchema {

    // JSON schema object
    private JSONObject parameters;


    // CONSTRUCTOR

    public InputParameterSchema(String schema) {
        try {
            InputStream schemaInput = new FileInputStream("src/main/java/ServiceSchema.json");
            parameters = new JSONObject(new JSONTokener(schemaInput));

            JSONArray js = (JSONArray) parameters.get("services");
            JSONObject subjects = (JSONObject) js.get(0);   // content of services
            JSONArray plans = (JSONArray) subjects.get(("plans")); // 0 small - 1 standard - 2 cluster
            subjects = (JSONObject) plans.get(0);
            subjects = (JSONObject) subjects.get("schemas");

            switch ((schema)) {
                case "si_create":
                    subjects = (JSONObject) subjects.get("service_instance");
                    subjects = (JSONObject) subjects.get("create");
                    break;
                case "si_update":
                    subjects = (JSONObject) subjects.get("service_instance");
                    subjects = (JSONObject) subjects.get("update");
                    break;
                case "sb":
                    subjects = (JSONObject) subjects.get("service_binding");
                    subjects = (JSONObject) subjects.get("create");
                    break;
            }
            parameters = subjects;

        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }
    }







    //IGNORE - TESTING ZONE//

/*    public static void main(String[] args) {
        try (InputStream inputStream = getClass().getResourceAsStream("/path/to/your/schema.json")) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(new JSONObject("{\"hello\" : \"world\"}")); // throws a ValidationException if this object is invalid
        }
    }
*/


/*public static void main(final String[]args) throws IOException {
        InputStream rootInput=new FileInputStream("model/src/main/java/de/thbin/epro/model/ServiceSchema.json");

        JSONObject root=new JSONObject(
        new JSONTokener(rootInput));

       // InputStream schemaInput=new FileInputStream("src/main/java/perftest.json");//schema-draft4.json");

       // JSONObject schemaObject=new JSONObject(
       // new JSONTokener(schemaInput));
        //Schema schema=SchemaLoader.load(schemaObject);
        //System.out.println(schema.getDescription());

        JSONArray js = (JSONArray) root.get("services");
        JSONObject subjects= (JSONObject) js.get(0);
        JSONArray plans = (JSONArray) subjects.get(("plans"));
        subjects = (JSONObject) plans.get(0);
        subjects = (JSONObject) subjects.get("schemas");
        subjects = (JSONObject) subjects.get("service_instance");
        subjects = (JSONObject) subjects.get("create");
    System.out.println(subjects);

        /*
        JSONObject subjects=root.getJSONObject("small"); //schemas

        String[]names=JSONObject.getNames(subjects);
        System.out.println("Now test overit...");
        long startAt=System.currentTimeMillis();
        for(int i=0;i< 500;++i){
        for(String subjectName:names){
        JSONObject subject=(JSONObject)subjects.get(subjectName);
        //schema.validate(subject);
        }
        if(i%20==0){
        System.out
        .println("Iteration "+i+" (in "+(System.currentTimeMillis()-startAt)+"ms)");
        }
        }
        long endAt=System.currentTimeMillis();
        long execTime=endAt-startAt;
        System.out.println("total time: "+execTime+" ms");

        }

*/
}
