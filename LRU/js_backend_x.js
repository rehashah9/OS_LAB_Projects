var ref_st = null; //current page reference string
var size = 0; //page capacity of cache
var m = 0; //number of pages referenced
var page_order = []; //array containing requested pages
var hits = 0; //number of hits
var faults = 0; //number of page faults
var table = []; //table storing page in memory record at each reference
var hit_miss = []; //array storing hit and miss data of each referenced page
//function to take user inputted question and display appropriate table
function try_answer()
{
  //getting refrence string and memory size value from user input and striping trailing and beginning spaces
  var ref_st = (document.getElementById('rs').value).trim();
  size_str = (document.getElementById('tsim').value).trim();
  //checking if size input is not int
  if((isNaN(size_str)==true)||(size_str==""))
  {
    document.getElementById('tsim').placeholder="value has to be integer!";
    document.getElementById('tsim').value="";
  }
  //if size is integer
  else
  {
    size=parseInt(size_str);
    if (size <= 0)
    {
      //error: memory capacity has to be positive
      document.getElementById('tsim').placeholder='value has to be a positive integer!';
      document.getElementById('tsim').value='';
    }
    //if size is positive
    else
    {
      if (ref_st.localeCompare("")==0)
      {
        //error: need to refer to at least one page to use lru algorithm
        document.getElementById('rs').placeholder='value cannot be an empty string!';
        document.getElementById('rs').value='';
      }
      //if size is proper
      else
      {
        lru_do(ref_st); //lru algorithm
        //rendering table for user input
        empty_table();
        //clearing answer input fields
        document.getElementById('nhit').value="";
        document.getElementById('nhit').style.backgroundColor="";
        document.getElementById('nmiss').value="";
        document.getElementById('nmiss').style.backgroundColor="";
        document.getElementById('hitr').value="";
        document.getElementById('hitr').style.backgroundColor="";
        document.getElementById('missr').value="";
        document.getElementById('missr').style.backgroundColor="";
        document.getElementById('correct').value="";
        document.getElementById('correct').style.backgroundColor="";
        document.getElementById('incorrect').value="";
        document.getElementById('incorrect').style.backgroundColor="";
      }
    }
  }
}
//displaying empty table to be filled by user
function empty_table()
{
  html_table = document.getElementById('table_mem'); //html table element
  html_table.innerHTML = ''; //clearing table
  var i;
  var j;
  var cell_width;
  //row for reference string values
  var ref_row = html_table.insertRow(-1);
  var label_cell = ref_row.insertCell(-1);
  label_cell.setHTML("Reference String: ");
  label_cell.style.fontWeight = 'bold';
  //adding cells in the row to show each referenced page seperately
  for(i=0;i<m;i++)
  {
    var j_cell = ref_row.insertCell(-1);
    j_cell.setHTML(page_order[i]);
    j_cell.style.fontWeight = 'bold';
  }
  //displaying actual values from variable table
  for(i=0;i<size;i++)
  {
    //creating a new row for each slot
    var new_row = html_table.insertRow(-1);
    var mem_label_cell = new_row.insertCell(-1);
    var st = "slot "+String(i)+": ";
    mem_label_cell.setHTML(st);
    mem_label_cell.style.fontWeight = 'bold';
    cell_width = mem_label_cell.offsetWidth;
    //for each slot adding cells to show page in the slot in each status of memory until all referenced pages are done
    for(j=0;j<m;j++)
    {
      var jth_cell = new_row.insertCell(-1);
      jth_cell.innerHTML='<input style="font-size: 17px; border:2px solid gray; border-radius: 6px; font-family: "Ubuntu", sans-serif; width: 100%" size="1" type="text">';
      
    }
  }
}
//calculating answer by doing the algorithm
function lru_do(s)
{
  hits = 0;
  faults = 0;
  m = 1;
  //initializing some variables
  var i = 0;
  var n = 0;
  var temp = null;
  var flag = 0;
  var empty_flag = 0;
  //counting number of pages referenced
  for (i = 0; i < s.length; i++)
  {
    if ((s.charAt(i) == ' ') && (s.charAt(i-1)!=' '))
      m += 1; 
  }
  //initializing arrays that need m or size values
  table = Array(m).fill(null).map(()=>new Array(size).fill(null));
  hit_miss = Array(m).fill(null);
  page_order = Array(m).fill(null);
  var score = Array(size).fill(0); //stores how recently a page in memory has been used
  var cur_mem = Array(size).fill(null); //stores current status of memory
  //initializing score and cur_mem arrays to default values
  for (i = 0; i < size; i++)
  {
    score[i] = -1;
    cur_mem[i] = "-";
  }
  //actual alog - runs for m times i.e. for each referenced page
  while (n < m)
  {
    ref_st = "";
    //storing data from reference string upto 'space' in ref_st
    temp = s.substring(0,1); //stores current 0 index value in reference string - s
    while (temp.localeCompare(" ") != 0)
    {
      ref_st += temp; //apending non-space character to ref_st
      s = s.substring(1); //deleteing first character from s  as it is stored in temp
      //checking if s is empty
      if (s.localeCompare("") == 0)
          temp = " "; //getting out of while loop
      else
          temp = s.substring(0,1); //going to next character in s
    }
    //removing 'space' from front and end in s
    s = s.trim();
    page_order[n]=""+ref_st;
    flag = 0; //indicates whether page in memory currently or not
    empty_flag = -1; //to store slot number i.e. index of cur_mem where we will put ref_st if it isn't in memory
    //checking in cur_mem if the referenced page is there or not
    for (i = 0; i < size; i++)
    {
      //if ith index is referenced page
      if (cur_mem[i].localeCompare(ref_st) == 0)
      {
        score[i] = 0;
        hits += 1; //incrementing hit count
        hit_miss[n] = "hit";
        flag = 1;
      }
      //if ith index is not empty slot and not referenced page
      else if (score[i] != -1)
        score[i]++;
      //if ith index is empty slot
      else if (empty_flag == -1)
        empty_flag = i;
    }
    //if required page not in cache
    if (flag == 0)
    {
      faults += 1;
      hit_miss[n] = "miss";
      //if no empty slot available - removing least used page
      if (empty_flag == -1)
      {
        //initializing flag and empty_flag to use index 0 page as  to be replaced
        flag = score[0];
        empty_flag = 0;
        //using flag as a variable to store max score
        for (i = 0; i < size; i++)
        {
          //if page with higher score found
          if (score[i] > flag)
          {
            flag = score[i]; //updating flag to new page score
            empty_flag = i; //putting index of this page to empty_flag
          }
        }
      }
      //adding referenced page to memory and making it the most recent page by making it score 0
      score[empty_flag] = 0;
      cur_mem[empty_flag] = ref_st; //putting ref_st in the index stored in empty_flag
    }
    //storing the nth state of memory or cur_mem to table
    for (i = 0; i < size; i++)
    {
      table[n][i] = cur_mem[i];
    }
    //incrementing count of number pages requests executed
    n++;
  }
}
//function to check user inputted solution
function check_user()
{
  //html table element
  html_table = document.getElementById('table_mem');
  if((html_table.innerHTML).localeCompare("<thead></thead><tbody><tr><td></td></tr></tbody>")!=0)
  {
    //required variables
    var input_int = 0;  //input value in integer form
    var right = 0;  //no of right answers
    var wrong = 0;  //no of wrong answers
    var i = 0;  //iterator
    var j = 0;  //iterator
    var cell_value = "";
    //getting user answers for the statistic values
    var hit_a = (document.getElementById('nhit').value).trim();
    var miss_a = (document.getElementById('nmiss').value).trim();
    var hitrate_a = (document.getElementById('hitr').value).trim();
    var missrate_a = (document.getElementById('missr').value).trim();
    //checking values in the table
    for(i = 0;i < size;i++)
    {
      for(j = 0;j < m;j++)
      {
        cell_value = (html_table.rows[i+1].cells[j+1].children[0].value).trim();
        //checking answer of cell[i][j]
        if(cell_value.localeCompare(table[j][i]) == 0)
        {
          html_table.rows[i+1].cells[j+1].children[0].style.backgroundColor = 'green';
          right+=1;
        }
        //if answer doesn't match simulated answer
        else
        {
          html_table.rows[i+1].cells[j+1].children[0].style.backgroundColor = 'red';
          wrong+=1;
        }
      }
    }
    //checking number of hits
    if((isNaN(hit_a)==true)||(hit_a==""))
    {
      document.getElementById('nhit').style.backgroundColor = 'red';
      wrong+=1;
    }
    //if input is integer
    else
    {
      input_int=parseInt(hit_a);
      if(input_int==hits)
      {
        document.getElementById('nhit').style.backgroundColor = 'green';
        right+=1;
      }
      else
      {
        document.getElementById('nhit').style.backgroundColor = 'red';
        wrong+=1;
      }
    }
    //checking number of misses
    if((isNaN(miss_a)==true)||(miss_a==""))
    {
      document.getElementById('nmiss').style.backgroundColor = 'red';
      wrong+=1;
    }
    //if input is integer
    else
    {
      input_int=parseInt(miss_a);
      if(input_int==faults)
      {
        document.getElementById('nmiss').style.backgroundColor = 'green';
        right+=1;
      }
      else
      {
        document.getElementById('nmiss').style.backgroundColor = 'red';
        wrong+=1;
      }
    }
    //checking hit rate
    if((isNaN(hitrate_a)==true)||(hitrate_a==""))
    {
      document.getElementById('hitr').style.backgroundColor = 'red';
      wrong+=1;
    }
    //if input is integer
    else
    {
      input_int=parseInt(hitrate_a*100);
      if(input_int==parseInt((hits*100)/m))
      {
        document.getElementById('hitr').style.backgroundColor = 'green';
        right+=1;
      }
      else
      {
        document.getElementById('hitr').style.backgroundColor = 'red';
        wrong+=1;
      }
    }
    //checking miss rate
    if((isNaN(missrate_a)==true)||(missrate_a==""))
    {
      document.getElementById('missr').style.backgroundColor = 'red';
      wrong+=1;
    }
    //if input is integer
    else
    {
      input_int=parseInt(missrate_a*100);
      if(input_int==parseInt((faults*100)/m))
      {
        document.getElementById('missr').style.backgroundColor = 'green';
        right+=1;
      }
      else
      {
        document.getElementById('missr').style.backgroundColor = 'red';
        wrong+=1;
      }
    }
    document.getElementById('correct').value = "correct: "+String(right);
    document.getElementById('correct').style.backgroundColor = 'green';
    document.getElementById('incorrect').value = "incorrect: "+String(wrong);
    document.getElementById('incorrect').style.backgroundColor = 'red';
  }
}
function clear_page()
{
  //clearing question input fields
  document.getElementById('rs').value="";
  document.getElementById('tsim').value="";
  //clearing answer input fields
  document.getElementById('nhit').value="";
  document.getElementById('nhit').style.backgroundColor="";
  document.getElementById('nmiss').value="";
  document.getElementById('nmiss').style.backgroundColor="";
  document.getElementById('hitr').value="";
  document.getElementById('hitr').style.backgroundColor="";
  document.getElementById('missr').value="";
  document.getElementById('missr').style.backgroundColor="";
  document.getElementById('correct').value="";
  document.getElementById('correct').style.backgroundColor="";
  document.getElementById('incorrect').value="";
  document.getElementById('incorrect').style.backgroundColor="";
  document.getElementById('table_mem').innerHTML = "<thead></thead><tbody><tr><td></td></tr></tbody>"; //clearing table
}