/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.LinkedList;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import jcd.components.JClass;
import jcd.gui.HandleEvent;
import jcd.gui.WorkSpace;

/**
 *
 * @author YinqianZheng
 */
public class DataManager {
    private static DataManager dm;
    private static HandleEvent handler;
    private static WorkSpace workPane;
    private static LinkedList<JClass> jList = new LinkedList<JClass>();
    private static JClass preSelectedJC;
    private static JClass selectedJC;
    final private static DropShadow highlight = new DropShadow(20, Color.YELLOW);

    private DataManager() {}
    
    public static DataManager getInstance(HandleEvent e){
        if(handler==null){
            dm = new DataManager();
            handler = e;
        }
        return dm;
    }

    public static void removeClass(JClass jc){
        jList.remove(jc);
    }
    
    public static JClass getSelectedJC(){
        return selectedJC;
    }
    
    public static void setSelectedJC(JClass jc){
        selectedJC = jc;
    }
        
    public static void addClassToList(JClass j){
        if (selectedJC!=null)
            preSelectedJC = selectedJC;
        jList.add(j);
        selectedJC = j;
        j.setEffect(highlight);
        j.setOnMouseClicked(select);
        if (preSelectedJC!=null)
            preSelectedJC.setEffect(null);
    }
    
    public static String getSelectedJCName(){
        return selectedJC.getClassName();
    }
    
    
    private static HandleEvent getHandler(){
        return handler;
    }
    private static EventHandler select = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent click) { 
            if (WorkSpace.isSelectMode==true){
                if (selectedJC!=null)
                    preSelectedJC = selectedJC;
                selectedJC = ((JClass)click.getSource());
            
                selectedJC.setEffect(highlight);   
                if (preSelectedJC!=null&&preSelectedJC!=selectedJC)
                    preSelectedJC.setEffect(null);
                HandleEvent.getWorkPane().setClassNameInput(selectedJC.getClassName());   
                HandleEvent.getWorkPane().setPackageNameInput(selectedJC.getPackageName());   
            }
        }
    };

    
    
}
