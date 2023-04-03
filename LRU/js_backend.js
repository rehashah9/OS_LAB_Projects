//time complexity of page replacement algorithm: O(n^2)
var ref_st = null; //current page reference string
var size = 0; //page capacity of cache
var m = 0; //number of pages referenced
var page_order = []; //array containing requested pages
var hits = 0; //number of hits
var faults = 0; //number of page faults
var table = []; //table storing page in memory record at each reference
var hit_miss = []; //array storing hit and miss data of each referenced page
//function to do actual lru page replacement
function lru_do(s)
{
  hits = 0;
  faults = 0;
  m = 1;
  if (size == 0)
    //error: memory capacity has to be assigned at least once before scheduling
    document.getElementById('tsim').value='value cannot be zero!';
  //if size is proper
  else
  {
    //initializing some variables
    var i = 0;
    var n = 0;
    var temp = null;
    var flag = 0;
    var empty_flag = 0;
    //counting number of pages referenced
    for (i = 0; i < s.length; i++)
    {
      if (s.charAt(i) == ' ')
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
      //removing 'space' from 0 index in s if s in not empty string
      if (s.localeCompare("")!= 0)
          s = s.substring(1);
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
    //rendering table displaying memory status after each page request
    table_render();
  }
}
//function called when simulate button clicked
function simulate()
{
  //getting refrence string and memory size value from user input
  var ref_st = document.getElementById('rs').value;
  size = parseInt(document.getElementById('tsim').value);
  //checking if size input is not int
  if(size!==size)
    document.getElementById('tsim').value="value has to be integer!";
  else
  {
    lru_do(ref_st); //lru algorithm
    //displaying statistical data output onto html page
    document.getElementById('noh').value=hits;
    document.getElementById('lor').value=faults;
    document.getElementById('hr').value=hits/m;
    document.getElementById('mr').value=faults/m;
  }
}
//function to render table displaying memory status after each page request
function table_render()
{
  html_table = document.getElementById('table_mem'); //html table element
  html_table.innerHTML = ''; //clearing table
  var i;
  var j;
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
    //for each slot adding cells to show page in the slot in each status of memory until all referenced pages are done
    for(j=0;j<m;j++)
    {
      var jth_cell = new_row.insertCell(-1);
      jth_cell.setHTML(table[j][i]);
      if(table[j][i]==page_order[j])
      {
        jth_cell.style.color = '#800000';
        jth_cell.style.fontWeight = 'bold';
      }
    }
  }
  //row to display hit or miss status
  var hm_row = html_table.insertRow(-1);
  var labelhm_cell = hm_row.insertCell(-1);
  labelhm_cell.setHTML("Hit/Miss: ");
  labelhm_cell.style.fontWeight = 'bold';
  //adding cells in the row to show hit or miss status of each referenced page seperately
  for(i=0;i<m;i++)
  {
    var jhm_cell = hm_row.insertCell(-1);
    jhm_cell.setHTML(hit_miss[i]);
    if(hit_miss[i].localeCompare("hit") == 0)
    {
      jhm_cell.style.color = 'green';
    }
    else
    {
      jhm_cell.style.color = 'red';
    }
  }
}