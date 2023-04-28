import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.lang.*;

public class ProcessGUI extends JFrame implements ActionListener {
    private JLabel arrivalLabel, burstLabel, priorityLabel;
    private JTextField arrivalField, burstField, priorityField;
    private JButton addButton;
    private JList<Process> processList;
    private DefaultListModel<Process> listModel;
    private ArrayList<Process> processes;

    public ProcessGUI() {
        setTitle("Process Scheduler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        arrivalLabel = new JLabel("Arrival Time:");
        arrivalField = new JTextField();
        burstLabel = new JLabel("Burst Time:");
        burstField = new JTextField();
        priorityLabel = new JLabel("Priority:");
        priorityField = new JTextField();
        addButton = new JButton("Add Process");
        addButton.addActionListener(this);

        inputPanel.add(arrivalLabel);
        inputPanel.add(arrivalField);
        inputPanel.add(burstLabel);
        inputPanel.add(burstField);
        inputPanel.add(priorityLabel);
        inputPanel.add(priorityField);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        processList = new JList<>(listModel);

        listPanel.add(new JScrollPane(processList), BorderLayout.CENTER);
        listPanel.add(addButton, BorderLayout.SOUTH);

        processes = new ArrayList<>();

        add(inputPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            float arrivalTime = Float.parseFloat(arrivalField.getText());
            float burstTime = Float.parseFloat(burstField.getText());
            int priority = Integer.parseInt(priorityField.getText());

            Process process = new Process() {
                @Override
                public OutputStream getOutputStream() {
                    return null;
                }

                @Override
                public InputStream getInputStream() {
                    return null;
                }

                @Override
                public InputStream getErrorStream() {
                    return null;
                }

                @Override
                public int waitFor() throws InterruptedException {
                    return 0;
                }

                @Override
                public int exitValue() {
                    return 0;
                }

                @Override
                public void destroy() {

                }
            };

            listModel.addElement(process);
            processes.add(process);

            arrivalField.setText("");
            burstField.setText("");
            priorityField.setText("");
        }
    }

    public static void main(String[] args) {
        ProcessGUI gui = new ProcessGUI();
        gui.setVisible(true);
    }
}
