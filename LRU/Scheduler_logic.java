class Scheduler
  {
    String ref_st; //current page reference string
    int size=0; //page capacity of cache
    int m; //number of pages referenced
    int hits; //number of hits
    int faults; //number of page faults
    String[][] table; //table storing page in memory record at each reference
    String hit_miss[]; //array storing hit and miss data of each referenced page
    //time complexity of scheduling:
    void lru_do(String s)
    {
      hits=0;
      faults=0;
      m=1;
      if(size==0)
      {
        //error: memory capacity has to be assigned at least once before scheduling
      }
      else
      {
        int i,n=0,flag,empty_flag;
        //counting number of pages referenced
        for(i=0;i<s.length();i++)
          {
            if(s.charAt(i)==' ')
            {
              m+=1;
            }
          }
        table = new String[m][size];
        hit_miss = new String[m];
        int score[] = new int[size]; //stores how recently a page in memory has been used
        String cur_mem[] = new String[size];
        for(i=0;i<size;i++)
          {
            score[i]=-1;
            cur_mem[i]="";
          }
        String temp;
        while(n<m)
          {
            ref_st="";
            temp=s.substring(0,1);
            while(temp.compareTo(" ")!=0)
              {
                ref_st+=temp;
                s=s.substring(1);
                if(s.compareTo("")==0)
                  temp=" ";
                else
                  temp=s.substring(0,1);
              }
            if(s.compareTo("")!=0)
              s=s.substring(1);
            flag=0;
            empty_flag=-1;
            for(i=0;i<size;i++)
              {
                if(cur_mem[i].compareTo(ref_st)==0)
                {
                  score[i]=0;
                  hits+=1;
                  hit_miss[n] = "hit";
                  flag=1;
                }
                else if(score[i]!=-1)
                {
                  score[i]++;
                }
                else if(empty_flag==-1)
                {
                  empty_flag=i;
                }
              }
            //if required page not in cache
            if(flag==0)
            {
              faults+=1;
              hit_miss[n] = "miss";
              //if no empty slot available - removing least used page
              if(empty_flag==-1)
              {
                flag=score[0];
                empty_flag=0;
                for(i=0;i<size;i++)
                  {
                    if(score[i]>flag)
                    {
                      flag=score[i];
                      empty_flag=i;
                    }
                  }
              }
              //adding referenced page to memory and making it the most recent page by making it score 0
              score[empty_flag]=0;
              cur_mem[empty_flag]=ref_st;
            }
            for(i=0;i<size;i++)
              {
               table[n][i]=cur_mem[i]; 
              }
            n++;
          }
      }
    }
    public static void main(String[] arg)
    {
      String rs = "1 2 3 4";
      Scheduler sc = new Scheduler();
      sc.size=2;
      sc.lru_do(rs);
      for(int i=0;i<sc.m;i++)
        {
          for(int j=0;j<sc.size;j++)
            {
              System.out.println(sc.table[i][j]);
            }
          System.out.println(sc.hit_miss[i]);
        }
      System.out.println(sc.hits+" "+sc.faults);
    }
  }