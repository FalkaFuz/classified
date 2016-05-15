package sk.zuzmat.classified.desktop;

import sk.zuzmat.classified.backend.AgentManager;
import sk.zuzmat.classified.backend.AgentManagerImpl;
import sk.zuzmat.classified.backend.MissionControlManager;
import sk.zuzmat.classified.backend.MissionControlManagerImpl;
import sk.zuzmat.classified.backend.MissionManager;
import sk.zuzmat.classified.backend.MissionManagerImpl;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Created by Matúš on 14. 5. 2016.
 */
public class MainFrame extends javax.swing.JFrame {

    Locale defaultLocale = Locale.getDefault();
    ResourceBundle text = ResourceBundle.getBundle("label", defaultLocale);

    private final AgentManager agentManager = new AgentManagerImpl();
    private final MissionManager missionManager = new MissionManagerImpl();
    private MissionControlManager missionControlManager = new MissionControlManagerImpl();

    //private final static org.slf4j.Logger log = LoggerFactory.getLogger(MainFrame.class);


    private JFrame agentsOnMission;

    //Variables declaration - do not modify
    private JTabbedPane tabbedPane;
    private JTable missionsTable;
    private JTextField missionLocationText;
    private JTextField missionCodeNameText;
    private JButton missionCreateButton;
    private JButton missionDeleteButton;
    private JTable agentsTable;
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
    private JPanel panel1;
    private JTable table1;
    //End of variables declaration

    private void createUIComponents() {
        tabbedPane = new JTabbedPane();
        missionsTable = new JTable();
        missionLocationText = new JTextField();
        missionCodeNameText = new JTextField();
        missionCreateButton = new JButton();
        missionDeleteButton = new JButton();
        agentsTable = new JTable();
        agentCreateButton = new JButton();
        agentDeleteButton = new JButton();
        agentNameLabel = new JLabel();
        agentCoverNameLabel = new JLabel();
        agentFavouriteWeaponLabel = new JLabel();
        agentNameText = new JTextField();
        agentCoverNameText = new JTextField();
        agentFavouriteWeaponText = new JTextField();
        missionLocationLabel = new JLabel();
        missionCodeNameLabel = new JLabel();
        agentRemoveButton = new JButton();
        agentAssignButton = new JButton();
        agentMissionComboBox = new JComboBox();
        agentMissionLabel = new JLabel();
        missionShowButton = new JButton();
        panel1 = new JPanel();
    }

    /**
     *
     * Creates new form MainFrame
     */
    public MainFrame(){

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
