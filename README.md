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
  - node level updates

- Ubuntu
  - Change the update play to flag the VM with `reboot`
  - build a reboot play that runs nightly looking for hosts tagged `reboot`

## Where I left off

- EOD - continue working on deploying ansible private key
