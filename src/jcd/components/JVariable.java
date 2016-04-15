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
public class JVariable extends Label {
    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleBooleanProperty isStatic;
    private SimpleStringProperty access;
    private Label variableLabel;
    private String vString;
    
    public JVariable(){
        variableLabel = this;
        this.name = new SimpleStringProperty("");
        this.type = new SimpleStringProperty("");
        this.isStatic = new SimpleBooleanProperty(false);
        this.access = new SimpleStringProperty("");
        
        name.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (name)
                variableLabel.setText(toString());
            }
        });
        
        type.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (type)
                variableLabel.setText(toString());
            }
        });
        
        isStatic.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                variableLabel.setText(toString());
            }
        });
        
        access.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (acess)
                variableLabel.setText(toString());
            }
        });
         
    }
    
    
    public SimpleStringProperty getName(){
        return this.name;
        
    }
    
    public SimpleStringProperty getType(){
        return this.type;
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
    
    
    public String toString(){
        vString = "";
        if (access.getValue().equals("public"))
            vString = vString + "+";
        else if (access.getValue().equals("private"))
            vString = vString + "-";
        else 
            vString = vString + "*"; 
        vString = vString + name.getValue() + " : " + type.getValue();

        return vString;
    }
  
    
}
