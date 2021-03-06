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
 * Created by zuzka on 15.05.2016.
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
        return missionManager.findAllMissions() != null ? missionManager.findAllMissions().size() : 0;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        List<Mission> missions = missionManager.findAllMissions();
        Mission mission = missions.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return mission.getCodeName();
            case 1:
                return mission.getLocation();
            default:
                throw new IllegalArgumentException("columnIndex");
        }

    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return label.getString("missCodLabel");
            case 1:
                return label.getString("missLocationLab");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
                return true;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }


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

    }
}
