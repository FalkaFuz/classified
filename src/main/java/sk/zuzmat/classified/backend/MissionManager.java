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
public interface MissionManager {
    
    public void createMission(Mission mission);
    
    public void updateMission(Mission mission);
    
    public void deleteMission(Mission mission);
    
    public Mission findMissionById(Long id);
    
    public List<Mission> findAllMissions();
    
}
