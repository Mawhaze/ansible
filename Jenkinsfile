pipeline {
    agent any

    environment {
        // Define the Docker image name
        IMAGE_NAME = "mawhaze/ansible"
        // Enable Docker BuildKit
        DOCKER_BUILDKIT = 1
    }

    triggers {
        // Trigger the pipeline on a push to the GitHub branch
        pollSCM('H/15 * * * *')
    }

    stages {
        stage('Checkout Code') {
            steps {
                // Checkout the code from GitHub
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    withCredentials([
                        sshUserPrivateKey(credentialsId: 'ansible_private_ssh_key', keyFileVariable: 'SSH_PRIVATE_KEY'),
                        string(credentialsId: 'ansible_public_ssh_key', variable: 'SSH_PUBLIC_KEY')
                    ]) {
                        echo "Running docker build command with SSH keys..."

                        // Execute the docker build command with secrets
                        sh """
                        docker build --no-cache --progress=plain \
                        --secret id=ssh_private_key,src=\${SSH_PRIVATE_KEY} \
                        --secret id=ssh_public_key,src=\${SSH_PUBLIC_KEY} \
                        -t ${env.IMAGE_NAME}:latest .
                        """
                    }
                }
            }
        }

        stage('Docker Login and Push') {
            steps {
                script {
                    // Use withCredentials to securely handle DockerHub login
                    withCredentials([
                        string(credentialsId: 'dockerhub_username', variable: 'DOCKERHUB_USERNAME'),
                        string(credentialsId: 'dockerhub_password', variable: 'DOCKERHUB_PASSWORD')
                    ]) {
                        // Login to DockerHub
                        sh 'echo $DOCKERHUB_PASSWORD | docker login --username $DOCKERHUB_USERNAME --password-stdin'
                        // Push the Docker image to DockerHub
                        sh "docker push ${IMAGE_NAME}:latest"
                        // Logout from DockerHub
                        sh "docker logout"
                    }
                }
            }
        }
    }

    post {
        always {
            sh "docker logout"
        }
    }
}