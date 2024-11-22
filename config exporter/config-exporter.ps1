#Imports
. ".\holiday-calendar.ps1"
. ".\can-I-wear-short-trousers-today.ps1"

#Variables
$gitDictectory = "C:\Projects\CMS\sources\Fingrid"
$FGRConfigExport = "implementation/fgr/config/functional/FGR-config-export.json"
$FGRLanguageAll = "implementation/fgr/config/functional/FGR-language-all.json"
$FGRConfigExportPath = Join-Path $gitDictectory $FGRConfigExport
$FGRLanguageAllPath = Join-Path $gitDictectory $FGRLanguageAll
$currentDate = Get-Date -Format "yyyyMMdd"
$currentUserName = $env:USERNAME
$dev1APIkey = "f0f78250-4030-4372-99f7-abf5fb5d4fe9"
$dev2APIkey = "a4898a36-d374-463c-9a6d-578a8d145d40"
$dev3APIkey = "ffc886d8-0f31-4b57-a5c2-5dc70bccca25"
$rel1APIkey = "cf3b284f-728c-410b-b642-76002025907d"
$rel2APIkey = "7a1851b9-1474-4890-9696-6a66a80191ed"

$holiday_dates = @{
    "2024-12-25" = $chrismas
    "2024-01-01" = $newyears
    "2024-10-31" = $halloween
    "2024-02-14" = $valentine
    "2024-04-21" = $easter
}

function Invoke-HttpRequest {
    param(
        $APIkey,
        $environment,
        $outFileConfigExport,
        $outFileLanguageExport
    )
    $headers = @{
        "Accept" = "application/json"
        "Authorization"   = "Bearer $APIkey"
    }
    Write-Host "Starting download for configuration files on the $environment environment"
    try {
        Invoke-RestMethod -Method "Get" -Uri "https://cms-fingrid-$environment-composer.azurewebsites.net/_api/FGR/config/download" -Headers $headers -OutFile $outFileConfigExport -TimeoutSec 600
        Invoke-RestMethod -Method "Get" -Uri "https://cms-fingrid-$environment-composer.azurewebsites.net/_api/FGR/config/download-language" -Headers $headers -OutFile $outFileLanguageExport -TimeoutSec 600
        Write-Host "Export files successfully downloaded for the $environment environment" -ForegroundColor Blue
    } catch {
        Write-Host "An error occurred while making the request: $($_.Exception.Message)" -ForegroundColor Red
    }
}

function Test-For-Changes {
    param (
        [string]$FGRConfigExport,
        [string]$FGRLanguageAll
    )
    $changes = git diff --name-only
    $changesArray = $changes -split "\r?\n"
    if ($changesArray.Count -eq 2 -and $changesArray -contains $FGRLanguageAll -and $changesArray -contains $FGRLanguageAll) {
        return $true
    } else {
        return $false
    }
}

function Push-To-Remote {
    param (
        [string]$environment,
        [string]$branch
    )
    git add *.json
    git commit -m "Daily $($environment)Line config export $currentDate"
    git push -u origin $branch
}

function Reset-And-Remove {
    param (
        [string]$remoteBranch,
        [string]$branch
    )

    Write-Host "The correct changes on $branch haven't been found. Resetting the automation, Please re-run the script." -ForegroundColor Red
    # reset the current $branch
    git reset --hard
    # switch the remote branch
    git switch $remoteBranch

}

function Confirm-Export {
    param (
        [string[]]$ResourceGroups,
        [string[]]$RemoteBranches
    )

    # Loop until the input is valid
    do {
        # Ensure ResourceGroups and remoteBranch are the same length
        if ($ResourceGroups.Count -ne $RemoteBranches.Count) {
            Write-Host "The number of ResourceGroups and remote branches don't match. Please provide correct input."
            $ResourceGroups = Read-Host "Enter Resource Groups (comma-separated / no spaces)"
            $RemoteBranches = Read-Host "Enter Remote Branches (comma-separated / no spaces)"
            # Split again after user input
            $ResourceGroups = $ResourceGroups -split ","
            $RemoteBranches = $RemoteBranches -split ","
        } else {
            Write-Host ""
            Write-Host ("{0,-30} {1,-30}" -f "Resource Group", "Remote Branch")
            Write-Host ("{0,-30} {1,-30}" -f "--------------", "-------------")

            for ($i = 0; $i -lt $ResourceGroups.Count; $i++) {
                Write-Host ("{0,-30} {1,-30}" -f $ResourceGroups[$i], $RemoteBranches[$i])
            }
            $confirmation = Read-Host "Is this correct? (y/n)"

            if ($confirmation -eq 'y') {
                Write-Host "Proceeding with the export..."
                return
            } else {
                Write-Host "Please enter the correct values."
                $ResourceGroups = Read-Host "Enter Resource Groups (comma-separated / no spaces)"
                $RemoteBranches = Read-Host "Enter Remote Branches (comma-separated / no spaces)"
                $ResourceGroups = $ResourceGroups -split ","
                $RemoteBranches = $RemoteBranches -split ","
            }
        }
    } while ($true)
}

