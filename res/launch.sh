#!/bin/bash

cd $1
python -m SimpleHTTPServer &

rmid -log $1/log -J-Djava.rmi.server.codebase=http://localhost:8000/common/ -J-Djava.security.policy=$1/common/rmid.policy -port 5551
