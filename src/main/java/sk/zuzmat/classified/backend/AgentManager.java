/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classified;

import java.util.List;

/**
 *
 * @author xschwar2
 */
public interface AgentManager {
    
    
    public void createAgent(Agent agent);
    
    public void updateAgent(Agent agent);
    
    public void deleteAgent(Agent agent);
    
    public Agent findAgentById(Long id);
    
    public List<Agent> findAllAgents();
    
    
    
}
