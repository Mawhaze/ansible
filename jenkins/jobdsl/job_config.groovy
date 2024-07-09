// Create the Ansible folder and job structure
folder('ansible/playbooks') {
  description('Ansible playbook jobs')
}

// Ansible job definitions
// Keep this alphabetical for easier maintenance
// Define the ubuntu_os_updates
pipelineJob('ansible/playbooks/ubuntu_os_updates') {
  definition {
    cps {
      // Inline Groovy script for pipeline definition
      script("""
pipeline {
  agent any
  stages {
      stage('Sign into DockerHub and Pull Docker Image') {
          steps {
              script {
                  docker.withRegistry('https://index.docker.io/v1/', 'dockerhub_credentials') {
                      // Pull the Docker image from DockerHub before running it
                      sh "docker pull mawhaze/ansible:latest"
                  }
              }
          }
      }
      stage('Run Ansible Playbook') {
          steps {
              withCredentials([
                  string(credentialsId: 'sa_ansible_aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'),
                  string(credentialsId: 'sa_ansible_aws_secret_access_key', variable: 'AWS_SECRET_ACCESS_KEY')
              ]) {
                  sh(
                      'docker run -e AWS_ACCESS_KEY_ID=\$AWS_ACCESS_KEY_ID -e AWS_SECRET_ACCESS_KEY=\$AWS_SECRET_ACCESS_KEY \
                      mawhaze/ansible:latest \
                      ansible-playbook -i /etc/ansible/inventories/inventory.proxmox.yml /etc/ansible/playbooks/ubuntu/os_updates.yml'
                  )
              }
          }
      }
  }
}
      """)
    }
  }
}

// Docker jobs for Ansible definitions
// Define the ansible_docker build job within the docker/build folder
pipelineJob('docker/build/ansible_docker') {
  description('Build the Ansible Docker image from a Jenkinsfile')
  definition {
    cpsScm {
      scm {
        git {
          remote {
            url('https://github.com/mawhaze/ansible.git')
            credentials('github_access_token')
          }
          branches('*/master')
          scriptPath('Jenkinsfile')
        }
      }
    }
  }
  triggers {
    scm('H/15 * * * *') // Poll SCM every 15 minutes.
  }
}