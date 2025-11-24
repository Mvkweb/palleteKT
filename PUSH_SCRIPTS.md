# PalleteKT Auto-Push Scripts

## Quick Usage

### Option 1: Double-click (Easiest)
Just double-click `push.bat` in Windows Explorer!

### Option 2: Command Line
```powershell
# From project directory
.\push.bat
```

### Option 3: Custom message
```powershell
.\push.ps1 -Message "PalleteKT v2.0.0 - New features"
```

---

## What It Does

1. ✅ Stages all changes (`git add .`)
2. ✅ Commits everything
3. ✅ Squashes ALL commits into ONE clean commit
4. ✅ Force pushes to GitHub
5. ✅ Your repo always shows only 1 commit!

---

## Automation Options

### Auto-push on Save (VS Code)

Add to `.vscode/tasks.json`:

```json
{
  "version": "2.0.0",
  "tasks": [
    {
      "label": "Auto Push",
      "type": "shell",
      "command": "${workspaceFolder}/push.bat",
      "problemMatcher": []
    }
  ]
}
```

Then press `Ctrl+Shift+P` → `Tasks: Run Task` → `Auto Push`

### Scheduled Auto-push (Every hour)

Create a Windows Task Scheduler task:
1. Open Task Scheduler
2. Create Basic Task
3. Trigger: Daily, repeat every 1 hour
4. Action: `E:\dev\palletekt\push.bat`

### Git Hook (On every commit)

Not recommended for this use case, but possible with `.git/hooks/post-commit`

---

## Notes

- 🔒 **Always 1 commit**: History is squashed every time
- ⚠️ **Force push**: Old commits are unreachable
- 🚀 **Fast**: Takes ~2 seconds
- ✅ **Safe**: Checks for changes first
