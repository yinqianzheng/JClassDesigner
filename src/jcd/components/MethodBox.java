/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jcd.data.DataManager;

/**
 *
 * @author YinqianZheng
 */
public class MethodBox extends VBox{
    private TableView<JMethod> mTable;
    private MethodBox methodBox;
    private SimpleBooleanProperty isContainAbstract;
    private SimpleBooleanProperty isInterface;
    private JClass jClass;
    private TableColumn<JMethod, String> accessColumn;
    private TableColumn<JMethod, Boolean> staticColumn;
    private TableColumn<JMethod, Boolean> abstractColumn;
    private TableColumn<JMethod, String> arg1Column;
    private TableColumn<JMethod, String> arg2Column;
    private TableColumn<JMethod, String> arg3Column;
    
    
    public MethodBox(JClass jc){
        isInterface = new SimpleBooleanProperty(false);
        jClass = jc;
        isContainAbstract = new SimpleBooleanProperty(false);
        mTable = new TableView<JMethod>();
        initTableView();
        mTable.getItems().addListener(new ListChangeListener(){
            @Override
            public void onChanged(ListChangeListener.Change change) {
                containAbstractMethod();
            }
        } );
        methodBox = this;
    }
    
    public TableView<JMethod> getMethodTable(){
        return mTable;
    }
    
