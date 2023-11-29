#!/bin/bash
# Aufruf:   .\pluto.ps1

release='verlag'
cd ./$release || exit 1
helm template $release . -f values.yaml -f dev.yaml > /tmp/$release.yaml
/home/florian/Zimmermann/pluto/pluto detect /tmp/$release.yaml
cd ..
rm -f /tmp/$release.yaml
