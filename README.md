# README

### Ansible in the Lab
As with all lab projects, the main purpose of Ansible is to learn how. However this project will be tied directly to Project: Ender, Ansible will be the main way Project: Ender is able to control opperations inside the lab. Any new playbooks will be tried out by hand, ran against the bots, and then adapted to be able to be ran by Ender. As a result, the capabilities of these playbooks will need to be exanded and refined. 

### Future Plans
1. Integration with Project: Ender. 
2. Docker compose playbook to pull and spin up new changed containers
3. Spin up new VMs on proxmox
  - use a disk image and provided specs
  - change host name in new VM
  - set static IP, probably always netplan
  - add new host to monitoring scrape list and update
4. flush out bootstrap playbook 
  - add k8s node 
  - add basic node
5. possibly some CI/CD stuff
6. add to update playbook to be aware of what containers are on the host
7. Maybe a telegram notification if the notification bot of Ender goes down
8. Find out what else can can be automated within the lab
9. Windows integration

### Next Steps
- Critical
  1. intigrade with project ender
  2. begin playbook expansion
  3. 
- Meh


### Current State
Initial set up and first steps
- Currently managed hosts | groups
  TBD

- Playbooks
  TBD

  ### Where i left off
  - adding windows into ansible
    1. update play worked but timed out at both steps
    2. add to the ansible notes section of homelab
    