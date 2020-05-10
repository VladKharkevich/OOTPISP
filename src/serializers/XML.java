package serializers;

import sample.MainScreen;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

import static pluginUtils.PluginUtils.*;

public class XML implements Serializable {

    static final String attribute = "<?xml";

    public static void serialize(String filename, Class plugCls){
        String tempFilename = filename;
        if (plugCls != null) {
            filename = "temp";
        }
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)));
        } catch (FileNotFoundException fileNotFound) {
            File dir1 = new File(filename);
        }
        for (int i = 0; i < MainScreen.storage.size(); i++)
            encoder.writeObject(MainScreen.storage.get(i));
        encoder.flush();
        encoder.close();
        if (plugCls != null){
            plugSerialize(tempFilename, plugCls);
        }
    }

    public static void deserialize(String filename){
        String plug = checkPlugin(filename, attribute);
        if (plug != null){
            plugDeserialize(filename, plug);
            filename = "temp";
        }
        MainScreen.storage.clear();
        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
            while (true) {
                Object obj = decoder.readObject();
                MainScreen.storage.add(obj);
            }
        } catch (FileNotFoundException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        if (plug != null){
            File temp = new File("temp");
            temp.delete();
        }
    }
}
