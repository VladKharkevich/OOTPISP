package serializers;

import sample.MainScreen;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Text {

    public static void serialize(String filename){
        try {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(filename);
            } catch (FileNotFoundException e) {
                File dir1 = new File(filename);
                try {
                    fos = new FileOutputStream(filename);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
            BufferedOutputStream fileWriter = new BufferedOutputStream(fos);
            int i = 0;
            for (Object terminator : MainScreen.storage) {
                fileWriter.write("{".getBytes());
                Class cls = terminator.getClass();
                ArrayList<Field> fields = getObjectFields(cls);
                String strTerm = terminator.toString();
                strTerm = strTerm.substring(0, strTerm.indexOf("@") + 1).concat(String.valueOf(i));
                fileWriter.write(strTerm.concat(",").getBytes());
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldType = field.getName();
                    fileWriter.write("\"".concat(fieldType).concat("\"").getBytes());
                    fileWriter.write(":".getBytes());
                    Object d = field.get(terminator);
                    if (d == null)
                        fileWriter.write("".getBytes());
                    else if (MainScreen.storage.indexOf(d) != -1) {
                        String strField = d.toString();
                        strField = strField.substring(0, strField.indexOf("@") + 1).concat(String.valueOf(MainScreen.storage.indexOf(d)));
                        fileWriter.write(strField.getBytes());
                    }
                    else
                        fileWriter.write(d.toString().getBytes());
                    if (!field.equals(fields.get(fields.size() - 1)))
                        fileWriter.write(",".getBytes());
                }
                fileWriter.write("}".getBytes());
                if (!terminator.equals(MainScreen.storage.get(MainScreen.storage.size()-1))) {
                    fileWriter.write("\n".getBytes());
                }
                i++;
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Field> getObjectFields(Class cls){
        if (cls.getName().equals(Object.class.getName())) {
            return new ArrayList<>(Arrays.asList(cls.getDeclaredFields()));
        } else {
            ArrayList<Field> result = new ArrayList<>(getObjectFields(cls.getSuperclass()));
            result.addAll(new ArrayList<>(Arrays.asList(cls.getDeclaredFields())));
            return result;
        }
    }

    public static void deserialize(String filename){
        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(filename);
            BufferedInputStream bis;
            bis = new BufferedInputStream(fis);
            byte[] dat = new byte[(int) file.length()];
            bis.read(dat);
            bis.close();
            String strDat = new String(dat, StandardCharsets.UTF_8);
            String[] content = strDat.split("\n");
            String str;
            MainScreen.storage.clear();
            for (int i = 0; i< content.length; i++) {
                MainScreen.storage.add(null);
            }
            ArrayList<Integer> tasks = new ArrayList<>();
            for (int i = 0; i < content.length; i++){
                str = content[i];
                Boolean isCreated = true;
                Map<String, String> lhMap = new LinkedHashMap<>();
                String[] elems = str.substring(1, str.length() - 1).split(",");
                Class cls = Class.forName(elems[0].substring(0, elems[0].indexOf("@")));
                for (int j = 1; j < elems.length; j++){
                    String key = elems[j].substring(1, elems[j].lastIndexOf(":")-1);
                    String value = elems[j].substring(elems[j].lastIndexOf(":") + 1);
                    if (value.contains("@")){
                        value = value.substring(value.lastIndexOf("@") + 1);
                        if (Integer.parseInt(value) > i){
                            tasks.add(i);
                            isCreated = false;
                            break;
                        }
                    }
                    lhMap.put(key, value);
                }
                if (isCreated) {
                    Constructor<?>[] constructors = cls.getConstructors();
                    Object terminator = null;
                    try {
                        if (constructors[0].getParameterCount() > 0)
                            terminator = constructors[0].newInstance(lhMap);
                        else
                            terminator = constructors[1].newInstance(lhMap);
                        MainScreen.storage.set(i, terminator);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            while (tasks.size() > 0){
                for (Integer i: tasks){
                    String[] elems = content[i].substring(1, content[i].length() - 1).split(",");
                    if (canCreate(elems, tasks)){
                        tasks.remove(i);
                        Map<String, String> lhMap = new LinkedHashMap<>();
                        Class cls = Class.forName(elems[0].substring(0, elems[0].indexOf("@")));
                        for (int j = 1; j < elems.length; j++){
                            String key = elems[j].substring(1, elems[j].lastIndexOf(":")-1);
                            String value = elems[j].substring(elems[j].lastIndexOf(":") + 1);
                            if (value.contains("@")){
                                value = value.substring(value.lastIndexOf("@") + 1);
                            }
                            lhMap.put(key, value);
                        }
                        Constructor<?>[] constructors = cls.getConstructors();
                        Object terminator = null;
                        try {
                            if (constructors[0].getParameterCount() > 0)
                                terminator = constructors[0].newInstance(lhMap);
                            else
                                terminator = constructors[1].newInstance(lhMap);
                            MainScreen.storage.set(i, terminator);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Boolean canCreate(String[] elems, ArrayList<Integer> tasks){
        for (int i = 1; i < elems.length; i++){
            if (elems[i].substring(elems[i].lastIndexOf(":") + 1).contains("@")){
                Integer number = Integer.parseInt(elems[i].substring(elems[i].lastIndexOf("@") + 1));
                if (tasks.contains(number)){
                    return false;
                }
            }
        }
        return true;
    }
}
