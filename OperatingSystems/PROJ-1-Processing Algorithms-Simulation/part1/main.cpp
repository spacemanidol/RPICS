#include <iostream>
#include <vector>
#include <algorithm>
#include <cstdlib>
#include "Process.h"

using namespace std;

float time_elpased = 0;

//value 0 is min value 1 is max value 2 is average
float roundrobbin=200;
int n = 20; //number of nodes


//sorts by priority, pid
bool myfunction (Process i,Process j) 
{ 
    if (i.get_priority()>j.get_priority())
    {
        return false;
    }
    else if(j.get_priority()>i.get_priority())
    {
        return true;
    }  
    else
        return (i.get_pid()<j.get_pid());
}

//sorts by time left
bool shortest_time (Process i,Process j) 
{ 
    if (i.get_time_left()>j.get_time_left())
    {
        return false;
    }
    else if(j.get_time_left()>i.get_time_left())
    {
        return true;
    }  
    else
        return (i.get_pid()<j.get_pid());
}


//loops throught the processes to find the min, max and average times for initial wait time
void get_initial_wait_times(vector<Process>original)
{
    float global_initial_wait[3];
    global_initial_wait[0]=original[0].get_initial_wait_time();
    global_initial_wait[1]=original[0].get_initial_wait_time();
    global_initial_wait[2]=0.0;
    for(unsigned int i=0;i<original.size();i++)
    {
        if(global_initial_wait[0]>original[i].get_initial_wait_time())
        {
            global_initial_wait[0]=original[i].get_initial_wait_time();
        }
        if(global_initial_wait[1]<original[i].get_initial_wait_time())
        {
            global_initial_wait[1]=original[i].get_initial_wait_time();
        }
        global_initial_wait[2]+=original[i].get_initial_wait_time();
    }
    float temp = original.size();
    float foo = global_initial_wait[2];
    global_initial_wait[2] = foo / temp;
    cout<<"Initial wait: min "<<global_initial_wait[0]<<" ms; avg "<<global_initial_wait[2]<<" ms ; max "<<global_initial_wait[1]<<" ms\n";
    
}
//loops throught the processes to find the min, max and average times for total wait time
void get_total_wait(vector<Process>original)
{
    float global_total_wait[3];
    global_total_wait[0]=original[0].get_wait_time();
    global_total_wait[1]=original[0].get_wait_time();
    global_total_wait[2]=0.0;
    for(unsigned int i=0;i<original.size();i++)
    {
        if(global_total_wait[0]>original[i].get_wait_time())
        {
            global_total_wait[0]=original[i].get_wait_time();
        }
        if(global_total_wait[1]<original[i].get_wait_time())
        {
            global_total_wait[1]=original[i].get_wait_time();
        }
        global_total_wait[2]+=original[i].get_wait_time();
    }
    float temp = original.size();
    float foo = global_total_wait[2];
    global_total_wait[2] = foo / temp;
    cout<<"Total wait time: min "<<global_total_wait[0]<<" ms; avg "<<global_total_wait[2]<<" ms ; max "<<global_total_wait[1]<<" ms\n";
}
//loops throught the processes to find the min, max and average times for turnaround time
void get_turnaround(vector<Process>original)
{
    
    float global_turn_around[3];
    global_turn_around[0]=original[0].get_turnaround_time();
    global_turn_around[1]=original[0].get_turnaround_time();
    global_turn_around[2] = 0.0;
    for(unsigned int i=0;i<original.size();i++)
    {
        if(global_turn_around[0]>original[i].get_turnaround_time())
        {
            global_turn_around[0]=original[i].get_turnaround_time();
        }
        if(global_turn_around[1]<original[i].get_turnaround_time())
        {
            global_turn_around[1]=original[i].get_turnaround_time();
        }
        global_turn_around[2]+=original[i].get_turnaround_time();
    }
    float temp = original.size();
    float foo = global_turn_around[2];
    global_turn_around[2] = foo / temp;
    cout<<"Turnaround time: min "<<global_turn_around[0]<<" ms; avg "<<global_turn_around[2]<<" ms ; max "<<global_turn_around[1]<<" ms\n";
}
//prints out the initial process detials
void process_creation(Process job1)
{
    cout<<"[time " << time_elpased << "ms] Process "<< job1.get_pid()
    <<" created (requires "<<job1.get_time_left()<<"ms CPU time)\n";
}
//prints out information when a process is done
void process_completion(Process job1)
{
    cout<<"[time " << time_elpased << "ms] Process "<<job1.get_pid()
    <<" completed its CPU burst (turnaround time  "<<job1.get_turnaround_time()
    <<"ms, initial wait time "<<job1.get_initial_wait_time()<<"ms, total wait time "<<job1.get_wait_time()<<"ms)\n";
    
}
//prints out info when a context switch occuers
void context_switch(Process job1, Process job2)
{
    time_elpased += 17.00;
    cout << "[time " << time_elpased << "ms] Context switch (swapping out process "
    << job1.get_pid() << "for process " << job2.get_pid() << ")" << endl;
}

