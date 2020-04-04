package serializers;

import sample.MainScreen;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class XML implements Serializable {
    public static void serialize(String filename){
        XMLEncoder encoder=null;
        try{
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)));
        } catch(FileNotFoundException fileNotFound){
            File dir1 = new File(filename);
        }
        for (int i = 0; i < MainScreen.storage.size(); i++)
            encoder.writeObject(MainScreen.storage.get(i));
        encoder.flush();
        encoder.close();
    }

    public static void deserialize(String filename){
        MainScreen.storage.clear();
        XMLDecoder decoder=null;
        try {
            decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
            while (true){
                System.out.println(MainScreen.storage);
                Object obj = decoder.readObject();
                MainScreen.storage.add(obj);
            }
        } catch (FileNotFoundException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
