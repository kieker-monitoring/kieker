#!/usr/bin/env python

import argparse
import itertools
import os
import platform
import time
from subprocess import *


######################################################################
# CONSTANTS 
######################################################################
# default disl_home value, relative to the script
DEFAULT_DISL_HOME = "../../"
# string to be substituted by the actual value of DISL_HOME in paths
VARIABLE_DISL_HOME = "${DISL_HOME}"


######################################################################
# DISL_HOME
#
# @return
#	DISL_HOME to be used in paths. Either default or from env. 
######################################################################
def disl_home():
	if os.getenv("DISL_HOME") is None:
		return DEFAULT_DISL_HOME
	else:
		return os.getenv("DISL_HOME")


######################################################################
# LIB_SUFFIX
######################################################################
def lib_suffix():
	if platform.system() == "Darwin":	
		return ".jnilib"
	else:
		return ".so"

######################################################################
# GENERAL_PARSER 
######################################################################
def general_parser(parser):
	# positional variant of i for simplicity
	# both cannot be set at once	
	parser.add_argument("instr", 
		default=None, 
		nargs="?",
		help="path to jar containing disl instrumentation code, same as '-i'")
	
	# positional variant of c_app for simplicity
	# both cannot be set at once	
	parser.add_argument("app", 
		default=None, 
		nargs="*",
		help="client jar or class and its arguments, same as '-c_app'")
	
	parser.add_argument("-d", 
		default=disl_home(),
		dest="disl_home",
		help="disl home directory")

	parser.add_argument("-cs",
		action="store_true",
		default=True,
		help="set to start client and server")
	
	parser.add_argument("-cse",
		action="store_true",
		default=False,
		help="set to start client, server and remote evaluation")
	
	parser.add_argument("-i", 
		dest="instrumentation",
		default=None, 
		metavar="PATH",
		help="path to jar containing disl instrumentation code, same as 'instr'")

	parser.add_argument("-t",
		dest="test_dir",
		default=None,
		metavar="PATH",
		help="directory to run in, default=current")
	
	#parser.add_argument("-p",
	#	action="store_true",
	#	default=False,
	#	dest="print_namespace",
	#	help="show all set options and default values")
	return
	

######################################################################
# CLIENT_PARSER 
######################################################################
def client_parser(parser):
	group = parser.add_argument_group("CLIENT")

	group.add_argument("-c_opts",
		action="append",
		default=[],
		metavar="A",
		nargs="+",
		help="java options of the client application")
	
	group.add_argument("-c_app",
		action="append",
		default=None,
		metavar="A",
		nargs="+",
		help="client jar or class and its arguments, same as 'app'")
	
	#############################
	# additional arguments follow
	#############################
	
	group.add_argument("-c_out", 
		default=None, 
		metavar="PATH",
		help="file to save client stdout to")
	
	group.add_argument("-c_err", 
		default=None, 
		metavar="PATH",
		help="file to save client stderr to")
	
	return


######################################################################
# SERVER_PARSER 
######################################################################
def server_parser(parser):
	group = parser.add_argument_group("SERVER")
	
	group.add_argument("-s_opts",
		action="append",
		default=[],
		metavar="A",
		nargs="+",
		help="java options of the server")

	group.add_argument("-s_args",
		action="append",
		default=[],
		metavar="A",
		nargs="+",
		help="arguments to the server application")
	
	group.add_argument("-s_out", 
		default=None, 
		metavar="PATH",
		help="file to save server stdout to")
	
	group.add_argument("-s_err", 
		default=None, 
		metavar="PATH",
		help="file to save server stderr to")
	
	#############################
	# additional arguments follow
	#############################

	group.add_argument("-s_debug", 
		action="store_true",
		default=False,
		help="enable debug output")

	group.add_argument("-s_noexcepthandler", 
		action="store_true",
		default=False,
		help="does not instrument exception handler (improves performance but does not protect from errors within instrumentation)")

	group.add_argument("-s_exclusionlist", 
		default=None,
		metavar="PATH",
		help="path to exclusion list")

	group.add_argument("-s_instrumented", 
		default=None,	
		metavar="PATH",
		help="dumps instrumented classes into specified directory")
	
	group.add_argument("-s_uninstrumented", 
		default=None,	
		metavar="PATH",
		help="dumps uninstrumented classes into specified directory")

	group.add_argument("-s_port", 
		default=None,	
		metavar="PORT",
		help="listening network port")

	return
	

