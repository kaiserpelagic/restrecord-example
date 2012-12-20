package code.snippet

import code.model._
import net.liftweb._
import common._
import http._
import js.JsCmds._
import util._
import Helpers._
import scala.xml._

import dispatch.Promise

object Twitter {

  def search = Search.find(("q", "lift framework"))()

  def render: CssSel = {
    val s: Search = search openOr Search.createRecord
    "li *" #> s.statuses.is.map(t => "@text *" #> Text(t.text.valueBox openOr ""))
  }
}
