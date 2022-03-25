package ui.gui;


import model.DrugPlan;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DiabetesAppGUI extends JPanel implements ListSelectionListener {
    protected DrugPlan yuDrugPlan;
    protected JList<String> myDrugPlan;
    protected DefaultListModel<String> defaultListModel;

    private JFrame frame;
    private JPanel inputPanel;
    private JPanel bottomPanel;
    private JScrollPane outputPanel;

    private JButton addButton;
    protected JButton deleteButton;
    protected JButton clearButton;
    protected JTextField drugName;
    protected JTextField takeTime;
    private JLabel drugNameLabel;
    private JLabel takeTimeLabel;


    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    public static final String JSON_STORE = ".data/DrugPlanProject.json";

    private AddDrugListener addDrugListener;
    private DeleteDrugListener deleteDrugListener;
    private ClearListener clearListener;

    //constructor
    DiabetesAppGUI() {
        super(new BorderLayout());
        yuDrugPlan = new DrugPlan("yiyu's drug plan");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        defaultListModel = new DefaultListModel<>();

        createPictureFrame();
        addDrugList();
        setOutputPanel();
        addDrugNameLabel();
        addDrugNameTextField();
        addTakeTimeLabel();
        addTakeTimeLabelTextField();


        setAddButton();
        addClearButton();
        addDeleteButton();

        setInputPanel();
        setBottomPanel();


        createFrame();

    }

    //EFFECTS: create a window and display a picture and welcome label at the start.
    private void createPictureFrame() {
        JWindow startWindow = new JWindow();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Icon img = new ImageIcon("./data/n.png");
        JLabel label = new JLabel("Welcome To Diabetes App!");
        label.setIcon(img);
        label.setSize(500, 600);
        Font font = new Font("wR", Font.BOLD, 30);
        label.setFont(font);
        label.setForeground(new Color(0xE3CAEE));
        startWindow.getContentPane().add(label);
        startWindow.setBounds(((int) d.getWidth() - 722) / 2, ((int) d.getHeight() - 401) / 2, 722, 401);
        startWindow.setVisible(true);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startWindow.setVisible(false);
    }

    //MODIFIES: this
    //EFFECTS: create a frame that displays all the function.
    private void createFrame() {
        this.frame = new JFrame("Diabetes App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.add(outputPanel, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.PAGE_START);
        frame.add(bottomPanel, BorderLayout.PAGE_END);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        loadSaveWindows();


    }


    //MODIFIES: this
    //EFFECTS: place a scroll list on the output panel.
    public void setOutputPanel() {
        outputPanel = new JScrollPane(myDrugPlan);
        outputPanel.createVerticalScrollBar();

    }

    public void setInputPanel() {
        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.LINE_AXIS));
        inputPanel.setBackground(new Color(0x84D7E3));
       // frame.add(inputPanel,BorderLayout.PAGE_START);
       // inputPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        inputPanel.add(drugNameLabel);
        inputPanel.add(Box.createHorizontalStrut(5));
        inputPanel.add(drugName);
        inputPanel.add(Box.createHorizontalStrut(5));
        inputPanel.add(takeTimeLabel);
        inputPanel.add(Box.createHorizontalStrut(5));
        inputPanel.add(takeTime);
        inputPanel.add(Box.createHorizontalStrut(5));
        inputPanel.add(addButton);
        inputPanel.add(Box.createHorizontalStrut(5));
        inputPanel.add(deleteButton);

        inputPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        add(inputPanel,BorderLayout.PAGE_START);
      //  inputPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

    }

    private void setBottomPanel() {
        bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(0xA7E8D3));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        bottomPanel.setLayout(new BoxLayout(bottomPanel,BoxLayout.LINE_AXIS));
        add(bottomPanel, BorderLayout.PAGE_END);

        inputPanel.add(Box.createHorizontalStrut(5));
        bottomPanel.add(clearButton);
    }


    public void addDrugNameLabel() {
        drugNameLabel = new JLabel("Drug Name: ");
    }

    private void addDrugNameTextField() {
        drugName = new JTextField(4);
        drugName.addActionListener(addDrugListener);
        drugName.getDocument().addDocumentListener(addDrugListener);

    }

    private void addTakeTimeLabel() {
        takeTimeLabel = new JLabel("Take Time: ");
    }

    private void addTakeTimeLabelTextField() {
        takeTime = new JTextField(4);
        takeTime.addActionListener(addDrugListener);
        takeTime.getDocument().addDocumentListener(addDrugListener);

    }



    private void addDrugList() {
        myDrugPlan = new JList<>(defaultListModel);
        myDrugPlan.setSelectionMode((ListSelectionModel.SINGLE_SELECTION));
        myDrugPlan.setSelectedIndex(0);
        myDrugPlan.addListSelectionListener(this);
    }

    private void setAddButton() {
        addButton = new JButton("add");
        addDrugListener = new AddDrugListener(this, addButton);
        addListener(addDrugListener);

    }

    public AddDrugListener getAddDrugListener() {
        return addDrugListener;
    }

    public void addListener(AddDrugListener listener) {
        if (getAddDrugListener() != addDrugListener) {
            this.addDrugListener = listener;
            addDrugListener.setGUI(this);
        }
        addButton.setActionCommand("add");
        addButton.addActionListener(addDrugListener);
        addButton.setEnabled(true);

    }


    private void addClearButton() {
        clearButton = new JButton("clear");
        clearListener = new ClearListener(this);
        clearButton.setActionCommand("clear");
        clearButton.addActionListener(clearListener);
        clearButton.setEnabled(true);

    }

    private void addDeleteButton() {
        deleteButton = new JButton("delete");
        deleteDrugListener = new DeleteDrugListener(this);
        deleteButton.setActionCommand("delete");
        deleteButton.addActionListener(deleteDrugListener);

    }

    //MODIFIES: this
    //EFFECTS: create dialog when opening
    private void loadSaveWindows() {
        load();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                save();
            }
        });
    }

    // EFFECTS: Opens a pop-up window, if yes, saves data; otherwise, does nothing
    private void save() {
        int n = JOptionPane.showConfirmDialog(frame,
                "Do you want to save data to yiyu's drugplan?",
                "Save data?",
                JOptionPane.YES_NO_CANCEL_OPTION);
        if (n == 0) {
            try {
                jsonWriter.open();
                jsonWriter.write(yuDrugPlan);
                jsonWriter.close();
                System.out.println("Saved to " + JSON_STORE);
                frame.dispose();
                yuDrugPlan.toString();
            } catch (FileNotFoundException e) {
                System.out.println("Unable to write to file: " + JSON_STORE);
            }
        } else if (n == 1) {
            frame.dispose();
            yuDrugPlan.toString();
        }
    }

    // MODIFIES: this
    // EFFECTS: if yes, load data; otherwise, does nothing
    private void load() {
        int n = JOptionPane.showConfirmDialog(frame,
                "Do you want to load from yiyu's drugplan?",
                "Load data?",
                JOptionPane.YES_NO_OPTION);
        if (n == 0) {
            try {
                yuDrugPlan = jsonReader.read();
                for (String t: yuDrugPlan.viewDrugs()) {
                    defaultListModel.addElement(t);
                }
            } catch (IOException e) {
                System.out.println("Unable to read from file: " + JSON_STORE);
                yuDrugPlan = new DrugPlan(" ");
            }
        } else {
            yuDrugPlan = new DrugPlan(" ");
        }
    }




    //public DefaultListModel<String> getDefaultListModel() {
     //   return defaultListModel;
   // }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (myDrugPlan.getSelectedIndex() == -1) {
                deleteButton.setEnabled(false);


            } else {
                deleteButton.setEnabled(true);

            }
        }

    }
}
