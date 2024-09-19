pipeline {
  agent any
  stages {
    stage("verify tooling") {
      steps {
        sh '''
          docker version
          docker info
          docker compose version 
          curl --version
          jq --version
        '''
      }
    }
    stage('Prune Docker data') {
      steps {
        sh 'docker system prune -a --volumes -f'
      }
    }
    stage('Start container') {
      steps {
        sh 'docker compose -f docker-compose.yml up -d --no-color --wait'
        sh 'docker compose ps'
      }
    }
  }
  post {
    always {
      sh 'docker compose -f docker-compose.yml down --remove-orphans -v'
    }
  }
}
