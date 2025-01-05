<p align="center">
  <img src="LogoSK.svg" width="100" alt="logo">
</p>

<h1 align="center">
  SkProfiler
</h1>
<h3 align="center">
  A Lightweight, dependency-free Java HTTP agent for profiling at runtime
</h3>
<h3 align="center">
  Development in progress
</h3>
<h3 align="center">
  Java 23+
</h3>

## Introduction

SkProfiler is a monitoring solution for Java applications that uses an agent to process HTTP requests via a specified 
port at runtime. 

The fact that **no dependencies are required** for the SkProfiler agent offers several benefits, especially for 
developers and teams looking for a lightweight and hassle-free integration. 

Using dependency-heavy tools can often lead to version mismatches or conflicts with the libraries your application already depends on. With SkProfiler, 
this is not an issue, as it operates independently of your application's dependency ecosystem.

---

## How to Start the SkProfiler Agent

To start the SkProfiler agent for your application, use the following command:

```bash
java -javaagent:SkProfiler.jar=<port> -jar YourApplication.jar
```

### Key Parameters:
- **`<port>`**: Specify a valid port number for the SkProfiler HTTP server to process requests.
- **`YourApplication.jar`**: Replace this with the path or name of the main JAR file of your application.

---

## Example Usage

If you want to run your application on port `8080`, use the following command:

```bash
java -javaagent:SkProfiler.jar=8080 -jar MyAwesomeApp.jar
```

This will launch the application **MyAwesomeApp** with the SkProfiler agent listening for HTTP requests on port **8080**.

---

## Warning

If no port is provided, the SkProfiler agent will not start correctly. Ensure that a valid port number is always
specified when launching your application.
