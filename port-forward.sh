#!/bin/bash

# Aufruf:   .\port-forward.sh
# ggf. vorher:  Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
# oder:         Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope CurrentUser
namespace='acme'
service='verlag'
kubectl port-forward service/$service 8080 --namespace $namespace
