#-------------------------------------------------------------------------------
# Name:Elevator Simulation
# Purpose:A program that simulates elevator usage in a building
#
# Author: Daniel Campos
#
# Created: Monday Oct 20th, 2014
#-------------------------------------------------------------------------------
import random
def print_same_goal(ID,floor):
    print "Customer " + str(ID) + " started on their goal floor " + str(floor)
def print_entrance(ID,floor):
    print "Customer " + str(ID) + " has entered the elevator on floor " + str(floor)
def print_exit(ID,floor):
    print "Customer " + str(ID) + " has exited the elevator on floor " + str(floor)
def print_init(customers):
    print "Start States:"
    for i in customers:
        print "Customer " + str(i.ID) +" is waiting on floor " + str(i.start_floor) + " going to floor " + str(i.destination_floor) 
    print "Start Simulation"
class Building:
    """This is a docstring, describes the thing"""
    def __init__(self,number_of_floors,customers,elevator):
        self.data = []
        self.number_of_floors = number_of_floors
        self.customers = customers
        self.elevator = elevator
    def __str__(self):
        return 'floors: ' + str(self.number_of_floors) + " elevator: [" + str(self.elevator) + "] customers: [" + "][".join(str(v) for v in self.customers) + "]"
    def not_done_moving(self):
        for i in self.customers:
            if i.finished == False:
                return True
        return False
    def run(self):
        while self.not_done_moving() or len(self.elevator.customers) != 0:
            #People Get Off
            flag = 0
            for i in self.elevator.customers:
                if self.elevator.current_floor == i.destination_floor:
                    self.elevator.customer_exits(i)
                    self.customers.append(i)
                    self.elevator.customers.remove(i)
                    flag = 1
                    break
            #People Get On
            for i in self.customers:
                if self.elevator.current_floor == i.start_floor and i.finished == False:
                    if self.elevator.current_floor == i.destination_floor:
                        print_same_goal(i.ID, self.elevator.current_floor)
                        i.finished = True
                    elif self.elevator.direction == i.direction:
                        self.elevator.customer_enters(i)
                        self.customers.remove(i)
                        flag = 1
                        break
            #Elevator Moves up
            if flag == 0:
                self.elevator.move(self.number_of_floors)
class Elevator:
    def __init__(self):
        self.data = []
        self.customers = []
        self.current_floor = 0
        self.direction = 1 # 1 is up, -1 is down
    def __str__(self):
        return 'current_floor: ' + str(self.current_floor) + ' direction: ' + str(self.direction) + " customers: [" + "][".join(str(v) for v in self.customers) + "]"
    def move(self,number_of_floors):
        if self.current_floor == 0 and self.direction == -1:
            self.direction = 1
        elif self.current_floor == number_of_floors - 1 and self.direction == 1:
            self.direction = -1
        else: 
            self.current_floor += self.direction
    def customer_enters(self,customer):
    	print_entrance(customer.ID, customer.start_floor)
        customer.in_elevator = True
        self.customers.append(customer)
    def customer_exits(self,customer):
        print_exit(customer.ID, customer.destination_floor)
        customer.in_elevator = False
        customer.finished = True
class Customer:
    def __init__(self,ID,floors):
        self.data = []
        self.start_floor = random.randint(0,floors-1) #10 floors including lobby
        self.destination_floor = random.randint(0,floors-1)
        self.ID = ID
        if self.start_floor > self.destination_floor:
            self.direction = -1
        else:
            self.direction = 1
        self.in_elevator = False
        self.finished = False
    def __str__(self):
        return 'start:%d end:%d ID:%d direction:%d elevator:%d finished:%d' % (self.start_floor, self.destination_floor, self.ID, self.direction, self.in_elevator, self.finished)
def Main():
    floors = 10
    #random.seed(floors)
    customers = []
    for i in range(0,floors):
        customers.append(Customer(i,floors))
    print_init(customers)
    Building(floors,customers,Elevator()).run()
if __name__ == '__main__':
    Main()