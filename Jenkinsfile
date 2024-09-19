pipeline {
    agent any

    stages {
        stage('Verify Tooling') {
            steps {
                script {
                    // Verificar as versões das ferramentas
                    sh '''
                        docker version
                        docker info
                        docker compose version
                        curl --version
                        jq --version
                    '''
                }
            }
        }
        stage('Prune Docker Data') {
            steps {
                script {
                    // Remover dados não utilizados do Docker
                    sh 'docker system prune -a --volumes -f'
                }
            }
        }
        stage('Start Containers') {
            steps {
                script {
                    // Subir os containers e verificar se estão rodando
                    sh '''
                        docker compose up -d --no-color
                        docker compose ps
                    '''
                }
            }
        }
    }

    post {
        always {
            script {
                // Parar e remover os containers após o pipeline (opcional)
                sh '''
                    docker compose down
                '''
            }
        }
        success {
            echo 'Pipeline executado com sucesso!'
        }
        failure {
            echo 'O pipeline falhou!'
        }
    }
}
