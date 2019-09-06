pipeline {
  agent any
  stages {
    stage('Fetch From GitHub') {
      steps {
        git(url: 'https://github.com/priyankshah217/ContractProducer.git', branch: 'master', changelog: true, poll: true)
      }
    }
    stage('Run Tests') {
      steps {
        sh './mvnw clean test pact:verify'
      }
    }
    stage('Archive Results') {
      steps {
        junit(keepLongStdio: true, allowEmptyResults: true, testResults: '**/target/surefire-reports/TEST-*.xml')
      }
    }
  }
}