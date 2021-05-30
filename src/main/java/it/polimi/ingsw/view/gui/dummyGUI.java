package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.panels.FaithTrackPanel;
import it.polimi.ingsw.view.gui.panels.LogoPanel;
import it.polimi.ingsw.view.gui.panels.NotifyPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class dummyGUI {

    public static void main (String[] args){
        /*
        /*
        //Login panel
        LogoPanel namePanel = new LogoPanel();
        JButton nameButton = new JButton();

        FaithTrackPanel faithTrack = new FaithTrackPanel();
        JButton trackButton = new JButton();


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Make Fullscreen


        trackButton.setBounds(100, 100, 50,50);
        nameButton.setBounds(100, 200, 50,50);
        frame.add(trackButton);
        frame.add(nameButton);
        frame.add(namePanel);
        frame.add(faithTrack);
        namePanel.setVisible(true);

        trackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                namePanel.setVisible(false);
                frame.add(faithTrack);
                faithTrack.setVisible(true);
            }
        });

        nameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                faithTrack.setVisible(false);
                frame.add(namePanel);
                namePanel.setVisible(true);
            }
        });

        //frame.pack();
        //frame.setLayout(null);
        frame.setVisible(true);
        */
        /*

        JPanel bigPanel = new JPanel();
        //LogoPanel logo = new LogoPanel();
        FaithTrackPanel track = new FaithTrackPanel();
        JButton nameButton = new JButton();
        JButton trackButton = new JButton();



        bigPanel.setBounds(0,0,1920-380,1080-230);
        bigPanel.setLayout(null);
        //bigPanel.setVisible(true);



        trackButton.setBounds(100, 100, 50,50);
        nameButton.setBounds(100, 200, 50,50);
        frame.add(trackButton);
        frame.add(nameButton);
        //frame.add(logo);
        //logo.setVisible(true);
        //logo.setBounds(0,0,1920-380,1080-230);
        track.setBounds(0,0,1920-380,1080-230);
       // bigPanel.add(logo);
        bigPanel.add(track);



        //trackButton.addActionListener(new ActionListener() {
        //    @Override
        //    public void actionPerformed(ActionEvent e) {
        //        GUI.switchPanels(bigPanel, track);
        //    }
        //});

        //nameButton.addActionListener(new ActionListener() {
        //    @Override
        //    public void actionPerformed(ActionEvent e) {
        //        GUI.switchPanels(bigPanel, logo);
        //    }
        //});

        frame.add(bigPanel);
        frame.setSize(1920-380, 1080-230);
        frame.setExtendedState(JFrame.MAXIMIZED_HORIZ);
        frame.setLayout(null);
        frame.setVisible(true);
        */


        /*
        // Create and set up a frame window
        JFrame frame = new JFrame("Gino");
        JPanel bigPanel = new JPanel();
        bigPanel.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Define new buttons
        JButton jb1 = new JButton("Button 1");
        JTextField jb2 = new JTextField("Button 2");
        JButton jb3 = new JButton("Button 3");


        // Define the panel to hold the buttons
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(jb1);
        panel.add(jb2);
        panel.add(jb3);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        JButton button = new JButton("Button4");
        panel2.add(button);

        jb3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.switchPanels(panel2);
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.switchPanels(panel);
            }
        });

        bigPanel.add(panel);
        // Set the window to be visible as the default to be false
        frame.add(bigPanel);
        frame.pack();
        frame.setVisible(true);

         */
        // Create and set up a frame window
        JFrame frame = new JFrame("Gino");
        JPanel bigPanel = new JPanel();
        bigPanel.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        NotifyPanel notifyPanel = new NotifyPanel();
        notifyPanel.appendMessage("Ciao");
        notifyPanel.appendMessage("Prova");
        bigPanel.add(notifyPanel);
        frame.add(bigPanel);
        frame.pack();
        frame.setVisible(true);

    }
}
