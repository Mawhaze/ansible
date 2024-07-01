// Create the root dsl seed job
job('root-dsl-seed') {
  description('Seed job for creating Jenkins jobs from DSL')
  logRotator {
    numToKeep(10)
  }
  scm {
    git {
      remote {
        url('https://github.com/Mawhaze/ansible.git')
        name('origin')
      }
      branch('main')
    }
  }
  triggers {
    scm('H/5 * * * *')
  }
  steps {
    dsl {
      external('ansible/jenkins/jobdsl/job_config.groovy')
    }
  }

// Create the Ansible folder and job structure
folder('ansible') {
  description('Ansible jobs')
}

folder('ansible/playbooks') {
  description('Ansible playbook jobs')
}

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
                    [$class: 'StringBinding', credentialsId: 'sa_ansible_aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'],
                    [$class: 'StringBinding', credentialsId: 'sa_ansible_aws_secret_access_key', variable: 'AWS_SECRET_ACCESS_KEY']
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
        """).sandbox(true)
      }
    }
  }
}