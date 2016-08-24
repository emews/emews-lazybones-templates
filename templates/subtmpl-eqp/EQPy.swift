import location;
pragma worktypedef resident_work;

@dispatch=resident_work
(void v) _void_py(string code) "turbine" "0.1.0"
    [ "turbine::python 1 <<code>>" ];
    
@dispatch=resident_work
(string output) _string_py(string code) "turbine" "0.1.0"
    [ "set <<output>> [ turbine::python 1 <<code>> ]" ];

string init_package_string = "import eqp\nimport %s\n" +
"import threading\n" +
"p = threading.Thread(target=%s.run)\np.start()\n\"\"";


(void v) EQP_init_package(location loc, string packageName){
    // printf("EQP_init_package called");
    string code = init_package_string % (packageName,packageName);
    // printf("Code is: \n%s", code);
    @location=loc _void_py(code) => v = propagate();
}

EQP_stop(location loc){
    // do nothing
}

string get_string = "result = eqp.output_q.get()\nresult";

(string result) EQP_get(location loc){
    // printf("EQP_get called");
    string code = get_string;
    result = @location=loc _string_py(code);
}

string put_string = """
eqp.input_q.put('%s')\n""
""";

(void v) EQP_put(location loc, string data){
    // printf("EQP_put called with: \n%s", data);
    string code = put_string % data;
    // printf("EQP_put code: \n%s", code);
    @location=loc _void_py(code) => v = propagate();
}   

// Local Variables:
// c-basic-offset: 4
// End:
