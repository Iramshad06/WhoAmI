Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "      Starting WhoAmI Server        " -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

Write-Host "Step 1: Compiling EmbeddedServer..." -ForegroundColor Yellow
javac -encoding UTF-8 -cp "lib\*" EmbeddedServer.java
if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful`n" -ForegroundColor Green
} else {
    Write-Host "Compilation failed" -ForegroundColor Red
    exit 1
}

Write-Host "Step 2: Initializing Database..." -ForegroundColor Yellow
javac -encoding UTF-8 -cp "lib\*;src\main\webapp\WEB-INF\lib\*" InitializeDatabase.java
java -cp "lib\*;src\main\webapp\WEB-INF\lib\*;." InitializeDatabase 2>$null
Write-Host "Database ready`n" -ForegroundColor Green

Write-Host "Step 3: Starting Server..." -ForegroundColor Yellow
Write-Host "========================================`n" -ForegroundColor Cyan
java -cp "lib\*;src\main\webapp\WEB-INF\lib\*;src\main\webapp\WEB-INF\classes;." EmbeddedServer
