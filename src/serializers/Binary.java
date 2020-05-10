package serializers;

import sample.MainScreen;

import java.io.*;

import static pluginUtils.PluginUtils.*;

public class Binary implements Serializable{

    static final byte[] attribute = {-84, -19};

    public static void serialize(String filename, Class plugCls){
        String tempFilename = filename;
        if (plugCls != null) {
            filename = "temp";
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
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
        if (plugCls != null){
            plugSerialize(tempFilename, plugCls);
        }
    }

    public static void deserialize(String filename) {
        String plug = checkPlugin(filename, attribute);
        if (plug != null){
            plugDeserialize(filename, plug);
            filename = "temp";
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (plug != null){
            File temp = new File("temp");
            temp.delete();
        }
    }

}
