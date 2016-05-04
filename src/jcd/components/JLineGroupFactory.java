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

/**
 *
 * @author YinqianZheng
 */
public class JLineGroupFactory {
   
    private static JClass child, parent;
    final private static DropShadow highlight = new DropShadow(20, Color.YELLOW);

    
    public static JLineGroup createJLineGroupforAggregation(JClass a, JClass b, double sx, double sy, double ex, double ey){
        JLineGroup jlg = new JLineGroup(sx, sy, ex, ey);
        jlg.setChildClass(a);
        jlg.setParentClass(b);
        jlg.createConnectorForAggregation();
//        jlg.getStartPoint().setOnMousePressed(press);
//        jlg.getStartPoint().setOnMouseDragged(drag);
        jlg.getStartPoint().setOnMouseReleased(startPointRelease);
//        jlg.getEndPoint().setOnMousePressed(press);
//        jlg.getEndPoint().setOnMouseDragged(drag);
        jlg.getEndPoint().setOnMouseReleased(endPointRelease);
        return jlg;
    }
    
    public static JLineGroup createJLineGroupforInheritance(JClass a, JClass b, double sx, double sy, double ex, double ey){
        JLineGroup jlg = new JLineGroup(sx, sy, ex, ey);
        jlg.setChildClass(a);
        jlg.setParentClass(b);
        jlg.createConnectorForInheritance();
//        jlg.getStartPoint().setOnMousePressed(press);
//        jlg.getStartPoint().setOnMouseDragged(drag);
        jlg.getStartPoint().setOnMouseReleased(startPointRelease);
//        jlg.getEndPoint().setOnMousePressed(press);
//        jlg.getEndPoint().setOnMouseDragged(drag);
        jlg.getEndPoint().setOnMouseReleased(endPointRelease);
        return jlg;
    }
    
    public static JLineGroup createJLineGroupforAggregationAndUses(double sx, double sy, double ex, double ey){
        JLineGroup jlg = new JLineGroup(sx, sy, ex, ey);
        jlg.createConnectorForInheritance();
        jlg.createConnectorForUses();
//        jlg.getStartPoint().setOnMousePressed(press);
//        jlg.getStartPoint().setOnMouseDragged(drag);
        jlg.getStartPoint().setOnMouseReleased(startPointRelease);
//        jlg.getEndPoint().setOnMousePressed(press);
//        jlg.getEndPoint().setOnMouseDragged(drag);
        jlg.getEndPoint().setOnMouseReleased(endPointRelease);
        return jlg;
    }
    
    
    private static EventHandler<MouseEvent> endPointRelease = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent release) {
            double x = ((JLinePoint)(release.getSource())).getJLineGroup().getParentClass().getLayoutX()
                            +((JLinePoint)(release.getSource())).getJLineGroup().getParentClass().getTranslateX();
            double y = ((JLinePoint)(release.getSource())).getJLineGroup().getParentClass().getLayoutY()
                            +((JLinePoint)(release.getSource())).getJLineGroup().getParentClass().getTranslateY();
            double width = ((JLinePoint)(release.getSource())).getJLineGroup().getParentClass().getWidth();
            double hight = ((JLinePoint)(release.getSource())).getJLineGroup().getParentClass().getHeight();
            
            double pointX =(((JLinePoint)(release.getSource())).getCenterX()+((JLinePoint)(release.getSource())).getTranslateX());
            double pointY =(((JLinePoint)(release.getSource())).getCenterY()+((JLinePoint)(release.getSource())).getTranslateY());
            
            if (pointX < x)
                ((JLinePoint)(release.getSource())).setTranslateX(
                        x - ((JLinePoint)(release.getSource())).getCenterX()
                            );
            else if (pointX > x+width)
                ((JLinePoint)(release.getSource())).setTranslateX(
                        x + width - ((JLinePoint)(release.getSource())).getCenterX()
                            );               
            
            
            if (pointY < y)
                ((JLinePoint)(release.getSource())).setTranslateY(
                    y - ((JLinePoint)(release.getSource())).getCenterY()
                        );
            else if (pointY > y+hight)
                ((JLinePoint)(release.getSource())).setTranslateY(
                    y + hight - ((JLinePoint)(release.getSource())).getCenterY()
                        );
            
            
            ((JLinePoint)(release.getSource())).setX(((JLinePoint)(release.getSource())).getCenterX()
                            +((JLinePoint)(release.getSource())).getTranslateX());
                
            ((JLinePoint)(release.getSource())).setY(((JLinePoint)(release.getSource())).getCenterY()
                            +((JLinePoint)(release.getSource())).getTranslateY());
                
            System.out.println(((JLinePoint)(release.getSource())).getX()+" "+((JLinePoint)(release.getSource())).getY());

        }
    };
    
    
    private static EventHandler<MouseEvent> startPointRelease = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent release) {
            double x = ((JLinePoint)(release.getSource())).getJLineGroup().getChildClass().getLayoutX()
                            +((JLinePoint)(release.getSource())).getJLineGroup().getChildClass().getTranslateX();
            double y = ((JLinePoint)(release.getSource())).getJLineGroup().getChildClass().getLayoutY()
                            +((JLinePoint)(release.getSource())).getJLineGroup().getChildClass().getTranslateY();
            double width = ((JLinePoint)(release.getSource())).getJLineGroup().getChildClass().getWidth();
            double hight = ((JLinePoint)(release.getSource())).getJLineGroup().getChildClass().getHeight();
            
            double pointX =(((JLinePoint)(release.getSource())).getCenterX()+((JLinePoint)(release.getSource())).getTranslateX());
            double pointY =(((JLinePoint)(release.getSource())).getCenterY()+((JLinePoint)(release.getSource())).getTranslateY());
            
            if (pointX < x)
                ((JLinePoint)(release.getSource())).setTranslateX(
                        x - ((JLinePoint)(release.getSource())).getCenterX()
                            );
            else if (pointX > x+width)
                ((JLinePoint)(release.getSource())).setTranslateX(
                        x + width - ((JLinePoint)(release.getSource())).getCenterX()
                            );               
            
            
            if (pointY < y)
                ((JLinePoint)(release.getSource())).setTranslateY(
                    y - ((JLinePoint)(release.getSource())).getCenterY()
                        );
            else if (pointY > y+hight)
                ((JLinePoint)(release.getSource())).setTranslateY(
                    y + hight - ((JLinePoint)(release.getSource())).getCenterY()
                        );
            
            
            ((JLinePoint)(release.getSource())).setX(((JLinePoint)(release.getSource())).getCenterX()
                            +((JLinePoint)(release.getSource())).getTranslateX());
                
            ((JLinePoint)(release.getSource())).setY(((JLinePoint)(release.getSource())).getCenterY()
                            +((JLinePoint)(release.getSource())).getTranslateY());
//                
            
        }
    };
    
}
