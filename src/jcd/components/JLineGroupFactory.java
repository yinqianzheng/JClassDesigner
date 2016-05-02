/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

/**
 *
 * @author YinqianZheng
 */
public class JLineGroupFactory {
    public static JLineGroup createJLineGroupforAggregation(double sx, double sy, double ex, double ey){
        JLineGroup jlg = new JLineGroup(sx, sy, ex, ey);
        jlg.createConnectorForAggregation();
        return jlg;
    }
    
    public static JLineGroup createJLineGroupforInheritance(double sx, double sy, double ex, double ey){
        JLineGroup jlg = new JLineGroup(sx, sy, ex, ey);
        jlg.createConnectorForInheritance();
        return jlg;
    }
    
    public static JLineGroup createJLineGroupforAggregationAndUses(double sx, double sy, double ex, double ey){
        JLineGroup jlg = new JLineGroup(sx, sy, ex, ey);
        jlg.createConnectorForInheritance();
        jlg.createConnectorForUses();
        return jlg;
    }
}
