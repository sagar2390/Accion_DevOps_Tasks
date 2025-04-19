pipeline {
    environment {
        WORKING_FOLDER ="Accion_DevOps_Tasks"
        ARTIFACTORY_REPOSITORY_NAME = "<Repo_url>"
        SERVICE_ACCOUNT_NAME        = "<SA_Name>"
        REPO_FOLDER = "nginx"
        IMAGE_BUILD_NUMBER ="${env.ENVIRONMENT == 'dev' ? 0 : env.BUILD_NUMBER}"
        KUBE_CONFIG_CREDENTIALS_ID =""
    }
    agent {
        kubernetes {
            yaml """
                apiVersion: v1
                kind: Pod
                spec:
                    serviceAccountName: ""
                    containers:
                        - name: aquacli
                          image: <aqua_image_url>
                          imagePullPolicy: Always
                          resources:
                            requests:
                              cpu: 500m
                              memory: 512Mi
                            limits:
                              cpu: 950m
                              memory: 1024Mi
                          command:
                            - cat
                          tty: true
                        - name: kaniko
                          image: art.pmideep.com/gcr/kaniko-project/executor:debug
                          resources:
                            requests:
                              cpu: 1000m
                              memory: 10240Mi
                            limits:
                              cpu: 2000m
                              memory: 12288Mi
                          command:
                            - /busybox/cat
                          tty: true
                          volumeMounts:
                            - name: jenkins-docker-cfg
                              mountPath: /kaniko/.docker
                    imagePullSecrets:
                        - name: "<Secret_name>"
                    volumes:
                        - name: jenkins-docker-cfg 
                          projected:
                            sources:
                              - secret:
                                  name: "<Secret_name>"
                                  items:
                                    - key: .dockerconfigjson
                                      path: config.json
            """
        }
    }
    parameters {
        string(name: 'SERVICE_ACCOUNT_NAME', defaultValue: "", description: 'Service account name')
        string(name: 'IMAGE_PULL_CREDENTIALS', defaultValue: "", description: 'Image pull credentials')
        string(name: 'AQUA_IMAGE_NAME', defaultValue: "", description: 'Aqua Image name')
    }
    stages {
        stage('Checkout Code') {
          steps {
              container('kaniko') {
                git url: 'https://github.com/<name>/nginx-secure-repo.git', branch: 'dev'
           }
        }
    }
        stage('Build docker image') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: env.ARTIFACTORY_TOKEN,
                        usernameVariable: 'ARTIFACTORY_USERNAME',
                        passwordVariable: 'ARTIFACTORY_PASSWORD')
                ]) 
                { container('kaniko')
                    {   
                      script {
                        sh '''
                        set -e
                        set -x
                        echo "Inside the kaniko build container"
                        /kaniko/executor \
                        --dockerfile ${WORKING_FOLDER}/Dockerfile                                            \
                        --context ${WORKSPACE}/${WORKING_FOLDER}                                             \
                        --force                                                                              \
                        --destination=${ARTIFACTORY_REPOSITORY_NAME}/${REPO_FOLDER}:${IMAGE_BUILD_NUMBER} \
                        --destination=${ARTIFACTORY_REPOSITORY_NAME}/${REPO_FOLDER}:latest \
                        --tarPath ${WORKSPACE}/${REPO_FOLDER}.tar
                        echo "after tar layout"
                        ls -lah ${WORKSAPCE}
                        '''
                    }

                  }
                }
            }
        }
         stage("Aqua Image Scan") {   
            steps {
                container('aquacli') {
                    withVault([configuration: configuration, vaultSecrets: secrets]) {
                        sh '''
                        echo "aqua scan started"
                        mkdir ${WORKSPACE}/AquaReport
                        /opt/aquasec/scannercli scan --dockerless --checkonly -H ${AQUA_URL} --token ${TOKEN} --docker-archive ${WORKSPACE}/${REPO_FOLDER}.tar --htmlfile ${WORKSPACE}/AquaReport/report.html --text --textfile ${WORKSPACE}/AquaReport/report.txt ${ARTIFACTORY_REPOSITORY_NAME}/${REPO_FOLDER}:${IMAGE_BUILD_NUMBER}
                        '''
                    }
                }  
                archiveArtifacts(artifacts: "AquaReport/*")
            }                        
        } 
        stage('Deploy to Kubernetes') {
          steps {
            withCredentials([file(credentialsId: "${KUBE_CONFIG_CREDENTIALS_ID}", variable: 'KUBECONFIG_FILE')]) {
            sh '''
            export KUBECONFIG=$KUBECONFIG_FILE
            kubectl apply -f ${WORKING_FOLDER}/nginx-statefulset.yaml
            '''
        }
      }
        } 
    }
}