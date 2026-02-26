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

Varying thread amounts will lead to different running times. To make sure the analysis will be as effective as possible you should also make null measurements of your devices energy consumption when it is doing nothing. To do this you can run the following:
```shell
scripts/null_measurement.ps1
```

## How to analyse data

After running measurements for 1 set of arguments, you will find 30 csv files containing the results in your repository. Create a directory in this repository with a name corresponding to the type of measurement and move all the csv files into it. See required directory names:

| Measurement type            | Directory name      |
|-----------------------------|---------------------|
| null measurement            | `null`              |
| measurement with n threads  | `threads_<n>`       |

So a directory with measurements for an experiment with 4 threads should be called `threads_4`

When you have done this you can run a python script:
```shell
python DataAnalysis.py
```

This will show:
- some plots of the acquired data
    - Average power (W) consumption of the null measurements
    - Total energy (J) consumption of the thread measurements
    - Average power (W) consumption of the thread measurements
- Shapiro p_values for each measurement that show if the data follows a normal distribution. 

If a measurement is not a normal distribution there might have been some issues with your testing set up. 