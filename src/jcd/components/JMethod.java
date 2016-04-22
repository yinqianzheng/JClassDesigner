/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import jcd.data.DataManager;

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
    public static HashMap<String,Object> returnMap;
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
    
    public static final Font ITALIC_FONT = Font.font(
                    "Serif",
                    FontPosture.ITALIC,
                    Font.getDefault().getSize()
                );
    
    public JMethod(MethodBox mbox){  
        this.mb = mbox;
        this.methodLabel = new Label();
        this.name = new SimpleStringProperty("method1");
        this.type = new SimpleStringProperty("void");
        this.isStatic = new SimpleBooleanProperty(false);
        this.isAbstract = new SimpleBooleanProperty(false);
        this.access = new SimpleStringProperty("public");
        this.arg1 = new SimpleStringProperty("");
        this.arg2 = new SimpleStringProperty("");
        this.arg3 = new SimpleStringProperty("");
        setActions();
        methodLabel.setText(toText());
    }
    
    public JMethod(MethodBox mbox,  String methodAccess, String methodType, String methodName, boolean s,
            boolean a, String arg1, String arg2, String arg3){  
        this.mb = mbox;
        this.methodLabel = new Label();
        this.name = new SimpleStringProperty(methodName);
        this.type = new SimpleStringProperty(methodType);
        this.isStatic = new SimpleBooleanProperty(s);
        this.isAbstract = new SimpleBooleanProperty(a);
        this.access = new SimpleStringProperty(methodAccess);
        this.arg1 = new SimpleStringProperty(arg1);
        this.arg2 = new SimpleStringProperty(arg2);
        this.arg3 = new SimpleStringProperty(arg3);
        setActions();
        methodLabel.setText(toText());
        if (a==true)
            methodLabel.setFont(ITALIC_FONT);  
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
                DataManager.setSaved(false);
            }
        });
        
        type.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (type)
                methodLabel.setText(toText());
                DataManager.setSaved(false);
            }
        });
        
        isStatic.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                methodLabel.setText(toText());
                DataManager.setSaved(false);
                if (isStatic.get() == true)
                    isAbstract.set(false);
            }
        });
        
        isAbstract.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (isAbstract.get() == true){
                    methodLabel.setFont(ITALIC_FONT);
                    isStatic.set(false);
                    access.set("public");
                }else{
                    methodLabel.setFont(Font.getDefault());
                }
                DataManager.setSaved(false);
                mb.checkAbstractMethod();
            }
        });
        
        access.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (acess)
                methodLabel.setText(toText());
                DataManager.setSaved(false);
            }
        });
        
        arg1.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (acess)
                methodLabel.setText(toText());
                DataManager.setSaved(false);
            }
        });
        
        arg2.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (acess)
                methodLabel.setText(toText());
                DataManager.setSaved(false);
            }
        });
        
        arg3.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (acess)
                methodLabel.setText(toText());
                DataManager.setSaved(false);
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
    
    
    public String toCode(){
        String isStaticMethod = "";
        String args= "";
        if (!arg1.get().equals("")){
            args =  arg1.get() + " arg1";
            if (!arg2.get().equals("")||!arg3.get().equals(""))
                args = args + ", ";
        }
        if (!arg2.get().equals("")){
            args = args + arg2.get() + " arg2";
            if (!arg3.get().equals(""))
                args = args + ", ";
        }
        if (!arg3.get().equals(""))
            args = args +arg3.get()  + " arg3";
        
        
        if (isStatic.get()==true)
            isStaticMethod = "static ";
        
        String initValue = "";
        if (returnMap.containsKey(type.get()))
            initValue = String.valueOf(returnMap.get(type.get()));
        else
            initValue = "new "+type.get()+"()";
        
        String code = "";
        if (isAbstract.get()==true){
            code = access.get()+" abstract "+ type.get()+" "+name.get()+"();";
        }else{
            code = access.get()+" "+isStaticMethod+ type.get()+" "+name.get()+"("+ args +"){\n";
            if (!type.get().equals("void"))
                code = code + " "+type.get()+" value = " +initValue +";\n  return value;";
            code = code + "\n}\n";
        }
        return code;
    }
    
    @Override
    public String toString(){
        String str =  "{\n\"name\":\""+name.get()+"\",\n"
                + "\"type\":\""+type.get()+"\",\n"
                + "\"access\":\""+access.get()+"\",\n"
                + "\"isStatic\":"+isStatic.get()+",\n"
                + "\"isAbstract\":"+isAbstract.get()+",\n"
                + "\"arg1\":\""+arg1.get()+"\",\n"
                + "\"arg2\":\""+arg2.get()+"\",\n"
                + "\"arg3\":\""+arg3.get()+"\"\n"
                + "}";
        
        
        return str;
    }
    
}
