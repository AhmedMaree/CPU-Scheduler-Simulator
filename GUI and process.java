import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
        import java.util.ArrayList;

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

            Process process = new Process(arrivalTime, burstTime, priority);

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

class Process implements Comparable<Process>{
    private float arrivalTime, burst, remainingTime, waitingTime, turnAroundTime;
    private int id, priority;

    public static int idCounter = 0;

    //Constuctors
    public Process(float arrivalTime, float burst, int priority) {
        this.arrivalTime = arrivalTime;
        this.burst = burst;
        this.priority = priority;
        this.remainingTime = burst;
    }

    public Process(float arrivalTime, float burst) {this(arrivalTime, burst, 0);}

    //Getters
    public float getArrivalTime() {return this.arrivalTime;}
    public float getBurst() {return this.burst;}
    public float getRemainingTime() {return this.remainingTime;}
    public float getWaitingTime() {return this.waitingTime;}
    public float getTurnAroundTime() {return this.turnAroundTime;}
    public int getId() {return this.id;}
    public int getPriority() {return this.priority;}

    //Setters
    public void setArrivalTime(float arrivalTime) {this.arrivalTime = arrivalTime;}
    public void setBurst(float burst) {this.burst = burst;}
    public void setRemainingTime(float remainingTime) {this.remainingTime = remainingTime;}
    public void setWaitingTime(float waitingTime) {this.waitingTime = waitingTime;}
    public void setTurnAroundTime(float turnAroundTime) {this.turnAroundTime = turnAroundTime;}
    public void setId(int id) {this.id = id;}
    public void setPriority(int priority) {this.priority = priority;}

    //Other methods
    public boolean isCompleted() {return this.remainingTime == 0;}

    public void execute(float time) {
        this.remainingTime -= time;
        if (this.remainingTime < 0) {
            this.remainingTime = 0;
        }
    }

    public int compareTo(Process other) {
        if (this.arrivalTime < other.arrivalTime) {
            return -1;
        } else if (this.arrivalTime > other.arrivalTime) {
            return 1;
        } else {
            return 0;
        }
    }

    public String toString() {
        return "Process " + this.id + " (Arrival Time: " + this.arrivalTime + ", Burst Time: " + this.burst + ", Priority: " + this.priority + ")";
    }
}
