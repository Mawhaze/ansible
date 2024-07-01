FROM ubuntu:24.04

# Set environment variables to avoid user interaction during installation
ENV DEBIAN_FRONTEND=noninteractive

# Update and install software-properties-common to add PPAs, then install Ansible dependencies
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y software-properties-common python3-pip python3-venv openssh-client && \
    apt-add-repository --yes --update ppa:ansible/ansible && \
    apt-get update && \
    apt-get install -y ansible

# Create a non-root user for running Ansible
RUN useradd -m -s /bin/bash sa-ansible

# Set working directory to sa-ansible home
WORKDIR /home/sa-ansible

# Accept SSH keys as build arguments
ARG SSH_PRIVATE_KEY
ARG SSH_PUBLIC_KEY

# Create SSH directory and write keys to files
RUN mkdir .ssh && \
    echo "$SSH_PRIVATE_KEY" > .ssh/id_ed25519 && \
    echo "$SSH_PUBLIC_KEY" > .ssh/id_ed25519.pub && \
    chown -R sa-ansible:sa-ansible .ssh && \
    chmod 600 .ssh/id_ed25519 && \
    chmod 644 .ssh/id_ed25519.pub && \
    echo "Host *\n\tStrictHostKeyChecking no\n" > .ssh/config

# Configure SSH
RUN echo "Host *\n\tStrictHostKeyChecking no\n" >> /home/sa-ansible/.ssh/config

# Copy in required files
COPY ./ansible.cfg /etc/ansible/ansible.cfg
COPY ./inventories /etc/ansible/inventories
COPY ./playbooks /etc/ansible/playbooks
COPY ./roles /etc/ansible/roles

# Change ownership of /etc/ansible to sa-ansible user
RUN chown -R sa-ansible:sa-ansible /etc/ansible

# Switch to sa-ansible user
USER sa-ansible

# Create a virtual environment for Ansible under the sa-ansible user's home directory
RUN python3 -m venv ansible-venv

# Activate the virtual environment and install Ansible, boto3, botocore
RUN . ansible-venv/bin/activate && \
    pip install --upgrade pip && \
    pip install ansible boto3 botocore proxmoxer requests

# Install the required collections
RUN . ansible-venv/bin/activate && \
    ansible-galaxy collection install community.general && \
    ansible-galaxy collection install amazon.aws && \
    ansible-galaxy collection install ansible.utils

# Set the working directory
WORKDIR /etc/ansible

# Use ENTRYPOINT to expect an Ansible command
ENTRYPOINT ["/bin/bash", "-c", "source /home/sa-ansible/ansible-venv/bin/activate && exec \"$@\"", "--"]
CMD ["ansible", "--version"]