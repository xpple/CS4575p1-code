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

echo "Performing $iterations iterations with args '$args'..."

for ($i = 1; $i -le $iterations; $i++) {
    energibridge -o results_$i.csv --summary -- java -jar ./build/libs/cs4575p1-1.0.0.jar $args
}
