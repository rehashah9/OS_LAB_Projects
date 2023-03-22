//REHA
package srt_os;

//each instance is an individual process in the CPU
class Process
  {
    String process_name;
    int process_id;
    int arrival_time;
    int burst_time;
    int remaining_time;
    int completion_time;
    int turnaround_time;
    int waiting_time;
    int response_time=-1;
    static int count=0;
    Process(String n,int a,int b)
    {
      process_name=n;
      arrival_time=a;
      burst_time=b;
      remaining_time=b;
      count+=1;
      process_id=count;
    }
    void set_ct(int c)
    {
      completion_time=c;
      turnaround_time=c-arrival_time;
      waiting_time=turnaround_time-burst_time;
    }
  }