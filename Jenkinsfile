pipeline {
    agent any

    environment {
        // Define DockerHub credentials
        DOCKERHUB_USERNAME = credentials('dockerhub_username')
        DOCKERHUB_PASSWORD = credentials('dockerhub_password')
        // Define the Docker image name
        IMAGE_NAME = "mawhaze/ansible"
        // Add sa-ansible keys
        SSH_PRIVATE_KEY = credentials('ansible_private_ssh_key')
        SSH_PUBLIC_KEY = credentials('ansible_public_ssh_key')

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
                    // Build the Docker image
                    sh "docker build -t ${IMAGE_NAME}:latest ."
                }
            }
        }

        stage('Docker Login') {
            steps {
                script {
                    // Login to DockerHub
                    sh('echo $DOCKERHUB_PASSWORD | docker login --username $DOCKERHUB_USERNAME --password-stdin')
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Push the Docker image to DockerHub
                    sh "docker push ${IMAGE_NAME}:latest"
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