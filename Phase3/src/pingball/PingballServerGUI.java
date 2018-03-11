package pingball;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class PingballServerGUI extends JFrame {
    /**
     * GUI that will start the server and allow the user to easily join board by 
     * entering in board names and selecting the join type.
     * 
     * The class is mutable.
     * 
     * Rep invariant:
     *      - the table displays all successful wall joins during entire session
     * 
     * GUI Thread-safety argument:  all methods that mutate the GUI are confined to the event
     *                              dispatch thread and are the wrapped in invokeLater
     */
    
    // This is our "new feature" for the project.  It's a GUI that gives the user of the
    // server an interface with which to connect boards.  We decided to keep it separate from
    // PingballServer so that PingballServer would still satisfy the given specs.  Run this
    // file separately if you want to run the GUI.
    //
    // The table at the bottom gives all the joins that have been successfully made.  Note
    // this isn't necessary the connections between boards that currently exist, since clients
    // can disconnect, change boards, or do any number of things which causes the join to be
    // unmade.
    
    private static final long serialVersionUID = 1L;
    /** Default server port. */
    private static final int DEFAULT_PORT = 10987;
    private final JTextField portField;
    private final JTextField leftTopBoardField;
    private final JTextField rightBottomBoardField;
    private final JButton startServerButton;
    private final JButton joinWallButton;
    private final JComboBox<String> joinTypeMenu;
    private final JScrollPane joinTableScrollPane;
    private final JTable joinTable;
    private final int currentPort;
    private PingballServer model;

    /**
     * Constructs an instance of the Pingball Server GUI, initializing
     * all elements and listeners.  
     * @throws IOException
     */
    public PingballServerGUI() throws IOException{
        setTitle("Pingball Server");
        portField = new JTextField("port");
        // Initializing all GUI components
        leftTopBoardField = new JTextField("left/top board");
        rightBottomBoardField = new JTextField("right/bottom board");
        startServerButton = new JButton("START");
        startServerButton.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        try {
                            String portString = portField.getText();
                            if (portString.length() > 0 && isInteger(portString)){
                                int newPort = Integer.parseInt(portField.getText());
                                if (newPort != currentPort) {
                                    model.setServerSocket(newPort);
                                }
                            }
                            new Thread(new RunServer()).start();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }


                });
        joinWallButton = new JButton("join");
        joinWallButton.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        try {
                            String joinType = joinTypeMenu.getSelectedItem().toString();
                            String leftTopBoard = leftTopBoardField.getText();
                            String rightBottomBoard = rightBottomBoardField.getText();
                            if (joinType.equals("Vertical Join")){
                                if (model.handleVerticalBoardJoins(leftTopBoard, rightBottomBoard)){
                                    SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            ((DefaultTableModel) joinTable.getModel()).addRow(new Object[]{joinType, leftTopBoard, rightBottomBoard});
                                        }
                                    });
                                }

                            } else {
                                if ( model.handleHorizontalBoardJoins(leftTopBoard, rightBottomBoard)){
                                    SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            ((DefaultTableModel) joinTable.getModel()).addRow(new Object[]{joinType, leftTopBoard, rightBottomBoard});
                                        }
                                    });
                                }
                            }
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
        joinTypeMenu = new JComboBox<String>();
        joinTypeMenu.addItem("Vertical Join");
        joinTypeMenu.addItem("Horizontal Join");
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn(""); 
        tableModel.addColumn("");
        tableModel.addColumn("");
        joinTable = new JTable(tableModel);
        joinTable.setName("guessTable");
        joinTableScrollPane = new JScrollPane(joinTable);
        joinTable.setFillsViewportHeight(true);
        currentPort = DEFAULT_PORT;
        model = new PingballServer(currentPort, new LinkedBlockingQueue<>(), new LinkedBlockingQueue<>(), new LinkedBlockingQueue<>());

        // Setting the GUI layout row by row
        JPanel serverRow = new JPanel();
        GroupLayout serverRowLayout = new GroupLayout(serverRow);
        serverRow.setLayout(serverRowLayout);
        serverRowLayout.setAutoCreateGaps(true);
        serverRowLayout.setAutoCreateContainerGaps(true);
        serverRowLayout.setHorizontalGroup(
                serverRowLayout.createSequentialGroup()
                .addComponent(portField)
                .addComponent(startServerButton)
                );
        serverRowLayout.setVerticalGroup(
                serverRowLayout.createSequentialGroup()
                .addGroup(serverRowLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(portField)
                        .addComponent(startServerButton))

                );
        JPanel boardsRow = new JPanel();
        GroupLayout boardsRowLayout = new GroupLayout(boardsRow);
        boardsRow.setLayout(boardsRowLayout);
        boardsRowLayout.setAutoCreateGaps(true);
        boardsRowLayout.setAutoCreateContainerGaps(true);
        boardsRowLayout.setHorizontalGroup(
                boardsRowLayout.createSequentialGroup()
                .addComponent(joinTypeMenu)
                .addComponent(leftTopBoardField)
                .addComponent(rightBottomBoardField)
                .addComponent(joinWallButton)
                );
        boardsRowLayout.setVerticalGroup(
                boardsRowLayout.createSequentialGroup()
                .addGroup(boardsRowLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(joinTypeMenu)
                        .addComponent(leftTopBoardField)
                        .addComponent(rightBottomBoardField)
                        .addComponent(joinWallButton)

                        ));

        GroupLayout pingballServerLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(pingballServerLayout);
        pingballServerLayout.setAutoCreateGaps(true);
        pingballServerLayout.setAutoCreateContainerGaps(true);
        pingballServerLayout.setHorizontalGroup(
                pingballServerLayout.createSequentialGroup()
                .addGroup(pingballServerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(serverRow)
                        .addComponent(boardsRow)
                        .addComponent(joinTableScrollPane)
                        )
                );
        pingballServerLayout.setVerticalGroup(
                pingballServerLayout.createSequentialGroup()
                .addComponent(serverRow)
                .addComponent(boardsRow)
                .addComponent(joinTableScrollPane)
                );

        // program stops when you close the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Checks whether or not the input String s is a nonnegative integer
     * @param s - the input string
     * @return a boolean indicating whether or not s is a nonnegative integer
     */
    public static boolean isInteger(String s) {
        try {
            int number = Integer.parseInt(s); 
            if (number < 0){
                return false;
            }
        } catch (NumberFormatException e) { 
            return false; 
        }
        return true;
    }
    
    /**
     * Runnable class that creates a new thread to run the server
     * so that the GUI stays responsive
     */
    class RunServer implements Runnable{
        public void run(){
            model.startThreadSendMessages(model);
            //  model.startThreadReadConsole(model); 
            model.startThreadDisconnectMessages(model); 

            // Use main thread for server.
            try {
                model.serve();
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }

    
    /**
     * Runs the server GUI.
     * @param args
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PingballServerGUI main;
                try {
                    main = new PingballServerGUI();
                    main.setSize(600, 300);
                    main.setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
