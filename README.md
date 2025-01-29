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

Using dependency-heavy tools can often lead to version mismatches or conflicts with the libraries your application
already depends on. With SkProfiler,
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

This will launch the application **MyAwesomeApp** with the SkProfiler agent listening for HTTP requests on port **8080
**.

### Warning

If no port is provided, the SkProfiler agent will not start correctly. Ensure that a valid port number is always
specified when launching your application.

---

## ENDPOINTS

### `/status`

- **Description**: Returns the current status of the **SkProfiler** agent, along with detailed information about the
runtime environment and the Java Virtual Machine in use.
- **Methods**: `GET`
- **Expected Response**:
  - **HTTP Code**: `200 OK` (success)
  - **Content** (JSON format):
    ```json
    {
      "command": <string>,
      "java": {
        "version": <string>,
        "home": <string>
      },
      "virtualMachine": {
        "name": <string>,
        "version": <string>,
        "vendor": <string>,
        "uptime": <string>
      }
    }
    ```
    
**Notes**:
- String values are replaced by `"Unknown"` if unavailable.

### `/memory`

- **Description**: Returns detailed information about the memory usage of the Java Virtual Machine, including both heap
and non-heap memory statistics.
- **Methods**: `GET`
- **Expected Response**:
  - **HTTP Code**: `200 OK` (success)
    - **Content** (JSON format):
      ```json
      {
        "heap": {
            "initial": {
                "bytes": <number>,
                "formatted": <string>
            },
            "used": {
                "bytes": <number>,
                "formatted": <string>
            },
            "committed": {
                "bytes": <number>,
                "formatted": <string>
            },
            "max": {
                "bytes": <number>,
                "formatted": <string>
            }
        },
        "nonHeap": {
            "initial": {
                "bytes": <number>,
                "formatted": <string>
            },
            "used": {
                "bytes": <number>,
                "formatted": <string>
            },
            "committed": {
                "bytes": <number>,
                "formatted": <string>
            },
            "max": {
                "bytes": <number>,
                "formatted": <string>
            }
        }
      }
      ```

**Notes**:
- Numeric values like `initial` and `max` are replaced by `-1` if they are missing or unbounded.
- String values, such as the human-readable `formatted` field, are replaced by `"Unknown"` if unavailable.


Here is a summary of the meaning of each field:

- `initial` : The initial amount of memory allocated when the JVM starts
- `used` : The current amount of memory in use
- `committed` : The guaranteed amount of memory available for the JVM
- `max` : The maximum amount of memory that can be allocated

### `/self-destruct`

- **Description**: Stops the **SkProfiler** agent gracefully. This endpoint is used to terminate the agent's operation
- securely after a brief delay.
- **Methods**: `DELETE`
- **Expected Response**:
  - **HTTP Code**: `200 OK` (success)
  - **Content** (JSON format):
    ```json
    {
      "message": <string>
    }
    ```