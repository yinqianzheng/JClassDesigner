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
import javafx.scene.shape.Polygon;

/**
 *
 * @author YinqianZheng
 */
public class JLinePoint extends Circle{
    private JLinePoint jLinePoint;
    private JLineGroup jLineGroup;
    private SimpleDoubleProperty x, y;
    private JLine parentLine, subLine1, subLine2;
    private double sceneX, sceneY, translateX, translateY;
    private Polygon diamond, triangle;
    final private  DropShadow highlight = new DropShadow(20, Color.YELLOW);
    
    public JLinePoint(JLineGroup jlg, double x, double y){
        jLineGroup = jlg;
        this.jLinePoint = this;
        this.setRadius(7);
        this.setStroke(Color.TRANSPARENT);
        this.setFill(Color.TRANSPARENT);
        this.setCenterX(x);
        this.setCenterY(y);
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.setOnMousePressed(press);
        this.setOnMouseDragged(drag);
        this.setOnMouseReleased(release);
        this.setOnMouseClicked(click);
        setActions();
    }
    
    public JLine getParentLine(){
        return parentLine;
    }
    
    public JLine getSubLine1(){
        return subLine1;
    }
    
    public JLine getSubLine2(){
        return subLine2;
    }
    
    public void setParentLine(JLine jl){
        parentLine = jl;
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
                    diamond.setLayoutX(x.get()-7);
                if (triangle!=null)
                    triangle.setLayoutX(x.get()-4.5);
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
                    diamond.setLayoutY(y.get()-7);
                if (triangle!=null)
                    triangle.setLayoutY(y.get()-6);
            }
        });
        jLinePoint.setOnMouseEntered(e->{
            jLinePoint.setEffect(highlight);
            jLinePoint.setFill(Color.WHITE);
            jLinePoint.setStroke(Color.BLACK);
            });
        jLinePoint.setOnMouseExited(e->{
            jLinePoint.setEffect(null);
            jLinePoint.setFill(Color.BLACK);
            jLinePoint.setStroke(Color.TRANSPARENT);
                });
    }
    
    public void addAggregationConnector(Polygon plg){
        diamond = plg;
        plg.setLayoutX(x.get()-7);
        plg.setLayoutY(y.get()-7);
        jLineGroup.getChildren().add(plg);
        plg.toFront();
        jLinePoint.toFront();
    }
    
    public void addInheritanceConnector(Polygon tri){
        triangle = tri;
        triangle.setLayoutX(x.get()-4.5);
        triangle.setLayoutY(y.get()-6);
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
            jLinePoint.setTranslateX(translateX + offsetX);
            jLinePoint.setTranslateY(translateY + offsetY);
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
                parentLine.SetStartPoint(subLine1.getStartPoint());
                parentLine.setEndPoint(subLine2.getEndPoint());
                parentLine.setStartX(subLine1.getStartX());
                parentLine.setStartY(subLine1.getStartY());
                parentLine.setEndX(subLine2.getEndX());
                parentLine.setEndY(subLine2.getEndY());
                subLine1.getStartPoint().setSubLine2(parentLine);
                subLine2.getEndPoint().setSubLine1(parentLine);
                jLineGroup.getChildren().removeAll(subLine1, subLine2, jLinePoint);
                jLineGroup.getChildren().add(parentLine);
                subLine1.getStartPoint().toFront();
                subLine2.getEndPoint().toFront();
            }
        }
    };
    
}
