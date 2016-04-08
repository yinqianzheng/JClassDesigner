/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



/**
 *
 * @author YinqianZheng
 */
public class WorkSpace extends Application{
    boolean isCreated = false;
    final BorderPane window = new BorderPane();
    final HBox topToolbar = new HBox();
    final SplitPane splitPane = new SplitPane();
    final Pane root = new Pane();
    final ScrollPane workPane = new ScrollPane(root);
    final VBox component = new VBox();
    final HBox fileToolbar = new HBox();
    final HBox editToolbar = new HBox();
    final HBox viewToolbar = new HBox();
    final Canvas canvas= new Canvas(3000, 3000);
    final TableView variableTable = new TableView();
    final TableView methodTable = new TableView();
    
    final CheckBox grid = new CheckBox("Grid");
    final CheckBox snap = new CheckBox("Snap");
    final DropShadow highlight = new DropShadow(20, Color.YELLOW);
    private TextField classNameInput;
    private TextField packageNameInput;
    private ComboBox parentsList;
    public static boolean isSelectMode = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initToolbar();
        initComponentToolbar();
        initWindow();
        Scene scene = new Scene(window, 1200, 900);
        scene.getStylesheets().add("file:./src/jcd/css/jcd_style.css");
        primaryStage.setTitle("JClassDesigner");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        HandleEvent.getInstance(this);
       
    }
    
        
    private void initWindow(){
        topToolbar.getChildren().addAll(fileToolbar, editToolbar, viewToolbar);
        topToolbar.getStyleClass().add("topToolbar_bg");
        window.setTop(topToolbar);
        canvas.setStyle("-fx-background-color: lightgoldenrodyellow;");
        canvas.setDisable(true);
        root.getChildren().add(canvas);
        component.setStyle("-fx-background-color: papayawhip;");
        root.setStyle("-fx-background-color: lightgoldenrodyellow;");
        canvas.setOnMouseClicked(HandleEvent.addClass);       
        fileToolbar.getStyleClass().add("fileToolbar");
        editToolbar.getStyleClass().add("toolbar");
        viewToolbar.getStyleClass().add("toolbar");
        splitPane.getItems().addAll(workPane, component);
        splitPane.setDividerPositions(0.75);
        window.setCenter(splitPane);
        window.setStyle("-fx-spacing: 10;");
    }
    
    private void initComponentToolbar(){
        classNameInput = new TextField ();
        classNameInput.setPrefWidth(120);
        classNameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            HandleEvent.changeClassName(newValue);
        });
        packageNameInput = new TextField ();
        packageNameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            HandleEvent.changePackageName(newValue);
        });
        
        packageNameInput.setPrefWidth(120);
        parentsList = new ComboBox();
        parentsList.setPrefWidth(120);
        VBox names = new VBox();
        names.getStyleClass().add("names_Style");
        HBox cpp = new HBox();
        Label titles = new Label("Class Name:");
        titles.getStyleClass().add("heading_label");
        cpp.getChildren().addAll(titles, classNameInput);
        cpp.setStyle("-fx-spacing: 15;");
        names.getChildren().add(cpp);
        cpp = new HBox();
        titles = new Label("Package:");
        titles.getStyleClass().add("subheading_label");
        cpp.getChildren().addAll(titles, packageNameInput);
        names.getChildren().add(cpp);
        cpp.setStyle("-fx-spacing: 72;");
        cpp = new HBox();
        titles = new Label("Parent:");
        titles.getStyleClass().add("subheading_label");
        cpp.getChildren().addAll(titles, parentsList);
        names.getChildren().add(cpp);
        cpp.setStyle("-fx-spacing: 85;");
        component.getChildren().add(names);
        
        VBox vB = new VBox();
        vB.getStyleClass().add("variables_methods_edit_window_style");
        HBox variables = new HBox();
        ScrollPane vscrollPane = new ScrollPane();
        initVTable();
        vscrollPane.setContent(variableTable);
        StackPane sp = new StackPane();
        sp.getChildren().add(vscrollPane);
        sp.setPrefSize(300, 200);
        vB.getChildren().addAll(variables, sp);
        component.getChildren().add(vB);
        titles = new Label("Variables:");
        HBox hp = new HBox();
        hp.setStyle("-fx-spacing: 5;");
        variables.getChildren().addAll(titles,hp);
        variables.setStyle("-fx-spacing: 10;");
        initbuttons(hp, "file:./images/add.png" , 20, 20, "add variable", HandleEvent.newEvent, true);
        initbuttons(hp, "file:./images/delete.png" , 20, 20, "remove variable", HandleEvent.newEvent, true);
        
        
        VBox mB = new VBox();
        mB.getStyleClass().add("variables_methods_edit_window_style");
        HBox methods = new HBox();
        ScrollPane mscrollPane = new ScrollPane();
        initMTable();
        mscrollPane.setContent(methodTable);
        sp = new StackPane();
        sp.getChildren().add(mscrollPane);
        sp.setPrefSize(300, 200);
        mB.getChildren().addAll(methods, sp);
        component.getChildren().add(mB);
        titles = new Label("Methods:");
        HBox fp2 = new HBox();
        fp2.setStyle("-fx-spacing: 5;");
        methods.getChildren().addAll(titles,fp2);
        methods.setStyle("-fx-spacing: 10;");
        initbuttons(fp2, "file:./images/add.png" , 20, 20, "add method", HandleEvent.newEvent, true);
        initbuttons(fp2, "file:./images/delete.png" , 20, 20, "remove method", HandleEvent.newEvent, true);
        component.getStyleClass().add("componentBar_view");
    }
        
    private void initVTable(){
        TableColumn variableName = new TableColumn("Name");
        TableColumn variableType = new TableColumn("Type");
        TableColumn isStatic = new TableColumn("Static");
        TableColumn access = new TableColumn("Access");
        variableTable.getColumns().addAll(variableName, variableType, isStatic, access);
        
    }
    
    private void initMTable(){
        TableColumn methodName = new TableColumn("Name");
        TableColumn returnType = new TableColumn("Type");
        TableColumn isStatic = new TableColumn("Static");
        TableColumn access = new TableColumn("Access");
        TableColumn arg1 = new TableColumn("arg1");
        TableColumn arg2 = new TableColumn("arg2");
        TableColumn arg3 = new TableColumn("arg3");
        methodTable.getColumns().addAll(methodName, returnType, isStatic, access, arg1, arg2, arg3);
     
    }
    
    private void initbuttons(HBox toolbar, String path, double width, double hight,
                                           String toolTip, EventHandler e, boolean tf){
        Button bt = new Button();
        bt.setTooltip(new Tooltip(toolTip));
        ImageView images = new ImageView(new Image(path));
        images.setFitWidth(width);
        images.setFitHeight(hight);
        bt.setGraphic(images);
        bt.setOnAction(e);
        bt.setDisable(tf);
        toolbar.getChildren().add(bt);
    }
    
    
    private void initToolbar(){
        initbuttons(fileToolbar, "file:./images/New.png" , 25, 25, "new", HandleEvent.newEvent, false);
        initbuttons(fileToolbar, "file:./images/Load.png" , 25, 25, "load", HandleEvent.loadEvent, true);
        initbuttons(fileToolbar, "file:./images/Save.png" , 25, 25, "save", HandleEvent.saveEvent, true);
        initbuttons(fileToolbar, "file:./images/SaveAs.png" , 25, 25, "save as", HandleEvent.saveAsEvent, true);
        initbuttons(fileToolbar, "file:./images/ExportImage.png" , 25, 25, "export photo", HandleEvent.selectEvent, true);
        initbuttons(fileToolbar, "file:./images/ExportCode.png" , 25, 25, "export code", HandleEvent.selectEvent, true);
        initbuttons(fileToolbar, "file:./images/Exit.png" , 25, 25, "exit", HandleEvent.exitEvent, false);
       
        initbuttons(editToolbar, "file:./images/Select.png" , 35, 40, "select", HandleEvent.selectEvent, false);
        initbuttons(editToolbar, "file:./images/Resize.png" , 35, 40, "resize", HandleEvent.selectEvent, true);
        initbuttons(editToolbar, "file:./images/AddClass.png" , 35, 40, "add class", HandleEvent.addEvent, false);
        initbuttons(editToolbar, "file:./images/AddInterface.png" , 35, 40, "add interface", HandleEvent.selectEvent, true);
        initbuttons(editToolbar, "file:./images/Remove.png" , 35, 40, "remove", HandleEvent.removeClass, false);
        initbuttons(editToolbar, "file:./images/undo.png" , 35, 40, "undo", HandleEvent.selectEvent, true);
        initbuttons(editToolbar, "file:./images/redo.png" , 35, 40, "redo", HandleEvent.exitEvent,true);
        
        initbuttons(viewToolbar, "file:./images/ZoomIn.png" , 35, 40, "zoom in", HandleEvent.selectEvent, true);
        initbuttons(viewToolbar, "file:./images/ZoomOut.png" , 35, 40, "zoom out", HandleEvent.selectEvent, true);
        VBox checkboxes = new VBox();
        checkboxes.getStyleClass().add("checkBoxes");
        checkboxes.getChildren().addAll(grid, snap);
        viewToolbar.getChildren().add(checkboxes);
    }
    
    public void clearClassNameInput(){
        classNameInput.clear();
    }
    
    public void setClassNameInput(String str){
        classNameInput.clear();
        if (str!=null)
            classNameInput.setText(str);

    }
    public void clearPackageNameInput(){
        packageNameInput.clear();
    }
    
    public void setPackageNameInput(String str){
        packageNameInput.clear();
        if (str!=null)
            packageNameInput.setText(str);
    }

    public void reload(){
        root.getChildren().clear();
        root.getChildren().add(canvas);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
