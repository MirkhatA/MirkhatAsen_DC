package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Processor of HTTP request.
 */
public class Processor implements Runnable {
    private final Socket socket;
    private final HttpRequest request;
    private String requestStr;

    public Processor(Socket socket, HttpRequest request) {
        this.socket = socket;
        this.request = request;
    }

    public void process() throws IOException {
        requestStr = request.getRequestLine().split(" ")[1];

        // Print request that we received.
        System.out.flush();

        // To send response back to the client.
        PrintWriter output = new PrintWriter(socket.getOutputStream());


        // if else statement to check route
        if (requestStr.isEmpty() || requestStr.equals("/")) {
            // if / route, return returnPage method with list of parameters
            // in this case we send "Hello world"
            returnPage(output, List.of("Hello world"));
        } else if (requestStr.equals("/create/itemid")) {
            // same for others requests
            returnPage(output, List.of("create endpoint was called"));
        } else if (requestStr.equals("/delete/itemid")) {
            // here I'm using thread sleep to add some delay between executing
            try {
                Thread.sleep(2500);
                returnPage(output, List.of("delete endpoint was called"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else if (requestStr.equals("/exec/params")) {
            try {
                Thread.sleep(2500);
                returnPage(output, List.of("exec params endpoint was called"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        socket.close();
    }

    // and method that returns simple page with value
    private <E> void returnPage(PrintWriter output, List<E> value) {
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html; charset=utf-8");
        output.println();
        output.println("<html>");
        output.println("<head><title>Hello</title></head>");
        output.printf("<body><p> %s </p></body>", value.get(0));
        output.println("</html>");
        output.flush();
    }

    @Override
    public void run() {
        try {
            process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}