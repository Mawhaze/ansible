# README

### How to run:
- winDsktpBootstrap
1. ansible-playbook -i HOSTFILE playbooks/TARGET_PLAYBOOK.yml
2. Required Variables
  > variable_host=TARGET_HOST_GROUP (default is winbots)

### Future Plans
1. Integration with Project: Ender. 
2. Docker compose playbook to pull and spin up new changed containers
3. Windows integration

### Next Steps
- Important
  1. intigrade with project ender
- Meh
  1. 

### Current State
Initial set up and first steps
- Currently managed groups
  1. winbots
  TBD

- Plays
  1. Windows
    > updateOS \
    > configureWSL2 \
- Playbooks
  1. Windows
    > winDsktpBootstrap \
  TBD

  ### Where i left off
  - adding windows into ansible
    1. plays for installing software windows
