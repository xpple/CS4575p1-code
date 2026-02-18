# Comparing the Energy Consumption of Single-Threaded and Multi-Threaded Programs

Code for CS4575 Sustainable Software Engineering Project 1.

## How to build

```shell
./gradlew clean build
```

The JAR will be in the `build/libs` folder.

## How to measure

Ensure you have built `energibridge` and have added its directory to PATH. Then, run the following in PowerShell:

```shell
Start-Process cmd -ArgumentList "/k cd /d `"$PWD`" && energibridge -o results.csv --summary -- java -jar ./build/libs/cs4575p1-1.0.0.jar 1000000" -Verb RunAs
```
