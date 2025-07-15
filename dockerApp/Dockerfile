# Base image for Raspberry Pi OS
FROM debian:latest

# Metadata
LABEL description="MPR-Bio-Remote Docker image for Raspberry Pi"

# Update and install system packages
RUN apt-get update && apt-get install -y \
    curl \
    gnupg2 \
    software-properties-common \
    lsb-release \
    git \
    openssh-client \
    build-essential \
    python3 \
    python3-pip

# Optional: ensure "python" points to python3
RUN ln -sf /usr/bin/python3 /usr/bin/python

# --- Install Tailscale ---
RUN curl -fsSL https://pkgs.tailscale.com/stable/debian/bullseye.noarmor.gpg | gpg --dearmor -o /usr/share/keyrings/tailscale-archive-keyring.gpg && \
    curl -fsSL https://pkgs.tailscale.com/stable/debian/bullseye.tailscale-keyring.list \
    | tee /etc/apt/sources.list.d/tailscale.list && \
    apt-get update && apt-get install -y tailscale


# Clone the GitHub repo containing the Python script
# (Replace with your actual repo URL)
RUN git clone https://github.com/mpr-lab/MotheterRemote.git /app


# Start Tailscale in the background and run your Python script
# NOTE: Running Tailscale inside Docker requires --cap-add=NET_ADMIN and --device=/dev/net/tun at runtime
# CMD tailscaled & sleep 3 && tailscale up --authkey=${TAILSCALE_AUTHKEY} && python main.py


# Copy your startup script into the image
COPY startup.sh /app/startup.sh
RUN chmod +x /app/startup.sh

# Set the working directory
WORKDIR /app

# Set the container's entrypoint
CMD ["/app/startup.sh"]
