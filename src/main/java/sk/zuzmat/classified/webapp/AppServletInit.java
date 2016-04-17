package sk.zuzmat.classified.webapp;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sk.zuzmat.classified.backend.Agent;
import sk.zuzmat.classified.backend.AgentManagerImpl;
import sk.zuzmat.classified.common.DBUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Dominik Szalai - emptulik at gmail.com on 16.04.2016.
 */
@WebListener
public class AppServletInit implements ServletContextListener
{
    private static final Logger log = LogManager.getLogger(AppServletInit.class);
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        log.info("Started");
        AgentManagerImpl manager = null;
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


        manager = new AgentManagerImpl();
        manager.setDataSource(ds);
        log.info("Manager {}",manager);
        log.info("DS {}",ds);
        sce.getServletContext().setAttribute("agentManager",manager);
        log.debug("manager set");

        Agent a = new Agent();
        a.setCoverName("Pista");
        a.setName("Stefan");
        a.setFavouriteWeapon("Fists");
        manager.createAgent(a);

        Agent b = new Agent();
        b.setCoverName("Vladimir");
        b.setName("Ilja");
        b.setFavouriteWeapon("AK47");

        manager.createAgent(b);
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
