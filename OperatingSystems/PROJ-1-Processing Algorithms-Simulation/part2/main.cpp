#include <iostream>
#include <vector>
#include <algorithm>
#include <cstdlib>
#include "Process.h"

using namespace std;

float time_elpased = 0;

//value 0 is min value 1 is max value 2 is average
float roundrobbin=200;
int n = 12; //number of nodes
bool myfunctionsort(Process job1, Process job2)//sort based upon the time the item arrived. if both arrived the same time then whichever had a lower pid is first
{
    if(job1.get_start_time()<job2.get_start_time())
    {
        return true;
    }
    else if(job2.get_start_time()<job1.get_start_time())
    {
        return false;
    }
    else
    {
        return (job1.get_pid()<job2.get_pid());
    }
}
bool shortest_time(Process i, Process j)
{
    
    if( j.get_start_time()<=time_elpased&&i.get_start_time()<=time_elpased)
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
    else{ return i.get_start_time()<j.get_start_time();}
}
bool fixed_time(Process i, Process j)
{
    if( j.get_start_time()<=time_elpased&&i.get_start_time()<=time_elpased)
    {
        if( i.get_priority()==j.get_priority())
        {
            if (i.get_time_left()>j.get_time_left())
            {
                return false;
            }
            else if(j.get_time_left()>i.get_time_left())
            {
                return true;
            }  
        }
        else if (i.get_priority()<j.get_priority())
        {
            return true;
        }

        else if(j.get_priority()>i.get_priority())
        {
            return false;
        }  
        else
            return (i.get_pid()<j.get_pid());
    }

    return i.get_start_time()<j.get_start_time();

}
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


//checks to see if every process is completed
bool full_done(vector<Process>original)
{
    for(unsigned int i=0;i<original.size();++i)
    {
        if(original[i].is_done()==false)
        {
            return false;
        }
    }
    return true;
}
//sorting for pre emptive shortest remaining job
bool shortest_time_org (Process i,Process j) 
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
//prints out initial job creation
void process_creation(Process job1)
{
    cout<<"[time " << job1.get_start_time() << "ms] Process "<< job1.get_pid()
    <<" created (requires "<<job1.get_time_left()<<"ms CPU time)\n"<<job1.get_priority()<<"priority\n";
}
//prints out when a job is completed
void process_completion(Process job1)
{
    cout<<"[time " << time_elpased << "ms] Process "<<job1.get_pid()
    <<" completed its CPU burst (turnaround time  "<<job1.get_turnaround_time()
    <<"ms, initial wait time "<<job1.get_initial_wait_time()<<"ms, total wait time "<<job1.get_wait_time()<<"ms)\n";
}
//prints out info for context switch
void context_switch(Process job1, Process job2)
{
    time_elpased += 17.00;
    cout << "[time " << time_elpased << "ms] Context switch (swapping out process "
    << job1.get_pid() << " for process " << job2.get_pid() << ")" << endl;
    
}
void context_change(int i, int j)
{
    time_elpased += 17.00;
    cout << "[time " << time_elpased << "ms] Context switch (swapping out process "
    << i << "for process " << j << ")" << endl;
}
int check_new_process_has_priority(vector<Process> &original,int priority)
{
    for(unsigned int i = 0; i < original.size(); i++)
    {
        if(original[i].get_start_time() <= time_elpased and original[i].is_created() == false&&original[i].get_priority()==priority)
        {
           original[i].create();
            process_creation(original[i]);
           return 1;

        }
    }
    return 0;
}
//checks if new process has been started and starts new processes
void check_new_process(vector<Process> &original)
{
    for(unsigned int i = 0; i < original.size(); i++)
    {
        if(original[i].get_start_time() <= time_elpased and original[i].is_created() == false)
        {
            original[i].create();
            process_creation(original[i]);
        }
    }
}


