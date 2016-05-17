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
                return label.getString("agents");
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
/*
    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        List<Agent> agents = agentManager.findAllAgents();
        final Agent agent = agents.get(rowIndex);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                switch (columnIndex) {
                    case 0:
                        agent.setCoverName((String) aValue);
                        break;
                    case 1:
                        agent.setName((String) aValue);
                        break;
                    case 2:
                        agent.setFavouriteWeapon((String) aValue);
                        break;
                    default:
                        throw new IllegalArgumentException("columnIndex");
                }
                return null;
            }

            @Override
            public void done() {
                agentManager.updateAgent(agent);
                fireTableCellUpdated(rowIndex, columnIndex);
            }

        }.execute();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
            case 2:
                return true;
            case 3:
                return false;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }


    public void addAgent(final Agent agent) {

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                agentManager.createAgent(agent);
                return null;
            }

            @Override
            protected void done() {
                int lastRow = agentManager.findAllAgents().size() - 1;
                fireTableRowsInserted(lastRow, lastRow);
            }

        }.execute();

    }

    public void removeAgent(final Agent agent) {

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                agentManager.deleteAgent(agent);
                return null;
            }

            @Override
            public void done() {
                int lastRow = agentManager.findAllAgents().size() - 1;
                fireTableRowsDeleted(lastRow, lastRow);
            }
        }.execute();
    }

    public void assignMission(final Agent agent, final Mission mission, int row){
        new SwingWorker<Void, Void>(){

            @Override
            protected Void doInBackground(){
                controlManager.assignAgentToMission(agent, mission);
                return null;
            }

            @Override
            protected void done(){
                fireTableCellUpdated(row, 3);
            }

        }.execute();
    }

    public void removeMission(final Agent agent, int row) {
        new SwingWorker<Void, Void>(){

            @Override
            protected Void doInBackground(){
                controlManager.removeAgentFromMission(agent, controlManager.getAssignedMission(agent));
                return null;
            }

            @Override
            protected void done(){
                fireTableCellUpdated(row, 3);
            }

        }.execute();
    }
    */

}