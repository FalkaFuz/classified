/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.zuzmat.classified.backend;

import sk.zuzmat.classified.common.DBUtils;
import sk.zuzmat.classified.common.IllegalEntityException;
import sk.zuzmat.classified.common.ServiceFailureException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.xml.crypto.Data;

/**
 *
 * @author Falka
 */
public class MissionControlManagerImpl implements MissionControlManager{

    private static final Logger logger = Logger.getLogger(
            MissionManagerImpl.class.getName());
    ///THIS

    public MissionControlManagerImpl(DataSource source) {
        this.dataSource = source;
    }

    public MissionControlManagerImpl() {
    }

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }


    @Override
    public void assignAgentToMission(Agent agent, Mission mission) throws ServiceFailureException, IllegalEntityException {
        checkDataSource();


        if (mission == null) {
            throw new IllegalArgumentException("mission is null");
        }
        if (mission.getId() == null) {
            throw new IllegalEntityException("mission id is null");
        }
        if (agent == null) {
            throw new IllegalArgumentException("agent is null");
        }
        if (agent.getId() == null) {
            throw new IllegalEntityException("agent id is null");
        }
        Connection conn = null;
        PreparedStatement updateSt = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            updateSt = conn.prepareStatement(
                    "UPDATE Agent SET missionId = ? WHERE id = ? AND missionId IS NULL");
            updateSt.setLong(1, mission.getId());
            updateSt.setLong(2, agent.getId());
            int count = updateSt.executeUpdate();
            if (count == 0) {
                throw new IllegalEntityException("Agent " + agent + " not found or it is already placed in some mission");
            }
            DBUtils.checkUpdatesCount(count, agent, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when assigning agent to mission";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, updateSt);
        }
    }

    @Override
    public void removeAgentFromMission(Agent agent, Mission mission) throws ServiceFailureException, IllegalEntityException {
        checkDataSource();
        if (mission == null) {
            throw new IllegalArgumentException("mission is null");
        }
        if (mission.getId() == null) {
            throw new IllegalEntityException("mission id is null");
        }
        if (agent == null) {
            throw new IllegalArgumentException("agent is null");
        }
        if (agent.getId() == null) {
            throw new IllegalEntityException("agent id is null");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE Agent SET missionId = NULL WHERE id = ? AND missionId = ?");
            st.setLong(1, agent.getId());
            st.setLong(2, mission.getId());
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, agent, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when removing agent from mission";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public List<Agent> getAssignedAgents(Mission mission) throws ServiceFailureException, IllegalEntityException {
        checkDataSource();
        if (mission == null) {
            throw new IllegalArgumentException("mission is null");
        }
        if (mission.getId() == null) {
            throw new IllegalEntityException("mission id is null");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT Agent.id, agentname, agentcover, favweapon " +
                            "FROM Agent JOIN Mission ON Missionid = Agent.missionId " +
                            "WHERE Missionid = ?");
            st.setLong(1, mission.getId());
            return AgentManagerImpl.executeQueryForMultipleAgents(st);
        } catch (SQLException ex) {
            String msg = "Error when trying to find agents on mission " + mission;
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }


    @Override
    public Mission getAssignedMission(Agent agent) throws ServiceFailureException, IllegalEntityException {
        checkDataSource();
        if (agent == null) {
            throw new IllegalArgumentException("agent is null");
        }
        if (agent.getId() == null) {
            throw new IllegalEntityException("agent id is null");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT Mission.id, location, codename " +
                            "FROM Mission JOIN Agent ON Mission.id = Agent.missionId " +
                            "WHERE Agent.id = ?");
            st.setLong(1, agent.getId());
            return MissionManagerImpl.executeQueryForSingleMission(st);
        } catch (SQLException ex) {
            String msg = "Error when trying to find mission with agent " + agent;
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }
    
}
