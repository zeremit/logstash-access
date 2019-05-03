package com.kharevich

import com.kharevich.server.log.AccessLogger
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.{Future, Stopwatch}
import javax.inject.{Inject, Singleton}

@Singleton
class LogbackAccessFilter @Inject()(finagleLog: AccessLogger) extends SimpleFilter[Request, Response] {

  override def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    val elapsed = Stopwatch.start()
    service(request) onSuccess { response =>
      finagleLog.log(request, response, elapsed.apply())
    } onFailure { e =>
      // should never get here since this filter is meant to be after the exception barrier
      finagleLog.log(request, e, elapsed.apply())
    }
  }
}
