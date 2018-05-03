# solarISConnector
read Solar Inverter Data from MQTT broker and Push to InitialState Bucket

## Requirements for using the Software

>>>
solarInitialStateConnector read solar Data from a MQTT topic.
So be Sure that you have a topic and your data from your Solar Inverter is pushed
to the Topic.
To get more Information how to get Solar Inverter Data to a MQTT Broker take a look at my

http://https://github.com/JKarthaus/smaDataLogger 
>>>

## Clone the repository



## Do some config

* edit */solarISConnector/Ansible/hosts*
* edit the app config */solarISConnector/Ansible/templates/solarISConnector.cfg*

## Build an install the solarInitialStateConnector

For build and deployment you need the following Tools on your host System

* maven
* ansible

On your Target System you need an installed Apache Karaf Server.
For the Ansible deployment you need a passwordless SSH to your Target.
