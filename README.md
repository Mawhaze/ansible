# Ansible Readme

## Current State

- Rebuilding the homelab ansible environment with dynamic inventories and roles
- All playbooks will be ran via jenkins and configured in ./jenkins/jobdsl/job_config.groovy
  - This includes docker build jobs for ansible.

## Features to add

- Discord integration for notifications

## Playbooks to make

- K8s
  - Add to boot strap
    > storageclass, 

- Docker
  - maintenance - updates, image cleanup, etc.
  - nightly jenkins cleanup

- Proxmox
  - maintenance
  - node level updates

- Ubuntu
  - Change the update play to flag the VM with `reboot` and notify discord
  - build a reboot play that runs nightly looking for hosts tagged `reboot`

## Where I left off
