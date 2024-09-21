pipeline {
    agent any
    stages {
           stage('Install Docker Compose') {
                       steps {
                           sh 'sudo apt-get update'
                           sh 'sudo apt-get install -y docker-compose-plugin'
                       }
                   }
            stage('Verify') {
                steps {
                    sh 'docker version'
                    sh 'docker-compose version'  // Certifique-se de usar a forma correta
                }
            }
        stage('Removing old containers'){
            steps{
                sh 'docker compose -f docker-compose.yml down --remove-orphans'
            }
         }

        stage('Verify Tooling') {
            steps {
                sh 'echo $PATH'
                sh 'docker version'
                sh 'docker compose version'
            }
        }

        stage('Start Container') {
            steps {
                sh 'docker compose -f docker-compose.yml up --build -d'
            }
        }
    }
}
