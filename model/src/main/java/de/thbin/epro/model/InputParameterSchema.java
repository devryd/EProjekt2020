package de.thbin.epro.model;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class InputParameterSchema {

    // JSON schema object
    private JSONObject parameters;


    // CONSTRUCTOR

    public InputParameterSchema() {
        try {
            InputStream schemaInput = new FileInputStream("src/main/java/ServiceSchema.json");
            parameters = new JSONObject(new JSONTokener(schemaInput));
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


/*public static void main(final String[]args)throws FileNotFoundException{
        InputStream rootInput=new FileInputStream("src/main/java/JsonObject.json");

        JSONObject root=new JSONObject(
        new JSONTokener(rootInput));

        InputStream schemaInput=new FileInputStream("src/main/java/perftest.json");//schema-draft4.json");

        JSONObject schemaObject=new JSONObject(
        new JSONTokener(schemaInput));
        Schema schema=SchemaLoader.load(schemaObject);
        System.out.println(schema.getDescription());

        JSONObject subjects=root.getJSONObject("user"); //schemas
        String[]names=JSONObject.getNames(subjects);
        System.out.println("Now test overit...");
        long startAt=System.currentTimeMillis();
        for(int i=0;i< 500;++i){
        for(String subjectName:names){
        JSONObject subject=(JSONObject)subjects.get(subjectName);
        schema.validate(subject);
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