######################################################################
# EVALUATION_PARSER 
######################################################################
def evaluation_parser(parser):
	group = parser.add_argument_group("EVALUATION")
	
	group.add_argument("-e_opts",
		action="append",
		default=[],
		nargs="+",
		metavar="A", 
		help="java options of the evaluation server")
	
	group.add_argument("-e_args",
		action="append",
		default=[],
		nargs="+",
		metavar="A", 
		help="arguments to the evaluation server application")
	
	group.add_argument("-e_out", 
		default=None, 
		metavar="PATH",
		help="file to save evaluation stdout to")
	
	group.add_argument("-e_err", 
		default=None, 
		metavar="PATH",
		help="file to save evaluation stderr to")

	#############################
	# additional arguments follow
	#############################

	group.add_argument("-e_debug", 
		action="store_true",
		default=False,
		help="enable debug output")

	group.add_argument("-e_port", 
		default=None,	
		metavar="PORT",
		help="listening network port")

	return


######################################################################
# DOCUMENTATION_PARSER 
######################################################################
def documentation_parser(parser):

	parser.formatter_class=argparse.RawTextHelpFormatter

	parser.description = """
This script is a DiSL client application, server and evaluation server starter.

By default a client application that will be instrumented and the server that 
will instrument the application will be started. To start remote evaluation
server too. Specify '-cse' option.

To pass option like arguments (starting with dash) one must either use equal 
sign or positional variant of the argument if it's present. For example 
'-c_app="-jar"' instead of '-c_app "-jar"'. The positional version must be 
preceded with '--' as in the example.

The '-d' option specifies where the DiSL framework is installed. In some cases 
it might work thanks to default relative path. In other cases one must either 
specify the correct location at the command line or set 'DISL_HOME' system 
variable. 
"""

	parser.epilog = """
EXAMPLES:

To execute the example application run following:
	./disl.py -- instr/build/disl-instr.jar -jar app/build/example-app.jar
or	
	./disl.py -cs -i=instr/build/disl-instr.jar -c_app=-jar c_app=app/build/example-app.jar
"""

######################################################################
# MAKE_PARSER 
######################################################################
def make_parser():
	parser = argparse.ArgumentParser()
	general_parser(parser)
	client_parser(parser)
	server_parser(parser)	
	evaluation_parser(parser)	
	documentation_parser(parser)
	
	return parser


######################################################################
# FLATTEN_ALL
#	Makes a list from nested lists or even a single non list
#	argument. Does not slice strings.
######################################################################
def flatten_all(object):
	if object is None:
		return None

	if isinstance(object, basestring):
		return [object]

	result = []
	for x in object:
		if hasattr(x, "__iter__") and not isinstance(x, basestring):
			result.extend(flatten_all(x))
		else:
			result.append(x)
	return result

	#return list(itertools.chain.from_iterable(object))


######################################################################
# PARSE_ARGUMENTS 
######################################################################
def parse_arguments(parser):
	args = parser.parse_args()

	args.c_opts = flatten_all(args.c_opts)
	args.c_app = flatten_all(args.c_app)
	args.app = flatten_all(args.app)

	args.s_opts = flatten_all(args.s_opts)
	args.s_args = flatten_all(args.s_args)
	if args.s_debug is True:
		args.s_opts+= ["-Ddebug=true"]
	if args.s_port is not None:
		args.s_opts+= ["-Ddislserver.port="+args.s_port]
	if args.s_noexcepthandler is True:
		args.s_opts+= ["-Ddisl.noexcepthandler"]
	if args.s_exclusionlist is not None:
		args.s_opts+= ["-Ddisl.exclusionList="+args.s_exclusionlist]
	if args.s_instrumented is not None:
		args.s_opts+= ["-Ddislserver.instrumented="+args.s_instrumented]
	if args.s_uninstrumented is not None:
		args.s_opts+= ["-Ddislserver.uninstrumented="+args.s_uninstrumented]

	args.e_opts = flatten_all(args.e_opts)
	args.e_args = flatten_all(args.e_args)
	if args.e_debug is True:
		args.e_opts+= ["-Ddebug=true"]
	if args.e_port is not None:
		args.e_opts+= ["-Ddislreserver.port="+args.e_port]

	# supply instrumentation from positional instr if set
	if args.instrumentation is not None and args.instr is not None:
		parser.error("-i and instr set both")	
	if args.instr is not None:
		args.instrumentation = args.instr

	# supply c_app from positional app if set
	if args.c_app is not None and args.app is not None:
		parser.error("-c_app and app set both")	
	if args.app is not None:
		args.c_app = args.app
	
	#if args.print_namespace:
	#	for key in args.__dict__:
	#		value = args.__dict__[key]
	#		print key + "=" + str(value)

	return args


