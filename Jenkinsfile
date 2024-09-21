pipeline {
    agent any
         tools {
         git 'Default'
          dockerTool "docker"
        }
    stages {
        stage('Install Docker Compose') {
            steps {
                sh 'curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /var/jenkins_home/docker-compose'
                sh 'chmod +x /var/jenkins_home/docker-compose'
                sh 'export PATH=$PATH:/var/jenkins_home'
            }
        }

       stage('Verify') {
            steps {
                sh 'echo $PATH'
                sh 'docker version'
                sh '/var/jenkins_home/docker-compose version'

            }
        }
        stage('Removing old containers'){
            steps{
                sh '/var/jenkins_home/docker-compose -f docker-compose.yml down --remove-orphans'
            }
         }

        stage('Start Container') {
            steps {
                sh '/var/jenkins_home/docker-compose -f docker-compose.yml up --build -d'
            }
        }
    }
}
