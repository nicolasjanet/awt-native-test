# Media Native Application

This demonstrates the ability to manipulate media (PDF, Images, Videos) using PDFBox and JavaCV in a native image.

3 artifacts can be run:
* a native executable produced by the Native Image Maven Plugin.
* a Docker image produced by the Spring Boot Maven Plugin.
* a Custom Docker image embedding the native executable and manually built using a Docker file.

Note: To ease tests, both docker images are handled with a using Docker Compose.

This application has three endpoints

* `/pdf` generates and returns a PDF
* `/image` generates also a PDF but converts it into an JPEG image.
* `/video` extracts a frame from a webm video and converts it into a JPEG image.

## Native Executable

First we need to build the native executable.

### Build

> Note: Requires Liberica-NIK-23.1.1-1

```
mvn native:compile -Pnative
```

this compilation produces an executable and several .so files.

### Run

it can be run locally

```
./target/awt-native-test
```

Use `-Dvideo=file:<path-to-webm>` to set the path of the webm video.

### Request

```
curl -X POST http://localhost:8080/pdf --output -
curl -X POST http://localhost:8080/image -o image.jpg
curl -X POST http://localhost:8080/video -o frame.jpg
```

A PDF file is returned.

## Docker

Then, let's try to build docker images.

### Build

> Note: Depending on your environment, remove the PROXY configuration from the Spring Maven Plugin.

Let's begin by building a Docker image using the Spring Boot Maven Plugin. 

```
mvn spring-boot:build-image -Pnative
```

`docker-vialink.vialink.local/awt-native-test` is available.

Then use Docker Compose

```
cd docker
docker compose build
```

### Run

```
docker compose up
```

This starts two containers.

### Request a PDF using the Spring Boot Image

```
curl -X POST http://localhost:8081/pdf --output -
```

It returns an error. Following stacktrace is visible in the logs:

```
java.lang.UnsatisfiedLinkError: No awt in java.library.path
        at org.graalvm.nativeimage.builder/com.oracle.svm.core.jdk.NativeLibrarySupport.loadLibraryRelative(NativeLibrarySupport.java:136) ~[na:na]
        at java.base@21.0.1/java.lang.ClassLoader.loadLibrary(ClassLoader.java:106) ~[com.vialink.awtnativetest.AwtNativeTestApplication:na]
        at java.base@21.0.1/java.lang.Runtime.loadLibrary0(Runtime.java:916) ~[na:na]
        at java.base@21.0.1/java.lang.System.loadLibrary(System.java:2059) ~[na:na]
        at java.desktop@21.0.1/java.awt.image.ColorModel$1.run(ColorModel.java:211) ~[na:na]
        at java.desktop@21.0.1/java.awt.image.ColorModel$1.run(ColorModel.java:209) ~[na:na]
        at java.base@21.0.1/java.security.AccessController.executePrivileged(AccessController.java:129) ~[na:na]
        at java.base@21.0.1/java.security.AccessController.doPrivileged(AccessController.java:319) ~[na:na]
        at java.desktop@21.0.1/java.awt.image.ColorModel.loadLibraries(ColorModel.java:208) ~[com.vialink.awtnativetest.AwtNativeTestApplication:na]
        at java.desktop@21.0.1/java.awt.image.ColorModel.<clinit>(ColorModel.java:221) ~[com.vialink.awtnativetest.AwtNativeTestApplication:na]
        at java.desktop@21.0.1/java.awt.image.Raster.<clinit>(Raster.java:172) ~[com.vialink.awtnativetest.AwtNativeTestApplication:na]
        at org.apache.pdfbox.pdmodel.PDDocument.<clinit>(PDDocument.java:107) ~[com.vialink.awtnativetest.AwtNativeTestApplication:2.0.30]
        at com.vialink.awtnativetest.PDFController.generate(PDFController.java:38) ~[com.vialink.awtnativetest.AwtNativeTestApplication:na]
        ...
```

.so files are missing in the Spring Boot Docker image.

### Request a PDF using the Custom Image

```
curl -X POST http://localhost:8082/pdf --output -
```

It works!

### Request an Image using the Custom Image

```
curl -X POST http://localhost:8082/image -o image.jpg
```

It works

### Request an Image using the Custom Image

```
curl -X POST http://localhost:8082/video -o frame.jpg
```

It works