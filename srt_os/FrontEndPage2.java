//VRUNDA 
package srt_os;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FrontEndPage2
{
  JFrame frame = new JFrame("Gant Chart and other statistics");
  private static JTable table;
  JTable gant_chart;
  DefaultTableModel model;
  DefaultTableModel gant_model;
  JPanel title = new JPanel();
  JLabel titlel = new JLabel("Gant Chart and other statistics");
  JLabel avgct;
  JLabel avgtat;
  JLabel avgwt;
  JLabel avgrt;
  JLabel time_com;
  JLabel noteLabel = new JLabel("NOTE: Unit of all time measurements is a CPU cycle - time taken by the CPU to finish 1 cycle of execution.");
  FrontEndPage2(ArrayList<Pr_Gant> arg,double avg_ct,double avg_tat,double avg_wt,double avg_rt) 
  {
      //initial GUI commands
      frame.setVisible(true);
    frame.setLayout(null);
    frame.setSize(1200,800);
    //average time display labels
    avgct = new JLabel("Average Completion Time (in cycles): "+avg_ct);
    avgtat = new JLabel("Average Turnaround Time (in cycles): "+avg_tat);
    avgwt = new JLabel("Average Waiting Time (in cycles): "+avg_wt);
    avgrt = new JLabel("Average Response Time (in cycles): "+avg_rt);
    time_com = new JLabel("Time Complexity of SRTF is O(n^2)");
    //gant chart table creation
    String[] columnNames = { "Process_id", "Executed_Time", "From(Starting time)", "Till(End)", "Remaining_Time" };
    Object[][] data = {};
    model = new DefaultTableModel(data, columnNames);
    table = new JTable(model);
    JScrollPane scrollPane = new JScrollPane(table);
    //gant chart creation
    String[] col_gant = {};
    Object[][] data_gant = {};
    gant_model = new DefaultTableModel(data_gant,col_gant);
    gant_chart = new JTable(gant_model);
    JScrollPane gant_scroll = new JScrollPane(gant_chart);
    gant_scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    //adding data into table and gant chart from arraylist
    Pr_Gant g;
    Iterator i = arg.iterator();
    int cur_t=0,col_no=1;
    gant_model.addColumn("process id: ");
    while(i.hasNext())
    {
      g=(Pr_Gant)i.next();
      if(g.from>cur_t)
      {
        gant_model.addColumn("idle");
        col_no++;
        model.addRow(new Object[] {"CPU idle",g.from-cur_t,cur_t,g.from,"-"});
      }
      gant_model.addColumn(g.process_id+"");
      col_no++;
      model.addRow(new Object[] {g.process_id, g.executed_time, g.from, g.till,g.remaining_time});
      cur_t=g.till;
    }
    Object[] gant_row = new Object[col_no];
    gant_row[0]="cycles: ";
    col_no=1;
    cur_t=0;
    i = arg.iterator();
    while(i.hasNext())
    {
      g=(Pr_Gant)i.next();
      if(g.from>cur_t)
      {
        gant_row[col_no]=Integer.toString(cur_t)+"-"+Integer.toString(g.from);
        col_no++;
      }
      gant_row[col_no]=Integer.toString(g.from)+"-"+Integer.toString(g.till);
      col_no++;
      cur_t=g.till;
    }
    gant_model.addRow(gant_row);
    //other components defined, bounded and added
    title.setLayout(new GridLayout(1, 1));
    title.setBackground(new Color(140,140,140));
    titlel.setFont(new Font("Serif", Font.PLAIN, 34));
    title.add(titlel);
    frame.add(title);
    frame.add(scrollPane);
    frame.add(avgct);
    frame.add(avgtat);
    frame.add(avgwt);
    frame.add(gant_scroll);
    frame.add(avgrt);
    frame.add(time_com);
    frame.add(noteLabel);
    title.setBounds(25,25,1150,100);
    avgct.setBounds(850,150,250,30);
    avgtat.setBounds(850,195,250,30);
    avgwt.setBounds(850,240,250,30);
    avgrt.setBounds(850,285,250,30);
    time_com.setBounds(850,330,250,30);
    scrollPane.setBounds(25,150,800,300);
    gant_scroll.setBounds(25,475,1150,100);
    noteLabel.setBounds(25,650,700,30);
    frame.pack();
     frame.getContentPane().setBackground(new Color(200,255,255));
     
  }
}
