#!/bin/bash
# Aufruf:   .\polaris.ps1

# https://polaris.docs.fairwinds.com
echo 'Webbrowser mit http://localhost:8008 aufrufen'
echo 'Zu "Topology" siehe: https://kubernetes.io/docs/concepts/scheduling-eviction/topology-spread-constraints'
echo ''
/home/florian/Zimmermann/polaris/polaris dashboard --port 8008
