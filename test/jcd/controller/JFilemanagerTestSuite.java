package jcd.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author YinqianZheng
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({jcd.controller.JFileManagerSaveTest.class, jcd.controller.JFileManagerLoadTest.class})
public class JFilemanagerTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
}
