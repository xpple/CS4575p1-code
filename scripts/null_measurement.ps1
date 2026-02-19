$IsAdmin = ([Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)

if (-not $IsAdmin) {
    $ScriptPath = $MyInvocation.MyCommand.Definition
    $Arguments = @("-NoProfile", "-ExecutionPolicy", "Bypass", "-File", "`"$ScriptPath`"")

    if ($args.Count -gt 0) {
        $Arguments += $args
    }

    Start-Process powershell -ArgumentList $Arguments -Verb RunAs
    exit
}

# Actual script

$iterations = 30

for ($i = 1; $i -le $iterations; $i++) {
    energibridge -o results_$i.csv --summary -- timeout /t 10 /nobreak
}
