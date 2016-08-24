
(void v) EQR_init_script(location L, string filename)
{
  v = @location=L EQR_tcl_initR(filename);
}


EQR_stop(location L)
{
  @location=L EQR_tcl_stop();
}


(string result) EQR_get(location L)
{
  result = @location=L EQR_tcl_get();
}


(void v) EQR_put(location L, string data)
{
  v = @location=L EQR_tcl_put(data);
}


@dispatch=WORKER
(void v) EQR_tcl_initR(string filename)
"eqr" "0.1"
[ "initR <<filename>>" ];

@dispatch=WORKER
EQR_tcl_stop()
"eqr" "0.1"
[ "stopIt ; deleteR" ];

@dispatch=WORKER
(string result) EQR_tcl_get()
"eqr" "0.1"
[ "set <<result>> [ OUT_get ]" ];

@dispatch=WORKER
(void v)
EQR_tcl_put(string data)
"eqr" "0.1"
[ "IN_put <<data>>" ];
