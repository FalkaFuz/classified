package sk.zuzmat.classified.webapp;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sk.zuzmat.classified.backend.Agent;
import sk.zuzmat.classified.backend.AgentManagerImpl;
import sk.zuzmat.classified.backend.Mission;
import sk.zuzmat.classified.backend.MissionManagerImpl;
import sk.zuzmat.classified.common.DBUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Falka
 */
@WebListener
public class AppServletInit implements ServletContextListener
{
    private static final Logger log = LogManager.getLogger(AppServletInit.class);
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        log.info("Started");
        AgentManagerImpl agentManager;
        MissionManagerImpl missionManager;
        DataSource ds = null;
        try
        {
            ds = prepareDataSource();
            DBUtils.executeSqlScript(ds,AppServletInit.class.getResource("/createtables.sql"));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }


        agentManager = new AgentManagerImpl();
        agentManager.setDataSource(ds);
        log.info("Manager {}",agentManager);
        log.info("DS {}",ds);
        sce.getServletContext().setAttribute("agentManager",agentManager);
        log.debug("agentManager set");

        Agent a = new Agent();
        a.setCoverName("Pista");
        a.setName("Stefan");
        a.setFavouriteWeapon("Fists");

        agentManager.createAgent(a);

        Agent b = new Agent();
        b.setCoverName("Vladimir");
        b.setName("Ilja");
        b.setFavouriteWeapon("AK47");

        agentManager.createAgent(b);

        //zuz hackuje

        missionManager = new MissionManagerImpl();
        missionManager.setDataSource(ds);
        log.info("Manager {}",missionManager);
        log.info("DS {}",ds);
        sce.getServletContext().setAttribute("missionManager",missionManager);
        log.debug("missionManager set");

        Mission m = new Mission();
        m.setCodeName("MaRaKuJa");
        m.setLocation("Bahamas");

        missionManager.createMission(m);

        Mission n = new Mission();
        n.setCodeName("zelenina");
        n.setLocation("New Zeland");

        missionManager.createMission(n);
        //koniec hackovania





    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {

    }

    private DataSource prepareDataSource() throws SQLException
    {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:agentmgr-test");
        ds.setCreateDatabase("create");
        return ds;
    }
}
