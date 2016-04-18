/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.LinkedList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
    private static TableView<JClass> jList = new TableView<JClass>();
    private static JClass preSelectedJC;
    private static JClass selectedJC;
    final private static ComboBox<String> parentList = new ComboBox<String>();
    final private static DropShadow highlight = new DropShadow(20, Color.YELLOW);

    private DataManager() {
        jList.getItems().addListener(new ListChangeListener<JClass>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends JClass> change) {
                renewParentList();
            }
        });
        
        // update jParent
        parentList.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                
                for (JClass jclass : jList.getItems()){
                    if ((jclass.getPackageName()+"."+ jclass.getClassName()).equals(t1)){
                        selectedJC.setJParent(jclass);
                        selectedJC.setJParentName();
                        try {
                            HandleEvent.getWorkPane().root.getChildren().add(selectedJC.setLinkToJParent());
                        } catch (Exception e) {
                        }
                        break;
                    }
                }
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
        j.setOnMouseClicked(select);
        if (preSelectedJC!=null)
            preSelectedJC.setEffect(null);
    }
    
        
    public static void removeClass(JClass jc){
        jList.getItems().remove(jc);
    }
    
    public static String getSelectedJCName(){
        return selectedJC.getClassName();
    }
    
    
    private static HandleEvent getHandler(){
        return handler;
    }
    private static EventHandler select = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent click) { 
            if (WorkSpace.isSelectMode==true){
                if (selectedJC!=null)
                    preSelectedJC = selectedJC;
                selectedJC = ((JClass)click.getSource());
            
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
                try {
                    HandleEvent.getWorkPane().parentsList.setValue(selectedJC.getJParent().getPackageName()+"."+
                    selectedJC.getJParent().getClassName());    
                } catch (Exception e) {
                }
            }
        }
    };

    public static void clear(){
        jList.getItems().clear();
        selectedJC = null;
        preSelectedJC = null;
    }
    
}
