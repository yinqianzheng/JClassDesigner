/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.util.HashMap;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
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
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import jcd.data.DataManager;



/**
 *
 * @author YinqianZheng
 */
public class WorkSpace extends Application{
    boolean isCreated = false;
    final BorderPane window = new BorderPane();
    final HBox topToolbar = new HBox();
    final SplitPane splitPane = new SplitPane();
    final public Pane root = new Pane();
    final Canvas canvas= new Canvas(3205, 2580);
    StackPane stackPane = new StackPane(root, canvas);
    final private Group gp = new Group(stackPane);
    final ScrollPane workPane = new ScrollPane(gp);
    final VBox component = new VBox();
    final HBox fileToolbar = new HBox();
    final HBox editToolbar = new HBox();
    final HBox viewToolbar = new HBox();
    final ScrollPane variablePane = new ScrollPane();
    final ScrollPane methodPane = new ScrollPane();
    final Button addVariable = new Button();
    final Button deleteVariable = new Button();
    final Button addMethod = new Button();
    final Button deleteMethod = new Button();
    final TableView methodTable = new TableView();
    final private CheckBox grid = new CheckBox("Grid");
    final private CheckBox snap = new CheckBox("Snap");
    private SimpleBooleanProperty isGridSnappingActived = new SimpleBooleanProperty(false);
    private SimpleDoubleProperty zoomValue = new SimpleDoubleProperty(1);
    final DropShadow highlight = new DropShadow(20, Color.YELLOW);
    public TextField classNameInput;
    public TextField packageNameInput;
    public ComboBox parentsList;
    public static boolean isSelectMode = false;
    
    public static Window primaryStageWindow;
    public HashMap<String, Button> buttonMap = new HashMap<>();
    //public CheckBox interfaceCheckBox = new CheckBox();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStageWindow = primaryStage;
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
        gridBackGroundSetActions();
        initFunctionForZoom_In_Out();
    }
    
    public double getZoomValue(){
        return zoomValue.get();
    }
    
    public void setZoomValue(double v){
        zoomValue.set(v);
    }
    
    private void gridBackGroundSetActions(){
        snap.setDisable(true);
        grid.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1){
                    root.getStyleClass().clear();
                    root.getStyleClass().add("root_Backgraound_gridLine");
                    snap.setDisable(false);                    
                }else{
                    root.getStyleClass().clear();
                    root.getStyleClass().add("root_Backgraound");
                    snap.setSelected(false);
                    snap.setDisable(true);                   
                }
            }
        });
        
        snap.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                isGridSnappingActived.set(t1);
            }
        }); 
    }
    
    private void initFunctionForZoom_In_Out(){
        buttonMap.get("zoom in").setOnAction(e->{
            zoomValue.set(zoomValue.get()*1.2);
        });
        buttonMap.get("zoom out").setOnAction(e->{
            zoomValue.set(zoomValue.get()/1.2);
        });
        buttonMap.get("original").setOnAction(e->{
            zoomValue.set(1);
        });
        zoomValue.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                if (zoomValue.get() < 0.28)
                    buttonMap.get("zoom out").setDisable(true);
                else if (zoomValue.get() > 3.5)
                    buttonMap.get("zoom in").setDisable(true);
                else{
                    buttonMap.get("zoom in").setDisable(false);
                    buttonMap.get("zoom out").setDisable(false);
                }
                stackPane.setScaleX(zoomValue.get());
                stackPane.setScaleY(zoomValue.get());
                    
            }
        });
    }
    
    public boolean isGridSnapActived(){
        return isGridSnappingActived.get();
    }
        
    private void initWindow(){
        topToolbar.getChildren().addAll(fileToolbar, editToolbar, viewToolbar);
        topToolbar.getStyleClass().add("topToolbar_bg");
        window.setTop(topToolbar);
        //canvas.setStyle("-fx-background-color: lightgoldenrodyellow;");
        canvas.setDisable(true);
        canvas.setMouseTransparent(false);
        canvas.setCursor(Cursor.CROSSHAIR);
        root.setCursor(Cursor.HAND);
        //root.getChildren().add(canvas);
        component.setStyle("-fx-background-color: papayawhip;");       
        root.getStyleClass().add("root_Backgraound");
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
            try{
            HandleEvent.changeClassName(newValue);
            }catch(Exception ex){
            }
        });
        packageNameInput = new TextField ();
        packageNameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                HandleEvent.changePackageName(newValue);
            } catch (Exception e) {
            }
        });
