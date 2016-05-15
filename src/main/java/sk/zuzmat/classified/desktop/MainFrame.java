package sk.zuzmat.classified.desktop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class MainFrame extends JFrame {

    Locale defaultLocale = Locale.getDefault();
    ResourceBundle text = ResourceBundle.getBundle("label", defaultLocale);

    private final AgentManager agentManager = new AgentManagerImpl();
    private final MissionManager missionManager = new MissionManagerImpl();
    private MissionControlManager missionControlManager = new MissionControlManagerImpl();

    private static final Logger log = LogManager.getLogger(MainFrame.class);


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
    private JPanel rootPanel;
    private JTable table1;
    //End of variables declaration

    private void createUIComponents() {

    }

    /**
     *
     * Creates new form MainFrame
     */
    public MainFrame(){
        //createUIComponents();
        setContentPane(rootPanel);
        pack();

        log.info("Inicialized");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
