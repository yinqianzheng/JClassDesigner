/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import jcd.gui.HandleEvent;

/**
 *
 * @author YinqianZheng
 */
public class JClass extends VBox{
    final private VBox name = new VBox();
    final private Label abstractLabel = new Label("{abstract}");
    final private Label interfaceLabel = new Label("{interface}");
    private SimpleBooleanProperty isAbstract;
    private SimpleBooleanProperty isInterface;
    private Label className = new Label("NewClass1");
    private String packageName = "";
    private VariableBox variableBox;
    private MethodBox methodBox;
    private JClass jParent;
    private LinkedList<String> parentList;
    private HashMap<String, JLineGroup> jLineGroupsList;
    private HashMap<String, JLineGroup> usesJLineGroupsList;
    private HashMap<String, JLineGroup> aggregationJLineGroupsList;
    private JLineGroup linkToParent;
    
    
    public JClass(double x, double y){
        parentList = new LinkedList<>();
        jLineGroupsList = new HashMap<String, JLineGroup>();
        usesJLineGroupsList = new HashMap<String, JLineGroup>();
        aggregationJLineGroupsList = new HashMap<String, JLineGroup>();
        isAbstract = new SimpleBooleanProperty(false);
        isInterface = new SimpleBooleanProperty(false);
        variableBox = new VariableBox(this);
        variableBox.getStyleClass().add("variable_method_Boxes_style");
        methodBox = new MethodBox(this);
        methodBox.getStyleClass().add("variable_method_Boxes_style");
        methodBox.getIsContainAbstract().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                setAbstract(t1);
            }
        });
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.getStyleClass().add("classWindow_style");
        name.getStyleClass().add("variable_method_Boxes_style");
        name.getChildren().addAll(className);
        this.getChildren().addAll(name, variableBox, methodBox);
        listenerForInterface();
    }
    
    public HashMap<String, JLineGroup> getUsesJLineGroupsList(){
        return usesJLineGroupsList;
    }
    
    public HashMap<String, JLineGroup> getAggregationJLineGroupsList(){
        return aggregationJLineGroupsList;
    }
    
    public boolean addParent(String str){
        boolean isParent = true;
        
        if (!parentList.isEmpty())
            for (String parent: parentList){
                if (parent.equals(str))
                    isParent = false; 
            }
        
        if (isParent == true)
            parentList.add(str);
        return isParent;
    }
    
    public JLineGroup linkToInterface(JClass parentInterface){
        JLineGroup linkToInterface = JLineGroupFactory.createJLineGroupforInheritance(this, parentInterface, this.getLayoutX()+this.getTranslateX(),
            this.getLayoutY()+this.getTranslateY(),
            parentInterface.getLayoutX()+parentInterface.getTranslateX(),
            parentInterface.getLayoutY()+parentInterface.getTranslateY());
        jLineGroupsList.put(parentInterface.getPackageName()+"."+parentInterface.getClassName(), linkToInterface);
        return linkToInterface;
    }
    
    public void clearJLineGroupList(){
        jLineGroupsList.clear();
    }
    
    public HashMap<String, JLineGroup> getJLineGroupList(){
        return jLineGroupsList;
    }
    
    public LinkedList<String> getInterfaceParentList(){
        return parentList;
    }
            
    public void setClassName(String str){
        className.setText(str);
    }
    
    public void setPackageName(String str){
        packageName = str;
    }
    
    public String getClassName(){
        return className.getText();
    }
    
    public String getPackageName(){
        return packageName;
    }
    
    private void setPosition(double x, double y){
        this.setTranslateX(x);
        this.setTranslateY(y);
    }
    
    public void setAbstract(Boolean b){
        if (!isInterface.get()){
        if (b == true){
            isAbstract.set(true);
            name.getChildren().clear();
            name.getChildren().addAll(className, abstractLabel);
        }else{
            isAbstract.set(false);
            name.getChildren().clear();
            name.getChildren().add(className);
        }
        }
    }
    
    public void setInterface(boolean tf){
        isInterface.set(tf);
    }
    
    public SimpleBooleanProperty getInterface(){
        return isInterface;
    }
    
    public void setJParent(JClass parent){
        jParent = parent;
    }
    
    public JClass getJParent(){
        return jParent;
    }
    
    public void removeJLineGroup(){
        linkToParent = null;
    }
    public JLineGroup setLinkToJParent(String str){
        System.out.println(str);
        System.out.println(this.getPackageName()+"."+ this.getClassName());
        if (jParent!=null){
            if (linkToParent != null){
                if (!(jParent.getPackageName()+"."+ jParent.getClassName()).equals(str)||str.equals("none")){
                    HandleEvent.getWorkPane().root.getChildren().remove(linkToParent);
                    linkToParent = JLineGroupFactory.createJLineGroupforInheritance(this, this.jParent, this.getLayoutX()+this.getTranslateX(),
                        this.getLayoutY()+this.getTranslateY(),
                        jParent.getLayoutX()+jParent.getTranslateX(),
                        jParent.getLayoutY()+jParent.getTranslateY());
                    return linkToParent;
                }
              
            }else{
                linkToParent = JLineGroupFactory.createJLineGroupforInheritance(this, this.jParent, this.getLayoutX()+this.getTranslateX(),
                        this.getLayoutY()+this.getTranslateY(),
                        jParent.getLayoutX()+jParent.getTranslateX(),
                        jParent.getLayoutY()+jParent.getTranslateY());
            return linkToParent;
            }
        }
        return linkToParent;
    }
    

    public VariableBox getVariableBox(){
        return variableBox;
    }
    
    public MethodBox getMethodBox(){
        return methodBox;
    }
    
    public VBox getTitleBox(){
        return name;
    }
    
    public JLineGroup getLine(){
        return linkToParent;
    }
    
    public void setLine(JLineGroup jlg){
        linkToParent = jlg;
    }


    private void listenerForInterface(){
        isInterface.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1){
                    variableBox.setVariableForInterface();
                    methodBox.setMethodForInterface();
                    name.getChildren().clear();
                    name.getChildren().addAll(className, interfaceLabel);
                }else{
                    variableBox.setVariableForClass();
                    methodBox.setMethodForClass();
                    name.getChildren().clear();
                    name.getChildren().add(className);
                    if (!methodBox.getChildren().isEmpty())
                        name.getChildren().add(abstractLabel);
                }
            }
        });
    };
    
    public String toCode(){
        String code = "";
        String isAbstractClass = "";
        String parents = "";
        if (jParent!=null)
            parents = " extends "+jParent.getClassName();
        if (!parentList.isEmpty()){
            parents = parents + " implements ";
            for (String str:parentList){
                parents = parents + str +", ";
            }
            parents = parents.substring(0, parents.length()-2);
        }
        if (isAbstract.get()==true)
            isAbstractClass = "abstract ";
        
        String title ="";
        if (!isInterface.get())
            title = isAbstractClass + "class ";
        else
            title = "interface ";
        
        code = "public " + title +className.getText()+ parents +
                "{\n";
        code = code + variableBox.toCode() +"\n"+ methodBox.toCode() +"\n}\n\n";
        
        return code;
    }
    
    
    // generate json-format string
    @Override
    public String toString(){
        String str = "";
        str = "{\n"
                +"\"class\":[\n"
                +"{\n"
                +"\"0\":{\n"  
                +"\"name\":\""+ className.getText()+"\",\n" 
                +"\"package\":\""+ packageName+"\",\n" 
                +"\"width\":"+ this.getWidth()+",\n" 
                +"\"hight\":"+ this.getHeight()+",\n" 
                +"\"variableBoxWidth\":"+ this.getVariableBox().getWidth()+",\n" 
                +"\"variableBoxHight\":"+ this.getVariableBox().getHeight()+",\n" 
                +"\"methodBoxWidth\":"+ this.getMethodBox().getWidth()+",\n" 
                +"\"methodBoxHight\":"+ this.getMethodBox().getHeight()+",\n" 
                +"\"abstract\":"+ isAbstract.get()+",\n" 
                +"\"interface\":"+ isInterface.get()+",\n" 
                +"\"x\":"+ (this.getLayoutX()+this.getTranslateX())+",\n"
                +"\"y\":"+ (this.getLayoutY()+this.getTranslateY())+"\n"
                +"}\n" 
                +"},\n" 
                +"{\n"
                +"\"1\":{\n"
                +"\"variables\":["
                +this.getVariableBox().toString()
                + "]"
                +"}\n"
                +"},\n"
                +"{\n"
                +"\"2\":{\n"
                +"\"methods\":["
                +this.getMethodBox().toString()
                + "]"
                +"}\n"
                +"},\n"
                +"{\n"
                +"\"3\":{\n"
                +"\"lines\":["
                + getParentLines()
                +getInterfaceLines()
                +getUsesLine()
                +getAggregationLines()
                + "]"
                +"}\n"
                +"}\n"   
                +"]\n"
                +"}\n";          
        return str;
    }
    
    private String getParentLines(){
        String lines = "";
        if (linkToParent==null)
            lines = "{\"extends\":{}},";
        else
            lines = "{\"extends\":"+linkToParent.toString()+ "},";
        return lines;
    }
    
    private String getInterfaceLines(){
        String lines = "";
        if (jLineGroupsList.isEmpty())
            lines = "{\"implements\":[{}]},";
        else{
            lines = "{\"implements\":[";
            for (Map.Entry<String, JLineGroup> entry: jLineGroupsList.entrySet()){
                lines = lines + entry.getValue().toString() + ",";
            }
            
            lines = lines +"{}]},";
        }
        return lines;
    }
    private String getUsesLine(){
        String lines = "";
        if (usesJLineGroupsList.isEmpty())
            lines = "{\"uses\":[{}]},";
        else{
            lines = "{\"uses\":[";
            for (Map.Entry<String, JLineGroup> entry: usesJLineGroupsList.entrySet()){
                lines = lines + entry.getValue().toString() + ",";
            }
            
            lines = lines +"{}]},";
        }
        return lines;
    }
    private String getAggregationLines(){
        String lines = "";
        if (aggregationJLineGroupsList.isEmpty())
            lines = "{\"aggregation\":[{}]}";
        else{
            lines = "{\"aggregation\":[";
            for (Map.Entry<String, JLineGroup> entry: aggregationJLineGroupsList.entrySet()){
                lines = lines + entry.getValue().toString() + ",";
            }
            
            lines = lines +"{}]}";
        }
        return lines;
    }
    
}