package pingball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;



public class PingballGUI extends JFrame {
    /**
     * Client GUI class that displays the board as well as buttons and fields to connect 
     * and disconnect from the server, and start, pause, resume, restart the game.
     * 
     * How to interact with the GUI:
     *      always press buttons, ENTER is not sufficient
     *      Load - loads a board file, if no file provided load Default
     *      START - runs the board
     *      pause - pauses game execution
     *      resume - resumes/starts game execution
     *      exit - closes GUI window
     *      connect - connects to provided host and port
     *      disconnect - disconnects from any server
     * 
     * The class is mutable. 
     * 
     * Rep invariant:
     *      board labels always display names of the board walls that are connected
     *      board panel always displays the board that is loaded
     * 
     * GUI Thread-safety argument:  all methods that mutate the GUI are confined to the event
     *                              dispatch thread is the wrapped in invokeLater
     */

    private String currentHost;
    private int currentPort;
    private String currentBoardName = "default.pb";

    private final JTextField boardFileField;
    private final JButton loadFileButton;
    private final JTextField hostField;
    private final JTextField portField;
    private final JButton connectButton;
    private final JButton disconnectButton;
    private final JLabel topWallConnection;
    private final JLabel bottomWallConnection;
    private final JLabel leftWallConnection;
    private final JLabel rightWallConnection;
    private final PingballPanel boardPanel;
    private final JButton startButton;
    private final JButton exitButton;
    private final JButton pauseButton;
    private final JButton resumeButton;
    private final JButton restartButton;
    private Pingball clientModel;
    private AtomicBoolean isBoardConnected;
    private Board currentBoard;
    
    private final static String VERTICAL_UNCONNECTED = "<html>U<br>N<br>C<br>O<br>N<br>N<br>E<br>C<br>T<br>E<br>D</html>";
    private final static String boardFileFieldText = "board file path";
    private final static String unconnectedText = "UNCONNECTED";

    private AtomicBoolean isPainting = new AtomicBoolean(false);
    
    private static final int DEFAULT_PORT = 10987;
    
