/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.zuzmat.classified.backend;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sk.zuzmat.classified.common.DBUtils;
import sk.zuzmat.classified.common.IllegalEntityException;
import sk.zuzmat.classified.common.ServiceFailureException;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


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
        agentNotInDB = new AgentBuilder().name("Agent not in DB").id(a5.getId() + 100).build();
        assertThat(agentManager.findAgentById(agentNotInDB.getId())).isNull();
    }


    @Test
    public void getAssignedAgents() {

        assertThat(manager.getAssignedAgents(m1)).isEmpty();
        assertThat(manager.getAssignedAgents(m2)).isEmpty();
        assertThat(manager.getAssignedAgents(m3)).isEmpty();

        manager.assignAgentToMission(a2, m3);
        manager.assignAgentToMission(a3, m2);
        manager.assignAgentToMission(a4, m3);
        manager.assignAgentToMission(a5, m2);

        assertThat(manager.getAssignedAgents(m1))
                .isEmpty();
        assertThat(manager.getAssignedAgents(m2))
                .usingFieldByFieldElementComparator()
                .containsOnly(a3,a5);
        assertThat(manager.getAssignedAgents(m3))
                .usingFieldByFieldElementComparator()
                .containsOnly(a2,a4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAssignedAgentsOnNullMission() {
        manager.getAssignedAgents(null);
    }

    @Test(expected = IllegalEntityException.class)
    public void getAssignedAgentsOnMissionHavingNullId() {
        manager.getAssignedAgents(missionWithNullId);
    }


    @Test
    public void assignAgentToMission() {

        assertThat(manager.getAssignedMission(a1)).isNull();
        assertThat(manager.getAssignedMission(a2)).isNull();
        assertThat(manager.getAssignedMission(a3)).isNull();
        assertThat(manager.getAssignedMission(a4)).isNull();
        assertThat(manager.getAssignedMission(a5)).isNull();

        manager.assignAgentToMission(a1, m3);
        manager.assignAgentToMission(a5, m1);
        manager.assignAgentToMission(a3, m3);

        assertThat(manager.getAssignedAgents(m1))
                .usingFieldByFieldElementComparator()
                .containsOnly(a5);
        assertThat(manager.getAssignedAgents(m2))
                .isEmpty();
        assertThat(manager.getAssignedAgents(m3))
                .usingFieldByFieldElementComparator()
                .containsOnly(a1,a3);

        assertThat(manager.getAssignedMission(a1))
                .isEqualToComparingFieldByField(m3);
        assertThat(manager.getAssignedMission(a2))
                .isNull();
        assertThat(manager.getAssignedMission(a3))
                .isEqualToComparingFieldByField(m3);
        assertThat(manager.getAssignedMission(a4))
                .isNull();
        assertThat(manager.getAssignedMission(a5))
                .isEqualToComparingFieldByField(m1);
    }

    @Test
    public void assignAgentOnMissionMultipleTime() {

        manager.assignAgentToMission(a1, m3);
        manager.assignAgentToMission(a5, m1);
        manager.assignAgentToMission(a3, m3);

        assertThatThrownBy(() -> manager.assignAgentToMission(a1, m3))
                .isInstanceOf(IllegalEntityException.class);

        // verify that failure was atomic and no data was changed
        assertThat(manager.getAssignedAgents(m1))
                .usingFieldByFieldElementComparator()
                .containsOnly(a5);
        assertThat(manager.getAssignedAgents(m2))
                .isEmpty();
        assertThat(manager.getAssignedAgents(m3))
                .usingFieldByFieldElementComparator()
                .containsOnly(a1,a3);
    }

    @Test
    public void  assignAgentOnMultipleMissions() {

        manager.assignAgentToMission(a1, m3);
        manager.assignAgentToMission(a5, m1);
        manager.assignAgentToMission(a3, m3);

        assertThatThrownBy(() -> manager.assignAgentToMission(a1, m2))
                .isInstanceOf(IllegalEntityException.class);

        // verify that failure was atomic and no data was changed
        assertThat(manager.getAssignedAgents(m1))
                .usingFieldByFieldElementComparator()
                .containsOnly(a5);
        assertThat(manager.getAssignedAgents(m2))
                .isEmpty();
        assertThat(manager.getAssignedAgents(m3))
                .usingFieldByFieldElementComparator()
                .containsOnly(a1,a3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignNullAgentToMission() {
        manager.assignAgentToMission(null, m2);
    }

    @Test(expected = IllegalEntityException.class)
    public void assignAgentWithNullIdToMission() {
        manager.assignAgentToMission(agentWithNullId, m2);
    }

    @Test(expected = IllegalEntityException.class)
    public void assignAgentNotInDBToMission() {
        manager.assignAgentToMission(agentNotInDB, m2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignAgentToNullMission() {
        manager.assignAgentToMission(a2, null);
    }

    @Test(expected = IllegalEntityException.class)
    public void assignAgentToMissionWithNullId() {
        manager.assignAgentToMission(a2, missionWithNullId);
    }


    @Test(expected = IllegalEntityException.class)
    public void assignAgentToMissionNotInDB() { manager.assignAgentToMission(a2, missionNotInDB); }

    @Test
    public void removeAgentFromMission() {


        manager.assignAgentToMission(a1, m3);
        manager.assignAgentToMission(a3, m3);
        manager.assignAgentToMission(a4, m3);
        manager.assignAgentToMission(a5, m1);

        assertThat(manager.getAssignedMission(a1))
                .isEqualToComparingFieldByField(m3);
        assertThat(manager.getAssignedMission(a2))
                .isNull();
        assertThat(manager.getAssignedMission(a3))
                .isEqualToComparingFieldByField(m3);
        assertThat(manager.getAssignedMission(a5))
                .isEqualToComparingFieldByField(m1);

        manager.removeAgentFromMission(a3, m3);

        assertThat(manager.getAssignedAgents(m1))
                .usingFieldByFieldElementComparator()
                .containsOnly(a5);
        assertThat(manager.getAssignedAgents(m2))
                .isEmpty();
        assertThat(manager.getAssignedAgents(m3))
                .usingFieldByFieldElementComparator()
                .containsOnly(a1,a4);


        assertThat(manager.getAssignedMission(a1))
                .isEqualToComparingFieldByField(m3);
        assertThat(manager.getAssignedMission(a2))
                .isNull();
        assertThat(manager.getAssignedMission(a3))
                .isNull();
        assertThat(manager.getAssignedMission(a4))
                .isEqualToComparingFieldByField(m3);
        assertThat(manager.getAssignedMission(a5))
                .isEqualToComparingFieldByField(m1);
    }


    @Test
    public void removeAgentFromMissionWhereIsNotAssigned() {

        manager.assignAgentToMission(a1, m3);
        manager.assignAgentToMission(a4, m3);
        manager.assignAgentToMission(a5, m1);

        assertThatThrownBy(() -> manager.assignAgentToMission(a1, m1))
                .isInstanceOf(IllegalEntityException.class);

        // Check that previous tests didn't affect data in database
        assertThat(manager.getAssignedAgents(m1))
                .usingFieldByFieldElementComparator()
                .containsOnly(a5);
        assertThat(manager.getAssignedAgents(m2))
                .isEmpty();
        assertThat(manager.getAssignedAgents(m3))
                .usingFieldByFieldElementComparator()
                .containsOnly(a1,a4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullAgentFromMission() {
        manager.removeAgentFromMission(null, m2);
    }

    @Test(expected = IllegalEntityException.class)
    public void removeAgentWithNullIdFromMission() {
        manager.removeAgentFromMission(agentWithNullId, m2);
    }

    @Test(expected = IllegalEntityException.class)
    public void removeAgentNotInDBFromMission() {
        manager.removeAgentFromMission(agentNotInDB, m2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeAgentFromNullMission() {
        manager.removeAgentFromMission(a2, null);
    }

    @Test(expected = IllegalEntityException.class)
    public void removeAgentFromMissionWithNullId() {
        manager.removeAgentFromMission(a2, missionWithNullId);
    }

    @Test(expected = IllegalEntityException.class)
    public void removeAgentFromMissionNotInDB() {
        manager.removeAgentFromMission(a2, missionNotInDB);
    }






    @FunctionalInterface
    private static interface Operation<T> {
        void callOn(T subjectOfOperation);
    }

    private void testExpectedServiceFailureException(Operation<MissionControlManager> operation) throws SQLException {
        SQLException sqlException = new SQLException();
        DataSource failingDataSource = mock(DataSource.class);
        when(failingDataSource.getConnection()).thenThrow(sqlException);
        manager.setDataSource(failingDataSource);
        assertThatThrownBy(() -> operation.callOn(manager))
                .isInstanceOf(ServiceFailureException.class)
                .hasCause(sqlException);
    }
    @Test
    public void getAssignedAgentsWithSqlExceptionThrown() throws SQLException {
        testExpectedServiceFailureException((missionControlManager) -> missionControlManager.getAssignedAgents(m1));
    }

    @Test
    public void getAssignedMissionWithSqlExceptionThrown() throws SQLException {
        testExpectedServiceFailureException((missionControlManager) -> missionControlManager.getAssignedMission(a1));
    }

    @Test
    public void assignAgentToMissionWithSqlExceptionThrown() throws SQLException {
        testExpectedServiceFailureException((missionControlManager) -> missionControlManager.assignAgentToMission(a1, m1));
    }

    @Test
    public void removeAgentFromMissionWithSqlExceptionThrown() throws SQLException {
        testExpectedServiceFailureException((missionControlManager) -> missionControlManager.removeAgentFromMission(a1, m1));
    }
}
