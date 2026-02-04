def call(String warPath, String ec2Host, String credId) {
    withCredentials([sshUserPrivateKey(credentialsId: credId, keyFileVariable: 'AWS_KEY')]) {
        sh """
        # Clean old deployment
        ssh -i $AWS_KEY -o StrictHostKeyChecking=no ubuntu@${ec2Host} \
            "sudo rm -rf /var/lib/tomcat10/webapps/webapp /var/lib/tomcat10/webapps/webapp.war"

        # Copy new WAR
        scp -i $AWS_KEY -o StrictHostKeyChecking=no ${warPath} ubuntu@${ec2Host}:/tmp/

        # Move WAR into Tomcat and restart
        ssh -i $AWS_KEY -o StrictHostKeyChecking=no ubuntu@${ec2Host} \
            "sudo mv /tmp/*.war /var/lib/tomcat10/webapps/ && sudo systemctl restart tomcat10"
        """
    }
}

