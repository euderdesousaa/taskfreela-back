pipeline {
    agent any
    stages {
         stage('Install Docker Compose') {
                    steps {
                        sh 'curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-Linux-x86_64" -o /usr/local/bin/docker-compose'
                        sh 'chmod +x /usr/local/bin/docker-compose'
                    }
                }

                stage('Verify Tooling') {
                    steps {
                        sh 'docker version'
                        sh 'docker-compose version'  // Certifique-se de usar a forma correta
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
