FROM ubuntu:22.04

LABEL Author="Evan Cast"
LABEL E-mail="evancast92@gmail.com"
LABEL version="0.0.1"

WORKDIR ~

COPY README.md ~/README.md

RUN apt update && apt upgrade -y