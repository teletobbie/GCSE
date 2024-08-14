# source: https://eddiejackson.net/wp/?p=9268
function Start-Music {

    Add-Type -AssemblyName presentationCore
    $mediaPlayer = New-Object system.windows.media.mediaplayer
    $musicPath = Join-Path $PSScriptRoot "brazilian-bossa-nova-jazz.mp3"
    $mediaPlayer.open($musicPath)
    $mediaPlayer.Play()
    $mediaPlayer.Stop()
    $mediaPlayer.

    # Add-Type -AssemblyName presentationCore

    # $mediaPlayer = New-Object system.windows.media.mediaplayer

    # # Path to your MP3 file. Ensure the path is correct.
    # $mp3Path = Join-Path $PSScriptRoot "brazilian-bossa-nova-jazz.mp3"

    # # Check if the file exists
    # if (-Not (Test-Path $mp3Path)) {
    #     Write-Host "File not found: $mp3Path"
    #     return
    # }

    # $mediaPlayer.open([uri]::new($mp3Path))

    # # Event handler for when media is opened
    # $mediaPlayer.add_MediaOpened({
    #     $mediaPlayer.Play()
    #     Write-Host "Playing music..."
    # })

    # # Event handler for when media fails to open
    # $mediaPlayer.add_MediaFailed({
    #     param($sender, $e)
    #     Write-Host "Failed to open media: $($e.ErrorException.Message)"
    # })
}

Start-Music
    
  
# Create a playlist of files from folder
# Preview each song for 30 seconds
# Add-Type -AssemblyName presentationCore
  
# $mediaPlayer = New-Object system.windows.media.mediaplayer
# $musicPath = "C:\music\"
# $mediaPlayer.open([uri]"$($file.fullname)")
 
# $musicFiles = Get-ChildItem -path $musicPath -include *.mp3,*.wma -recurse
 
# foreach($file in $musicFiles)
# {
#  "Playing $($file.BaseName)"
#   $mediaPlayer.open([uri]"$($file.fullname)")
#   $mediaPlayer.Play()
#   Start-Sleep -Seconds 30
#   $mediaPlayer.Stop()
# } 