    /**
     * Constructs an instance of the PingballGUI, initializing all elements
     * and listeners.
     * @throws IOException
     */
    public PingballGUI(Optional<Board> board, Optional<String> host, Optional<Integer> port) throws IOException {
    setTitle("Pingball Client");
        isBoardConnected = new AtomicBoolean(false);
        clientModel = new Pingball();
        
        // Initializing all GUI components
        boardFileField = new JTextField(boardFileFieldText);
        hostField = new JTextField("host");
        portField = new JTextField("port");
        loadFileButton = new JButton("load");
        connectButton = new JButton("connect");
        connectButton.addActionListener( // connects client to server in hostField
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        try {
                            currentPort = Integer.parseInt(portField.getText());
                        } catch (NumberFormatException ne) {
                            // ignore invalid number formats
                            return;
                        }
                        String host = hostField.getText();
                        currentHost = host;
                        if (host.length() > 0) {
                            try {
                                clientModel.connect (currentHost, currentPort);
                                isBoardConnected.set(true);
                                clientModel.startThreadListenMessages(clientModel); 
                            } catch (IOException e1) {
                                // ignore if server with host and port not running
                                return;
                            }

                        }
                    }
                });
        disconnectButton = new JButton("disconnect");
        disconnectButton.addActionListener( // disconnects client from server
                new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        if (isBoardConnected.get()) {
                            try {
                                clientModel.close();
                                isBoardConnected.set(false);
                                currentBoard.disconnect();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });

        startButton = new JButton("START");
        startButton.addActionListener( // begins simulation of current board
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        clientModel.resume();
                        if (isBoardConnected.get()){ 
                            //      clientModel.startThreadListenMessages(clientModel); 
                            //    clientModel.startThreadBoardUpdate(clientModel);
                        }
                        clientModel.startThreadBoardUpdate(clientModel);

                        // update graphics
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                try {
                                    if (isPainting.get()) {
                                        return;
                                    }
                                    isPainting.set(true);
                                    while(true){
                                        Thread.sleep(Pingball.TIME_RESOLUTION);
                                        SwingUtilities.invokeLater(new Runnable() {
                                            public void run() {
                                                boardPanel.repaint();
                                                updateWallNames(clientModel.getBoard().getConnectedWalls());
                                            }
                                        });
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
        exitButton = new JButton("exit"); // closes program and window
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0); 
            }
        });
        pauseButton = new JButton("pause");
        pauseButton.addActionListener( // stops current simulation until resumed
                new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        clientModel.pause();
                    }
                });

        resumeButton = new JButton("resume");
        resumeButton.addActionListener( // causes current simulation to stop being stopped
                new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        clientModel.resume();
                    }
                });

        restartButton = new JButton("restart");
        restartButton.addActionListener( // resets the board to its original position
                new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        try {
                            currentBoard = FileParsing.createBoardFromFile(
                                    new File("boards/" + currentBoardName));
                            if (isBoardConnected.get()) {
                                clientModel.restart();
                            }

                            clientModel.setBoard(currentBoard);
                            boardPanel.setBoard(currentBoard);
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    setAllWallsUnconnected();
                                }
                            });
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

        topWallConnection = new JLabel("UNCONNECTED");
        bottomWallConnection = new JLabel("UNCONNECTED");
        leftWallConnection = new JLabel(VERTICAL_UNCONNECTED);
        leftWallConnection.setMaximumSize(new Dimension(2, 20));
        rightWallConnection = new JLabel(VERTICAL_UNCONNECTED);
        rightWallConnection.setMaximumSize(new Dimension(2, 20));
        currentBoard = new Board();
        clientModel = new Pingball(currentBoard);
        boardPanel = new PingballPanel(clientModel.getBoard());
        boardPanel.setSize(500, 500);
        boardPanel.requestFocusInWindow();
        // listen for keyboard input
        boardPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                currentBoard.keyTrigger(e.getKeyCode(), true);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                currentBoard.keyTrigger(e.getKeyCode(), false);
            }
        });
        loadFileButton.addActionListener(new ActionListener() { // loads board from file
            public void actionPerformed(ActionEvent e) { // entered into boardFileField
                String defaultBoardName = "default.pb";
                currentBoardName = boardFileField.getText();
                if (currentBoardName.length() <= 0
                        || currentBoardName.equals(boardFileFieldText)) {
                    currentBoardName = defaultBoardName;
                }
                try {
                    currentBoard = FileParsing.createBoardFromFile(new File(
                            "boards/" + currentBoardName));
                } catch (FileNotFoundException fnfe) {
                    // mute file not found error
                    return;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                clientModel.pause();
                clientModel.setBoard(currentBoard);
                clientModel.restart();
                boardPanel.setBoard(clientModel.getBoard());
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        boardPanel.repaint();
                        setAllWallsUnconnected();
                    }
                });
            }
        });

        // Setting the GUI layout row by row
        JPanel fileRow = new JPanel();
        GroupLayout fileRowLayout = new GroupLayout(fileRow);
        fileRow.setLayout(fileRowLayout);
        fileRowLayout.setAutoCreateGaps(true);
        fileRowLayout.setAutoCreateContainerGaps(true);
        fileRowLayout.setHorizontalGroup(
                fileRowLayout.createSequentialGroup()
                .addComponent(boardFileField)
                .addComponent(loadFileButton)
                );
        fileRowLayout.setVerticalGroup(
                fileRowLayout.createSequentialGroup()
                .addGroup(fileRowLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(boardFileField)
                        .addComponent(loadFileButton))

                );
        JPanel serverRow = new JPanel();
        GroupLayout serverRowLayout = new GroupLayout(serverRow);
        serverRow.setLayout(serverRowLayout);
        serverRowLayout.setAutoCreateGaps(true);
        serverRowLayout.setAutoCreateContainerGaps(true);
        serverRowLayout.setHorizontalGroup(
                serverRowLayout.createSequentialGroup()
                .addComponent(hostField)
                .addComponent(portField)
                .addComponent(connectButton)
                .addComponent(disconnectButton)
                );
        serverRowLayout.setVerticalGroup(
                serverRowLayout.createSequentialGroup()
                .addGroup(serverRowLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(hostField)
                        .addComponent(portField)
                        .addComponent(connectButton)
                        .addComponent(disconnectButton))

                );

        JPanel boardRow = new JPanel();
        GroupLayout boardRowLayout = new GroupLayout(boardRow);
        boardRow.setLayout(boardRowLayout);
        boardRowLayout.setAutoCreateGaps(true);
        boardRowLayout.setAutoCreateContainerGaps(true);
        boardRowLayout.setHorizontalGroup(
                boardRowLayout.createSequentialGroup()
                .addComponent(leftWallConnection)
                .addComponent(boardPanel, 500, 500, 500)
                .addComponent(rightWallConnection)
                );
        boardRowLayout.setVerticalGroup(
                boardRowLayout.createSequentialGroup()
                .addGroup(boardRowLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(leftWallConnection)
                        .addComponent(boardPanel, 500, 500, 500)
                        .addComponent(rightWallConnection))

                );

        JPanel actionRow = new JPanel();
        GroupLayout actionRowLayout = new GroupLayout(actionRow);
        actionRow.setLayout(actionRowLayout);
        actionRowLayout.setAutoCreateGaps(true);
        actionRowLayout.setAutoCreateContainerGaps(true);
        actionRowLayout.setHorizontalGroup(
                actionRowLayout.createSequentialGroup()
                .addComponent(startButton)
                .addComponent(exitButton)
                .addComponent(pauseButton)
                .addComponent(resumeButton)
                .addComponent(restartButton)
                );
        actionRowLayout.setVerticalGroup(
                actionRowLayout.createSequentialGroup()
                .addGroup(actionRowLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(startButton)
                        .addComponent(exitButton)
                        .addComponent(pauseButton)
                        .addComponent(resumeButton)
                        .addComponent(restartButton))

                );

        GroupLayout pingballLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(pingballLayout);
        pingballLayout.setAutoCreateGaps(true);
        pingballLayout.setAutoCreateContainerGaps(true);
        pingballLayout.setHorizontalGroup(
                pingballLayout.createSequentialGroup()
                .addGroup(pingballLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(fileRow)
                        .addComponent(serverRow)
                        .addComponent(topWallConnection)
                        .addComponent(boardRow)
                        .addComponent(bottomWallConnection)
                        .addComponent(actionRow)
                        )
                );
        pingballLayout.setVerticalGroup(
                pingballLayout.createSequentialGroup()
                .addComponent(fileRow)
                .addComponent(serverRow)
                .addComponent(topWallConnection)
                .addComponent(boardRow)
                .addComponent(bottomWallConnection)
                .addComponent(actionRow)
                );

        // program stops when you close the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // initiates board if necessary (uses command line arguments)
        if (board.isPresent()){
            currentBoard = board.get();
            currentBoardName = currentBoard.getName();
            clientModel.setBoard(currentBoard);
            clientModel.restart();
            clientModel.pause();
            boardPanel.setBoard(clientModel.getBoard());
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    boardPanel.repaint();
                    setAllWallsUnconnected();
                }
            });
        }
        if (host.isPresent()){
            if (port.isPresent()){
                currentPort = port.get();
                portField.setText(port.get() + "");
            } else {
                currentPort = DEFAULT_PORT;
            }
            String hostname = host.get();
            currentHost = hostname;
            hostField.setText(host.get());
            if (hostname.length() > 0) {
                try {
                    clientModel.connect (currentHost, currentPort);
                    isBoardConnected.set(true);
                    clientModel.startThreadListenMessages(clientModel); 
                } catch (IOException e1) {
                    // ignore if server with host and port not running
                    return;
                }

            }
        }

        // paint the board initially
        boardPanel.setBackground(Color.WHITE);
        boardPanel.repaint();
    }

    /**
     * Updates the wall labels indicating whether a wall is connected
     * to another board or whether it is unconnected.
     * @param connectedWallMap a map of wall types to names of boards the wall types
     *      are connected to, (WallType type --> String nameOfBoard)
     */
    private void updateWallNames(Map<WallType, String> connectedWallMap) {
        if (isBoardConnected.equals(new AtomicBoolean(false))){
            setAllWallsUnconnected();

        } else {
            if(connectedWallMap.containsKey(WallType.TOP)){
                if (connectedWallMap.get(WallType.TOP).length() > 0){
                    topWallConnection.setText(connectedWallMap.get(WallType.TOP));
                } else if (!topWallConnection.getText().equals(unconnectedText)){
                    topWallConnection.setText(unconnectedText);
                }
            } 
            if(connectedWallMap.containsKey(WallType.BOTTOM)){
                if (connectedWallMap.get(WallType.BOTTOM).length() > 0){
                    bottomWallConnection.setText(connectedWallMap.get(WallType.BOTTOM));
                } else if (!bottomWallConnection.getText().equals(unconnectedText)){
                    bottomWallConnection.setText(unconnectedText);
                }
            } 
            if(connectedWallMap.containsKey(WallType.LEFT)){
                if (connectedWallMap.get(WallType.LEFT).length() > 0){
                    leftWallConnection.setText(createVerticalBoardName(connectedWallMap.get(WallType.LEFT)));
                } else if (!leftWallConnection.getText().equals(unconnectedText)){
                    leftWallConnection.setText(VERTICAL_UNCONNECTED);
                }
            } 
            if(connectedWallMap.containsKey(WallType.RIGHT)){
                if (connectedWallMap.get(WallType.RIGHT).length() > 0){
                    rightWallConnection.setText(createVerticalBoardName(connectedWallMap.get(WallType.RIGHT)));
                } else if (!rightWallConnection.getText().equals(unconnectedText)){
                    rightWallConnection.setText(VERTICAL_UNCONNECTED);
                }
            }
        }
    }

    /**
     * Adds tags to boardname so that it will display vertically
     * @param boardName - the input boardName
     * @return - the boardName with the appropiate html tags so that it will 
     * display vertically
     */
    public static String createVerticalBoardName(String boardName){
        String output = "<html>";
        for (int i = 0; i < boardName.length(); i++){
            output += boardName.charAt(i) + "<br>";
        }
        return output + "</html>";
    }

    /**
     * Sets all wall labels to "UNCONNECTED"
     */
    private void setAllWallsUnconnected(){
        topWallConnection.setText(unconnectedText);
        bottomWallConnection.setText(unconnectedText);
        leftWallConnection.setText(VERTICAL_UNCONNECTED);
        rightWallConnection.setText(VERTICAL_UNCONNECTED);
    }

    /**
     * Runs the GUI.
     * @param args
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    PingballGUI main = new PingballGUI(Optional.empty(), Optional.empty(), Optional.empty());
                    main.setSize(555, 730);
                    main.setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
