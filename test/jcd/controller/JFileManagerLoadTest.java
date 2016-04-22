/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Window;
import javax.json.JsonArray;
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
public class JFileManagerLoadTest {
    
    public JFileManagerLoadTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("* JFileManagerLoadTest: @BeforeClass loadFile");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("* JFileManagerLoadTest: @BeforeClass loadFile");
    }

     /**
     * Test of loadFile method, of class JFileManager.
     */    
    @Test
    public void testLoadFile1() throws InterruptedException, IOException{
        System.out.println("* JFileManagerLoadTest: test method 1 - testLoadFile1()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String path = "./src/jcd/test_bed/testLoad3.json";
                            JsonObject returnObj = JFileManager.loadFile(path);
                            JsonObject classes = ((JsonObject)((JsonObject)((JsonArray)returnObj.get("classes")).get(0)).get(String.valueOf("0")));
                            JsonObject temp = ((JsonObject)((JsonObject)((JsonArray)classes.get("class")).get(0)).get("0"));
                            String name = temp.get("name").toString();
                            name = name.substring(1, name.length()-1);
        
                            String result = name;
                            assertEquals("testLoad3", result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerLoadTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }
    

     /**
     * Test of loadFile method, of class JFileManager.
     */    
    @Test
    public void testLoadFile2() throws InterruptedException, IOException{
        System.out.println("* JFileManagerLoadTest: test method 2 - testLoadFile2()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String path = "./src/jcd/test_bed/testLoad3.json";
                            JsonObject returnObj = JFileManager.loadFile(path);
                            JsonObject classes = ((JsonObject)((JsonObject)((JsonArray)returnObj.get("classes")).get(0)).get(String.valueOf("0")));
                            JsonObject temp = ((JsonObject)((JsonObject)((JsonArray)classes.get("class")).get(0)).get("0"));
                            boolean result = Boolean.parseBoolean(temp.get("abstract").toString());
                            
                            assertEquals(false, result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerLoadTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }
    

     /**
     * Test of loadFile method, of class JFileManager.
     */    
    @Test
    public void testLoadFile3() throws InterruptedException, IOException{
        System.out.println("* JFileManagerLoadTest: test method 3 - testLoadFile3()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String path = "./src/jcd/test_bed/testLoad3.json";
                            JsonObject returnObj = JFileManager.loadFile(path);
                            JsonObject classes = ((JsonObject)((JsonObject)((JsonArray)returnObj.get("classes")).get(0)).get(String.valueOf("0")));
                            JsonObject temp = ((JsonObject)((JsonObject)((JsonArray)classes.get("class")).get(1)).get("1"));
                            temp = ((JsonObject)((JsonObject)((JsonArray)temp.get("variables")).get(0)).get(String.valueOf(0)));

                            String name = temp.get("name").toString();
                            name = name.substring(1, name.length()-1);
        
                            String result = name;
                            assertEquals("test3var1", result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerLoadTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }
    
     /**
     * Test of loadFile method, of class JFileManager.
     */    
    @Test
    public void testLoadFile4() throws InterruptedException, IOException{
        System.out.println("* JFileManagerLoadTest: test method 4 - testLoadFile4()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String path = "./src/jcd/test_bed/testLoad3.json";
                            JsonObject returnObj = JFileManager.loadFile(path);
                            JsonObject classes = ((JsonObject)((JsonObject)((JsonArray)returnObj.get("classes")).get(0)).get(String.valueOf("0")));
                            JsonObject temp = ((JsonObject)((JsonObject)((JsonArray)classes.get("class")).get(1)).get("1"));
                            temp = ((JsonObject)((JsonObject)((JsonArray)temp.get("variables")).get(0)).get(String.valueOf(0)));

                            String type = temp.get("type").toString();
                            type = type.substring(1, type.length()-1);
        
                            String result = type;
                            assertEquals("String", result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerLoadTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }
    
     /**
     * Test of loadFile method, of class JFileManager.
     */    
    @Test
    public void testLoadFile5() throws InterruptedException, IOException{
        System.out.println("* JFileManagerLoadTest: test method 5 - testLoadFile5()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String path = "./src/jcd/test_bed/testLoad3.json";
                            JsonObject returnObj = JFileManager.loadFile(path);
                            JsonObject classes = ((JsonObject)((JsonObject)((JsonArray)returnObj.get("classes")).get(0)).get(String.valueOf("0")));
                            JsonObject temp = ((JsonObject)((JsonObject)((JsonArray)classes.get("class")).get(1)).get("1"));
                            temp = ((JsonObject)((JsonObject)((JsonArray)temp.get("variables")).get(0)).get(String.valueOf(0)));

                            String access = temp.get("access").toString();
                            access = access.substring(1, access.length()-1);
        
                            String result = access;
                            assertEquals("public", result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerLoadTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }

     /**
     * Test of loadFile method, of class JFileManager.
     */    
    @Test
    public void testLoadFile6() throws InterruptedException, IOException{
        System.out.println("* JFileManagerLoadTest: test method 6 - testLoadFile6()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String path = "./src/jcd/test_bed/testLoad3.json";
                            JsonObject returnObj = JFileManager.loadFile(path);
                            JsonObject classes = ((JsonObject)((JsonObject)((JsonArray)returnObj.get("classes")).get(0)).get(String.valueOf("0")));
                            JsonObject temp = ((JsonObject)((JsonObject)((JsonArray)classes.get("class")).get(1)).get("1"));
                            temp = ((JsonObject)((JsonObject)((JsonArray)temp.get("variables")).get(1)).get(String.valueOf(1)));

                            String name = temp.get("name").toString();
                            name = name.substring(1, name.length()-1);
        
                            String result = name;
                            assertEquals("test3var2", result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerLoadTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }
    

     /**
     * Test of loadFile method, of class JFileManager.
     */    
    @Test
    public void testLoadFile7() throws InterruptedException, IOException{
        System.out.println("* JFileManagerLoadTest: test method 7 - testLoadFile7()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String path = "./src/jcd/test_bed/testLoad3.json";
                            JsonObject returnObj = JFileManager.loadFile(path);
                            JsonObject classes = ((JsonObject)((JsonObject)((JsonArray)returnObj.get("classes")).get(0)).get(String.valueOf("0")));
                            JsonObject temp = ((JsonObject)((JsonObject)((JsonArray)classes.get("class")).get(1)).get("1"));
                            temp = ((JsonObject)((JsonObject)((JsonArray)temp.get("variables")).get(2)).get(String.valueOf(2)));

                            String name = temp.get("name").toString();
                            name = name.substring(1, name.length()-1);
        
                            String result = name;
                            assertEquals("test3var3", result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerLoadTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }
    
     /**
     * Test of loadFile method, of class JFileManager.
     */    
    @Test
    public void testLoadFile8() throws InterruptedException, IOException{
        System.out.println("* JFileManagerLoadTest: test method 8 - testLoadFile8()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String path = "./src/jcd/test_bed/testLoad3.json";
                            JsonObject returnObj = JFileManager.loadFile(path);
                            JsonObject classes = ((JsonObject)((JsonObject)((JsonArray)returnObj.get("classes")).get(0)).get(String.valueOf("0")));
                            JsonObject temp = ((JsonObject)((JsonObject)((JsonArray)classes.get("class")).get(1)).get("1"));
                            temp = ((JsonObject)((JsonObject)((JsonArray)temp.get("variables")).get(2)).get(String.valueOf(2)));

                            String access = temp.get("access").toString();
                            access = access.substring(1, access.length()-1);
        
                            String result = access;
                            assertEquals("public", result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerLoadTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }
    
     /**
     * Test of loadFile method, of class JFileManager.
     */    
    @Test
    public void testLoadFile9() throws InterruptedException, IOException{
        System.out.println("* JFileManagerLoadTest: test method 9 - testLoadFile9()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String path = "./src/jcd/test_bed/testLoad3.json";
                            JsonObject returnObj = JFileManager.loadFile(path);
                            JsonObject classes = ((JsonObject)((JsonObject)((JsonArray)returnObj.get("classes")).get(0)).get(String.valueOf("0")));
                            JsonObject temp = ((JsonObject)((JsonObject)((JsonArray)classes.get("class")).get(1)).get("1"));
                            temp = ((JsonObject)((JsonObject)((JsonArray)temp.get("variables")).get(2)).get(String.valueOf(2)));

                            String type = temp.get("type").toString();
                            type = type.substring(1, type.length()-1);
        
                            String result = type;
                            assertEquals("long", result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerLoadTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }
    
     /**
     * Test of loadFile method, of class JFileManager.
     */    
    @Test
    public void testLoadFile10() throws InterruptedException, IOException{
        System.out.println("* JFileManagerLoadTest: test method 4 - testLoadFile4()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String path = "./src/jcd/test_bed/testLoad3.json";
                            JsonObject returnObj = JFileManager.loadFile(path);
                            JsonObject classes = ((JsonObject)((JsonObject)((JsonArray)returnObj.get("classes")).get(0)).get(String.valueOf("0")));
                            JsonObject temp = ((JsonObject)((JsonObject)((JsonArray)classes.get("class")).get(1)).get("1"));
                            temp = ((JsonObject)((JsonObject)((JsonArray)temp.get("variables")).get(1)).get(String.valueOf(1)));

                            String type = temp.get("type").toString();
                            type = type.substring(1, type.length()-1);
        
                            String result = type;
                            assertEquals("int", result);
                        } catch (IOException ex) {
                            Logger.getLogger(JFileManagerLoadTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        thread.start();
    }

}
