# Ansible Readme

## Current State

- Rebuilding the homelab ansible environment with dynamic inventories and roles
- All playbooks will be ran via jenkins and configured in ./jenkins/jobdsl/job_config.groovy
  - This includes docker build jobs for ansible.

## Playbooks to make

- Docker
  - maintenance - updates, image cleanup, etc.
  - build job for ansible

- Proxmox
  - maintenance
  - 

## Where I left off

- EOD - continue working on deploying ansible private key
