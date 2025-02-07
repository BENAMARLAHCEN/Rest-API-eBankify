pipeline {
    agent any
    environment {
        SONAR_TOKEN = "sqa_d9c07f15818cd84a813dc457ff68127423774c67"
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'DevOps', url: 'https://github.com/BENAMARLAHCEN/Rest-API-eBankify.git'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean install -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeDevops') {
                    withCredentials([string(credentialsId: 'sonar-token2', variable: 'SONAR_TOKEN')]) {
                        sh '''
                            ./mvnw sonar:sonar \
                            -Dsonar.projectKey=com.banking:restapiebankify \
                            -Dsonar.host.url=http://host.docker.internal:9000 \
                            -Dsonar.login=$SONAR_TOKEN
                        '''
                    }
                }
            }
        }

        stage('Unit Tests & Coverage') {
            steps {
                sh './mvnw test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    jacoco execPattern: 'target/jacoco.exec', classPattern: 'target/classes', sourcePattern: 'src/main/java'
                }
            }
        }

        stage('Quality Gate Check') {
            steps {
                script {
                    echo "Checking SonarQube Quality Gate..."
                    def qualityGate = sh(
                        script: """
                        curl -s -u "${SONAR_TOKEN}:" \
                        "http://host.docker.internal:9000/api/qualitygates/project_status?projectKey=com.banking:restapiebankify" \
                        | jq -r '.projectStatus.status'
                        """,
                        returnStdout: true
                    ).trim()
                    if (qualityGate != "OK") {
                        error "Quality Gate failed! Stopping the build."
                    } else {
                        echo "Quality Gate passed! Proceeding..."
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t myapp:latest .'
            }
        }

        stage('Stop and Remove Container') {
            steps {
                sh 'docker stop myapp-container || true'
                sh 'docker rm myapp-container || true'
            }
        }

        stage('Run Container') {
            steps {
                sh 'docker run -d -p 8080:8080 --name myapp-container myapp:latest'
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished'
        }
    }
}