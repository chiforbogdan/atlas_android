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
ATLAS Android is a smartphone application which allows authorization/approval for IoT client commands.
This application allows adding the owner paradigm to the ATLAS system: the administrator of the IoT network which is a separate entity from the cloud administrator. Thus, the cloud administrator may send commands to the IoT client device, which need approval from the owner.

The first steps in using ATLAS Android is to claim a gateway device, which consists in sharing a secret key between the Android application and the gateway. After the gateway is claimed, it will reject any further claim attempts.

If the cloud application issues a command to the IoT client, ATLAS Android will be notified using a Firebase and it will fetch all pending commands from the cloud.
The approved/rejected commands are cryptographically signed (HMAC) using the secret key shared with the gateway at claim time. If approved, the command will be transmitted to the gateway which cryptographically validates the command and then transmits the command to the IoT client device.

#### How to build it
ATLAS Android can be build using Android Studio. Before building the application, the ATLAS_CLOUD_BASE_URL and ATLAS_CLOUD_PORT parameters have to be configured in the build.gradle file in order to point to the cloud web application:
```
buildConfigField("String", "ATLAS_CLOUD_BASE_URL", "\"https://192.168.0.20\"")
buildConfigField("Integer", "ATLAS_CLOUD_PORT", "8888")
```

#### How to use it
After installing the ATLAS Android application, the owner has to claim a gateway, using the following steps:
* Start the gateway process and search the last claim code in the gateway log file (e.g. tail -f atlas_gateway.log | grep -i claim).
* In the Claim tab of ATLAS Android application enter the gateway IP address or hostname, the claim code obtained at the previous step and a gateway alias (friendly name).
* After the gateway is claimed, the new information will appear in the Commands tab of the application.
* If the cloud administrator issues a command which needs owner approval, ATLAS Android will receive a notification and it will fetch all pending commands from the cloud side.
* The owner has to use the Commands tab and press either Approve or Reject for each pending command.

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