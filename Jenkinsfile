pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Clonar o repositório do código
               git url: 'https://github.com/euderdesousaa/taskfreela-back.git', credentialsId: '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' 
            }
        }

        stage('Build') {
            steps {
                script {
                    // Fazer build dos containers Docker definidos no compose.yml
                    sh 'docker compose -f compose.yml build'
                }
            }
        }

        stage('Run Containers') {
            steps {
                script {
                    // Subir os containers definidos no compose.yml
                    sh 'docker compose -f compose.yml up -d'
                }
            }
        }
    }

    post {
        always {
            script {
                // Parar e remover os containers após o pipeline (opcional)
                sh 'docker compose -f compose.yml down'
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
