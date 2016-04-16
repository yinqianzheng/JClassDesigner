/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

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
public class MethodBox extends VBox{
    private MTableView<JMethod> mTable;
    private MethodBox mb;
    public MethodBox(){
        mTable = new MTableView<JMethod>();
        initTableView();
        mb = this;
    }
    
    
    public MTableView<JMethod> getMethodTable(){
        return mTable;
    }
    
    
        public void addMethod(){
        JMethod jm = new JMethod();
        mTable.getItems().add(jm);
        this.getChildren().add(jm.getLabel());
    }
    
    public void removeMethod(){
        ObservableList<JMethod> methodSelected = mTable.getSelectionModel().getSelectedItems();
        this.getChildren().remove(methodSelected.get(0).getLabel());
        mTable.getItems().removeAll(methodSelected); 
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
        TableColumn<JMethod, String> accessColumn = new TableColumn<>("Access");
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
        TableColumn<JMethod, Boolean> staticColumn = new TableColumn<>("Static");
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
        TableColumn<JMethod, Boolean> abstractColumn = new TableColumn<>("Abstract");
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
        TableColumn<JMethod, String> arg1Column = new TableColumn<>("Arg1");
        arg1Column.setMinWidth(50);
        arg1Column.setCellValueFactory(new PropertyValueFactory<>("arg1"));
        arg1Column.setCellFactory(TextFieldTableCell.<JMethod>forTableColumn());
        arg1Column.setOnEditCommit(
            (TableColumn.CellEditEvent<JMethod, String> t) -> {
                ((JMethod) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setType(t.getNewValue());
        });
        
        // create arg2 column
        TableColumn<JMethod, String> arg2Column = new TableColumn<>("Arg2");
        arg2Column.setMinWidth(50);
        arg2Column.setCellValueFactory(new PropertyValueFactory<>("arg2"));
        arg2Column.setCellFactory(TextFieldTableCell.<JMethod>forTableColumn());
        arg2Column.setOnEditCommit(
            (TableColumn.CellEditEvent<JMethod, String> t) -> {
                ((JMethod) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setType(t.getNewValue());
        });
        
        // create arg3 column
        TableColumn<JMethod, String> arg3Column = new TableColumn<>("Arg3");
        arg3Column.setMinWidth(50);
        arg3Column.setCellValueFactory(new PropertyValueFactory<>("arg3"));
        arg3Column.setCellFactory(TextFieldTableCell.<JMethod>forTableColumn());
        arg3Column.setOnEditCommit(
            (TableColumn.CellEditEvent<JMethod, String> t) -> {
                ((JMethod) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setType(t.getNewValue());
        });
        
        
        // add columns into table
        mTable.getColumns().addAll(nameColumn, returnColumn, staticColumn, abstractColumn, accessColumn, arg1Column, arg2Column, arg3Column);
    }
    
    public class MTableView<JVariable> extends TableView{
        public MTableView(){
        }
        
        
        public MethodBox getParentBox(){
            return mb;
        }
    }
}