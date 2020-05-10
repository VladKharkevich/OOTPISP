package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pluginUtils.PlugLoader;
import serializers.SupportedFileFormats;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static pluginUtils.PluginUtils.getClassFromPlugins;

public class MainController {
    @FXML
    public ComboBox<String> cbFields;
    public VBox vEditCont;
    public VBox vBackground;
    public ComboBox cbTypeSer;
    public VBox vSerializers;
    public ComboBox cbTypePlug;
    public VBox vPlugins;
    @FXML
    private Button btnCreateObject;

    @FXML
    public ComboBox<String> cbObjects;
    @FXML
    public static Stage mainStage;

    @FXML protected void handleYourButtonAction(ActionEvent event) {
        mainStage.close();
    }

    public void handleOnMouseClicked(MouseEvent mouseEvent) {
        cbObjects.getItems().clear();
        for (Object terminator : MainScreen.storage) {
            Class termClass = terminator.getClass();
            Field field = null;
            try {
                field = termClass.getField("model");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            if(!field.isAccessible()){
                field.setAccessible(true);
            }
            try {
                Object value = field.get(terminator);
                String name = termClass.getName();
                cbObjects.getItems().add(name.substring(name.lastIndexOf('.') + 1) + " (" + value.toString() + " )");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void createNewWindow(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CreateObject/secondwindow.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("second window");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void getFields(ActionEvent actionEvent) throws IllegalAccessException {
        cbFields.getItems().clear();
        Integer index = cbObjects.getSelectionModel().getSelectedIndex();
        if (index != -1){
            vEditCont.getChildren().clear();
            Object terminator = MainScreen.storage.get(index);
            Class c = terminator.getClass();
            Field[] fields = c.getFields();
            for (Field field : fields) {
                cbFields.getItems().add(field.getName());
            }
            String name = cbObjects.getValue();
            FileInputStream input = null;
            try {
                input = new FileInputStream("./src/images/"+ name.substring(0, name.indexOf(' ')) + ".jpeg");
                Image image = new Image(input);
                BackgroundImage backgroundimage = new BackgroundImage(image,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
                Background background = new Background(backgroundimage);
                vBackground.setBackground(background);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                vBackground.setBackground(new Background(new BackgroundFill(Color.web("0x388888"), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    public void deleteObject(ActionEvent actionEvent) {
        Integer index = cbObjects.getSelectionModel().getSelectedIndex();
        MainScreen.storage.remove((int)index);
        cbObjects.getSelectionModel().select(-1);
    }

    public void createEditComp(ActionEvent actionEvent) throws NoSuchFieldException, IllegalAccessException {
        vEditCont.getChildren().removeAll(vEditCont.getChildren());
        Integer index = cbObjects.getSelectionModel().getSelectedIndex();
        Object terminator = MainScreen.storage.get(index);
        Class c = terminator.getClass();
        Field field = c.getField(cbFields.getValue());
        if(!field.isAccessible()){
            field.setAccessible(true);
        }
        Label label = null;
        String value = "";
        if (field.getType().getPackageName().equals("Terminators") && !(field.getType().isEnum())){
            if (MainScreen.storage.contains(field.get(terminator))){
                Field modelField = field.get(terminator).getClass().getField("model");
                String model = modelField.get(field.get(terminator)).toString();
                String className = field.get(terminator).getClass().getName().substring(field.get(terminator).getClass().getName().indexOf('.')+1);
                value = className + " (" + model + ")";
            }
            else {
                String className = field.get(terminator).getClass().getName().substring(field.get(terminator).getClass().getName().indexOf('.')+1);
                value = className + " doesn't exist";
            }
        }
        else {
            value = field.get(terminator).toString();
        }
        label = new Label(value);
        label.setFont(new Font("Arial", 24));
        label.setPadding(new Insets(17, 10, 5, 12));
        vEditCont.getChildren().add(label);

        int modifiers = field.getModifiers();
        if (!Modifier.isFinal(modifiers)) {
            Object comp = null;
            if (field.getType().isEnum()){
                ComboBox<String> comboBox = new ComboBox<>();
                for (Object obj :field.getType().getEnumConstants()){
                    comboBox.getItems().add(obj.toString());
                }
                comboBox.setValue(value);
                vEditCont.getChildren().add(comboBox);
                comp = comboBox;
            }
            else if (field.getType().getPackageName().equals("Terminators")){
                ComboBox<String> comboBox = new ComboBox<>();
                for (Object term: MainScreen.storage) {
                    if (term.getClass() == field.getType()) {
                        Field tempfield = null;
                        try {
                            tempfield = term.getClass().getField("model");
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                        if (!tempfield.isAccessible()) {
                            tempfield.setAccessible(true);
                        }
                        try {
                            Object myValue = tempfield.get(term);
                            String name = term.getClass().getName();
                            comboBox.getItems().add(name.substring(name.lastIndexOf('.') + 1) + " (" + myValue.toString() + " )");
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                comboBox.setValue(value);
                vEditCont.getChildren().add(comboBox);
                comp = comboBox;
            }
            else {
                TextField textField = new TextField();
                vEditCont.getChildren().add(textField);
                comp = (Object) textField;
            }
            Button button = new Button("Edit");
            Object finalComp = comp;
            Label finalLabel = label;
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        Class fieldType = field.getType();
                        if (finalComp instanceof ComboBox){
                            ComboBox comboBox = (ComboBox) finalComp;
                            Integer index = comboBox.getSelectionModel().getSelectedIndex();
                            if (index != -1){
                                String text = comboBox.getItems().get(index).toString();
                                if (text.contains("(")){
                                    int counter = 0;
                                    int i = 0;
                                    while ((counter <= index) && (i < MainScreen.storage.size())){
                                        if (MainScreen.storage.get(i).getClass().getName().equals("Terminators." + text.substring(0, text.indexOf(' ')))){
                                            counter ++;
                                        }
                                        i++;
                                    }
                                    i--;
                                    field.set(terminator, MainScreen.storage.get(i));
                                }
                                else {
                                    Method val = fieldType.getMethod("valueOf", String.class);
                                    field.set(terminator, val.invoke(field, text));
                                }
                                finalLabel.setText(text);
                            }
                        }
                        else {
                            Constructor constructor = fieldType.getDeclaredConstructor(String.class);
                            TextField textField = (TextField) finalComp;
                            Object value = constructor.newInstance(textField.getText());
                            field.set(terminator, value);
                            finalLabel.setText(textField.getText());
                        }
                        if (vEditCont.getChildren().size() == 4){
                            vEditCont.getChildren().remove(3);
                        }
                    } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException ex) {
                        ex.printStackTrace();
                        messageError();
                    }
                }

                private void messageError() {
                    if (vEditCont.getChildren().size() == 3) {
                        Label errorLabel = new Label("Error! Check your data");
                        errorLabel.setPadding(new Insets(17, 10, 5, 12));
                        errorLabel.setTextFill(Color.web("#ff2400"));
                        vEditCont.getChildren().add(errorLabel);
                    }
                }
            });
            vEditCont.getChildren().add(button);
        }
        else{
            Label tempLabel = new Label("This field cannot be modificated!");
            tempLabel.setPadding(new Insets(17, 10, 5, 12));
            label.setFont(new Font("Arial", 14));
            tempLabel.setTextFill(Color.web("#ff2400"));
            vEditCont.getChildren().add(tempLabel);
        }
    }

    public void serialize(ActionEvent actionEvent) {
        if (vSerializers.getChildren().get(vSerializers.getChildren().size() - 1) instanceof Label){
            vSerializers.getChildren().remove(vSerializers.getChildren().size() - 1);
        }
        try {
            Method plugMethod = null;
            Class plugCls = null;
            if (cbTypePlug.getValue() != null) {
                if (!checkPlugin((String) cbTypePlug.getValue()))
                    throw new FileNotFoundException();
                else {
                    plugCls = getClassFromPlugins((String) cbTypePlug.getValue());
                    if (plugCls == null){
                        String clsName = cbTypePlug.getValue() + "Plugin";
                        plugCls = getClassFromPlugins(clsName);
                        if (plugCls == null)
                            throw new FileNotFoundException();
                    }

                }
            }
            String format = SupportedFileFormats.values()[cbTypeSer.getSelectionModel().getSelectedIndex()].name();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Serialize to");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(cbTypeSer.getValue() + " Files", "*." + format));
            String fileName = fileChooser.showOpenDialog(mainStage).getName();
            Class cls = Class.forName("serializers." + cbTypeSer.getValue());
            Method method = cls.getMethod("serialize", String.class, Class.class);
            method.invoke(cls, fileName, plugCls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Error! Serializer doesn't found");
            errorLabel.setPadding(new Insets(17, 10, 5, 12));
            errorLabel.setTextFill(Color.web("#ff2400"));
            vSerializers.getChildren().add(errorLabel);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Label errorLabel = new Label("Error! Plugin doesn't found");
            errorLabel.setPadding(new Insets(17, 10, 5, 12));
            errorLabel.setTextFill(Color.web("#ff2400"));
            vPlugins.getChildren().add(errorLabel);
        }
    }

    private boolean checkPlugin(String value) {
        File pluginDir = new File("plugins");
        File[] jars = pluginDir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile() && file.getName().endsWith(".jar");
            }
        });
        for (File f : jars) {
            try {
                JarFile jarFile = new JarFile(f);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.isDirectory()) {
                        continue;
                    }
                    String name = entry.getName();
                    String ext = "";
                    int index = name.lastIndexOf('.') + 1;
                    if (index > 0) {
                        String extension = name.substring(index);
                        ext = extension;
                    }
                    if (ext.equals("class")) {
                        String className = name.substring(0, name.length() - 6);
                        if (className.contains(value))
                            return true;
                    }
                }
                jarFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void deserialize(ActionEvent actionEvent) {
        if (vSerializers.getChildren().get(vSerializers.getChildren().size() - 1) instanceof Label){
            vSerializers.getChildren().remove(vSerializers.getChildren().size() - 1);
        }
        try {
            String format = SupportedFileFormats.values()[cbTypeSer.getSelectionModel().getSelectedIndex()].name();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Deserialize to");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(cbTypeSer.getValue() + " Files", "*." + format));
            String fileName = fileChooser.showOpenDialog(mainStage).getName();
            Class cls = Class.forName("serializers." + cbTypeSer.getValue());
            Method method = cls.getMethod("deserialize", String.class);
            method.invoke(cls, fileName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Error! Deserializer doesn't found");
            errorLabel.setPadding(new Insets(17, 10, 5, 12));
            errorLabel.setTextFill(Color.web("#ff2400"));
            vSerializers.getChildren().add(errorLabel);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(MainScreen.storage);
    }


    public void handleDownloadPlugins(MouseEvent mouseEvent) {
        cbTypePlug.getItems().clear();
        if (vPlugins.getChildren().size() == 3){
            vPlugins.getChildren().remove(2);
        }
        PlugLoader pluginLoader = new PlugLoader();
        ArrayList<Class> plugins = pluginLoader.getPlugins();
        for (Class plugin : plugins) {
            String plugName = plugin.getName();
            if (plugName.endsWith("Plugin")){
                plugName = plugName.substring(0, plugName.length()-6);
            }
            cbTypePlug.getItems().add(plugName);
        }
    }

    private List<String> getClassNames(String path){
        List<String> classNames = new ArrayList<>();
        File dir = new File(path);
        File[] arrFiles = dir.listFiles();
        for (File file: arrFiles){
            String filename = file.getName();
            if (filename.contains("Plugin.java")){
                int index = filename.lastIndexOf("Plugin.java");
                if (index != 0)
                    classNames.add(filename.substring(0, index));
            }
        }
        return classNames;
    }
}
