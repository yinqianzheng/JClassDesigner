/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;

/**
 *
 * @author YinqianZheng
 */
public class JLineGroup extends Group{
   // private String childClass, parentClass;
    private JLine jLine;
    private JLinePoint startPoint, endPoint;
    private Polygon triangle, diamond, arrow;
    private JClass child, parent;
    
    public JLineGroup(double sx, double sy, double ex, double ey){
        jLine = new JLine(this, sx, sy, ex, ey);
        startPoint = new JLinePoint(this, sx, sy);
        endPoint = new JLinePoint(this, ex, ey);
        this.getChildren().addAll(startPoint, endPoint);
        initRelations(startPoint, jLine, endPoint);
    }
    
//    public String getChildClass(){
//        return childClass;
//    }
//    
//    public String getParentClass(){
//        return parentClass;
//    }
    
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
        0.0, 20.0,
        14.0, 10.0});
        triangle.setStrokeWidth(1.5);
        triangle.setFill(Color.WHITE);
        triangle.setStroke(Color.BLACK);
        endPoint.addInheritanceConnector(triangle);
    }
    
//    public Polygon getTriangle(){
//        return triangle;
//    }
    
    public void createConnectorForAggregation(){
        diamond = new Polygon();
        diamond.getPoints().addAll(new Double[]{
        0.0, 10.0,
        10.0, 0.0,
        20.0, 10.0,
        10.0, 20.0});
        diamond.setStrokeWidth(1.5);
        diamond.setFill(Color.WHITE);
        diamond.setStroke(Color.BLACK);
        startPoint.addAggregationConnector(diamond);
    }
    
    public Polygon getDiamond(){
        return diamond;
    }
    
    public void createConnectorForUses(){
        arrow = new Polygon();
        arrow.getPoints().addAll(new Double[]{
        -10.0, -10.0,
        0.0, 0.0,
        -10.0, 10.0,
        0.0, 0.0});
        arrow.setStroke(Color.BLACK);
        arrow.setStrokeWidth(2);
        arrow.setFill( null);
        endPoint.addUsesConnector(arrow);
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
    
    // generate json-format string
    @Override
    public String toString(){
        String str =  "{\n\"childClass\":\""+child.getPackageName()+"."+child.getClassName()+"\",\n"
                + "\"parentClass\":\""+parent.getPackageName()+"."+parent.getClassName()+"\",\n"
                + "\"startPoit\":"+startPoint.toString()+",\n"
                + "\"endPoint\":"+endPoint.toString()+",\n"
                + "\"points\":[{}]\n"
                + "}";
        return str;
    }
}
