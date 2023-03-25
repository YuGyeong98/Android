package com.example.client_server

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket

// jvm 위에서 서버 실행하기
// (localhost:8080 || 127.0.0.1:8080 || ip주소:8080)을 입력하면 확인 가능
fun main() {
    Thread {
        val port = 8080
        val server = ServerSocket(port)

        while (true) {
            val socket = server.accept()
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val printer = PrintWriter(socket.getOutputStream())

            var input: String? = "-1"
            while (input != null && input != "") {
                input = reader.readLine()
            }
            println("READ DATA $input")

            printer.println("HTTP/1.1 200 OK")
            printer.println("Content-Type: text/html\r\n")
            printer.println("{\"message\": \"Hello World\"}")
            printer.println("\r\n")
            printer.flush()

            printer.close()
            reader.close()
            socket.close()
        }
    }.start()
}