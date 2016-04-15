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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Falka
 */
public class AgentManagerImpl implements AgentManager{

        private static final Logger logger = Logger.getLogger(
                AgentManagerImpl.class.getName());

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
    public void createAgent(Agent agent) {
        checkDataSource();
        validate(agent);
        if (agent.getId() != null) {
            throw new IllegalArgumentException("agent id is already set");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "INSERT INTO AGENT (agentname,agentcover,favweapon) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, agent.getName());
            st.setString(2, agent.getCoverName());
            st.setString(3, agent.getFavouriteWeapon());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, agent, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            agent.setId(id);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when inserting agent into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public void updateAgent(Agent agent) {
        checkDataSource();
        validate(agent);
        if (agent.getId() == null) {
            throw new IllegalEntityException("agent id is null");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE AGENT SET agentname = ?, agentcover = ?, favweapon = ? WHERE id = ?");
            st.setString(1, agent.getName());
            st.setString(2, agent.getCoverName());
            st.setString(3, agent.getFavouriteWeapon());
            st.setLong(4, agent.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, agent, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when updating agent in the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public void deleteAgent(Agent agent) {
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
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "DELETE FROM AGENT WHERE ID = ?");
            st.setLong(1, agent.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, agent, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when deleting agent from the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public Agent findAgentById(Long id) {

        checkDataSource();

        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,agentname,agentcover,favweapon FROM agent WHERE id = ?");
            st.setLong(1, id);
            return executeQueryForSingleAgent(st);
        } catch (SQLException ex) {
            String msg = "Error when getting agent with id = " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public List<Agent> findAllAgents() {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,agentname,agentcover,favweapon FROM AGENT");
            return executeQueryForMultipleAgents(st);
        } catch (SQLException ex) {
            String msg = "Error when getting all agents from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    static Agent executeQueryForSingleAgent(PreparedStatement st) throws SQLException, ServiceFailureException {
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Agent result = rowToAgent(rs);
            if (rs.next()) {
                throw new ServiceFailureException(
                        "Internal integrity error: more agents with the same id found!");
            }
            return result;
        } else {
            return null;
        }
    }

    static List<Agent> executeQueryForMultipleAgents(PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
        List<Agent> result = new ArrayList<Agent>();
        while (rs.next()) {
            result.add(rowToAgent(rs));
        }
        return result;
    }

    private static Agent rowToAgent(ResultSet rs) throws SQLException {
        Agent result = new Agent();
        result.setId(rs.getLong("id"));
        result.setName(rs.getString("agentname"));
        result.setCoverName(rs.getString("agentcover"));
        result.setFavouriteWeapon(rs.getString("favweapon"));
        return result;
    }



    private void validate(Agent agent) throws IllegalArgumentException {
        if (agent == null) {
            throw new IllegalArgumentException("agent is null");
        }
        if (agent.getName() == null || agent.getName().isEmpty()) {
            throw new IllegalArgumentException("agent name is null or empty");
        }
        if (agent.getCoverName() == null || agent.getCoverName().isEmpty()) {
            throw new IllegalArgumentException("agent cover name is null or empty");
        }
        if (agent.getName().equals(agent.getCoverName())) {
            throw new IllegalArgumentException("name and cover name are same");
        }
        if (agent.getFavouriteWeapon() == null || agent.getFavouriteWeapon().isEmpty()) {
            throw new IllegalArgumentException("favorite weapon is null or empty");
        }

    }
}
