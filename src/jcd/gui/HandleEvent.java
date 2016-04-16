/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.util.Optional;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import jcd.components.JClass;
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
           wp.reload();
            Alert imformDialog = new  Alert(Alert.AlertType.INFORMATION);
            imformDialog.setTitle("New Work!");
            imformDialog.setHeaderText("New work space is ready for editting!");          
            Optional<ButtonType> result = imformDialog.showAndWait();
        }
    };
    
    static EventHandler loadEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            System.out.println("load");
        }
    };
    
    static  EventHandler saveEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            System.out.println("save");
        }
    };
    
    static  EventHandler saveAsEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
            System.out.println("saveAs");
        }
    };
    
    static EventHandler exitEvent = new EventHandler() {
        @Override
        public void handle(Event event) {
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
                wp.root.getChildren().add(jc);
                dataManager.addClassToList(jc);
                dataManager.setSelectedJC(jc);
                wp.clearPackageNameInput();
                wp.setClassNameInput(jc.getClassName());
                wp.variablePane.setContent(jc.getVariableBox().getVariableTable());
                wp.methodPane.setContent(jc.getMethodBox().getMethodTable());
                if (dataManager.getSelectedJC()!=null)
                    wp.addVariable.setDisable(false);
                    wp.deleteVariable.setDisable(false);
                    wp.addMethod.setDisable(false);
                    wp.deleteMethod.setDisable(false);
            }
        }
    };
    
    static EventHandler removeClass = new EventHandler() {
        @Override
        public void handle(Event event) {
            wp.root.getChildren().remove(dataManager.getSelectedJC());
            dataManager.removeClass(dataManager.getSelectedJC());
            dataManager.setSelectedJC(null);
            if (dataManager.getSelectedJC()!=null){
                wp.addVariable.setDisable(false);
                wp.deleteVariable.setDisable(false);
            }else{
                wp.addVariable.setDisable(true);
                wp.deleteVariable.setDisable(true);
                wp.addMethod.setDisable(true);
                wp.deleteMethod.setDisable(true);
                wp.variablePane.setContent(null);
                wp.methodPane.setContent(null);
                wp.clearClassNameInput();
                wp.clearPackageNameInput();
            }
        }
    };
    
    public static void changeClassName (String str){
       DataManager.getSelectedJC().setClassName(str);
    };
    
    public static void changePackageName (String str){
       DataManager.getSelectedJC().setPackageName(str);
    };
    
    
    
    static EventHandler addVariable = new EventHandler() {
        @Override
        public void handle(Event event) {
            dataManager.getSelectedJC().getVariableBox().addVariable();
        }
    };
    
    static EventHandler deleteVariable = new EventHandler() {
        @Override
        public void handle(Event event) {
            try {
                dataManager.getSelectedJC().getVariableBox().removeVariable();
            } catch (Exception e) {
            }
        }
    };
    
    static EventHandler addMethod = new EventHandler() {
        @Override
        public void handle(Event event) {
            dataManager.getSelectedJC().getMethodBox().addMethod();
        }
    };
    
    static EventHandler deleteMethod = new EventHandler() {
        @Override
        public void handle(Event event) {
            try {
                dataManager.getSelectedJC().getMethodBox().removeMethod();
            } catch (Exception e) {
            }
        }
    };
}
