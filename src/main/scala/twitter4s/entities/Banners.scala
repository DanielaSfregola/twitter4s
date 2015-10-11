package twitter4s.entities

case class Banners(sizes: Map[String, Banner] = Map.empty)

case class Banner(h: Int, w: Int, url: String)
