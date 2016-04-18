package sk.zuzmat.classified.webapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sk.zuzmat.classified.backend.Mission;
import sk.zuzmat.classified.backend.MissionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Falka on 18. 4. 2016.
 */

@WebServlet(name = "Missions", urlPatterns = "/missions/*")
public class MissionServlet extends HttpServlet
{
    private static final Logger log = LogManager.getLogger(MissionServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        log.info("Requested url {}", req.getPathInfo());

        String action = req.getPathInfo();
        switch (action)
        {
            case "/":
                listAll(req);
                defaultAction(req, resp);
                break;
            case "/edit":
                Mission mission = getMm(req).findMissionById(Long.valueOf(req.getParameter("id")));
                req.setAttribute("mission",mission);
                req.getRequestDispatcher("/pages/mission.edit.jsp").forward(req,resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Unknown action: " + action);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        log.info("Inside post {}", req.getPathInfo());

        String action = req.getPathInfo();
        switch (action)
        {
            case "/add-mission":
                addMission(req);
                break;
            case "/delete-missions":
                deleteMissions(req);
                break;
            case "/submit-edit":
                editMission(req);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Unknown action: " + action);
        }

        //defaultAction(req, resp);
        resp.sendRedirect("/classified/missions/");
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
        req.setAttribute("missionList", getMm(req).findAllMissions());
        req.getRequestDispatcher("/pages/mission.list.jsp").forward(req, resp);
    }

    private void listAll(HttpServletRequest req)
    {
        req.setAttribute("missionList", getMm(req).findAllMissions());
    }

    private void deleteMissions(HttpServletRequest req)
    {
        MissionManager mm = getMm(req);
        for(String id : req.getParameterValues("id"))
        {
            Mission m = new Mission();
            m.setId(Long.valueOf(id));

            mm.deleteMission(m);
        }
    }


    private void editMission(HttpServletRequest req)
    {
        Mission m = new Mission();
        m.setCodeName(req.getParameter("name"));
        m.setLocation(req.getParameter("location"));
        m.setId(Long.valueOf(req.getParameter("id")));
        log.info("Mission with id {} is going to be changed",m.getId());

        getMm(req).updateMission(m);
    }

    private void addMission(HttpServletRequest req)
    {
        Mission m = new Mission();
        m.setCodeName(req.getParameter("name"));
        m.setLocation(req.getParameter("location"));

        log.info("About to persist mission {} .", m);

        getMm(req).createMission(m);
    }

    private MissionManager getMm(HttpServletRequest req)
    {
        return (MissionManager) req.getServletContext().getAttribute("missionManager");
    }

}
