#!/bin/sh
Xvfb :99 -screen 0 1024x768x16 &

which google-chrome

google-chrome-stable --version

google-chrome-stable  --no-sandbox --disable-dev-shm-usage --disable-gpu --no-first-run --disable-fre --no-default-browser-check --remote-debugging-port=9222 &


#unzip /UserData.zip -d /target
#cp -r "/target/User Data" /target/UserData

#ls -l /target/UserData


echo "Current user: $(whoami) (UID: $(id -u))"

# Các lệnh yêu cầu quyền root
#touch /root/testfile && echo "Created file in /root" || echo "Failed to create file"
##mkdir -p /etc/test && echo "Created directory in /etc" || echo "Failed to create directory"
#chmod 700 /etc/test && echo "Changed permissions" || echo "Failed to change permissions"

echo "All commands executed with root privileges"

# Kiểm tra chạy google-chrome-stable trực tiếp
#echo "Attempting to start google-chrome-stable..."
#google-chrome-stable --headless --no-sandbox --disable-dev-shm-usage --disable-gpu --remote-debugging-port=9222 &

#echo "sleep chrome..."
#sleep 10

#netstat -tuln | grep 9222 || echo "Warning: Selenium Server not listening on 9222 yet"


# Kiểm tra chạy google-chrome-stable trực tiếp
#echo "Attempting to start google-chrome-stable..."
#google-chrome-stable --headless --no-sandbox --disable-dev-shm-usage --disable-gpu --remote-debugging-port=9222 &

#echo "sleep chrome..."
#sleep 10

#netstat -tuln | grep 9222 || echo "Warning: Selenium Server not listening on 9222 yet"

# Start the Java application 
exec java -Dfile.encoding=UTF-8 -jar /target/dependency/webapp-runner.jar --port 7860 /target/*.war
