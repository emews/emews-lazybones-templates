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
