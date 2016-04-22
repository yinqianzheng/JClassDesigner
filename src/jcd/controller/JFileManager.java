/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jcd.components.JClass;
import jcd.data.DataManager;
import jcd.gui.HandleEvent;

/**
 *
 * @author YinqianZheng
 */
public class JFileManager {
    
    public static boolean saveAs(Object ob, Window window) {
        
        FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("TEXT files (*.json)", "*.json");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Save Work");
        File file = fileChooser.showSaveDialog(window);
        if (saveData(ob.toString(), file.getPath()))
            return true;
        else 
            return false;
    }
    
    // save file to the target location
    public static boolean saveData(Object ob, String path){
        PrintWriter pw;
        File jfile = new File(path); 
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
    
    
    public static JsonObject loadFile(Window window) throws IOException{
        FileChooser.ExtensionFilter extFilter = 
                        new FileChooser.ExtensionFilter("TEXT files (*.json)", "*.json");
               
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Load Work");
        File file = fileChooser.showOpenDialog(window);
        JsonObject jsonList = loadJSONFile(file.getPath());

        return  jsonList;
    }
    
    private static JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
        }
    
    public static void createJObject(JsonObject jObj){
        JClass j;
        JsonObject temp = ((JsonObject)((JsonObject)((JsonArray)jObj.get("class")).get(0)).get("0"));
        JsonObject variableList = ((JsonObject)((JsonObject)((JsonArray)jObj.get("class")).get(1)).get("1"));
        JsonObject tempvList;

        JsonObject methodList = ((JsonObject)((JsonObject)((JsonArray)jObj.get("class")).get(2)).get("2"));
        JsonObject tempmList;

        double x = Double.parseDouble(temp.get("x").toString());
        double y = Double.parseDouble(temp.get("y").toString());
        String name = temp.get("name").toString();
        name = name.substring(1, name.length()-1);
        String packageName = temp.get("package").toString();
        packageName = packageName.substring(1, packageName.length()-1);
        boolean isAbstract = Boolean.parseBoolean(temp.get("abstract").toString());
        j = new JClass(x, y);
        j.setClassName(name);
        j.setPackageName(packageName);
        j.setAbstract(isAbstract);
        // create variables
        for (int i = 0; i<100; i++){   
            tempvList = ((JsonObject)((JsonObject)((JsonArray)variableList.get("variables")).get(i)).get(String.valueOf(i)));
            if (tempvList.get("name")==null)
                break;
            else
                createVariable(tempvList, j);
        }  
        
        // create methods
        for (int i = 0; i<100; i++){   
        tempmList = ((JsonObject)((JsonObject)((JsonArray)methodList.get("methods")).get(i)).get(String.valueOf(i)));

        if (tempmList.get("name")==null)
                break;
            else
                createMethod(tempmList, j);
        }  

        HandleEvent.addToScreen(j);
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
        System.out.println(methodName);
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
}
