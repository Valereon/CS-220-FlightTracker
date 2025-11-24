package cs220.flighttracker;

import javax.swing.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.imageio.plugins.jpeg.JPEGImageReadParam;
/*
 * Used ThreadExample from the SwingWorker as the base layout
 */
public class View implements ActionListener {

    private App app;

    private JFrame frame = new JFrame("FlightTracker");

    private JPanel pane = new JPanel(new GridLayout(6,1));

    private JPanel inputPanel = new JPanel(new FlowLayout());

    private JLabel searchLabel = new JLabel("What would you like to search by?");

    private JButton searchNumButton = new JButton("Flight number");
    private JButton searchStateButton = new JButton("Departing/Current State");

    private JTextField searchInput;

    private int userInput;

     public View(App app) {

        this.app = app;
        
        try {
            app.Run();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "API Load Error: " + e.getMessage());
        }

    inputPanel.add(searchLabel);
    inputPanel.add(searchNumButton);
    inputPanel.add(searchStateButton);

    pane.add(inputPanel);

    frame.add(pane);
    frame.pack();
    frame.setVisible(true);

    searchNumButton.addActionListener(this);
    searchStateButton.addActionListener(this);
    
     }
      @Override
	public void actionPerformed(ActionEvent arg0) 
	{
         if(arg0.getSource() == searchNumButton) {

            inputPanel.remove(searchNumButton);
            inputPanel.remove(searchStateButton);

            JTextField searchInput = new JTextField();
            searchInput.setPreferredSize(new Dimension(150, 25));

            inputPanel.add(searchInput);

            searchLabel.setText("Enter the Flight Number: ");

            //Refreshes the GUI to display correctly
            inputPanel.revalidate();
            inputPanel.repaint();
            
            String flightNumber = searchInput.getText();
            App.GetAndDisplayFlightByNumber(app.getStateVectors(), flightNumber);
    
         }
         if(arg0.getSource() == searchStateButton) {

            inputPanel.remove(searchNumButton);
            inputPanel.remove(searchStateButton);

            JTextField searchInput = new JTextField();
            searchInput.setPreferredSize(new Dimension(150, 25));

            inputPanel.add(searchInput);

            searchLabel.setText("Enter a US state: ");

            //Refreshes the GUI to display correctly
            inputPanel.revalidate();
            inputPanel.repaint();

            String statename = searchInput.getText();
            
            App.GetAndDisplayFlightByState(statename, app.getStateVectors(), app.getActualStates());
    
         }
    }
}