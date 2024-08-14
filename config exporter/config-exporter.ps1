#Imports
. ".\holiday-calendar.ps1"
. ".\can-I-wear-short-trousers-today.ps1"

#Variables
$gitDictectory = ""
$FGRConfigExport = ""
$FGRLanguageAll = ""
$FGRConfigExportPath = Join-Path $gitDictectory $FGRConfigExport
$FGRLanguageAllPath = Join-Path $gitDictectory $FGRLanguageAll
$currentDate = Get-Date -Format "yyyyMMdd"
$currentUserName = $env:USERNAME
$dev1APIkey = ""
$dev2APIkey = ""
$dev3APIkey = ""
$rel1APIkey = ""

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
        "Accept-Encoding" = "application/json"
        "Authorization"   = "Bearer $APIkey"
    }
    Write-Host "Starting download for configuration files on the $environment environment"
    try {
        Invoke-RestMethod -Method "Get" -Uri "" -Headers $headers -OutFile $outFileConfigExport -TimeoutSec 600
        Invoke-RestMethod -Method "Get" -Uri "" -Headers $headers -OutFile $outFileLanguageExport -TimeoutSec 600
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

function Get-LatestGitBranch {
    param (
        [string]$remoteName = "origin",
        [string]$branchPattern = "release/"
    )

    # Define the pattern to match branches
    $pattern = "$remoteName/$branchPattern"

    # Get a list of remote branches matching the pattern
    $branches = git branch -r | Where-Object { $_ -like "*$pattern*" }

    if ($branches.Count -eq 0) {
        Write-Output "No branches found matching the pattern." -ForegroundColor Red
        return
    }

    # Extract version numbers from branch names to only take branches with two digits
    # $versions = $branches | ForEach-Object { $_ -replace ".*$pattern(\d+\.\d+\.\d+).*", '$1' }
    # $versions = $branches | ForEach-Object { $_ -replace ".*$pattern(\d+\.\d+).*", '$1' }
    $versions = $branches | ForEach-Object { $_ -replace ".*$pattern(\d+\.\d+)(?!\.\d)", '$1' }

    # Sort the version numbers and get the latest one
    $latestVersion = $versions | Sort-Object -Descending | Select-Object -First 1

    # Construct the name of the latest branch
    $latestBranch = "$branchPattern$latestVersion"

    return $latestBranch
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
# Setup music source: https://eddiejackson.net/wp/?p=9268
Add-Type -AssemblyName presentationCore
$mediaPlayer = New-Object system.windows.media.mediaplayer
$musicPath = Join-Path $PSScriptRoot "brazilian-bossa-nova-jazz.mp3"
$mediaPlayer.open($musicPath)
# Fetch changes from the remote repository
git fetch --all --prune
# Create a branch names
$dailyDevlineExport = "$currentUserName/DailyDevlineLastestConfig$currentDate"
$dailyRellineExport = "$currentUserName/DailyRellineLastestConfig$currentDate"

# start music
$mediaPlayer.Play()

# dev1 export
Start-Export -branch $dailyDevlineExport -remoteBranch "master" -APIkey $dev1APIkey -environment "dev1" -ConfigExportPath $FGRConfigExportPath -ConfigLanguageAllPath $FGRLanguageAllPath -ConfigExport $FGRConfigExport -ConfigLanguage $FGRLanguageAll

# dev3 export
# Start-Export -branch $dailyDevlineExport -remoteBranch "master" -APIkey $dev3APIkey -environment "dev3" -ConfigExportPath $FGRConfigExportPath -ConfigLanguageAllPath $FGRLanguageAllPath -ConfigExport $FGRConfigExport -ConfigLanguage $FGRLanguageAll

# rel1 export
# $latestReleaseBranch = Get-LatestGitBranch
# Start-Export -branch $dailyRellineExport -remoteBranch $latestReleaseBranch -APIkey $rel1APIkey -environment "rel1" -ConfigExportPath $FGRConfigExportPath -ConfigLanguageAllPath $FGRLanguageAllPath -ConfigExport $FGRConfigExport -ConfigLanguage $FGRLanguageAll

#clean up
Write-Host "Clean up local branches $dailyDevlineExport and $dailyRellineExport, if available" -ForegroundColor Blue
git switch master
git branch -D $dailyDevlineExport
git branch -D $dailyRellineExport

$mediaPlayer.Stop()
Write-Host "Export complete! :D" -ForegroundColor Green
$holiday_message = Get-ClosestHoliday -currentDate $currentDate -holidayDates $holiday_dates
Write-Host $holiday_message -ForegroundColor Magenta
Test-Short-Trouser-Day
Read-Host "Press Enter to exit"