/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.zuzmat.classified.backend;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;


/**
 *
 * @author Falka
 */
public class MissionControlManagerImplTest {

    public MissionControlManagerImplTest() {
    }

    private MissionControlManagerImpl manager;
    private AgentManagerImpl agentManager;
    private MissionManagerImpl missionManager;
    private DataSource ds;


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:agentmgr-test");
        ds.setCreateDatabase("create");
        return ds;
    }

    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds, MissionManager.class.getResource("/createtables.sql"));
        manager = new MissionControlManagerImpl();
        manager.setDataSource(ds);
        agentManager = new AgentManagerImpl();
        agentManager.setDataSource(ds);
        missionManager = new MissionManagerImpl();
        missionManager.setDataSource(ds);
        prepareTestData();
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds, MissionManager.class.getResource("/droptables.sql"));
    }

    private Mission m1, m2, m3, missionWithNullId, missionNotInDB;
    private Agent a1, a2, a3, a4, a5, agentWithNullId, agentNotInDB;

    private void prepareTestData(){

        m1 = new MissionBuilder().location("Brno 1").codeName("Mission 1").build();
        m2 = new MissionBuilder().location("Brno 2").codeName("Mission 2").build();
        m3 = new MissionBuilder().location("Brno 3").codeName("Mission 3").build();

        a1 = new AgentBuilder().name("Agent 1").coverName("Cover 1").favouriteWeapon("Knife 1").build();
        a2 = new AgentBuilder().name("Agent 2").coverName("Cover 2").favouriteWeapon("Knife 2").build();
        a3 = new AgentBuilder().name("Agent 3").coverName("Cover 3").favouriteWeapon("Knife 3").build();
        a4 = new AgentBuilder().name("Agent 4").coverName("Cover 4").favouriteWeapon("Knife 4").build();
        a5 = new AgentBuilder().name("Agent 5").coverName("Cover 5").favouriteWeapon("Knife 5").build();

        agentManager.createAgent(a1);
        agentManager.createAgent(a2);
        agentManager.createAgent(a3);
        agentManager.createAgent(a4);
        agentManager.createAgent(a5);

        missionManager.createMission(m1);
        missionManager.createMission(m2);
        missionManager.createMission(m3);

        missionWithNullId = new MissionBuilder().id(null).build();
        missionNotInDB = new MissionBuilder().id(m3.getId() + 100).build();
        assertThat(missionManager.findMissionById(missionNotInDB.getId())).isNull();

        agentWithNullId = new AgentBuilder().name("Agent with null id").id(null).build();
        agentNotInDB = new AgentBuilder().name("Body not in DB").id(a5.getId() + 100).build();
        assertThat(agentManager.findAgentById(agentNotInDB.getId())).isNull();
    }

}
