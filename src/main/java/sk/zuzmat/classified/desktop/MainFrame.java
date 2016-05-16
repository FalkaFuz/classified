package sk.zuzmat.classified.desktop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sk.zuzmat.classified.backend.Agent;
import sk.zuzmat.classified.backend.AgentManagerImpl;
import sk.zuzmat.classified.backend.Mission;
import sk.zuzmat.classified.backend.MissionControlManager;
import sk.zuzmat.classified.backend.MissionControlManagerImpl;
import sk.zuzmat.classified.backend.MissionManagerImpl;
import sk.zuzmat.classified.common.DBUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Created by Matúš on 14. 5. 2016.
 */
public class MainFrame extends JFrame {

    Locale defaultLocale = Locale.getDefault();
    ResourceBundle label = ResourceBundle.getBundle("label", defaultLocale);

    private final AgentManagerImpl agentManager = new AgentManagerImpl();
    private final MissionManagerImpl missionManager = new MissionManagerImpl();
    private final MissionControlManagerImpl controlManager = new MissionControlManagerImpl();

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

        agentMissionComboBox = new JComboBox();



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

        agentManager.setDataSource(DBUtils.getDataSource());
        missionManager.setDataSource(DBUtils.getDataSource());
        controlManager.setDataSource(DBUtils.getDataSource());

        List<Mission> missions = missionManager.findAllMissions();
        agentMissionComboBox.setModel(new DefaultComboBoxModel(missions.toArray()));
        agentMissionComboBox.insertItemAt(null, 0);
        agentMissionComboBox.setSelectedIndex(0);
        agentMissionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agentMissionComboBoxAction(e);
            }
        });

        agentDeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agentDeleteButtonAction(e);
            }
        });
        missionDeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                missionDeleteButtonAction(e);
            }
        });
        missionCreateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                missionCreateButtonAction(e);
            }
        });
        agentCreateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agentCreateButtonAction(e);
            }
        });
        agentAssignButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agentAssignButtonAction(e);
            }
        });
        agentRemoveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agentRemoveButtonAction(e);
            }
        });


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);

    }

    private void agentMissionComboBoxAction(ActionEvent e){}

    private void agentRemoveButtonAction(ActionEvent e){

        try {
            int row = agentsTable.getSelectedRow();
            AgentTableModel tableModel = (AgentTableModel) agentsTable.getModel();

            List<Agent> agents = agentManager.findAllAgents();
            Agent agent = agents.get(row);

            if(agent == null){
                throw new NullPointerException("agent is null");
            }

            log.info("Removing agent from mission");
            tableModel.removeMission(agent, row);
        } catch (NullPointerException ex){
            log.error("agent is not selected");
            JOptionPane.showMessageDialog(null, "Agent is not selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void agentAssignButtonAction(ActionEvent e){

        try {
            Mission mission = (Mission) agentMissionComboBox.getSelectedItem();
            int row = agentsTable.getSelectedRow();
            AgentTableModel tableModel = (AgentTableModel) agentsTable.getModel();

            List<Agent> agents = agentManager.findAllAgents();
            Agent agent = agents.get(row);

            if(agent == null || mission == null){
                throw new NullPointerException("agent or mission is null");
            }

            log.info("Agent " + agent + " sending to " + mission);
            tableModel.assignMission(agent, mission, row);

            agentMissionComboBox.setSelectedIndex(0);

        } catch(NullPointerException | IndexOutOfBoundsException ex){
            log.error("Not selected agent or mission");
            JOptionPane.showMessageDialog(null, "Select something", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agentCreateButtonAction(ActionEvent evt){
        try {
            Agent agent = new Agent();
            AgentTableModel tableModel = (AgentTableModel) agentsTable.getModel();


            agent.setName(agentNameText.getText());
            agent.setCoverName(agentCoverNameText.getText());
            agent.setFavouriteWeapon(agentFavouriteWeaponText.getText());

            log.info("Adding agent " + agent + " into db");
            tableModel.addAgent(agent);

            agentNameText.setText(null);
            agentCoverNameText.setText(null);
            agentFavouriteWeaponText.setText(null);
        } catch (IllegalArgumentException e){
            log.error("Check something", e);

        }
    }

    private void missionCreateButtonAction(ActionEvent evt) {

        try {
            Mission mission = new Mission();
            MissionTableModel tableModel = (MissionTableModel) missionsTable.getModel();

            mission.setCodeName(missionCodeNameText.getText());
            mission.setLocation(missionLocationText.getText());

            missionManager.validate(mission);
            log.info("Adding mission " + mission + " into db via swing components");
            tableModel.addMission(mission);

            DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) agentMissionComboBox.getModel();
            comboBoxModel.addElement(mission);

            missionCodeNameText.setText(null);
            missionLocationText.setText(null);


        } catch (IllegalArgumentException ex) {
            String msg = "Check the filling fields! [some is not correct]";
            log.error(msg + " when adding mission", ex);
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agentDeleteButtonAction(ActionEvent evt) {
        int row = agentsTable.getSelectedRow();
        List<Agent> agents = agentManager.findAllAgents();
        Agent agent = null;

        try {
            agent = agents.get(row);
        } catch (ArrayIndexOutOfBoundsException ex) {
            String msg = "Nothing to remove!";
            log.error(msg + " [table of agents is empty]");
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AgentTableModel tableModel = (AgentTableModel) agentsTable.getModel();
        tableModel.removeAgent(agent);

    }

    private void missionDeleteButtonAction(ActionEvent evt) {
        int row = missionsTable.getSelectedRow();
        List<Mission> missions = missionManager.findAllMissions();
        Mission mission = null;

        try {
            mission = missions.get(row);
        } catch (ArrayIndexOutOfBoundsException ex) {
            String msg = "Nothing to remove!";
            log.error(msg + " [table of missions is empty]");
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        MissionTableModel tableModel = (MissionTableModel) missionsTable.getModel();
        tableModel.removeMission(mission);

        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) agentMissionComboBox.getModel();
        comboBoxModel.removeElement(mission);
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
