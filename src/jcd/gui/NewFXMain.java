/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jcd.components.JClass;

/**
 *
 * @author YinqianZheng
 */
public class NewFXMain extends Application {
    private double x;
    private double y;
    private double h;
    private double w;
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        Group root = new Group();
        VBox vb = new VBox();
        
        
        
        
 
        vb.setOnMouseReleased(t->{
            System.out.println(vb.getHeight());
            System.out.println(vb.getWidth());
        });
        vb.setPrefWidth(100);
        vb.setPrefHeight(100);
        vb.getChildren().add(btn);
        vb.setLayoutX(100);
        vb.setLayoutY(100);
        vb.setStyle("-fx-background-color: red;");
        vb.setOnMousePressed(e->{
            System.out.println("sf");
            x = e.getSceneX();
            y = e.getSceneY();
            h = ((VBox)e.getSource()).getHeight();
            w = ((VBox)e.getSource()).getWidth();
        });
        vb.setOnMouseDragged(f->{
           
            vb.setPrefSize(w+f.getSceneX()-x, h+f.getSceneY()-y);
        });
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                System.out.println(vb.getHeight());
                System.out.println(vb.getWidth());
            }
        });
        root.getChildren().add(vb);
        vb.toFront();
        Scene scene = new Scene(root, 300, 300);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
