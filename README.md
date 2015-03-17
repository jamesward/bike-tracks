Building Multi-Sensory Apps that Connect IoT to Business Processes
==================================================================

The Internet of Things opens a new world of experiences and deep connectivity for both consumers and businesses.  Always-on sensor streams, big data, and machine learning are dramatically changing the kinds of experiences software developers are building.  These changes require those of us who build software to think differently about what software is and how it is composed.  Software is no longer simple database-centric, CRUD interfaces.  [Multi-Sensory Applications](http://TODO) are systems which connect all the things: IoT, business processes, data analytics, and immerse user experiences.

Before we dive into a very simple MSA that connects IoT data to a back-office business process, go read about the [Principals of Multi-Sensory Applications](http://TODO) so that you understand the basic ideas.

Ok, now lets walk through an MSA that uses location sensor data and wraps a business process around that data.  We will start with the basics and add additional senses and transducers in future blog posts.  This example focuses on a rental bike business that needs to address a problem with bikes periodically going missing.  Here is the business process:

 * A customer rents a bike and GPS tracking is enabled
 * Throughout the rental, the bike transmits it current location to the cloud
 * If no information about the bike's location has been received for 30 minutes, a support case including the last known location of the bike is created
 * An employee can then go investigate the disappearance

*** App Overview ***

*** Video ***

*** Architecture & Code ***