//first come first serve implimentation
void first_come_first_served(vector<Process> original)
{
    //will loop through each task finishing it
    for(float i=0;i<original.size();i++)
    {
        original[i].run_time_off(original[i].get_time_left(), time_elpased);
        if(original[i].is_done())
        {
            process_completion(original[i]);
        }
        if(i<original.size())
        {
            context_switch(original[i], original[i+1]);
        }
        
    }
    //calling up functions to print out results when 
    //done with runnning everything
    get_turnaround(original);
    get_initial_wait_times(original);
    get_total_wait(original);
}
//Non pre-emptive
void shortest_job_next(vector<Process> original)
{
    sort(original.begin(), original.end(), shortest_time);
    for (unsigned int i = 0; i < original.size(); ++i)
    {
        float temp = original[i].get_time_left();
        original[i].run_time_off(temp, time_elpased);
        process_completion(original[i]);
        if(i!=original.size())
        {
            context_switch(original[i],original[i+1]);
        }
    }
    //calling up functions to print out results when 
    //done with runnning everything
    get_turnaround(original);
    get_initial_wait_times(original);
    get_total_wait(original);
}
//pre emptive
void shortest_remainning_time(vector<Process> original)
{
    sort(original.begin(), original.end(), shortest_time);
    for (unsigned int i = 0; i < original.size(); ++i)
    {
        float temp = original[i].get_time_left();
        original[i].run_time_off(temp, time_elpased);
        process_completion(original[i]);
        if(i!=original.size())
        {
            context_switch(original[i],original[i+1]);
        }
    }
    //calling up functions to print out results when 
    //done with runnning everything
    get_turnaround(original);
    get_initial_wait_times(original);
    get_total_wait(original);
}
void round_robbin(vector<Process> original)
{
    float index_previous=0;
    bool tasks_not_completed=true;
    while(tasks_not_completed)
    {
        tasks_not_completed=false;
        for(float i=0;i<original.size();i++)
        {
            if(original[i].is_done()!=true)
            {
                tasks_not_completed=true;
                if(index_previous!=i)
                    context_switch(original[index_previous],original[i]);
                if(original[i].get_time_left()<roundrobbin)
                {
                original[i].run_time_off(original[i].get_time_left(),time_elpased);
                }
                else
                {
                    original[i].run_time_off(roundrobbin,time_elpased);
                }
                index_previous=i;
                if(original[i].is_done())
                {
                process_completion(original[i]);
                }
            }
            
        }
    }
    //calling up functions to print out results when 
    //done with runnning everything
    get_turnaround(original);
    get_initial_wait_times(original);
    get_total_wait(original);
}


//used this funtion in fixed priority it handle running off jobs
//is a almost identical copy of round robbin except it returns the final vector 
//instead of printing out the results
std::vector<Process> mini_round_robbin(vector<Process> original)
{
    float index_previous=0;
    bool tasks_not_completed=true;
    int first_time=false;
    while(tasks_not_completed)
    {
        tasks_not_completed=false;
        for(float i=0;i<original.size();i++)
        {
            if(original[i].is_done()!=true)
            {
                if(index_previous!=i)
                    context_switch(original[index_previous],original[i]);
                tasks_not_completed=true;
                if(original[i].get_time_left()<roundrobbin)
                {
                original[i].run_time_off(original[i].get_time_left(),time_elpased);
                }
                else
                {
                    original[i].run_time_off(roundrobbin,time_elpased);
                }
                index_previous=i;
            if(original[i].is_done())
            {
               process_completion(original[i]);
            }
            }
            
        }
    }
    return original;
}
//fixed priority
void fixed_priority(vector<Process> original)
{
    sort(original.begin(), original.end(), myfunction);  
    vector<Process> bar;
    for(int j = 0; j < 5; j++)
    {
        vector<Process> foo;
        for (unsigned int i = 0; i < original.size(); ++i)
        {
            if(original[i].get_priority() == j)
                foo.push_back(original[i]);
        }
        foo = mini_round_robbin(foo);
        for (unsigned int i = 0; i < foo.size(); ++i)
        {
            bar.push_back(foo[i]);
        }
    }
    //calling up functions to print out results when 
    //done with runnning everything
    get_turnaround(bar);
    get_initial_wait_times(bar);
    get_total_wait(bar);
}
int main()
{
    //create all the needed vectors for process
    //one for each algo
    std::vector<Process> orginal;
    vector<Process>FCFS;
    vector<Process>SJN;
    vector<Process>SJF;
    vector<Process>RR;
    vector<Process>Preemptive;
    //create the processes
    for (float i = 0; i < n; ++i)
    {
        float temp = rand() % 3500 + 500;
        float priority= rand()%4+1;
        Process foo(0, temp, i + 1, priority);
        orginal.push_back(foo);
        process_creation(foo);
    }
    FCFS=orginal;
    SJN=orginal;
    SJF=orginal;
    RR=orginal;
    Preemptive=orginal;
    //call each algo and run the simluations
    cout<<"\n\nFirst Come First Served\n";
    first_come_first_served(FCFS);

    time_elpased = 0;
    cout<<"\n\nShortest Job First no preemption\n";
    shortest_job_next(SJN);

    time_elpased = 0;
    cout<<"\n\nShortest Job First preemption\n";
    shortest_remainning_time(SJF);

    time_elpased = 0;
    cout<<"\n\nRound Robin\n";
    round_robbin(RR);
    
    time_elpased = 0;
    cout<<"\n\nFixed Priority\n";
    fixed_priority(Preemptive);
    
    return 0;
}