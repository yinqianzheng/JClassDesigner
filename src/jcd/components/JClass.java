/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

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
    final private VBox variables = new VBox();
    final private VBox methods = new VBox();
    final private Label classType = new Label("{abstract}");
    private Label className = new Label("NewClass");
    private String packageName = "";
    private TableView variableTableView;
    private TableView methodTableView;
    //final DropShadow highlight = new DropShadow(20, Color.YELLOW);
    private double sceneX;
    private double sceneY;
    private double translateX;
    private double translateY;
    
    
    public JClass(double x, double y){
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setMinWidth(100);
        this.getStyleClass().add("classWindow_style");
        name.getStyleClass().add("classWindow_style");
        name.getChildren().addAll(className);
        initVariableTableView();
        initMethodTableView();
        this.getChildren().addAll(name, variables, methods);
        this.setOnMousePressed(pressed);
        this.setOnMouseDragged(dragged);
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
    
    
    public void initVariableTableView(){
        variableTableView = new TableView();
        TableColumn variableName = new TableColumn("Name");
        TableColumn variableType = new TableColumn("Type");
        TableColumn isStatic = new TableColumn("Static");
        TableColumn access = new TableColumn("Access");
        variableTableView.getColumns().addAll(variableName, variableType, isStatic, access);
    }
    
    public void initMethodTableView(){
        methodTableView = new TableView();
        TableColumn methodName = new TableColumn("Name");
        TableColumn returnType = new TableColumn("Type");
        TableColumn isStatic = new TableColumn("Static");
        TableColumn access = new TableColumn("Access");
        TableColumn arg1 = new TableColumn("arg1");
        TableColumn arg2 = new TableColumn("arg2");
        TableColumn arg3 = new TableColumn("arg3");
        methodTableView.getColumns().addAll(methodName, returnType, isStatic, access, arg1, arg2, arg3);
    }
    
    public void addVariable(){
        JVariable jv = new JVariable();
        
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
    
    public class JVariable extends VBox {
        private String variableName;
        private String variableType;
        private Boolean isStatic;
        
        public JVariable (){
            
        }
        
        public void setVariableName(String name){
            this.variableName = name;
        }
        
        public void setVariableType(String type){
            this.variableName = type;
        }
        
        public void setStatic(){
            isStatic = true;
        }
        
        public void setNotStatic(){
            isStatic = false;
        }  
    }
    
}
