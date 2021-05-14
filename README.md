# README

### Current State
- Groups
  1. winbots
  2. bots (ubuntu)
- Playbooks
  1. Windows
      > winDsktpBootstrap 
  2. Ubuntu 
      > ubuntuServerBootstrap
- Plays
  1. Windows
      > updateOS \
      > configureWSL2 \
      > getHostInfo
  2. Ubuntu
      > getHostInfo \
      > updateOS

### How to run:
1. ansible-playbook -i HOSTFILE playbooks/windows/TARGET_PLAYBOOK.yml

### Future Plans
1. Integration with Project: Ender. 
2. Docker admin plays
3. Windows integration
4. Linux server admin
5. workstation bootstrap (linux and windows)

### Next Steps
- Important
  1. Build out admin plays
  2. Integrate with P:E
- Meh
  1. 

### Where i left off
- adding windows into ansible
- both gather facts plays now write host info to logs/ansible/HOSTNAME.json on the file server

