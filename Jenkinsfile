pipeline {
    agent any

    environment {
        // Define the Docker image name
        IMAGE_NAME = "mawhaze/ansible"
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
                    // Use withCredentials to securely handle SSH keys
                    withCredentials([
                        sshUserPrivateKey(credentialsId: 'ansible_private_ssh_key', keyFileVariable: 'SSH_PRIVATE_KEY'),
                        string(credentialsId: 'ansible_public_ssh_key', variable: 'SSH_PUBLIC_KEY')
                    ]) {
                        // Debug: Show the command that will be executed without actual sensitive data
                        echo "Running docker build command with SSH keys..."

                        // Ensure the command is executed in a single shell context
                        sh """
                        echo "Using SSH_PUBLIC_KEY from \$SSH_PUBLIC_KEY"
                        echo \$SSH_PUBLIC_KEY > ssh_public_key.tmp
                        docker build --build-arg SSH_PRIVATE_KEY="\$(cat \$SSH_PRIVATE_KEY)" --build-arg SSH_PUBLIC_KEY="\$(cat ssh_public_key.tmp)" -t ${IMAGE_NAME}:latest .
                        rm ssh_public_key.tmp
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
                        string(credentialsId: 'dockerhub_username')
                        string(credentialsId: 'dockerhub_password')
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