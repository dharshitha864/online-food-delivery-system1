pipeline {
    agent any

    tools {
        // Maven and JDK tool identifiers as configured in Jenkins Global Tool Configuration
        maven 'M3'
        jdk   'Java-17'
    }

    options {
        timeout(time: 15, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                echo '=== Stage 1: Checkout Source Code ==='
                checkout scm
            }
        }

        stage('Compile') {
            steps {
                echo '=== Stage 2: Compiling Spring Boot Application ==='
                bat 'mvn compile'
            }
        }

        stage('Unit & Integration Tests') {
            steps {
                echo '=== Stage 3: Running JUnit 5 Test Suite ==='
                bat 'mvn clean test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo '=== Stage 4: Packaging Executable Spring Boot JAR ==='
                bat 'mvn package -DskipTests'
            }
        }

        stage('Archive Artifacts') {
            steps {
                echo '=== Stage 5: Archiving Build Artifacts ==='
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true, allowEmptyArchive: false
            }
        }
    }

    post {
        success {
            echo '========================================================'
            echo ' BUILD SUCCESS: QuickBite Food Delivery CI Completed! '
            echo '========================================================'
        }
        failure {
            echo '========================================================'
            echo ' BUILD FAILED: Check test or compilation errors above. '
            echo '========================================================'
        }
    }
}
