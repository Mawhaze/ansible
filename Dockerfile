FROM ubuntu:24.04

# Set environment variables to avoid user interaction during installation
ENV DEBIAN_FRONTEND=noninteractive

# Update and install software-properties-common to add PPAs, then install Ansible
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y software-properties-common && \
    apt-add-repository --yes --update ppa:ansible/ansible && \
    apt-get install -y ansible openssh-client

# Create Ansible user and SSH keys
RUN useradd -m -s /bin/bash ansible

COPY ansible_ed25519 /home/sa-ansible/.ssh/id_ed25519
COPY ansible_ed25519.pub /home/sa-ansible/.ssh/id_ed25519.pub

# Set permissions on SSH keys
RUN chown -R sa-ansible:sa-ansible /home/sa-ansible/.ssh && \
    chmod 600 /home/sa-ansible/.ssh/id_ed25519 && \
    chmod 644 /home/sa-ansible/.ssh/id_ed25519.pub

# Configure SSH
RUN echo "Host *\n\tStrictHostKeyChecking no\n" >> /home/ansible/.ssh/config

# Copy in required files
COPY ./ansible.cfg /etc/ansible/ansible.cfg
COPY ./inventories /etc/ansible/inventory
COPY ./playbooks /etc/ansible/playbooks
COPY ./roles /etc/ansible/roles

# Install the required collections
RUN ansible-galaxy collection install community.general
RUN ansible-galaxy collection install amazon.aws

# Verify installation
RUN ansible --version

# Set the working directory
WORKDIR /etc/ansible

# Command to run the Ansible inventory command
CMD ["ansible-inventory", "-i", "inventory.proxmox.yml", "--list"]