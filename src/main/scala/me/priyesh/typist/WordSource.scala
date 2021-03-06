package me.priyesh.typist

import scala.concurrent.duration.Duration
import scala.util.Random

object WordSource {
  final def takeFor(duration: Duration): List[String] =
    Random.shuffle(words).take(Math.round(duration.toSeconds / 60.0 * 250).toInt).toList

  val words: Seq[String] = ("help he low was line for before on turn are cause with same as mean differ his move " +
    "they right be boy at old one too have does this tell from sentence or set had three by want hot air but well " +
    "some also what  out home other read were hand all port your large when spell up add use even word land how " +
    "here said must an big each high she such which follow do act their through and just form in much is great why " +
    "time ask if men will change way went about pants light many kind then off them need would house write picture " +
    "like try so us these again her animal long point make public mother static thing world see near him build void " +
    "two self has earth look father more head day stand could own go page come should did country my found sound " +
    "answer no school most grow number study who still over learn know plant water cover than food call sun first " +
    "four people thought may let down keep side eye been never now last find door any between new city work tree " +
    "part cross take since get hard place start made might live story where profile saw after far back sea little " +
    "draw only left round late man run year came while show press every close good night me real give string life " +
    "our few under stop open ten seem simple together several next vowel white toward children war begin lay got " +
    "against walk pattern example slow ease center paper love often person typing mineral seven eight nine " +
    "everything something standard distant monkey paint always money music serve those appear both road mark map " +
    "book science letter rule until govern mile pull wear river cold car notice feet voice care fall second power " +
    "group town carry fine took certain rain fly eat unit room lead friend cry began dark idea machine fish note " +
    "mountain wait north plan once figure base star hear box horse noun cut field sure rest watch correct color " +
    "able face pound wood done main beauty enough drive plain stood girl contain usual front young teach ready week " +
    "above final ever gave red green list though quick feel develop talk sleep play there small we end can put bird " +
    "warm soon free body minute dog strong family special direct mind pose behind leave clear song tail measure " +
    "produce state fact product street black inch short lot numeral nothing class me of very to it think you say " +
    "that course wind stay question wheel happen full complete force ship blue area object half decide rock surface " +
    "order deep fire moon south island problem foot piece yet told busy knew test pass record farm boat top common " +
    "whole gold king possible size plane heard age best dry hour wonder better laugh true thousand during ago " +
    "hundred ran check remember game step shape early yes hold hot west miss ground brought interest heat reach " +
    "snow fast bed five bring sing sit listen perhaps six banana apple bottom jeans boots with the fur fill table " +
    "east travel weight less language morning among speed screen").split(" ")
}
