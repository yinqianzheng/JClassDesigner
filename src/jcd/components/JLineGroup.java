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
    private JClass child, parent;
    
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
    
    public void setChildClass(JClass ch){
        child = ch;
    }
    
    public void setParentClass(JClass parent){
        this.parent = parent;
    }
    
    public JClass getChildClass(){
        return child;
    }
    
    public JClass getParentClass(){
        return this.parent;
    }
    
    public double getStartX(){
        return startPoint.getX();
    }
    
    public double getStartY(){
        return startPoint.getY();
    }
    
    public double getEndX(){
        return endPoint.getX();
    }
    
    public double getEndY(){
        return endPoint.getY();
    }
    
    public void createConnectorForInheritance(){
        triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
        0.0, 0.0,
        0.0, 16.0,
        12.0, 8.0});
        triangle.setFill(Color.WHITE);
        triangle.setStroke(Color.BLACK);
        endPoint.addInheritanceConnector(triangle);
    }
    
    public Polygon getTriangle(){
        return triangle;
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
    
    public Polygon getDiamond(){
        return diamond;
    }
    
    public void createConnectorForUses(){
        
    }
    
    private void initRelations(JLinePoint sp, JLine jl, JLinePoint ep){
        sp.setSubLine2(jl);
        jl.SetStartPoint(sp);
        jl.setEndPoint(ep);
        ep.setSubLine1(jl);
        sp.setOnMouseReleased(null);
        sp.setOnMouseClicked(null);
        ep.setOnMouseReleased(null);
        ep.setOnMouseClicked(null);
    }
}
