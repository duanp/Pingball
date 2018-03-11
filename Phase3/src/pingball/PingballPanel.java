package pingball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Graphics;

import java.util.List;

import pingball.Ball;
import pingball.Gadgets.Gadget;

/**
 * A mutable class that displays the board graphics.  It is a JComponent which displays the
 * board graphics to the nearest pixel, showing all the gadgets and balls in the correct
 * locations at the time and the correct shapes.
 */
public class PingballPanel extends JPanel implements MouseListener {
    
    private static final long serialVersionUID = 1L;
    final int SCALE = 25; // pixels per 1L
    private Board board;

    /**
     * Constructs an instance of the panel initializing the board and listening
     * for mouse clicks
     * @param inputBoard the board the panel should be displaying
     */
    public PingballPanel(Board inputBoard){
        setFocusable(true);
        addMouseListener(this);
        board = inputBoard;
    }
    
    /**
     * Displays on this PingballPanel the current state of a board, including all the gadgets
     * and balls in their proper shapes.
     */
    @Override
    public void paintComponent(Graphics g) {
        setBackground(Color.WHITE);
        
        Graphics2D graphics = (Graphics2D) g;
        for (Gadget gadget: board.getGadgets()){
            Shape shape = gadget.getShape(SCALE);
            graphics.setColor(gadget.getDrawColor());
            graphics.draw(shape);
            graphics.fill(shape);
        }
        for (Ball ball: board.getBalls()){
            Shape shape = ball.getShape(SCALE);
            graphics.setColor(Color.BLACK);
            graphics.draw(shape);
            graphics.fill(shape);
        }
    }

    /**
     * Sets the board the panel should display
     * @param newBoard the board to display, must not be null
     */
    public void setBoard(Board newBoard){
        board = newBoard;
    }

    /**
     * Gives the "focus" to the PingballPanel when clicked, so further input (such as keyboard
     * strokes to trigger gadgets) matters.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();
    }

    /**
     * Does nothing, required by MouseListener.
     */
    @Override
    public void mousePressed(MouseEvent e) {}

    /**
     * Does nothing, required by MouseListener.
     */
    @Override
    public void mouseReleased(MouseEvent e) {}

    /**
     * Does nothing, required by MouseListener.
     */
    @Override
    public void mouseEntered(MouseEvent e) {}

    /**
     * Does nothing, required by MouseListener.
     */
    @Override
    public void mouseExited(MouseEvent e) {}
}
