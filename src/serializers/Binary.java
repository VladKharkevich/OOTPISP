package serializers;

import sample.MainScreen;

import java.io.*;

public class Binary implements Serializable{

    public static void serialize(String filename){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            File dir1 = new File(filename);
            e.printStackTrace();
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(fos);
            for (int i = 0; i < MainScreen.storage.size(); i++)
                oos.writeObject(MainScreen.storage.get(i));
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deserialize(String filename){
        System.out.println(filename);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream oin = null;
        MainScreen.storage.clear();
        try {
            oin = new ObjectInputStream(fis);
            while (true)
                MainScreen.storage.add(oin.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