///first come first serve
void first_come_first_served(vector<Process> original)
{
    check_new_process(original);
        sort(original.begin(),original.end(),fixed_time);
        vector<Process>foo;
        vector<Process>other;
        int priority=-1;
        bool first=false;
        //this will create a vector of all the items with the highest priority
        for(unsigned int i=0; i<original.size();i++)
        {
            if(original[i].is_done()==false&&first==false&&original[i].get_start_time()<=time_elpased)
            {
                first=true;
                priority=original[i].get_priority();
            }
            if(original[i].get_priority()==priority&&original[i].get_start_time()<=time_elpased)
            {
                foo.push_back(original[i]);
                original.erase(original.begin()+i);
            }
        }
        unsigned int i=0;
        unsigned int times_run=0;
        if(foo.size()==0)
        {
            time_elpased++;
        }
        int index=i;
        int current=0;
        //runs the time off the items with highest priority
        while(i<foo.size())
        {
            //this will create a vector of all the items with the highest priority
        for(unsigned int i=0; i<original.size();i++)
        {
            if(original[i].get_wait_time()/1000==0)//only multiples of a thousand
            {
               original[i].increase_priority();
                cout<<"[time " << time_elpased << "ms] Increased priority of process "<<i<<" to "<<original[i].get_priority()<<" due to aging\n";
            }
        }
            current=i;
            int priority=check_new_process_has_priority(original,foo[0].get_priority());
            check_new_process(original);
            if(priority==1)
            {
                break;
            }

            else{
                foo[i].run_time_off(1, time_elpased);
                times_run++;
                if(foo[i].is_done() == true){process_completion(foo[i]);break;}
            }
        }
        for(int i=0;i<foo.size();i++)
        {
            original.push_back(foo[i]);
        }
        get_turnaround(original);
    get_initial_wait_times(original);
    get_total_wait(original);
}
//Non pre-emptive
void shortest_job_next(vector<Process> original)
{
    while (full_done(original) == false)
    {
        sort(original.begin(), original.end(), shortest_time);
        bool no_present_things=true;
        for (unsigned int i = 0; i < original.size(); ++i)
        {
            if(original[i].is_done()!=true)
            {
                float temp = original[i].get_time_left();
                original[i].run_time_off(temp, time_elpased);
                check_new_process(original);
                process_completion(original[i]);
                if(i!=original.size()&&i!=original.size()-1)
                {
                    context_switch(original[i],original[i+1]);
                    check_new_process(original);
                }
                no_present_things=false;
                break;

            }
        }
        if(no_present_things==true)
        {
            time_elpased++;
            check_new_process(original);
        }    
    }
    get_turnaround(original);
    get_initial_wait_times(original);
    get_total_wait(original);
}
//sorts the vector by priroty and shortest time
void shortest_time_sort(vector<Process> &original)
{
    vector<Process> v;
    
    for(int j = 0; j < 5;j++)
    {
        vector<Process> temp;
        for(unsigned int i = 0; i < original.size(); i++)
        {
            if(original[i].get_priority() == j)
            {
                temp.push_back(original[i]);
            }
        }
        sort(temp.begin(), temp.end(), shortest_time_org);
        for(unsigned int i = 0; i < temp.size(); i++)
        {
            v.push_back(temp[i]);
        }
    }
    original = v;
}
//pre emptive
void shortest_remainning_time(vector<Process> original)
{
    unsigned int j = 0;//place
    shortest_time_sort(original);
    float pid = original[0].get_pid();
    bool run_off = true;
    bool waiting = true;
    while(full_done(original) == false)
    {
        run_off = true;
        waiting = true;
        check_new_process(original);
        shortest_time_sort(original);
        for(unsigned int i = 0; i < original.size();i++)
        {
            if(original[i].is_done() != true and original[i].is_created() == true)
            {
                if(original[i].get_pid() == pid){
                    j = i;
                    break;
                }
                else{
                    context_switch(original[j], original[i]);
                    waiting = false;
                    j = i;
                    pid = original[i].get_pid();
                    run_off = false;
                    break;
                }
            }
        }
        if(run_off and original[j].is_done() != true)
        {
            original[j].run_time_off(1, time_elpased);
            waiting = false;
            if(original[j].is_done() == true)
                process_completion(original[j]);
        }
        if(waiting)
            time_elpased++;
    }
    get_turnaround(original);
    get_initial_wait_times(original);
    get_total_wait(original); 
}

