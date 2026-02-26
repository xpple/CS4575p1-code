$IsAdmin = ([Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)

if (-not $IsAdmin) {
    $ScriptPath = $MyInvocation.MyCommand.Definition
    $Arguments = @("-NoExit", "-NoProfile", "-ExecutionPolicy", "Bypass", "-File", "`"$ScriptPath`"", "-OriginalDir", "`"$PWD`"")

    if ($args.Count -gt 0) {
        $Arguments += $args
    }

    Start-Process powershell -ArgumentList $Arguments -Verb RunAs
    exit
}

if ($args[0] -eq "-OriginalDir") {
    Set-Location $args[1]
    # Remove arguments
    $args = $args[2..($args.Count - 1)]
} else {
    echo "Expected '-OriginalDir' parameter"
    exit
}

# Actual script

$iterations = 30

$pauseSeconds = 15
if ($env:MEASUREMENT_PAUSE_SECONDS) {
    $parsed = 0
    if ([int]::TryParse($env:MEASUREMENT_PAUSE_SECONDS, [ref]$parsed)) {
        $pauseSeconds = $parsed
    }
}
if ($pauseSeconds -gt 30) { $pauseSeconds = 30 }
if ($pauseSeconds -lt 0) { $pauseSeconds = 0 }

if ($pauseSeconds -gt 0) {
    echo "Using $pauseSeconds second pause before and after each run."
}

for ($i = 1; $i -le $iterations; $i++) {
    if ($pauseSeconds -gt 0) { Start-Sleep -Seconds $pauseSeconds }
    energibridge -o results_$i.csv --summary -- timeout /t 10 /nobreak
    if ($pauseSeconds -gt 0) { Start-Sleep -Seconds $pauseSeconds }
}