    public void addMethod(){
        JMethod jm = new JMethod(methodBox);
        String methodName;
        SimpleBooleanProperty validName = new SimpleBooleanProperty(true);
        ObservableList<JMethod> methods = mTable.getItems();        
        for (int i = 1; i < 100; i++){
            methodName = "method" + String.valueOf(i);
            validName.set(true);
            for (JMethod j: methods){
                if ((j.getName()).equals(methodName)){
                    validName.set(false);
                }           
            }        
            if (validName.get() == true){
                jm.setName(methodName);
                break;
            }
        }
        jm.setAbstract(isInterface.get());
        mTable.getItems().add(jm);
        jm.getNameProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                // update label (name)
                jm.getLabel().setText(jm.toText());
                DataManager.setSaved(false);
            }
        });;
        this.getChildren().add(jm.getLabel());
    }
    
    public void addMethod(String methodAccess, String methodType, String methodName, boolean s,
            boolean a, String arg1, String arg2, String arg3){
        JMethod jm = new JMethod(methodBox, methodAccess, methodType, methodName, s, a, arg1, arg2, arg3);
        mTable.getItems().add(jm);
        this.getChildren().add(jm.getLabel());
    }     
    
    public void removeMethod(){
        ObservableList<JMethod> methodSelected = mTable.getSelectionModel().getSelectedItems();
        this.getChildren().remove(methodSelected.get(0).getLabel());
        mTable.getItems().removeAll(methodSelected); 
    }
    
    
    public SimpleBooleanProperty getIsContainAbstract(){
        return isContainAbstract;
    }
    
    public Boolean containAbstractMethod(){
        ObservableList<JMethod> methods = mTable.getItems();
        for (JMethod jm : methods){
            if (jm.getIsAbstract().get() == true){
                isContainAbstract.set(true);
                return true;
            }      
        }
        isContainAbstract.set(false);
        return false;
    }
    
        public void setMethodForInterface(){
        ObservableList<JMethod> methods = mTable.getItems();
        if (!methods.isEmpty()){
            for (JMethod jm : methods){
                jm.setAbstract(true);
                jm.setAccess("public");
                jm.setArg1("");
                jm.setArg2("");
                jm.setArg3("");
                jm.setForInterface();
            }
        }
        
        accessColumn.setEditable(false);
        staticColumn.setEditable(false);
        abstractColumn.setEditable(false);
        arg1Column.setEditable(false);
        arg2Column.setEditable(false);
        arg3Column.setEditable(false);
        isInterface.set(true);
    }
    
    public void setMethodForClass(){
        ObservableList<JMethod> methods = mTable.getItems();
        if (!methods.isEmpty()){
            for (JMethod jm : methods){
                jm.setForClass();
            }
        }
        
        accessColumn.setEditable(true);
        staticColumn.setEditable(true);
        abstractColumn.setEditable(true);
        arg1Column.setEditable(true);
        arg2Column.setEditable(true);
        arg3Column.setEditable(true);
        isInterface.set(false);       
    }
    
    public void initTableView(){
        mTable.setEditable(true);
        
        // create name column
        TableColumn<JMethod, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(50);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.<JMethod>forTableColumn());
        nameColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<JMethod, String> t) -> {
                ((JMethod) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setName(t.getNewValue());
        });
        
        
        // create access column
        accessColumn = new TableColumn<>("Access");
        accessColumn.setMinWidth(50);
        accessColumn.setCellFactory(ComboBoxTableCell.forTableColumn("private","public"));
        accessColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<JMethod, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<JMethod, String> p) {
                return p.getValue().getAccess();
            }
        });
        accessColumn.setOnEditCommit((TableColumn.CellEditEvent<JMethod, String> t) -> {
                ((JMethod) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setAccess(t.getNewValue());
        });
        
        
        // create type column
        TableColumn<JMethod, String> returnColumn = new TableColumn<>("Type");
        returnColumn.setMinWidth(50);
        returnColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        returnColumn.setCellFactory(TextFieldTableCell.<JMethod>forTableColumn());
        returnColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<JMethod, String> t) -> {
                ((JMethod) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setType(t.getNewValue());
        });
        
        
        // create static column
        staticColumn = new TableColumn<>("Static");
        staticColumn.setMinWidth(50);
        staticColumn.setCellValueFactory(new PropertyValueFactory<>("isStatic"));   
        staticColumn.setCellFactory(new Callback<TableColumn<JMethod, Boolean>, TableCell<JMethod, Boolean>>() {
            @Override
            public TableCell<JMethod, Boolean> call(TableColumn<JMethod, Boolean> p) {
                final CheckBoxTableCell checkCell = new CheckBoxTableCell<>();
                    checkCell.setSelectedStateCallback((new Callback<Integer, ObservableValue<Boolean>>() {
                        @Override
                        public ObservableValue<Boolean> call(Integer index) {
                            return ((JMethod)mTable.getItems().get(index)).getIsStatic();
                        }
                    }));
                    return checkCell;
                }
        });
        
        // create abstract column
        abstractColumn = new TableColumn<>("Abstract");
        abstractColumn.setMinWidth(50);
        abstractColumn.setCellValueFactory(new PropertyValueFactory<>("isAbstract"));   
        abstractColumn.setCellFactory(new Callback<TableColumn<JMethod, Boolean>, TableCell<JMethod, Boolean>>() {
            @Override
            public TableCell<JMethod, Boolean> call(TableColumn<JMethod, Boolean> p) {
                final CheckBoxTableCell checkCell = new CheckBoxTableCell<>();
                    checkCell.setSelectedStateCallback((new Callback<Integer, ObservableValue<Boolean>>() {
                        @Override
                        public ObservableValue<Boolean> call(Integer index) {
                            return ((JMethod)mTable.getItems().get(index)).getIsAbstract();
                        }
                    }));
                    return checkCell;
                }
        });
        
        // create arg1 column
        arg1Column = new TableColumn<>("Arg1");
        arg1Column.setMinWidth(50);
        arg1Column.setCellValueFactory(new PropertyValueFactory<>("arg1"));
        arg1Column.setCellFactory(TextFieldTableCell.<JMethod>forTableColumn());
        arg1Column.setOnEditCommit(
            (TableColumn.CellEditEvent<JMethod, String> t) -> {
                ((JMethod) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setArg1(t.getNewValue());
        });
        
        // create arg2 column
        arg2Column = new TableColumn<>("Arg2");
        arg2Column.setMinWidth(50);
        arg2Column.setCellValueFactory(new PropertyValueFactory<>("arg2"));
        arg2Column.setCellFactory(TextFieldTableCell.<JMethod>forTableColumn());
        arg2Column.setOnEditCommit(
            (TableColumn.CellEditEvent<JMethod, String> t) -> {
                ((JMethod) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setArg2(t.getNewValue());
        });
        
        // create arg3 column
        arg3Column = new TableColumn<>("Arg3");
        arg3Column.setMinWidth(50);
        arg3Column.setCellValueFactory(new PropertyValueFactory<>("arg3"));
        arg3Column.setCellFactory(TextFieldTableCell.<JMethod>forTableColumn());
        arg3Column.setOnEditCommit(
            (TableColumn.CellEditEvent<JMethod, String> t) -> {
                ((JMethod) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setArg3(t.getNewValue());
        });
        
        
        // add columns into table
        mTable.getColumns().addAll(nameColumn, returnColumn, staticColumn, abstractColumn, accessColumn, arg1Column, arg2Column, arg3Column);
    }
        
    public String toCode(){
        ObservableList<JMethod> methods = mTable.getItems();
        String methodCode = "";
        for (JMethod j: methods){
            methodCode = methodCode + "\n" + j.toCode();  
        }  
        return methodCode;
    } 
    
    @Override
    public String toString(){
        String str="";
        ObservableList<JMethod> methods = mTable.getItems();
        if (methods.isEmpty()){
            str ="{}";
        }else{
            SimpleIntegerProperty i = new SimpleIntegerProperty(0);
            for (JMethod jm : methods){
                str = str+ "{\""+i.get()+"\":" + jm.toString() + "},\n";
                i.set(i.get()+1);
            }
               str = str + "{\""+i.get()+"\":{}}\n"; 
        }               
        return str;
    }
}
