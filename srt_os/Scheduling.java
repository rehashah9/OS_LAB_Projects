//REHA
package srt_os;

import java.util.ArrayList;
import java.util.Iterator;

class Scheduling {
  //function used to schedule the processes using SRT - time complexity: O(n^2)
  ArrayList<Pr_Gant> schedule(ArrayList<Process> ap) {
    // declaring variables
    ArrayList<Process> p_list = new ArrayList<>();
    ArrayList<Pr_Gant> order = new ArrayList<>();
    int current_time, min_r, next_a = 0, min_a, next_exists = 0;
    Process temp = null;
    Iterator i = ap.iterator();
    //storing all the process in temporary arraylist p_list
    while (i.hasNext()) {
      temp = (Process) i.next();
      p_list.add(temp);
    }
    //re-initializing all the remaining times and response times
    i = p_list.iterator();
    while (i.hasNext()) {
      temp = (Process) i.next();
      temp.remaining_time=temp.burst_time;
      temp.response_time=-1;
    }
    // going through all the processes to store earliest arrival time in current_time
    current_time=p_list.get(0).arrival_time; //initializing to index 0 process arrival time
    i = p_list.iterator();
    while (i.hasNext()) {
      temp = (Process) i.next();
      if (temp.arrival_time < current_time)
        current_time = temp.arrival_time;
    }
    // actual scheduling
    while (p_list.size() != 0) {
      // selecting the process to be done now
      i = p_list.iterator();
      min_r = 0;
      min_a = -1;
      next_exists = 0;
      while (i.hasNext()) {
        temp = (Process) i.next();
        // considering processes that have arrived till now
        if (temp.arrival_time <= current_time) {
          // taking process with shortest remaining time as current process
          if ((temp.remaining_time < min_r)||(min_r==0)) {
            min_r = temp.remaining_time;
            min_a = p_list.indexOf(temp);
          }
          // using process id as tie breaker
          else if (temp.remaining_time == min_r) {
            if (temp.process_id < p_list.get(min_a).process_id)
              min_a = p_list.indexOf(temp);
          }
        }
        // setting the remaining time of arrival of immediately next process as next_a
        else {
          next_exists = 1;
          if ((next_a == 0)
              || (((temp.arrival_time - current_time) < next_a) && ((temp.arrival_time - current_time) > 0))) {
            next_a = temp.arrival_time - current_time;
          }
        }
        // making next_a=0 if no more processes are left to arrive
        if (next_exists == 0) {
          next_a = 0;
        }
      }
      //checking if there even exists a process in queue at current_time
      if(min_a!=-1){
        //updating response time of current process if this is the first time it has been called
        if (p_list.get(min_a).response_time == -1) {
          p_list.get(min_a).response_time = current_time-p_list.get(min_a).arrival_time;
        }
        // removing current process from list if it finishes before next process enters
        if ((p_list.get(min_a).remaining_time <= next_a) || (next_a == 0)) {
          current_time += p_list.get(min_a).remaining_time;
          order.add(new Pr_Gant(p_list.get(min_a).process_id, p_list.get(min_a).remaining_time, current_time,0));
          p_list.get(min_a).set_ct(current_time); //updating ct,tat,wt,rt of process once its done
          p_list.remove(min_a);
        }
        // decreasing current process's remaining time till when the next process to arrive arrives
        else {
          current_time += next_a;
          p_list.get(min_a).remaining_time -= next_a;
          order.add(new Pr_Gant(p_list.get(min_a).process_id, next_a, current_time,p_list.get(min_a).remaining_time));
        }
      }
      //if no process in queue at current_time
      else
      {
        current_time+=next_a;
      }
    }
    //returning arraylist of gant chart
    return (order);
  }
}