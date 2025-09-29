package com.lucienvirecourt.barracuda.infrastructure.adapter.logging

import com.lucienvirecourt.barracuda.application.port.out.LoggerPort
import org.slf4j.LoggerFactory

class Slf4jLoggerAdapter(private val className: String) : LoggerPort {
  private val logger = LoggerFactory.getLogger(className);

  override fun info(message: String) {
    logger.info(message)
  }

  override fun warn(message: String) {
    logger.warn(message)
  }

  override fun error(message: String, throwable: Throwable) {
    logger.error(message, throwable)
  }

  override fun debug(message: String) {
    logger.debug(message)
  }
}
