#!/bin/bash
# Aufruf:   .\dive.sh
# ggf. vorher:  Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
# oder:         Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope CurrentUser

base='buildpacks'

diveVersion='v0.11.0'
imagePrefix='floriansauer/'
imageBase='verlag'
imageTag="2023.10.0-$base"
image="$imagePrefix${imageBase}:$imageTag"

# https://github.com/wagoodman/dive#installation
docker run --rm --interactive --tty \
  --mount type='bind,source=/var/run/docker.sock,destination=/var/run/docker.sock' \
  --hostname dive --name dive \
  --read-only --cap-drop ALL \
  wagoodman/dive:$diveVersion $image
