#!/bin/bash
javac -d ./classes -sourcepath ./src src/vttp/batch5/sdf/task01/Main.java && \
	java -cp classes vttp.batch5.sdf.task01.Main
