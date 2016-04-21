/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

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
    
    public JVariable(){
        this.variableLabel = new Label();
        this.name = new SimpleStringProperty("var1");
        this.type = new SimpleStringProperty("int");
        this.isStatic = new SimpleBooleanProperty(false);
        this.access = new SimpleStringProperty("public");
        
        setActions();
        variableLabel.setText(toText());
    }
    public JVariable(String access, String type, String name, boolean b){
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
    
    private void setActions(){
        name.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (name)
                variableLabel.setText(toText());
            }
        });
        
        type.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (type)
                variableLabel.setText(toText());
            }
        });
        
        isStatic.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                variableLabel.setText(toText());
            }
        });
        
        access.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (acess)
                variableLabel.setText(toText());
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
        code = access.get()+" "+isStaticVariable+ type.get()+" "+name.get()+";";
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
