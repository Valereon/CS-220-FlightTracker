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
    private JButton backButton = new JButton("Back");

    private JTextArea output = new JTextArea(10,40);

    private JScrollPane scroll = new JScrollPane(output);
    

    private JTextField searchInput;

    private int userInput;

     public View(App app) {

        this.app = app;
        app.setView(this);
        
        try {
            app.Run();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "API Load Error: " + e.getMessage());
        }

    inputPanel.add(searchLabel);
    inputPanel.add(searchNumButton);
    inputPanel.add(searchStateButton);

    pane.add(inputPanel);
    pane.add(scroll);

    frame.add(pane);
    frame.pack();
    frame.setVisible(true);

    searchNumButton.addActionListener(this);
    searchStateButton.addActionListener(this);
    backButton.addActionListener(this);
    
     }
      @Override
    /**
     * Makes the buttons search buttons and back buttons work
     * @param arg0 is the action event
     */
	public void actionPerformed(ActionEvent arg0) 
	{
         if(arg0.getSource() == searchNumButton) {

            inputPanel.remove(searchNumButton);
            inputPanel.remove(searchStateButton);


            JTextField searchInput = new JTextField();
            searchInput.setPreferredSize(new Dimension(150, 25));

            output.setText("");

            JButton submit = new JButton("Search");

            inputPanel.add(searchInput);
            inputPanel.add(submit);
            inputPanel.add(backButton);

            searchLabel.setText("Enter the Flight Number: ");

            //Refreshes the GUI to display correctly
            inputPanel.revalidate();
            inputPanel.repaint();

            //run this code when submit is pressed
            submit.addActionListener(e -> { //e is event
                String flightNumber = searchInput.getText();
                String result = app.GetAndDisplayFlightByNumber(app.getStateVectors(), flightNumber);
                displayInView(result);
            });
            
         }
         if(arg0.getSource() == searchStateButton) {

            inputPanel.remove(searchNumButton);
            inputPanel.remove(searchStateButton);

            JTextField searchInput = new JTextField();
            searchInput.setPreferredSize(new Dimension(150, 25));

            output.setText("");

            JButton submit = new JButton("Search");

            inputPanel.add(searchInput);
            inputPanel.add(submit);
            inputPanel.add(backButton);

            searchLabel.setText("Enter a US state: ");

            //Refreshes the GUI to display correctly
            inputPanel.revalidate();
            inputPanel.repaint();

            submit.addActionListener(e -> {
            String statename = searchInput.getText();
            String result = app.GetAndDisplayFlightByState(statename, app.getStateVectors(), app.getActualStates());
            displayInView(result);
            });
         }
         if(arg0.getSource() == backButton) {
            inputPanel.removeAll();
            inputPanel.add(searchLabel);
            inputPanel.add(searchNumButton);
            inputPanel.add(searchStateButton);

            searchLabel.setText("What would you like to search by?");

            inputPanel.revalidate();
            inputPanel.repaint();
         }

    }
    /**
     * Displays the strings from methods in App to display in the GUI
     * @param text
     */
    public void displayInView(String text) {
    output.append(text + "\n");
}
}