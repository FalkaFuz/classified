package sk.zuzmat.classified.webapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sk.zuzmat.classified.backend.Agent;
import sk.zuzmat.classified.backend.AgentManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Dominik Szalai - emptulik at gmail.com on 16.04.2016.
 */
@WebServlet(urlPatterns = "/agents/*")
public class AgentServlet extends HttpServlet
{
    private static final Logger log = LogManager.getLogger(AgentServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        log.info("Requested url {}", req.getPathInfo());
        switch (req.getPathInfo())
        {
            case "/":
                listAll(req);
                defaultAction(req, resp);
                break;
            case "/edit":
                Agent agent = getAm(req).findAgentById(Long.valueOf(req.getParameter("id")));
                req.setAttribute("agent",agent);
                req.getRequestDispatcher("/pages/agent.edit.jsp").forward(req,resp);
                break;
            default:
                throw new IllegalArgumentException("huehue");
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        log.info("Inside post {}", req.getPathInfo());
        switch (req.getPathInfo())
        {
            case "/add-agent":
                addAgent(req);
                break;
            case "/delete-agents":
                deleteAgents(req);
            break;
            case "/submit-edit":
                editAgent(req);
                break;
            default:
                throw new IllegalArgumentException("huehue");
        }

        defaultAction(req, resp);
    }

    /**
     * TODO REDIRECT
     *
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void defaultAction(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        req.setAttribute("agentList", getAm(req).findAllAgents());
        req.getRequestDispatcher("/pages/agent.list.jsp").forward(req, resp);
    }

    private void listAll(HttpServletRequest req)
    {
        req.setAttribute("agentList", getAm(req).findAllAgents());
    }

    private void deleteAgents(HttpServletRequest req)
    {
        AgentManager am = getAm(req);
        for(String id : req.getParameterValues("id"))
        {
            Agent a = new Agent();
            a.setId(Long.valueOf(id));

            am.deleteAgent(a);
        }
    }

    private void editGet(HttpServletRequest req)
    {

    }

    private void editAgent(HttpServletRequest req)
    {
        Agent a = new Agent();
        a.setName(req.getParameter("name"));
        a.setCoverName(req.getParameter("coverName"));
        a.setFavouriteWeapon(req.getParameter("favWeapon"));
        a.setId(Long.valueOf(req.getParameter("id")));
        log.info("Agent with id {} is going to be changed",a.getId());

        getAm(req).updateAgent(a);
    }

    private void addAgent(HttpServletRequest req)
    {
        Agent a = new Agent();
        a.setName(req.getParameter("name"));
        a.setCoverName(req.getParameter("coverName"));
        a.setFavouriteWeapon(req.getParameter("favWeapon"));

        log.info("About to persist agent {} .", a);

        getAm(req).createAgent(a);
    }

    private AgentManager getAm(HttpServletRequest req)
    {
        return (AgentManager) req.getServletContext().getAttribute("agentManager");
    }


}
