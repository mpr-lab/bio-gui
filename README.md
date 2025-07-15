# MPR-Bio-Remote (GUI)
This project is a java based graphical user interface (GUI), with an ssh-based python backend. The MPR Lab biological monitoring team works with raspberry pis and sensors to collect and sync data remotely through radio, cellular, or Wi-Fi. Its purpose is to deploy RPi modules to citizen biologists for their own research, however, the MotheterRemote and NFC projects which previously ran from a command line may not be intuitive for such audience. This GUI thus aims to make the technology more accessible for a wider range of people.

## GUI App Installation
### For Windows:
### For Mac:
### For Linux:

---
## Raspberry Pi Setup & App Installation

Follow these steps to set up your Raspberry Pi and run the Docker app.



### 1. Prepare Your Raspberry Pi

#### Download Raspberry Pi Imager
- Go to: [https://www.raspberrypi.com/software](https://www.raspberrypi.com/software)
- Download and install the **Raspberry Pi Imager** for your computer.

#### Flash Raspberry Pi OS
- Insert your microSD card.
- In Raspberry Pi Imager:
    - Choose **Raspberry Pi OS (32-bit)** as the system.
    - Choose your SD card as the storage device.

#### Enable SSH (Remote Access)


This enables remote access (optional if you’re connecting keyboard/mouse/monitor directly).

---

### 2. First-Time Raspberry Pi Setup

#### Plug in your Raspberry Pi
- Insert the SD card into the Pi.
- Plug in power, HDMI (if needed), and internet (Wi-Fi or Ethernet).
- Wait 1–2 minutes for it to boot.

#### (Optional) Connect via SSH
If you enabled SSH:
```bash
ssh pi@<your-pi-ip>
# Default password is: raspberry
```
---
### 3. Install Docker
The custom RPi image was created using docker. You will need to install docker to properly run the MPR-Bio-Remote.
#### Open the terminal on your Pi and run these commands:
```bash
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
```
#### Then add permissions:
```bash
sudo usermod -aG docker pi
newgrp docker
```
#### Make sure to test that it works:
```bash
docker --version
```

---
### 4. Download and run the app
You can install the custom docker app from the [***mpr-lab*** bio-gui github repo](https://github.com/mpr-lab/bio-gui) under the releases tab. After downloading the `rpi-app.tar` file,
#### Move the file to the Pi (using USB, SCP, or download it).
#### Run these commands:
```bash 
docker load < rpi-python-app.tar
```
If you will be using Tailscale, also run this command:
```bash
docker run -it --rm \
  --cap-add=NET_ADMIN \
  --device=/dev/net/tun \
  -e TAILSCALE_AUTHKEY=<insert_tailscale_key> \
  rpi-app
```
Make sure to replace <insert_tailscale_key> with your real Tailscale Auth Key (get one from https://login.tailscale.com/admin/authkeys).

