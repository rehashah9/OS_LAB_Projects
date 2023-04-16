//VRUNDA
package srt_os;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

class FrontEndPage1 implements ActionListener {
  private static JTable table;
  DefaultTableModel model;
  JFrame frame = new JFrame("CPU Scheduling using SRTN");
  JPanel titlepanel = new JPanel();
  JButton addButton = new JButton("Add");
  JButton delButton = new JButton("Delete");
  JButton gant_chart = new JButton("See Gant Chart and other statistics");
  JButton reloadButton = new JButton("Reset and Clear");
  JLabel titleLabel = new JLabel("CPU Scheduling using SRTN Simulator");
  JLabel addLabel = new JLabel("To add process to scheduling:");
  JLabel delLabel = new JLabel("To remove process from scheduling:");
  JLabel process_nameLabel = new JLabel("Process Name (max. 25 characters): ");
  JLabel a_tLabel = new JLabel("Arival Time (in cycles): ");
  JLabel b_tLabel = new JLabel("CPU Burst Time (in cycles): ");
  JLabel id_tLabel = new JLabel("Process ID: ");
  JTextField process_nameField = new JTextField();
  JLabel noteLabel = new JLabel(
      "NOTE: Unit of all time measurements is a CPU cycle - time taken by the CPU to finish 1 cycle of execution.");
  JTextField a_tField = new JTextField();
  JTextField b_tField = new JTextField();
  JTextField id_tField = new JTextField();
  Object[][] data = {};
  ArrayList<Process> p_list = new ArrayList<>();
  ArrayList<Pr_Gant> gant_list = new ArrayList<>();
  Scheduling sc = new Scheduling();

