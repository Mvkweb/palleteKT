#!/usr/bin/env pwsh
# PalleteKT - Auto Squash & Push Script
# Automatically commits all changes, squashes history, and force pushes

param(
    [string]$Message = "PalleteKT - Smooth gradient text library for Minecraft"
)

Write-Host "[PalleteKT] Auto-Push Script" -ForegroundColor Cyan
Write-Host "=============================" -ForegroundColor Cyan
Write-Host ""

# Check if we're in a git repository
if (-not (Test-Path .git)) {
    Write-Host "[ERROR] Not a git repository!" -ForegroundColor Red
    exit 1
}

# Stage all changes
Write-Host "[1/4] Staging all changes..." -ForegroundColor Yellow
git add .

# Check if there are changes to commit
$status = git status --porcelain
if ([string]::IsNullOrWhiteSpace($status)) {
    Write-Host "[SUCCESS] No changes to commit. Repository is clean!" -ForegroundColor Green
    exit 0
}

# Commit changes
Write-Host "[2/4] Committing changes..." -ForegroundColor Yellow
git commit -m "temp" -q

# Get total number of commits
$commitCount = (git rev-list --count HEAD)

if ($commitCount -gt 1) {
    Write-Host "[3/4] Squashing $commitCount commits into one..." -ForegroundColor Yellow
    
    # Squash all commits into one
    git reset --soft HEAD~$($commitCount - 1) 2>$null
    git commit --amend -m $Message -q
} else {
    Write-Host "[3/4] Amending commit message..." -ForegroundColor Yellow
    git commit --amend -m $Message -q
}

# Force push
Write-Host "[4/4] Force pushing to origin/main..." -ForegroundColor Yellow
$pushOutput = git push origin main --force 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "[SUCCESS] Pushed! Repository now has 1 clean commit." -ForegroundColor Green
    Write-Host "https://github.com/Mvkweb/palleteKT" -ForegroundColor Cyan
} else {
    Write-Host ""
    Write-Host "[ERROR] Push failed:" -ForegroundColor Red
    Write-Host $pushOutput -ForegroundColor Red
    exit 1
}