######################################################################
# TRY_KILL
######################################################################
def try_kill(pid_file_name):
	try:
		with open(os.devnull, "w") as devnull:
			with open(pid_file_name, "r") as pid_file:
				pid = pid_file.readline()
				kill = Popen(["kill", pid], stdout=devnull, stderr=devnull, shell=False)
	except IOError:
		pass


######################################################################
# TRY_REMOVE_FILE
######################################################################
def try_remove_file(file_name):
	if os.path.isfile(file_name):
		os.remove(file_name)


######################################################################
# RUN
######################################################################
def run(cmd, out, err):
	out_f = None
	err_f = None	
	if out is not None:
		out_f = open(out, "w")
	if err is not None:
		err_f = open(err, "w")
	
	process = Popen(cmd, stdout=out_f, stderr=err_f, shell=False)	
	return process	


######################################################################
# RUN_SERVER
######################################################################
def run_server(args, parser):
	if args.instrumentation is None:
		parser.error("argument instr (-i) is required to run the client")	

	try_kill(".server.pid")

	s_jar = args.disl_home+"/build/disl-server.jar"
	s_class = "ch.usi.dag.dislserver.DiSLServer" 

	s_cmd = ["java"]
	s_cmd+= args.s_opts
	s_cmd+= ["-cp", args.instrumentation + ":" + s_jar]
	s_cmd+= [s_class]
	s_cmd+= args.s_args

	#print s_cmd

	server = run(s_cmd, args.s_out, args.s_err)
	
	with open(".server.pid", "w") as pid_file:
		pid_file.write(str(server.pid))
	
	return


######################################################################
# RUN_EVALUATION
######################################################################
def run_evaluation(args, parser):
	if args.instrumentation is None:
		parser.error("argument instr (-i) is required to run the client")	

	try_kill(".evaluation.pid")

	e_jar = args.disl_home+"/build/dislre-server.jar"
	e_class = "ch.usi.dag.dislreserver.DiSLREServer" 

	e_cmd = ["java"]
	e_cmd+= args.s_opts
	e_cmd+= ["-Xms1G", "-Xmx2G"]
	e_cmd+= ["-cp", args.instrumentation + ":" + e_jar]
	e_cmd+= [e_class]
	e_cmd+= args.e_args

	#print e_cmd
	
	evaluation = run(e_cmd, args.e_out, args.e_err)
	
	with open(".evaluation.pid", "w") as pid_file:
		pid_file.write(str(evaluation.pid))

	return


######################################################################
# RUN_CLIENT
######################################################################
def run_client(args, parser):
	if args.c_app is None:
		parser.error("argument app (-c_app) is required to run the client")	
	
	if args.instrumentation is None:
		parser.error("argument instr (-i) is required to run the client")	

	cagent = args.disl_home+"/build/libdislagent"+lib_suffix()
	eagent = args.disl_home+"/build/libdislreagent"+lib_suffix()
	jagent = args.disl_home+"/build/disl-agent.jar"
	dispatch = args.disl_home+"/build/dislre-dispatch.jar"

	c_cmd = ["java"]
	c_cmd+= args.c_opts
	c_cmd+= ["-agentpath:"+cagent]
	
	if args.cse == True:
		c_cmd+= ["-agentpath:"+eagent]

	c_cmd+= ["-javaagent:"+jagent]

	if args.cse == True:
		c_cmd+= ["-Xbootclasspath/a:"+jagent+":"+args.instrumentation+":"+dispatch]
	else:
		c_cmd+= ["-Xbootclasspath/a:"+jagent+":"+args.instrumentation]

	c_cmd+= args.c_app

	#print c_cmd

	client = run(c_cmd, args.c_out, args.c_err)	
	client.wait()

	# let server and evaluation finish
	time.sleep(1)
	
	try_kill(".server.pid")
	try_kill(".evaluation.pid")
	try_remove_file(".server.pid")
	try_remove_file(".evaluation.pid")

	return


######################################################################
# MAIN
######################################################################
def main():
	parser = make_parser()
	args = parse_arguments(parser)

	if args.test_dir is not None:
		os.chdir(args.test_dir)

	if args.cs == True:
		run_server(args, parser)
	
	if args.cse == True:
		run_evaluation(args, parser)

	if args.cs == True or args.cse == True:
		time.sleep(3)
		run_client(args, parser)

	return


######################################################################
# ENTRY_POINT
######################################################################
if __name__ == "__main__":
	main()