//        interfaceCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
//                HandleEvent.setToInterface(t1);
//            }
//        });
        
        packageNameInput.setPrefWidth(120);
        parentsList = DataManager.getParentList();
        parentsList.setPrefWidth(120);
        VBox names = new VBox();
        names.getStyleClass().add("names_Style");
        HBox cpp = new HBox();
        Label titles = new Label("Class Name:");
        titles.getStyleClass().add("heading_label");
        cpp.getChildren().addAll(titles, classNameInput);
        cpp.setStyle("-fx-spacing: 15;");
        names.getChildren().add(cpp);
        
//        cpp = new HBox();
//        titles = new Label("Set to interface");
//        titles.getStyleClass().add("heading_label");
//        cpp.getChildren().addAll(interfaceCheckBox, titles);
//        names.getChildren().add(cpp);
        
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
        StackPane sp = new StackPane();
        sp.getChildren().add(variablePane);
        sp.setPrefSize(300, 200);
        vB.getChildren().addAll(variables, sp);
        component.getChildren().add(vB);
        titles = new Label("Variables:");
        HBox hp = new HBox();
        hp.setStyle("-fx-spacing: 5;");
        variables.getChildren().addAll(titles,hp);
        variables.setStyle("-fx-spacing: 10;");
        
        addVariable.setTooltip(new Tooltip("add variable"));
        ImageView images = new ImageView(new Image("file:./images/add.png"));
        images.setFitWidth(20);
        images.setFitHeight(20);
        addVariable.setGraphic(images);
        addVariable.setOnAction(HandleEvent.addVariable);
        addVariable.setDisable(true);
        hp.getChildren().add(addVariable);
        deleteVariable.setTooltip(new Tooltip("remove variable"));
        images = new ImageView(new Image("file:./images/delete.png"));
        images.setFitWidth(20);
        images.setFitHeight(20);
        deleteVariable.setGraphic(images);
        deleteVariable.setOnAction(HandleEvent.deleteVariable);
        deleteVariable.setDisable(true);
        hp.getChildren().add(deleteVariable);
        
        
        VBox mB = new VBox();
        mB.getStyleClass().add("variables_methods_edit_window_style");
        HBox methods = new HBox();
        sp = new StackPane();
        sp.getChildren().add(methodPane);
        sp.setPrefSize(300, 200);
        mB.getChildren().addAll(methods, sp);
        component.getChildren().add(mB);
        titles = new Label("Methods:");
        HBox fp2 = new HBox();
        fp2.setStyle("-fx-spacing: 5;");
        methods.getChildren().addAll(titles,fp2);
        methods.setStyle("-fx-spacing: 10;");
        
        addMethod.setTooltip(new Tooltip("add method"));
        images = new ImageView(new Image("file:./images/add.png"));
        images.setFitWidth(20);
        images.setFitHeight(20);
        addMethod.setGraphic(images);
        addMethod.setOnAction(HandleEvent.addMethod);
        addMethod.setDisable(true);
        fp2.getChildren().add(addMethod);
        deleteMethod.setTooltip(new Tooltip("remove method"));
        images = new ImageView(new Image("file:./images/delete.png"));
        images.setFitWidth(20);
        images.setFitHeight(20);
        deleteMethod.setGraphic(images);
        deleteMethod.setOnAction(HandleEvent.deleteMethod);
        deleteMethod.setDisable(true);
        fp2.getChildren().add(deleteMethod);
        component.getStyleClass().add("componentBar_view");
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
        buttonMap.put(toolTip, bt);
    }
    
    
    private void initToolbar(){
        initbuttons(fileToolbar, "file:./images/New.png" , 25, 25, "new", HandleEvent.newEvent, false);
        initbuttons(fileToolbar, "file:./images/Load.png" , 25, 25, "load", HandleEvent.loadEvent, false);
        initbuttons(fileToolbar, "file:./images/Save.png" , 25, 25, "save", HandleEvent.saveEvent, true);
        initbuttons(fileToolbar, "file:./images/SaveAs.png" , 25, 25, "save as", HandleEvent.saveAsEvent, true);
        initbuttons(fileToolbar, "file:./images/ExportImage.png" , 25, 25, "export photo", HandleEvent.exportPhoto, true);
        initbuttons(fileToolbar, "file:./images/ExportCode.png" , 25, 25, "export code", HandleEvent.exportCode, true);
        initbuttons(fileToolbar, "file:./images/Exit.png" , 25, 25, "exit", HandleEvent.exitEvent, false);
       
        initbuttons(editToolbar, "file:./images/Select.png" , 35, 40, "select", HandleEvent.selectEvent, false);
        initbuttons(editToolbar, "file:./images/Resize.png" , 35, 40, "resize", HandleEvent.resizeEvent, true);
        initbuttons(editToolbar, "file:./images/AddClass.png" , 35, 40, "add class", HandleEvent.addEvent, false);
        initbuttons(editToolbar, "file:./images/AddInterface.png" , 35, 40, "add interface", HandleEvent.addInterface, false);
        initbuttons(editToolbar, "file:./images/Remove.png" , 35, 40, "remove", HandleEvent.removeClass, false);
        initbuttons(editToolbar, "file:./images/undo.png" , 35, 40, "undo", HandleEvent.undoEvent, true);
        initbuttons(editToolbar, "file:./images/redo.png" , 35, 40, "redo", HandleEvent.redoEvent,true);
        
        initbuttons(viewToolbar, "file:./images/OriginalZoom.png" , 35, 40, "original", HandleEvent.originalZoomEvent, false);
        initbuttons(viewToolbar, "file:./images/ZoomIn.png" , 35, 40, "zoom in", HandleEvent.zoomInEvent, false);
        initbuttons(viewToolbar, "file:./images/ZoomOut.png" , 35, 40, "zoom out", HandleEvent.zoomOutEvent, false);
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
        try {
            packageNameInput.clear();
        } catch (Exception e) {
        }
    }
    
    public void setPackageNameInput(String str){
        packageNameInput.clear();
        if (str!=null)
            packageNameInput.setText(str);
    }
    
    public void setVariablePane(TableView vb){
        variablePane.setContent(vb);
    }
    
    public void setMethodPane(TableView mb){
        methodPane.setContent(mb);
    }
    
    public Button getAddVariableButton(){
        return addVariable;
    }
    
    public Button getDeleteVariableButton(){
        return deleteVariable;
    }
    
    public Button getAddMethodButton(){
        return addMethod;
    }
    
    public Button getDeleteMethodButton(){
        return deleteMethod;
    }

    public void reload(){
        root.getChildren().clear();
        //root.getChildren().add(canvas);
        variablePane.setContent(null);
        methodPane.setContent(null);
        addVariable.setDisable(true);
        deleteVariable.setDisable(true);
        addMethod.setDisable(true);
        deleteMethod.setDisable(true);
        clearClassNameInput();
        clearPackageNameInput();
        DataManager.clear();
        buttonMap.get("undo").setDisable(true);
        buttonMap.get("redo").setDisable(true);
        root.setScaleX(1);
        root.setScaleY(1);
    }
    
    public void reloadForUndoRedo(){
        root.getChildren().clear();
        variablePane.setContent(null);
        methodPane.setContent(null);
        addVariable.setDisable(true);
        deleteVariable.setDisable(true);
        addMethod.setDisable(true);
        deleteMethod.setDisable(true);
        clearClassNameInput();
        clearPackageNameInput();
        DataManager.clearForUndoRedo();
        root.setScaleX(1);
        root.setScaleY(1);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
