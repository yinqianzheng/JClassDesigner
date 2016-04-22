/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import static java.awt.SystemColor.window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.json.JsonObject;
import jcd.components.JClass;
import jcd.controller.JFileManager;
import jcd.data.DataManager;

/**
 *
 * @author YinqianZheng
 */
public class HandleEvent {

    private static HandleEvent obj;
    private static WorkSpace wp;
    private static DataManager dataManager;
    private HandleEvent() {}
     
    public static HandleEvent getInstance(WorkSpace workspace){
        if(obj==null){
            obj = new HandleEvent();
            wp = workspace;
        }
        dataManager = DataManager.getInstance(obj);
        
        return obj;
    }
    
    public static WorkSpace getWorkPane(){
        return wp;
    }
    
    
    static EventHandler newEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            if (!DataManager.isSaved.get())
                if (!promoteUserToSave())
                    return;
            wp.reload();
            DataManager.hasDirectory.set(false);
            Alert imformDialog = new  Alert(Alert.AlertType.INFORMATION);
            imformDialog.setTitle("New Work!");
            imformDialog.setHeaderText("New work space is ready for editting!");          
            Optional<ButtonType> result = imformDialog.showAndWait();
            DataManager.setSaved(true);
        }
    };
    
    static EventHandler loadEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            try {
                if (!DataManager.isSaved.get())
                    if (!promoteUserToSave())
                        return;
                wp.reload();
                JsonObject jsonObj = JFileManager.loadFile(wp.primaryStageWindow);
                JFileManager.createClasses(jsonObj);
                DataManager.setSaved(true);
            } catch (IOException ex) {
                Logger.getLogger(HandleEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    
    static  EventHandler saveEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            if (DataManager.hasDirectory.get())
                JFileManager.saveData(DataManager.getInstance(obj), DataManager.getDirectory());
            else
                JFileManager.saveAs(DataManager.getInstance(obj), wp.primaryStageWindow);
            DataManager.setSaved(true);
        }
    };
    
    static  EventHandler saveAsEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            JFileManager.saveAs(DataManager.getInstance(obj), wp.primaryStageWindow);
            DataManager.setSaved(true);
        }
    };
    
    static EventHandler exportPhoto = new EventHandler() {
        @Override
        public void handle(Event event) {
            WritableImage wim = new WritableImage(1000, 1000);
            WritableImage snapImage = wp.root.snapshot(new SnapshotParameters(), wim);
            try{
                FileChooser.ExtensionFilter extFilter = 
                        new FileChooser.ExtensionFilter("Image files (*.png)", "*.png");
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(extFilter);
                fileChooser.setTitle("Save Image");
                File file = fileChooser.showSaveDialog(WorkSpace.primaryStageWindow);
      
            if (file != null) 
                
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                } catch (Exception s) {
                }
            }catch(Exception ex){
                }
        }
    };
    
     
    static EventHandler exportCode = new EventHandler() {
        @Override
        public void handle(Event event) {
            
            JFileManager.exportCode();
            
        }
    };
    
    static EventHandler exitEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            if (!DataManager.isSaved.get())
                if (!promoteUserToSave())
                    return;
            System.exit(0);
        }
    };
    
    
    static EventHandler selectEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            wp.canvas.setDisable(true);
            wp.canvas.toBack();
            wp.root.setCursor(Cursor.HAND);
            WorkSpace.isSelectMode = true;
        }
    };
    
    static EventHandler addEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            wp.canvas.toFront();
            wp.canvas.setCursor(Cursor.CROSSHAIR);
            wp.canvas.setDisable(false);
            WorkSpace.isSelectMode = false;
        }
    };
    
    
        
    static EventHandler addClass = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent click) {
            if (click.getClickCount() == 2){     
                JClass jc = new JClass(click.getX(), click.getY());
                String name;
                SimpleBooleanProperty validName = new SimpleBooleanProperty(true);
                
                for (int i = 1; i < 100; i++){
                    name = "NewClass" + String.valueOf(i);
                    validName.set(true);
                    for (JClass jclass : DataManager.getJClassList().getItems()){
                        if ((jclass.getClassName()).equals(name)){
                            validName.set(false);
                        }           
                    }
                    
                    if (validName.get() == true){
                        jc.setClassName(name);
                        break;
                    }
                }
               
                addToScreen(jc);
                DataManager.setSaved(false);
            }
        }
    };
    
    public static void addToScreen(JClass jc){
                wp.root.getChildren().add(jc);
                dataManager.addClassToList(jc);
                dataManager.setSelectedJC(jc);
                wp.clearPackageNameInput();
                wp.setClassNameInput(jc.getClassName());
                wp.setPackageNameInput(jc.getPackageName());
                wp.variablePane.setContent(jc.getVariableBox().getVariableTable());
                wp.methodPane.setContent(jc.getMethodBox().getMethodTable());
                if (dataManager.getSelectedJC()!=null)
                    wp.addVariable.setDisable(false);
                    wp.deleteVariable.setDisable(false);
                    wp.addMethod.setDisable(false);
                    wp.deleteMethod.setDisable(false);
    }
    
    static EventHandler removeClass = new EventHandler() {
        @Override
        public void handle(Event event) {
            if (dataManager.getSelectedJC()!=null){
                wp.root.getChildren().removeAll(dataManager.getSelectedJC(), dataManager.getSelectedJC().getLine());
                dataManager.removeChildrenLines();
                dataManager.removeClass(dataManager.getSelectedJC());
                dataManager.setSelectedJC(null);
                wp.addVariable.setDisable(true);
                wp.deleteVariable.setDisable(true);
                wp.addMethod.setDisable(true);
                wp.deleteMethod.setDisable(true);
                wp.variablePane.setContent(null);
                wp.methodPane.setContent(null);
                try {
                    wp.clearClassNameInput();
                    wp.clearPackageNameInput();
                   wp.parentsList.setValue(null);
                } catch (Exception e) {
                }
            }
        }
    };
    
    public static Boolean changeClassName (String str){
        wp.classNameInput.setStyle(null);
        try {
            String newName = DataManager.getSelectedJC().getPackageName() + "." + str;
            for (JClass jclass : DataManager.getJClassList().getItems()){
                if (!jclass.equals(DataManager.getSelectedJC()))
                    if ((jclass.getPackageName()+"."+jclass.getClassName()).equals(newName)){
                        wp.classNameInput.setStyle("-fx-text-fill: red;"); ;
                        return true;
                    }           
            }    
            DataManager.getSelectedJC().setClassName(str);
            DataManager.setSaved(false);
        } catch (Exception e) {
        }
        wp.classNameInput.setStyle(null);
        DataManager.renewParentList();
        wp.parentsList.setValue(DataManager.getSelectedJC().getJParent().getPackageName()+
                "."+DataManager.getSelectedJC().getJParent().getClassName());
        
        return false;
    };
    
    public static void changePackageName (String str){
        try {
            DataManager.getSelectedJC().setPackageName(str);
            DataManager.setSaved(false);
        } catch (Exception e) {
        }
    };
    
    
    
    static EventHandler addVariable = new EventHandler() {
        @Override
        public void handle(Event event) {
            dataManager.getSelectedJC().getVariableBox().addVariable();
            DataManager.setSaved(false);
        }
    };
    
    static EventHandler deleteVariable = new EventHandler() {
        @Override
        public void handle(Event event) {
            try {
                dataManager.getSelectedJC().getVariableBox().removeVariable();
                DataManager.setSaved(false);
            } catch (Exception e) {
            }
        }
    };
    
    static EventHandler addMethod = new EventHandler() {
        @Override
        public void handle(Event event) {
            dataManager.getSelectedJC().getMethodBox().addMethod();
            DataManager.setSaved(false);
        }
    };
    
    static EventHandler deleteMethod = new EventHandler() {
        @Override
        public void handle(Event event) {
            try {
                dataManager.getSelectedJC().getMethodBox().removeMethod();
                DataManager.setSaved(false);
            } catch (Exception e) {
            }
        }
    };
    
    static boolean promoteUserToSave(){
        Alert yesNoDialog = new  Alert(Alert.AlertType.CONFIRMATION);
            yesNoDialog.setTitle("Unsaved work!");
            yesNoDialog.setHeaderText("Do you want to save the unsaved work?");
            ButtonType buttonTypeYes = new ButtonType("YES");
            ButtonType buttonTypeNo = new ButtonType("NO");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            yesNoDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);
            Optional<ButtonType> result = yesNoDialog.showAndWait();
            if (result.get() == buttonTypeYes) {
                if (DataManager.hasDirectory.get())
                    JFileManager.saveData(DataManager.getSelectedJC(), DataManager.getDirectory());
                else
                    JFileManager.saveAs(DataManager.getSelectedJC(), wp.primaryStageWindow);
                return true;
            }else if (result.get() == buttonTypeNo){
                return true;
            }else{
                return false;
            }
                
    }
    
    
}
