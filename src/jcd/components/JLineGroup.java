/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 *
 * @author YinqianZheng
 */
public class JLineGroup extends Group{
    private JLine jLine;
    private JLinePoint startPoint, endPoint;
    private Polygon triangle, diamond;
    
    public JLineGroup(double sx, double sy, double ex, double ey){
        jLine = new JLine(this, sx, sy, ex, ey);
        startPoint = new JLinePoint(this, sx, sy);
        endPoint = new JLinePoint(this, ex, ey);
        this.getChildren().addAll(startPoint, endPoint);
        initRelations(startPoint, jLine, endPoint);
    }
    
    
    public JLinePoint getStartPoint(){
        return startPoint;
    }
    
    public JLinePoint getEndPoint(){
        return endPoint;
    }
    
    public void setEndPoint(double x, double y){
        endPoint.setX(x);
        endPoint.setY(y);

    }
    
    
    public void setStartX(double x){
        startPoint.setX(x);
    }
    
    public void setStartY(double y){
        startPoint.setY(y);
    }
    
    public void setEndX(double x){
        endPoint.setX(x);
    }
    
    public void setEndY(double y){
        endPoint.setY(y);
    }
    
    public void createConnectorForInheritance(){
        triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
        0.0, 0.0,
        0.0, 12.0,
        9.0, 6.0});
        triangle.setFill(Color.WHITE);
        triangle.setStroke(Color.BLACK);
        endPoint.addInheritanceConnector(triangle);
    }
    
    public void createConnectorForAggregation(){
        diamond = new Polygon();
        diamond.getPoints().addAll(new Double[]{
        0.0, 7.0,
        7.0, 0.0,
        14.0, 7.0,
        7.0, 14.0});
        diamond.setFill(Color.WHITE);
        diamond.setStroke(Color.BLACK);
        startPoint.addAggregationConnector(diamond);
    }
    
    public void createConnectorForUses(){
        
    }
    
    private void initRelations(JLinePoint sp, JLine jl, JLinePoint ep){
        sp.setSubLine2(jl);
        jl.SetStartPoint(sp);
        jl.setEndPoint(ep);
        ep.setSubLine1(jl);
        sp.setOnMousePressed(null);
        sp.setOnMouseDragged(null);
        sp.setOnMouseReleased(null);
        sp.setOnMouseClicked(null);
        ep.setOnMousePressed(null);
        ep.setOnMouseDragged(null);
        ep.setOnMouseReleased(null);
        ep.setOnMouseClicked(null);
    }
}
