# Comparing the Energy Consumption of Single-Threaded and Multi-Threaded Programs

Code for CS4575 Sustainable Software Engineering Project 1.

## How to build

To measure the energy consumption, first build the program:

```shell
./gradlew clean build
```

The JAR will be in the `build/libs` folder.

## How to measure

Ensure you have built `energibridge` and have added its directory to PATH. To run the various programs, see the available parameters:

| Program                 | Arguments                    |
|-------------------------|------------------------------|
| `SINGLE_THREADED`       | `<input size>`               |
| `MULTI_THREADED`        | `<input size> <num threads>` |
| `MULTI_THREADED_CACHED` | `<input size> <num threads>` |

Then, run the following:

```shell
scripts/program_measurement.ps1 <program> <arguments>
```

By default, each iteration includes a pause before and after the run to allow for a cold start. You can adjust the pause (capped at 30 seconds) with `MEASUREMENT_PAUSE_SECONDS`.

```shell
$env:MEASUREMENT_PAUSE_SECONDS=15
scripts/program_measurement.ps1 SINGLE_THREADED 100
```

For example:

```shell
scripts/program_measurement.ps1 SINGLE_THREADED 100
```
