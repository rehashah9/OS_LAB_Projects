#importing required modules
from js import document
from pyodide import create_proxy
import sqlite3
import matplotlib.pyplot as plt
import os

#function to do the sstf scheduling - T(n)=O(n^2)
def disk_sstf(head_pos,req_queue):
  res_queue=[] #returned queue
  tot_mov=0 #total movement done by head
  #iterating through request queue to add all the request into result in specific order
  while(req_queue!=[]):
    m=abs(head_pos-req_queue[0][1]) #to store minimum distance
    ind=0 #to store index of next request
    #iterating through request queue to find minimum
    for i in range(len(req_queue)):
      #if less than current minimum then updating minimum
      if abs(head_pos-req_queue[i][1]) < m:
        m=abs(head_pos-req_queue[i][1])
        ind=i
      #if equal to current min and has higher p_id then updating min
      elif (abs(head_pos-req_queue[i][1])==m) and (req_queue[i][0] < req_queue[ind][0]):
        m=abs(head_pos-req_queue[i][1])
        ind=i
    #adding request with min distance to list containng order of requests fulfilled
    res_queue.append(req_queue[ind])
    tot_mov+=m
    head_pos=req_queue[ind][1]
    del req_queue[ind]  #removing completed request from request queue
  return (res_queue,tot_mov)

#adding request into database and generating id
def add_req(e):
  global co
  global mo
  req_txtf=Element('ior').element
  req_no_all=req_txtf.value #loading requests
  #checking for any non-numeric and non-space characters in input
  flag=0
  for i in req_no_all:
    #updating flag if illegal character found
    if not(i.isdigit() or i.isspace()):
      flag=1
      break 
  #if string has some input and it is valid
  if flag==0 and req_no_all!="":
    Element('tr').element.value=""
    #iterating through string to filter out seperate requests
    while(req_no_all!=""):
      space_index=req_no_all.find(" ")  #index of next space
      #if some digits are there before space
      if(space_index>0):
        req_no=req_no_all[0:space_index+1] #adding request
        req_no_all=req_no_all[space_index+1:] #changing main str to remove currently extracted request
      #if no space in string
      elif(space_index<0):
        req_no=req_no_all  #full str is next request
        req_no_all=""  #main str to empty
      #if space is at index 0 then skip it
      else:
        req_no_all=req_no_all[space_index+1:]
        continue
      #if request is non-negative
      if int(req_no)>=0:
        #generating next unique p_id
        co.execute('select max(id) from requests')
        m_id=co.fetchone();
        if m_id[0]==None:
          r_id=1
        else:
          r_id=m_id[0]+1
        #putting request in db
        ins="insert into requests values(?,?)"
        r_ins=(r_id,int(req_no))
        co.execute(ins,r_ins)
        mo.commit()
        #displaying generated id
        Element('tr').element.value+=(str(r_id)+" ")
        req_list_dis()
      #if value is negative
      else:
        req_txtf.value=""
        req_txtf.placeholder="value cannot be negative"
    req_txtf.placeholder=req_txtf.value+" added"
    req_txtf.value=""
  #if invalid characters in value
  else:
    req_txtf.value=""
    req_txtf.placeholder="invalid value!"

#deleting request from database by p_id i p_id exists in database
def del_req(e):
  global co
  global mo
  rid_txtf=Element('idr').element
  d_id=(rid_txtf.value).strip(" ")  #removing trailing and starting spaces
  #if id is a digit and not empty string
  if d_id.isdigit() and d_id!="":
    #if id is positive
    if int(d_id)>0:
      co.execute('select * from requests where id = ?',(int(d_id),))
      res_d=co.fetchone();
      #if id not in db
      if res_d==None:
        rid_txtf.placeholder="given id doesn't exist"
        rid_txtf.value=""
      #deleting request with given id
      else:
        co.execute('delete from requests where id = ?',(int(d_id),))
        mo.commit()
        rid_txtf.placeholder=rid_txtf.value+" deleted"
        rid_txtf.value=""
        req_list_dis()
    #if input is not positive
    else:
      rid_txtf.placeholder="id has to be a positive integer"
      rid_txtf.value=""
  #if there are invalid characters
  else:
    rid_txtf.placeholder="invalid value!"
    rid_txtf.value=""

