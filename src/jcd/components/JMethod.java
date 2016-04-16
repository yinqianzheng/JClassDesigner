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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

/**
 *
 * @author YinqianZheng
 */
public class JMethod {
    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleBooleanProperty isStatic;
    private SimpleBooleanProperty isAbstract;
    private SimpleStringProperty access;
    private SimpleStringProperty arg1;
    private SimpleStringProperty arg2;
    private SimpleStringProperty arg3;
    private Label methodLabel;
    private MethodBox mb;
    public static final Font ITALIC_FONT = Font.font(
                    "Serif",
                    FontPosture.ITALIC,
                    Font.getDefault().getSize()
                );
    
    public JMethod(MethodBox mbox){
        this.mb = mbox;
        this.methodLabel = new Label();
        this.name = new SimpleStringProperty("");
        this.type = new SimpleStringProperty("");
        this.isStatic = new SimpleBooleanProperty(false);
        this.isAbstract = new SimpleBooleanProperty(false);
        this.access = new SimpleStringProperty("public");
        this.arg1 = new SimpleStringProperty("");
        this.arg2 = new SimpleStringProperty("");
        this.arg3 = new SimpleStringProperty("");
        setActions();
        methodLabel.setText(toText());
    }

    
    public Label getLabel(){
        return methodLabel;
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
    
    public SimpleBooleanProperty getIsAbstract(){
        return this.isAbstract;
    }
    
    public String getArg1(){
        return this.arg1.get();
    }
    
    public String getArg2(){
        return this.arg2.get();
    }
    
    public String getArg3(){
        return this.arg3.get();
    }
    
    public void setStatic(Boolean b){
        this.isStatic.set(b);
    }
    
    public void setAbstract(Boolean b){
        this.isAbstract.set(b);
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
    
    public void setArg1(String arg){
        this.arg1.set(arg);
    }
    
    public void setArg2(String arg){
        this.arg2.set(arg);
    }
    
    public void setArg3(String arg){
        this.arg3.set(arg);
    }
    
    private void setActions(){
        name.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (name)
                methodLabel.setText(toText());
            }
        });
        
        type.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (type)
                methodLabel.setText(toText());
            }
        });
        
        isStatic.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                methodLabel.setText(toText());
            }
        });
        
        isAbstract.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (isAbstract.get() == true)
                    methodLabel.setFont(ITALIC_FONT);
                else
                    methodLabel.setFont(Font.getDefault());
                
                mb.checkAbstractMethod();
            }
        });
        
        access.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (acess)
                methodLabel.setText(toText());
            }
        });
        
        arg1.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (acess)
                methodLabel.setText(toText());
            }
        });
        
        arg2.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (acess)
                methodLabel.setText(toText());
            }
        });
        
        arg3.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (acess)
                methodLabel.setText(toText());
            }
        });
    }
    
    private String toText(){
        String text = "";
        String args = "";
        if (!arg1.get().equals("")){
            args = "arg1 : " + arg1.get();
            if (!arg2.get().equals("")||!arg3.get().equals(""))
                args = args + ", ";
        }
        if (!arg2.get().equals("")){
            args = args + "arg2 : " + arg2.get();
            if (!arg3.get().equals(""))
                args = args + ", ";
        }
        if (!arg3.get().equals(""))
            args = args + "arg3 : " + arg3.get();
        
        
        
        if (isStatic.get() == false){
            if (access.getValue().equals("public"))
                text = "+" + name.get() + "("+ args +" ) : " + type.get();
            else if (access.getValue().equals("private"))
                text = "-" + name.get() + "("+ args +" ) : " + type.get();
            else
                text = name.get() + "("+ args +") : " + type.get();
        }else{
            if (access.getValue().equals("public"))
                text = "+$" + name.get() + "("+ args +" ) : " + type.get();
            else if (access.getValue().equals("private"))
                text = "-$" + name.get() + "("+ args +" ) : " + type.get();
            else
                text = "$" + name.get() + "("+ args +" ) : " + type.get();
        }
        return text;
    }
    
    
    
}
