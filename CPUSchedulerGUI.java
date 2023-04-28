import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CPUSchedulerGUI extends JFrame implements ActionListener {
    private JLabel processLabel, arrivalLabel, burstLabel, priorityLabel, quantumLabel;
    private JTextField processField, arrivalField, burstField, priorityField, quantumField;
    private JButton addButton, runButton;
    private JList<Process> processList;
    private DefaultListModel<Process> listModel;
    private ArrayList<Process> processes;
    private TimelinePanel timelinePanel;

    public CPUSchedulerGUI() {
        setTitle("CPU Scheduler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));

        processLabel = new JLabel("Process Name:");
        processField = new JTextField();
        arrivalLabel = new JLabel("Arrival Time:");
        arrivalField = new JTextField();
        burstLabel = new JLabel("Burst Time:");
        burstField = new JTextField();
        priorityLabel = new JLabel("Priority:");
        priorityField = new JTextField();
        quantumLabel = new JLabel("Time Quantum:");
        quantumField = new JTextField();
        addButton = new JButton("Add Process");
        addButton.addActionListener(this);
        runButton = new JButton("Run Scheduler");
        runButton.addActionListener(this);

        inputPanel.add(processLabel);
        inputPanel.add(processField);
        inputPanel.add(arrivalLabel);
        inputPanel.add(arrivalField);
        inputPanel.add(burstLabel);
        inputPanel.add(burstField);
        inputPanel.add(priorityLabel);
        inputPanel.add(priorityField);
        inputPanel.add(quantumLabel);
        inputPanel.add(quantumField);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        processList = new JList<>(listModel);

        listPanel.add(new JScrollPane(processList), BorderLayout.CENTER);
        listPanel.add(addButton, BorderLayout.SOUTH);

        processes = new ArrayList<>();

        timelinePanel = new TimelinePanel();

        add(inputPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.WEST);
        add(timelinePanel, BorderLayout.CENTER);
        add(runButton, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String name = processField.getText();
            float arrivalTime = Float.parseFloat(arrivalField.getText());
            float burstTime = Float.parseFloat(burstField.getText());
            int priority = Integer.parseInt(priorityField.getText());

            Process process = new Process(name, arrivalTime, burstTime, priority);

            listModel.addElement(process);
            processes.add(process);

            processField.setText("");
            arrivalField.setText("");
            burstField.setText("");
            priorityField.setText("");
        }
        else if (e.getSource() == runButton) {
            float quantum = Float.parseFloat(quantumField.getText());
            CPUScheduler scheduler = new CPUScheduler(processes, quantum);
            scheduler.run();
            timelinePanel.updateTimeline(scheduler.getTimeline());
        }
    }

    public static void main(String[] args) {
        CPUSchedulerGUI gui = new CPUSchedulerGUI();
        gui.setVisible(true);
    }
}

class TimelinePanel extends JPanel {
    private ArrayList<TimeSlot> timeline;

    public TimelinePanel() {
        timeline = new ArrayList<>();
    }

    public void updateTimeline(ArrayList<TimeSlot> newTimeline) {
        timeline = newTimeline;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int y = 50;
        for (TimeSlot slot : timeline) {
            g.drawString(slot.getProcess().getName(), 50, y);
            g.fillRect((int)slot.getStartTime(), y - 20, (int)slot.getDuration(), 20);
            g.drawString(String.format("%.2f", slot.getStartTime()), (int)slot.getStartTime(), y + 10);
            g.drawString(String.format("%.2f", slot.getEndTime()), (int)slot.getEndTime() - 20, y + 10);
            y += 30;
        }
    }
}

class TimeSlot {
    private Process process;
    private float startTime, endTime;

    public TimeSlot(Process process, float startTime, float endTime) {
        this.process = process;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Process getProcess() {
        return process;
    }

    public float getStartTime() {
        return startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public float getDuration() {
        return endTime - startTime;
    }
}

class Process {
    private String name;
    private float arrivalTime, burstTime;
    private int priority;

    public Process(String name, float arrivalTime, float burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public float getArrivalTime() {
        return arrivalTime;
    }

    public float getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setBurstTime(int i) {
    }
}

class CPUScheduler {
    private ArrayList<Process> processes;
    private float quantum;
    private ArrayList<TimeSlot> timeline;

    public CPUScheduler(ArrayList<Process> processes, float quantum) {
        this.processes = processes;
        this.quantum = quantum;
        timeline = new ArrayList<>();
    }

    public ArrayList<TimeSlot> getTimeline() {
        return timeline;
    }

   public void run() {
        ArrayList<Process> queue1 = new ArrayList<>();
        ArrayList<Process> queue2 = new ArrayList<>();
        ArrayList<Process> queue3 = new ArrayList<>();
        ArrayList<Process> queue4 = new ArrayList<>();
        for (Process process : processes) {
            if (process.getPriority() == 1) {
                queue1.add(process);
            }
            else if (process.getPriority() == 2) {
                queue2.add(process);
            }
            else if (process.getPriority() == 3) {
                queue3.add(process);
            }
            else {
                queue4.add(process);
            }
        }
        runRR(queue1, 8);
        runSJF(queue2);
        runPriority(queue3);
        runFCFS(queue4);
    }

    private void runFCFS(ArrayList<Process> queue4) {
    }

    private void runPriority(ArrayList<Process> queue3) {
    }

    private void runRR(ArrayList<Process> queue, int timeSlice) {
        ArrayList<Process> remainingProcesses = new ArrayList<>(queue);
        float currentTime = 0;
        while (!remainingProcesses.isEmpty()) {
            boolean allDone = true;
            for (Process process : remainingProcesses) {
                if (process.getArrivalTime() <= currentTime) {
                    allDone = false;
                    break;
                }
            }
            if (allDone) {
                currentTime++;
                continue;
            }
            for (Process process : remainingProcesses) {
                if (process.getArrivalTime() <= currentTime) {
                    float quantumLeft = quantum;
                    while (quantumLeft > 0) {
                        if (process.getBurstTime() <= quantumLeft) {
                            timeline.add(new TimeSlot(process, currentTime, currentTime + process.getBurstTime()));
                            currentTime += process.getBurstTime();
                            quantumLeft -= process.getBurstTime();
                            process.setBurstTime(0);
                            break;
                        }
                        else {
                            timeline.add(new TimeSlot(process, currentTime, currentTime + quantumLeft));
                            currentTime += quantumLeft;
                            process.setBurstTime((int) (process.getBurstTime() - quantumLeft));
                            quantumLeft = 0;
                        }
                    }
                    if (process.getBurstTime() == 0) {
                        remainingProcesses.remove(process);
                    }
                }
            }
        }
    }

    private void runSJF(ArrayList<Process> queue) {
        ArrayList<Process> remainingProcesses = new ArrayList<>(queue);
        float currentTime = 0;
        while (!remainingProcesses.isEmpty()) {
            boolean allDone = true;
            for (Process process : remainingProcesses) {
                if (process.getArrivalTime() <= currentTime) {
                    allDone = false;
                    break;
                }
            }
            if (allDone) {
                currentTime++;
                continue;
            }}}}
            /*Process shortestProcess = null;
            for (Process process : remainingProcesses) {
                if (process.getArrivalTime() <= currentTime) {
                    if (shortestProcess == null || process.getBurst*/
