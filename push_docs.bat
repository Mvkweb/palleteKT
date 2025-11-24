@echo off
echo 🚀 Pushing Mintlify Docs...
cd mintlify-docs

:: Check for changes
git status --porcelain > nul
if %errorlevel% neq 0 (
    echo No changes to push.
    goto :end
)

:: Add all changes
git add .

:: Ask for commit message with default
set /p commit_msg="Enter commit message (default: 'Update documentation'): "
if "%commit_msg%"=="" set commit_msg=Update documentation

:: Commit and push
git commit -m "%commit_msg%"
git push

echo ✅ Docs pushed successfully!

:end
cd ..
pause