#checking if initial position of head given is appropriate and then running sheduling algo and generating graph
def sim_req(e):
  global co
  global fig
  hip_txtf=Element('ip').element
  h_ip=(hip_txtf.value).strip(" ")  #removing white spaces
   #if head position is a digit and not empty string
  if h_ip.isdigit() and h_ip!="":
    #if head position is non-negative
    if int(h_ip)>=0:
      co.execute('select * from requests')
      req_data=co.fetchall()
      #if no request added
      if req_data==None:
        Element('tr').element.placeholder="Can't simulate with no data!"
        Element('tr').element.value=""
      #doing scheduling
      else:
        sch_queue,total_mov=disk_sstf(int(h_ip),req_data)
        Element('mior').element.value=str(total_mov)
        st_list=""  #for displaying scheduled order
        x=[int(h_ip)]
        y=[0]
        itt=1
        #creating list for graph and output fields
        for i in sch_queue:
          st_list+=str(i[0])+"-"+str(i[1])+","
          x.append(i[1])
          y.append(itt)
          itt+=1
        Element('oc').element.value=st_list
        req_list_dis()
        #creating graph
        fig, ax = plt.subplots()
        plt.plot(x,y)
        plt.xlabel('track position')
        plt.ylabel('iteration')
    #negative input
    else:
      hip_txtf.placeholder="value has to be non-negative"
      hip_txtf.value=""
  #illegal character input
  else:
    hip_txtf.placeholder="invalid value!"
    hip_txtf.value=""
  #displaying graph
  pyscript.write('plot',fig)

#clearing database and all the textfields
def cls_req(e):
  global co
  global mo
  co.execute('delete from requests')
  mo.commit()
  #clearing all text fields and reseting graph
  Element('ip').element.placeholder="All current requests have been cleared"
  Element('ip').element.value=""
  Element('ior').element.placeholder="Enter track no requested by I/O operation"
  Element('ior').element.vaule=""
  Element('tr').element.placeholder="ID"
  Element('tr').element.value=""
  Element('idr').element.placeholder="Enter id of request to be deleted"
  Element('idr').element.value=""
  Element('lor').element.placeholder="List of I/O Request"
  Element('lor').element.value=""
  Element('oc').element.placeholder="Order of Completion"
  Element('oc').element.value=""
  Element('mior').element.placeholder="Total movement done by I/O Request"
  Element('mior').element.value=""
  x=[0,0,0,0]
  y=[0,0,0,0]
  fig, ax = plt.subplots()
  plt.plot(x,y)
  plt.xlabel('track position')
  plt.ylabel('iteration')
  pyscript.write('plot',fig)
        
#retrieves all the requests from the database and displays them in a text box
def req_list_dis():
  global co
  global mo
  co.execute('select * from requests')
  req_data=co.fetchall()
  print(req_data)
  str_dis=""
  for i in req_data:
    str_dis+=str(i[0])+"-"+str(i[1])+","
  Element('lor').element.value=str_dis

#checks correctness and then adds user details given in contact_us to database
def sub_req(e):
  global co
  global mo
  name=Element('name').element.value
  email=Element('email').element.value
  phone=Element('phone').element.value
  text=Element('text').element.value
  if name!="":
    if email!="":
      if phone!="":
        if text!="":
          coninp=(name,email,phone,text)
          co.execute('insert into contact_us values(?,?,?,?)',coninp)
          mo.commit()
        else:
          Element('text').element.placeholder="enter text!"
          Element('text').element.value=""
      else:
        Element('phone').element.placeholder="enter text!"
        Element('phone').element.value=""
    else:
      Element('email').element.placeholder="enter text!"
      Element('email').element.value=""
  else:
    Element('name').element.placeholder="enter text!"
    Element('name').element.value=""
  co.execute('select * from contact_us')
  con_data=co.fetchall()
  print(con_data)

#connecting to database and creating both the tables
mo=sqlite3.connect('os_lab_proj3.db') #connection object
co=mo.cursor() #cursor object
co.execute('create table requests(id int primary key, track_no int)')
mo.commit()
co.execute('create table contact_us(name text,email text,phone text,problem text)')
mo.commit()

#proxy for add function and connecting it to add button
add_func=create_proxy(add_req)
add_but=document.getElementById("add")
add_but.addEventListener("click",add_func)

#proxy for delete function and connecting it to delete button
del_func=create_proxy(del_req)
del_but=document.getElementById("del")
del_but.addEventListener("click",del_func)

#proxy for simulate function and connecting it to simulate button
sim_func=create_proxy(sim_req)
sim_but=document.getElementById("sim")
sim_but.addEventListener("click",sim_func)

#proxy for clear function and connecting it to clear button
cls_func=create_proxy(cls_req)
cls_but=document.getElementById("cls")
cls_but.addEventListener("click",cls_func)

#proxy for submit function and connecting it to submit button
sub_func=create_proxy(sub_req)
sub_but=document.getElementById("sub")
sub_but.addEventListener("click",sub_func)

#creating a placeholder graph for creating proper graph in simulate
x=[0,0,0,0]
y=[0,0,0,0]
fig, ax = plt.subplots()
plt.plot(x,y)
plt.xlabel('track position')
plt.ylabel('iteration')
pyscript.write('plot',fig)