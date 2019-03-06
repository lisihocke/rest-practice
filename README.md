# REST Practice Project

## #CodeConfident
This repository is the second chosen challenge on [my journey of becoming #CodeConfident](https://www.lisihocke.com/p/codeconfident.html).

## Scope
The scope for this practice project is to implement a simple RESTful service and use RestAssured to implement a handful of basic tests via the API.

## How to run the service and the tests

### Get the code

Git:

    git clone https://github.com/lisihocke/rest-practice.git
    cd rest-practice

Or simply [download a zip](https://github.com/lisihocke/rest-practice/archive/master.zip) file.

### Run the service

Open a command window and run:

    gradlew bootRun

### Run the tests

Open a command window and run:

    gradlew test

### View the test reports

The command provided above will produce a Serenity test report in the `target/site/serenity` directory. Go take a look by opening the `index.html`!