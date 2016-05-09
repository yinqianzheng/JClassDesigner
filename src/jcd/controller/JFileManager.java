/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Cursor;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import jcd.components.JClass;
import jcd.components.JLine;
import jcd.components.JLineGroup;
import jcd.components.JLineGroupFactory;
import jcd.components.JLinePoint;
import jcd.data.DataManager;
import static jcd.data.DataManager.isResizeMode;
import jcd.gui.HandleEvent;

/**
 *
 * @author YinqianZheng
 */
public class JFileManager {
    private static LinkedList<JsonObject> linesList = new LinkedList<JsonObject>();
    
    
    public static boolean saveAs(Object ob, Window window) throws IOException, NullPointerException {
        FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("TEXT files (*.json)", "*.json");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Save Work");
        File file = fileChooser.showSaveDialog(window);
        
        if (saveData(ob.toString(), file.getPath())){
            DataManager.hasDirectory().set(true);
            DataManager.setDirectory(file.getAbsolutePath());
            return true;
        }else{ 
            return false;
        }
    }
    
    // save file to the target location
    public static boolean saveData(Object ob, String path) throws IOException{
        PrintWriter pw;
        File jfile = new File(path); 
        if (!jfile.getParentFile().exists())
            jfile.getParentFile().mkdirs();
        if (!jfile.exists())
            jfile.createNewFile();
        try {
            pw = new PrintWriter(jfile);
            pw.write(ob.toString());  
            pw.close();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HandleEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    
    public static String getDirectory(Window window){
        FileChooser.ExtensionFilter extFilter = 
                        new FileChooser.ExtensionFilter("TEXT files (*.json)", "*.json");
               
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Load Work");
        File file = fileChooser.showOpenDialog(window);
        return file.getPath();
    }
    
    public static JsonObject loadFile(String path) throws IOException{
        JsonObject jsonList = createJsonObject(path);
        DataManager.hasDirectory().set(true);
        DataManager.setDirectory(path);
        return  jsonList;    
    }
    
    public static void createHistoryList(JsonObject source){
        DataManager.getHistoryList().clear();
//        createClasses(((JsonObject)((JsonObject)((JsonArray)source.get("history")).get(1)).get("1")));
        int size = ((JsonArray)source.get("history")).size();
        for (int i=0; i<size; i++){
                DataManager.getHistoryList().add(((JsonObject)((JsonObject)((JsonArray)source.get("history")).get(i)).get(String.valueOf(i))).toString());
        }
        
        DataManager.currentCursor.set(Integer.parseInt(source.get("cursor").toString()));
    }
    
    private static JsonObject createJsonObject(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    public static void createClasses(JsonObject jObj) {
        linesList.clear();
        JsonObject classes;
        for (int i = 0; i<100; i++){ 
            try {
                classes = ((JsonObject)((JsonObject)((JsonArray)jObj.get("classes")).get(i)).get(String.valueOf(i)));
                if (classes==null)
                    break;
                if (classes.get("class")==null)
                    break;
                else
                    createClass(classes);
            } catch (Exception e) {
            }     
        }
        addLines();
        if (jObj.get("zoomValue")!=null)
            HandleEvent.getWorkPane().setZoomValue(Double.parseDouble(jObj.get("zoomValue").toString()));
        linesList.clear();
        if (isResizeMode.get())
            for (JClass jc : DataManager.getJClassList().getItems()){
                jc.setOnMouseEntered(e->jc.setCursor(Cursor.SE_RESIZE));
            }
    }
    
    public static void createClass(JsonObject jObj){
        JClass j;
        JsonObject temp = ((JsonObject)((JsonObject)((JsonArray)jObj.get("class")).get(0)).get("0"));
        JsonObject variableList = ((JsonObject)((JsonObject)((JsonArray)jObj.get("class")).get(1)).get("1"));
        JsonObject tempvList;

        JsonObject methodList = ((JsonObject)((JsonObject)((JsonArray)jObj.get("class")).get(2)).get("2"));
        JsonObject tempmList;
        
        JsonObject lineGroups = ((JsonObject)((JsonObject)((JsonArray)jObj.get("class")).get(3)).get("3"));
        linesList.add(lineGroups);

        double x = Double.parseDouble(temp.get("x").toString());
        double y = Double.parseDouble(temp.get("y").toString());
        String name = temp.get("name").toString();
        name = name.substring(1, name.length()-1);
        String packageName = temp.get("package").toString();
        packageName = packageName.substring(1, packageName.length()-1);
        boolean isAbstract = Boolean.parseBoolean(temp.get("abstract").toString());
        boolean isInterface = Boolean.parseBoolean(temp.get("interface").toString());
        j = new JClass(x, y);
        j.setClassName(name);
        j.setPackageName(packageName);
        j.setAbstract(isAbstract);
        j.setPrefSize(Double.parseDouble(temp.get("width").toString()), Double.parseDouble(temp.get("hight").toString()));
        j.getVariableBox().setPrefSize(Double.parseDouble(temp.get("variableBoxWidth").toString()), Double.parseDouble(temp.get("variableBoxHight").toString()));
        j.getMethodBox().setPrefSize(Double.parseDouble(temp.get("methodBoxWidth").toString()), Double.parseDouble(temp.get("methodBoxHight").toString()));
        for (int i = 0; i<100; i++){   
            tempvList = ((JsonObject)((JsonObject)((JsonArray)variableList.get("variables")).get(i)).get(String.valueOf(i)));
            if (tempvList==null)
                break;
            if (tempvList.get("name")==null)
                break;
            else
                createVariable(tempvList, j);
        }  
        
        // create methods
        for (int i = 0; i<100; i++){   
        tempmList = ((JsonObject)((JsonObject)((JsonArray)methodList.get("methods")).get(i)).get(String.valueOf(i)));
        if (tempmList==null)
            break;
        if (tempmList.get("name")==null)
            break;
        else
            createMethod(tempmList, j);
        }  
        if (isInterface)
            j.setInterface(true);
        HandleEvent.addToScreen(j);
        //System.out.println(j.toCode());
    }
    
    private static void addLines(){
        for (JsonObject jso: linesList){
            if (((JsonObject)((JsonObject)((JsonArray)jso.get("lines")).get(0)).get("extends")).containsKey("childClass")){
                HandleEvent.getWorkPane().root.getChildren().add(addLinesHelper(((JsonObject)((JsonObject)((JsonArray)jso.get("lines")).get(0)).get("extends")), 0));
            }   
            
            for (int i = 0; i<100; i++){
                if (((JsonObject)((JsonArray)((JsonObject)((JsonArray)jso.get("lines")).get(1)).get("implements")).get(i)).containsKey("childClass"))
                    HandleEvent.getWorkPane().root.getChildren().add(addLinesHelper(((JsonObject)((JsonArray)((JsonObject)((JsonArray)jso.get("lines")).get(1)).get("implements")).get(i)), 1)); 
                else
                    break;
            }
            
            for (int i = 0; i<100; i++){
                if (((JsonObject)((JsonArray)((JsonObject)((JsonArray)jso.get("lines")).get(2)).get("uses")).get(i)).containsKey("childClass"))
                    HandleEvent.getWorkPane().root.getChildren().add(addLinesHelper(((JsonObject)((JsonArray)((JsonObject)((JsonArray)jso.get("lines")).get(2)).get("uses")).get(i)), 2)); 
                else
                    break;
            }
            
            for (int i = 0; i<100; i++){
                if (((JsonObject)((JsonArray)((JsonObject)((JsonArray)jso.get("lines")).get(3)).get("aggregation")).get(i)).containsKey("childClass"))
                    HandleEvent.getWorkPane().root.getChildren().add(addLinesHelper(((JsonObject)((JsonArray)((JsonObject)((JsonArray)jso.get("lines")).get(3)).get("aggregation")).get(i)), 3)); 
                else
                    break;
            }
        }
    }
    private static JLineGroup addLinesHelper(JsonObject obj, int indexNum){
                String childClass = obj.get("childClass").toString();
                childClass = childClass.substring(1, childClass.length()-1);
                String parentClass = obj.get("parentClass").toString();
                parentClass = parentClass.substring(1, parentClass.length()-1);
                JsonObject startP = (JsonObject) obj.get("startPoint");
                JsonObject endP = ((JsonObject)obj.get("endPoint"));
                JsonArray points = ((JsonArray)obj.get("points"));
                JClass child = null, parent = null;
                for (JClass jc : DataManager.getJClassList().getItems()){
                    if ((jc.getPackageName()+"."+jc.getClassName()).equals(childClass))
                        child = jc;
                    if ((jc.getPackageName()+"."+jc.getClassName()).equals(parentClass))
                        parent = jc;
                }
                double sx = Double.parseDouble(startP.get("x").toString());
                double sy = Double.parseDouble(startP.get("y").toString());
                double sr = Double.parseDouble(startP.get("rotateValue").toString());
                double ex = Double.parseDouble(endP.get("x").toString());
                double ey = Double.parseDouble(endP.get("y").toString());
                double er = Double.parseDouble(endP.get("rotateValue").toString());
                
                JLineGroup jlg;
                if (indexNum==0||indexNum==1)
                    jlg = JLineGroupFactory.createJLineGroupforInheritance(child, parent, sx, sy, ex, ey);
                else if (indexNum==2)
                    jlg = JLineGroupFactory.createJLineGroupforUses(child, parent, sx, sy, ex, ey);
                else 
                    jlg = JLineGroupFactory.createJLineGroupforAggregation(child, parent, sx, sy, ex, ey);
                
                jlg.getStartPoint().setRotateForConnector(sr);
                jlg.getEndPoint().setRotateForConnector(er);
                
                if (indexNum == 0){
                    child.setLine(jlg);
                    child.setJParent(parent);
                }else if (indexNum == 1){
                    try {
                        child.addParent(parent.getClassName());
                        child.getJLineGroupList().put(parentClass, jlg);
                    } catch (Exception e) {
                    }
                }else if (indexNum == 2)
                    child.getUsesJLineGroupsList().put(parent.getClassName(), jlg);
                else
                    child.getAggregationJLineGroupsList().put(parent.getClassName(), jlg);
                addPoints(points, jlg);
                return jlg;
    }
    
    private static void addPoints(JsonArray ja, JLineGroup jlg){
        for (int i=0; i<100; i++){
            if (ja.get(i).toString().equals("{}"))
                break;
            else{
                JLinePoint a = jlg.getEndPoint().getSubLine1().getStartPoint();
                JLinePoint b = jlg.getEndPoint();
                jlg.getChildren().remove(jlg.getEndPoint().getSubLine1());
                JsonObject p = (JsonObject)ja.get(i);
                JLinePoint jlp = new JLinePoint(jlg, Double.parseDouble(p.get("x").toString()), Double.parseDouble(p.get("y").toString()));
                JLine l1 = new JLine(jlg);
                JLine l2 = new JLine(jlg);
                l1.SetStartPoint(a);
                l1.setStartX(a.getX());
                l1.setStartY(a.getY());
                l1.setEndPoint(jlp);
                l1.setEndX(jlp.getX());
                l1.setEndY(jlp.getY());
                l2.SetStartPoint(jlp);
                l2.setEndPoint(b);
                l2.setStartX(jlp.getX());
                l2.setStartY(jlp.getY());
                l2.setEndX(b.getX());
                l2.setEndY(b.getY());
                jlp.setSubLine1(l1);
                jlp.setSubLine2(l2);
                a.setSubLine2(l1);
                b.setSubLine1(l2);
                jlp.toFront();
                jlg.getStartPoint().connectorToFront();
                b.connectorToFront();
                jlg.getChildren().add(jlp);
            }
        }
    }
    
    private static void createVariable(JsonObject variableList, JClass j){
        String varName = variableList.get("name").toString();
        varName = varName.substring(1, varName.length()-1);
        String varType = variableList.get("type").toString();
        varType = varType.substring(1, varType.length()-1);
        String varAccess = variableList.get("access").toString();
        varAccess = varAccess.substring(1, varAccess.length()-1);
        String varStatic = variableList.get("isStatic").toString();
        j.getVariableBox().addVariable(varAccess, varType, varName, Boolean.parseBoolean(varStatic));    
    }
    
    private static void createMethod(JsonObject methodList, JClass j){
        // obtain the name of method
        String methodName = methodList.get("name").toString();
        methodName = methodName.substring(1, methodName.length()-1);
        // obtain the type of method
        String methodType = methodList.get("type").toString();
        methodType = methodType.substring(1, methodType.length()-1);
        // obtain the arg1 of method
        String arg1 = methodList.get("arg1").toString();
        arg1 = arg1.substring(1, arg1.length()-1);
        // obtain the arg2 of method
        String arg2 = methodList.get("arg2").toString();
        arg2 = arg2.substring(1, arg2.length()-1);
        // obtain the arg3 of method
        String arg3 = methodList.get("arg3").toString();
        arg3 = arg3.substring(1, arg3.length()-1);
        // obtain the access of method
        String methodAccess = methodList.get("access").toString();
        methodAccess = methodAccess.substring(1, methodAccess.length()-1);
        // obtain the static of method
        String methodStatic = methodList.get("isStatic").toString();
        // obtain the abstract of method
        String methodAbstract = methodList.get("isAbstract").toString();
        j.getMethodBox().addMethod(methodAccess, methodType, methodName, Boolean.parseBoolean(methodStatic),
                Boolean.parseBoolean(methodAbstract), arg1, arg2, arg3);
        
    }
    
    public static void exportCode(){
            
        DirectoryChooser folderChooser = new DirectoryChooser();
            folderChooser.setTitle("Export Code");
            
        
            File file = folderChooser.showDialog(HandleEvent.getWorkPane().primaryStageWindow);
            
            
            String path = file.getPath();
            int i = path.indexOf("src", 0);
            
            if (i < 0){
                for (JClass jc : DataManager.getCurrentList().getItems()){
                    String newPath = path;
                    if (!jc.getPackageName().equals(""))
                        newPath = newPath + "/" + jc.getPackageName();
                    File jfile = new File(newPath);
                    if (!jfile.exists())
                        jfile.mkdirs();
                    
                        jfile = new File(newPath+"/"+jc.getClassName()+".java");
                        PrintWriter pw;
                    try {
                        pw = new PrintWriter(jfile);
                        pw.write(jc.toCode());
                        pw.close();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(HandleEvent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }else{
                for (JClass jc : DataManager.getCurrentList().getItems()){
                    String newPath = path;
                    String pcgstr = path.substring(i);
                    pcgstr = pcgstr.substring(4);
                    String packageStr = "package ";
                    for (int index = 0; index < pcgstr.length(); index++){
                        if (pcgstr.charAt(index)=='/')
                            packageStr = packageStr + ".";
                        else
                            packageStr = packageStr + pcgstr.charAt(index);
                    }
                    
                    if (!jc.getPackageName().equals("")){
                        newPath = newPath + "/";
                        for (int index = 0; index < jc.getPackageName().length(); index++){
                            if (jc.getPackageName().charAt(index)=='.')
                                newPath = newPath + "/";
                            else
                                newPath = newPath + jc.getPackageName().charAt(index);
                        }
                        packageStr = packageStr + "." + jc.getPackageName();
                    }
                    packageStr = packageStr + ";";
                    
                    File jfile = new File(newPath);
                    if (!jfile.exists())
                        jfile.mkdirs();
                    
                    jfile = new File(newPath+"/"+jc.getClassName()+".java");
                    PrintWriter pw;
                    
                    try {
                        pw = new PrintWriter(jfile);
                        pw.write(packageStr+"\n\n"+jc.toCode());
                        pw.close();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(HandleEvent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            
                
            } 
    }
}
