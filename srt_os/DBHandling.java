//ASTHA
package srt_os;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;
class DBHandling
  {
    //function to load process table data from database
    static ArrayList<Process> readProcess()
    {
      ArrayList<Process> array = new ArrayList<>();  //list to store data
      try
      {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/srtn","root","");
        Statement stmt=con.createStatement();
        ResultSet res=stmt.executeQuery("select * from process");
        Process temp;
        //iterating through result set and adding each proces in list
        while(res.next())
          {
            temp = new Process("a",0,0);
            temp.process_id=res.getInt(2);
            temp.process_name=res.getString(1);
            temp.arrival_time=res.getInt(3);
            temp.burst_time=res.getInt(4);
            temp.remaining_time=res.getInt(5);
            temp.completion_time=res.getInt(6);
            temp.turnaround_time=res.getInt(7);
            temp.waiting_time=res.getInt(8); 
            temp.response_time=res.getInt(9);
            temp.count=temp.process_id;
            array.add(temp);
          }
      con.close();
      }
      catch(Exception ex)
       {}
      return(array);
    }

    //function to load gant chart data from database
    static ArrayList<Pr_Gant> readGant()
    {
      ArrayList<Pr_Gant> array = new ArrayList<>();
      try
      {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/srtn","root","");
        Statement stmt=con.createStatement();
        ResultSet res=stmt.executeQuery("select * from gangtable");
        Pr_Gant temp;
        //iterating through result set and adding each gant chart entry in list
        while(res.next())
          {
            temp = new Pr_Gant(res.getInt(1),res.getInt(2),res.getInt(4),res.getInt(5));
            array.add(temp);
          }
        con.close();
      }
      catch(Exception ex){}
      return(array);
    }
    //function to put data about the processes into database
    static void writeProcess(ArrayList<Process> arp)
    {
      try
      {
        System.out.println("1");
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("2");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/srtn","root","");
        System.out.println("3");
        Statement stmt=con.createStatement();
        System.out.println("4");
        stmt.executeUpdate("delete from process;");
        System.out.println("5");
        Process temp;
        System.out.println("6");
        Iterator i = arp.iterator();
        System.out.println("7");
        while(i.hasNext())
        {
          temp=(Process)i.next();
          System.out.println(temp);
          Class.forName("com.mysql.jdbc.Driver");
          con=DriverManager.getConnection("jdbc:mysql://localhost:3306/srtn","root","");
          stmt=con.createStatement();
          stmt.executeUpdate("INSERT INTO process VALUES ("+temp.process_id+",'"+temp.process_name+"',"+temp.arrival_time+","+temp.burst_time+","+temp.remaining_time+","+temp.completion_time+","+temp.turnaround_time+","+temp.waiting_time+","+temp.response_time+");");
          System.out.println("Process inserted succesfully");
        }
        con.close();
      }
      catch(Exception ex){
		  ex.printStackTrace();
	  }
    }
    //function to put data about gant chart into database
    static void writeGant(ArrayList<Pr_Gant> arg)
    {
      try
      {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/srtn","root","");
        Statement stmt=con.createStatement();
        stmt.executeUpdate("delete from gangtable;");
        Pr_Gant temp;
        Iterator i = arg.iterator();
        while(i.hasNext())
        {
          temp=(Pr_Gant)i.next();
          System.out.println(temp);
          Class.forName("com.mysql.jdbc.Driver");
          con=DriverManager.getConnection("jdbc:mysql://localhost:3306/srtn","root","");
          stmt=con.createStatement();
          stmt.executeUpdate("INSERT INTO gangtable VALUES ("+temp.process_id+","+temp.executed_time+","+temp.from+","+temp.till+","+temp.remaining_time+");");
        }
        con.close();
      }
      catch(Exception ex){}
    }
  }