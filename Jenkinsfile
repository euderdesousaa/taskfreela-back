pipeline {
    agent any
    tools {
        git 'Default'
        dockerTool "docker"
    }
    stages {
        stage('Install Docker Compose') {
            steps {
                script {
                    // Baixa a versão mais recente do Docker Compose
                    sh 'sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose'
                    // Adiciona permissão de execução
                    sh 'sudo chmod +x /usr/local/bin/docker-compose'
                }
            }
        }

        stage('Removing old containers') {
            steps {
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
