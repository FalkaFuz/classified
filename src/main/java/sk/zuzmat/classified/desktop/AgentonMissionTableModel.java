package sk.zuzmat.classified.desktop;

import sk.zuzmat.classified.backend.Agent;
import sk.zuzmat.classified.backend.AgentManagerImpl;
import sk.zuzmat.classified.backend.Mission;
import sk.zuzmat.classified.backend.MissionControlManagerImpl;
import sk.zuzmat.classified.common.DBUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Created by Zuuz on 14. 5. 2016.
 */
public class AgentonMissionTableModel extends AbstractTableModel {

    private final AgentManagerImpl agentManager = new AgentManagerImpl();
    private final MissionControlManagerImpl controlManager = new MissionControlManagerImpl();

    Locale defaultLocale = Locale.getDefault();
    ResourceBundle label = ResourceBundle.getBundle("label", defaultLocale);

    Mission mission;

    public AgentonMissionTableModel(Mission mission) {
        agentManager.setDataSource(DBUtils.getDataSource());
        controlManager.setDataSource(DBUtils.getDataSource());
        this.mission = mission;
    }

    @Override
    public int getRowCount() {
        if (mission == null)
            return 0;
        List<Agent> agents = controlManager.getAssignedAgents(mission);
        return  agents != null ? agents.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        List<Agent> agents = controlManager.getAssignedAgents(mission);
        Agent agent = agents.get(rowIndex);


        switch (columnIndex) {
            case 0:
                return agent.getCoverName();
            default:
                throw new IllegalArgumentException("columnIndex");
        }

    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return label.getString("agentTab");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

}