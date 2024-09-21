pipeline {
    agent {
        docker {
            image 'docker/compose-bin'
        }
    }

    stages {
        stage('Verify Tooling') {
            steps {
                sh 'echo $PATH'
                sh 'docker version'
                sh 'docker compose version'
            }
        }

        stage('Test') {
            steps {
                sh './build_docker.sh jenkinstests'
            }
        }

        stage('Removing old containers') {
            steps {
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
