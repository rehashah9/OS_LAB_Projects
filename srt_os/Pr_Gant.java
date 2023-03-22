//REHA
package srt_os;

//each instance stores a particular entry in the gant chart
class Pr_Gant
  {
    int process_id;
    int executed_time;
    int from;
    int till;
    int remaining_time;
    Pr_Gant(int a,int e,int t,int r)
    {
      process_id=a;
      executed_time=e;
      from=t-e;
      till=t;
      remaining_time=r;
    }
  }