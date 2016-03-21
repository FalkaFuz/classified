/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classified;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import javax.sql.DataSource;

/**
 *
 * @author Falka
 */
public class AgentManagerImpl implements AgentManager{

      private final DataSource dataSource;

    public AgentManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public void createAgent(Agent agent) {
        validate(agent);
        if (agent.getId() != null) {
            throw new IllegalArgumentException("agnet id is already set");
        }

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "INSERT INTO AGENT (agentname,agentcover,favweapon) VALUES (?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, agent.getName());
            st.setString(2, agent.getCoverName());
            st.setString(3, agent.getFavouriteWeapon());

            ResultSet keyRS = st.getGeneratedKeys();
            agent.setId(getKey(keyRS, agent));

        } catch (SQLException ex) {
            throw new ServiceFailureException("Error when inserting agent " + agent, ex);
        }
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

    private Long getKey(ResultSet keyRS, Agent agent) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {

            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert agent " + agent
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert agent " + agent
                    + " - no key found");
        }
    }

    @Override
    public void updateAgent(Agent agent) {

        validate(agent);
        if (agent.getId() == null) {
            throw new IllegalArgumentException("agent id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "UPDATE Agent SET agentname = ?, agentcover = ?, favweapon = ? WHERE id = ?")) {

            st.setString(1, agent.getName());
            st.setString(2, agent.getCoverName());
            st.setString(3, agent.getFavouriteWeapon());
            st.setLong(4, agent.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("Agent " + agent + " was not found in database!");
            } else if (count != 1) {
                throw new ServiceFailureException("Invalid updated rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when updating agent " + agent, ex);

        }
    }
    
    @Override
    public void deleteAgent(Agent agent) throws ServiceFailureException {
        if (agent == null) {
            throw new IllegalArgumentException("agent is null");
        }
        if (agent.getId() == null) {
            throw new IllegalArgumentException("agent id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                    "DELETE FROM agent WHERE id = ?")) {

            st.setLong(1, agent.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("Agent " + agent + " was not found in database!");
            } else if (count != 1) {
                throw new ServiceFailureException("Invalid updated rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when updating agent " + agent, ex);
        }
    }
    
    
    @Override
    public Agent findAgentById(Long id) throws ServiceFailureException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,agentname,agentcover,favweapon FROM agent WHERE id = ?")) {

            st.setLong(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Agent agent = resultSetToAgent(rs);

                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + agent + " and " + resultSetToAgent(rs));
                }

                return agent;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving agent with id " + id, ex);
        }
    }
    
    
    @Override
    public List<Agent> findAllAgents() throws ServiceFailureException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,agentname,agentcover,favweapon FROM agent")) {

            ResultSet rs = st.executeQuery();

            List<Agent> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToAgent(rs));
            }
            return result;

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving all agents", ex);
        }
    }
    
    private Agent resultSetToAgent(ResultSet rs) throws SQLException {
        Agent agent = new Agent();
        agent.setId(rs.getLong("id"));
        agent.setName(rs.getString("agentname"));
        agent.setCoverName(rs.getString("agentcover"));
        agent.setFavouriteWeapon(rs.getString("favweapon"));
        return agent;
    }

}
