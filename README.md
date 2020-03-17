# README

### Ansible management of homelab
- pull project to /etc/ansible
### Current State
Initial set up and first steps
- Currently managed hosts | groups
  1. CDocker01            | 1. ubuntu
  2. CNetServ01           | 2. proxmox
  3. WDevTest01           | 3. docker
  4. CDevTest01           | 4. bots
  5. Dev
  6. Workstation
  7. Storage
  8. WBot01
  9. WBot02
- Playbooks
  1. ubuntu-update.yml
     - Updates ubuntu OS for the following hosts
       > Cdocker01, CNetServ01, CDevTest01, WDevTest01
  2. container-update.yml
  3. prox-update.yml
     - Updates Proxmox/Debian OS for the following hosts
       > Dev, Workstation, Storage
  4. serverdeploy.yml
     - The usable version of test.servertools.yml
       > run with --extra-vars "variable_host=TARGETHOST"
  5. test.servertools.yml
     - Updates OS, installs Docker and compose and elevates command privlages

### Future Plans
1. finish test.servertools
  - add docker compose and test on test nodes
    > WBot01, WBot02
