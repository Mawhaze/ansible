// Create the Ansible folder and job structure
folder('ansible/playbooks') {
  description('Ansible playbook jobs')
}

// Ansible job definitions
// Keep this alphabetical for easier maintenance
// Define the ubuntu_docker_container_updates
pipelineJob('ansible/playbooks/ubuntu_docker_container_updates') {
  logRotator {
    numToKeep(10) //Only keep the last 10
  }
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
                      ansible-playbook -i /etc/ansible/inventories/inventory.proxmox.yml /etc/ansible/playbooks/ubuntu/docker_updates.yml'
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

// Define the ubuntu_os_updates job
pipelineJob('ansible/playbooks/ubuntu_os_updates') {
  logRotator {
    numToKeep(10) //Only keep the last 10
  }
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

// Define the ubuntu_bootstrap job
pipelineJob('ansible/playbooks/ubuntu_bootstrap') {
  logRotator {
    numToKeep(10) //Only keep the last 10
  }
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
                      ansible-playbook -i /etc/ansible/inventories/inventory.proxmox.yml /etc/ansible/playbooks/ubuntu/bootstrap.yml'
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

// Define the ubuntu_deploy_k8s_cluster job
pipelineJob('ansible/playbooks/ubuntu_deploy_k8s_cluster') {
  logRotator {
    numToKeep(10) //Only keep the last 10
  }
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
                  ansible-playbook -i /etc/ansible/inventories/inventory.proxmox.yml /etc/ansible/playbooks/ubuntu/test_k8s.yml'
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

// Docker build job for Ansible
// Define the ansible_docker build job within the docker/build folder
pipelineJob('docker/build/ansible_docker') {
  description('Build the Ansible Docker image from a Jenkinsfile')
  logRotator {
    numToKeep(10) //Only keep the last 10
  }
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