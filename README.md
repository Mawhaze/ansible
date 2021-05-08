# README

### Current State
- Groups
  1. winbots
- Playbooks
  1. Windows
      > winDsktpBootstrap 
- Plays
  1. Windows
      > updateOS \
      > configureWSL2 

### How to run:
- winDsktpBootstrap
1. ansible-playbook -i HOSTFILE playbooks/windows/TARGET_PLAYBOOK.yml
2. Required Variables
    > variable_host=TARGET_HOST_GROUP (default is winbots)

### Future Plans
1. Integration with Project: Ender. 
2. Docker compose playbook to pull and spin up new changed containers
3. Windows integration

### Next Steps
- Important
  1. Integrate with P:E
- Meh
  1. 

### Where i left off
- adding windows into ansible
  1. plays for installing desktop software windows
  2. creating a domain controller
