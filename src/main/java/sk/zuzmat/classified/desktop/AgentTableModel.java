package sk.zuzmat.classified.desktop;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import sk.zuzmat.classified.backend.*;
import sk.zuzmat.classified.common.DBUtils;



/**
 * Created by Matúš on 14. 5. 2016.
 */
public class AgentTableModel extends AbstractTableModel {

    private final AgentManagerImpl agentManager = new AgentManagerImpl();
    private final MissionControlManagerImpl controlManager = new MissionControlManagerImpl();

    Locale defaultLocale = Locale.getDefault();
    ResourceBundle label = ResourceBundle.getBundle("label", defaultLocale);


    public AgentTableModel() {
        agentManager.setDataSource(DBUtils.getDataSource());
        controlManager.setDataSource(DBUtils.getDataSource());
    }

    @Override
    public int getRowCount() {
        return agentManager.findAllAgents() != null ? agentManager.findAllAgents().size() : 0;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        List<Agent> agents = agentManager.findAllAgents();
        Agent agent = agents.get(rowIndex);

        Mission mission = controlManager.getAssignedMission(agent);

        switch (columnIndex) {
            case 0:
                return agent.getCoverName();
            case 1:
                return agent.getName();
            case 2:
                return agent.getFavouriteWeapon();
            case 3:
                return mission != null ? mission.getCodeName() : "";
            default:
                throw new IllegalArgumentException("columnIndex");
        }

    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return label.getString("agentCoverLabel");
            case 1:
                return label.getString("agentNameLabel");
            case 2:
                return label.getString("agentFav");
            case 3:
                return label.getString("agent_mission");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
            case 2:
            case 3:
                return String.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

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

}