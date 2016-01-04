#ifndef Process_h_
#define Process_h_


class Process
{
public:
    Process(float start, float time_required,
            float given_pid, float given_priority );
    
    //returns the time left on the cpu
    float get_time_left();
    //is the job done
    bool is_done();
    bool is_created();
    //returns true if time left is less then x
    bool will_run_time_off(float x);
    //runs x time off of the cpu time remaining
    //if not started calculates the initial wait time
      void run_time_off(float x, float & time_elpased);
    void create();
    void increase_priority();
    //cacluates and returns wait time
    float get_wait_time();
    //returns initial wait time
    float get_initial_wait_time();
    //returns turnaround time
    float get_turnaround_time();
    //get pid
    float get_pid();
    //get priority
    float get_priority();
    float get_start_time();
    
private:
    /* data */
    float pid;//process id number 0-20
    
    float priority; //0-4 lower is higher
    
    float required_cpu_time;
    bool created;
    float cpu_time_remaining;
    bool done;
    //time tracking information
    float start_time;
    float end_time;
    float turnaround_time;
    float initial_wait_time;
    float wait_time;
    
    
};




#endif