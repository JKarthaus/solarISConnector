#!/bin/sh



echo "Install ->                                                                       "
echo " ######   #######  ##          ###    ########  ########     ###    ########    ###    ##        #######   ######    ######   ######## ########   "                                 
echo "##    ## ##     ## ##         ## ##   ##     ## ##     ##   ## ##      ##      ## ##   ##       ##     ## ##    ##  ##    ##  ##       ##     ##  "                               
echo "##       ##     ## ##        ##   ##  ##     ## ##     ##  ##   ##     ##     ##   ##  ##       ##     ## ##        ##        ##       ##     ##  " 
echo " ######  ##     ## ##       ##     ## ########  ##     ## ##     ##    ##    ##     ## ##       ##     ## ##   #### ##   #### ######   ########   "
echo "      ## ##     ## ##       ######### ##   ##   ##     ## #########    ##    ######### ##       ##     ## ##    ##  ##    ##  ##       ##   ##    "
echo "##    ## ##     ## ##       ##     ## ##    ##  ##     ## ##     ##    ##    ##     ## ##       ##     ## ##    ##  ##    ##  ##       ##    ##   "
echo " ######   #######  ######## ##     ## ##     ## ########  ##     ##    ##    ##     ## ########  #######   ######    ######   ######## ##     ##  "
echo "1. check that you habe gradle installed "
echo "2. check Ip Adresse in File: Ansible/hosts "
echo "3. check remote_user setting in File Ansible/ansible.cfg"
echo "4. check the config in Ansible/roles/install/templates/solarISConnector.cfg  "
echo " "
read -p "Press any key to continue... " bla

./mvn -Dmaven.test.skip=true compile org.apache.felix:maven-bundle-plugin:bundle

cd Ansible

ansible-playbook solarISConnector.yml
