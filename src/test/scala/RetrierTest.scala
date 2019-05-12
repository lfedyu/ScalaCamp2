import org.scalatest.FunSuite
import scala.concurrent.duration._

class RetrierTest extends FunSuite {

  test("testLessThen") {
    val res = Retrier.retry[Int](block = attempt => {
      println("block " + (attempt+1))
      attempt+1
    },
      acceptResult = res => res<3,
      retries = List(0.seconds, 1.seconds, 2.seconds, 3.second))
    assert(res===2)
  }

  test("testFailed") {
    intercept[Exception]{
      val res = Retrier.retry[Int](block = attempt => {
        println("block " + attempt)
        attempt
      },
        acceptResult = res => res>100,
        retries = List(0.seconds, 1.seconds, 2.seconds, 3.second, 4.second))
    }
  }

}
