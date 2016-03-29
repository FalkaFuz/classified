package sk.zuzmat.classified.backend;

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
 * Created by Mat� on 14. 3. 2016.
 */
public class MissionManagerImplTest {

    private MissionManagerImpl manager;
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
        DBUtils.executeSqlScript(ds,MissionManager.class.getResource("/createtables.sql"));
        manager = new MissionManagerImpl();
        manager.setDataSource(ds);
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds,MissionManager.class.getResource("/droptables.sql"));
    }


    @Test
    public void createMission() {
        Mission mission = newMission("Bora", "piesocnata plaz");
        manager.createMission(mission);

        Long missionId = mission.getId();

        assertThat(mission.getId(), is(not(equalTo(null))));

        Mission result = manager.findMissionById(missionId);

        assertThat(result, is(equalTo(mission)));
        assertThat(result, is(not(sameInstance(mission))));

        assertDeepEquals(mission, result);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNull() throws Exception {
        manager.createMission(null);
    }


    @Test
    public void createMissionWithNullLocation() {
        Mission mission;
        mission = newMission(null, "palma");
        try {
            manager.createMission(mission);
            fail("location can not be null");
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }
    @Test
    public void createMissionWithEmptyLocation() {
        Mission mission;
        mission = newMission("", "palma");
        try {
            manager.createMission(mission);
            fail("location can not be empty");
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }
    @Test
    public void createMissionWithNullCodename() {
        Mission mission;
        mission = newMission("Bora", null);
        try {
            manager.createMission(mission);
            fail("codeName can not be null");
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }
    @Test
    public void createMissionWithEmptyCodename() {
        Mission mission;
        mission = newMission("Bora", "");
        try {
            manager.createMission(mission);
            fail("codeName can not be empty");
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }


    @Test
    public void findAllMissions() {

        assertTrue(manager.findAllMissions().isEmpty());

        Mission g1 = newMission("Brno", "macka");
        Mission g2 = newMission("Praha", "pes");

        manager.createMission(g1);
        manager.createMission(g2);

        List<Mission> expected = Arrays.asList(g1, g2);
        List<Mission> actual = manager.findAllMissions();

        Collections.sort(actual, idComparator);
        Collections.sort(expected, idComparator);

        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }



    @Test
    public void updateMission() {
        Mission mission = newMission("Malyp", "smrt");
        Mission m2 = newMission("Ukrajina", "jelen");

        manager.createMission(mission);
        manager.createMission(m2);

        Long missionId = mission.getId();

        mission.setLocation("jam");
        manager.updateMission(mission);
        mission = manager.findMissionById(missionId);
        assertEquals("jam", mission.getLocation());
        assertEquals("smrt", mission.getCodeName());

        mission.setCodeName("la");
        manager.updateMission(mission);
        mission = manager.findMissionById(missionId);
        assertEquals("jam", mission.getLocation());
        assertEquals("la", mission.getCodeName());

        assertDeepEquals(m2, manager.findMissionById(m2.getId()));
    }

/*
    @Test
    public void updateMissionWithWrongAttributes() {

        Mission mission = newMission("Afrika", "kebab");
        manager.createMission(mission);
        Long missionId = mission.getId();

        try {
            manager.updateMission(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            mission = manager.findMissionById(missionId);
            mission.setId(null);
            manager.updateMission(mission);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            mission = manager.findMissionById(missionId);
            mission.setId(missionId - 1);
            manager.updateMission(mission);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            mission = manager.findMissionById(missionId);
            mission.setLocation(null);
            manager.updateMission(mission);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            mission = manager.findMissionById(missionId);
            mission.setLocation("");
            manager.updateMission(mission);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            mission = manager.findMissionById(missionId);
            mission.setCodeName(null);
            manager.updateMission(mission);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            mission = manager.findMissionById(missionId);
            mission.setCodeName("");
            manager.updateMission(mission);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }
    */


    @Test
    public void deleteMission() {

        Mission m1 = newMission("Kreta", "vlocka");
        Mission m2 = newMission("Bulharsko", "kleopatra");
        manager.createMission(m1);
        manager.createMission(m2);

        assertNotNull(manager.findMissionById(m1.getId()));
        assertNotNull(manager.findMissionById(m2.getId()));

        manager.deleteMission(m1);

        assertNull(manager.findMissionById(m1.getId()));
        assertNotNull(manager.findMissionById(m2.getId()));

    }
    /*
    @Test
    public void deleteMissionWithWrongAttributes() {

        Mission mission = newMission("Korea", "lakatos");

        try {
            manager.deleteMission(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            mission.setId(null);
            manager.deleteMission(mission);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            mission.setId(13L);
            manager.deleteMission(mission);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

    }

*/
    private static Mission newMission(String location, String codename) {
        Mission mission = new Mission();
        mission.setLocation(location);
        mission.setCodeName(codename);

        return mission;
    }

    private void assertDeepEquals(List<Mission> expectedList, List<Mission> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Mission expected = expectedList.get(i);
            Mission actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

    private void assertDeepEquals(Mission expected, Mission actual) {
        assertEquals("id value is not equal",expected.getId(), actual.getId());
        assertEquals("location value is not equal",expected.getLocation(), actual.getLocation());
        assertEquals("codeName value is not equal",expected.getCodeName(), actual.getCodeName());

    }

    private static Comparator<Mission> idComparator = new Comparator<Mission>() {
        @Override
        public int compare(Mission o1, Mission o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };
}