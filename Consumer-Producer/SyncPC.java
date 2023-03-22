package com.example.os;

import android.util.Log;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncPC {

    Lock lock = new ReentrantLock();
    Condition not_empty = lock.newCondition();
    Condition not_full = lock.newCondition();
    public int s=-1;
    public int i=0;
    public   String[] stack;
    public int z=0;
      //Producer code
    void simulate(int n, String[] stack1, TableLayout tb, Solution m) throws Exception
    {
        this.stack = stack1;
        s=-1;
        Runnable r1 = new Runnable(){
            public void run()
            {
                while(z==1)
                {
                    lock.lock();
                  //Critical Section 
                    try
                    {
                        if (s==(n-1))
                        {
                            try{
                                m.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(m,"Producer is waiting",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            catch (Exception r){
                            }
                            not_full.await();
                        }
                        s++;
                        stack[s]="item";
                        try{
                            m.runOnUiThread(new Runnable() {
                              	//The current thread is a producer that produces a value s, updates some UI components, adds a record to an array, and inserts data into a database.
                                @Override
                                public void run() {
                                    Solution.update_table(stack,tb,m);
                                    m.consumer.setText("Producer " +Thread.currentThread().getId()+ " ");
                                    m.producer.setText(s+ " p ");
                                    Solution.r_array.add(new RecordEntry("Producer ",s,"produced"));
                                    m.db.insertData(n,"Producer" + Thread.currentThread().getId(),s);
                                }
                            });
                        }
                        catch (Exception e){
                        }
                        Log.i("Tag","producer "+Thread.currentThread().getId()+" ");
                        Log.i("Tag",s+ "p");

                        Thread.sleep(300);
                        not_empty.signalAll();
                    }
                    catch(Exception ex){
                    }
                    finally {
                        lock.unlock();//allow other threads to acquire the lock
                    }

                }
              //Clear the table and reset the producer and consumer labels after the simulation has completed
                try{
                    m.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m.tb.removeAllViews();
                            m.producer.setText("");
                            m.consumer.setText("");
                        }
                    });
                }
                catch(Exception e){ }
            }
        };
      //Consumer code
        Runnable r2 = new Runnable(){
            public void run()
            {
                while(z==1)
                {
                    lock.lock();
                  //Critical Section 
                    try
                    {
                        if(s==-1)
                        {
                            try{
                                m.runOnUiThread(new Runnable() {
                                  //Maintain the state of the simulation and track the history of operations performed by the consumer threads.

                                    @Override
                                    public void run() {
                                        Toast.makeText(m,"Consumer is waiting",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            catch (Exception r){
                            }
                            not_empty.await();
                        }
                        stack[s]="-";
                        s--;

                        try{
                            m.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Solution.update_table(stack,tb,m);
                                    m.consumer.setText("Consumer " +Thread.currentThread().getId()+ " ");
                                    m.producer.setText((s+1)+ " c ");
                                    Solution.r_array.add(new RecordEntry("Consumer ",s+1,"consumed"));
                                    m.db.insertData(n,"Consumer" + Thread.currentThread().getId(),s);
                                }
                            });
                        }
                        catch (Exception e){
                        }
                        Log.i("Tag","consumer "+Thread.currentThread().getId()+" ");
                        Log.i("Tag",s+ "c");
                        Thread.sleep(300);
                        not_full.signalAll();
                    }
                    catch(Exception ex){
                    }
                    finally {
                        lock.unlock();//allow other threads to acquire the lock
                    }
                }
                try{
                  //Clear the table and reset the producer and consumer labels after the simulation has completed
                    m.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m.tb.removeAllViews();
                            m.producer.setText("");
                            m.consumer.setText("");
                        }
                    });
                }
                catch(Exception e){ }
            }
        };
        Thread pr = new Thread(r1);
        Thread co = new Thread(r2);
        pr.start();
        co.start();
    }
}
