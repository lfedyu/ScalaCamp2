import scala.annotation.tailrec
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Retrier {

  def main(args: Array[String]): Unit = {
    retry[Int](block = attempt => {
      println("block " + (attempt+1))
      attempt+1
    },
      acceptResult = res => res%3 == 0,
      retries = List(0.seconds, 1.seconds, 2.seconds, 3.second))
  }

  @tailrec
  final def retry[A](block: Int => A,
                     acceptResult: A => Boolean,
                     retries: List[FiniteDuration]): A = {
    var attempt = retries.length
    if (attempt <= 0 || acceptResult(block(attempt))) {
      block(attempt)
    }
    else {

      println("There are " + (attempt - 1) + " attempts")
      Thread.sleep(retries.head.toMillis)
      println("pause: " + retries.head)
      //not attempt below
      retry[A](block, acceptResult, retries.tail)
    }
  }

}
