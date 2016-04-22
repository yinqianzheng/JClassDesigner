/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import jcd.data.DataManager;
import jcd.gui.WorkSpace;

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
    public double sceneX;
    public double sceneY;
    public double translateX;
    public double translateY;
    VariableBox variableBox;
    MethodBox methodBox;
    private JClass jParent;
    private Line linkToParent;
    
    
    public JClass(double x, double y){
        isAbstract = new SimpleBooleanProperty(false);
        variableBox = new VariableBox();
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
        this.setMinWidth(100);
        this.getStyleClass().add("classWindow_style");
        name.getStyleClass().add("variable_method_Boxes_style");
        name.getChildren().addAll(className);
        this.getChildren().addAll(name, variableBox, methodBox);    
    }

    
    public void setJParent(JClass parent){
        jParent = parent;
    }
    
    public JClass getJParent(){
        return jParent;
    }
    
    public Line setLinkToJParent(){
        if (jParent!=null){
            if (linkToParent != null){
                linkToParent.setEndX(jParent.getLayoutX()+jParent.getTranslateX());
                linkToParent.setEndY(jParent.getLayoutY()+jParent.getTranslateY());
            }else{
                linkToParent = new Line(this.getLayoutX()+this.getTranslateX(),
                        this.getLayoutY()+this.getTranslateY(),
                        jParent.getLayoutX()+jParent.getTranslateX(),
                        jParent.getLayoutY()+jParent.getTranslateY());
            return linkToParent;
            }
        }
        return linkToParent;
    }
    
    public void setEndPoint(double x, double y){
        if (linkToParent!=null){
            linkToParent.setEndX(x);
            linkToParent.setEndY(y);
        }
    }
    
    public Line getLine(){
        return linkToParent;
    }
    
    public VariableBox getVariableBox(){
        return variableBox;
    }
    
    public MethodBox getMethodBox(){
        return methodBox;
    }
    
    public void setAbstract(Boolean b){
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
    
    private void setPosition(double x, double y){
        this.setTranslateX(x);
        this.setTranslateY(y);
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
    
    
    public void changeSceneX(double x){
        this.sceneX = x;
    }
    
    public void changeSceneY(double y){
        this.sceneY = y;
    }
    
    public void changeTranslateX(double x){
        this.translateX = x;
    }
    
    public void changeTranslateY(double y){
        this.translateY = y;
    }
    
    public void clone(JClass j){
        ObservableList<JVariable> jvList = this.getVariableBox().getVariableTable().getItems();
        for (JVariable jv : jvList){
            j.getVariableBox().addVariable(jv);
        }
        ObservableList<JMethod> jmList = this.getMethodBox().getMethodTable().getItems();
        for (JMethod jm : jmList){
            j.getMethodBox().addMethod(jm);
        }
    }
    
    public String toCode(){
        String code = "";
        String isAbstractClass = "";
        if (isAbstract.get()==true)
            isAbstractClass = "abstract ";
        
        code = "public " + isAbstractClass +"class "+className.getText()+
                "{\n";
        code = code + variableBox.toCode() +"\n"+ methodBox.toCode() +"\n}\n";
        
        return code;
    }
    
    @Override
    public String toString(){
        String str = "";
        str = "{\n"
                +"\"class\":[\n"
                
                +"{\n"
                +"\"0\":{\n"  
                +"\"name\":\""+ className.getText()+"\",\n" 
                +"\"package\":\""+ packageName+"\",\n" 
                +"\"abstract\":"+ isAbstract.get()+",\n" 
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
                +"}\n"
                +"]\n"
                +"}\n";
                  
        return str;
    }
    
}