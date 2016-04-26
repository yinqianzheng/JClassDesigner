/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
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

/**
 *
 * @author YinqianZheng
 */
public class VariableBox extends VBox{
    private VTableView<JVariable> vTable;
    private VariableBox vb;
    private JClass jclass;
    private TableColumn<JVariable, Boolean> staticColumn;
    private TableColumn<JVariable, String> accessColumn;
    public VariableBox(JClass jc){
        vTable = new VTableView<JVariable>();
        initTableView();
        vb = this;
        jclass = jc;
    }
    
    
    
    
    public VTableView<JVariable> getVariableTable(){
        return vTable;
    }
    
    public void setVariableForInterface(){
        ObservableList<JVariable> variables = vTable.getItems();
        if (!variables.isEmpty()){
            for (JVariable jv : variables){
                jv.setStatic(true);
                jv.setAccess("public");
            }
        }
        staticColumn.setEditable(false);
        accessColumn.setEditable(false);
    }
    
    public void setVariableForClass(){
        staticColumn.setEditable(true);
        accessColumn.setEditable(true);
    }
    
    public JClass getJClass(){
        return jclass;
    }
    
    public void addVariable(){
        JVariable jv = new JVariable(vb);
        String varName;
        SimpleBooleanProperty validName = new SimpleBooleanProperty(true);
        ObservableList<JVariable> variables = vTable.getItems();        
 
        
        for (int i = 1; i < 100; i++){
            varName = "var" + String.valueOf(i);
            validName.set(true);
            for (JVariable j: variables){
                if ((j.getName()).equals(varName)){
                    validName.set(false);
                }           
            }        
            if (validName.get() == true){
                jv.setName(varName);
                break;
            }
        }
        vTable.getItems().add(jv);
        this.getChildren().add(jv.getLabel());
    }
    
    public void addVariable(String access, String type, String name, boolean b){
        JVariable jv = new JVariable(vb, access, type, name, b);
        vTable.getItems().add(jv);
        this.getChildren().add(jv.getLabel());
    }
    
    public String toCode(){
        ObservableList<JVariable> variables = vTable.getItems();
        String variableCode = "";
        for (JVariable j: variables){
            variableCode = variableCode + "\n" + j.toCode();       
        }    
        return variableCode;
    }
    
    public void addVariable(JVariable jv){
        vTable.getItems().add(jv);
        this.getChildren().add(jv.getLabel());
    }
    
    public void removeVariable(){
        ObservableList<JVariable> variableSelected = vTable.getSelectionModel().getSelectedItems();
        this.getChildren().remove(variableSelected.get(0).getLabel());
        vTable.getItems().removeAll(variableSelected); 
    }
    
    public void initTableView(){
        vTable.setEditable(true);
        
        // create name column
        TableColumn<JVariable, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(50);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.<JVariable>forTableColumn());
        nameColumn.setCellFactory(TextFieldTableCell.<JVariable>forTableColumn());
//        nameColumn.setCellFactory(new Callback<TableColumn<JVariable, String>, TableCell<JVariable, String>>() {
//            @Override
//            public TableCell<JVariable, String> call(TableColumn<JVariable, String> p) {
//                TextFieldTableCell<JVariable, String> myTextCell = new TextFieldTableCell<JVariable, String>(){
//                    @Override
//                    public void commitEdit(String str){
//                        super.startEdit();
//                        if (isEditing() && myTextField == null) {
//        // most simple case, assuming that there is no graphic other than the field 
//        // TBD: implement the general case: walk the tree and find the field
//                        myTextField = (TextField) getGraphic();
//                        myTextField.focusedProperty().addListener((e, old, nvalue) -> {
//                    if (!nvalue) {
//                    T edited = getConverter().fromString(myTextField.getText());
//                    commitEdit(edited);
//            }
//
//        });
//    }       
//                    } 
//                };
//                return myTextCell;
//            }
//        });
        nameColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<JVariable, String> t) -> {
                ((JVariable) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setName(t.getNewValue());
        });
        
        
        // create access column
        accessColumn = new TableColumn<>("Access");
        accessColumn.setMinWidth(50);
        accessColumn.setCellFactory(ComboBoxTableCell.forTableColumn("private","public", "protected"));
        accessColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<JVariable, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<JVariable, String> p) {
                return p.getValue().getAccess();
            }
        });
        accessColumn.setOnEditCommit((TableColumn.CellEditEvent<JVariable, String> t) -> {
                ((JVariable) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setAccess(t.getNewValue());
        });
        
        
        // create type column
        TableColumn<JVariable, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setMinWidth(50);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setCellFactory(TextFieldTableCell.<JVariable>forTableColumn());
        typeColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<JVariable, String> t) -> {
                ((JVariable) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setType(t.getNewValue());
        });
        
        
        // create static column
        staticColumn = new TableColumn<>("Static");
        staticColumn.setMinWidth(50);
        staticColumn.setCellValueFactory(new PropertyValueFactory<>("isStatic"));   
        staticColumn.setCellFactory(new Callback<TableColumn<JVariable, Boolean>, TableCell<JVariable, Boolean>>() {
            @Override
            public TableCell<JVariable, Boolean> call(TableColumn<JVariable, Boolean> p) {
                final CheckBoxTableCell checkCell = new CheckBoxTableCell<>();
                    checkCell.setSelectedStateCallback((new Callback<Integer, ObservableValue<Boolean>>() {
                        @Override
                        public ObservableValue<Boolean> call(Integer index) {
                            return ((JVariable)vTable.getItems().get(index)).getIsStatic();
                        }
                    }));
                    return checkCell;
                }
        });
        
        // add columns into table
        vTable.getColumns().addAll(nameColumn, typeColumn, staticColumn, accessColumn);
    }
    
    @Override
    public String toString(){
        String str="";
        ObservableList<JVariable> variables = vTable.getItems();
        if (variables.isEmpty()){
            str ="{}";
        }else{
            SimpleIntegerProperty i = new SimpleIntegerProperty(0);
            for (JVariable jv : variables){
                str = str+ "{\""+i.get()+"\":" + jv.toString() + "},\n";
                i.set(i.get()+1);
            }
               str = str + "{\""+i.get()+"\":{}}\n"; 
        }
               
        return str;
    }
    
    public class VTableView<JVariable> extends TableView{
        public VTableView(){
        }
        
        
        public VariableBox getParentBox(){
            return vb;
        }
    }
}
