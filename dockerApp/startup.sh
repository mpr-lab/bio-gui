#!/bin/bash

# Kill all running Python processes (safely)
pkill -f python || true

# Navigate to the directory (expand wildcard)
cd ~/MotheterRemote/ssh 2>/dev/null || {
  echo "Directory ~/MotheterRemote/ssh not found"
  exit 1
}

# Attempt to pull the latest changes
git pull || echo "Git pull failed (no internet?)"

# Run setup script
python3 first_time_setup.py

cd ~/MotheterRemote/scripts 2>/dev/null || {
  echo "Directory ~/MotheterRemote/scripts not found"
  exit 1
}

# Run runner scripts
./rpi_runner.sh
./sensor_runner.sh