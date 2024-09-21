pipeline {
    agent any
    tools {
        git 'Default'
        dockerTool 'docker'
    }

    stages {
        stage('Install Docker Compose') {
            steps {
                script {
                    def composePath = '/var/jenkins_home/docker-compose'
                    sh """
                        curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-\$(uname -s)-\$(uname -m)" -o ${composePath} &&
                        chmod +x ${composePath}
                    """
                }
            }
        }

        stage('Verify') {
            steps {
                script {
                    sh 'echo $PATH'
                    sh 'docker version'
                    sh "${composePath} version"
                }
            }
        }

        stage('Removing old containers') {
            steps {
                sh "${composePath} -f docker-compose.yml down --remove-orphans"
            }
        }

        stage('Start Container') {
            steps {
                sh "${composePath} -f docker-compose.yml up --build -d"
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
