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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author YinqianZheng
 */
public class VariableBox extends VBox{
    final private VTableView<JVariable> vTable;
    private VariableBox vb;
    public VariableBox(){
        vTable = new VTableView<>();
        vb = this;
        initTableView();
    }
    
    
    
    
    public TableView<JVariable> getVariableTable(){
        return vTable;
    }
    
    public void addVariable(){
        JVariable jv = new JVariable();
        vTable.getItems().add(jv);
        this.getChildren().add(jv.getLabel());
    }
    
    public void removeVariable(){
        ObservableList<JVariable> variableSelected = vTable.getSelectionModel().getSelectedItems();
        this.getChildren().remove(variableSelected.get(0).getLabel());
        vTable.getItems().removeAll(variableSelected); 
    }
    
    private void initTableView(){
        vTable.setEditable(true);
        
        // create name column
        TableColumn<JVariable, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.<JVariable>forTableColumn());
        nameColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<JVariable, String> t) -> {
                ((JVariable) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setName(t.getNewValue());
        });
        
        
        // create access column
        TableColumn<JVariable, String> accessColumn = new TableColumn<>("Access");
        accessColumn.setCellFactory(ComboBoxTableCell.forTableColumn("Private","Public", "Protected"));
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
        typeColumn.setMinWidth(100);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setCellFactory(TextFieldTableCell.<JVariable>forTableColumn());
        typeColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<JVariable, String> t) -> {
                ((JVariable) t.getTableView().getItems().get(t.getTablePosition().getRow())
                    ).setType(t.getNewValue());
        });
        
        
        // create static column
        TableColumn<JVariable, Boolean> staticColumn = new TableColumn<>("Static");
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
    }
    
    public class VTableView<JVariable> extends TableView{
        public VTableView(){
        }
        
        
        public VariableBox getParentBox(){
            return vb;
        }
    }
}
