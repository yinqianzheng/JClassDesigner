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
import jcd.gui.WorkSpace;

/**
 *
 * @author YinqianZheng
 */
public class JClass extends VBox{
    final private VBox name = new VBox();
    final private Label isAbstract = new Label("{abstract}");
    private Label className = new Label("NewClass");
    private String packageName = "";
    private double sceneX;
    private double sceneY;
    private double translateX;
    private double translateY;
    VariableBox variableBox;
    MethodBox methodBox;
    private SimpleStringProperty jParent = new SimpleStringProperty();
    
    
    public JClass(double x, double y){
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
        this.setOnMousePressed(pressed);
        this.setOnMouseDragged(dragged);
    }
    
    public VariableBox getVariableBox(){
        return variableBox;
    }
    
    public MethodBox getMethodBox(){
        return methodBox;
    }
    
    public void setAbstract(Boolean b){
        if (b == true){
            name.getChildren().clear();
            name.getChildren().addAll(className, isAbstract);
        }else{
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
    
    
    EventHandler pressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent click) {
            
            sceneX = click.getSceneX();
            sceneY = click.getSceneY();
            
            translateX = ((JClass)click.getSource()).getTranslateX();
            translateY = ((JClass)click.getSource()).getTranslateY();
        }
    };
    
    EventHandler dragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent click) {
            if (WorkSpace.isSelectMode==true){
                double offsetX = click.getSceneX() - sceneX;
                double offsetY = click.getSceneY() - sceneY;
                double newTranslateX = translateX + offsetX;
                double newTranslateY = translateY + offsetY;

                ((JClass)(click.getSource())).setTranslateX(newTranslateX);
                ((JClass)(click.getSource())).setTranslateY(newTranslateY);
            }
        }
    };

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
    
}
