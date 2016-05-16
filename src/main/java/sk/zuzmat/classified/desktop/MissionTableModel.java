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
        List<Mission> missions = missionManager.findAllMissions();
        Mission mission = missions.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return "asdfsdfdfasdf";
            case 1:
                return "sdfsadfasdf";
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
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
}
