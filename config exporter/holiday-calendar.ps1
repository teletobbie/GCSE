$easter = @"
(\(\  
( -.-)
o_(")(")
Happy Easter
"@

$chrismas = @"
   /\
  / *\
 / * *\
/_*__*_\
  ||||
Merry Chrismas
"@

$newyears = @"
|_|  .  |) |) \./
| | /-\ |  |   |
' ''   ''  '   '
 . .  _ |  |
 |\| |- \/\/
 ' '  ~
\./  _  .  |)
 |  |- /-\ |\
 '   ~'   '' '  
"@

$valentine = @"
  _  _
 ( \/ )
  .---.   \  /   .-"-. 
 /   6_6   \/   / 4 4 \
 \_  (__\       \_ v _/
 //   \\        //   \\
((     ))      ((     ))
=""===""========""===""=======
|||            |||
|              |
     Valentine's day ❤️
"@

$halloween = @"
       ___)__|_
  .-*'          '*-,
 /      /|   |\     \
;      /_|   |_\     ;
;   |\           /|  ;
;   | ''--...--'' |  ;
 \  ''---.....--''  /
  ''*-.,_______,.-*'    
     Trick Or Treat
"@

function Get-ClosestHoliday {
    param (
        $currentDate
    )
    
    $holidayDates = @{
        "2024-12-25" = $chrismas
        "2024-01-01" = $newyears
        "2024-10-31" = $halloween
        "2024-02-14" = $valentine
        "2024-04-21" = $easter
    }
    $closestHoliday = $null

    foreach ($date in $holidayDates.Keys) {
        # Convert the holiday date to "yyyyMMdd" format for comparison
        $holidayDate = [datetime]::ParseExact($date, "yyyy-MM-dd", $null).ToString("yyyyMMdd")
        
        # If closestDate is null or the current date is closer to the holiday date than the previous closest date
        if ($null -eq $closestDate -or (abs($holidayDate - $currentDate) -lt abs($closestDate - $currentDate))) {
            $closestHoliday = $holidayDates[$date]
        }
    } 
    return $closestHoliday 
}
