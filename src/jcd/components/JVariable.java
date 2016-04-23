/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

import java.util.HashMap;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import jcd.data.DataManager;

/**
 *
 * @author YinqianZheng
 */
public class JVariable {
    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleBooleanProperty isStatic;
    private SimpleStringProperty access;
    private Label variableLabel;
    private VariableBox variableBox;
    private static HashMap<String,Object> returnMap;
    static{
        returnMap = new HashMap();
        returnMap.put("int", 0);
        returnMap.put("double", 0);
        returnMap.put("boolean", true);
        returnMap.put("short", 0);
        returnMap.put("long", 0);
        returnMap.put("String", "\"\" ");
        returnMap.put("float", 0);
        returnMap.put("char", " '0' ");
        returnMap.put("byte", 0);
    }
    
    public JVariable(VariableBox vb){
        variableBox = vb;
        this.variableLabel = new Label();
        this.name = new SimpleStringProperty("var1");
        this.type = new SimpleStringProperty("int");
        this.isStatic = new SimpleBooleanProperty(false);
        this.access = new SimpleStringProperty("public");
        
        setActions();
        variableLabel.setText(toText());
    }
    public JVariable(VariableBox vb, String access, String type, String name, boolean b){
        variableBox = vb;
        this.variableLabel = new Label();
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.isStatic = new SimpleBooleanProperty(b);
        this.access = new SimpleStringProperty(access);
        
        setActions();
        variableLabel.setText(toText());
    }
    
    public Label getLabel(){
        return variableLabel;
    }
    
    public VariableBox getVariableBox(){
        return variableBox;
    }
    private void setActions(){
        name.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (name)
                variableLabel.setText(toText());
                DataManager.setSaved(false);
            }
        });
        
        type.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (type)
                variableLabel.setText(toText());
                DataManager.setSaved(false);
            }
        });
        
        isStatic.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                variableLabel.setText(toText());
                DataManager.setSaved(false);
            }
        });
        
        access.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (acess)
                variableLabel.setText(toText());
                DataManager.setSaved(false);
            }
        });
    }
    
    public String getName(){
        return this.name.get();
        
    }
    
    public String getType(){
        return this.type.get();
    }
    
    public SimpleStringProperty getAccess(){
        return this.access;
    }
    
    public SimpleBooleanProperty getIsStatic(){
        return this.isStatic;
    }
    
    public void setStatic(Boolean b){
        this.isStatic.set(b);
    }
    
    public void setName(String name){
        this.name.set(name);
    }
    
    public void setType(String type){
        this.type.set(type);
    }
    
    public void setAccess(String access){
        this.access.set(access);
    }
    
    
    // generate label's text
    public String toText(){
        String str = "";
        
        if (isStatic.get() == false){
            if (access.getValue().equals("public"))
                str = "+" + name.get() + " : " + type.get();
            else if (access.getValue().equals("private"))
                str = "-"+ name.get() + " : " + type.get();
            else if (access.getValue().equals("protected"))
                str = "#"+ name.get() + " : " + type.get();
            else
                str = name.get() + " : " + type.get();
        }else{
            if (access.getValue().equals("public"))
                str = "+$" + name.get() + " : " + type.get();
            else if (access.getValue().equals("private"))
                str = "-$"+ name.get() + " : " + type.get();
            else if (access.getValue().equals("protected"))
                str = "#$"+ name.get() + " : " + type.get();
            else
                str = "$" + name.get() + " : " + type.get();
        }
        
        return str;
    }
    
    // generate code for the variable
    public String toCode(){
        String isStaticVariable = "";
        if (isStatic.get()==true)
            isStaticVariable = "static ";
        String code = "";
        code = access.get()+" "+isStaticVariable+ type.get()+" "+name.get()+" ";
        if (variableBox.getJClass().getInterface().get()){
            if (returnMap.containsKey(type.get()))
                code = code + "= " + String.valueOf(returnMap.get(type.get()));
        }
        code = code +";";
        return code;
    }
    
    @Override
    public String toString(){
        String str = "{\n\"name\":\""+name.get()+"\",\n"
                + "\"type\":\""+type.get()+"\",\n"
                + "\"access\":\""+access.get()+"\",\n"
                + "\"isStatic\":"+isStatic.get()+"\n}";
        return str;
    }
}
