pipeline {
    agent any
         tools {
         git 'Default'
          dockerTool "docker"
        }
    stages {
        stage('Install Docker Compose') {
            steps {
                sh 'curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /var/jenkins_home'
                sh 'chmod +x /var/jenkins_home/workspace/pepeline taskfreela-backend'
                sh 'export PATH=$PATH:/var/jenkins_home/workspace/pepeline taskfreela-backend'
                sh 'pwd'
            }
        }

       stage('Verify') {
            steps {
                sh 'echo $PATH'
                sh 'docker version'
                sh 'docker compose version'

            }
        }
        stage('Removing old containers'){
            steps{
                sh 'docker compose -f docker-compose.yml down --remove-orphans'
            }
         }

        stage('Start Container') {
            steps {
                sh 'docker compose -f docker-compose.yml up --build -d'
            }
        }
    }
}
