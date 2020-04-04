package CreateObject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.MainController;
import sample.MainScreen;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Controller {

    public Button btnCloseWindow;
    public ComboBox cbTerminators;
    public VBox vContainer;
    private MainController main;

    public void downloadFields(ActionEvent actionEvent) {
        try{
            Class<?> terminatorClass = Class.forName("Terminators." + cbTerminators.getValue());
            Map<String,String> dictionary = new LinkedHashMap<String,String>();
            Field[] fields = terminatorClass.getFields();
            vContainer.getChildren().removeAll(vContainer.getChildren());

            for (Field field : fields) {
                System.out.println(field);
                if (!Modifier.toString(field.getModifiers()).contains("final")) {
                    dictionary.put(field.getName(), "");
                    String inputName = field.toString();
                    inputName = inputName.substring(inputName.lastIndexOf('.') + 1);
                    inputName = inputName.replace('_', ' ');
                    inputName = inputName.substring(0, 1).toUpperCase() + inputName.substring(1).toLowerCase();
                    Label label = new Label(inputName);
                    vContainer.getChildren().add(label);
                    if (field.getType().isEnum()){
                        ComboBox comboBox = new ComboBox();
                        comboBox.getItems().addAll(field.getType().getEnumConstants());
                        vContainer.getChildren().add(comboBox);
                    }
                    else if (field.getType().getPackageName().equals("Terminators")){
                        ComboBox comboBox = new ComboBox();
                        for (Object term: MainScreen.storage){
                            if (term.getClass() == field.getType()){
                                Field tempfield = null;
                                try {
                                    tempfield = term.getClass().getField("model");
                                } catch (NoSuchFieldException e) {
                                    e.printStackTrace();
                                }
                                if(!tempfield.isAccessible()){
                                    tempfield.setAccessible(true);
                                }
                                try {
                                    Object value = tempfield.get(term);
                                    String name = term.getClass().getName();
                                    comboBox.getItems().add(name.substring(name.lastIndexOf('.') + 1) + " (" + value.toString() + " )");
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        vContainer.getChildren().add(comboBox);
                    }
                    else {
                        TextField textField = new TextField();
                        vContainer.getChildren().add(textField);
                    }
                }
            }
            Button btnCreate = new Button("Create");
            btnCreate.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    // cleaning error messages
                    if (vContainer.getChildren().get(vContainer.getChildren().size() - 1) instanceof Label){
                        vContainer.getChildren().remove(vContainer.getChildren().size() - 1);
                    }
                    try {
                        List<String> nameFields = getTextFromFields();
                        int counter = 0;
                        for (String key: dictionary.keySet()){
                            dictionary.put(key, nameFields.get(counter));
                            counter++;
                        }
                        Constructor<?>[] constructors = terminatorClass.getConstructors();
                        Object terminator = null;
                        try {
                            if (constructors[0].getParameterCount() > 0)
                                terminator = constructors[0].newInstance(dictionary);
                            else
                                terminator = constructors[1].newInstance(dictionary);
                            MainScreen.storage.add(terminator);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                            ex.printStackTrace();
                            createError();
                        }
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                        createError();
                    }
                }

                private void createError(){
                    Label lblError = new Label("Error! Check your data!");
                    lblError.setPadding(new Insets(17, 10, 5, 12));
                    lblError.setFont(new Font("Arial", 14));
                    lblError.setTextFill(Color.web("#ff2400"));
                    vContainer.getChildren().add(lblError);
                }

                private List getTextFromFields() throws Exception {
                    List nameFields = new ArrayList();
                    for (Node children : vContainer.getChildren()){
                        if (children instanceof TextField){
                            TextField child = (TextField) children;
                            String textFieldText = child.getText();
                            if (textFieldText.length() == 0){
                                throw new Exception();
                            }
                            nameFields.add(textFieldText);
                        }
                        if (children instanceof ComboBox){
                            ComboBox child = (ComboBox) children;
                            Integer index = child.getSelectionModel().getSelectedIndex();
                            if (index != -1){
                                String cbText = child.getItems().get(index).toString();
                                if (cbText.contains("(")){
                                    int counter = 0;
                                    int i = 0;
                                    while ((counter <= index) && (i < MainScreen.storage.size())){
                                        System.out.println(MainScreen.storage.get(i).getClass().getName());
                                        if (MainScreen.storage.get(i).getClass().getName().equals("Terminators." + cbText.substring(0, cbText.indexOf(' ')))){
                                            counter ++;
                                        }
                                        i++;
                                    }
                                    i--;
                                    nameFields.add(String.valueOf(i));
                                }
                                else
                                    nameFields.add(cbText);
                            }
                            else{
                                throw new Exception();
                            }
                        }
                    }
                    return nameFields;
                }
            });

            vContainer.getChildren().add(btnCreate);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCloseWindow.getScene().getWindow();
        stage.close();
    }
}
