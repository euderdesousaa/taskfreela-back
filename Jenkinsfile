pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    // Fazer build dos containers Docker definidos no compose.yml
                    sh 'docker compose build'
                }
            }
        }
         stage('Initialize'){
        def dockerHome = tool 'myDocker'
        env.PATH = "${dockerHome}/bin:${env.PATH}"
    }

        stage('Run Containers') {
            steps {
                script {
                    // Subir os containers definidos no compose.yml
                    sh 'docker compose compose.yml up -d'
                }
            }
        }
    }
     stage('Initialize'){
        def dockerHome = tool 'myDocker'
        env.PATH = "${dockerHome}/bin:${env.PATH}"
    }

    post {
        always {
            script {
                // Parar e remover os containers após o pipeline (opcional)
                sh 'docker compose down'
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
