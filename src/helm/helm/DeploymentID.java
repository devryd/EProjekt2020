package helm;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DeploymentID implements Serializable {
    private AtomicLong id;

    DeploymentID(){
        File file = new File("savedID");
        if(file.exists()){
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                Object idObject = in.readObject();
                id = (AtomicLong) idObject;
                in.close();
            }
            catch(IOException | ClassNotFoundException ex){ ex.printStackTrace() ;};
        }
        else{
            id = new AtomicLong();
        }
    }
    public long getNewID(){
        long toReturn = id.getAndIncrement();
        return toReturn;
    }

    public long getCurrentID(){
        return id.get();
    }

    public void save(){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("savedID"));
            out.writeObject(id);
        }catch(IOException ex){ex.printStackTrace();}
    }
}
