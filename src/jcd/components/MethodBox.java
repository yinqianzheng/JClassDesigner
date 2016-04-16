/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.components;

import javafx.beans.value.ObservableValue;
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
    private MTableView<JVariable> mTable;
    private MethodBox mb;
    public MethodBox(){
        mTable = new MTableView<JVariable>();
        initTableView();
        mb = this;
    }
    
    
    public MTableView<JVariable> getMethodTable(){
        return mTable;
    }
    
    public void initTableView(){
        mTable.setEditable(true);
        
        // create name column
        TableColumn<JVariable, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(50);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.<JVariable>forTableColumn());
        nameColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<JVariable, String> t) -> {
                ((JVariable) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setName(t.getNewValue());
        });
        
        
        // create access column
        TableColumn<JVariable, String> accessColumn = new TableColumn<>("Access");
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
        TableColumn<JVariable, String> returnColumn = new TableColumn<>("Type");
        returnColumn.setMinWidth(50);
        returnColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        returnColumn.setCellFactory(TextFieldTableCell.<JVariable>forTableColumn());
        returnColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<JVariable, String> t) -> {
                ((JVariable) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setType(t.getNewValue());
        });
        
        
        // create static column
        TableColumn<JVariable, Boolean> staticColumn = new TableColumn<>("Static");
        staticColumn.setMinWidth(50);
        staticColumn.setCellValueFactory(new PropertyValueFactory<>("isStatic"));   
        staticColumn.setCellFactory(new Callback<TableColumn<JVariable, Boolean>, TableCell<JVariable, Boolean>>() {
            @Override
            public TableCell<JVariable, Boolean> call(TableColumn<JVariable, Boolean> p) {
                final CheckBoxTableCell checkCell = new CheckBoxTableCell<>();
                    checkCell.setSelectedStateCallback((new Callback<Integer, ObservableValue<Boolean>>() {
                        @Override
                        public ObservableValue<Boolean> call(Integer index) {
                            return ((JVariable)mTable.getItems().get(index)).getIsStatic();
                        }
                    }));
                    return checkCell;
                }
        });
        
        // create abstract column
        TableColumn<JVariable, Boolean> abstractColumn = new TableColumn<>("Abstract");
        abstractColumn.setMinWidth(50);
        abstractColumn.setCellValueFactory(new PropertyValueFactory<>("isAbstract"));   
        abstractColumn.setCellFactory(new Callback<TableColumn<JVariable, Boolean>, TableCell<JVariable, Boolean>>() {
            @Override
            public TableCell<JVariable, Boolean> call(TableColumn<JVariable, Boolean> p) {
                final CheckBoxTableCell checkCell = new CheckBoxTableCell<>();
                    checkCell.setSelectedStateCallback((new Callback<Integer, ObservableValue<Boolean>>() {
                        @Override
                        public ObservableValue<Boolean> call(Integer index) {
                            return ((JVariable)mTable.getItems().get(index)).getIsStatic();
                        }
                    }));
                    return checkCell;
                }
        });
        
        // create arg1 column
        TableColumn<JVariable, String> arg1Column = new TableColumn<>("Arg1");
        arg1Column.setMinWidth(50);
        arg1Column.setCellValueFactory(new PropertyValueFactory<>("arg1"));
        arg1Column.setCellFactory(TextFieldTableCell.<JVariable>forTableColumn());
        arg1Column.setOnEditCommit(
            (TableColumn.CellEditEvent<JVariable, String> t) -> {
                ((JVariable) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setType(t.getNewValue());
        });
        
        // create arg2 column
        TableColumn<JVariable, String> arg2Column = new TableColumn<>("Arg2");
        arg2Column.setMinWidth(50);
        arg2Column.setCellValueFactory(new PropertyValueFactory<>("arg2"));
        arg2Column.setCellFactory(TextFieldTableCell.<JVariable>forTableColumn());
        arg2Column.setOnEditCommit(
            (TableColumn.CellEditEvent<JVariable, String> t) -> {
                ((JVariable) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setType(t.getNewValue());
        });
        
        // create arg3 column
        TableColumn<JVariable, String> arg3Column = new TableColumn<>("Arg3");
        arg3Column.setMinWidth(50);
        arg3Column.setCellValueFactory(new PropertyValueFactory<>("arg3"));
        arg3Column.setCellFactory(TextFieldTableCell.<JVariable>forTableColumn());
        arg3Column.setOnEditCommit(
            (TableColumn.CellEditEvent<JVariable, String> t) -> {
                ((JVariable) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setType(t.getNewValue());
        });
        
        
        // add columns into table
        mTable.getColumns().addAll(nameColumn, returnColumn, staticColumn, abstractColumn, accessColumn);
    }
    
    public class MTableView<JVariable> extends TableView{
        public MTableView(){
        }
        
        
        public MethodBox getParentBox(){
            return mb;
        }
    }
}
