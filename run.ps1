$FX = ".\lib\javafx-sdk-26\lib"

Write-Host "Compiling..." -ForegroundColor Cyan

$files = Get-ChildItem -Recurse -Filter "*.java" -Path "src" `
         | Select-Object -ExpandProperty FullName

javac --module-path $FX `
      --add-modules javafx.controls,javafx.graphics,javafx.base `
      -cp "src" `
      -d bin `
      $files

if ($LASTEXITCODE -eq 0) {
    Write-Host "Launching..." -ForegroundColor Green
    java --module-path $FX `
         --add-modules javafx.controls,javafx.graphics,javafx.base `
         -cp bin `
         gui.MainApp
} else {
    Write-Host "Compilation failed." -ForegroundColor Red
}