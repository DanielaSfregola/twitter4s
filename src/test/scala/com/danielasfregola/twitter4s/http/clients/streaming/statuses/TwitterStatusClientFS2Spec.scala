// package com.danielasfregola.twitter4s.http.clients.streaming.statuses

// import akka.http.scaladsl.model.HttpMethods
// import com.danielasfregola.twitter4s.helpers.ClientSpec
// import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage

// import scala.concurrent.ExecutionContext.Implicits.global

// import cats.effect._
// import fs2.Sink
// import fs2.async.mutable.Queue

// class TwitterStatusClientFS2Spec extends ClientSpec {

//   class TwitterStatusClientSpecContext extends StreamingClientSpecContext with TwitterStatusClient

//   "Twitter Status Streaming Client with FS2" should {

//     /*
//      This test requires a set of Tweets.  Perhaps Twitter's response to the statuses/sample.json
//      endpoint can be mocked.

//      */

//     "enqueue sample statuses into an FS2 Sink" in new TwitterStatusClientSpecContext {
//       val result: IO[Unit] = Queue.circularBuffer(64)(IO.ioEffect, global).flatMap { (queue: Queue[IO, StreamingMessage]) => 
//         val sink: Sink[IO, StreamingMessage] = queue.enqueue

//         IO {
//           // when(sampleStatuses()(dummyProcessing))
          
//           when(FS2.sampleStatusesStream()(sink))
//             .expectRequest { request =>
//               request.method === HttpMethods.GET
//               request.uri.endpoint === "https://stream.twitter.com/1.1/statuses/sample.json"
//               request.uri.queryString() === Some("filter_level=none&stall_warnings=false")
//             }
//             .respondWithOk
//             .await
//         }
//       }

//       result.unsafeRunSync().isInstanceOf[Unit] should beTrue
//     }
//   }
// }
