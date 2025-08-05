package com.lucienvirecourt.prime.application.port.out

interface LoggerPort {
  fun info(message: String)
  fun warn(message: String)
  fun error(message: String, throwable: Throwable)
  fun debug(message: String)
}
