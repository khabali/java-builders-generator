## Java Builders Generator (Eclipse Plugin)  [![Build Status](https://travis-ci.org/khabali/java-builders-generator.svg?branch=master)](https://travis-ci.org/khabali/java-builders-generator) 

Eclipse plugin to generate builders using class fields.

You can specify some fields to make them mandatory in the generated builder.
## installation instructions
Drag the install button to your eclipse.

[![Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client](https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.png)](http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=3494661 "Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client")

*Or*
1) Download the latest updatesite-**version**.zip archive from the ![release section](https://github.com/khabali/java-builders-generator/releases "release section")
2) From Eclipse go to _Help_ > _Install New Software..._ Click _Add_ Button then select the archive that you have just downloaded

## What you will get?
This plugin will add a simple unique command to the _Source_ context menu that will let you generate Builders for your Java classes easly.

Asuming that you have a simple User java class as below
```java
public class User {

	private String firstName;
	private String lastName;
	private int age;

}
  
```
You can just request builder generation as shown in the screenshot below after installing the plugin and you will get the class builder generated.
After the builder code generation, you will be able to instanciate your class as :
```java
User me =  User.builder().firstName("Anas")
                         .lastName("KHABALI")
                         .age(27)
                         .build();
```
Asuming that in the user class we want to make the _lastName_ attribute mandatory. 

So just select it in the list of the attributes that will show after clicking _Generate Builders using Fields_ and you will get a builder that force the _lastName_ attribute to be set to build an instance.
I let you discover that.
```java
// this will not compile, and you have to set the mandatory lastName attribute
User me =  User.builder().firstName("Anas")
                         .age(27)
                         .build();
```

I let you discover that!

![screen shot](https://github.com/khabali/java-builders-generator/blob/master/screenshots/java-builders-generator-menu.png)

## Contributions
Please before [contributing](CONTRIBUTING.md) to this project have a look at our [Contributor Covenant Code of Conduct](code_of_conduct.md).

Thanks for reading :)
