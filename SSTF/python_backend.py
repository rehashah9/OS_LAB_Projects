`#importing required modules
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
      #if minimum or equal to minimum and with higher p_id
      if abs(head_pos-req_queue[i][1]) < m:
        m=abs(head_pos-req_queue[i][1])
        ind=i
      elif (abs(head_pos-req_queue[i][1])==m) and (req_queue[i][0] < req_queue[ind][0]):
        m=abs(head_pos-req_queue[i][1])
        ind=i
    res_queue.append(req_queue[ind])
    tot_mov+=m
    head_pos=req_queue[ind][1]
    del req_queue[ind]
  return (res_queue,tot_mov)

#adding request into database and generating id
def add_req(e):
  global co
  global mo
  req_txtf=Element('ior').element
  req_no_all=req_txtf.value
  #adding request to database if it is int and non-neg
  flag=0
  for i in req_no_all:
    if not(i.isdigit() or i.isspace()):
      flag=1
      break 
  if flag==0 and req_no_all!="":
    Element('tr').element.value=""
    while(req_no_all!=""):
      space_index=req_no_all.find(" ")
      if(space_index>0):
        req_no=req_no_all[0:space_index+1]
        req_no_all=req_no_all[space_index+1:]
      else:
        req_no=req_no_all
        req_no_all=""
      if int(req_no)>=0:
        #generating next unique p_id
        co.execute('select max(id) from requests')
        m_id=co.fetchone();
        if m_id[0]==None:
          r_id=1
        else:
          r_id=m_id[0]+1
        ins="insert into requests values(?,?)"
        r_ins=(r_id,int(req_no))
        co.execute(ins,r_ins)
        mo.commit()
        Element('tr').element.value+=(str(r_id)+" ")
        req_txtf.value=req_txtf.value+" added"
        req_list_dis()
      else:
        req_txtf.value="value cannot be negative"
  else:
    req_txtf.value="invalid value!"

#deleting request from database by p_id i p_id exists in database
def del_req(e):
  global co
  global mo
  rid_txtf=Element('idr').element
  d_id=rid_txtf.value
  if d_id.isdigit() and d_id!="":
    if int(d_id)>0:
      co.execute('select * from requests where id = ?',(int(d_id),))
      res_d=co.fetchone();
      if res_d==None:
        rid_txtf.value="given id doesn't exist"
      else:
        co.execute('delete from requests where id = ?',(int(d_id),))
        mo.commit()
        rid_txtf.value=rid_txtf.value+" deleted"
        req_list_dis()
    else:
      rid_txtf.value="id has to be a positive integer"
  else:
    rid_txtf.value="invalid value!"

#checking if initial position of head given is appropriate and then running sheduling algo and generating graph
def sim_req(e):
  global co
  global fig
  hip_txtf=Element('ip').element
  h_ip=hip_txtf.value
  if h_ip.isdigit() and h_ip!="":
    if int(h_ip)>=0:
      co.execute('select * from requests')
      req_data=co.fetchall()
      if req_data[0]==None:
        Element('tr').element.vaule="Can't simulate with no data!"
      else:
        sch_queue,total_mov=disk_sstf(int(h_ip),req_data)
        Element('mior').element.value=str(total_mov)
        st_list=""
        x=[int(h_ip)]
        y=[0]
        itt=1
        for i in sch_queue:
          st_list+=str(i[0])+"-"+str(i[1])+","
          x.append(i[1])
          y.append(itt)
          itt+=1
        Element('oc').element.value=st_list
        req_list_dis()
        #display graph
        fig, ax = plt.subplots()
        plt.plot(x,y)
        plt.xlabel('track position')
        plt.ylabel('iteration')
    else:
      hip_txtf.value="value has to be non-negative"
  else:
    hip_txtf.value="invalid value!"
  pyscript.write('plot',fig)

#clearing database and all the textfields
def cls_req(e):
  global co
  global mo
  co.execute('delete from requests')
  mo.commit()
  Element('ip').element.value="Database has been cleared"
  Element('ior').element.vaule=""
  Element('tr').element.value=""
  Element('idr').element.value=""
  Element('lor').element.value=""
  Element('oc').element.vaule=""
  Element('mior').element.value=""
        
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
          Element('text').element.value="saved!"
          Element('phone').element.value=""
          Element('email').element.value=""
          Element('name').element.value=""
        else:
          Element('text').element.value="enter text!"
      else:
        Element('phone').element.value="enter text!"
    else:
      Element('email').element.value="enter text!"
  else:
    Element('name').element.value="enter text!"
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