# README

### Ansible management of homelab
- pull project to /etc/ansible
### Current State
Initial set up and first steps
- Currently managed hosts
  1. CDocker01
  2. CNetServ01
  3. WDevTest01
  4. CDevTest01
  5. Dev
  6. Workstation
  7. Storage
- Playbooks
  1. ubuntu-update.yml
     - Updates ubuntu OS for the following hosts
       > Cdocker01, CNetServ01, CDevTest01, WDevTest01
  2. container-update.yml
  3. prox-update.yml
     - Updates Proxmox/Debian OS for the following hosts
       > Dev, Workstation, Storage

### Future Plans

