/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Window;
import javax.json.JsonObject;
import jcd.components.JClass;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author YinqianZheng
 */
public class JFileManagerSaveTest {
    
    public JFileManagerSaveTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("* JFileManagerSaveTest: @BeforeClass saveData");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("* JFileManagerSaveTest: @AfterClass saveData");
    }



    /**
     * Test of saveData method, of class JFileManager.
     */
    @Test
    public void testSaveData1() throws IOException {
        System.out.println("* JFilemanagerTest: test method 1 - testSaveData1()");
        String str = "adf";
        String path = "./src/jcd/test_bed/testSave.json";
        boolean result = JFileManager.saveData(str, path);
        assertEquals(true, result);
    }

    /**
     * Test of saveData method, of class JFileManager.
     */
    @Test
    public void testSaveData2() throws IOException {
        System.out.println("* JFilemanagerTest: test method 2 - testSaveData2()");
        int i = 24;
        String path = "./src/jcd/test_bed/testSave.json";
        boolean result = JFileManager.saveData(i, path);
        assertEquals(true, result);
    }

    /**
     * Test of saveData method, of class JFileManager.
     */
    @Test
    public void testSaveData3() throws IOException {
        System.out.println("* JFilemanagerTest: test method 3 - testSaveData3()");
        String path = "./src/jcd/test_bed/testSave.json";
        boolean result = JFileManager.saveData("ew", path);
        assertEquals(true, result);
    }

    /**
     * Test of saveData method, of class JFileManager.
     */
    @Test
    public void testSaveData4() throws IOException {
        System.out.println("* JFilemanagerTest: test method 4 - testSaveData4()");
        Rectangle trg = new Rectangle();
        String path = "./src/jcd/test_bed/testSave.json";
        boolean result = JFileManager.saveData(trg, path);
        assertEquals(true, result);
    }

    /**
     * Test of saveData method, of class JFileManager.
     */
    
    @Test
    public void testSaveData5() throws InterruptedException, IOException{
        System.out.println("* JFilemanagerTest: test method 5 - testSaveData5()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JClass jc = new JClass(34, 56);
                            String path = "./src/jcd/test_bed/testSave.json";
                            boolean result = JFileManager.saveData(jc, path);
                            assertEquals(true, result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerSaveTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }

    /**
     * Test of saveData method, of class JFileManager.
     */
    
    @Test
    public void testSaveData6() throws InterruptedException, IOException{
        System.out.println("* JFilemanagerTest: test method 6 - testSaveData6()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JClass jc = new JClass(34, 56);
                            String path = "./src/jcd/test_bed/testSave.json";
                            boolean result = JFileManager.saveData(jc, path);
                            assertEquals(true, result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerSaveTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }

    /**
     * Test of saveData method, of class JFileManager.
     */
    
    @Test
    public void testSaveData7() throws InterruptedException, IOException{
        System.out.println("* JFilemanagerTest: test method 7 - testSaveData7()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JClass jc = new JClass(34, 56);
                            String path = "./src/jcd/test_bed/testSave.json";
                            boolean result = JFileManager.saveData(jc, path);
                            assertEquals(true, result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerSaveTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }

    /**
     * Test of saveData method, of class JFileManager.
     */
    
    @Test
    public void testSaveData8() throws InterruptedException, IOException{
        System.out.println("* JFilemanagerTest: test method 8 - testSaveData8()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JClass jc = new JClass(34, 56);
                            String path = "./src/jcd/test_bed/testSave.json";
                            boolean result = JFileManager.saveData(jc, path);
                            assertEquals(true, result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerSaveTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }

    /**
     * Test of saveData method, of class JFileManager.
     */
    
    @Test
    public void testSaveData10() throws InterruptedException, IOException{
        System.out.println("* JFilemanagerTest: test method 10 - testSaveData10()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JClass jc = new JClass(34, 56);
                            String path = "./src/jcd/test_bed/testSave.json";
                            boolean result = JFileManager.saveData(jc, path);
                            assertEquals(true, result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerSaveTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }

    
     /**
     * Test of saveData method, of class JFileManager.
     */
    @Test
    public void testSaveData9() throws IOException {
        System.out.println("* JFilemanagerTest: test method 9 - testSaveData9()");
        String str = "wefsfasfjkahwehfkk\nasfsd\n\nasfsadf";
        String path = "./src/jcd/test_bed/testSave.json";
        boolean result = JFileManager.saveData(str, path);
        assertEquals(true, result);
    }
    
}
