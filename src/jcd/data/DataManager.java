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
import java.util.Map;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import jcd.components.JClass;
import jcd.components.JLineGroup;
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
    public static SimpleBooleanProperty isInterface = new SimpleBooleanProperty(false);
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
        public void handle(MouseEvent pressed) {
            isMoved = false;
            if (WorkSpace.isSelectMode==true||isResizeMode.get()==true){
                if (selectedJC!=null)
                    preSelectedJC = selectedJC;
                selectedJC = ((JClass)pressed.getSource());
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
//                HandleEvent.getWorkPane().interfaceCheckBox.setSelected(selectedJC.getInterface().get());
                try {
                    HandleEvent.getWorkPane().parentsList.setValue(selectedJC.getJParent().getPackageName()+"."+
                    selectedJC.getJParent().getClassName());    
                } catch (Exception e) {
                }
            }
            
            if (selectedJC.getJParent()!=null){
                selectedJC.getLine().getEndPoint().setTranslateY(selectedJC.getLine().getEndY()-selectedJC.getLine().getEndPoint().getCenterY());
                selectedJC.getLine().getStartPoint().markTranslateValue();
            }
            
            if (!selectedJC.getJLineGroupList().isEmpty()){
                for (Map.Entry<String, JLineGroup> entry: selectedJC.getJLineGroupList().entrySet()){
                    entry.getValue().getStartPoint().markTranslateValue();
                }
//                for (JLineGroup jlg: selectedJC.getJLineGroupList()){
//                    jlg.getStartPoint().markTranslateValue();
//                }
            }
            
            if (!selectedJC.getUsesJLineGroupsList().isEmpty()){
                for (Map.Entry<String, JLineGroup> entry: selectedJC.getUsesJLineGroupsList().entrySet()){
                    entry.getValue().getStartPoint().markTranslateValue();
                }
            }
            
            if (!selectedJC.getAggregationJLineGroupsList().isEmpty()){
                for (Map.Entry<String, JLineGroup> entry: selectedJC.getAggregationJLineGroupsList().entrySet()){
                    entry.getValue().getStartPoint().markTranslateValue();
                }
//                for (JLineGroup jlg: selectedJC.getAggregationJLineGroupsList()){
//                    jlg.getStartPoint().markTranslateValue();
//                }
            }
            

            if (!selectedJC.getInterface().get()){
                for (JClass jclass : jList.getItems()){
                    if (jclass.getJParent()!=null){
                        if (jclass.getJParent().equals(selectedJC)){
                            jclass.getLine().getEndPoint().markTranslateValue();
                        }
                    }
                }
            }else{
                for (JClass jclass : jList.getItems()){
                    if (!jclass.getJLineGroupList().isEmpty()){
                        //int index = jclass.getInterfaceParentList().indexOf(selectedJC.getPackageName()+"."+selectedJC.getClassName());
                        if (jclass.getJLineGroupList().containsKey(selectedJC.getPackageName()+"."+selectedJC.getClassName())){
                            jclass.getJLineGroupList().get(selectedJC.getPackageName()+"."+selectedJC.getClassName()).getEndPoint().markTranslateValue();
                        }
                    }
                }
            }
            
            for (JClass jclass : jList.getItems()){
                    //int index = jclass.getAggregationClassList().indexOf(selectedJC.getClassName());
                    if (jclass.getAggregationJLineGroupsList().containsKey(selectedJC.getClassName())){
                        jclass.getAggregationJLineGroupsList().get(selectedJC.getClassName()).getEndPoint().markTranslateValue();
                    }
            }
            
            for (JClass jclass : jList.getItems()){
                    //int index = jclass.getUsesClassList().indexOf(selectedJC.getClassName());
                    if (jclass.getUsesJLineGroupsList().containsKey(selectedJC.getClassName())){
                        jclass.getUsesJLineGroupsList().get(selectedJC.getClassName()).getEndPoint().markTranslateValue();
                    }
            }
            
            
            
            
            
            
            
            
            try {
                sceneX = pressed.getSceneX();
                sceneY = pressed.getSceneY();
                translateX = ((JClass)pressed.getSource()).getTranslateX();
                translateY = ((JClass)pressed.getSource()).getTranslateY();
                width = ((JClass)pressed.getSource()).getWidth();
                hight = ((JClass)pressed.getSource()).getHeight();
                tHight = ((JClass)pressed.getSource()).getTitleBox().getHeight();
                vHight = ((JClass)pressed.getSource()).getVariableBox().getHeight();
                mHight = ((JClass)pressed.getSource()).getMethodBox().getHeight()+2;
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
                    for (Map.Entry<String, JLineGroup> entry: selectedJC.getJLineGroupList().entrySet()){
                        entry.getValue().getStartPoint().addOffset(offsetX/HandleEvent.getWorkPane().getZoomValue(), offsetY/HandleEvent.getWorkPane().getZoomValue());
                    }
                    for (Map.Entry<String, JLineGroup> entry: selectedJC.getAggregationJLineGroupsList().entrySet()){
                        entry.getValue().getStartPoint().addOffset(offsetX/HandleEvent.getWorkPane().getZoomValue(), offsetY/HandleEvent.getWorkPane().getZoomValue());
                    }
//                    for (JLineGroup jlg: selectedJC.getAggregationJLineGroupsList()){
//                        jlg.getStartPoint().addOffset(offsetX/HandleEvent.getWorkPane().getZoomValue(), offsetY/HandleEvent.getWorkPane().getZoomValue());
//                    }
                    for (Map.Entry<String, JLineGroup> entry: selectedJC.getUsesJLineGroupsList().entrySet()){
                        entry.getValue().getStartPoint().addOffset(offsetX/HandleEvent.getWorkPane().getZoomValue(), offsetY/HandleEvent.getWorkPane().getZoomValue());
                    }
                    selectedJC.getLine().getStartPoint().addOffset(offsetX/HandleEvent.getWorkPane().getZoomValue(), offsetY/HandleEvent.getWorkPane().getZoomValue());
                } catch (Exception e) {
                }
                
                if (!selectedJC.getInterface().get()){
                    for (JClass jclass : jList.getItems()){
                         System.out.println("class");
                        if (jclass.getJParent()!=null){
                            if (jclass.getJParent().equals(selectedJC)){
                                jclass.getLine().getEndPoint().addOffset(offsetX/HandleEvent.getWorkPane().getZoomValue(), offsetY/HandleEvent.getWorkPane().getZoomValue());
                            }
                        }
                    }
                }else{
                    for (JClass jclass : jList.getItems()){
                        if (!jclass.getJLineGroupList().isEmpty()){
                            //int index = jclass.getInterfaceParentList().indexOf(selectedJC.getPackageName()+"."+selectedJC.getClassName());
                            if (jclass.getJLineGroupList().containsKey(selectedJC.getPackageName()+"."+selectedJC.getClassName())){
                                jclass.getJLineGroupList().get(selectedJC.getPackageName()+"."+selectedJC.getClassName()).getEndPoint().addOffset(offsetX/HandleEvent.getWorkPane().getZoomValue(), offsetY/HandleEvent.getWorkPane().getZoomValue());
                            }
                        }
                    }
                    
                }
                
                for (JClass jclass : jList.getItems()){
//                    int index = jclass.getAggregationClassList().indexOf(selectedJC.getClassName());
                    if (jclass.getAggregationJLineGroupsList().containsKey(selectedJC.getClassName())){
                        jclass.getAggregationJLineGroupsList().get(selectedJC.getClassName()).getEndPoint().addOffset(offsetX/HandleEvent.getWorkPane().getZoomValue(), offsetY/HandleEvent.getWorkPane().getZoomValue());
                    }
                }
                
                for (JClass jclass : jList.getItems()){
                    //int index = jclass.getUsesClassList().indexOf(selectedJC.getClassName());
                    if (jclass.getUsesJLineGroupsList().containsKey(selectedJC.getClassName())){
                        jclass.getUsesJLineGroupsList().get(selectedJC.getClassName()).getEndPoint().addOffset(offsetX/HandleEvent.getWorkPane().getZoomValue(), offsetY/HandleEvent.getWorkPane().getZoomValue());
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
//            System.out.println(selectedJC.getLayoutX()+selectedJC.getTranslateX());
//                System.out.println(selectedJC.getLayoutY()+selectedJC.getTranslateY());
            if (HandleEvent.getWorkPane().isGridSnapActived()){
                if (HandleEvent.getWorkPane().isSelectMode==true){
                    double x = ((JClass)(release.getSource())).getLayoutX() + ((JClass)(release.getSource())).getTranslateX();
                    double y = ((JClass)(release.getSource())).getLayoutY() + ((JClass)(release.getSource())).getTranslateY();
                    
                    double pointOffsetX =((int)(x/40)*40) - x;
                    double pointOffsetY =((int)(y/40)*40) - y;
                    System.out.println(pointOffsetX+" "+pointOffsetY);
                    x = (int)(x/40)*40;
                    y = (int)(y/40)*40;
                    
                    
                    
                    ((JClass)(release.getSource())).setTranslateX(x-((JClass)(release.getSource())).getLayoutX());
                    ((JClass)(release.getSource())).setTranslateY(y-((JClass)(release.getSource())).getLayoutY());
                    
                    try {
                        if (selectedJC.getLine()!=null)
                            selectedJC.getLine().getStartPoint().addOffsetForMouseReleased(pointOffsetX, pointOffsetY);
                        if (!selectedJC.getAggregationJLineGroupsList().isEmpty())
                            for (Map.Entry<String, JLineGroup> entry: selectedJC.getAggregationJLineGroupsList().entrySet()){
                                entry.getValue().getStartPoint().addOffsetForMouseReleased(pointOffsetX, pointOffsetY);
                            }
//                            for (JLineGroup jlg: selectedJC.getAggregationJLineGroupsList()){
//                                jlg.getStartPoint().addOffsetForMouseReleased(pointOffsetX, pointOffsetY);
//                            }
                        if (!selectedJC.getJLineGroupList().isEmpty())
                            for (Map.Entry<String, JLineGroup> entry: selectedJC.getJLineGroupList().entrySet()){
                                entry.getValue().getStartPoint().addOffsetForMouseReleased(pointOffsetX, pointOffsetY);
                            }
//                            for (JLineGroup jlg : selectedJC.getJLineGroupList()){
//                                jlg.getStartPoint().addOffsetForMouseReleased(pointOffsetX, pointOffsetY);
//                            }
                        if (!selectedJC.getUsesJLineGroupsList().isEmpty())
                            for (Map.Entry<String, JLineGroup> entry: selectedJC.getUsesJLineGroupsList().entrySet()){
                                entry.getValue().getStartPoint().addOffsetForMouseReleased(pointOffsetX, pointOffsetY);
                            }
                    } catch (Exception e) {
                        System.err.println(e);
                    }
//                
                if (!selectedJC.getInterface().get()){
                    for (JClass jclass : jList.getItems()){
                         System.out.println("class");
                        if (jclass.getJParent()!=null){
                            if (jclass.getJParent().equals(selectedJC)){
                                jclass.getLine().getEndPoint().addOffsetForMouseReleased(pointOffsetX, pointOffsetY);
                            }
                        }
                    }
                }else{
                    for (JClass jclass : jList.getItems()){
                        if (jclass.getInterfaceParentList()!=null){
                            //int index = jclass.getInterfaceParentList().indexOf(selectedJC.getPackageName()+"."+selectedJC.getClassName());
                            if (jclass.getJLineGroupList().containsKey(selectedJC.getPackageName()+"."+selectedJC.getClassName())){
                                jclass.getJLineGroupList().get(selectedJC.getPackageName()+"."+selectedJC.getClassName()).getEndPoint().addOffsetForMouseReleased(pointOffsetX, pointOffsetY);
                            }
                        }
                    }
                }
                
                for (JClass jclass : jList.getItems()){
                    //int index = jclass.getAggregationClassList().indexOf(selectedJC.getClassName());
                    if (jclass.getAggregationJLineGroupsList().containsKey(selectedJC.getClassName())){
                        jclass.getAggregationJLineGroupsList().get(selectedJC.getClassName()).getEndPoint().addOffsetForMouseReleased(pointOffsetX, pointOffsetY);
                    }
                }
                
                for (JClass jclass : jList.getItems()){
                    //int index = jclass.getUsesClassList().indexOf(selectedJC.getClassName());
                    if (jclass.getUsesJLineGroupsList().containsKey(selectedJC.getClassName())){
                        jclass.getUsesJLineGroupsList().get(selectedJC.getClassName()).getEndPoint().addOffsetForMouseReleased(pointOffsetX, pointOffsetY);
                    }
                }
////                  
                }
            }
            // addToHistoryList();
            if (isMoved)
                setUnSaved();
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
                String preParent = "none";
                try {                   
                    if (t1.equals("none")){
                    selectedJC.setJParent(null);
                    HandleEvent.getWorkPane().root.getChildren().remove(selectedJC.getLine());
                    selectedJC.removeJLineGroup();
                    //selectedJC.removeJLineGroup();
                    }else {
                        if (selectedJC.getJParent()!=null){
                            preParent = selectedJC.getJParent().getPackageName()+"."+ selectedJC.getJParent().getClassName();
                        }
                        for (JClass jclass : jList.getItems()){
                            if ((jclass.getPackageName()+"."+ jclass.getClassName()).equals(t1)){
                                if (jclass.getInterface().get()){
                                    if (selectedJC.addParent(t1))
                                        HandleEvent.getWorkPane().root.getChildren().add(selectedJC.linkToInterface(jclass));
                                }else{
                                    selectedJC.setJParent(jclass);
                                    try {
                                        HandleEvent.getWorkPane().root.getChildren().add(selectedJC.setLinkToJParent(preParent));
                                    } catch (Exception e) {
                                    }
                                break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                } 
            }
        });
        
        parentList.setEditable(true);

        
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
        //HandleEvent.getWorkPane().root.setOnMouseClicked(clicked);
        historyList.add("{}");
    }
    
    private static EventHandler lineBePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent clicked) {
                if (selectedJC!=null)
                    preSelectedJC = selectedJC;
                selectedJC = null;
                if (preSelectedJC!=null)
                    preSelectedJC.setEffect(null);
                
                HandleEvent.getWorkPane().getAddVariableButton().setDisable(true);
                HandleEvent.getWorkPane().getDeleteVariableButton().setDisable(true);
                HandleEvent.getWorkPane().getAddMethodButton().setDisable(true);
                HandleEvent.getWorkPane().getDeleteMethodButton().setDisable(true);
                HandleEvent.getWorkPane().setVariablePane(null);
                HandleEvent.getWorkPane().setMethodPane(null);
                HandleEvent.getWorkPane().setClassNameInput(null);   
                HandleEvent.getWorkPane().setPackageNameInput(null);
//                HandleEvent.getWorkPane().interfaceCheckBox.setSelected(false);
        }
    };
    
    public static LinkedList<String> getHistoryList(){
        return historyList;
    }

        
    public static void setSaved(boolean b){
        isSaved.set(b);
        if (b==false){
        }
    }
    
    public static void setUnSaved(){
        isSaved.set(false);
        System.out.println("addtohistorylist");
        //addToHistoryList();
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
    
    //@Override
    public String tosString(){
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
