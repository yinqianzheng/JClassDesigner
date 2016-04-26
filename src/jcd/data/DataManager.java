/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import jcd.components.JClass;
import jcd.gui.HandleEvent;
import jcd.gui.WorkSpace;

/**
 *
 * @author YinqianZheng
 */
public class DataManager {
    private static DataManager dm;
    private static HandleEvent handler;
    private static WorkSpace workPane;
    public static TableView<JClass> jList = new TableView<JClass>();
    private static JClass preSelectedJC;
    private static JClass selectedJC;
    final private static ComboBox<String> parentList = new ComboBox<String>();
    final private static DropShadow highlight = new DropShadow(20, Color.YELLOW);
    public static SimpleBooleanProperty isSaved = new SimpleBooleanProperty(true);
    public static SimpleBooleanProperty hasDirectory = new SimpleBooleanProperty(false);
    private static String directory = "";

    private DataManager() {
        // renew parent list
        jList.getItems().addListener(new ListChangeListener<JClass>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends JClass> change) {
                renewParentList();
                HandleEvent.getWorkPane().buttonMap.get("export code").setDisable(jList.getItems().isEmpty());
                HandleEvent.getWorkPane().buttonMap.get("export photo").setDisable(jList.getItems().isEmpty());
                HandleEvent.getWorkPane().buttonMap.get("save as").setDisable(jList.getItems().isEmpty());
                HandleEvent.getWorkPane().buttonMap.get("save").setDisable(jList.getItems().isEmpty());
                if (jList.getItems().isEmpty())
                    setSaved(true);
            }
        });
        
        // update jParent
        parentList.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                try {
                    if (t1.equals("none")){
                    selectedJC.setJParent(null);
                    HandleEvent.getWorkPane().root.getChildren().remove(selectedJC.getLine());
                }else {
                    for (JClass jclass : jList.getItems()){
                        if ((jclass.getPackageName()+"."+ jclass.getClassName()).equals(t1)){
                            selectedJC.setJParent(jclass);
                            try {
                                HandleEvent.getWorkPane().root.getChildren().add(selectedJC.setLinkToJParent());
                            } catch (Exception e) {
                            }
                            break;
                        }
                    }
                }
                } catch (Exception e) {
                }
                
            }
        });
        
        isSaved.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    HandleEvent.getWorkPane().buttonMap.get("save").setDisable(isSaved.get());
            }
        });
    }
    
    public void removeChildrenLines(){
        for (JClass jclass : jList.getItems()){
            if (jclass.getJParent()!=null){
                if (jclass.getJParent().equals(selectedJC)){
                    HandleEvent.getWorkPane().root.getChildren().remove(jclass.getLine());
                }
            }
        }
    }
    
    public static void renewParentList(){
        parentList.getItems().clear();
        parentList.getItems().add("none");
            for (JClass jclass : jList.getItems()){
                if (!jclass.equals(selectedJC))
                    parentList.getItems().add(jclass.getPackageName()+
                            "."+ jclass.getClassName());
            }     
    }
    
    public static ComboBox getParentList(){
        return parentList;
    }
    
    public static DataManager getInstance(HandleEvent e){
        if(handler==null){
            dm = new DataManager();
            handler = e;
        }
        return dm;
    }
    
    public static TableView<JClass> getJClassList(){
        return jList;
    }
    
    public static JClass getSelectedJC(){
        return selectedJC;
    }
    
    public static void setSelectedJC(JClass jc){
        selectedJC = jc;
    }
        
    public static void addClassToList(JClass j){
        if (selectedJC!=null)
            preSelectedJC = selectedJC;
        jList.getItems().add(j);        
        selectedJC = j;
        j.setEffect(highlight);
        j.setOnMousePressed(pressed);
        j.setOnMouseDragged(dragged);
        j.setOnMouseReleased(released);
        if (preSelectedJC!=null)
            preSelectedJC.setEffect(null);
    }
    
        
    public static void removeClass(JClass jc){
        jList.getItems().remove(jc);
    }
    
    public static String getSelectedJCName(){
        return selectedJC.getClassName();
    }
    
    public static void setDirectory(String dir){
        directory = dir;
    }
    
    public static String getDirectory(){
        return directory;
    }
    
    private static HandleEvent getHandler(){
        return handler;
    }
    
    
    private static EventHandler pressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent click) {
            
            if (WorkSpace.isSelectMode==true){
                if (selectedJC!=null)
                    preSelectedJC = selectedJC;
                selectedJC = ((JClass)click.getSource());
                jList.getItems().remove(selectedJC);
                jList.getItems().add(selectedJC);
                selectedJC.setEffect(highlight);   
                if (preSelectedJC!=null&&preSelectedJC!=selectedJC)
                    preSelectedJC.setEffect(null);
                
                HandleEvent.getWorkPane().getAddVariableButton().setDisable(false);
                HandleEvent.getWorkPane().getDeleteVariableButton().setDisable(false);
                HandleEvent.getWorkPane().getAddMethodButton().setDisable(false);
                HandleEvent.getWorkPane().getDeleteMethodButton().setDisable(false);
                HandleEvent.getWorkPane().setVariablePane(selectedJC.getVariableBox().getVariableTable());
                HandleEvent.getWorkPane().setMethodPane(selectedJC.getMethodBox().getMethodTable());
                HandleEvent.getWorkPane().setClassNameInput(selectedJC.getClassName());   
                HandleEvent.getWorkPane().setPackageNameInput(selectedJC.getPackageName());
                HandleEvent.getWorkPane().interfaceCheckBox.setSelected(selectedJC.getInterface().get());
                try {
                    HandleEvent.getWorkPane().parentsList.setValue(selectedJC.getJParent().getPackageName()+"."+
                    selectedJC.getJParent().getClassName());    
                } catch (Exception e) {
                }
            }
            try {
                selectedJC.sceneX = click.getSceneX();
                selectedJC.sceneY = click.getSceneY();
                selectedJC.translateX = ((JClass)click.getSource()).getTranslateX();
                selectedJC.translateY = ((JClass)click.getSource()).getTranslateY();
          
            } catch (Exception e) {
            }  
        }
    };
    
    private static EventHandler dragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent click) {
            if (HandleEvent.getWorkPane().isSelectMode==true){
                double offsetX = click.getSceneX() - selectedJC.sceneX;
                double offsetY = click.getSceneY() - selectedJC.sceneY;
                
                double newTranslateX = selectedJC.translateX + offsetX/HandleEvent.getWorkPane().getZoomValue();
                double newTranslateY = selectedJC.translateY + offsetY/HandleEvent.getWorkPane().getZoomValue();

                ((JClass)(click.getSource())).setTranslateX(newTranslateX);
                ((JClass)(click.getSource())).setTranslateY(newTranslateY);
                try {
                    selectedJC.getLine().setStartX(((JClass)(click.getSource())).getLayoutX()+newTranslateX);
                    selectedJC.getLine().setStartY(((JClass)(click.getSource())).getLayoutY()+newTranslateY);
                } catch (Exception e) {
                }
                
                for (JClass jclass : jList.getItems()){
                    if (jclass.getJParent()!=null){
                        if (jclass.getJParent().equals(selectedJC)){
                            jclass.getLine().setEndX(jclass.getJParent().getLayoutX()+jclass.getJParent().getTranslateX());
                            jclass.getLine().setEndY(jclass.getJParent().getLayoutY()+jclass.getJParent().getTranslateY());
                        }
                    }
                }
            }
            DataManager.setSaved(false);
        }
    };
    
    private static EventHandler released = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent release) {
            if (HandleEvent.getWorkPane().isGridSnapActived()){
                if (HandleEvent.getWorkPane().isSelectMode==true){
                    double x = ((JClass)(release.getSource())).getLayoutX() + ((JClass)(release.getSource())).getTranslateX();
                    double y = ((JClass)(release.getSource())).getLayoutY() + ((JClass)(release.getSource())).getTranslateY();
                
                    x = (int)(x/40)*40;
                    y = (int)(y/40)*40;
                    
                    ((JClass)(release.getSource())).setTranslateX(x-((JClass)(release.getSource())).getLayoutX());
                    ((JClass)(release.getSource())).setTranslateY(y-((JClass)(release.getSource())).getLayoutY());
                    
                    try {
                        selectedJC.getLine().setStartX(((JClass)(release.getSource())).getLayoutX()+((JClass)(release.getSource())).getTranslateX());
                        selectedJC.getLine().setStartY(((JClass)(release.getSource())).getLayoutY()+((JClass)(release.getSource())).getTranslateY());
                    } catch (Exception e) {
                    }
                
                    for (JClass jclass : jList.getItems()){
                        if (jclass.getJParent()!=null){
                            if (jclass.getJParent().equals(selectedJC)){
                                jclass.getLine().setEndX(jclass.getJParent().getLayoutX()+jclass.getJParent().getTranslateX());
                                jclass.getLine().setEndY(jclass.getJParent().getLayoutY()+jclass.getJParent().getTranslateY());
                            }
                        }
                    }
                }
                DataManager.setSaved(false);
            }
        }
    };
    
    public static void setSaved(boolean b){
        isSaved.set(b);
    }
    
    public static void clear(){
        jList.getItems().clear();
        selectedJC = null;
        preSelectedJC = null;
    }
    
    @Override
    public String toString(){
        String str = "";
               
        if (jList.getItems().isEmpty()){
            str ="{}";
        }else{
            str ="{\n"
                +"\"classes\":[\n";
            SimpleIntegerProperty i = new SimpleIntegerProperty(0);
            for (JClass jc : jList.getItems()){
                str = str+ "{\""+i.get()+"\":" + jc.toString() + "},\n";
                i.set(i.get()+1);
            }
               str = str + "{\""+i.get()+"\":{}}]}\n"; 
        }          
        return str;
    }
}
