// Create the Ansible folder and job structure
folder('ansible') {
  description('Ansible jobs')
}

folder('ansible/playbooks') {
  description('Ansible playbook jobs')
}
// Create the Docker jobs for ansible
folder('docker') {
  description('Docker jobs')
}

folder('docker/build') {
  description('Docker build jobs')
}

// Ansible job definitions
// Define the ubuntu_os_updates pipeline job within the ansible folder
  pipelineJob('ansible/playbooks/ubuntu_os_updates') {
    definition {
      cps {
        // Inline Groovy script for pipeline definition
        script("""
pipeline {
    agent any
    stages {
        stage('Setup Credentials') {
            steps {
                // Correctly inject credentials
                withCredentials([
                    [\$class: 'StringBinding', credentialsId: 'sa_ansible_aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'],
                    [\$class: 'StringBinding', credentialsId: 'sa_ansible_aws_secret_access_key', variable: 'AWS_SECRET_ACCESS_KEY']
                ]) {
                    script {
                        echo "Credentials are set up."
                    }
                }
            }
        }
        stage('Pull Docker Image') {
            steps {
                script {
                    // Pull the Docker image from DockerHub before running it
                    sh "docker pull mawhaze/ansible:latest"
                }
            }
        }
        stage('Run Ansible Playbook') {
            steps {
                script {
                    // Run Docker command to execute Ansible playbook
                    docker.image('mawhaze/ansible:latest').inside("-e AWS_ACCESS_KEY_ID=\${env.AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=\${env.AWS_SECRET_ACCESS_KEY} -e AWS_DEFAULT_REGION=\${env.AWS_DEFAULT_REGION}") {
                        sh "ansible-playbook -i /etc/ansible/inventories/inventory.proxmox.yml /etc/ansible/playbooks/updates/os_updates.yml -vvv"
                    }
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
    scm('*/15 * * * *') // Poll SCM every 15 minutes.
  }
}