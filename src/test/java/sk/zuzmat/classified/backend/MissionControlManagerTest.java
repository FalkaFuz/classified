/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classified;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Falka
 */
public class MissionControlManagerTest {
    
    public MissionControlManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of assignAgentToMission method, of class MissionControlManager.
     */
    @Test
    public void testAssignAgentToMission() {
        System.out.println("assignAgentToMission");
        Agent agent = null;
        Mission mission = null;
        MissionControlManager instance = new MissionControlManagerImpl();
        instance.assignAgentToMission(agent, mission);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeAgentFromMission method, of class MissionControlManager.
     */
    @Test
    public void testRemoveAgentFromMission() {
        System.out.println("removeAgentFromMission");
        Agent agent = null;
        Mission mission = null;
        MissionControlManager instance = new MissionControlManagerImpl();
        instance.removeAgentFromMission(agent, mission);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAssignedAgents method, of class MissionControlManager.
     */
    @Test
    public void testGetAssignedAgents() {
        System.out.println("getAssignedAgents");
        Mission mission = null;
        MissionControlManager instance = new MissionControlManagerImpl();
        List<Agent> expResult = null;
        List<Agent> result = instance.getAssignedAgents(mission);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAssignedMission method, of class MissionControlManager.
     */
    @Test
    public void testGetAssignedMission() {
        System.out.println("getAssignedMission");
        Agent agent = null;
        MissionControlManager instance = new MissionControlManagerImpl();
        Mission expResult = null;
        Mission result = instance.getAssignedMission(agent);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class MissionControlManagerImpl implements MissionControlManager {

        public void assignAgentToMission(Agent agent, Mission mission) {
        }

        public void removeAgentFromMission(Agent agent, Mission mission) {
        }

        public List<Agent> getAssignedAgents(Mission mission) {
            return null;
        }

        public Mission getAssignedMission(Agent agent) {
            return null;
        }
    }
    
}
