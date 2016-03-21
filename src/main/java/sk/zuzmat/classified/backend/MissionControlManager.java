/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.zuzmat.classified.backend;

import java.util.List;

/**
 *
 * @author xschwar2
 */
public interface MissionControlManager {
    
    public void assignAgentToMission(Agent agent, Mission mission);
    
    public void removeAgentFromMission(Agent agent, Mission mission);

    public List<Agent> getAssignedAgents(Mission mission);
    
    public Mission getAssignedMission(Agent agent);
    
    
}
