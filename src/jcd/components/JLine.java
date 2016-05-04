/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author YinqianZheng
 */
public class JLine extends Line{
    private JLine line;
    private JLinePoint startPoint, endPoint;
    private JLineGroup jLineGroup;
    private double sceneX, sceneY, startX, startY, endX, endY;
    private JLine a, b;
    final private  DropShadow highlight = new DropShadow(20, Color.YELLOW);

    
    public JLine(JLineGroup jlg){
        this.line = this;
        jLineGroup = jlg;
        this.setStrokeWidth(2);
        this.setStroke(Color.GRAY);
        jLineGroup.getChildren().add(this);
        this.setOnMouseEntered(e->this.setEffect(highlight));
        this.setOnMouseExited(e->this.setEffect(null));
        this.setOnMousePressed(lineBePressed);
        this.setOnMouseDragged(lineBeDragged);
        this.setOnMouseReleased(lineBeReleased);
    }
    
    public JLine(JLineGroup jlg, double startX, double startY, double endX, double endY){
        super(startX, startY, endX, endY);
        this.line = this;
        jLineGroup = jlg;
        this.setStrokeWidth(2);
        this.setStroke(Color.GRAY);
        jLineGroup.getChildren().add(this);
        this.setOnMouseEntered(e->this.setEffect(highlight));
        this.setOnMouseExited(e->this.setEffect(null));
        this.setOnMousePressed(lineBePressed);
        this.setOnMouseDragged(lineBeDragged);
        this.setOnMouseReleased(lineBeReleased);
    }
    
    public JLinePoint getStartPoint(){
        return startPoint;
    }
    
    public JLinePoint getEndPoint(){
        return endPoint;
    }
    
    public void SetStartPoint(JLinePoint sp){
        startPoint = sp;
    }
    
    public void setEndPoint(JLinePoint ep){
        endPoint = ep;
    }
    
    
    private EventHandler lineBePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent pressed) {
            System.out.println(pressed.getX()+ "  "+ pressed.getY());
            a = new JLine(jLineGroup);
            b = new JLine(jLineGroup);
            sceneX = pressed.getSceneX();
            sceneY = pressed.getSceneY();
            
            a.setStartX(line.getStartX());
            a.setStartY(line.getStartY());
            a.setEndY(pressed.getY());
            a.setEndX(pressed.getX());

            b.setStartY(pressed.getY());
            b.setStartX(pressed.getX());
            b.setEndX(line.getEndX());
            b.setEndY(line.getEndY());
            startX = b.getStartX();
            startY = b.getStartY();
            endX = a.getEndX();
            endY = a.getEndY();
            jLineGroup.getChildren().remove(line);

        }
    };
    
    private EventHandler lineBeDragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent dragged) {
            a.setEndX(endX + dragged.getSceneX() - sceneX);
            a.setEndY(endY + dragged.getSceneY() - sceneY);
            b.setStartX(startX + dragged.getSceneX() - sceneX);
            b.setStartY(startY + dragged.getSceneY() - sceneY);
        }
    };
    
    private EventHandler lineBeReleased = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent released) {
            JLinePoint lp = new JLinePoint(jLineGroup, a.getEndX(), a.getEndY());
            lp.setParentLine(line);
            line.getStartPoint().setSubLine2(a);
            line.getEndPoint().setSubLine1(b);
            lp.setSubLine1(a);
            lp.setSubLine2(b);
            a.SetStartPoint(line.getStartPoint());
            a.setEndPoint(lp);
            b.SetStartPoint(lp);
            b.setEndPoint(line.getEndPoint());
            jLineGroup.getChildren().remove(b);
            jLineGroup.getChildren().addAll(b, lp);
            a.toBack();
            b.toBack();
            System.out.println(jLineGroup.getLayoutX());
            System.out.println(released);
            
            System.out.println(released.getX()+" "+released.getSceneX()+" "+released.getScreenX());
        }
    };
            
    
    
  

}