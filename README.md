# APIServer Daemon
[![Travis](http://img.shields.io/travis/FutureGateway/geAPIServer/master.png)](https://travis-ci.org/FutureGateway/geAPIServer)
[![License](https://img.shields.io/github/license/FutureGateway/geAPIServer.svg?style?flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)

RESTful API Server compliant with [CSGF APIs][specs] specifications.

This service is the FutureGateway component that phisically execute tasks on top of distributed infrastructures. Its most important features are:
 - It exploits the [CSGF][CSGF]' GridnCloudEngine system to target distributed ifnrastructures with JSAGA
 - Is provides the TOSCA executor interface to address the INDIGO-dc Tosca orhcestrator
 - It can support other executor services just providing the correct interface class
 - This daemon communicates with the API Server   [front-end][fgAPIServer]
 - The API server front-end manages incoming REST calls and then instructs the proper executor interface accordingly to the target infrastructure

   [specs]: <http://docs.csgfapis.apiary.io/#reference/v1.0/application/create-a-task>
   [CSGF]: <https://www.catania-science-gateways.it>
   [fgAPIServer]: <https://github.com/FutureGateway/fgAPIServer>
