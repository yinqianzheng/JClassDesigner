/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author YinqianZheng
 */
public class JLineGroupFactory {
    private static double sceneX, sceneY, translateX, translateY;
    private static JClass child, parent;
    
    public static JLineGroup createJLineGroupforAggregation(JClass a, JClass b, double sx, double sy, double ex, double ey){
        JLineGroup jlg = new JLineGroup(sx, sy, ex, ey);
        jlg.setChildClass(a);
        jlg.setParentClass(b);
        jlg.createConnectorForAggregation();
        return jlg;
    }
    
    public static JLineGroup createJLineGroupforInheritance(JClass a, JClass b, double sx, double sy, double ex, double ey){
        JLineGroup jlg = new JLineGroup(sx, sy, ex, ey);
        jlg.setChildClass(a);
        jlg.setParentClass(b);
        jlg.createConnectorForInheritance();
        jlg.getStartPoint().setOnMousePressed(press);
        jlg.getStartPoint().setOnMouseDragged(drag);
        jlg.getStartPoint().setOnMouseReleased(endPointRelease);
        jlg.getEndPoint().setOnMousePressed(press);
        jlg.getEndPoint().setOnMouseDragged(drag);
        jlg.getEndPoint().setOnMouseReleased(endPointRelease);
        return jlg;
    }
    
    public static JLineGroup createJLineGroupforAggregationAndUses(double sx, double sy, double ex, double ey){
        JLineGroup jlg = new JLineGroup(sx, sy, ex, ey);
        jlg.createConnectorForInheritance();
        jlg.createConnectorForUses();
        return jlg;
    }
    
    
    private static EventHandler<MouseEvent> press = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            sceneX = event.getSceneX();
            sceneY = event.getSceneY();
            translateX = ((JLinePoint)(event.getSource())).getTranslateX();
            translateY = ((JLinePoint)(event.getSource())).getTranslateY();}
    };
    
    private static EventHandler<MouseEvent> drag = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent drag) {
            double offsetX = drag.getSceneX() - sceneX;
            double offsetY = drag.getSceneY() - sceneY;
            ((JLinePoint)(drag.getSource())).setTranslateX(translateX + offsetX);
            ((JLinePoint)(drag.getSource())).setTranslateY(translateY + offsetY);
            ((JLinePoint)(drag.getSource())).setX(((JLinePoint)(drag.getSource())).getCenterX()+((JLinePoint)(drag.getSource())).getTranslateX());
            ((JLinePoint)(drag.getSource())).setY(((JLinePoint)(drag.getSource())).getCenterY()+((JLinePoint)(drag.getSource())).getTranslateY());}
    };
    
    private static EventHandler<MouseEvent> endPointRelease = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent release) {
            double x = ((JLinePoint)(release.getSource())).getJLineGroup().getParentClass().getLayoutX()
                            +((JLinePoint)(release.getSource())).getJLineGroup().getParentClass().getTranslateX();
            double y = ((JLinePoint)(release.getSource())).getJLineGroup().getParentClass().getLayoutY()
                            +((JLinePoint)(release.getSource())).getJLineGroup().getParentClass().getTranslateY();
            double width = ((JLinePoint)(release.getSource())).getJLineGroup().getParentClass().getWidth();
            double hight = ((JLinePoint)(release.getSource())).getJLineGroup().getParentClass().getHeight();
            
            
            
            
            
            if ((((JLinePoint)(release.getSource())).getCenterX()+((JLinePoint)(release.getSource())).getTranslateX()) < (x+width)/2){
                ((JLinePoint)(release.getSource())).setTranslateX(
                        x - ((JLinePoint)(release.getSource())).getCenterX()
                            );
                
                //if (release.getY() < y)
                   // ((JLinePoint)(release.getSource()))
            }else{
                ((JLinePoint)(release.getSource())).setTranslateX(
                        x + width - ((JLinePoint)(release.getSource())).getCenterX()
                            );
            }
            
        }
    };
    
}
