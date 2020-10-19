[![License](https://img.shields.io/badge/license-GPL%20v3.0%20or%20later-brightgreen.svg)](https://github.com/chiforbogdan/atlas_android/blob/master/LICENSE)

# ATLAS IoT Security Platform - general information
ATLAS consists in a 3-tier IoT security platform which offers the following modules:
* A lightweight software client which runs on the IoT device ([ATLAS_Client])
* A gateway software which runs on the network edge and manages all the clients from the network ([ATLAS_Gateway])
* A cloud platform which allows managing the gateways and the clients ([ATLAS_Cloud])
* An Android management application which allows IoT command authorization ([ATLAS_Android])

ATLAS provides security management for a fleet of IoT devices and enables a reputation based Sensing-as-a-service platform. It also offers the capability to inspect the IoT device telemetry values and supports the CoAP lightweight protocol for the communication between the IoT device and the gateway.
On the IoT data plane layer, ATLAS provides an API which can be integrated with a user application and offers the following capabilities:
* Install a firewall rule on the gateway side
* Send packet statistics to the gateway and cloud
* Get the device with the most trusted reputation within a category and provide reputation feedback

## ATLAS Android
ATLAS Android is a smartphone application which allows authorization/approval for IoT client commands. This application allows adding the owner paradigm to the ATLAS system: the administrator of the IoT network which is a separate entity from the cloud administrator. Thus, the cloud administrator may send commands to the IoT client device, which need approval from thw owner.

#### Authors
ATLAS Client was developed by:
* Bogdan-Cosmin Chifor
* Mirabela Medvei

ATLAS project is sponsored by [UEFISCDI].

----

#### License
GNU General Public License v3.0 or later.

See LICENSE file to read the full text.

[ATLAS_Client]: https://github.com/chiforbogdan/atlas_client
[ATLAS_Gateway]: https://github.com/chiforbogdan/atlas_gateway
[ATLAS_Cloud]: https://github.com/chiforbogdan/atlas_cloud
[ATLAS_Android]: https://github.com/chiforbogdan/atlas_android
[UEFISCDI]: https://uefiscdi.gov.ro/