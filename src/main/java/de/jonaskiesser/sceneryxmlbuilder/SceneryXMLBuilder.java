package de.jonaskiesser.sceneryxmlbuilder;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SceneryXMLBuilder extends JFrame implements ActionListener {
    
    JPanel panel;
    JTextField nameTextField, latlonTextField, altTextField;
    JTextArea resultTextArea;
    JLabel nameLabel, latlonLabel, altLabel, typeLabel;
    JButton buttonSave, buttonExport;
    JComboBox comboBoxTypeSelector;
    ArrayList<LandmarkLocation> landmarkLocationList;
    JFileChooser fileChooser;
    JScrollPane scrollPane;
    
    public SceneryXMLBuilder() {
        
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SceneryXMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(SceneryXMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SceneryXMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(SceneryXMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
        this.setTitle("SceneryXMLBuilder");
        
        GridLayout g = new GridLayout(0, 1, 0, 0);
        g.setVgap(5);
        panel = new JPanel(g);
        panel.setBorder(BorderFactory.createLineBorder(panel.getBackground(), 20));

        //Label Name ini
        nameLabel = new JLabel();
        nameLabel.setText("Name:");
        panel.add(nameLabel);

        //Textfield Name ini
        nameTextField = new JTextField(15);
        nameTextField.setToolTipText("Name");
        panel.add(nameTextField);

        //Label LatLon ini
        latlonLabel = new JLabel();
        latlonLabel.setText("Lat Lon:");
        panel.add(latlonLabel);

        //Textfield LatLon ini
        latlonTextField = new JTextField(15);
        latlonTextField.setToolTipText("LatLon");
        panel.add(latlonTextField);

        //Label Alt ini
        altLabel = new JLabel();
        altLabel.setText("Alt:");
        panel.add(altLabel);

        //Textfield Alt ini
        altTextField = new JTextField(4);
        altTextField.setToolTipText("Altitude");
        panel.add(altTextField);

        //Label Type ini
        typeLabel = new JLabel();
        typeLabel.setText("Type:");
        panel.add(typeLabel);

        //new stuff
        String[] comboBoxList = {"POI", "TEST2", "TEST3", "TEST4"};
        comboBoxTypeSelector = new JComboBox(comboBoxList);
        panel.add(comboBoxTypeSelector);
        
        buttonSave = new JButton("Save");
        buttonSave.addActionListener(this);
        panel.add(buttonSave);

        //TextArea Result ini
        resultTextArea = new JTextArea(40, 9);
        resultTextArea.setEditable(false);

        //SrollPane ini
        scrollPane = new JScrollPane(resultTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane);

        //Button Export ini
        buttonExport = new JButton("Export");
        buttonExport.addActionListener(this);
        panel.add(buttonExport);

        //Path chooser
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        this.add(panel, "Center");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        landmarkLocationList = new ArrayList();
    }
    
    public static void main(String[] args) {
        SceneryXMLBuilder sxb = new SceneryXMLBuilder();
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.buttonSave) {
            
            createResult();
            
        }
        
        if (e.getSource() == this.buttonExport) {
            if (!landmarkLocationList.isEmpty()) {
                createResult();
                exportResult(createStringFromList());
            } else {
                JOptionPane.showMessageDialog(this, "No entrys to export");
            }
        }
    }
    
    public void createResult() {
        
        String lat, lon;
        
        String name = nameTextField.getText();
        String latlon = latlonTextField.getText();
        
        String[] latlonarray = latlon.split(",");
        
        String alt = altTextField.getText();
        
        if (alt.trim().isEmpty()) {
            alt = "0";
        }
        
        try {
            lat = latlonarray[0].trim();
            lon = latlonarray[1].trim();
            landmarkLocationList.add(new LandmarkLocation((String) comboBoxTypeSelector.getSelectedItem(), name, lat, lon, alt));
            nameTextField.setText("");
            latlonTextField.setText("");
            altTextField.setText("");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Not a valid format!");
            System.err.println("Not a valid Lat Lon format. Try like this: \"33.70233999204387, -84.3954496633918\"");
            return;
            
        }
        
        resultTextArea.setText(createStringFromList());
        
    }
    
    private String createStringFromList() {
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < landmarkLocationList.size(); i++) {
            sb.append(landmarkLocationList.get(i).toXML()).append("\n");
        }
        
        StringSelection stringSelection = new StringSelection(sb.toString().trim());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        
        return sb.toString();
    }
    
    private void exportResult(String s) {
        
        int option = fileChooser.showSaveDialog(panel);
        if (option == JFileChooser.APPROVE_OPTION) {
            
            File file = fileChooser.getSelectedFile();
            System.out.println(file.getAbsolutePath());
            
            try {
                System.out.println(file.getAbsolutePath() + "\\output.xml");
                OutputStream outputStream = new FileOutputStream(file.getAbsolutePath() + "\\output.xml");
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                outputStreamWriter.write(s);
                outputStreamWriter.close();
                JOptionPane.showMessageDialog(this, "File Exported!");
            } catch (FileNotFoundException ex) {
                System.out.println("FileNotFound");
            } catch (IOException ex) {
                System.out.println("IOException ex" + ex.getMessage());
            }
        }
        
    }
    
}
