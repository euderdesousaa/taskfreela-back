pipeline {
    agent any

    stages {
        stage('Removing old containers'){
            steps{
                sh 'docker-compose -f docker-compose.yml down --remove-orphans -v'
            }
         }
        stage('Verify Tooling') {
            steps {
                sh 'echo $PATH'
                sh 'docker version'
                sh 'docker-compose version'
            }
        }

        stage('Start Container') {
            steps {
                sh 'docker-compose -f docker-compose.yml up --build -d'
            }
        }
    }
}
