package sk.zuzmat.classified.desktop;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import sk.zuzmat.classified.backend.Mission;
import sk.zuzmat.classified.backend.MissionManagerImpl;
import sk.zuzmat.classified.common.DBUtils;
/**
 * Created by Matúš on 16. 5. 2016.
=======

/**
 * Created by zuzka on 15.05.2016.
>>>>>>> e1dbe5fc38117a7c7d6346f8acf4252d907799ac
 */
public class MissionTableModel extends AbstractTableModel {

    private final MissionManagerImpl missionManager = new MissionManagerImpl();

    Locale defaultLocale = Locale.getDefault();
    ResourceBundle label = ResourceBundle.getBundle("label", defaultLocale);

    public MissionTableModel() {
        missionManager.setDataSource(DBUtils.getDataSource());
    }

    @Override
    public int getRowCount() {
        return 50;
        //return agentManager.findAllAgents() != null ? agentManager.findAllAgents().size() : 0;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //List<Mission> missions = missionManager.findAllMissions();
        //Mission mission = missions.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return "asdfsdfdfasdf";
            case 1:
                return "sdfsadfasdf";
/*
                return mission.getCodeName();
            case 1:
                return mission.getLocation();
            case 2:
                return mission.getId();*/
            default:
                throw new IllegalArgumentException("columnIndex");
        }

    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return label.getString("mission_name");
            case 1:
                return label.getString("mission_localization");
/*
                return text.getString("code_name");
            case 1:
                return text.getString("location");
            case 2:
                return text.getString("id_check");*/
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
/*
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Long.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
=======

    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        List<Mission> missions = missionManager.findAllMissions();
        final Mission mission = missions.get(rowIndex);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                switch (columnIndex) {
                    case 0:
                        mission.setCodeName((String) aValue);
                        break;
                    case 1:
                        mission.setLocation((String) aValue);
                        break;
                    default:
                        throw new IllegalArgumentException("columnIndex");
                }
                return null;
            }

            @Override
            public void done() {
                missionManager.updateMission(mission);
                fireTableCellUpdated(rowIndex, columnIndex);
            }

        }.execute();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
                return true;
            case 2:
                return false;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    public void addMission(final Mission mission) {

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                missionManager.createMission(mission);
                return null;
            }

            @Override
            protected void done() {
                int lastRow = missionManager.findAllMissions().size() - 1;
                fireTableRowsInserted(lastRow, lastRow);
            }

        }.execute();

    }

    public void removeMission(final Mission mission) {

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                missionManager.deleteMission(mission);
                return null;
            }

            @Override
            protected void done() {
                int lastRow = missionManager.findAllMissions().size() - 1;
                fireTableRowsDeleted(lastRow, lastRow);
            }
        }.execute();

    }*/
}
