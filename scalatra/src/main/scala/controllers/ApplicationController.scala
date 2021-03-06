package controllers

import org.scalatra.ScalatraKernel
import org.scalatra.scalate.ScalateSupport

trait ApplicationController extends ScalatraKernel with ScalateSupport
  with ScalatraDatabaseSupport with RichMapSupport
{
}


trait RichMapSupport {
  case class Converter[T](f: String => T)

  case class RichMap(map: Map[String, String]) {
    def getAs[T](key: String)(implicit converter: Converter[T]) = try {
      map.get(key).map(converter.f)
    } catch {
      case e => None
    }
  }

  implicit def paramsToRichMap(map: Map[String, String])  = RichMap(map)

  implicit val stringToString = Converter(s => s)
  implicit val stringToInt = Converter(_.toInt)
  implicit val stringToLong = Converter(_.toLong)
  implicit val stringToDouble = Converter(_.toDouble)
}
