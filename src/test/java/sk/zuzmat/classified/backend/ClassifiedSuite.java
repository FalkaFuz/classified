/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classified;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Falka
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({classified.MissionControlManagerImplTest.class, classified.ClassifiedTest.class, classified.MissionManagerTest.class, classified.MissionManagerImplTest.class, classified.MissionControlManagerTest.class, classified.AgentTest.class, classified.AgentManagerTest.class, classified.AgentManagerImplTest.class, classified.MissionTest.class})
public class ClassifiedSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