//round robbin
void round_robbin(vector<Process> original, int timeburst)
{
    float index_previous=0;
    bool first=false;
    bool waiting = true;
    check_new_process(original);
    while(full_done(original) == false)
    {

        for(unsigned int i=0;i<original.size();i++)
        {
            waiting = true;
            if(original[i].is_done()!=true && original[i].get_start_time()<=time_elpased)
            {
                if(index_previous!=i&&first)
                {
                    check_new_process(original);
                    context_switch(original[index_previous],original[i]);
                    waiting = false;
                    check_new_process(original);
                }
                 first=true;

                if(original[i].get_time_left()<timeburst)
                {
                    check_new_process(original);
                    original[i].run_time_off(original[i].get_time_left(),time_elpased);
                    first=true;
                    waiting = false;
                    check_new_process(original);
                }
                else
                {
                    check_new_process(original);
                    original[i].run_time_off(timeburst,time_elpased);
                    waiting = false;
                    check_new_process(original);
                }
                index_previous=i;
                if(original[i].is_done())
                {
                    process_completion(original[i]);
                }
            }
        }
        if(waiting)
            time_elpased++;
 
    }
    get_turnaround(original);
    get_initial_wait_times(original);
    get_total_wait(original);  
}
//works
void fixed_priority(vector<Process> original, int timeburst)
{
    int priority;
    while(full_done(original) == false)
    {
       
        check_new_process(original);
        sort(original.begin(),original.end(),fixed_time);
        vector<Process>foo;
        vector<Process>other;
        priority=-1;
        bool first=false;
        //this will create a vector of all the items with the highest priority
        for(unsigned int i=0; i<original.size();i++)
        {
            if(original[i].is_done()==false&&first==false&&original[i].get_start_time()<=time_elpased)
            {
                first=true;
                priority=original[i].get_priority();
            }
            if(original[i].get_priority()==priority&&original[i].get_start_time()<=time_elpased)
            {
                foo.push_back(original[i]);
                original.erase(original.begin()+i);
            }
        }
        unsigned int i=0;
        unsigned int times_run=0;
        if(foo.size()==0)
        {
            time_elpased++;
        }
        int index=i;
        int current=0;
        //runs the time off the items with highest priority
        while(i<foo.size())
        {
            current=i;
            int priority=check_new_process_has_priority(original,foo[0].get_priority());
            check_new_process(original);
            if(priority==1)
            {
                break;
            }

            else{
                if(times_run==timeburst)
                {
                    i++;
                    times_run=0;
                    if(foo.size()==1)
                    {
                        break;
                    }
                    
                    if(i!=foo.size())
                    {
                        context_switch(foo[index],foo[current]);
                    }
                }
                else{
                        
                        foo[i].run_time_off(1, time_elpased);
                        times_run++;
                        if(foo[i].is_done() == true){process_completion(foo[i]);break;}
                }
            }
        }
        for(int i=0;i<foo.size();i++)
        {
            original.push_back(foo[i]);
        }
    }
     get_turnaround(original);
    get_initial_wait_times(original);
    get_total_wait(original); 
}
int main()
{
    std::vector<Process> orginal;
    vector<Process>PreemptiveFCFS;
    vector<Process>SJN;
    vector<Process>SJF;
    vector<Process>RR;
    vector<Process>PreemptiveRR;
    int ratio=10;
    for (unsigned int i = 0; i < n; ++i)
    {
        float temp = rand() % 3500 + 500;
        float priority= rand()%4+1;
        float start_time=rand()%5000+800;
        if((i % ratio)==0)//change for ratio
        {
            Process foo(0, temp, i + 1, priority);
            orginal.push_back(foo);
        }
        else{
            Process foo(start_time,temp,i+1,priority);
            
            orginal.push_back(foo);
        }

    }

     for(unsigned int i=0;i<orginal.size();i++)
    {
        cout<<"processs: "<<orginal[i].get_pid()<<" start_time: "<<orginal[i].get_start_time()<<" time left:"<<orginal[i].get_time_left()<<endl;
    }
    SJN=orginal;
    SJF=orginal;
    RR=orginal;
    PreemptiveRR=orginal;
    PreemptiveFCFS=orginal;

    //done
    time_elpased = 0;
    cout<<"\n\nShortest Job First no preemption\n";
    shortest_job_next(SJN);

    //done
    time_elpased = 0;
    cout<<"\n\nShortest Job First preemption\n";
    shortest_remainning_time(SJF);

    //done
    time_elpased = 0;
    cout<<"\n\nRound Robin with Preemption\n";
    round_robbin(RR,200);
    
    time_elpased = 0;
    cout<<"\n\nFixed Priority\n";
    fixed_priority(PreemptiveRR,100);

    time_elpased = 0;
    cout<<"\n\naging fcss\n";
    first_come_first_served(PreemptiveFCFS);

    
    return 0;
}
