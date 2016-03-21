/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classified;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.sql.DataSource;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author xschwar2
 */
public class AgentManagerImplTest {
    
    private AgentManagerImpl manager;
    private DataSource dataSource;

    @Rule
    // attribute annotated with @Rule annotation must be public :-(
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws SQLException {
        dataSource = prepareDataSource();
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("CREATE TABLE AGENT ("
                    + "id bigint primary key generated always as identity,"
                    + "name varchar(50) not null,"
                    + "covername varchar(50) not null,"
                    + "weapon varchar(50) not null)").executeUpdate();
        }
        manager = new AgentManagerImpl(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("DROP TABLE AGENT").executeUpdate();
        }
    }

    
    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        //we will use in memory database
        ds.setDatabaseName("memory:agentmgr-test");
        ds.setCreateDatabase("create");
        return ds;
    }
  


    @Test
    public void testCreateAgent(){
        Agent agent = newAgent("Hanamiki", "Black Fish", "katana");
        manager.createAgent(agent);

        Long agentId = agent.getId();
        
        assertThat(agent.getId(), is(not(equalTo(null))));

        Agent resultAgent = manager.findAgentById(agentId);
        
        assertThat(resultAgent, is(equalTo(agent)));
        assertThat(resultAgent, is(not(sameInstance(agent))));

        assertDeepEquals(agent, resultAgent);
        
    }
    
    
    @Test
    public void testUpdateAgent(){
        Agent agent1 = newAgent("Maly Pete", "Zahradnik smrti", "hrable");
        Agent agent2 = newAgent("Axel", "Lexa", "grenade launcher");
       
        
        manager.createAgent(agent1);
        manager.createAgent(agent2);

        Long agent1Id = agent1.getId();

        
        agent1.setName("James");
        manager.updateAgent(agent1);
        agent1 = manager.findAgentById(agent1Id);
        assertThat(agent1.getName(), is(equalTo("James")));
        assertThat(agent1.getCoverName(), is(equalTo("Zahradnik smrti")));
        assertThat(agent1.getFavouriteWeapon(), is(equalTo("hrable")));

        agent1.setCoverName("007");
        manager.updateAgent(agent1);
        agent1 = manager.findAgentById(agent1Id);
        assertThat(agent1.getName(), is(equalTo("James")));
        assertThat(agent1.getCoverName(), is(equalTo("007")));
        assertThat(agent1.getFavouriteWeapon(), is(equalTo("hrable")));
        

        agent1.setFavouriteWeapon("glock");
        manager.updateAgent(agent1);
        agent1 = manager.findAgentById(agent1Id);
        assertThat(agent1.getName(), is(equalTo("James")));
        assertThat(agent1.getCoverName(), is(equalTo("007")));
        assertThat(agent1.getFavouriteWeapon(), is(equalTo("glock")));

        assertDeepEquals(agent2, manager.findAgentById(agent2.getId()));
    }
    
    
    
     @Test
    public void testDeleteAgent(){

        Agent agent1 = newAgent("Hanamiki", "Black Fish", "katana");
        Agent agent2 = newAgent("John", "Snake", "pistol");
        
        manager.createAgent(agent1);
        manager.createAgent(agent2);

        assertNotNull(manager.findAgentById(agent1.getId()));
        assertNotNull(manager.findAgentById(agent2.getId()));

        manager.deleteAgent(agent1);

        assertNull(manager.findAgentById(agent1.getId()));
        assertNotNull(manager.findAgentById(agent2.getId()));

    }

    
    
     @Test
    public void testCreateAgentWithWrongName(){
        Agent agent;
        agent = newAgent("", "Dark Deer", "desert eagle");
        try {
            manager.createAgent(agent);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }
    
     @Test
    public void testCreateAgentWithNullName(){
        Agent agent;
        agent = newAgent(null, "Dark Deer", "desert eagle");
        try {
            manager.createAgent(agent);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }
    
     @Test
    public void testCreateAgentWithWrongCoverName(){
        Agent agent;
        agent = newAgent("Jeremy", "", "zweihander");
        try {
            manager.createAgent(agent);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }
     @Test
    public void testCreateAgentWithNullCoverName(){
        Agent agent;
        agent = newAgent("Jeremy", null, "zweihander");
        try {
            manager.createAgent(agent);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }
    
     @Test
    public void testCreateAgentWithWrongFavouriteWeapon(){
        Agent agent;
        agent = newAgent("Jacob", "Clown", "");
        try {
            manager.createAgent(agent);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }
    
     @Test
    public void testCreateAgentWitNullFavouriteWeapon(){
        Agent agent;
        agent = newAgent("Jacob", "Clown", null);
        try {
            manager.createAgent(agent);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }
    
     @Test
    public void testCreateAgentWithSameNameAndCoverName(){
        Agent agent;
        agent = newAgent("Axel", "Axel", "grenade launcher");
        try {
            manager.createAgent(agent);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    
    @Test
    public void testFindAgentById(){
    
        assertNull(manager.findAgentById(1098l));

        Agent agent = newAgent("Maly Pete", "Zahradnik smrti", "hrable");
        manager.createAgent(agent);
        Long agentId = agent.getId();

        Agent resultAgent = manager.findAgentById(agentId);
        assertEquals(agent, resultAgent);
        assertDeepEquals(agent, resultAgent);
    }
    
    @Test
    public void testFindAllAgents(){
        assertTrue(manager.findAllAgents().isEmpty());

        Agent agent1 = newAgent("Hanamiki", "Black Fish", "katana");
        Agent agent2 = newAgent("John", "Snake", "pistol");
        Agent agent3 = newAgent("Maly Pete", "Zahradnik smrti", "hrable");

        manager.createAgent(agent1);
        manager.createAgent(agent2);
        manager.createAgent(agent3);

        List<Agent> expected = Arrays.asList(agent1, agent2, agent3);
        List<Agent> actual = manager.findAllAgents();

        Collections.sort(actual, idComparator);
        Collections.sort(expected, idComparator);

        assertEquals(expected, actual);
        assertCollectionDeepEquals(expected, actual);
    }
    
    private static Agent newAgent(String name, String coverName, String favouriteWeapon) {
        Agent agent = new Agent();
        agent.setName(name);
        agent.setCoverName(coverName);
        agent.setFavouriteWeapon(favouriteWeapon);
        return agent;
    }
    
    protected void assertDeepEquals(Agent expected, Agent actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCoverName(), actual.getCoverName());
        assertEquals(expected.getFavouriteWeapon(), actual.getFavouriteWeapon());
    }
    
    protected void assertCollectionDeepEquals(List<Agent> expectedList, List<Agent> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Agent expected = expectedList.get(i);
            Agent actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }
    
    private static Comparator<Agent> idComparator = new Comparator<Agent>() {
        @Override
        public int compare(Agent o1, Agent o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };
   
    
    
    
    
    public AgentManagerImplTest() {
    }
    
    
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
      
    

    
   

}