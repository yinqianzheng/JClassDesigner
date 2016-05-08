/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;
import jcd.gui.HandleEvent;

/**
 *
 * @author YinqianZheng
 */
public class JLinePoint extends Circle{
    private JLinePoint jLinePoint;
    private JLineGroup jLineGroup;
    private SimpleDoubleProperty x, y;
    private JLine subLine1, subLine2;
    private double sceneX, sceneY, translateX, translateY;
    private Polygon diamond, triangle, arrow;
    private SimpleDoubleProperty rotateValue;
    final private  DropShadow highlight = new DropShadow(20, Color.YELLOW);
    
    public JLinePoint(JLineGroup jlg, double x, double y){
        jLineGroup = jlg;
        this.jLinePoint = this;
        this.setRadius(8);
        this.setStroke(Color.TRANSPARENT);
        this.setFill(Color.TRANSPARENT);
        this.setCenterX(x);
        this.setCenterY(y);
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.rotateValue = new SimpleDoubleProperty(0);
        this.setOnMousePressed(press);
        this.setOnMouseDragged(drag);
        this.setOnMouseReleased(release);
        this.setOnMouseClicked(click);
        setActions();
    }
    
    
    public JLine getSubLine1(){
        return subLine1;
    }
    
    public JLine getSubLine2(){
        return subLine2;
    }
    
    
    public void setSubLine1(JLine sub1){
        subLine1 = sub1;
    }
    
    public void setSubLine2(JLine sub2){
        subLine2 = sub2;
    }
    
    public void setX(double x){
        this.x.set(x);
    }
    
    public void setY(double y){
        this.y.set(y);
    }
    
    public double getX(){
        return this.x.get();
    }
    
    public double getY(){
        return this.y.get();
    }
    
    public JLineGroup getJLineGroup(){
        return jLineGroup;
    }
    
    private void setActions(){
        x.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                jLinePoint.setCenterX(x.get());
                if (subLine1!=null)
                    subLine1.setEndX(x.get());
                if (subLine2!=null)
                    subLine2.setStartX(x.get());
                if (diamond!=null)
                    diamond.setLayoutX(x.get()-10);
                if (triangle!=null)
                    triangle.setLayoutX(x.get()-7);
                if (arrow!=null)
                    arrow.setLayoutX(x.get());
            }
        });
        y.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                jLinePoint.setCenterY(y.get());
                if (subLine1!=null)
                    subLine1.setEndY(y.get());
                if (subLine2!=null)
                    subLine2.setStartY(y.get());
                if (diamond!=null)
                    diamond.setLayoutY(y.get()-10);
                if (triangle!=null)
                    triangle.setLayoutY(y.get()-10);
                if (arrow!=null)
                    arrow.setLayoutY(y.get());
            }
        });
        jLinePoint.setOnMouseEntered(e->{
            jLinePoint.setEffect(highlight);
            jLinePoint.setFill(Color.WHITE);
            jLinePoint.setStroke(Color.BLACK);
            });
        jLinePoint.setOnMouseExited(e->{
            jLinePoint.setEffect(null);
            jLinePoint.setFill(Color.TRANSPARENT);
            jLinePoint.setStroke(Color.TRANSPARENT);
                });
    }
    public void connectorToFront(){
        if (diamond!=null)
            diamond.toFront();
        if (triangle!=null)
            triangle.toFront();
        if (arrow!=null)
            arrow.toFront();
        this.toFront();
    }
    public void setRotateForConnector(double value){
        if (rotateValue == null){
            rotateValue.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                    if (diamond!=null)
                        diamond.setRotate(rotateValue.get());
                    if (triangle!=null)
                        triangle.setRotate(rotateValue.get());
                    if (arrow!=null)
                        arrow.setRotate(rotateValue.get()); 
                }
            });
            rotateValue.set(value);
        }else{
            rotateValue.set(value);
        }
        
    }
    
    public void markTranslateValue(){
        translateX = this.getTranslateX();
        translateY = this.getTranslateY();
    }
    
    public void addOffset(double x, double y){
        this.setTranslateX(translateX + x);
        this.setTranslateY(translateY + y);
        this.x.set(this.getCenterX()+this.getTranslateX());
        this.y.set(this.getCenterY()+this.getTranslateY());
    }
    
    public void addOffsetForMouseReleased(double x, double y){
        this.setTranslateX(this.getTranslateX() + x);
        this.setTranslateY(this.getTranslateY() + y);
        this.x.set(this.getCenterX()+this.getTranslateX());
        this.y.set(this.getCenterY()+this.getTranslateY());
    }
    
    public void addAggregationConnector(Polygon plg){
        diamond = plg;
        plg.setLayoutX(x.get()-10);
        plg.setLayoutY(y.get()-10);
        jLineGroup.getChildren().add(plg);
        plg.toFront();
        jLinePoint.toFront();
    }
    
    public void addUsesConnector(Polygon ar){
        arrow = ar;
        arrow.setLayoutX(x.get());
        arrow.setLayoutY(y.get());
        jLineGroup.getChildren().add(arrow);
        arrow.toFront();
        jLinePoint.toFront();
    }
    
    public void addInheritanceConnector(Polygon tri){
        triangle = tri;
        triangle.setLayoutX(x.get()-7);
        triangle.setLayoutY(y.get()-10);
        jLineGroup.getChildren().add(triangle);
        triangle.toFront();
        jLinePoint.toFront();
    }
    
    private EventHandler<MouseEvent> press = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent press) {
            sceneX = press.getSceneX();
            sceneY = press.getSceneY();
            translateX = jLinePoint.getTranslateX();
            translateY = jLinePoint.getTranslateY();
            
        }
    };
    
    private EventHandler<MouseEvent> drag = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent drag) {
            double offsetX = drag.getSceneX() - sceneX;
            double offsetY = drag.getSceneY() - sceneY;
            jLinePoint.setTranslateX(translateX + offsetX/HandleEvent.getWorkPane().getZoomValue());
            jLinePoint.setTranslateY(translateY + offsetY/HandleEvent.getWorkPane().getZoomValue());
            x.set(jLinePoint.getCenterX()+jLinePoint.getTranslateX());
            y.set(jLinePoint.getCenterY()+jLinePoint.getTranslateY());
            
        }
    };
    
    private EventHandler<MouseEvent> release = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent release) {
            
        }
    };
    
    private EventHandler<MouseEvent> click = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent click) {
            if (click.getClickCount()==2){
                JLine newLine = new JLine(jLineGroup);
                newLine.SetStartPoint(subLine1.getStartPoint());
                newLine.setEndPoint(subLine2.getEndPoint());
                newLine.setStartX(subLine1.getStartX());
                newLine.setStartY(subLine1.getStartY());
                newLine.setEndX(subLine2.getEndX());
                newLine.setEndY(subLine2.getEndY());
                subLine1.getStartPoint().setSubLine2(newLine);
                subLine2.getEndPoint().setSubLine1(newLine);
                jLineGroup.getChildren().removeAll(subLine1, subLine2, jLinePoint);
                subLine1.getStartPoint().toFront();
                subLine2.getEndPoint().toFront();
                newLine.toBack();
            }
        }
    };
    
    // generate json-format string
    @Override
    public String toString(){
        String str = "{\n\"x\":"+x.get()+",\n"
                + "\"y\":"+y.get()+",\n"
                + "\"rotateValue\":"+rotateValue.get()+"\n"
                + "}";
        return str;
    }
    
}
