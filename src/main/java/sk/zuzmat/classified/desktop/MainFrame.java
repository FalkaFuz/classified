package sk.zuzmat.classified.desktop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sk.zuzmat.classified.backend.AgentManager;
import sk.zuzmat.classified.backend.AgentManagerImpl;
import sk.zuzmat.classified.backend.MissionControlManager;
import sk.zuzmat.classified.backend.MissionControlManagerImpl;
import sk.zuzmat.classified.backend.MissionManager;
import sk.zuzmat.classified.backend.MissionManagerImpl;
import sk.zuzmat.classified.common.DBUtils;
import javax.sql.DataSource;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Created by Matúš on 14. 5. 2016.
 */
public class MainFrame extends JFrame {

    Locale defaultLocale = Locale.getDefault();
    ResourceBundle label = ResourceBundle.getBundle("label", defaultLocale);


    private static final Logger log = LogManager.getLogger(MainFrame.class);


    private JFrame agentsOnMission;

    //Variables declaration - do not modify
    private JTabbedPane tabbedPane;
    private JTextField missionLocationText;
    private JTextField missionCodeNameText;
    private JButton missionCreateButton;
    private JButton missionDeleteButton;
    private JButton agentCreateButton;
    private JButton agentDeleteButton;
    private JLabel agentNameLabel;
    private JLabel agentCoverNameLabel;
    private JLabel agentFavouriteWeaponLabel;
    private JTextField agentNameText;
    private JTextField agentCoverNameText;
    private JTextField agentFavouriteWeaponText;
    private JLabel missionLocationLabel;
    private JLabel missionCodeNameLabel;
    private JButton agentRemoveButton;
    private JButton agentAssignButton;
    private JComboBox agentMissionComboBox;
    private JLabel agentMissionLabel;
    private JButton missionShowButton;
    private JPanel rootPanel;
    private JPanel agentsPanel;
    private JPanel missionsPanel;
    private JTable agentsTable;
    private JTable missionsTable;
    private JTable table1;
    private JScrollPane agentsScroll;
    private JScrollPane missionScroll;
    //End of variables declaration

    private void setNames(){
        //agentsPanel.setName(label.getString("agentsPanel"));
        agentNameLabel.setText(label.getString("agentNameLabel"));
        agentCoverNameLabel.setText(label.getString("agentCoverLabel"));
        agentFavouriteWeaponLabel.setText(label.getString("agentFav"));
        agentDeleteButton.setText(label.getString("agentDelete"));
        agentCreateButton.setText(label.getString("agentCreate"));
        agentMissionLabel.setText(label.getString("agentMissionLabel"));
        agentRemoveButton.setText(label.getString("agentRemove"));
        agentAssignButton.setText(label.getString("agentAssign"));
        missionLocationLabel.setText(label.getString("missLocationLab"));
        missionCodeNameLabel.setText(label.getString("missCodLabel"));
        missionDeleteButton.setText(label.getString("missDelete"));
        missionCreateButton.setText(label.getString("missCreate"));
        missionShowButton.setText(label.getString("missShow"));

    }

    private void createUIComponents(){
        agentsTable = new JTable();
        agentsTable.setModel(new AgentTableModel());

        missionsTable = new JTable();
        missionsTable.setModel(new MissionTableModel());


    }

    /**
     *
     * Creates new form MainFrame
     */
    public MainFrame(){
        setContentPane(rootPanel);
        setNames();
        log.info("Inicialized");
        this.setTitle(label.getString("appName"));



        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);

    }


    public static void main(String[] args) throws SQLException, InvocationTargetException, InterruptedException {


        System.out.println("Start");
        java.awt.EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run(){
                new MainFrame().setVisible(true);
            }
        });
        System.out.println("End");
    }
}
