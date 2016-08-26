import threading
import queue
import sys

input_q = queue.Queue()
output_q = queue.Queue()

def OUT_put(string_params):
    output_q.put(string_params)
    
def IN_get():
    global input_q
    result = input_q.get()
    return result    

#     with open("pyout.txt", "a") as myfile:
#         myfile.write("In in get\n")
#     global input_q
#     with open("pyout.txt", "a") as myfile:
#         myfile.write("after global \n")
#     try:
#         result = input_q.get()
#     except Exception as inst:
#         with open("pyout.txt", "a") as myfile:
#             myfile.write(type(inst))
#        print(type(inst))    # the exception instance
#        print(inst.args)     # arguments stored in .args
#        print(inst)

#     with open("pyout.txt", "a") as myfile:
#         myfile.write("in IN_get\n")
#     return '2,1,0,"/project/jozik/repos/wintersim2016_adv_tut/uc2/swift_proj/data/params_for_deap.csv"'



#     global input_q
#     
#     with open("pyout.txt", "a") as myfile:
#         myfile.write("after global 2\n")
# 
# 
#     return "blah"
#     try:
#         with open("pyout.txt", "a") as myfile:
#             myfile.write("in try block\n")
# 
#         result = input_q.get()
#         with open("pyout.txt", "a") as myfile:
#             myfile.write("result is: {}\n",result)
#     finally:
#         with open("pyout.txt", "a") as myfile:
#             myfile.write("Unexpected error:")
#     return result
# 