function Start-Export {
    param (
        [string]$branch,
        [string]$remoteBranch,
        [string]$APIkey,
        [string]$environment,
        [string]$ConfigExportPath,
        [string]$ConfigLanguageAllPath,
        [string]$ConfigExport,
        [string]$ConfigLanguage
    )
    # Switch to the remote branch and pull the latest changes
    git switch $remoteBranch
    git pull
    # Create a new branch based on remote branch using the branch name variable
    Write-Host "Create branches for: $branch based on $remoteBranch" -ForegroundColor Blue
    git checkout -b $branch
    # get the config files via the composer API
    Invoke-HttpRequest -APIkey $APIkey -environment $environment -outFileConfigExport $ConfigExportPath -outFileLanguageExport $ConfigLanguageAllPath
    # check if the branch has the correct changes to be pushed to the remote
    if (Test-For-Changes -FGRConfigExport $ConfigExport -FGRLanguageAll $ConfigLanguage) {
        Push-To-Remote -environment $environment -branch $branch
        Write-Host "Successfully pushed the $environment config using $branch to remote" -ForegroundColor Green
    } else {
        Write-Host "Download of $environment config failed using $branch to remote" -ForegroundColor Red
        Reset-And-Remove -remoteBranch $remoteBranch -branch $branch
    }
}

Set-Location -Path $gitDictectory
$preChanges = git diff --name-only
if($preChanges) {
    Write-Host "Found the following changes: $preChanges make sure commit & push your changes first before running the script." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit
}

$ResourceGroups = Read-Host "Enter Resource Groups (comma-separated / no spaces)"
$RemoteBranches = Read-Host "Enter Remote Branches (comma-separated / no spaces)"

$ResourceGroupsList = $ResourceGroups.Split(',') 
$RemoteBranchesList = $RemoteBranches.Split(',')

# confirm and validate export user input
Confirm-Export -ResourceGroups $ResourceGroupsList -RemoteBranches $RemoteBranchesList

# Fetch changes from the remote repository
git fetch --all --prune

for ($i = 0; $i -lt $ResourceGroupsList.Count; $i++) {
    $resourceGroup = $ResourceGroupsList[$i].Trim()
    $remoteBranch = $RemoteBranchesList[$i].Trim()
    $environment = $resourceGroup.Split('-')[2]
    $apiKey = switch ($environment) {
        "dev1" { $dev1APIkey }
        "dev2" { $dev2APIkey }
        "dev3" { $dev3APIkey }
        "rel1" { $rel1APIkey }
        "rel2" { $rel2APIkey }
        default { "Invalid input" }
    }
    # Create a export branch based on remote branch and start export
    $dailyExport = "$currentUserName/Daily" + $environment + "LineLastestConfig" + $currentDate
    Start-Export -branch $dailyExport -remoteBranch $remoteBranch -APIkey $apiKey -environment $environment -ConfigExportPath $FGRConfigExportPath -ConfigLanguageAllPath $FGRLanguageAllPath -ConfigExport $FGRConfigExport -ConfigLanguage $FGRLanguageAll
    # After export clean the exportBranch
    Write-Host "Clean up local branches $dailyExport, if available" -ForegroundColor Blue
    git switch master
    git branch -D $dailyExport
}

Write-Host "Export complete! :D" -ForegroundColor Green
$holiday_message = Get-ClosestHoliday -currentDate $currentDate -holidayDates $holiday_dates
Write-Host $holiday_message -ForegroundColor Magenta
Test-Short-Trouser-Day
Read-Host "Press Enter to exit"