  FrontEndPage1() {
    // loading data from database to ArrayLists
    p_list = DBHandling.readProcess();
    gant_list = DBHandling.readGant();
    // start of GUI
    frame.setSize(1200, 800);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(null);
    frame.getContentPane().setBackground(new Color(200, 255, 255));

    // Creating the table
    String[] columnNames = { "Process_Id", "Process_Name", "Arival_Time", "CPU_burst_Time", "Completion_Time",
        "Turn_Around_Time", "Waiting_Time", "Response_Time" };
    model = new DefaultTableModel(data, columnNames);
    table = new JTable(model);
    Process p;
    Iterator i = p_list.iterator();
    while (i.hasNext()) {
      p = (Process) i.next();
      model.addRow(new Object[] { p.process_id, p.process_name, p.arrival_time, p.burst_time, p.completion_time,
          p.turnaround_time, p.waiting_time, p.response_time });
    }
    JScrollPane scrollPane = new JScrollPane(table);
    // creating, setting bounds and adding all the components
    titlepanel.setBackground(new Color(140, 140, 140));
    titleLabel.setFont(new Font("Serif", Font.PLAIN, 34));
    delButton.addActionListener(this);
    addButton.addActionListener(this);
    gant_chart.addActionListener(this);
    reloadButton.addActionListener(this);
    titlepanel.add(titleLabel);
    frame.add(titlepanel);
    frame.add(addLabel);
    frame.add(process_nameLabel);
    frame.add(process_nameField);
    frame.add(a_tLabel);
    frame.add(a_tField);
    frame.add(b_tLabel);
    frame.add(b_tField);
    frame.add(addButton);
    frame.add(scrollPane);
    frame.add(delLabel);
    frame.add(id_tLabel);
    frame.add(id_tField);
    frame.add(delButton);
    frame.add(reloadButton);
    frame.add(gant_chart);
    frame.add(noteLabel);
    titlepanel.setBounds(25, 25, 850, 100);
    scrollPane.setBounds(25, 150, 850, 400);
    gant_chart.setBounds(525, 575, 250, 50);
    reloadButton.setBounds(25, 575, 200, 50);
    addLabel.setBounds(925, 25, 300, 50);
    process_nameLabel.setBounds(925, 90, 180, 30);
    a_tLabel.setBounds(925, 130, 180, 30);
    b_tLabel.setBounds(925, 170, 180, 30);
    process_nameField.setBounds(1125, 90, 200, 30);
    a_tField.setBounds(1125, 130, 200, 30);
    b_tField.setBounds(1125, 170, 200, 30);
    addButton.setBounds(1125, 215, 75, 50);
    delLabel.setBounds(925, 290, 300, 50);
    id_tLabel.setBounds(925, 355, 100, 30);
    id_tField.setBounds(1125, 355, 200, 30);
    delButton.setBounds(1125, 400, 75, 50);
    noteLabel.setBounds(25, 650, 700, 30);
    frame.pack();
    // inserting updated data into database before program closes - lazy shifting
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        DBHandling.writeProcess(p_list);
        DBHandling.writeGant(gant_list);
      }
    });
  }

  // REHA
  public void actionPerformed(ActionEvent e) {
    // if delete button is pressed
    if (e.getSource() == delButton) {
      String del_id = id_tField.getText().trim();
      // if no data in textfield
      if (del_id.equals("")) {
        // TANYA
        JFrame frame = new JFrame();
        JLabel label = new JLabel("All fields are compulsory", JLabel.CENTER);
        label.setBounds(80, 0, 150, 100);
        frame.add(label);
        frame.setTitle("Invalid");
        frame.setSize(320, 180);
        frame.setLayout(null);
        frame.setVisible(true);

      } else {
        // actual functionality
        try {
          int id = Integer.parseInt(del_id), flag = 0;
          Process p;
          Iterator i = p_list.iterator();
          // deleting process with given process id
          while (i.hasNext()) {
            p = (Process) i.next();
            if (p.process_id == id) {
              p_list.remove(p);
              flag = 1;
              break;
            }
          }
          // if given process id actually existed
          if (flag == 1) {
            gant_list = sc.schedule(p_list);
            model.setRowCount(0);
            i = p_list.iterator();
            while (i.hasNext()) {
              p = (Process) i.next();
              model.addRow(new Object[] { p.process_id, p.process_name, p.arrival_time, p.burst_time, p.completion_time,
                  p.turnaround_time, p.waiting_time, p.response_time });
            }
          }
          // if given process id didn't exist as is
          else {
            // TANYA
            JFrame frame = new JFrame();
            JLabel label = new JLabel("Process id doesn't exist", JLabel.CENTER);
            label.setBounds(60, 0, 180, 100);
            frame.add(label);
            frame.setTitle("Invalid");
            frame.setSize(320, 180);
            frame.setLayout(null);
            frame.setVisible(true);
          }
          id_tField.setText("");
        }
        // if data in text field is not int
        catch (Exception ex) {
          // TANYA
          JFrame frame = new JFrame();
          JLabel label = new JLabel("Process id should be integer", JLabel.CENTER);
          label.setBounds(60, 0, 180, 100);
          frame.add(label);
          frame.setTitle("Invalid");
          frame.setSize(320, 180);
          frame.setLayout(null);
          frame.setVisible(true);

        }
      }
    }
    // if add button clicked
    else if (e.getSource() == addButton) {
      String pn = process_nameField.getText().trim();
      String at = a_tField.getText().trim();
      String bt = b_tField.getText().trim();
      // if data not given in any field
      if ((pn.equals("")) || (at.equals("")) || (bt.equals(""))) {
        // TANYA
        JFrame frame = new JFrame();
        JLabel label = new JLabel("All fields are compulsory", JLabel.CENTER);
        label.setBounds(80, 0, 150, 100);
        frame.add(label);
        frame.setTitle("Invalid");
        frame.setSize(320, 180);
        frame.setLayout(null);
        frame.setVisible(true);

      } else {
        // actual functionality
        try {
          int ar = Integer.parseInt(at);
          int bu = Integer.parseInt(bt);
          //if arrival and burst time are positive
          if((ar>=0)&&(bu>0))
          {
            Process p = new Process(pn, ar, bu);
            p_list.add(p);
            gant_list = sc.schedule(p_list);
            model.setRowCount(0);
            Iterator i = p_list.iterator();
            while (i.hasNext()) {
              p = (Process) i.next();
              model.addRow(new Object[] { p.process_id, p.process_name, p.arrival_time, p.burst_time, p.completion_time,
                  p.turnaround_time, p.waiting_time, p.response_time });
            }
            a_tField.setText("");
            b_tField.setText("");
            process_nameField.setText("");
          }
          else
          {
            // TANYA
            JFrame frame = new JFrame();
            JLabel label = new JLabel("Arrival time should be non-negative and Burst time should be positive", JLabel.CENTER);
            label.setBounds(40, 0, 240, 100);
            frame.add(label);
            frame.setTitle("Invalid");
            frame.setSize(320, 180);
            frame.setLayout(null);
            frame.setVisible(true);
          }
        }
        // if given arrival or burst time is not integer
        catch (Exception ex) {
          // TANYA
          JFrame frame = new JFrame();
          JLabel label = new JLabel("Arrival and Burst time should be integer", JLabel.CENTER);
          label.setBounds(40, 0, 240, 100);
          frame.add(label);
          frame.setTitle("Invalid");
          frame.setSize(320, 180);
          frame.setLayout(null);
          frame.setVisible(true);

        }
      }
    }
    // button to show gant chart and average values
    else if (e.getSource() == gant_chart) {
      double avgCT = 0, avgTAT = 0, avgWT = 0, avgRT = 0;
      Iterator i = p_list.iterator();
      Process temp;
      // calculating averages
      while (i.hasNext()) {
        temp = (Process) i.next();
        avgCT += temp.completion_time;
        avgTAT += temp.turnaround_time;
        avgWT += temp.waiting_time;
        avgRT += temp.response_time;
      }
      avgCT /= (double) p_list.size();
      avgTAT /= (double) p_list.size();
      avgWT /= (double) p_list.size();
      avgRT /= (double) p_list.size();
      // gant chart page
      FrontEndPage2 gant_page = new FrontEndPage2(gant_list, avgCT, avgTAT, avgWT, avgRT);
    }
    // button to reset all the data
    else if (e.getSource() == reloadButton) {
      // clearing the table and both the lists
      model.setRowCount(0);
      p_list.clear();
      gant_list.clear();
    }
  }

}