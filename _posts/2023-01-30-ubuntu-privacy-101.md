---
layout: single
title:  "Ubuntu Privacy 101"
date:   2023-01-30 19:57
description: Linux is more private OS. Learn basic setup that increases privacy even more.

categories:

- Linux, security

tags:

- Linux, security, privacy

---

### Why Linux is nice and Ubuntu is adequate?

Because it is more private than other operating systems. Yes, I know, independence and openness of Ubuntu is disputable - 
it belongs to Canonical. I heard that if one wishes real freedom and hardcore privacy, one shall go for professional Debian or paranoid Qubes OS. 

But still:

> "Everybody has to start somewhere. You have your whole future ahead of you. Perfection doesn't happen right away."
> 
> Haruki Murakami, *Blind Willow, Sleeping Woman (めくらやなぎと眠る女, Mekurayanagi to nemuru onna)*

Ubuntu is good for a start.

### Basic privacy for Ubuntu

First of all, before it is installed, let's verify if downloaded version has not been intercepted or tampered with. 
[Official tutorial](https://ubuntu.com/tutorials/how-to-verify-ubuntu#1-overview) shows how to use **gpg** and **sha256** tools.
Linux Mint ISO image was hacked in this way circa 2016 by putting backdoors into the OS.

Then, although telemetry is minimal comparing to other OS, disable crash reporting and usage statistics:

```shell
sudo apt purge -y apport

sudo apt remove -y popularity-constest

sudo apt autoremove -y
```

Now, go into the GUI and turn off **Notifications** in **Settings**:

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/notifications_off.png"  alt="Turn off Notifications in Settings" title="Notifications">
</div>
<br>

Note BleachBit - usuful for file cleaning and trash removal. Turn off **File history** and so on:

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/history.png"  alt="Turn off File history" title="File history">
</div>
<br>

Be sure **Conectivity** is off, as well as similar features, like **Location**:

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/connectivity.png"  alt="Turn off Connectivity" title="Connectivity">
</div>
<br>

Finally, disable **Diagnostics**:

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/diagnostics.png"  alt="Turn off Diagnostics" title="Diagnostics">
</div>
<br>

### Lock your screen

Do not forget to enable **Screen Lock** (screen saver). Set up a useful keyboard shortcut to lock the screen on demand,
if it is not set. In Focal Fossa, Ubuntu 20.04 it is **SUPER + L**. 

Do not leave your computer unlocked even if there is no one around at the moment!

### Quick turning off

Set keyboard shortcut for quick shutdown. If it is not defaulted, select unused combination of keys, easily accessible when in a rush,
like when leaving the office all of a sudden. **CTRL + ESC** works well - possible to press both with left hand fingers when right hand is busy
with closing the laptop cover or taking off your mug from the desk. 

Then **RIGHT ARROW** twice and **ENTER**. Even if you don't press the keys, your device will be gracefully shut down within 60 seconds.
Enough time to wash your mug, get back from company's kitchen and assemble your things while controlling the shutting down process.
Once it's done, you can leave for home.

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/shutting_down.png"  alt="Shutting down" title="Shutting down">
</div>
<br>

Why it is important? In Ubuntu, shut down action requires three clicks in GUI,
including first in very remote, upper right corner. And next clicks are no more pleasant. 
Here, Windows XP was unrivalled. We want to be sure that the computer has been shut off completely.

Not every office cleaner will be kind enough to do this for us!

Remember:

> "Trust No 1" - *The X-Files*


You don't want to believe - you just want to be sure that your computer is off and secured.


### Which means: set up user / admin password and encrypt the entire disk (using different password)

First thing is obvious. The latter is more complicated, but Ubuntu allows the encryption during installation process.
Correct encryption is possible when Ubuntu is only one OS installed. In case of dual boot computers, it is not so easy.
We will get back to this.


### What about antivirus?

Malware targetting Linux OS is less frequent (fortunately) but it may happen. But even the detection of non-Linux malware can be
a valuable hint for us. ClamAV is antivirus software for Linux. To install:

```shell
sudo apt update

sudo apt install -y clamav clamav-daemon
```
Now stop the service:

```shell
sudo systemctl stop clamav-freshclam
```

Update the database:

```shell
sudo freshclam
```

Restart the service. It downloads virus definition update and should be executed before every scan.

```shell
sudo systemctl start clamav-freshclam
```

Scan without removal (notification only):

```shell
sudo clamscan -r -i /
```

Scan with deletion option - but false-positives may occur:

```shell
sudo clamscan -r -i --remove=yes / 
```

Keep all apps updated all the time:

```shell
sudo apt update

sudo apt upgrade
```
