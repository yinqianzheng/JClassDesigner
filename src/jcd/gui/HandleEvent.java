/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.io.File;
import java.io.IOException;
import java.util.Map;
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
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.json.JsonObject;
import jcd.components.JClass;
import jcd.components.JLineGroup;
import jcd.controller.JFileManager;
import jcd.data.DataManager;
import static jcd.data.DataManager.currentCursor;

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
            if (!DataManager.isSaved().get())
                try {
                    if (!promoteUserToSave())
                        return;
            } catch (IOException ex) {
                Logger.getLogger(HandleEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
            wp.reload();
            wp.reload();
            DataManager.hasDirectory().set(false);
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
                if (!DataManager.isSaved().get())
                    if (!promoteUserToSave())
                        return;
                wp.reload();
                wp.reload();
                String path = JFileManager.getDirectory(wp.primaryStageWindow);
                JsonObject jsonObj = JFileManager.loadFile(path);
                JFileManager.createHistoryList(jsonObj);
                DataManager.setSaved(true);
            } catch (IOException ex) {
                Logger.getLogger(HandleEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    
    static  EventHandler saveEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            if (!DataManager.getHistoryList().get(DataManager.currentCursor.get()).equals(dataManager.currentDiagram()))
                DataManager.addToHistoryList();
            if (DataManager.hasDirectory().get())
                try {
                    JFileManager.saveData(DataManager.getInstance(obj), DataManager.getDirectory());
                } catch (IOException ex) {
                    Logger.getLogger(HandleEvent.class.getName()).log(Level.SEVERE, null, ex);
                }
            else
                try {
                    JFileManager.saveAs(DataManager.getInstance(obj), wp.primaryStageWindow);
            } catch (IOException ex) {
                Logger.getLogger(HandleEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
            DataManager.setSaved(true);
        }
    };
    
    static  EventHandler saveAsEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            if (!DataManager.getHistoryList().get(DataManager.currentCursor.get()).equals(dataManager.currentDiagram()))
                DataManager.addToHistoryList();
            try {
                JFileManager.saveAs(DataManager.getInstance(obj), wp.primaryStageWindow);
            } catch (IOException ex) {
                Logger.getLogger(HandleEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            if (!DataManager.isSaved().get())
                try {
                    if (!promoteUserToSave())
                        return;
            } catch (IOException ex) {
                Logger.getLogger(HandleEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            dataManager.isResizeMode.set(false);
        }
    };
    
    static EventHandler resizeEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            wp.canvas.setDisable(true);
            wp.canvas.toBack();
            //wp.root.setCursor(Cursor.HAND);
            dataManager.isResizeMode.set(true);
            WorkSpace.isSelectMode = false;
        }
    };
    
    static EventHandler addEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            wp.canvas.toFront();
            //wp.root.setCursor(Cursor.CROSSHAIR);
            wp.canvas.setDisable(false);
            WorkSpace.isSelectMode = false;
            dataManager.isResizeMode.set(false);
            dataManager.isInterface.set(false);
        }
    };
    
    static EventHandler addInterface = new EventHandler() {
        @Override
        public void handle(Event event) {
            wp.canvas.toFront();
            wp.canvas.setDisable(false);
            WorkSpace.isSelectMode = false;
            dataManager.isResizeMode.set(false);
            dataManager.isInterface.set(true);
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
                jc.setMouseTransparent(false);
                if (dataManager.isInterface.get())
                    jc.setInterface(true);
                addToScreen(jc);
                wp.canvas.toFront();
                //DataManager.addToHistoryList();
                DataManager.setUnSaved();
            }
        }
    };
    
    public static void addToScreen(JClass jc){
                jc.setCursor(Cursor.OPEN_HAND);
                wp.root.getChildren().add(jc);
                dataManager.addClassToList(jc);
                dataManager.setSelectedJC(jc);
                wp.clearPackageNameInput();
                wp.setClassNameInput(jc.getClassName());
                wp.setPackageNameInput(jc.getPackageName());
//                wp.interfaceCheckBox.setSelected(jc.getInterface().get());
                wp.variablePane.setContent(jc.getVariableBox().getVariableTable());
                wp.methodPane.setContent(jc.getMethodBox().getMethodTable());
          
                if (dataManager.getSelectedJC()!=null){
                    wp.addVariable.setDisable(false);
                    wp.deleteVariable.setDisable(false);
                    wp.addMethod.setDisable(false);
                    wp.deleteMethod.setDisable(false);
                }
    }
    
    static EventHandler removeClass = new EventHandler() {
        @Override
        public void handle(Event event) {
            if (dataManager.getSelectedJC()!=null){
                dataManager.removeChildrenLines();
                wp.root.getChildren().removeAll(dataManager.getSelectedJC(), dataManager.getSelectedJC().getLine());
                removeAllLines(dataManager.getSelectedJC());
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
                DataManager.setUnSaved();
            }
        }
    };
    
    
    static EventHandler undoEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            DataManager.currentCursor.set(DataManager.currentCursor.get()-1);
        }
    }; 
    
    static EventHandler redoEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            DataManager.currentCursor.set(DataManager.currentCursor.get()+1);
        }
    }; 
    
    static EventHandler originalZoomEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
           // initFunctionForZoom_In_Out()
        }
    }; 
    
    static EventHandler zoomInEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
             // initFunctionForZoom_In_Out()
        }
    }; 
    
    static EventHandler zoomOutEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
             // initFunctionForZoom_In_Out()
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
    
    public static void setToInterface(boolean tf){
        try {
            DataManager.getSelectedJC().setInterface(tf);
            DataManager.setSaved(false);
        } catch (Exception e) {
        }
    }
    
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
    
    static boolean promoteUserToSave() throws IOException{
        Alert yesNoDialog = new  Alert(Alert.AlertType.CONFIRMATION);
            yesNoDialog.setTitle("Unsaved work!");
            yesNoDialog.setHeaderText("Do you want to save the unsaved work?");
            ButtonType buttonTypeYes = new ButtonType("YES");
            ButtonType buttonTypeNo = new ButtonType("NO");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            yesNoDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);
            Optional<ButtonType> result = yesNoDialog.showAndWait();
            if (result.get() == buttonTypeYes) {
                if (!DataManager.getHistoryList().get(DataManager.currentCursor.get()).equals(dataManager.currentDiagram()))
                    DataManager.addToHistoryList();
                if (DataManager.hasDirectory().get())
                    try {
                        JFileManager.saveData(DataManager.getInstance(obj), DataManager.getDirectory());
                    } catch (IOException ex) {
                        Logger.getLogger(HandleEvent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                else
                    try {
                        JFileManager.saveAs(DataManager.getInstance(obj), wp.primaryStageWindow);
                    } catch (IOException ex) {
                        Logger.getLogger(HandleEvent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                DataManager.setSaved(true);
                return true;
            }else if (result.get() == buttonTypeNo){
                return true;
            }else{
                return false;
            }
                
    }
    
    
    public static void removeAllLines(JClass jc){
        if (!jc.getAggregationJLineGroupsList().isEmpty())
            for (Map.Entry<String, JLineGroup> entry: jc.getAggregationJLineGroupsList().entrySet()){
                wp.root.getChildren().remove(entry.getValue());
            };
        
        if (!jc.getUsesJLineGroupsList().isEmpty())
            for (Map.Entry<String, JLineGroup> entry: jc.getUsesJLineGroupsList().entrySet()){
                wp.root.getChildren().remove(entry.getValue());
            };
        
        if (!jc.getJLineGroupList().isEmpty())
            for (Map.Entry<String, JLineGroup> entry: jc.getJLineGroupList().entrySet()){
                wp.root.getChildren().remove(entry.getValue());
            };
    }
    
}
