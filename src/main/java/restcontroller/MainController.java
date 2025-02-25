/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import ConstantVariable.VariableSession;
import ConstantVariable.Constant;
import Service.Codenvy;
import Service.CreateWebdriver;
import Service.DowloadService;
import Utils.ProxyWithSSH;
import Utils.Utils;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    CreateWebdriver createWebdriver;
    @Autowired
    Codenvy codenvy;
    @Autowired
    Utils utils;
    @Autowired
    TaskController taskController;
    @Autowired
    DowloadService dowloadService;
    @Autowired
    ProxyWithSSH proxyWithSSH;
    public static WebDriver webDriver = null;
    public static boolean isDone = false;
    public static String verifyCode = "";
    public static Actions myAction1;

    @RequestMapping(value = "/startAuto", method = RequestMethod.GET, headers = "Connection!=Upgrade")
    public @ResponseBody
    String startAuto() {
        try {
            if (VariableSession.flag_status_is_first_run_app) {
//                Runtime.getRuntime().exec("google-chrome-stable --headless --no-sandbox --disable-gpu --remote-debugging-port=9222");
                webDriver = createWebdriver.getGoogle(Constant.binaryGoogleHeroku);
                VariableSession.flag_status_is_first_run_app = false;
                myAction1 = new Actions(MainController.webDriver);
//                startProxy(proxyWithSSH);
//                webDriver.manage().window().maximize();
                webDriver.manage().window().fullscreen();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return "running";
    }

    @RequestMapping(value = "/resetDriver", method = RequestMethod.GET, headers = "Connection!=Upgrade")
    public @ResponseBody
    String resetDriver() {

        Thread startThread = new Thread() {
            @Override
            public void run() {
                try {
                    webDriver.quit();
                    webDriver = createWebdriver.getGoogle(Constant.binaryGoogleHeroku);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        };
        startThread.start();

        return "running";
    }

    public static void startProxy(ProxyWithSSH proxyWithSSH) {
        try {
            URL Urlssh = MainController.class
                    .getClassLoader().getResource("ssh.txt");
            proxyWithSSH.setting(Urlssh.getPath(), 1080);
            proxyWithSSH.start();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @RequestMapping(value = "/getImg", method = RequestMethod.GET, headers = "Connection!=Upgrade")
    public @ResponseBody
    String getImg(@RequestParam String url) {
        try {

            Thread startThread = new Thread() {
                @Override
                public void run() {
                    try {
                        codenvy.Start(webDriver, url);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            };
            startThread.start();
        } catch (Exception e) {
            e.getMessage();
        }
        return "running";
    }

    @RequestMapping(value = "/KeepGoogleLive", method = RequestMethod.GET, headers = "Connection!=Upgrade")
    public @ResponseBody
    String KeepGoogleLive(
            @RequestParam String user,
            @RequestParam String pass,
            @RequestParam String phone) {
        try {
            Thread startThread = new Thread() {
                @Override
                public void run() {
                    try {
                        codenvy.loginGoogle(webDriver, user, pass, phone);
                        codenvy.KeepGoogleLive(webDriver);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            };
            startThread.start();
        } catch (Exception e) {
            e.getMessage();
        }
        return "running";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
            driver.get("https://www.google.com");
            System.out.println("Tiêu đề trang: " + driver.getTitle());
            driver.quit();
        } catch (MalformedURLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "index";
    }

    @RequestMapping(value = "/setVerifyCode", method = RequestMethod.GET, headers = "Connection!=Upgrade")
    public String setVerifyCode(
            @RequestParam String code) {
        verifyCode = code;
        isDone = true;
        return "index";
    }

    @RequestMapping(value = "/startSpam", method = RequestMethod.GET, headers = "Connection!=Upgrade")
    public void startSpam() {
        TaskController.isStart = true;
    }

    @RequestMapping(value = "/stopSpam", method = RequestMethod.GET, headers = "Connection!=Upgrade")
    public void stopSpam() {
        TaskController.isStart = false;
    }

    @RequestMapping(value = "/toado", method = RequestMethod.GET)
    public String toado(
            @RequestParam(value = "x", required = true) int x,
            @RequestParam(value = "y", required = true) int y) {
        try {
            Thread startThread = new Thread() {
                @Override
                public void run() {
                    try {
                        myAction1 = new Actions(webDriver);
                        myAction1.moveByOffset(x, y).click().build().perform();
                        myAction1.moveByOffset(-x, -y).build().perform();
                        taskController.getScreenShot(dowloadService.dowloadImgTypeBase64(webDriver));
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            };
            startThread.start();

        } catch (Exception e) {
            return "loi : " + e.getMessage();
        }
        return "";
    }

    @RequestMapping(value = "/sendKey", method = RequestMethod.GET)
    public String sendKey(
            @RequestParam(value = "s", required = true) String s) {
        try {
            Thread startThread = new Thread() {
                @Override
                public void run() {
                    try {
                        myAction1 = new Actions(webDriver);
                        myAction1.sendKeys(s).perform();
                        //myAction1.sendKeys(Keys.ENTER).build().perform();
                        taskController.getScreenShot(dowloadService.dowloadImgTypeBase64(webDriver));
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            };
            startThread.start();

        } catch (Exception e) {
            return "loi : " + e.getMessage();
        }
        return "";
    }

    @RequestMapping(value = "/getWith", method = RequestMethod.GET)
    public int greeding() {
        try {
            return webDriver.manage().window().getSize().width;
        } catch (Exception e) {
            e.getMessage();
            return -1;
        }

    }

    @RequestMapping(value = "/getHigth", method = RequestMethod.GET)
    public int getHigth() {
        try {
            return webDriver.manage().window().getSize().height;
        } catch (Exception e) {
            e.getMessage();
            return -1;
        }

    }

    @RequestMapping(value = "/cmd", method = RequestMethod.GET)
    public String cmd(@RequestParam(value = "cmd", required = true) String cmd) {
        try {
            return executeCommand(cmd);
        } catch (Exception e) {
            e.getMessage();
            return e.getMessage();
        }

    }

    public String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }
}
