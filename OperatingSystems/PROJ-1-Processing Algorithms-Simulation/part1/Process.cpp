#include "Process.h"

Process::Process(float start, float time_required,
        float given_pid, float given_priority )
{
  pid = given_pid;
  priority = given_priority;
  required_cpu_time = time_required;
  cpu_time_remaining = required_cpu_time;
  done = false;
  start_time = start;
  end_time = 0.0;
  turnaround_time = 0.0;
  initial_wait_time = 0.0;
  wait_time = 0.0;
}

//returns the time left on the cpi
float Process::get_time_left()
{
  return cpu_time_remaining;
}
//is the job done
bool Process::is_done()
{
  return done;
}
//returns true if time left is less then x
bool Process::will_run_time_off(float x)
{
  return x > cpu_time_remaining;
}
//runs x time off of the cpu time remaining
//if not started calculates the initial wait time
void Process::run_time_off(float x, float & time_elpased)
{
  if(cpu_time_remaining == required_cpu_time)
  {
    initial_wait_time = time_elpased - start_time;
  }
  cpu_time_remaining -= x;
  if(cpu_time_remaining == 0)
  {
    done = true;
    end_time = time_elpased + x;

  }
  time_elpased += x;

}
//cacluates and returns wait time
float Process::get_wait_time()
{
  wait_time = end_time - start_time - required_cpu_time;
  return wait_time;
}
//returns initial wait time
float Process::get_initial_wait_time()
{
  return initial_wait_time;
}
//returns turnaround time
float Process::get_turnaround_time()
{
  turnaround_time = end_time - start_time;
  return turnaround_time;
}

//get pid
float Process::get_pid()
{
  return pid;
}
//get priority
float Process::get_priority()
{
  return priority;
}
