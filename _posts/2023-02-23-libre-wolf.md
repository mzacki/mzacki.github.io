---
layout: single
title: "LibreWolf review"
date:   2023-02-23 17:29
last_modified_at: 2023-03-20 18:37
description: Private Firefox-based browser for Linux and macOS.

categories:

- security, Linux, networking

tags:

- security, Linux, privacy, networking, LibreWolf

---

### What is LibreWolf?

LibreWolf is [open-source (repo available)](https://gitlab.com/librewolf-community), Firefox-based lightweight browser for Linux (and macOS). The key feature is extensive privacy enabled by default.

### License & Privacy Policy

LibreWolf is not affiliated to Mozilla (the creators of the Firefox web browser) - this information has been stressed at LibreWolf website.
Nevertheless, the project has been based on Mozilla Firefox, and the source code is licensed under [Mozilla Public License 2.0](https://librewolf.net/license-disclaimers/).
According to [LibreWolf Privacy Policy](https://librewolf.net/license-disclaimers/), LibreWolf does not collect any user data nor conducts telemetry on purpose.

> One of the goals of LibreWolf is to remove the data collection and telemetry from Firefox,
> and thus we don't collect any data from the user in the LibreWolf browser or on the LibreWolf website.

However, the creators of LibreWolf cannot guarantee that no data is sent to Mozilla or third parties, and for more information we are redirected to [Firefox Privacy Notice](https://www.mozilla.org/en-US/privacy/firefox/).

> We can't always assure that no data is sent from the browser to Mozilla or other third parties,
> but we try our best to achieve that. For that case, also check out the Firefox Privacy Notice.

Well, nobody's perfect.


### Installation for Ubuntu

Installation may vary depending on the OS, [installation manual](https://librewolf.net/installation/) at Libre Wolf website comes in handy.
Ubuntu is based on Debian, so the following commands are required in this case:

```shell
sudo apt update && sudo apt install -y wget gnupg lsb-release apt-transport-https ca-certificates

distro=$(if echo " una vanessa focal jammy bullseye vera uma" | grep -q " $(lsb_release -sc) "; then echo $(lsb_release -sc); else echo focal; fi)

wget -O- https://deb.librewolf.net/keyring.gpg | sudo gpg --dearmor -o /usr/share/keyrings/librewolf.gpg

sudo tee /etc/apt/sources.list.d/librewolf.sources << EOF > /dev/null
Types: deb
URIs: https://deb.librewolf.net
Suites: $distro
Components: main
Architectures: amd64
Signed-By: /usr/share/keyrings/librewolf.gpg
EOF

sudo apt update

sudo apt install librewolf -y
```

That's it, hit the ```Super``` key and search for LibreWolf already installed.

### First impression

Simple, Zen-like design, similar to Firefox in private mode. I did not touch anything to see what are default settings.
GUI theme set to ```System theme - auto``` by default (as an add-on). Since it inherits theme settings from the system, I am wondering whether it allows fingerprinting to some extent?

DuckDuckGo is default search engine. Here, the theme can also be set (manually). Along with LibreWolf dark mode, setting DDG to dark makes them both 
to look really nice in a calm, eyes-friendly manner. But how it affects privacy protection?

Regarding DuckDuckGo, it is considered privacy-friendly (at least it used to be). As of today, it is certainly better than mainstream search engines. For sure, it facilitates our escape from the most popular search engine fiter bubble...
But when it comes to privacy and security details, I would recommend to dig a bit more into this topic.

The only one extension installed and enabled in LibreWolf is uBlock Origin. Good thing, we do not have to do it manually.

There is no Pocket - Mozilla's app for saving websites in the cloud. Fine, one issue less.

### Privacy and security settings

Unlike the browser theme, auto-changing websites' appearance to dark mode is blocked by default:

> This feature is disabled because ResistFingerprinting is enabled. 
> This means LibreWolf will force web content to display in a light theme.

LibreWolf discourages disabling or even altering ResistFingerprinting (RFP) mechanism, although [RFP might be uncomfortable for many reasons](https://librewolf.net/docs/faq/#what-are-the-most-common-downsides-of-rfp-resist-fingerprinting).

At first glance, overall security level looks decently. **[Enhanced Tracking Protection](https://librewolf.net/docs/faq/#what-is-enhanced-tracking-protection)**
is in Strict mode.

> LibreWolf supports - and it enables by default - Enhanced Tracking Protection in Strict mode. 
> This is one of the most important settings in the browser, as it provides state partitioning, strict blocking lists and other neat privacy features. 
> We do not recommend changing to other modes.


On the other hand, minor issues appear. Spell checker is enabled (see general settings). I do not like when something follows when I write (it's irritating). Its security is questionable, even if the spell checker had no Internet connection (but who knows?).
No information about how it works in [the docs](https://librewolf.net/docs/faq/) - one more reason to disable it.   

Network settings allow to set up proxy connection, enable ```Enable DNS over HTTPS``` and so on.

```Do Not Track``` option is not set to ```always```, enabled only conditionally, so it should be changed manually.

```Delete cookies and site data when LibreWolf is closed``` option is check-marked. Very good, the only problem is, it did not work. LibreWolf stored my previous session history. Tabs are still open...
I am very sensitive to such behaviour. It looks like a serious bug. When we scroll down, there is an explanation - contradictory **History settings**.

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/librewolf_settings.png"  alt="LibreWolf settings" title="LibreWolf settings">
</div>
<br>

So ```Delete cookies and site data when LibreWolf is closed``` is enabled, but ```Remember browsing and download history``` is also enabled at the same time.
To add even more trouble, detailed settings related to the **History** storage are to be additionally modified in the pop-up. And **Address Bar suggestions** might be configured as well - and they are **enabled by default**.
Users should fix this mess themselves. Not good.

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/librewolf_settings2.png"  alt="LibreWolf settings" title="LibreWolf settings">
</div>
<br>

So much for simple, plug and play, very private from the very start, light-weight browser. **Everything must be carefully checked - and fixed - as usual...**

But there are bright sides as well. **Forms and Autofill** are disabled, **password and login saving** disabled, good for them this time. Media **autoplay** is fortunately blocked from the start. 
Very kind, given that it is one of the most annoying so-called *achievments of technology and web marketing*, that fights like cat and dog with my Asperger's syndrome.
**But all other Permissions need to be blocked manually, including microphone and camera**. Seriously??? What is the problem in defaulting it?

At least pop-ups are blocked, and forceful add-ons installing, too.

Even more settings are available in browser's settings tabs. [Advanced configuration](https://librewolf.net/docs/settings/) using config file is also possible. 
See [full feature list](https://librewolf.net/docs/features/) for complete description of security implementations. And, last but not least, LibreWolf is not recommended for Tor.

### Privacy features

These are privacy features listed at LibreWolf webiste, but as explained earlier, not everything is enabled from the stratch.
A careful user should thouroughly check it before rely on it in practice.

- [x] Delete cookies and website data on close.
- [x] Include only privacy respecting search engines like DuckDuckGo and Searx.
- [x] Include uBlockOrigin with custom default filter lists, and Tracking Protection in strict mode, to block trackers and ads.
- [x] Strip tracking elements from URLs, both natively and through uBO.
- [x] Enable dFPI, also known as Total Cookie Protection.
- [x] Enable RFP which is part of the Tor Uplift project. RFP is considered the best in class anti-fingerprinting solution, and its goal is to make users look the same and cover as much metrics as possible, in an effort to block fingerprinting techniques.
- [x] Always display user language as en-US to websites, in order to protect the language used in the browser and in the OS.
- [x] Disable WebGL, as it is a strong fingerprinting vector.
- [x] Prevent access to the location services of the OS, and use Mozilla's location API instead of Google's API.
- [x] Limit ICE candidates generation to a single interface when sharing video or audio during a videoconference.
- [x] Force DNS and WebRTC inside the proxy, when one is being used.
- [x] Trim cross-origin referrers, so that they don't include the full URI.
- [x] Disable search and form history.
- [x] Disable form autofill.
- [x] Disable link prefetching and speculative connections.
- [x] Disable disk cache and clear temporary files on close.
- [x] Use CRL as the default certificate revocation mechanism, as it is faster and privacy-oriented. For security and usability reasons, the browser might fall back to OCSP in some instances: when that happens, OCSP will be stapled to preserve privacy.

### Security-related features

And in this case as well - check it.

- [x] Stay up to date with upstream Firefox releases, in order to timely apply security patches.

Meaning: it stays OR YOU STAY? Is it up to the user to update and patch? Not clear... Maybe it's just a misspelling or a typo.

- [x] Enable HTTPS-only mode.

Same thing here, is it a possibility or default mode? Such possiblity exists in other browsers, so...

- [x] Enable stricter negotiation rules for TLS/SSL.
- [x] Always force user interaction when deciding the download location of a file.
- [x] Disable scripting in the built-in pdf reader.
- [x] Protect against IDN homograph attack. 
- [x] Implement optional extension firewall, which can be enabled manually.
- [x] Revert user-triggered TLS downgrades at the end of each session.
- [x] Set OCSP to hard-fail in case a certain CA cannot be reached.

And so on, and the like. It looks like al features would require extensive testing in order to be confirmed as default or optional - or at least manual testing that takes time... 

### Some other features

Among some other features, the authors listed:

- [x] Prevent window resizing from scripts.
- [x] Disable autoplay of media.
- [x] Disable search suggestions and ads in the urlbar.
- [x] Remove all the distracting and sponsored content from the home page.
- [x] Remove the Pocket extension at compile time.
- [x] Remove Mozilla VPN ads.
- [x] Disable Firefox Sync, unless explicitly enabled by the user.
- [x] Disable extension recommendations.
- [x] Easy and Docker-based build process, so that everyone can build from source in few steps and without local dependencies.
- [x] LibreWolf specific UI that exposes the most important privacy and security settings, to allow you to easily control them.
- [x] Completely disable telemetry, including crash report, normandy, studies and personalized recommendations.
- [x] No data collection of any kind. In fact, as stated in our privacy policy, we wouldn't even have the infrastructure to do that, making it impossible from a technical standpoint.
- [x] Disable Google Safe Browsing, over censorship concerns, and in an effort to prevent Google from controlling another aspect of the internet. This would also make a developer key a requirement to build from source, which is something we are not comfortable with.
- [x] Disable DRM, as it is a limitation to user freedom.
- [x] Avoid making unnecessary changes that increase the fingerprint without giving any privacy gain.
- [x] Only allow outgoing connections that are not privacy invading.
- [x] Disable built-in password manager and suggest more robust options.

The whole list looks really impressive. But the devil is in the details. Anyway, it is an interesting project, even if it requires expert knowledge to do some fine-tuning
for a good start. And even if we need a lot of time get through it to check for potential bugs and issues. This is what open-source & community projects are for, right?

### Summary

I tested LibreWolf 110.0-2 as of February 23, 2023. I consider it as a promissing project, despite some drawbacks (that I assume are easily fixable).
It was advertised in [Linux Magazine](https://www.linux-magazine.com/Issues/2023/266/LibreWolf/(language)/eng-US) as hardened browser with pre-defined security and privacy set up.
And mostly it is, but it requires further work and careful configuration to be done by the user, to keep the privacy and security on some decent level. 

Otherwise, all this hard work put in LibreWolf by the creators
might be lost due to simple security issues, like not blocking a camera by default, and the like. 

I would not expect from an average user to pay attention to all these settings that need to be set.
On the contrary: I would like to have pre-set browser for everyday use, with high level of privacy and security as a default (but with options to loosen the policy for advanced users).

| PROS                                                                          | CONS |
|-------------------------------------------------------------------------------|--|
| open-source                                                                 | still under development |
| more secure than other browsers                                               | not fully independent of Mozilla and third parties |
| possibly more private that other browsers                                     | still requires manual configration |
| light-weight, good-looking                                                   | clashing privacy options |
| security features configurable in details                                              | not for beginners, not as simple as it wants to be|
| good documentation, a lot of fantastic stuff to learn networking and security | requires further study to fully understand how it works |

Conclusion: good work (for a start), but do not just advertise it as a more private and secure browser. Just make it like that, with no compromise. It is worth it.

Anyway, [LibreWolf FAQ answers to plethora of questions, including security and privacy concerns](https://librewolf.net/docs/faq/).
That makes it a great resource for learning.

P.S. Greetings to the Wolves team (not related to LibreWolf community), where I had first opportunity to learn Linux environment. 
Everything that has wolfish features (like wolfish name) reminds me of the team.

P.S.2 Update as of March 20, 2023:

LibreWolf lists all its subpages when ```about:about``` is being put as a URL address:

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/librewolf_about.png"  alt="LibreWolf about" title="LibreWolf about">
</div>
<br>
