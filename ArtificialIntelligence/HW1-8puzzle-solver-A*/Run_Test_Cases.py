# A program for running test cases on an input/output process through the
# command line.
# By Yuriy Stejko
# Writen on 9/9/2013
# Last modified on 9/21/2013

import os
import sys
import subprocess
from time import sleep

#Ask for the test case file
print("Please type in the absolute location of the file containing the test cases.")
file_input = input(">> ")

#Ask the student to imput the command line instruction to execute his/her code
print("Please type the command line instruction that would be required to" + \
    " execute your program.")
command_line_input = input(">> ")

#Get the test data from the file
file = open(file_input, 'r')

tests = []

results_print = ""

while(True):
    #print indicators
    sys.stdout.write('.')
    sys.stdout.flush()
    #Get the current case number
    current_line = file.readline()
    while not (current_line[:4] == "Case"):
        if(current_line[:3] != "END"):
            current_line = file.readline()
        else:
            #Print the message indicating the results of the test cases.
            print("|")
            print("Test cases complete.")
            
            print("")
            #print any error messages
            if results_print == "":
                print("No test failure messages were issued.")
            else:
                print(results_print)
            
            print("")
            
            print(tests)
            
            result_counter = 0
            pass_counter = 0
            
            for result in tests:
                result_counter = result_counter + 1
                if result[1] == "Passed":
                    pass_counter = pass_counter + 1
            
            print("")
            
            print(str(pass_counter) + " of " + str(result_counter) + " tests passed.")
            sys.exit()
    
    number_string = current_line[5:].replace(":", "")
    test_number = int(number_string)
    
    test_case_failed = False
    
    #Check for properly formed test case file
    current_line = file.readline()
    if current_line[:6] != "Input:":
        print("Malformed test case file: '",current_line, "' was expecting 'Input:'")
        sys.exit()
    
    puzzle_line_1 = file.readline()
    puzzle_line_2 = file.readline()
    puzzle_line_3 = file.readline()
    
    #load string to test the program with
    test_string = (puzzle_line_1 + puzzle_line_2 + puzzle_line_3).rstrip()
    
    #starts the process
    process_to_test = subprocess.Popen(command_line_input , stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=None, shell=False)
    
    #sends in the initial data
    output ,error = process_to_test.communicate(bytes(test_string, 'UTF-8'))
    
    #Convert the result of the program to a string
    program_run_result = output.decode("utf-8")
    
    #Then to a list
    program_run_result_lines = program_run_result.splitlines()
    
    #Check for properly formed test case file
    current_line = file.readline()
    if current_line[:7] != "Output:":
        print("Malformed test case file: '",current_line, "' was expecting 'Output:'")
        sys.exit()
    
    #load the expected puzzle
    expected_puzzle_line_1 = (file.readline()).rstrip('\n')
    expected_puzzle_line_2 = (file.readline()).rstrip('\n')
    expected_puzzle_line_3 = (file.readline()).rstrip('\n')
    
    #load the result puzzle
    result_puzzle_line_1 = program_run_result_lines[0]
    result_puzzle_line_2 = program_run_result_lines[1]
    result_puzzle_line_3 = program_run_result_lines[2]
    
    #Compare
    if expected_puzzle_line_1 == result_puzzle_line_1 and \
        expected_puzzle_line_2 == result_puzzle_line_2 and \
        expected_puzzle_line_3 == result_puzzle_line_3:
        #If the first result is correct pop it from the return data
        program_run_result_lines.pop(0)
        program_run_result_lines.pop(0)
        program_run_result_lines.pop(0)
    else:
        program_run_result_lines.pop(0)
        program_run_result_lines.pop(0)
        program_run_result_lines.pop(0)
        results_print += "Test case " + str(test_number) + " Failed: Expecting first puzzle output to be the " +  "initial state: \n"
        results_print += '"' + expected_puzzle_line_1 + '"' + "\n"
        results_print += '"' + expected_puzzle_line_2 + '"' + "\n"
        results_print += '"' + expected_puzzle_line_3 + '"' + "\n"
        results_print += "Received: \n"
        results_print += '"' + result_puzzle_line_1 + '"' + "\n"
        results_print += '"' + result_puzzle_line_2 + '"' + "\n"
        results_print += '"' + result_puzzle_line_3 + '"' + "\n"
        results_print += 'The quotation marks (") are not part of the output, they are'+\
        ' only added to demonstrate whitespace diffrences. \n'
        test_case_failed = True
    
    poping_blank_line = (file.readline()).rstrip()
    
    length_of_path = int((file.readline()).rstrip())
    
    loop_counter = 0
    while(length_of_path > loop_counter):
        #check to make sure that the not all of the printed path is passed through
        if not program_run_result_lines:
            if test_case_failed == False:
                results_print += "Test case "+number_string+" Failed: path generated too short. \n"
            test_case_failed = True
        else:
            current_result_line = program_run_result_lines[0]
        
        # if the line is a common skip line/seperator format, ignore it (Blank, -,_,=,+)
        if ((len(current_result_line.rstrip().lstrip()) == 0 or \
            current_result_line[:1] == "-" or current_result_line[:1] == "_" or \
            current_result_line[:1] == "=" or current_result_line[:1] == "+")\
            and test_case_failed == False):
            program_run_result_lines.pop(0)
        
        # else extract the next step from the equation
        if not program_run_result_lines:
            if test_case_failed == False:
                results_print += "Test case "+number_string+" Failed: path generated too short. \n"
            test_case_failed = True
        else:
            program_run_result_lines.pop(0)
        if not program_run_result_lines:
            if test_case_failed == False:
                results_print += "Test case "+number_string+" Failed: path generated too short. \n"
            test_case_failed = True
        else:
            program_run_result_lines.pop(0)
        if not program_run_result_lines:
            if test_case_failed == False:
                results_print += "Test case "+number_string+" Failed: path generated too short. \n"
            test_case_failed = True
        else:
            program_run_result_lines.pop(0)
        
        loop_counter = loop_counter + 1
    
    #check for the ending state:
    if not program_run_result_lines:
        if test_case_failed == False:
            results_print += "Test case "+number_string+" Failed: path generated too short. \n"
        test_case_failed = True
    else:
        current_result_line = program_run_result_lines[0]
    
    #check if there are any lines left
    if not program_run_result_lines:
        if test_case_failed == False:
            results_print += "Test case "+number_string+" Failed: path generated too short. \n"
        test_case_failed = True
    
    # if the line is a common skip line/seperator format, ignore it (Blank, -,_,=,+)
    if ((len(current_result_line.rstrip().lstrip()) == 0 or \
        current_result_line[:1] == "-" or current_result_line[:1] == "_" or \
        current_result_line[:1] == "=" or current_result_line[:1] == "+")\
        and test_case_failed == False):
        program_run_result_lines.pop(0)
    
    poping_blank_line = (file.readline()).rstrip()
    
    #load the expected puzzle
    expected_puzzle_line_1 = (file.readline()).rstrip('\n')
    expected_puzzle_line_2 = (file.readline()).rstrip('\n')
    expected_puzzle_line_3 = (file.readline()).rstrip('\n')
    
    #load the result puzzle
    #check if there are any lines left
    if not program_run_result_lines:
        if test_case_failed == False:
            results_print += "Test case "+number_string+" Failed: path generated too short. \n"
        test_case_failed = True
    else:
        result_puzzle_line_1 = program_run_result_lines[0]
    #check if there are any lines left
    if not program_run_result_lines:
        if test_case_failed == False:
            results_print += "Test case "+number_string+" Failed: path generated too short. \n"
        test_case_failed = True
    else:
        result_puzzle_line_2 = program_run_result_lines[1]
    #check if there are any lines left
    if not program_run_result_lines:
        if test_case_failed == False:
            results_print += "Test case "+number_string+" Failed: path generated too short. \n"
        test_case_failed = True
    else:
        result_puzzle_line_3 = program_run_result_lines[2]
    
    #Compare
    if expected_puzzle_line_1 == result_puzzle_line_1 and \
        expected_puzzle_line_2 == result_puzzle_line_2 and \
        expected_puzzle_line_3 == result_puzzle_line_3 and \
        test_case_failed == False:
        #If the first result is correct pop it from the return data
        program_run_result_lines.pop(0)
        program_run_result_lines.pop(0)
        program_run_result_lines.pop(0)
    elif test_case_failed == False:
        program_run_result_lines.pop(0)
        program_run_result_lines.pop(0)
        program_run_result_lines.pop(0)
        results_print += "Test case " + str(test_number) + " Failed: Expecting final puzzle output to be:\n"
        results_print += '"' + expected_puzzle_line_1 + '"' + "\n"
        results_print += '"' + expected_puzzle_line_2 + '"' + "\n"
        results_print += '"' + expected_puzzle_line_3 + '"' + "\n"
        results_print += "Received: \n"
        results_print += '"' + result_puzzle_line_1 + '"' + "\n"
        results_print += '"' + result_puzzle_line_2 + '"' + "\n"
        results_print += '"' + result_puzzle_line_3 + '"' + "\n"
        results_print += "This may be the result of the path being too long. \n"
        results_print += 'The quotation marks (") are not part of the output, they are'+\
        ' only added to demonstrate whitespace diffrences. \n'
        test_case_failed = True
    
    if test_case_failed:
        tests.append(("Test " + str(test_number), "Failed"))
    else:
        tests.append(("Test " + str(test_number), "Passed"))
