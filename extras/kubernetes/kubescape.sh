#!/bin/bash
# Aufruf:   .\kubescape.ps1


release='verlag'
cd ./$release || exit 1
helm template $release . -f values.yaml -f dev.yaml > /tmp/$release.yaml
/home/florian/Zimmermann/kubescape/kubescape scan --verbose /tmp/$release.yaml
cd ..
rm -f /tmp/$release.yaml
