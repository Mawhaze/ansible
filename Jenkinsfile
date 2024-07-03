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
        
        stage('Pre-Build Debug') {
            steps {
                script {
                    // Print Docker version to ensure compatibility
                    sh "docker --version"
                    // Check if DOCKER_BUILDKIT is enabled in the environment
                    sh "echo DOCKER_BUILDKIT=$DOCKER_BUILDKIT"
                    // Optionally, print Docker info for more detailed debugging
                    sh "docker info"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    withCredentials([
                        sshUserPrivateKey(credentialsId: 'ansible_private_ssh_key', keyFileVariable: 'SSH_PRIVATE_KEY'),
                        string(credentialsId: 'ansible_public_ssh_key', variable: 'SSH_PUBLIC_KEY')
                    ]) {
                        // Write the SSH key content to a temporary file
                        sh "echo \${SSH_PUBLIC_KEY} > ssh_public_key.tmp"
                        writeFile file: 'ssh_private_key.tmp', text: env.SSH_PRIVATE_KEY

                        echo "Running docker build command with SSH key files..."

                        // Execute the docker build command with secrets, using temporary file paths
                        sh """
                        docker buildx build --no-cache --progress=plain \
                        --secret id=ssh_private_key,src=ssh_private_key.tmp \
                        --secret id=ssh_public_key,src=ssh_public_key.tmp \
                        -t ${env.IMAGE_NAME}:latest .
                        """
                        // Clean up the temporary SSH public key file
                        sh "rm -f ssh_public_key.tmp"
                        sh "rm -f ssh_private_key.tmp"
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