/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import jcd.components.JClass;
import jcd.controller.JFileManager;
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
    private static LinkedList<String> historyList = new LinkedList<String>();
    private static JClass preSelectedJC;
    private static JClass selectedJC;
    final private static ComboBox<String> parentList = new ComboBox<String>();
    final private static DropShadow highlight = new DropShadow(20, Color.YELLOW);
    private static SimpleBooleanProperty isSaved = new SimpleBooleanProperty(true);
    private static SimpleBooleanProperty hasDirectory = new SimpleBooleanProperty(false);
    private static String directory = "";
    private static double sceneX;
    private static double sceneY;
    private static double translateX;
    private static double translateY;
    private static double width;
    private static double hight;
    private static double tHight;
    private static double vHight;
    private static double mHight;
    public static SimpleBooleanProperty isResizeMode = new SimpleBooleanProperty(false);
    public static SimpleIntegerProperty currentCursor = new SimpleIntegerProperty(0);
    private static boolean isMoved = false;

    private DataManager() {
        setListeners();
    }
    
    public static DataManager getInstance(HandleEvent e){
        if(handler==null){
            dm = new DataManager();
            handler = e;
        }
        return dm;
    }
    
    public static TableView<JClass> getCurrentList(){
        return jList;
    }
    
    public static SimpleBooleanProperty isSaved(){
        return isSaved;
    }
    
    public static SimpleBooleanProperty hasDirectory(){
        return hasDirectory;
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
            isMoved = false;
            if (WorkSpace.isSelectMode==true||isResizeMode.get()==true){
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
                sceneX = click.getSceneX();
                sceneY = click.getSceneY();
                translateX = ((JClass)click.getSource()).getTranslateX();
                translateY = ((JClass)click.getSource()).getTranslateY();
                width = ((JClass)click.getSource()).getWidth();
                hight = ((JClass)click.getSource()).getHeight();
                tHight = ((JClass)click.getSource()).getTitleBox().getHeight();
                vHight = ((JClass)click.getSource()).getVariableBox().getHeight();
                mHight = ((JClass)click.getSource()).getMethodBox().getHeight()+2;
            } catch (Exception e) {
            }  
        }
    };
    
    private static EventHandler dragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent click) {
            if (HandleEvent.getWorkPane().isSelectMode==true){
                double offsetX = click.getSceneX() - sceneX;
                double offsetY = click.getSceneY() - sceneY;
                double newTranslateX = translateX + offsetX/HandleEvent.getWorkPane().getZoomValue();
                double newTranslateY = translateY + offsetY/HandleEvent.getWorkPane().getZoomValue();

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
            }else if (isResizeMode.get()==true){
                double ratioX = (click.getSceneX() - sceneX)/width;
                double ratioY = (click.getSceneY() - sceneY)/hight;
                
                if (!(ratioX <= -1 || ratioY <= -1)){
                    selectedJC.setPrefSize(width*(1+ratioX), (hight-tHight)*(1+ratioY));
                    selectedJC.getVariableBox().setPrefHeight(vHight*(1+ratioY));
                    selectedJC.getMethodBox().setPrefHeight(mHight*(1+ratioY));
                }  
            }
            isMoved = true;
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
            }
            // addToHistoryList();
            if (isMoved)
                setSaved(false);
            // add size to json-format String
        }
    };
    
    private void setListeners(){
        // renew parent list
        jList.getItems().addListener(new ListChangeListener<JClass>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends JClass> change) {
                renewParentList();
                HandleEvent.getWorkPane().buttonMap.get("export code").setDisable(jList.getItems().isEmpty());
                HandleEvent.getWorkPane().buttonMap.get("export photo").setDisable(jList.getItems().isEmpty());
                HandleEvent.getWorkPane().buttonMap.get("save as").setDisable(jList.getItems().isEmpty());
                HandleEvent.getWorkPane().buttonMap.get("save").setDisable(jList.getItems().isEmpty());
                HandleEvent.getWorkPane().buttonMap.get("resize").setDisable(jList.getItems().isEmpty());
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
        
        isResizeMode.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1){
                    System.out.println(t1);
                    for (JClass jc : jList.getItems()){
                        jc.setOnMouseEntered(e->jc.setCursor(Cursor.SE_RESIZE));
                    }}
                else{
                    System.out.println(t1);
                    for (JClass jc : jList.getItems()){
                        jc.setOnMouseEntered(e->jc.setCursor(Cursor.OPEN_HAND));
                    }}
            }
        });
        
        currentCursor.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                System.out.println("nani");
                if (currentCursor.get()<= 0)
                    HandleEvent.getWorkPane().buttonMap.get("undo").setDisable(true);
                else
                    HandleEvent.getWorkPane().buttonMap.get("undo").setDisable(false);
                    
                if (currentCursor.get()>= historyList.size()-1)
                    HandleEvent.getWorkPane().buttonMap.get("redo").setDisable(true);
                else
                    HandleEvent.getWorkPane().buttonMap.get("redo").setDisable(false);
                
                try {
                        if (!historyList.isEmpty())
                            HandleEvent.getWorkPane().reload();
                            jList.getItems().clear();
                            createJsonObjectByString(historyList.get(currentCursor.get()));
                    } catch (IOException ex) {
                        Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
            }
        });
        historyList.add("{}");
    }
    
    public static LinkedList<String> getHistoryList(){
        return historyList;
    }

        
    public static void setSaved(boolean b){
        isSaved.set(b);
        if (b==false){
            addToHistoryList();
            System.out.println("addtohistorylist");
        }
    }
    
    public static void clear(){
        jList.getItems().clear();
        selectedJC = null;
        preSelectedJC = null;
    }
    
    public static void createJsonObjectByString(String str) throws IOException {
	InputStream is = new StringBufferInputStream(str);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
        JFileManager.createClasses(json);
    }
    
    public static void addToHistoryList(){
        if (currentCursor.get() != historyList.size()-1)
            historyList.subList(currentCursor.get()+1, historyList.size()).clear();
        
        
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
        historyList.add(str);
        currentCursor.set(DataManager.currentCursor.get()+1);  
    }
    
    @Override
    public String toString(){
        String str = "";
               
        if (historyList.isEmpty()){
            str ="{}";
        }else{
            str ="{\n"
                +"\"history\":[\n";
            SimpleIntegerProperty i = new SimpleIntegerProperty(0);
            for (Iterator<String> it = historyList.iterator(); it.hasNext();) {
                String str_jList = it.next();
                str = str+ "{\""+i.get()+"\":" + str_jList + "},\n";
                i.set(i.get()+1);
            }
               str = str + "{\""+i.get()+"\":{}}]\n"
    //                   + "\"cursor\":" + index
                       + "}\n"; 
        }          
        return str;
    }
}
