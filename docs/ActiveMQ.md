# ActiveMQ

## Installation 

Install ActiveMQ broker as "root" to be run as "activemq":

```
useradd -m activemq -d /srv/activemq
cd /srv/activemq
tar zxvf apache-activemq-<version>-bin.tar.gz
ln -snf apache-activemq-<version> current
chown -R activemq:users apache-activemq-<version>
```

Create a global default configuration and edit the configuration:

```
cp apache-activemq-<version>/bin/env /etc/default/activemq
sed -i '~s/^ACTIVEMQ_USER=""/ACTIVEMQ_USER="activemq"/' /etc/default/activemq
vim /etc/default/activemq
chmod 644 /etc/default/activemq
```

> Consider to move the folders "data", "tmp" and "conf" out of the installation path

Install the `init.d` script:

```
ln -snf  /srv/activemq/current/bin/activemq /etc/init.d/activemq
```

Activate the init script at system startup:

```
# RHEL
chkconfig --add activemq
chkconfig activemq on

# Debian/Ubuntu
update-rc.d activemq defaults
```

Manage the service:

```
service activemq start|stop|status
```

## Set-up a Key- and Trust-Store

### PaNeRo Platform:

```
keytool -genkey -alias broker -keyalg RSA -keysize 1024 -validity 365 -keystore broker.ks
keytool -export -alias broker -keystore broker.ks -file broker.crt
keytool -import -file broker.crt -alias broker -keystore broker.ts
```

Convert keystore to PKCS#12:

```
keytool -importkeystore -srckeystore broker.ks -destkeystore broker.p12 -srcalias broker -srcstoretype jks -deststoretype pkcs12
```

Convert PKCS#12 to PEM:

```
openssl pkcs12 -in broker.p12 -out broker.pem
```

Export public key from PEM file:

```
openssl rsa -in broker.pem -pubout > broker.pub
```


### PaNeRo Gateway (clients):

```
keytool -genkey -alias client -keyalg RSA -keysize 1024 -validity 365 -keystore client.ks
keytool -import -file broker.crt -alias broker -keystore client.ts
```
